import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JNetServer extends ServerStart implements Runnable {
	protected Selector Selector; // 서버 셀렉터.
	protected ServerSocketChannel ServerChannel = null; // 서버 채널.
	protected SocketChannel SocketChannel = null; // 소켓 채널.
	public static InetSocketAddress socketAddress; // IP주소.
	private int port = 20000; // 포트번호.
	private boolean bstart = true; // 서버스레드 진행or멈춤.

	private final int THREAD_CNT = 5; // 쓰레드 풀 갯수
	private ExecutorService threadPool = null; // 쓰레드풀

	protected boolean aceptLimit = false; // Accept 제한.
	protected ByteBuffer RecvBuf; // Recv 버퍼
	protected SelectionKey RecvKey; // Recv Key값

	public JNetServer() {
		// 포트설정
		socketAddress = new InetSocketAddress(port);
		threadPool = Executors.newFixedThreadPool(THREAD_CNT);
		// 받을 Buffer 크기 설정
		this.RecvBuf = ByteBuffer.allocate(1000);
		// 서버 스레드시작
		Thread thread = new Thread(this);
		thread.start();
	}

	// IP주소와 PORT 번호 따오기.
	public static String LocalIP() throws UnknownHostException {
		InetAddress inetAddres = InetAddress.getLocalHost();
		return "IP : " + inetAddres.getHostAddress() + " Port : " + socketAddress.getPort();
	}

	// 스레드시작.
	@Override
	public void run() {
		try {
			Selector = java.nio.channels.Selector.open();
			ServerChannel = ServerSocketChannel.open();
			// Non-Blocking 실행
			ServerChannel.configureBlocking(false);
			// 포트번호 지정.
			ServerChannel.socket().bind(socketAddress);
			// 받는 버퍼사이즈 1000, 종료되면 포트해제(소켓설정)
			ServerChannel.setOption(StandardSocketOptions.SO_RCVBUF, 128 * 1000);
			ServerChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
			ServerChannel.register(Selector, SelectionKey.OP_ACCEPT);

		} catch (Exception e) {
			System.out.println("Server Run Error " + e.getMessage());
		}
		// Server Success
		while (bstart) {
			try {
				// 서버 채널이 준비되어 있는지 선택함.
				Selector.select();
				Iterator<SelectionKey> Selectkeys = Selector.selectedKeys().iterator();
				// 클라이언트가 송신올때 Key의 상태에 의해 Accept으로 등록하거나,Read해서 이벤트 처리하거나.
				while (Selectkeys.hasNext()) {
					SelectionKey key = Selectkeys.next();
					Selectkeys.remove();
					switch (key.interestOps()) {
					case SelectionKey.OP_ACCEPT:
						// Accept상태.
						Accept(key);
						break;
					case SelectionKey.OP_READ:
						// Read상태.
						ReadPacket(key);
						break;
					case SelectionKey.OP_WRITE:
						break;
					default:
						// 이상한 놈이 들어올땐 열결해제
						disConnect(key);
						break;
					}
				}
			} catch (Exception e) {
			}
		}

	}

	// 들어온 클라이언트에 대해 소켓 생성
	protected void Accept(SelectionKey key) {
		// 클라이언트 수제한 1000개이상 넘어가면 못받게
		if (Selector.keys().size() > 1000) {
			aceptLimit = true;
		} else if (Selector.keys().size() <= 1000) {
			aceptLimit = false;
		}
		if (!aceptLimit) {
			// 전달 받은 SelectionKey로 Accept 진행
			ServerChannel = (ServerSocketChannel) key.channel();
			SocketChannel channel = null;
			try {
				// 채널에 Accept 하고,Non-Blocking, 읽기 가능한 상태로 등록.
				channel = ServerChannel.accept();
				channel.configureBlocking(false);
				channel.register(Selector, SelectionKey.OP_READ);
				JSFrame.ConectCnt();
			} catch (Exception e) {
				System.out.println("Accept Error " + e.getMessage());
			}
		}

	}

	// 데이터를 읽는부분
	protected void ReadPacket(SelectionKey key) {
		// 데이터의 키값을 채널로 변환.
		SocketChannel = (SocketChannel) key.channel();
		this.RecvBuf.clear();
		try {
			// RecvBuf 에 데이터를 복사후 쓰기상태로 등록.
			SocketChannel.read(this.RecvBuf);
			SocketChannel.register(Selector, SelectionKey.OP_WRITE);
			// key값과 받은데이터를 가지고 이벤트 처리.
			RegMessage(key, this.RecvBuf, this);
		} catch (Exception ex) {
			disConnect(key);
		}
	}

	// 보내기
	protected void Send(JNetPacket packet, SelectionKey NetId) {
		SocketChannel = (SocketChannel) NetId.channel();
		try {
			SocketChannel.write(packet.getBuf());
			// 다시 읽을수 있게끔.
			SocketChannel.register(Selector, SelectionKey.OP_READ);
		} catch (Exception ex) {
			// stateMsg("Send Error : " + ex.getMessage() + "\n");
		}
	}

	// 전체보내기
	protected void SendAll(JNetPacket packet) {
		// 로그인한 사람 한해서 전송
		Iterator<String> itr = ServerStart.LoginMap.keySet().iterator();
		while (itr.hasNext()) {
			try {
				// 채널들 받기
				SelectionKey NetId = ServerStart.LoginMap.get((String) itr.next());
				SocketChannel = (SocketChannel) NetId.channel();
				SocketChannel.write(packet.getBuf());
				SocketChannel.register(Selector, SelectionKey.OP_READ);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	// 동기화특성땜에 기본값보내기
	protected void RecvSend(SelectionKey NetId) {
		SocketChannel = (SocketChannel) NetId.channel();
		JNetPacket RecvPacket = new JNetPacket();
		RecvPacket.Type(JNetPacket.DEFAULT);
		try {
			SocketChannel.write(RecvPacket.getBuf());
			SocketChannel.register(Selector, SelectionKey.OP_READ);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// 그룹끼리보내기
	protected void SendGroup(JNetPacket packet, String Sent) {
		// 그룹으로 정해진 사람들로 하여금
		Iterator<Long> itr = ServerStart.GroupMap.keySet().iterator();
		while (itr.hasNext()) {
			// 링크리스트
			Long key = itr.next();
			LinkedList<String> list = ServerStart.GroupMap.get(key);
			if (list.contains(Sent)) {
				// 수신자의 키값을 찾기
				for (String GroupName : list) {
					// LoginMap을 통하여 클라이언트 NetId 받아내기.
					SelectionKey NetId = ServerStart.LoginMap.get(GroupName);
					SocketChannel = (SocketChannel) NetId.channel();
					try {
						// 바이너리형식으로 보내고 다시 읽기 가능한 상태.
						SocketChannel.write(packet.getBuf());
						SocketChannel.register(Selector, SelectionKey.OP_READ);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		}
	}

	// 연결해제
	protected void disConnect(SelectionKey NetId) {
		SocketChannel disChannel = (SocketChannel) NetId.channel();
		Iterator<String> itr = ServerStart.LoginMap.keySet().iterator();
		while (itr.hasNext()) {
			// 연결해제할사용자 찾기
			String disUser = itr.next();
			SelectionKey disKey = ServerStart.LoginMap.get(disUser);
			if (disKey.equals(NetId)) {
				itr.remove();
				// 사용자 찾음
				ServerStart.LoginMap.remove(disUser);
				// 그룹맵도 삭제
				Iterator<Long> disItr = ServerStart.GroupMap.keySet().iterator();
				while (disItr.hasNext()) {
					// 링크리스트안에 있는 사용자 값을 삭제.
					Long key = disItr.next();
					LinkedList<String> list = ServerStart.GroupMap.get(key);
					if (list.contains(disUser)) {
						list.remove(disUser);
					}
				}
			}
		}
		JSFrame.DisConectCnt();
		try {
			// 채널종료.
			disChannel.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// NetId 삭제.
		NetId.attach(null);
		NetId.cancel();
	}
	
	

}
