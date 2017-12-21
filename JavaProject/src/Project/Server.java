package Project;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.ImageIcon;

public class Server {
	Selector selector; // 넌블로킹의 핵심인 Selector 필드 선언
	ServerSocketChannel serverSocketChannel; // 클라이언트 연결 수락하는 ServerSocketChannel
	InetSocketAddress address;
	List<Client> connections = new Vector<Client>(); // 연결된 클라이언트를 저장하는
	Map<String, Client> userMap = new HashMap<String,Client>();

	void startServer() {
		try {
			selector = Selector.open(); // 셀렉터 생성
			serverSocketChannel = ServerSocketChannel.open(); // 생성
			serverSocketChannel.configureBlocking(false); // 넌블로킹 설정
			serverSocketChannel.bind(address); // 포트에 바인딩
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		} catch (Exception e) {
			if (serverSocketChannel.isOpen()) {
				stopServer();
			}
			return;// 예외발생시 서버소켓채널이 열려있으면 stop() 호출
		}
		Thread thread = new Thread() {
			@Override
			public void run() {
				while (true) {
					try {
						int keyCount = selector.select();// 작업 처리가 준비된
						if (keyCount == 0) {
							continue;
						}
						Set<SelectionKey> selectedKeys = selector.selectedKeys();

						Iterator<SelectionKey> iterator = selectedKeys.iterator();

						while (iterator.hasNext()) {
							SelectionKey selectionKey = iterator.next();
							if (selectionKey.isAcceptable()) {// 연결 수락 작업일 경우
								accept(selectionKey);
							} else if (selectionKey.isReadable()) {// 읽기 작업일 경우
								Client client = (Client) selectionKey.attachment();
								client.receive(selectionKey);
							} else if (selectionKey.isWritable()) {// 쓰기 작업일 경우
								Client client = (Client) selectionKey.attachment();
								client.send(selectionKey);
							}
							iterator.remove();// 선택된 키셋에서 처리 완료된
						}
					} catch (Exception e) {
						if (serverSocketChannel.isOpen()) {
							stopServer();
						}
						break;
					}
				}
			}
		};
		thread.start();
	}

	void stopServer() {
		try {
			Iterator<Client> iterator = connections.iterator();
			while (iterator.hasNext()) {
				Client client = iterator.next();
				client.socketChannel.close();
				iterator.remove();
			}
			if (serverSocketChannel != null && serverSocketChannel.isOpen()) {
				serverSocketChannel.close();
			}
			if (selector != null && selector.isOpen()) {
				selector.close();
			}

		} catch (Exception e) {
		}
	}

	void accept(SelectionKey selectionKey) {
		try {
			ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();// SelectionKey로부터
			SocketChannel socketChannel = serverSocketChannel.accept();
			// ServerSocketChannel의 accept()를 호출하면, SocketChannel리턴.
			String message = "[연결 수락: " + socketChannel.getRemoteAddress() + ": " + Thread.currentThread().getName()
					+ "]\n";
			txtDisplay.append(message);
			Client client = new Client(socketChannel);// Client생성하고
			connections.add(client);

		} catch (Exception e) {
			if (serverSocketChannel.isOpen()) {
				stopServer();
			}
		}
	}

	class Client {

		SocketChannel socketChannel;

		String sendData; // 클라이언트에 보낼 데이터를 저장할 필드

		Client(SocketChannel socketChannel) throws IOException {

			this.socketChannel = socketChannel;

			socketChannel.configureBlocking(false); // 넌블로킹 지정

			SelectionKey selectionKey = socketChannel.register(selector, SelectionKey.OP_READ); // 읽기작업 유형으로 Selector에
																								// 등록

			selectionKey.attach(this);// SelectionKey에 자신을 첨부 객체로 저장.

		}

		void receive(SelectionKey selectionKey) {

			try {
				ByteBuffer byteBuffer = ByteBuffer.allocate(100);
				int byteCount = socketChannel.read(byteBuffer);
				if (byteCount == -1) {
					throw new IOException();
				}

				String message = "[요청 처리: " + socketChannel.getRemoteAddress() + ": " + Thread.currentThread().getName()
						+ "]\n";
				txtDisplay.append(message);
				byteBuffer.flip();
				Charset charset = Charset.forName("UTF-8");
				String data = charset.decode(byteBuffer).toString(); // 문자열 변환
				
				
				
				for (Client client : connections) {

					client.sendData = data;

					SelectionKey key = client.socketChannel.keyFor(selector);

					key.interestOps(SelectionKey.OP_WRITE);// 모든 클라이언트에게
				}

				selector.wakeup(); // 변경된 작업 유형일 감지하도록 하기위해

			} catch (Exception e) {

				try {

					connections.remove(this);

					String message = "[클라이언트 통신 안됨: " + socketChannel.getRemoteAddress() + ": "
							+ Thread.currentThread().getName() + "]\n";
					txtDisplay.append(message);
					socketChannel.close();

				} 
				catch (IOException e2) {
				}
			}
		}

		void send(SelectionKey selectionKey) {

			try {
				Charset charset = Charset.forName("UTF-8");
				ByteBuffer byteBuffer = charset.encode(sendData);
				socketChannel.write(byteBuffer);// 데이터 보내기
				selectionKey.interestOps(SelectionKey.OP_READ);// 작업 유형 변경
				selector.wakeup();

			} catch (Exception e) {

				try {

					String message = "[클라이언트 통신 안됨: " + socketChannel.getRemoteAddress() + ": "
							+ Thread.currentThread().getName() + "]\n";
					txtDisplay.append(message);

					connections.remove(this);

					socketChannel.close();

				} catch (IOException e2) {
				}

			}

		}

	}

	TextArea txtDisplay;
	Frame mainWin;
	Button btnStartStop;

	public Server(String ip,String port) {
		address = new InetSocketAddress(ip, Integer.parseInt(port));
		
		mainWin = new Frame("Server");
		txtDisplay = new TextArea(null,30,30,TextArea.SCROLLBARS_NONE);
		btnStartStop = new Button("Start");
		Panel bPanel = new Panel();
		bPanel.setLayout(new FlowLayout());
		bPanel.add(btnStartStop);
		btnStartStop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Button o =(Button)e.getSource();
				if(o.getLabel() == "Start") {
					startServer();
					txtDisplay.append("서버 구동 [IP:"+address.getHostString()+" || Port: "+address.getPort()+"]\n");
					o.setLabel("Stop");
				}
				else if(o.getLabel() == "Stop") {
					stopServer();
					txtDisplay.append("서버 중지 [IP:"+address.getHostString()+" || Port: "+address.getPort()+"]\n");
					o.setLabel("Start");
				}
			}
		});
		
		mainWin.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				stopServer();
				mainWin.dispose();
			}
		});
		mainWin.add(txtDisplay,"Center");
		mainWin.add(bPanel,"South");
		mainWin.setSize(400, 500);
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize();
		mainWin.setLocation(screenSize.width/2-mainWin.getWidth()/2, screenSize.height/2-mainWin.getHeight()/2);
		mainWin.setIconImage(new ImageIcon("server.png").getImage());
		mainWin.setVisible(true);
	}
}