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

		// 프레임 위치 지정
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize();
		info.setLocation(screenSize.width / 2 - 70, screenSize.height / 2 - 45);

		info.setSize(350, 90); // 프레임 사이즈
		info.setLayout(new FlowLayout()); // 레이아웃
		Label msg = new Label(message, Label.CENTER);
		Button ok = new Button("OK"); // 버튼 생성
		// 로그인할 때 OK버튼 클릭시, 창이 닫히는 이벤트
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				info.dispose();
			}
		});
		info.add(msg); // 메시지 추가
		info.add(ok); // 버튼 추가
		// parent.setVisible(true);
		info.setVisible(true);
	}
}
