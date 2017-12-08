package Project;

import java.awt.*;
import java.awt.event.*;

public class msg {
	public msg(Frame parent, String message) {
		final Dialog info = new Dialog(parent, "Login Information", true);

		info.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				info.setVisible(false);
				info.dispose();
			}
		});

		// ������ ��ġ ����
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize();
		info.setLocation(screenSize.width / 2 - 70, screenSize.height / 2 - 45);

		info.setSize(350, 90); // ������ ������
		info.setLayout(new FlowLayout()); // ���̾ƿ�
		Label msg = new Label(message, Label.CENTER);
		Button ok = new Button("OK"); // ��ư ����
		// �α����� �� OK��ư Ŭ����, â�� ������ �̺�Ʈ
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				info.dispose();
			}
		});
		info.add(msg); // �޽��� �߰�
		info.add(ok); // ��ư �߰�
		// parent.setVisible(true);
		info.setVisible(true);
	}
}
