import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import MsgPacker.MessagePacker;
import MsgPacker.MessageProtocol;

public class ServerCore extends ChatServer implements Runnable {
	protected Selector selector;
	protected ServerSocketChannel serverChannel = null;
	protected SocketChannel socketChannel = null;
	public static InetSocketAddress socketAddress;
	private int port = 50000;
	private boolean bstart = true;
	
	private final int THREAD_CNT = 5;
	private ExecutorService threadPool = null;
	
	protected boolean aceptLimit = false;
	protected ByteBuffer recvBuf;
	protected SelectionKey recvKey;

	public ServerCore(String ip,int p) {
		this.port = p;
		socketAddress = new InetSocketAddress(ip, port);
		threadPool = Executors.newFixedThreadPool(THREAD_CNT);
		this.recvBuf = ByteBuffer.allocate(1024);
		Thread thread = new Thread(this);
		thread.start();
	}
	
	@Override
	public void run() {
		try {
			selector = Selector.open();
			serverChannel = ServerSocketChannel.open();
			serverChannel.configureBlocking(false);
			serverChannel.socket().bind(socketAddress);
			serverChannel.setOption(StandardSocketOptions.SO_RCVBUF,128*1024);
			serverChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
			serverChannel.register(selector, SelectionKey.OP_ACCEPT);
		}
		catch (Exception e) {
			System.out.println("Server Run Error " + e.getMessage());
		}
		
		while(bstart) {
			try {
				selector.select();
				Iterator<SelectionKey> selectkeys = selector.selectedKeys().iterator();
				
				while(selectkeys.hasNext()) {
					SelectionKey key = selectkeys.next();
					selectkeys.remove();
					switch(key.interestOps()) {
					case SelectionKey.OP_ACCEPT:
						// Accept����.
						Accept(key);
						break;
					case SelectionKey.OP_READ:
						// Read����.
						ReadPacket(key);
						break;
					case SelectionKey.OP_WRITE:
						break;
					default:
						// �̻��� ���� ���ö� ��������
						disConnect(key);
						break;
					}
				}
			}
			catch (Exception e) {
			}
		}
		
	}
	
	protected void Accept(SelectionKey key) {
		// Ŭ���̾�Ʈ ������ 1000���̻� �Ѿ�� ���ް�
		if (selector.keys().size() > 1000) {
			aceptLimit = true;
		} else if (selector.keys().size() <= 1000) {
			aceptLimit = false;
		}
		if (!aceptLimit) {
			// ���� ���� SelectionKey�� Accept ����
			serverChannel = (ServerSocketChannel) key.channel();
			SocketChannel channel = null;
			try {
				// ä�ο� Accept �ϰ�,Non-Blocking, �б� ������ ���·� ���.
				channel = serverChannel.accept();
				channel.configureBlocking(false);
				channel.register(selector, SelectionKey.OP_READ);
				ChatServer.userMap.put(channel.socket().toString(), channel);
				
			} catch (Exception e) {
				System.out.println("Accept Error " + e.getMessage());
			}
		}

	}
	
	protected void ReadPacket(SelectionKey key) {
		// �������� Ű���� ä�η� ��ȯ.
		socketChannel = (SocketChannel) key.channel();
		recvBuf.clear();
		try {
			// RecvBuf �� �����͸� ������ ������·� ���.
			socketChannel.read(this.recvBuf);
			socketChannel.register(selector, SelectionKey.OP_WRITE);
			// key���� ���������͸� ������ �̺�Ʈ ó��.
			
			MessagePacker recvPacket = new MessagePacker(recvBuf.array());
			DisposeMessage(key, recvPacket, this);
		} catch (Exception ex) {
			disConnect(key);
		}
	}
	
	public void send(MessagePacker data,SelectionKey netid) {
		socketChannel = (SocketChannel)netid.channel();
		try {
			socketChannel.write(data.getBuffer());
			socketChannel.register(selector,SelectionKey.OP_READ);
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	protected void SendAll(MessagePacker data) {
		// �α����� ��� ���ؼ� ����
		Iterator<String> itr = ChatServer.userMap.keySet().iterator();
		while (itr.hasNext()) {
			try {
				// ä�ε� �ޱ�
				socketChannel = ChatServer.userMap.get((String) itr.next());
				socketChannel.write(data.getBuffer());
				socketChannel.register(selector, SelectionKey.OP_READ);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public void disConnect(SelectionKey netid) {
		SocketChannel disChannel = (SocketChannel)netid.channel();
		Iterator<String> itr = ChatServer.userMap.keySet().iterator();
		while(itr.hasNext()) {
			
			String disUser = itr.next();
			SocketChannel disKey = ChatServer.userMap.get(disUser);
			if(disKey.equals(disChannel)) {
				itr.remove();
				ChatServer.userMap.remove(disUser);
			}
		}
		try {
			disChannel.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		netid.attach(null);
		netid.cancel();
	}

	protected void StopServer() {
		
		ChatServer.userMap.clear();
		ChatServer.roomManager.clear();
		
		try {
			socketChannel.close();
			selector.close();
			serverChannel.close();
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
}
