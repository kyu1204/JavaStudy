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

public class Client {
	private final Client my = this;
	private String NickName;
	
	Frame main;
	TextArea textView = new TextArea(null,30,30,TextArea.SCROLLBARS_NONE);
	TextArea chatBox = new TextArea(null,30,30,TextArea.SCROLLBARS_NONE);
	Panel bPanel = new Panel();
	Button bsend = new Button("전송");
	
	AsynchronousChannelGroup channelGroup;
	AsynchronousSocketChannel socketChannel;
	public Client(String ip,String port,String nick) {
		this.NickName = nick;
		main = new Frame("채팅 프로그램");
		textView.setEditable(false);
		textView.setBackground(Color.WHITE);
		startClient(ip, String.valueOf(port));
		bPanel.setLayout(null);
		bPanel.setSize(500, 100);
		chatBox.setBounds(5, 5, 370, 90);
		bPanel.add(chatBox);
		bsend.setBounds(385, 5, 90, 90);
		bPanel.add(bsend);

		
		bsend.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MessagePacker msg = new MessagePacker();
				msg.SetProtocol(MessageProtocol.CHAT);
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
				stopClient();

				System.exit(0);
			}
		});
		main.add(textView, "Center");
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
					MessagePacker msg = new MessagePacker(); // MessagePacker 사용해보자
					msg.SetProtocol(MessageProtocol.LOGIN);
					msg.add(NickName);
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
					case MessageProtocol.CHAT: {
						textView.append(msg.getString());
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