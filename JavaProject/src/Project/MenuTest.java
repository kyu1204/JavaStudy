package Project;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MenuTest extends Frame {

	private final Frame f = this;
	MenuTest() {//�⺻ ������(�ʱ�ȭ)
		super("Java Project"); //�������� ���� ����(���� Ŭ����)
		setSize(500, 500);//������ ����, ����
		
		//���� ������ â �ݴ� �̺�Ʈ ���
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0); //���α׷� ����
			}
		});
		
		//��ġ ����
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize();
		setLocation(screenSize.width/2-getWidth()/2, screenSize.height/2-getHeight()/2);
		setIconImage(new ImageIcon("java-icon.png").getImage());
		
		MenuBar mb = new MenuBar(); //�޴���(1����)
		
		Menu mFile = new Menu("File"); //�޴�
		MenuItem miJoin = new MenuItem("Join",new MenuShortcut('J')); //ȸ������ �޴�������
		miJoin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Login(f,"�α���");
			}
		});
		
		MenuItem miExit = new MenuItem("Exit",new MenuShortcut('E')); //Exit �޴�������
		//awt������ ����Ű ���� (Ctrl + E: File�� �����׸� Exit ����)
		miExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//MenuItem miExit = (MenuItem)e.getSource();
				System.exit(0); //���α׷� ����
			}
		});
		
		Menu mMenu = new Menu("Menu"); //�޴�
		MenuItem miGraphicesTest = new MenuItem("GraphicesTest");
		miGraphicesTest.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new GraphicesTest("�׷��� �׽�Ʈ"); //GraphicesTest(���ڿ�)������ ȣ��!
			}
		});
		
		
		MenuItem miCoffee = new MenuItem("CoffeeMachine");
		miCoffee.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Coffee("Ŀ�� ���Ǳ�");
			}
		});
		
		MenuItem miVending = new MenuItem("VendingMachine");
		miVending.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new VendingMachine("���� ���Ǳ�");
			}
		});
		
		MenuItem miCard = new MenuItem("CardLayout");
		miCard.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new CardLayoutEvent("ī�� ���̾ƿ�");
			}
		});
		
		MenuItem mix_mas = new MenuItem("X-mas");
		mix_mas.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Christmas();
			}
		});
		
		
		Menu mMyMenu = new Menu("MyMenu"); //�޴�
		MenuItem miLifeGame = new MenuItem("LifeGame");
		miLifeGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new LifeGame();
			}
		});
		
		MenuItem miOmock = new MenuItem("����");
		miOmock.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new OmockLogin();
			}
		});
		MenuItem miMyMenu3 = new MenuItem("���� �޴�3");
		
		
		Menu mHelp = new Menu("Help"); //�޴�
		MenuItem miHelp = new MenuItem("����");
		miHelp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Help();
			}
		});
		
		
		
		//mFile�޴��� �޴������� �߰�
		mFile.add(miJoin);
		mFile.addSeparator();//���м� �߰�
		mFile.add(miExit);
		
		//mMenu�޴��� �޴������� �߰�
		mMenu.add(miGraphicesTest);
		mMenu.add(miCoffee);
		mMenu.add(miVending);
		mMenu.add(miCard);
		mMenu.add(mix_mas);
		
		//mMyMenu�޴��� �޴������� �߰�
		mMyMenu.add(miLifeGame);
		mMyMenu.add(miOmock);
		mMyMenu.add(miMyMenu3);
		
		mHelp.add(miHelp);
		
		//�޴��ٿ� �޴� �߰�
		mb.add(mFile);
		mb.add(mMenu);
		mb.add(mMyMenu);
		mb.add(mHelp);
		
		setMenuBar(mb);//�����ӿ� �޴��� ���̱�
		setVisible(true);//������ ���̱�
	}
	
	
	@Override
	public void paint(Graphics g) {
		Image image = Toolkit.getDefaultToolkit().getImage("java-logo.png");
		g.drawImage(image, 53, 75, this);
	}
	
	
	public static void main(String[] args) {
		MenuTest mainWin = new MenuTest();
		//new Login(mainWin,"�α���");
	}

}
