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
	protected Selector Selector; // ���� ������.
	protected ServerSocketChannel ServerChannel = null; // ���� ä��.
	protected SocketChannel SocketChannel = null; // ���� ä��.
	public static InetSocketAddress socketAddress; // IP�ּ�.
	private int port = 20000; // ��Ʈ��ȣ.
	private boolean bstart = true; // ���������� ����or����.

	private final int THREAD_CNT = 5; // ������ Ǯ ����
	private ExecutorService threadPool = null; // ������Ǯ

	protected boolean aceptLimit = false; // Accept ����.
	protected ByteBuffer RecvBuf; // Recv ����
	protected SelectionKey RecvKey; // Recv Key��

	public JNetServer() {
		// ��Ʈ����
		socketAddress = new InetSocketAddress(port);
		threadPool = Executors.newFixedThreadPool(THREAD_CNT);
		// ���� Buffer ũ�� ����
		this.RecvBuf = ByteBuffer.allocate(1000);
		// ���� ���������
		Thread thread = new Thread(this);
		thread.start();
	}

	// IP�ּҿ� PORT ��ȣ ������.
	public static String LocalIP() throws UnknownHostException {
		InetAddress inetAddres = InetAddress.getLocalHost();
		return "IP : " + inetAddres.getHostAddress() + " Port : " + socketAddress.getPort();
	}

	// ���������.
	@Override
	public void run() {
		try {
			Selector = java.nio.channels.Selector.open();
			ServerChannel = ServerSocketChannel.open();
			// Non-Blocking ����
			ServerChannel.configureBlocking(false);
			// ��Ʈ��ȣ ����.
			ServerChannel.socket().bind(socketAddress);
			// �޴� ���ۻ����� 1000, ����Ǹ� ��Ʈ����(���ϼ���)
			ServerChannel.setOption(StandardSocketOptions.SO_RCVBUF, 128 * 1000);
			ServerChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
			ServerChannel.register(Selector, SelectionKey.OP_ACCEPT);

		} catch (Exception e) {
			System.out.println("Server Run Error " + e.getMessage());
		}
		// Server Success
		while (bstart) {
			try {
				// ���� ä���� �غ�Ǿ� �ִ��� ������.
				Selector.select();
				Iterator<SelectionKey> Selectkeys = Selector.selectedKeys().iterator();
				// Ŭ���̾�Ʈ�� �۽ſö� Key�� ���¿� ���� Accept���� ����ϰų�,Read�ؼ� �̺�Ʈ ó���ϰų�.
				while (Selectkeys.hasNext()) {
					SelectionKey key = Selectkeys.next();
					Selectkeys.remove();
					switch (key.interestOps()) {
					case SelectionKey.OP_ACCEPT:
						// Accept����.
						Accept(key);
						break;
					case SelectionKey.OP_READ:
						// Read����.
						ReadPacket(key);
						break;
					case SelectionKey.OP_WRITE:
						break;
					default:
						// �̻��� ���� ���ö� ��������
						disConnect(key);
						break;
					}
				}
			} catch (Exception e) {
			}
		}

	}

	// ���� Ŭ���̾�Ʈ�� ���� ���� ����
	protected void Accept(SelectionKey key) {
		// Ŭ���̾�Ʈ ������ 1000���̻� �Ѿ�� ���ް�
		if (Selector.keys().size() > 1000) {
			aceptLimit = true;
		} else if (Selector.keys().size() <= 1000) {
			aceptLimit = false;
		}
		if (!aceptLimit) {
			// ���� ���� SelectionKey�� Accept ����
			ServerChannel = (ServerSocketChannel) key.channel();
			SocketChannel channel = null;
			try {
				// ä�ο� Accept �ϰ�,Non-Blocking, �б� ������ ���·� ���.
				channel = ServerChannel.accept();
				channel.configureBlocking(false);
				channel.register(Selector, SelectionKey.OP_READ);
				JSFrame.ConectCnt();
			} catch (Exception e) {
				System.out.println("Accept Error " + e.getMessage());
			}
		}

	}

	// �����͸� �дºκ�
	protected void ReadPacket(SelectionKey key) {
		// �������� Ű���� ä�η� ��ȯ.
		SocketChannel = (SocketChannel) key.channel();
		this.RecvBuf.clear();
		try {
			// RecvBuf �� �����͸� ������ ������·� ���.
			SocketChannel.read(this.RecvBuf);
			SocketChannel.register(Selector, SelectionKey.OP_WRITE);
			// key���� ���������͸� ������ �̺�Ʈ ó��.
			RegMessage(key, this.RecvBuf, this);
		} catch (Exception ex) {
			disConnect(key);
		}
	}

	// ������
	protected void Send(JNetPacket packet, SelectionKey NetId) {
		SocketChannel = (SocketChannel) NetId.channel();
		try {
			SocketChannel.write(packet.getBuf());
			// �ٽ� ������ �ְԲ�.
			SocketChannel.register(Selector, SelectionKey.OP_READ);
		} catch (Exception ex) {
			// stateMsg("Send Error : " + ex.getMessage() + "\n");
		}
	}

	// ��ü������
	protected void SendAll(JNetPacket packet) {
		// �α����� ��� ���ؼ� ����
		Iterator<String> itr = ServerStart.LoginMap.keySet().iterator();
		while (itr.hasNext()) {
			try {
				// ä�ε� �ޱ�
				SelectionKey NetId = ServerStart.LoginMap.get((String) itr.next());
				SocketChannel = (SocketChannel) NetId.channel();
				SocketChannel.write(packet.getBuf());
				SocketChannel.register(Selector, SelectionKey.OP_READ);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	// ����ȭƯ������ �⺻��������
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

	// �׷쳢��������
	protected void SendGroup(JNetPacket packet, String Sent) {
		// �׷����� ������ ������ �Ͽ���
		Iterator<Long> itr = ServerStart.GroupMap.keySet().iterator();
		while (itr.hasNext()) {
			// ��ũ����Ʈ
			Long key = itr.next();
			LinkedList<String> list = ServerStart.GroupMap.get(key);
			if (list.contains(Sent)) {
				// �������� Ű���� ã��
				for (String GroupName : list) {
					// LoginMap�� ���Ͽ� Ŭ���̾�Ʈ NetId �޾Ƴ���.
					SelectionKey NetId = ServerStart.LoginMap.get(GroupName);
					SocketChannel = (SocketChannel) NetId.channel();
					try {
						// ���̳ʸ��������� ������ �ٽ� �б� ������ ����.
						SocketChannel.write(packet.getBuf());
						SocketChannel.register(Selector, SelectionKey.OP_READ);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		}
	}

	// ��������
	protected void disConnect(SelectionKey NetId) {
		SocketChannel disChannel = (SocketChannel) NetId.channel();
		Iterator<String> itr = ServerStart.LoginMap.keySet().iterator();
		while (itr.hasNext()) {
			// ���������һ���� ã��
			String disUser = itr.next();
			SelectionKey disKey = ServerStart.LoginMap.get(disUser);
			if (disKey.equals(NetId)) {
				itr.remove();
				// ����� ã��
				ServerStart.LoginMap.remove(disUser);
				// �׷�ʵ� ����
				Iterator<Long> disItr = ServerStart.GroupMap.keySet().iterator();
				while (disItr.hasNext()) {
					// ��ũ����Ʈ�ȿ� �ִ� ����� ���� ����.
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
			// ä������.
			disChannel.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// NetId ����.
		NetId.attach(null);
		NetId.cancel();
	}
	
	

}
