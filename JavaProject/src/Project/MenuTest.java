package Project;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MenuTest extends Frame {

	private final Frame f = this;
	MenuTest() {//기본 생성자(초기화)
		super("Java Project"); //프레임의 제목 설정(상위 클래스)
		setSize(500, 500);//프레임 가로, 세로
		
		//열린 윈도우 창 닫는 이벤트 등록
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0); //프로그램 종료
			}
		});
		
		//위치 지정
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize();
		setLocation(screenSize.width/2-getWidth()/2, screenSize.height/2-getHeight()/2);
		setIconImage(new ImageIcon("java-icon.png").getImage());
		
		MenuBar mb = new MenuBar(); //메뉴바(1개만)
		
		Menu mFile = new Menu("File"); //메뉴
		MenuItem miJoin = new MenuItem("Join",new MenuShortcut('J')); //회원가입 메뉴아이템
		miJoin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Login(f,"로그인");
			}
		});
		
		MenuItem miExit = new MenuItem("Exit",new MenuShortcut('E')); //Exit 메뉴아이템
		//awt에서의 단축키 설정 (Ctrl + E: File의 세부항목 Exit 생성)
		miExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//MenuItem miExit = (MenuItem)e.getSource();
				System.exit(0); //프로그램 종료
			}
		});
		
		Menu mMenu = new Menu("Menu"); //메뉴
		MenuItem miGraphicesTest = new MenuItem("GraphicesTest");
		miGraphicesTest.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new GraphicesTest("그래픽 테스트"); //GraphicesTest(문자열)생성자 호출!
			}
		});
		
		
		MenuItem miCoffee = new MenuItem("CoffeeMachine");
		miCoffee.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Coffee("커피 자판기");
			}
		});
		
		MenuItem miVending = new MenuItem("VendingMachine");
		miVending.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new VendingMachine("음료 자판기");
			}
		});
		
		MenuItem miCard = new MenuItem("CardLayout");
		miCard.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new CardLayoutEvent("카드 레이아웃");
			}
		});
		
		MenuItem mix_mas = new MenuItem("X-mas");
		mix_mas.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Christmas();
			}
		});
		
		
		Menu mMyMenu = new Menu("MyMenu"); //메뉴
		MenuItem miLifeGame = new MenuItem("LifeGame");
		miLifeGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new LifeGame();
			}
		});
		
		MenuItem miOmock = new MenuItem("오목");
		miOmock.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new OmockLogin();
			}
		});
		MenuItem miMyMenu3 = new MenuItem("나의 메뉴3");
		
		
		Menu mHelp = new Menu("Help"); //메뉴
		MenuItem miHelp = new MenuItem("도움말");
		miHelp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Help();
			}
		});
		
		
		
		//mFile메뉴에 메뉴아이템 추가
		mFile.add(miJoin);
		mFile.addSeparator();//구분선 추가
		mFile.add(miExit);
		
		//mMenu메뉴에 메뉴아이템 추가
		mMenu.add(miGraphicesTest);
		mMenu.add(miCoffee);
		mMenu.add(miVending);
		mMenu.add(miCard);
		mMenu.add(mix_mas);
		
		//mMyMenu메뉴에 메뉴아이템 추가
		mMyMenu.add(miLifeGame);
		mMyMenu.add(miOmock);
		mMyMenu.add(miMyMenu3);
		
		mHelp.add(miHelp);
		
		//메뉴바에 메뉴 추가
		mb.add(mFile);
		mb.add(mMenu);
		mb.add(mMyMenu);
		mb.add(mHelp);
		
		setMenuBar(mb);//프레임에 메뉴바 붙이기
		setVisible(true);//프레임 보이기
	}
	
	
	@Override
	public void paint(Graphics g) {
		Image image = Toolkit.getDefaultToolkit().getImage("java-logo.png");
		g.drawImage(image, 53, 75, this);
	}
	
	
	public static void main(String[] args) {
		MenuTest mainWin = new MenuTest();
		//new Login(mainWin,"로그인");
	}

}
