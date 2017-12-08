package Project;

import java.awt.*;
import java.awt.event.*;

// �α���
public class Login extends Frame {
	private Label lid = null;
	private Label lpwd = null;
	private TextField tfId = null;
	private TextField tfPwd = null;
	private Button ok = null;
	private final Frame f = this;
	private static Frame parent = null;

	public Login(Frame parent, String title) {
		super(title); // ������ ���
		this.parent = parent;

		// ������ â �ݴ� �̺�Ʈ
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				System.exit(0);
			}
		});
		// �α��� â ��ġ ����
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize();
		setLocation(screenSize.width / 2 - 225, screenSize.height / 2 - 33);

		lid = new Label("ID :", Label.RIGHT); // ������Ʈ ������ ����
		lpwd = new Label("Password :", Label.RIGHT);
		tfId = new TextField(10); // �ؽ�Ʈ �ʵ�, ���ڼ� ����
		tfPwd = new TextField(10);
		tfPwd.setEchoChar('*'); // ��й�ȣ *�� ǥ�� ����
		ok = new Button("OK");

		// �̺�Ʈ ���
		tfId.addActionListener(new EventHandler());
		tfPwd.addActionListener(new EventHandler());
		ok.addActionListener(new EventHandler());
		setLayout(new FlowLayout()); // ���̾ƿ�(��ġ)
		add(lid); // ���̺� �߰�
		add(tfId); // �ؽ�Ʈ �ʵ� �߰�
		add(lpwd); // ���̺� �߰�
		add(tfPwd); // �ؽ�Ʈ �ʵ� �߰�
		add(ok); // OK��ư �߰�(�α���)

		setSize(450, 70); // �α��� ������ ������
		setVisible(true); // ������ ���̵��� ��
	}

	class EventHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) { // �̺�Ʈ ó����
			String id = tfId.getText();
			String password = tfPwd.getText();

			if (id.equals("admin")) { // �α��� ��, ID, PASSWORD ��
				if (password.equals("admin")) {
					msg md = new msg(f, id + "��, ���������� �α��� �Ǿ����ϴ�.");
					f.setVisible(false); // ���̾�α� ȭ�鿡 �������� ����
					f.dispose(); // �޸𸮿��� ����
				} else { // �н����� �߸� �Է����� ��, ��� �޽��� ���
					msg md = new msg(f, "�Է��Ͻ� ��й�ȣ�� Ʋ�Ƚ��ϴ�. �ٽ� �Է��� �ֽñ� �ٶ��ϴ�.");
					tfPwd.requestFocus(); // �޽����� ���� ��, ��Ŀ���� password �ؽ�Ʈ �ʵ�� �ֱ� ����
					tfPwd.selectAll(); // password ��ü ������
				}
			} else if (id.equals("admin2")) {
				if (password.equals("admin2")) {
					msg md = new msg(f, id + "��, ���������� �α��� �Ǿ����ϴ�.");
					f.setVisible(false);
					f.dispose();
				} else {
					msg md = new msg(f, "�Է��Ͻ� ��й�ȣ�� Ʋ�Ƚ��ϴ�. �ٽ� �Է��� �ֽñ� �ٶ��ϴ�.");
					tfPwd.requestFocus();
					tfPwd.selectAll();
				}
			} else {
				msg md = new msg(f, "�Է��Ͻ� id�� ��ȿ���� �ʽ��ϴ�. �ٽ� �Է��� �ּ���.");
				tfId.requestFocus();
				tfId.selectAll();
			}
		}
	}
};
