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
	Selector selector; // �ͺ��ŷ�� �ٽ��� Selector �ʵ� ����
	ServerSocketChannel serverSocketChannel; // Ŭ���̾�Ʈ ���� �����ϴ� ServerSocketChannel
	InetSocketAddress address;
	List<Client> connections = new Vector<Client>(); // ����� Ŭ���̾�Ʈ�� �����ϴ�
	Map<String, Client> userMap = new HashMap<String,Client>();

	void startServer() {
		try {
			selector = Selector.open(); // ������ ����
			serverSocketChannel = ServerSocketChannel.open(); // ����
			serverSocketChannel.configureBlocking(false); // �ͺ��ŷ ����
			serverSocketChannel.bind(address); // ��Ʈ�� ���ε�
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		} catch (Exception e) {
			if (serverSocketChannel.isOpen()) {
				stopServer();
			}
			return;// ���ܹ߻��� ��������ä���� ���������� stop() ȣ��
		}
		Thread thread = new Thread() {
			@Override
			public void run() {
				while (true) {
					try {
						int keyCount = selector.select();// �۾� ó���� �غ��
						if (keyCount == 0) {
							continue;
						}
						Set<SelectionKey> selectedKeys = selector.selectedKeys();

						Iterator<SelectionKey> iterator = selectedKeys.iterator();

						while (iterator.hasNext()) {
							SelectionKey selectionKey = iterator.next();
							if (selectionKey.isAcceptable()) {// ���� ���� �۾��� ���
								accept(selectionKey);
							} else if (selectionKey.isReadable()) {// �б� �۾��� ���
								Client client = (Client) selectionKey.attachment();
								client.receive(selectionKey);
							} else if (selectionKey.isWritable()) {// ���� �۾��� ���
								Client client = (Client) selectionKey.attachment();
								client.send(selectionKey);
							}
							iterator.remove();// ���õ� Ű�¿��� ó�� �Ϸ��
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
			ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();// SelectionKey�κ���
			SocketChannel socketChannel = serverSocketChannel.accept();
			// ServerSocketChannel�� accept()�� ȣ���ϸ�, SocketChannel����.
			String message = "[���� ����: " + socketChannel.getRemoteAddress() + ": " + Thread.currentThread().getName()
					+ "]\n";
			txtDisplay.append(message);
			Client client = new Client(socketChannel);// Client�����ϰ�
			connections.add(client);

		} catch (Exception e) {
			if (serverSocketChannel.isOpen()) {
				stopServer();
			}
		}
	}

	class Client {

		SocketChannel socketChannel;

		String sendData; // Ŭ���̾�Ʈ�� ���� �����͸� ������ �ʵ�

		Client(SocketChannel socketChannel) throws IOException {

			this.socketChannel = socketChannel;

			socketChannel.configureBlocking(false); // �ͺ��ŷ ����

			SelectionKey selectionKey = socketChannel.register(selector, SelectionKey.OP_READ); // �б��۾� �������� Selector��
																								// ���

			selectionKey.attach(this);// SelectionKey�� �ڽ��� ÷�� ��ü�� ����.

		}

		void receive(SelectionKey selectionKey) {

			try {
				ByteBuffer byteBuffer = ByteBuffer.allocate(100);
				int byteCount = socketChannel.read(byteBuffer);
				if (byteCount == -1) {
					throw new IOException();
				}

				String message = "[��û ó��: " + socketChannel.getRemoteAddress() + ": " + Thread.currentThread().getName()
						+ "]\n";
				txtDisplay.append(message);
				byteBuffer.flip();
				Charset charset = Charset.forName("UTF-8");
				String data = charset.decode(byteBuffer).toString(); // ���ڿ� ��ȯ
				
				
				
				for (Client client : connections) {

					client.sendData = data;

					SelectionKey key = client.socketChannel.keyFor(selector);

					key.interestOps(SelectionKey.OP_WRITE);// ��� Ŭ���̾�Ʈ����
				}

				selector.wakeup(); // ����� �۾� ������ �����ϵ��� �ϱ�����

			} catch (Exception e) {

				try {

					connections.remove(this);

					String message = "[Ŭ���̾�Ʈ ��� �ȵ�: " + socketChannel.getRemoteAddress() + ": "
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
				socketChannel.write(byteBuffer);// ������ ������
				selectionKey.interestOps(SelectionKey.OP_READ);// �۾� ���� ����
				selector.wakeup();

			} catch (Exception e) {

				try {

					String message = "[Ŭ���̾�Ʈ ��� �ȵ�: " + socketChannel.getRemoteAddress() + ": "
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
					txtDisplay.append("���� ���� [IP:"+address.getHostString()+" || Port: "+address.getPort()+"]\n");
					o.setLabel("Stop");
				}
				else if(o.getLabel() == "Stop") {
					stopServer();
					txtDisplay.append("���� ���� [IP:"+address.getHostString()+" || Port: "+address.getPort()+"]\n");
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