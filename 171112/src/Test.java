import java.awt.*; //awt 하위 아이템 사용을 위해 import

public class Test 
{
	public static void main(String[] args) 
	{
		Frame f = new Frame("Java Final Project"); //프레임 생성 및 타이틀 지정
		f.setSize(300,350); //크기 지정
		f.setLayout(null); //Layout을 사용 안함
		
///////////////////Menu//////////////////////////
		MenuBar mb = new MenuBar(); //MenuBar 생성
		
		Menu mFile = new Menu("File"); //Menu 생성
		MenuItem miNew = new MenuItem("New"); //MenuItem 생성
		MenuItem miOpen = new MenuItem("Open");
		MenuItem miSave = new MenuItem("Save");
		MenuItem miExit = new MenuItem("Exit");
		mFile.add(miNew); //Menu에 MenuItem 연결
		mFile.add(miOpen);
		mFile.add(miSave);
		mFile.addSeparator(); //구분선 추가
		mFile.add(miExit);
		
		Menu mMenu = new Menu("Menu");
		
		Menu mAddMenu = new Menu("AddMenu");
		Menu mNumber = new Menu("Number");
		CheckboxMenuItem mi1 = new CheckboxMenuItem("1"); //체크박스메뉴아이템 생성
		CheckboxMenuItem mi2 = new CheckboxMenuItem("2");
		CheckboxMenuItem mi3 = new CheckboxMenuItem("3");
		mAddMenu.add(mNumber); //메뉴에 메뉴 연결
		mNumber.add(mi1); //메뉴에 체크박스메뉴아이템 연결
		mNumber.add(mi2);
		mNumber.add(mi3);
		
		Menu mHelp = new Menu("Help");
		
		mb.add(mFile); //메뉴바에 메뉴 연결
		mb.add(mMenu);
		mb.add(mAddMenu);
		mb.setHelpMenu(mHelp); //해당 메뉴를 HelpMenu로 설정
		
///////////////////////회원 가입////////////////////////////		
		Label signup = new Label("회원가입"); //라벨 생성
		signup.setBounds(95, 60, 100, 30); //위치,크기 지정
		signup.setFont(new Font("Serif",Font.PLAIN,25)); //폰트 지정

//////////////////////ID////////////////////////////////
		Label id = new Label("ID:",Label.LEFT); //라벨 생성
		id.setBounds(25, 100, 30, 30);
		TextField idBox = new TextField(20); //텍스트필드 생성
		idBox.setBounds(55, 105, 80, 20);
		
//////////////////////PW////////////////////////////////
		Label pw = new Label("PW:",Label.LEFT); //라벨생성
		pw.setBounds(155, 100, 30, 30);
		TextField pwBox = new TextField(20); //텍스트필드생성
		pwBox.setBounds(192, 105, 80, 20); //보여지는 문자를 *로 변경 (password)
		pwBox.setEchoChar('*');
		
//////////////////////Phone////////////////////////////////
		Label phone = new Label("핸드폰번호:",Label.LEFT); //라벨생성
		phone.setBounds(15, 150, 70, 20);
		Choice firstfield = new Choice(); //Choice 생성 (010~ 019) 선택
		firstfield.setBounds(85, 145, 50, 20);
		firstfield.add("010"); //choice 목록 추가
		firstfield.add("011");
		firstfield.add("016");
		firstfield.add("017");
		firstfield.add("018");
		firstfield.add("019");
		Label sep1 = new Label("-");
		sep1.setBounds(140, 150, 15, 15);
		TextField secfield = new TextField(4);
		secfield.setBounds(155, 148, 45, 20);
		Label sep2 = new Label("-");
		sep2.setBounds(205, 150, 15, 15);
		TextField tirfield = new TextField(4);
		tirfield.setBounds(220, 148, 45, 20);
		
//////////////////////주소////////////////////////////////
		Label address = new Label("주소:"); //라벨 생성
		address.setBounds(15, 210, 30, 20);
		TextArea addressArea = new TextArea(10,50); //TextArea 생성
		addressArea.setBounds(50, 195, 230, 60);
		
//////////////////////혈액형////////////////////////////////
		Label blood = new Label("혈액형:");
		blood.setBounds(15, 275, 40, 20);
		CheckboxGroup abo = new CheckboxGroup(); //CheckboxGroup 생성 (체크 박스를 라디오 버튼으로 묶음!)
		Checkbox a = new Checkbox("A", abo, false); //Grouping 될 체크박스 생성
		a.setBounds(75, 275, 30, 20);
		Checkbox b = new Checkbox("B", abo, false);
		b.setBounds(125, 275, 30, 20);
		Checkbox ab = new Checkbox("AB", abo, false);
		ab.setBounds(175, 275, 35, 20);
		Checkbox o = new Checkbox("O", abo, false);
		o.setBounds(230, 275, 30, 20);
		
//////////////////////버튼////////////////////////////////		
		Button send = new Button("등록"); //버튼 생성
		send.setBounds(35, 310, 80, 20);
		Button cancel = new Button("취소");
		cancel.setBounds(180, 310, 80, 20);
		
		
		
		f.add(signup); //frame에 컴포넌트 연결
		f.add(id);
		f.add(idBox);
		f.add(pw);
		f.add(pwBox);
		f.add(phone);
		f.add(firstfield);
		f.add(sep1);
		f.add(secfield);
		f.add(sep2);
		f.add(tirfield);
		f.add(address);
		f.add(addressArea);
		f.add(blood);
		f.add(a);
		f.add(b);
		f.add(ab);
		f.add(o);
		f.add(send);
		f.add(cancel);
		
		f.setMenuBar(mb); //메뉴바 설정
		f.setVisible(true); //화면에 띄우기 
	}
}
