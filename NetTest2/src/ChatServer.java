import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.text.DecimalFormat;
import java.util.HashMap;

import MsgPacker.MessagePacker;
import MsgPacker.MessageProtocol;

import java.awt.*;
import java.awt.event.*;

public class ChatServer{
	public static HashMap<Integer,GameRoom> roomManager;
	public static HashMap<String,SocketChannel> userMap;
	public static ServerCore Core = null;
	public static Frame mainGui = null;
	public static int roomCnt = 0;
	
	public ChatServer() {
		
	}
	
	public void ServerStart(String ip,int port) {
		Core = new ServerCore(ip,port);
		roomManager = new HashMap<Integer,GameRoom>();
		roomManager.put(++roomCnt, new GameRoom());
		userMap = new HashMap<String,SocketChannel>();
	}
	public void ServerStop() {
		
	}
	
	public void DisposeMessage(SelectionKey netid,MessagePacker data,ServerCore core) {
		
		byte protocol = data.getProtocol();
		
		switch (protocol) {
		case MessageProtocol.LOGIN: {
			MessagePacker reply = new MessagePacker();
			reply.SetProtocol(MessageProtocol.LOGIN);
			reply.add(roomManager.size());
			for (int roomkey : roomManager.keySet()) {
				reply.add(roomManager.get(roomkey).getName());
			}
			reply.Finish();
			core.send(reply,netid);
			break;
		}
		case MessageProtocol.CLOSE: {
			core.disConnect(netid);
			break;
		}

		case MessageProtocol.CREATE: {
			String roomname = data.getString();
			++roomCnt;
			roomname = "No." + roomCnt + " " + roomname + " ";
			int roomkey = roomCnt;
			GameRoom gr = new GameRoom(roomkey, roomname, netid);
			roomManager.put(roomkey, gr);

			/*
			MessagePacker reply = new MessagePacker();
			reply.SetProtocol(MessageProtocol.CREATE);
			reply.add(roomkey);
			reply.add(roomname);
			reply.Finish();
			core.send(reply,netid);
			*/

			gr.broadcast(MessageProtocol.JOIN);
			
			try {
				Thread.sleep(100);
			} 
			catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			MessagePacker broadcast = new MessagePacker();
			broadcast.SetProtocol(MessageProtocol.LOGIN);
			broadcast.add(roomManager.size());
			for (int item : roomManager.keySet()) {
				broadcast.add(roomManager.get(item).getName());
			}
			broadcast.Finish();
			
			core.SendAll(broadcast);

			break;
		}

		case MessageProtocol.JOIN: {
			int roomkey = data.getInt();
			GameRoom gr = roomManager.get(roomkey);
			if (gr.getNumberOfPeople() < 2) {
				gr.enterUser(netid);
				gr.ReName();
				gr.broadcast(MessageProtocol.JOIN);
			}
			
			MessagePacker broadcast = new MessagePacker();
			broadcast.SetProtocol(MessageProtocol.LOGIN);
			broadcast.add(roomManager.size());
			for (int item : roomManager.keySet()) {
				broadcast.add(roomManager.get(item).getName());
			}
			broadcast.Finish();
			
			core.SendAll(broadcast);
			break;
		}

		case MessageProtocol.READYLEAVE: {
			int roomkey = data.getInt();
			GameRoom room = roomManager.get(roomkey);
			room.exitUser(netid);
			
			if(room.getNumberOfPeople()>=1) {
				room.broadcast(MessageProtocol.JOIN);
				MessagePacker reply = new MessagePacker();
				reply.SetProtocol(MessageProtocol.LOGIN);
				reply.add(roomManager.size());
				for (int item : roomManager.keySet()) {
					reply.add(roomManager.get(item).getName());
				}
				reply.Finish();
				core.send(reply, netid);
			}
			else {
				MessagePacker broadcast = new MessagePacker();
				broadcast.SetProtocol(MessageProtocol.LOGIN);
				broadcast.add(roomManager.size());
				for (int item : roomManager.keySet()) {
					broadcast.add(roomManager.get(item).getName());
				}
				broadcast.Finish();
				core.SendAll(broadcast);
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
	}	
}
@SuppressWarnings("serial")
class ServerGUI extends Frame implements ActionListener//,Runnable
{
	
	Button start = new Button("     시작     ");
	Button stop = new Button("     정지     ");
	Panel bPanel = new Panel();
	TextArea log = new TextArea();
	
	private Frame MainFrame = new Frame("OmokServer");
	private boolean serverflag = false;
	private ChatServer server = null;
	private String ip;
	private int port;
	
	public ServerGUI(String ip,int port) {
		this.ip = ip;
		this.port = port;
		bPanel.setLayout(new FlowLayout());
		bPanel.add(start);
		bPanel.add(stop);
		start.addActionListener(this);
		stop.addActionListener(this);
		
		MainFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		MainFrame.add(bPanel,"North");
		MainFrame.add(log,"Center");
		MainFrame.setSize(500,800);
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize();
		MainFrame.setLocation(screenSize.width/2-MainFrame.getWidth()/2, screenSize.height/2-MainFrame.getHeight()/2);
		MainFrame.setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if(o.equals(start))
		{
			if (serverflag == false) {
				log.append("IP:" + ip + "\nPort:" + port + "\n서버 시작\n");
				server = new ChatServer();
				server.ServerStart(ip,port);
				serverflag = true;
				
				//Thread outPut = new Thread(this);
				//outPut.start();
			}
			else {
				log.append("서버가 이미 구동중입니다!\n");
			}
			
		}
		else if(o.equals(stop)) {
			if(serverflag == true) {
				System.exit(0);
				//ChatServer.Core.StopServer();
				//serverflag = false;
			}
		}
	}
	
	/*
	@Override
	public void run() {
		Runtime Memory = Runtime.getRuntime();
		DecimalFormat
	};
	*/
}