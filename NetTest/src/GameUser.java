import java.net.SocketAddress;
import java.nio.channels.AsynchronousSocketChannel;

public class GameUser {
	private SocketAddress id;
	private GameRoom room;
	AsynchronousSocketChannel socket;
	
	public GameUser() {
		
	}
	public GameUser(SocketAddress id,AsynchronousSocketChannel socket) {
		this.id = id;
		this.socket = socket;
	}
	
	public void enterRoom(GameRoom room) {
		room.enterUser(this);
		this.room = room;
	}
	public void exitRoom(GameRoom room) {
		room.exitUser(this);
		this.room = null;
	}

	public String getId() {
		return id.toString();
	}
	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		if(obj == null || this.getClass() != obj.getClass())
			return false;
		
		GameUser gameUser = (GameUser)obj;
		return id == gameUser.id;
	}
}
