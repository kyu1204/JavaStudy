package Project;

import java.awt.*;
import java.awt.event.*;

public class VendingMachine extends Frame{

	int realmoney=0;
	String drink;
	
	Image img = null;
	Image mi1,mi2,mi3,mi4,mi5,mi6;
	Panel menus;
	MenuImage menu1,menu2,menu3,menu4,menu5,menu6;
	Panel payled;
	Label pay;
	Label won;
	Panel moneys;
	Button money_100;
	Button money_500;
	Button money_1000;
	Button money_5000;
	Button but_return;
	TextArea bill;
	
	
	Button but1,but2,but3,but4,but5,but6;
	
	TextField led;
	public VendingMachine(String title)
	{
		super(title);
		
		menus = new Panel();
		menus.setBounds(50, 50, 400, 400);
		menus.setLayout(new GridLayout(2, 3, 5, 5));
		menus.setBackground(Color.lightGray);
		
		menu1 = new MenuImage();
		menu1.setLayout(new BorderLayout());
		menu1.setBackground(Color.white);
		menu1.getImage("cola.png");
		
		menu2 = new MenuImage();
		menu2.setLayout(new BorderLayout());
		menu2.setBackground(Color.white);
		menu2.getImage("cider.jpg");
		
		menu3 = new MenuImage();
		menu3.setLayout(new BorderLayout());
		menu3.setBackground(Color.white);
		menu3.getImage("power.jpg");
		
		menu4 = new MenuImage();
		menu4.setLayout(new BorderLayout());
		menu4.setBackground(Color.white);
		menu4.getImage("letsbe.jpg");
		
		menu5 = new MenuImage();
		menu5.setLayout(new BorderLayout());
		menu5.setBackground(Color.white);
		menu5.getImage("3dasu.jpg");
		
		menu6= new MenuImage();
		menu6.setLayout(new BorderLayout());
		menu6.setBackground(Color.white);
		menu6.getImage("vitamin.jpg");
		
		but1 = new Button("콜라 - 1000원");
		but1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				drink = "콜라";
				if(realmoney < 1000)
				{
					bill.setText("");
					bill.setText("잔액이 부족합니다.");
				}
				else
				{
					realmoney -= 1000;
					bill.setText("");
					bill.setText("음료: "+drink+"\n금액: 1000 ￦\n잔액: "+realmoney+" ￦");
					pay.setText(String.valueOf(realmoney));
				}
			}
		});
		menu1.add(but1,"South");
		but2 = new Button("사이다 - 700원");
		but2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				drink = "사이다";
				if(realmoney < 700)
				{
					bill.setText("");
					bill.setText("잔액이 부족합니다.");
				}
				else
				{
					realmoney -= 700;
					bill.setText("");
					bill.setText("음료: "+drink+"\n금액: 700 ￦\n잔액: "+realmoney+" ￦");
					pay.setText(String.valueOf(realmoney));
				}
			}
		});
		menu2.add(but2,"South");
		but3 = new Button("파워에이드 - 800원");
		but3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				drink = "파워에이드";
				if(realmoney < 800)
				{
					bill.setText("");
					bill.setText("잔액이 부족합니다.");
				}
				else
				{
					realmoney -= 800;
					bill.setText("");
					bill.setText("음료: "+drink+"\n금액: 800 ￦\n잔액: "+realmoney+" ￦");
					pay.setText(String.valueOf(realmoney));
				}
			}
		});
		menu3.add(but3,"South");
		but4 = new Button("레쓰비 - 600원");
		but4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				drink = "레쓰비";
				if(realmoney < 600)
				{
					bill.setText("");
					bill.setText("잔액이 부족합니다.");
				}
				else
				{
					realmoney -= 600;
					bill.setText("");
					bill.setText("음료: "+drink+"\n금액: 600 ￦\n잔액: "+realmoney+" ￦");
					pay.setText(String.valueOf(realmoney));
				}
			}
		});
		menu4.add(but4,"South");
		but5 = new Button("삼다수 - 500원");
		but5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				drink = "삼다수";
				if(realmoney < 500)
				{
					bill.setText("");
					bill.setText("잔액이 부족합니다.");
				}
				else
				{
					realmoney -= 500;
					bill.setText("");
					bill.setText("음료: "+drink+"\n금액: 500 ￦\n잔액: "+realmoney+" ￦");
					pay.setText(String.valueOf(realmoney));
				}
			}
		});
		menu5.add(but5,"South");
		but6 = new Button("비타민워터 - 1300원");
		but6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				drink = "비타민워터";
				if(realmoney < 1300)
				{
					bill.setText("");
					bill.setText("잔액이 부족합니다.");
				}
				else
				{
					realmoney -= 1300;
					bill.setText("");
					bill.setText("음료: "+drink+"\n금액: 1300 ￦\n잔액: "+realmoney+" ￦");
					pay.setText(String.valueOf(realmoney));
				}
			}
		});
		menu6.add(but6,"South");
		
		menus.add(menu1);
		menus.add(menu2);
		menus.add(menu3);
		menus.add(menu4);
		menus.add(menu5);
		menus.add(menu6);
		
		payled = new Panel();
		payled.setBounds(50, 460, 400, 60);
		payled.setBackground(Color.black);
		payled.setLayout(null);
		pay = new Label(String.valueOf(realmoney));
		pay.setBounds(0, 15, 350, 30);
		pay.setAlignment(Label.RIGHT);
		pay.setFont(new Font("Serif", Font.BOLD, 25));
		pay.setForeground(Color.yellow);
		won = new Label(" ￦");
		won.setBounds(345, 19, 32, 30);
		won.setFont(new Font("Serif", Font.BOLD, 25));
		won.setForeground(Color.yellow);
		payled.add(pay);
		payled.add(won);
		
		moneys = new Panel();
		moneys.setBounds(50,530,400,80);
		moneys.setBackground(Color.lightGray);
		moneys.setLayout(null);
		money_100 = new Button("100 ￦");
		money_100.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				realmoney += 100;
				pay.setText(String.valueOf(realmoney));
			}
		});
		money_100.setBounds(5, 3, 125, 35);
		money_500 = new Button("500 ￦");
		money_500.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				realmoney += 500;
				pay.setText(String.valueOf(realmoney));
			}
		});
		money_500.setBounds(5, 43, 125, 35);
		money_1000 = new Button("1000 ￦");
		money_1000.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				realmoney += 1000;
				pay.setText(String.valueOf(realmoney));
			}
		});
		money_1000.setBounds(140, 3, 120, 35);
		money_5000 = new Button("5000 ￦");
		money_5000.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				realmoney += 5000;
				pay.setText(String.valueOf(realmoney));
			}
		});
		money_5000.setBounds(140, 43, 120, 35);
		but_return = new Button("반환");
		but_return.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				realmoney = 0;
				pay.setText(String.valueOf(realmoney));
			}
		});
		but_return.setBounds(270, 3, 125, 75);
		moneys.add(money_100);
		moneys.add(money_500);
		moneys.add(money_1000);
		moneys.add(money_5000);
		moneys.add(but_return);
		
		bill = new TextArea("", 10, 10, TextArea.SCROLLBARS_NONE);
		bill.setBounds(50, 620, 400, 150);
		bill.setEditable(false);
		bill.setFont(new Font("Serif",Font.BOLD,30));
		bill.setBackground(Color.gray);
		bill.setForeground(Color.GREEN);
		
		
		
	
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose(); //현재 창만 종료
				//System.exit(0); //프로그램 종료
			}
		});
		
		Toolkit tk = Toolkit.getDefaultToolkit();
		img = tk.getImage("vending.jpg"); //이미지 파일명
		
		add(menus);
		add(payled);
		add(moneys);
		add(bill);
		setLayout(null);
		setResizable(false);
		setBounds(100,100,500,900);
		setVisible(true);
	}
	
	public void paint(Graphics g)
	{
		if(img == null)
			return;
		int imgWidth = img.getWidth(this);
		int imgHeight = img.getHeight(this);
		
		g.drawImage(img, (getWidth()-imgWidth)/2, (getHeight() - imgHeight)/2, this);
	
	}
	
	class MenuImage extends Panel
	{
		Toolkit tk=null;
		Image img=null;
		
		public void getImage(String path)
		{
			tk = getToolkit();
			img = tk.getImage(path);
			img = img.getScaledInstance(150,150, img.SCALE_SMOOTH);
			
		}
		@Override
		public void paint(Graphics g) {
			if(img == null)
				return;
			
			int imgWidth = img.getWidth(this);
			int imgHeight = img.getHeight(this);
			
			g.drawImage(img, (getWidth()-imgWidth)/2, (getHeight() - imgHeight)/2, this);
		}
	}
}

