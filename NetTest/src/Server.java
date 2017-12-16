import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.awt.*;
import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


import MsgPacker.MessagePacker;
import MsgPacker.MessageProtocol;

public class Server implements Runnable {

	private Selector selector;
	private ServerSocketChannel socketChannel;
	private HashMap<SocketChannel,String>dataMapper;
	private InetSocketAddress socketAddress;
	private Thread workthread;
	private MessagePacker msg; // 여기서 MessagePacker를 써보자
	private final Server my = this;
	private boolean flag = false;
	
	List<String> room = new ArrayList();
	Button start = new Button("     시작     ");
	Panel bPanel = new Panel();
	TextArea log = new TextArea();

	public Server(String ip,String port) {
		Frame f = new Frame("OmokServer");
		room.add("No.1 다 덤벼 (0/1)");
		socketAddress = new InetSocketAddress(ip,Integer.parseInt(port)); // 소켓 주소 설정
		dataMapper = new HashMap<SocketChannel, String>();
		
		bPanel.setLayout(new FlowLayout());
		bPanel.add(start);
		start.addActionListener(new ActionHandle());
		
		f.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		f.add(bPanel,"North");
		f.add(log,"Center");
		f.setSize(500,800);
		f.setVisible(true);
	}
	class ActionHandle implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			Object o = e.getSource();
			if(o.equals(start))
			{
				if (flag == false) {
					log.append("IP:" + socketAddress.getAddress().getHostAddress() + "\nPort:" + socketAddress.getPort()
							+ "\n서버 시작\n");
					my.run();
					flag = true;
				}
				else {
					log.append("서버가 이미 구동중입니다!\n");
				}
				
			}
		}
		
	}

	@Override
	public void run() { // 서버가 실행되면 호출된다.
		// TODO Auto-generated method stub
		try {
			selector = Selector.open(); // 소켓 셀렉터 열기
			socketChannel = ServerSocketChannel.open(); // 서버소켓채널 열기
			socketChannel.configureBlocking(false); // 블럭킹 모드를 False로 설정한다.

			socketChannel.bind(socketAddress); // 서버 주소로 소켓을 설정한다.
			socketChannel.register(selector, SelectionKey.OP_ACCEPT); // 서버셀렉터를 등록한다.

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		workthread = new Thread(){
			@Override
			public void run() {
				while (true) {
					try {
						if(Thread.interrupted())
							break;
						selector.select(); // 셀럭터로 소켓을 선택한다. 여기서 Blocking 됨.

						Iterator<?> keys = selector.selectedKeys().iterator();

						while (keys.hasNext()) { // 셀렉터가 가지고 있는 정보와 비교해봄

							SelectionKey key = (SelectionKey) keys.next();
							keys.remove();

							if (!key.isValid()) { // 사용가능한 상태가 아니면 그냥 넘어감.
								continue;
							}

							if (key.isAcceptable()) { // Accept가 가능한 상태라면
								accept(key);
							}

							else if (key.isReadable()) { // 데이터를 읽을 수 있는 상태라면
								readData(key);
							}

						}

					}
					catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		workthread.start();

	}

	private void accept(SelectionKey key) { // 전달받은 SelectionKey로 Accept를 진행
		ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
		SocketChannel channel;

		try {
			channel = serverChannel.accept();
			channel.configureBlocking(false);
			Socket socket = channel.socket();
			SocketAddress remoteAddr = socket.getRemoteSocketAddress();
			// register channel with selector for further IO
			dataMapper.put(channel, remoteAddr.toString());
			channel.register(this.selector, SelectionKey.OP_READ);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void readData(SelectionKey key) { // 전달받은 SelectionKey에서 데이터를 읽는다.

		SocketChannel channel = (SocketChannel) key.channel();
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		SocketAddress remoteAddr=null;
		Socket socket;
		int numRead = -1;

		try {
			numRead = channel.read(buffer);
			socket = channel.socket();
			remoteAddr = socket.getRemoteSocketAddress();
			if (numRead == -1) { // 아직 읽지 않았다면 읽는다.
				this.dataMapper.remove(channel);
				socket = channel.socket();
				remoteAddr = socket.getRemoteSocketAddress();
				channel.close();
				key.cancel();
				return;
			}

		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		msg = new MessagePacker(buffer.array()); // Byte Data를 다 받아왔다.
		byte protocol = msg.getProtocol();

		switch (protocol) {
		case MessageProtocol.CLOSE:{
			try {
				log.append(remoteAddr+" 접속 종료!\n");
				channel.close();
				key.cancel();
			} 
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
		}
		case MessageProtocol.LOGIN:{
			MessagePacker sendmsg = new MessagePacker();
			msg.SetProtocol(MessageProtocol.LOGIN);
			msg.add(room.size());
			for(String item:room)
			{
				msg.add(item);
			}
			msg.Finish();
			try {
				channel.write(msg.getBuffer());
			} 
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			log.append(remoteAddr+" login!\n");
			break;
		}

		case MessageProtocol.JOIN: {
			log.append("CHAT\n");
			log.append(msg.getString()+"\n"); 
			log.append(msg.getInt()+"\n");
			break;
		}

		case MessageProtocol.BATTLE_START: {
			log.append("BATTLE_START\n");
			break;
		}

		case MessageProtocol.BATTLE_END: {
			log.append("BATTLE_END\n");
			break;
		}

		case MessageProtocol.BATTLE: {
			log.append("BATTLE\n");
			break;
		}
		}

	}
}
