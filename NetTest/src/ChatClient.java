import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import MsgPacker.MessagePacker;
import MsgPacker.MessageProtocol;

public class ChatClient {
 
    public void startClient() throws IOException, InterruptedException {
 
    	
        InetSocketAddress hostAddress = new InetSocketAddress("127.0.0.1", 50000);
        SocketChannel client = SocketChannel.open(hostAddress);
 
        System.out.println("Client Started!");
        
        MessagePacker msg = new MessagePacker(); // MessagePacker ����غ���
        
        msg.SetProtocol(MessageProtocol.JOIN);
        msg.add("ä�� �޼��� ���� �׽�Ʈ�Դϴ�~~ This is test message for CHAT");
        msg.add(1204);
        msg.Finish();
        
        client.write(msg.getBuffer());
        client.close();
    }
    
    public static void main(String args[]) throws IOException, InterruptedException{
    	
    	ChatClient client = new ChatClient();
    	client.startClient();
    }
}