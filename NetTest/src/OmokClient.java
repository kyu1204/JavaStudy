import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.Executors;
import java.awt.*;
import java.awt.List;
import java.awt.event.*;
import java.util.*;

import MsgPacker.MessagePacker;
import MsgPacker.MessageProtocol;

public class OmokClient {
	private final OmokClient my = this;
	private int roomkey;
	private String roomName;
	public boolean readyflag = false;
	
	Frame main;
	List roomview = new List();
	Panel bPanel = new Panel();
	Button createroom = new Button("�� �����");
	Button joinroom = new Button("����");
	Button refresh = new Button("���ΰ�ħ");
	ArrayList<String> room = new ArrayList<String>();
	
	AsynchronousChannelGroup channelGroup;
	AsynchronousSocketChannel socketChannel;
	public OmokClient(String ip,String port) {
		main = new Frame("Omok");
		startClient(ip, String.valueOf(port));
		roomview.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		bPanel.setLayout(new FlowLayout(FlowLayout.CENTER,50,30));
		bPanel.add(createroom);
		bPanel.add(joinroom);
		bPanel.add(refresh);
		
		joinroom.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String roomName = roomview.getSelectedItem();
				String stringkey = roomName.substring(3, 4);
				int key = Integer.parseInt(stringkey) ^ 1204;
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
				Dialog createRoom = new Dialog(main, "�� �����", true);
				createRoom.setLayout(null);
				
				Label lname = new Label("�� �̸�:");
				lname.setBounds(20, 90, 50, 20);
				createRoom.add(lname);
				
				TextField tfname = new TextField();
				tfname.setBounds(70, 90, 150, 20);
				createRoom.add(tfname);
				
				Button bok = new Button("�����");
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
				
				Button bcancel = new Button("���");
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
				stopClient();

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

	public void startClient(String ip, String port) {
		InetSocketAddress hostAddress = new InetSocketAddress(ip, Integer.parseInt(port));
		try {
			channelGroup = AsynchronousChannelGroup.withFixedThreadPool(Runtime.getRuntime().availableProcessors(),
					Executors.defaultThreadFactory());
			socketChannel = AsynchronousSocketChannel.open(channelGroup);
			socketChannel.connect(hostAddress, null, new CompletionHandler<Void, Void>() {
				@Override
				public void completed(Void result, Void attachment) {
					MessagePacker msg = new MessagePacker(); // MessagePacker ����غ���

					msg.SetProtocol(MessageProtocol.LOGIN);
					msg.Finish();
					send(msg);

					receive();
				}

				@Override
				public void failed(Throwable exc, Void attachment) {
					if (socketChannel.isOpen()) {
						stopClient();
					}
					new Alram(main, "WARNING", true);
				}
			});
		} catch (Exception e) {
		}
    }
    public void stopClient() {
		try {
			if (channelGroup != null && !channelGroup.isShutdown()) {

				channelGroup.shutdownNow();
			}
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    void receive() {
    	ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
    	
		socketChannel.read(byteBuffer, byteBuffer, new CompletionHandler<Integer, ByteBuffer>() {
			@Override
			public void completed(Integer result, ByteBuffer attachment) {
				try {
					MessagePacker msg = new MessagePacker(attachment.array());
					byte protocol = msg.getProtocol();
					
					switch (protocol) {
					case MessageProtocol.LOGIN: {
						int size = msg.getInt();
						room.clear();
						for (int i = 0; i < size; ++i) {
							room.add(msg.getString());
						}
						roomview.removeAll();
						for(int i = 0; i< room.size();++i)
							roomview.add((String)room.get(i));
						break;
					}

					case MessageProtocol.CREATE: {
						roomkey = msg.getInt();
						roomName = msg.getString();
						//main.setVisible(false);
						break;
					}

					case MessageProtocol.JOIN: {
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
						readyflag = true;
						/*
						if(ready != null) {
							ready.member.removeAll();
							for(int i = 0; i<member.size();++i) {
								ready.member.add(member.get(i));
							}
						}
						else if(ready == null) {
							main.setVisible(false);
							ready = new ReadyRoom(roomid,roomName,main,my);
							ready.member.removeAll();
							for(int i = 0; i<member.size();++i) {
								ready.member.add(member.get(i));
							}
						}*/
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
					socketChannel.read(byteBuffer, byteBuffer, this);
				} 
				catch (Exception e) {}
			}

			@Override
			public void failed(Throwable exc, ByteBuffer attachment) {
				stopClient();
			}
		});
	}
    void send(MessagePacker data) {
		socketChannel.write(data.getBuffer(), null, new CompletionHandler<Integer, Void>() {
			@Override
			public void completed(Integer result, Void attachment) {
				// TODO Auto-generated method stub

			}
			@Override
			public void failed(Throwable exc, Void attachment) {
				stopClient();
			}
		});
	}
    
    
}