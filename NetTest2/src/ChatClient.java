import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;

import MsgPacker.MessagePacker;
import MsgPacker.MessageProtocol;

public class ChatClient implements Runnable{
	
	Frame main;
	List roomview = new List();
	Panel bPanel = new Panel();
	Button createroom = new Button("방 만들기");
	Button joinroom = new Button("들어가기");
	Button refresh = new Button("새로고침");
	public final ChatClient my = this;
	
	// 소켓채널
	private Selector Selector = null;
	private SocketChannel Client = null;
	private InetSocketAddress connectAddress;
	private boolean bStart = true;
	
	private ByteBuffer recvBuff;

	public ChatClient(String ip,int port) {
		
		connectAddress = new InetSocketAddress(ip, port);
		main = new Frame("OmokClient");
		recvBuff = ByteBuffer.allocate(1024);
	}
	
	public void View() {
		bPanel.setLayout(new FlowLayout(FlowLayout.CENTER,50,30));
		bPanel.add(createroom);
		bPanel.add(joinroom);
		bPanel.add(refresh);
		
		joinroom.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String roomName = roomview.getSelectedItem();
				String stringkey = roomName.substring(3, 4);
				int key = Integer.parseInt(stringkey);
				MessagePacker msg = new MessagePacker();
				msg.SetProtocol(MessageProtocol.JOIN);
				msg.add(key);
				msg.Finish();
				send(msg);
			}
		});
		
		refresh.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MessagePacker msg = new MessagePacker();
				msg.SetProtocol(MessageProtocol.LOGIN);
				msg.Finish();
				send(msg);
			}
		});
		
		createroom.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Dialog createRoom = new Dialog(main, "방 만들기", true);
				createRoom.setLayout(null);
				
				Label lname = new Label("방 이름:");
				lname.setBounds(20, 90, 50, 20);
				createRoom.add(lname);
				
				TextField tfname = new TextField();
				tfname.setBounds(70, 90, 150, 20);
				createRoom.add(tfname);
				
				Button bok = new Button("만들기");
				bok.setBounds(60, 150, 50, 30);
				bok.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if(tfname.getText().length()>0) {
							MessagePacker msg = new MessagePacker();
							msg.SetProtocol(MessageProtocol.CREATE);
							msg.add(tfname.getText());
							msg.Finish();
							send(msg);
							createRoom.dispose();
						}
						else {
							tfname.setFocusable(true);
						}
					}
				});
				createRoom.add(bok);
				
				Button bcancel = new Button("취소");
				bcancel.setBounds(150, 150, 50, 30);
				bcancel.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						createRoom.dispose();
					}
				});
				createRoom.add(bcancel);
				
				createRoom.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent e) {
						createRoom.dispose();
					}
				});
				createRoom.setSize(250, 200);
				Toolkit tk = Toolkit.getDefaultToolkit();
				Dimension screenSize = tk.getScreenSize();
				createRoom.setBackground(Color.white);
				createRoom.setLocation(screenSize.width/2-createRoom.getWidth()/2, screenSize.height/2-createRoom.getHeight()/2);
				createRoom.setVisible(true);
			}
		});
		
		main.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {

				MessagePacker msg = new MessagePacker();
				msg.SetProtocol(MessageProtocol.CLOSE);
				msg.Finish();
				send(msg);
				bStart = false;
				System.exit(0);
			}
		});
		
		main.add(roomview, "Center");
		main.add(bPanel, "South");
		main.setSize(500,500);
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize();
		main.setLocation(screenSize.width/2-main.getWidth()/2, screenSize.height/2-main.getHeight()/2);
		main.setVisible(true);
	}
	
	public void Connect() {
		try {
			Client = SocketChannel.open();
			// 논블로킹,보내고 받는사이즈 1000,서버와 계속해서 유지,해체시 바로 데이터들 삭제.
			Client.configureBlocking(false);
			Client.setOption(StandardSocketOptions.SO_RCVBUF, 128 * 1024);
			Client.setOption(StandardSocketOptions.SO_SNDBUF, 128 * 1024);
			Client.setOption(StandardSocketOptions.SO_KEEPALIVE, true);
			Client.setOption(StandardSocketOptions.SO_LINGER, 0);
			// 요청연결
			Client.connect(connectAddress);
			Thread thread = new Thread(this);
			thread.start();
		} catch (Exception e) {
			System.out.println("Connect Error : " + e.getMessage());
		}
	}
	
	public void RecvEvt() {
		MessagePacker msg = new MessagePacker(recvBuff.array());
		
		byte protocol = msg.getProtocol();
		switch (protocol) {
		case MessageProtocol.LOGIN:{
			int size = msg.getInt();
			roomview.removeAll();
			for (int i = 0; i < size; ++i) {
				roomview.add(msg.getString());
			}
			break;
		}
		case MessageProtocol.CREATE:

			break;
		case MessageProtocol.JOIN:{
			int roomid = msg.getInt();
			String roomName = msg.getString();
			ArrayList<String> member = new ArrayList<String>();
			int size = msg.getInt();
			for(int i = 0; i<size;++i) {
				member.add(msg.getString());
			}
			main.setVisible(false);
			ReadyRoom r = new ReadyRoom(roomid,roomName,main,my);
			for(int i = 0; i<member.size();++i) {
				r.member.add(member.get(i));
			}
			break;
		}
		case MessageProtocol.READY:

			break;
		case MessageProtocol.READYLEAVE:

			break;
		case MessageProtocol.READYCANCEL:

			break;
		case MessageProtocol.BATTLE_START:

			break;
		case MessageProtocol.BATTLE:

			break;
		case MessageProtocol.BATTLE_END:

			break;
		default:
			recvBuff.clear();
			break;
		}
	}
	
	public void send(MessagePacker data) {
		try {
			Client.write(data.getBuffer());
			// Thread.sleep(10);
		} catch (Exception e) {
			System.out.println("Send Error : " + e.getMessage());
		}

	}

	@Override
	public void run() {
		try {
			Selector = java.nio.channels.Selector.open();
			Client.register(Selector, SelectionKey.OP_CONNECT);
		} catch (Exception ex) {
		}
		while (bStart) {
			try {
				Selector.select();
				Iterator<SelectionKey> keys = Selector.selectedKeys().iterator();
				while (keys.hasNext()) {
					SelectionKey key = keys.next();
					keys.remove();
					if (key.isConnectable()) {
						if (Client.finishConnect()) {
							key.interestOps(SelectionKey.OP_READ);
						} 
						else {
							key.cancel();
						}
					} 
					else if (key.isReadable()) {
						recvBuff.clear();
						Client.read(recvBuff);
						RecvEvt();
					}
				}
			} 
			catch (Exception e) {
				this.bStart = false;
			}
		}
	}
	
}