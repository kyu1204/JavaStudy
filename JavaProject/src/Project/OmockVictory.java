package Project;

import java.awt.*;
import java.awt.event.*;

public class OmockVictory{
	public OmockVictory(Frame parent,Stone stone) {
		Dialog view = new Dialog(parent, "�¸�", true);
		Label l1 = new Label("",Label.CENTER);
		if(stone == Stone.BLACK)
			l1.setText("���� �� �¸�!");
		else if(stone == Stone.WHITE)
			l1.setText("�Ͼ� �� �¸�!");
		
		Button ok = new Button("Ȯ��");
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
