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
	private MessagePacker msg; // ���⼭ MessagePacker�� �Ẹ��
	private final Server my = this;
	private boolean flag = false;
	
	List<String> room = new ArrayList();
	Button start = new Button("     ����     ");
	Panel bPanel = new Panel();
	TextArea log = new TextArea();

	public Server(String ip,String port) {
		Frame f = new Frame("OmokServer");
		room.add("No.1 �� ���� (0/1)");
		socketAddress = new InetSocketAddress(ip,Integer.parseInt(port)); // ���� �ּ� ����
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
							+ "\n���� ����\n");
					my.run();
					flag = true;
				}
				else {
					log.append("������ �̹� �������Դϴ�!\n");
				}
				
			}
		}
		
	}

	@Override
	public void run() { // ������ ����Ǹ� ȣ��ȴ�.
		// TODO Auto-generated method stub
		try {
			selector = Selector.open(); // ���� ������ ����
			socketChannel = ServerSocketChannel.open(); // ��������ä�� ����
			socketChannel.configureBlocking(false); // ��ŷ ��带 False�� �����Ѵ�.

			socketChannel.bind(socketAddress); // ���� �ּҷ� ������ �����Ѵ�.
			socketChannel.register(selector, SelectionKey.OP_ACCEPT); // ���������͸� ����Ѵ�.

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
						selector.select(); // �����ͷ� ������ �����Ѵ�. ���⼭ Blocking ��.

						Iterator<?> keys = selector.selectedKeys().iterator();

						while (keys.hasNext()) { // �����Ͱ� ������ �ִ� ������ ���غ�

							SelectionKey key = (SelectionKey) keys.next();
							keys.remove();

							if (!key.isValid()) { // ��밡���� ���°� �ƴϸ� �׳� �Ѿ.
								continue;
							}

							if (key.isAcceptable()) { // Accept�� ������ ���¶��
								accept(key);
							}

							else if (key.isReadable()) { // �����͸� ���� �� �ִ� ���¶��
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

	private void accept(SelectionKey key) { // ���޹��� SelectionKey�� Accept�� ����
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

	private void readData(SelectionKey key) { // ���޹��� SelectionKey���� �����͸� �д´�.

		SocketChannel channel = (SocketChannel) key.channel();
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		SocketAddress remoteAddr=null;
		Socket socket;
		int numRead = -1;

		try {
			numRead = channel.read(buffer);
			socket = channel.socket();
			remoteAddr = socket.getRemoteSocketAddress();
			if (numRead == -1) { // ���� ���� �ʾҴٸ� �д´�.
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

		msg = new MessagePacker(buffer.array()); // Byte Data�� �� �޾ƿԴ�.
		byte protocol = msg.getProtocol();

		switch (protocol) {
		case MessageProtocol.CLOSE:{
			try {
				log.append(remoteAddr+" ���� ����!\n");
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
