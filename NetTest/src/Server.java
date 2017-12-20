import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Executors;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import MsgPacker.MessagePacker;
import MsgPacker.MessageProtocol;

public class Server {

	/////////Network////////////////////////
	private AsynchronousChannelGroup channelGroup;
	private AsynchronousServerSocketChannel serverSocketChannel;
	List<Client> connections = new Vector<Client>();
	private InetSocketAddress socketAddress;
	
	/////UI////////////
	public static HashMap<Integer, GameRoom> roomManager = new HashMap<Integer,GameRoom>();
	private boolean flag = false;
	private final Server my = this;
	Button start = new Button("     시작     ");
	Button stop = new Button("     정지     ");
	Panel bPanel = new Panel();
	TextArea log = new TextArea();
	static int roomcount = 0;

	public Server(String ip,String port) {
		Frame f = new Frame("OmokServer");
		roomcount++;
		roomManager.put(roomcount^1204,new GameRoom(roomcount^1204,"No."+roomcount+" 다 덤벼 "));
		
		socketAddress = new InetSocketAddress(ip,Integer.parseInt(port)); // 소켓 주소 설정
		
		bPanel.setLayout(new FlowLayout());
		bPanel.add(start);
		bPanel.add(stop);
		start.addActionListener(new ActionHandle());
		stop.addActionListener(new ActionHandle());
		
		f.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		f.add(bPanel,"North");
		f.add(log,"Center");
		f.setSize(500,800);
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize();
		f.setLocation(screenSize.width/2-f.getWidth()/2, screenSize.height/2-f.getHeight()/2);
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
					my.startServer();
					flag = true;
				}
				else {
					log.append("서버가 이미 구동중입니다!\n");
				}
				
			}
			else if(o.equals(stop)) {
				if(flag == true) {
					my.stopServer();
					flag = false;
				}
			}
		}

	}

	void startServer() {
		try {
			channelGroup = AsynchronousChannelGroup.withFixedThreadPool(Runtime.getRuntime().availableProcessors(), Executors.defaultThreadFactory());
			
			serverSocketChannel = AsynchronousServerSocketChannel.open(channelGroup);
			serverSocketChannel.bind(socketAddress);
		}catch (Exception e) {
			if(serverSocketChannel.isOpen()) {
				stopServer();
			}
			return;
		}
		
		serverSocketChannel.accept(null, new CompletionHandler<AsynchronousSocketChannel, Void>() {
			@Override
			public void completed(AsynchronousSocketChannel socketChannel, Void attachment) {
				try {
					log.append( "[연결 수락: " + socketChannel.getRemoteAddress()  + ": " + Thread.currentThread().getName() + "]\n");
				}
				catch (Exception e) {}
				Client client = new Client(socketChannel);
				connections.add(client);
				serverSocketChannel.accept(null,this);
			}
			@Override
			public void failed(Throwable exc, Void attachment) {
				if(serverSocketChannel.isOpen()) {
					stopServer();
				}
			}
		});
	}
	
	void stopServer() {
		try {
			connections.clear();
			if(channelGroup != null && !channelGroup.isShutdown()) {
				channelGroup.shutdownNow();
			}
			log.append("서버 중지\n");
		}catch (Exception e) {}
	}
	
	class Client{
		AsynchronousSocketChannel socketChannel;
		GameUser user;
		
		public Client(AsynchronousSocketChannel socketChannel) {
			this.socketChannel = socketChannel;
			try {
				user = new GameUser(socketChannel.getRemoteAddress(),socketChannel);
			} 
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			receive();
		}
		
		public void receive() {
			ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
			socketChannel.read(byteBuffer,byteBuffer,new CompletionHandler<Integer, ByteBuffer>() {
				@Override
				public void completed(Integer result, ByteBuffer attachment) {
					try {
						MessagePacker msg = new MessagePacker(attachment.array());
						byte protocol = msg.getProtocol();
						
						
						switch (protocol) {
						case MessageProtocol.LOGIN: {
							MessagePacker reply = new MessagePacker();
							reply.SetProtocol(MessageProtocol.LOGIN);
							reply.add(roomManager.size());
							for(int roomkey:roomManager.keySet())
							{
								reply.add(roomManager.get(roomkey).getName());
							}
							reply.Finish();
							send(reply);
							break;
						}
						case MessageProtocol.CLOSE: {
							connections.remove(socketChannel);
							log.append("[연결 종료: " + socketChannel.getRemoteAddress() + ": " + Thread.currentThread().getName() + "]\n");
							socketChannel.close();
							break;
						}

						case MessageProtocol.CREATE: {
							String roomname = msg.getString();
							++roomcount;
							roomname = "No."+roomcount+" "+roomname+" ";
							int roomkey = roomcount ^ 1204;
							GameRoom gr = new GameRoom(roomkey,roomname,user);
							roomManager.put(roomkey, gr);
							
							MessagePacker reply = new MessagePacker();
							reply.SetProtocol(MessageProtocol.CREATE);
							reply.add(roomkey);
							reply.add(roomname);
							reply.Finish();
							send(reply);
							
							gr.broadcast(MessageProtocol.JOIN);
							
							MessagePacker broadcast = new MessagePacker();
							broadcast.SetProtocol(MessageProtocol.LOGIN);
							broadcast.add(roomManager.size());
							for(int item:roomManager.keySet())
							{
								broadcast.add(roomManager.get(item).getName());
							}
							broadcast.Finish();
							for(Client client: connections) {
								client.send(broadcast);
							}
							
							break;
						}

						case MessageProtocol.JOIN: {
							int roomkey = msg.getInt();
							GameRoom gr = roomManager.get(roomkey);
							if(gr.getNumberOfPeople() < 2) {
								gr.enterUser(user);
								gr.broadcast(MessageProtocol.JOIN);
							}
							break;
						}
						
						case MessageProtocol.READYLEAVE: {
							int roomkey = msg.getInt();
							GameRoom room = roomManager.get(roomkey);
							room.exitUser(user);
							room.broadcast(MessageProtocol.JOIN);
							
							MessagePacker broadcast = new MessagePacker();
							broadcast.SetProtocol(MessageProtocol.LOGIN);
							broadcast.add(roomManager.size());
							for(int item:roomManager.keySet())
							{
								broadcast.add(roomManager.get(item).getName());
							}
							broadcast.Finish();
							for(Client client: connections) {
								client.send(broadcast);
							}
							break;
						}

						case MessageProtocol.BATTLE_START: {

							break;
						}

						case MessageProtocol.BATTLE_END: {

							break;
						}

						case MessageProtocol.BATTLE: {

							break;
						}
						}
						ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
						socketChannel.read(byteBuffer,byteBuffer,this);
					}catch (Exception e) {}
				}
				@Override
				public void failed(Throwable exc, ByteBuffer attachment) {
					try {
						log.append( "[클라이언트 통신 안됨: " + socketChannel.getRemoteAddress() + ": " + Thread.currentThread().getName() + "]\n");
						connections.remove(Client.this);
						socketChannel.close();
					}catch (Exception e) {}
				}
			});
		}
		
		void send(MessagePacker data) {
			socketChannel.write(data.getBuffer(),null,new CompletionHandler<Integer, Void>() {
				@Override
				public void completed(Integer result, Void attachment) {
				}
				@Override
				public void failed(Throwable exc, Void attachment) {
					try {
						log.append( "[클라이언트 통신 안됨: " + socketChannel.getRemoteAddress() + ": " + Thread.currentThread().getName() + "]\n");
						connections.remove(Client.this);
						socketChannel.close();
					}catch (Exception e) {}
				}
			});
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	/*
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
		Socket socket=null;
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
			sendmsg.SetProtocol(MessageProtocol.LOGIN);
			sendmsg.add(room.size());
			for(int roomkey:room.keySet())
			{
				sendmsg.add(room.get(roomkey));
			}
			sendmsg.Finish();
			try {
				channel.write(sendmsg.getBuffer());
			} 
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			log.append(remoteAddr+" login!\n");
			break;
		}
		
		case MessageProtocol.CREATE: {
			String roomname = msg.getString();
			roomname = String.format(roomname+" (0/1)");
			int roomkey = room.size() ^ 1204;
			room.put(roomkey, roomname);
			
			MessagePacker sendmsg = new MessagePacker();
			sendmsg.SetProtocol(MessageProtocol.LOGIN);
			sendmsg.add(room.size());
			for(int item:room.keySet())
			{
				sendmsg.add(room.get(item));
			}
			sendmsg.Finish();
			
			Iterator<?> keys = selector.selectedKeys().iterator();

			while (keys.hasNext()) { // 셀렉터가 가지고 있는 정보와 비교해봄

				SelectionKey key1 = (SelectionKey) keys.next();
				keys.remove();

				if (!key1.isValid()) { // 사용가능한 상태가 아니면 그냥 넘어감.
					continue;
				}
				if (key1.isReadable()) { // 데이터를 읽을 수 있는 상태라면
					SocketChannel target = (SocketChannel)key1.channel();
					try {
						target.write(sendmsg.getBuffer());
					} 
					catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
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
	*/
}
