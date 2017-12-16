import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;

import MsgPacker.MessagePacker;
import MsgPacker.MessageProtocol;

public class OmokClient {
	Frame main;
	List roomview = new List();
	Panel bPanel = new Panel();
	Button createroom = new Button("방 만들기");
	Button joinroom = new Button("들어가기");
	
	ArrayList<String> room = new ArrayList<>();
	SocketChannel client;
	public OmokClient() {
		main = new Frame("Omok");
		
		bPanel.setLayout(new FlowLayout(FlowLayout.CENTER,50,30));
		bPanel.add(createroom);
		bPanel.add(joinroom);
		
		
		main.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					MessagePacker msg = new MessagePacker();
					msg.SetProtocol(MessageProtocol.CLOSE);
					msg.Finish();
					client.write(msg.getBuffer());
					client.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				System.exit(0);
			}
		});
		main.add(roomview, "Center");
		main.add(bPanel, "South");
		main.setSize(500,500);
		main.setVisible(true);
	}

    public void startClient(String ip,String port) throws IOException, InterruptedException {
        InetSocketAddress hostAddress = new InetSocketAddress(ip, Integer.parseInt(port));
        client = SocketChannel.open(hostAddress);
        
        {
        	MessagePacker msg = new MessagePacker(); // MessagePacker 사용해보자
        
        	msg.SetProtocol(MessageProtocol.LOGIN);
        	msg.Finish();
        
        	client.write(msg.getBuffer());
        }
        {
        	ByteBuffer bf = ByteBuffer.allocate(1024);
        	client.read(bf);
        	MessagePacker msg = new MessagePacker(bf.array());
        	byte protocol = msg.getProtocol();
        	if(protocol == MessageProtocol.LOGIN) {
        		int size = msg.getInt();
				for (int i = 0; i < size; ++i) {
					room.add(msg.getString());
				}
				for(String item:room) {
					roomview.removeAll();
					roomview.add(item);
				}
        	}	
        }
        	
        
    }
}