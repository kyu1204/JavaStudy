package Project;

import java.awt.*;
import java.awt.event.*;

public class OmockVictory{
	public OmockVictory(Frame parent,Stone stone) {
		Dialog view = new Dialog(parent, "½Â¸®", true);
		Label l1 = new Label("",Label.CENTER);
		if(stone == Stone.BLACK)
			l1.setText("°ËÁ¤ µ¹ ½Â¸®!");
		else if(stone == Stone.WHITE)
			l1.setText("ÇÏ¾á µ¹ ½Â¸®!");
		
		Button ok = new Button("È®ÀÎ");
		ok.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				parent.dispose();
				view.dispose();
			}
		});
		
		view.setSize(300, 250);
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize();
		view.setLocation(screenSize.width/2-view.getWidth()/2, screenSize.height/2-view.getHeight()/2);
		view.setLayout(new BorderLayout());
		view.add(l1,"Center");
		view.add(ok, "South");
		view.setVisible(true);
	}

}
