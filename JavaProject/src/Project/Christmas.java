package Project;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class back extends JPanel {
	private int nCase;
	Point pos = new Point(100, 100); // �����͸� ����

	back() {
		nCase = 0;
	}

	public void setnCase(int n) {// public Ŭ�������� �޾ƿ� ���� nCase�� �־���
		this.nCase = n;
		this.repaint();
	}

	public void paintComponent(Graphics g) {
		Image img = Toolkit.getDefaultToolkit().getImage("christmas.jpg"); // ����̹���
		g.drawImage(img, 0, 0, this);
		Image img1 = Toolkit.getDefaultToolkit().getImage("bbe.png"); // �̹����� �־���
		Image img2 = Toolkit.getDefaultToolkit().getImage("ge.png");
		Image img4 = Toolkit.getDefaultToolkit().getImage("rbe.png");
		Image img5 = Toolkit.getDefaultToolkit().getImage("re.png");
		Image img8 = Toolkit.getDefaultToolkit().getImage("ybe.png");
		Image img10 = Toolkit.getDefaultToolkit().getImage("roo.png");
		Image img11 = Toolkit.getDefaultToolkit().getImage("gift.png");
		Image ring = Toolkit.getDefaultToolkit().getImage("ring.png");
		Image bell = Toolkit.getDefaultToolkit().getImage("bell.png");
		Image santa = Toolkit.getDefaultToolkit().getImage("santa.png");
		Image santa1 = Toolkit.getDefaultToolkit().getImage("santa1.png");
		Image img9 = Toolkit.getDefaultToolkit().getImage("tree.png");
		Image tree1 = Toolkit.getDefaultToolkit().getImage("tree1.png");
		Image tree2 = Toolkit.getDefaultToolkit().getImage("tree2.png");
		Image roo1 = Toolkit.getDefaultToolkit().getImage("roo1.png");
		Image gift1 = Toolkit.getDefaultToolkit().getImage("gift1.png");

		switch (nCase) { // nCase�� ���� case�� ����
		case 1:
			g.drawImage(img9, 30, 30, this); // �̹����� �׷���
			g.drawImage(img1, 80, 140, this);
			g.drawImage(img2, 240, 240, this);
			g.drawImage(img5, 25, 330, this);
			g.drawImage(img11, 280, 320, this);
			g.drawImage(img8, 180, 280, this);
			g.drawImage(img4, 300, 280, this);
			g.drawImage(bell, 100, 280, this);
			g.drawImage(ring, 85, -30, this);
			g.drawImage(bell, 200, 200, this);
			break;
		case 2:
			g.drawImage(img10, 70, 30, this);
			g.drawImage(img5, 90, 160, this);
			g.drawImage(img2, 240, 160, this);
			g.drawImage(santa1, 200, 200, this);
			break;
		case 3:
			g.drawImage(tree2, -20, 120, this);
			g.drawImage(tree1, 300, 100, this);
			g.drawImage(santa, 170, 380, this);
			g.drawImage(roo1, 200, 360, this);
			g.drawImage(gift1, 150, 400, this);
			g.drawImage(gift1, 150, 410, this);
			g.drawImage(gift1, 140, 400, this);
			g.drawImage(gift1, 160, 410, this);
			break;
		case 4:
			g.drawImage(tree1, 100, 100, this);
			g.drawImage(img10, 20, 20, this);
			g.drawImage(roo1, 200, 260, this);
			g.drawImage(bell, 250, 150, this);
			break;
		case 5:
			g.drawImage(gift1, 20, 120, this);
			g.drawImage(img5, 300, 190, this);
			g.drawImage(santa, 70, 100, this);
			g.drawImage(santa, 100, 100, this);
			g.drawImage(santa, 130, 100, this);
			g.drawImage(santa, 160, 100, this);
			g.drawImage(santa, 190, 100, this);
			g.drawImage(santa, 210, 100, this);
			g.drawImage(roo1, 200, 360, this);
			g.drawImage(gift1, 150, 400, this);
			break;
		}
	}
}

public class Christmas extends JFrame {
	back bk = new back();

	public Christmas() {
		this.addWindowListener(new WindowAdapter() { // �ݱ�
			public void windowClosing(WindowEvent we) {
				dispose();
			}
		});
		
		
		this.setSize(650, 500);
		// ��ġ ����
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize();
		this.setLocation(screenSize.width / 2 - this.getWidth() / 2, screenSize.height / 2 - this.getHeight() / 2);
		this.setTitle("Christmas");
		this.setVisible(true);
		JPanel jp = new JPanel();
		jp.setLayout(new GridLayout(5, 1)); // ��ư�� �׸��巹�̾ƿ����� �迭�� ����
		ButtonGroup grp = new ButtonGroup();

		JRadioButton rb1 = new JRadioButton("Ʈ��1");
		JRadioButton rb2 = new JRadioButton("Ʈ��2");
		JRadioButton rb3 = new JRadioButton("Ʈ��3");
		JRadioButton rb4 = new JRadioButton("Ʈ��4");
		JRadioButton rb5 = new JRadioButton("Ʈ��5");

		jp.setBackground(Color.white);
		rb1.setBackground(Color.white);
		rb2.setBackground(Color.white);
		rb3.setBackground(Color.white);
		rb4.setBackground(Color.white);
		rb5.setBackground(Color.white);

		grp.add(rb1);
		grp.add(rb2);
		grp.add(rb3);
		grp.add(rb4);
		grp.add(rb5);
		jp.add(rb1);
		jp.add(rb2);
		jp.add(rb3);
		jp.add(rb4);
		jp.add(rb5);
		rb1.addActionListener(al); // �׼Ǹ����� �߰�
		rb2.addActionListener(al);
		rb3.addActionListener(al);
		rb4.addActionListener(al);
		rb5.addActionListener(al);

		JPanel menu = new JPanel();
		menu.setLayout(new BorderLayout());
		menu.add(jp, BorderLayout.CENTER);

		Container ct = getContentPane();
		ct.setLayout(new BorderLayout());
		ct.add(menu, BorderLayout.EAST);
		ct.add(bk, BorderLayout.CENTER);
	}

	ActionListener al = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("Ʈ��1")) { // ���� ��ư�� Ʈ��1�� ������ back��setnCase�� ��
				System.out.println("Ʈ��1 ����");
				bk.setnCase(1);
			} else if (e.getActionCommand().equals("Ʈ��2")) {
				System.out.println("Ʈ��2 ����");
				bk.setnCase(2);
			} else if (e.getActionCommand().equals("Ʈ��3")) {
				System.out.println("Ʈ��3 ����");
				bk.setnCase(3);
			} else if (e.getActionCommand().equals("Ʈ��4")) {
				System.out.println("Ʈ��4 ����");
				bk.setnCase(4);
			} else if (e.getActionCommand().equals("Ʈ��5")) {
				System.out.println("Ʈ��5 ����");
				bk.setnCase(5);
			}
		}
	};
}