

import java.awt.*;
import java.awt.event.*;

public class Alram extends Dialog{
	public Alram(Frame parent,String title,boolean modal) {
		super(parent,title,modal);
		Button ok = new Button("확인");
		ok.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				System.exit(0);
			}
		});
		Panel bPanel = new Panel();
		bPanel.setLayout(new FlowLayout());
		bPanel.add(ok);
		add(new Label("     서버가 꺼져 있습니다!"), "Center");
		add(bPanel, "South");
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
				System.exit(0);
			}
		});
		setSize(150, 150);
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize();
		setLocation(screenSize.width / 2 - getWidth() / 2,
				screenSize.height / 2 - getHeight() / 2);
		setVisible(true);
	}
	
}
