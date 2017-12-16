package Project;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class OmockLogin extends Frame{

	Button single;
	Button multi;
	OmockLogin()
	{
		super("Login");
		
		setIconImage(new ImageIcon("5mock.jpg").getImage());
		setSize(300,300);
		setLayout(null);
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize();
		setLocation(screenSize.width/2-getWidth()/2, screenSize.height/2-getHeight()/2);
		
		single = new Button("싱글 모드");
		single.setBounds(100, 80, 100, 50);
		single.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Omock();
				dispose();
			}
		});
		
		multi = new Button("멀티 모드");
		multi.setBounds(100, 180, 100, 50);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});
		
		add(single);
		add(multi);
		setVisible(true);
	}
}
