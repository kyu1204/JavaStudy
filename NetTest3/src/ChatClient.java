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
	private final ChatClient my = this;
	private String NickName;
	
	Frame main;
	TextArea textView = new TextArea(null,30,30,TextArea.SCROLLBARS_NONE);
	TextArea chatBox = new TextArea(null,30,30,TextArea.SCROLLBARS_NONE);
	Panel bPanel = new Panel();
	Button bsend = new Button("전송");
	Label lnick = new Label("닉네임: ");
	TextField tfnick = new TextField();
	Button blogin = new Button("로그인");
	Panel lPanel = new Panel();
	
	
	// 소켓채널
	private Selector Selector = null;
	private SocketChannel Client = null;
	private InetSocketAddress connectAddress;
	private boolean bStart = true;
	
	private ByteBuffer recvBuff;

	public ChatClient(String ip,int port) {
		connectAddress = new InetSocketAddress(ip, port);
		main = new Frame("채팅 프로그램");
		recvBuff = ByteBuffer.allocate(1024);
	}
	
	public void View() {
		
		main.setResizable(false);
		textView.setEditable(false);
		textView.setBackground(Color.WHITE);
		
		bPanel.setLayout(null);
		bPanel.setSize(500, 100);
		chatBox.setBounds(5, 5, 370, 90);
		bPanel.add(chatBox);
		bsend.setBounds(385, 5, 95, 90);
		bPanel.add(bsend);
		
		lPanel.setLayout(null);
		lPanel.setSize(500,50);
		lnick.setBounds(145, 10, 50, 20);
		lPanel.add(lnick);
		tfnick.setBounds(195, 10, 100, 20);
		lPanel.add(tfnick);
		blogin.setBounds(305, 5, 50, 30);
		lPanel.add(blogin);
		
		blogin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MessagePacker login = new MessagePacker(); // MessagePacker 사용해보자
				login.SetProtocol(MessageProtocol.LOGIN);
				login.add(NickName);
				login.Finish();
				send(login);
				blogin.setVisible(false);
				tfnick.setEnabled(false);
			}
		});

		
		bsend.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MessagePacker msg = new MessagePacker();
				msg.SetProtocol(MessageProtocol.CHAT);
				msg.add(NickName);
				msg.add(chatBox.getText());
				msg.Finish();
				send(msg);
				chatBox.setText("");
			}
		});
		
		main.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				MessagePacker msg = new MessagePacker();
				msg.SetProtocol(MessageProtocol.CLOSE);
				msg.Finish();
				send(msg);

				System.exit(0);
			}
		});
		
		main.add(lPanel, "North");
		main.add(textView, "Center");
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
		
		case MessageProtocol.CHAT: {
			textView.append(msg.getString());
			break;
		}
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