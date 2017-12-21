package Project;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

import javax.swing.ImageIcon;

public class Client {

	SocketChannel socketChannel;
	InetSocketAddress address;

	void startClient() {
		Thread thread = new Thread() { // ������ ����
			@Override
			public void run() {
				try {
					socketChannel = SocketChannel.open(); // ���� ���� �� ���� ��û
					socketChannel.configureBlocking(true);
					socketChannel.connect(address);
					textView.append("[���� �Ϸ�: " + socketChannel.getRemoteAddress() + "]\n");

				} catch (Exception e) {

					if (socketChannel.isOpen()) {
						stopClient();
					}
					return;
				}
				receive(); // �������� ���� ������ �ޱ�
			}
		};
		thread.start(); // ������ ����
	}

	void stopClient() {
		try {
			if (socketChannel != null && socketChannel.isOpen()) {
				socketChannel.close();
			}
		} catch (IOException e) {
		}
	}

	void receive() {
		while (true) {
			try {
				ByteBuffer byteBuffer = ByteBuffer.allocate(100);
				int readByteCount = socketChannel.read(byteBuffer); // �����͹ޱ�
				if (readByteCount == -1) {
					throw new IOException();
				}
				byteBuffer.flip(); // ���ڿ��� ��ȯ
				Charset charset = Charset.forName("UTF-8");
				String data = charset.decode(byteBuffer).toString();
				
				textView.append(data+"\n");
			} catch (Exception e) {
				stopClient();
				break;
			}
		}
	}

	void send(String msg) {
		Thread thread = new Thread() { // ������ ����
			@Override
			public void run() {
				try {
					Charset charset = Charset.forName("UTF-8");
					ByteBuffer byteBuffer = charset.encode(msg);
					socketChannel.write(byteBuffer); // ������ ������ ������
				} catch (Exception e) {
					stopClient();
				}
			}
		};
		thread.start(); // ������ ����
	}
	
	TextArea textView = new TextArea(null, 30, 30, TextArea.SCROLLBARS_NONE);
	TextArea chatBox = new TextArea(null, 30, 30, TextArea.SCROLLBARS_NONE);
	Panel bPanel = new Panel();
	Button bsend = new Button("����");
	Panel lPanel = new Panel();
	Label lnick = new Label("�г���:");
	TextField tnick = new TextField(20);
	Button blogin = new Button("�α���");
	Button bstart = new Button("����");
	Frame main = new Frame("ä�� ���α׷�");
	String NickName;
	
	public Client(String ip,String port) {
		
		address = new InetSocketAddress(ip, Integer.parseInt(port));
		
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
		lPanel.setSize(500, 50);
		lnick.setBounds(150, 20, 50, 20);
		lnick.setVisible(false);
		tnick.setBounds(200, 20, 100, 20);
		tnick.setVisible(false);
		blogin.setBounds(310, 15, 50, 30);
		blogin.setVisible(false);
		bstart.setBounds(190, 15, 100, 30);
		lPanel.add(lnick);
		lPanel.add(tnick);
		lPanel.add(blogin);
		lPanel.add(bstart);
		
		bstart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				startClient();
				bstart.setVisible(false);
				lnick.setVisible(true);
				tnick.setVisible(true);
				blogin.setVisible(true);
			}
		});
		
		blogin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				NickName = tnick.getText();
				String message = "["+NickName+"]���� �����߽��ϴ�.";
				send(message);
				blogin.setVisible(false);
				tnick.setEnabled(false);
			}
		});
		
		bsend.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (NickName != null) {
					String message = "[" + NickName + "]: " + chatBox.getText();
					send(message);
					chatBox.setText("");
				}
				else {
					Dialog nickerror = new Dialog(main,"����",true);
					
					nickerror.add(new Label("    �г����� �Է��ϼ���!"),"Center");
					nickerror.addWindowListener(new WindowAdapter() {
						@Override
						public void windowClosing(WindowEvent e) {
							nickerror.dispose();
						}
					});
					Button bok = new Button("Ȯ��");
					bok.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							nickerror.dispose();
						}
					});
					nickerror.add(bok,"South");
					nickerror.setSize(150, 150);
					Toolkit tk = Toolkit.getDefaultToolkit();
					Dimension screenSize = tk.getScreenSize();
					nickerror.setLocation(screenSize.width/2-nickerror.getWidth()/2, screenSize.height/2-nickerror.getHeight()/2);
					nickerror.setVisible(true);
				}
			}
		});

		main.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				String message = "["+NickName+"]���� �������ϴ�.";
				send(message);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				stopClient();
				main.dispose();
				}
		});

		main.add(lPanel, "North");
		main.add(textView, "Center");
		main.add(bPanel, "South");
		main.setSize(500, 500);
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize();
		main.setLocation(screenSize.width / 2 - main.getWidth() / 2, screenSize.height / 2 - main.getHeight() / 2);
		main.setIconImage(new ImageIcon("client.png").getImage());
		main.setVisible(true);
	}
}
