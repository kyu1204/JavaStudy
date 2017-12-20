import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import MsgPacker.MessagePacker;
import MsgPacker.MessageProtocol;

public class ReadyRoom {
    	int roomkey;
    	Frame rf;
    	Frame parent;
    	Panel bPanel;
    	Button bReady;
    	Button bCancel;
    	ChatClient client;
    	public List member;
    	public ReadyRoom(int key,String title,Frame parent,ChatClient c) {
    		roomkey = key;
    		member = new List();
    		this.client = c;
    		rf = new Frame(title);
    		bPanel = new Panel();
    		bPanel.setLayout(new FlowLayout());
    		bReady = new Button("준비");
    		bCancel = new Button("나가기");
    		bPanel.add(bReady);
    		bPanel.add(bCancel);
  
    		rf.addWindowListener(new WindowAdapter() {
    			@Override
    			public void windowClosing(WindowEvent e) {
    				parent.setVisible(true);
    				
    				MessagePacker data = new MessagePacker();
    				data.SetProtocol(MessageProtocol.READYLEAVE);
    				data.add(roomkey);
    				data.Finish();
    				client.send(data);
    				//client.readyflag = false;
    				rf.dispose();
    			}
    		});
    		rf.add(member,"Center");
    		rf.add(bPanel,"South");
    		rf.setSize(500, 500);
    		rf.setVisible(true);
    	}

    }
