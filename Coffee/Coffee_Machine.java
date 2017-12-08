package Coffee;
import java.awt.*; //awt 사용
import java.awt.event.*; //event 사용

public class Coffee_Machine extends Frame{ //frame 상속

	MenuBar mb; //메뉴바 
	Menu mFile; //메뉴
	MenuItem miNew; //메뉴아이템
	MenuItem miSave;
	MenuItem miOpen;
	MenuItem miExit;
	
	Menu mView;
	MenuItem miCoffee;
	MenuItem miDessert;
	MenuItem miBrunch;
	
	Menu mHelp;
	MenuItem miHelp;
	
	Label mainLabel; //라벨
	Label menu;
	Label size;
	
	CheckboxGroup cofg; //체크박스 그룹
	Checkbox cof1; //체크박스
	Checkbox cof2;
	Checkbox cof3;
	
	CheckboxGroup sizeg;
	Checkbox size1;
	Checkbox size2;
	Checkbox size3;
	
	Button paybutton; //버튼
	
	TextArea bill; //텍스트에리어
	public Coffee_Machine(String title) { //생성자에서 바로 프레임 생성
		super(title); //매개변수로 타이틀 지정 (매개변수있는 프레임 생성자 호출)
		mb = new MenuBar(); //메뉴바 생성
		
		mFile = new Menu("File"); //메뉴생성
		miNew = new MenuItem("New"); //메뉴아이템 생성
		miSave = new MenuItem("Save");
		miOpen = new MenuItem("Open");
		miExit = new MenuItem("Exit");
		
		miExit.addActionListener(new ActionListener() 
		{ //메뉴아이템에 이벤트 리스너등록 밑 무명클래스로 핸들러 생성
			public void actionPerformed(ActionEvent e)  //메뉴 아이템 클릭시 해당 메소드 실행
			{
				System.exit(0); //프로그램 종료
			}
		});
		
		mFile.add(miNew); //메뉴에 메뉴아이템 등록
		mFile.add(miSave);
		mFile.add(miOpen);
		mFile.addSeparator();
		mFile.add(miExit);
		
		mb.add(mFile); //메뉴바에 메뉴등록
		
		mView = new Menu("View");
		miCoffee = new MenuItem("Coffee");
		miDessert = new MenuItem("Dessert");
		miBrunch = new MenuItem("Brunch");
		
		mView.add(miCoffee);
		mView.add(miDessert);
		mView.add(miBrunch);
		
		mb.add(mView);
		
		mHelp = new Menu("Help");
		miHelp = new MenuItem("Help");
		mHelp.add(miHelp);
		mb.setHelpMenu(mHelp); //해당 메뉴를 Help메뉴로 등록
		
		mainLabel = new Label("COFFEE",Label.CENTER); //라벨생성 , 가운데 정렬
		mainLabel.setForeground(Color.orange); //글자색 지정
		mainLabel.setFont(new Font("Serif", Font.BOLD, 25)); //폰트 설정
		mainLabel.setBounds(150, 40, 100, 60); //위치 및 크기 지정
		
		menu = new Label("커피를 선택하세요.");
		menu.setFont(new Font("SansSerif", Font.BOLD, 13));
		menu.setBounds(40, 100, 148, 20);
		
		cofg = new CheckboxGroup(); //체크박스 그룹 생성
		cof1 = new Checkbox(" 아메리카노 - 1000원",cofg,true); //체크박스 생성 밑 그룹핑
		cof1.setBounds(40, 130, 130, 25); //크기 및 위치 지정
		cof2 = new Checkbox(" 카페라떼 - 1500원",cofg,false);
		cof2.setBounds(40, 160, 130, 25);
		cof3 = new Checkbox(" 카라멜마끼아또 - 2000원",cofg,false);
		cof3.setBounds(40, 190, 160, 25);
		
		size = new Label("사이즈를 선택하세요.");
		size.setFont(new Font("SansSerif", Font.BOLD, 13));
		size.setBounds(40, 230, 148, 20);
		
		sizeg = new CheckboxGroup();
		size1 = new Checkbox(" 스몰",sizeg,true);
		size1.setBounds(40, 260, 130, 25);
		size2 = new Checkbox(" 미디움 (+500원)",sizeg,false);
		size2.setBounds(40, 290, 130, 25);
		size3 = new Checkbox(" 라지 (+1000원)",sizeg,false);
		size3.setBounds(40, 320, 130, 25);

		paybutton = new Button("계산하기"); //버튼 생성
		paybutton.setBounds(30, 360, 340, 40); //위치 및 크기 지정
		paybutton.addActionListener(new EventHandle()); //이벤트리스너 등록 및 핸들러 설정(EventHandle 클래스)
		
		bill = new TextArea("",10,10,TextArea.SCROLLBARS_NONE); //텍스트 에리어 생성 10행 10열 스크롤바 x
		bill.setEditable(false); //입력 불가능 하게 설정
		bill.setBounds(30, 405, 340, 80); //위치 및 크기 지정
		bill.setBackground(Color.white); //배경색 흰색으로

		
		addWindowListener(new WindowAdapter() //프레임에 윈도우리스너 등록 및 무명클래스로 윈도우어뎁터 생성(이벤트 핸들러) 
		{
			public void windowClosing(WindowEvent we) //닫기 버튼 눌렸을 시 해당 메소드 실행
			{
				System.exit(0); //종료
			}
		});
				
		setMenuBar(mb); //컴포넌트 등록
		add(mainLabel);
		add(menu);
		add(cof1);
		add(cof2);
		add(cof3);
		add(size);
		add(size1);
		add(size2);
		add(size3);
		add(paybutton);
		add(bill);
		setLayout(null); //레이아웃 설정 안함
		setSize(400,500); //윈도우 크기지정
		setVisible(true); //화면에 보이기
	}
	public static void main(String[] args) 
	{
		Coffee_Machine mainWindow = new Coffee_Machine("Coffee_Machine"); //윈도우 생성
	}
	
	class EventHandle implements ActionListener //이벤트 핸들러 
	{
		@Override
		public void actionPerformed(ActionEvent e) //버튼 클릭시 해당 메소드 실행
		{
			int result = 0; //가격을 담을 변수
			String coffee; //메뉴를 담을 변수
			String size; //크기를 담을 변수
			Checkbox selectmenu = cofg.getSelectedCheckbox(); //해당 체크박스 그룹에서 선택된 체크박스 가져오기 (메뉴)
			Checkbox selectsize = sizeg.getSelectedCheckbox(); //해당 체크박스 그룹에서 선택된 체크박스 가져오기 (사이즈)
			
			if(selectmenu.getLabel() == cof1.getLabel()) //선택된 메뉴의 라벨이 해당 메뉴의 라벨인지 검사
			{
				coffee = "아메리카노"; //메뉴 등록
				result += 1000; //가격 등록
			}
			else if(selectmenu.getLabel() == cof2.getLabel())
			{
				coffee = "카페라떼";
				result += 1500;
			}
			else
			{
				coffee = "카라멜마끼야또";
				result += 2000;
			}
			
			if(selectsize.getLabel() == size2.getLabel()) //선택된 사이즈의 라벨이 해당 사이즈의 라벨인지 검사
			{
				size = "미디움"; //사이즈 등록
				result += 500; //추가 가격 등록
			}
			else if(selectsize.getLabel() == size3.getLabel())
			{
				size = "라지";
				result += 1000;
			}
			else
			{
				size = "스몰";
			}
			
			bill.setText(""); //텍스트 에리어 비우기
			
			String total = "선택: "+coffee+"\n"+"\n사이즈: "+size+"\n"+"\n가격: "+result; //텍스트 에리어에 쓰여질 스트링 생성 메뉴,사이즈,가격 작성
			bill.setText(total); //텍스트에리어에 등록 
		}
	}
}
