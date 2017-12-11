package Project;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class LifeGameHelp extends Dialog {
	public LifeGameHelp(Frame parent, String title, boolean modal) {
		super(parent, title, modal);

		setLayout(new FlowLayout());
		setSize(200, 160);
		setResizable(false);
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize();
		setLocation(screenSize.width / 2 - getWidth() / 2, screenSize.height / 2 - getHeight() / 2);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});
		
		Label l1 = new Label("Left_Click: Create Cell");
		Label l2 = new Label("Right_Click: Start or Stop");
		Label l3 = new Label("Ctrl + S: Rate of growth");
		Label l4 = new Label("Ctrl + C: Choose Color");
		

		add(l1);
		add(l2);
		add(l3);
		add(l4);
	}

}
