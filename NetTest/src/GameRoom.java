import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;
import java.util.*;

import MsgPacker.MessagePacker;
import MsgPacker.MessageProtocol;


public class GameRoom {

	private int id;
	private List<GameUser> userList;
	private GameUser roomOwner;
	private String roomName;
	
	public GameRoom(int roomId) {
		this.id = roomId;
		userList = new ArrayList();
	}
	public GameRoom(int roomID,String roomName) {
		userList = new ArrayList();
		this.id = roomID;
		this.roomName = roomName+"("+userList.size()+"/2)";
	}
	public GameRoom(int roomID,String roomName, GameUser user) {
		userList = new ArrayList<GameUser>();
		user.enterRoom(this);
		this.id = roomID;
		this.roomOwner = user;
		this.roomName = roomName+"("+userList.size()+"/2)";
	}
	public void enterUser(GameUser user) {
		userList.add(user);
	}
	public void exitUser(GameUser user) {
		userList.remove(user);
		
		if(userList.size()<1) {
			Server.roomManager.remove(id);
			Server.roomcount--;
			return;
		}
		
		if(userList.size()<2) {
			this.roomOwner = userList.get(0);
			return;
		}
	}
	public void close() {
		for(GameUser user: userList) {
			user.exitRoom(this);
		}
		this.userList.clear();
		this.userList = null;
		Server.roomcount--;
	}
	
	///////////////////////////////
	
	public void broadcast(byte protocol) {
		MessagePacker broad = new MessagePacker();
		broad.SetProtocol(protocol);
		broad.add(id);
		broad.add(roomName);
		broad.add(userList.size());
		for(GameUser user: userList) {
			broad.add(user.getId());
		}
		broad.Finish();
		for(GameUser user: userList) {
			user.socket.write(broad.getBuffer());
		}
	}
	public int getNumberOfPeople() {
		return userList.size();
	}
	public int getID() {
		return id;
	}
	public String getName() {
		return roomName;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		if (obj == null || this.getClass() != obj.getClass())
			return false;
		
		GameRoom gameRoom = (GameRoom) obj;
		return id == gameRoom.id;
	}
	@Override
	public int hashCode() {
		return id;
	}
}
