package Project;

import java.awt.*;
import java.awt.event.*;

// 로그인
public class Login extends Frame {
	private Label lid = null;
	private Label lpwd = null;
	private TextField tfId = null;
	private TextField tfPwd = null;
	private Button ok = null;
	private final Frame f = this;
	private static Frame parent = null;

	public Login(Frame parent, String title) {
		super(title); // 프레임 상속
		this.parent = parent;

		// 윈도우 창 닫는 이벤트
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				System.exit(0);
			}
		});
		// 로그인 창 위치 지정
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize();
		setLocation(screenSize.width / 2 - 225, screenSize.height / 2 - 33);

		lid = new Label("ID :", Label.RIGHT); // 컴포넌트 오른쪽 정렬
		lpwd = new Label("Password :", Label.RIGHT);
		tfId = new TextField(10); // 텍스트 필드, 글자수 지정
		tfPwd = new TextField(10);
		tfPwd.setEchoChar('*'); // 비밀번호 *로 표시 위함
		ok = new Button("OK");

		// 이벤트 등록
		tfId.addActionListener(new EventHandler());
		tfPwd.addActionListener(new EventHandler());
		ok.addActionListener(new EventHandler());
		setLayout(new FlowLayout()); // 레이아웃(배치)
		add(lid); // 레이블 추가
		add(tfId); // 텍스트 필드 추가
		add(lpwd); // 레이블 추가
		add(tfPwd); // 텍스트 필드 추가
		add(ok); // OK버튼 추가(로그인)

		setSize(450, 70); // 로그인 프레임 사이즈
		setVisible(true); // 프레임 보이도록 함
	}

	class EventHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) { // 이벤트 처리기
			String id = tfId.getText();
			String password = tfPwd.getText();

			if (id.equals("admin")) { // 로그인 시, ID, PASSWORD 비교
				if (password.equals("admin")) {
					msg md = new msg(f, id + "님, 성공적으로 로그인 되었습니다.");
					f.setVisible(false); // 다이얼로그 화면에 보여주지 않음
					f.dispose(); // 메모리에서 제거
				} else { // 패스워드 잘못 입력했을 때, 경고 메시지 출력
					msg md = new msg(f, "입력하신 비밀번호가 틀렸습니다. 다시 입력해 주시기 바랍니다.");
					tfPwd.requestFocus(); // 메시지를 보낸 후, 포커스를 password 텍스트 필드로 주기 위함
					tfPwd.selectAll(); // password 전체 선택함
				}
			} else if (id.equals("admin2")) {
				if (password.equals("admin2")) {
					msg md = new msg(f, id + "님, 성공적으로 로그인 되었습니다.");
					f.setVisible(false);
					f.dispose();
				} else {
					msg md = new msg(f, "입력하신 비밀번호가 틀렸습니다. 다시 입력해 주시기 바랍니다.");
					tfPwd.requestFocus();
					tfPwd.selectAll();
				}
			} else {
				msg md = new msg(f, "입력하신 id가 유효하지 않습니다. 다시 입력해 주세요.");
				tfId.requestFocus();
				tfId.selectAll();
			}
		}
	}
};
