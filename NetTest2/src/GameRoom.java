import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

import MsgPacker.MessagePacker;

public class GameRoom {

	private int roomkey;
	private String roomFullName;
	private String roomName;
	private ArrayList<SelectionKey> userList;
	
	public GameRoom() {
		roomFullName = "Test";
	}
	
	public GameRoom(int roomkey,String roomName,SelectionKey netid) {
		userList = new ArrayList<SelectionKey>();
		userList.add(netid);
		this.roomkey = roomkey;
		this.roomName = roomName;
		this.roomFullName = roomName+"("+userList.size()+"/2)";
		
	}
	
	public void ReName() {
		this.roomFullName = roomName+"("+userList.size()+"/2)";	
	}
	
	public void broadcast(byte protocol) {
		MessagePacker broad = new MessagePacker();
		broad.SetProtocol(protocol);
		broad.add(roomkey);
		broad.add(roomName);
		broad.add(userList.size());
		for(SelectionKey user: userList) {
			broad.add(user.toString());
		}
		broad.Finish();
		for(SelectionKey user: userList) {
			SocketChannel sc = (SocketChannel) user.channel();
			Selector selc = (Selector)user.selector();
			try {
				sc.write(broad.getBuffer());
				sc.register(selc, SelectionKey.OP_READ);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public void exitUser(SelectionKey netid) {
		userList.remove(netid);
		
		if(userList.size()<1) {
			ChatServer.roomManager.remove(roomkey);
			ChatServer.roomCnt--;
			return;
		}
	}
	public void enterUser(SelectionKey netid) {
		userList.add(netid);
	}
	public int getNumberOfPeople() {
		return userList.size();
	}
	public String getName() {
		return roomFullName;
	}
}
