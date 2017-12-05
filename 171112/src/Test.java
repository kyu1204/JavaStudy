import java.awt.*; //awt ���� ������ ����� ���� import

public class Test 
{
	public static void main(String[] args) 
	{
		Frame f = new Frame("Java Final Project"); //������ ���� �� Ÿ��Ʋ ����
		f.setSize(300,350); //ũ�� ����
		f.setLayout(null); //Layout�� ��� ����
		
///////////////////Menu//////////////////////////
		MenuBar mb = new MenuBar(); //MenuBar ����
		
		Menu mFile = new Menu("File"); //Menu ����
		MenuItem miNew = new MenuItem("New"); //MenuItem ����
		MenuItem miOpen = new MenuItem("Open");
		MenuItem miSave = new MenuItem("Save");
		MenuItem miExit = new MenuItem("Exit");
		mFile.add(miNew); //Menu�� MenuItem ����
		mFile.add(miOpen);
		mFile.add(miSave);
		mFile.addSeparator(); //���м� �߰�
		mFile.add(miExit);
		
		Menu mMenu = new Menu("Menu");
		
		Menu mAddMenu = new Menu("AddMenu");
		Menu mNumber = new Menu("Number");
		CheckboxMenuItem mi1 = new CheckboxMenuItem("1"); //üũ�ڽ��޴������� ����
		CheckboxMenuItem mi2 = new CheckboxMenuItem("2");
		CheckboxMenuItem mi3 = new CheckboxMenuItem("3");
		mAddMenu.add(mNumber); //�޴��� �޴� ����
		mNumber.add(mi1); //�޴��� üũ�ڽ��޴������� ����
		mNumber.add(mi2);
		mNumber.add(mi3);
		
		Menu mHelp = new Menu("Help");
		
		mb.add(mFile); //�޴��ٿ� �޴� ����
		mb.add(mMenu);
		mb.add(mAddMenu);
		mb.setHelpMenu(mHelp); //�ش� �޴��� HelpMenu�� ����
		
///////////////////////ȸ�� ����////////////////////////////		
		Label signup = new Label("ȸ������"); //�� ����
		signup.setBounds(95, 60, 100, 30); //��ġ,ũ�� ����
		signup.setFont(new Font("Serif",Font.PLAIN,25)); //��Ʈ ����

//////////////////////ID////////////////////////////////
		Label id = new Label("ID:",Label.LEFT); //�� ����
		id.setBounds(25, 100, 30, 30);
		TextField idBox = new TextField(20); //�ؽ�Ʈ�ʵ� ����
		idBox.setBounds(55, 105, 80, 20);
		
//////////////////////PW////////////////////////////////
		Label pw = new Label("PW:",Label.LEFT); //�󺧻���
		pw.setBounds(155, 100, 30, 30);
		TextField pwBox = new TextField(20); //�ؽ�Ʈ�ʵ����
		pwBox.setBounds(192, 105, 80, 20); //�������� ���ڸ� *�� ���� (password)
		pwBox.setEchoChar('*');
		
//////////////////////Phone////////////////////////////////
		Label phone = new Label("�ڵ�����ȣ:",Label.LEFT); //�󺧻���
		phone.setBounds(15, 150, 70, 20);
		Choice firstfield = new Choice(); //Choice ���� (010~ 019) ����
		firstfield.setBounds(85, 145, 50, 20);
		firstfield.add("010"); //choice ��� �߰�
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
		
//////////////////////�ּ�////////////////////////////////
		Label address = new Label("�ּ�:"); //�� ����
		address.setBounds(15, 210, 30, 20);
		TextArea addressArea = new TextArea(10,50); //TextArea ����
		addressArea.setBounds(50, 195, 230, 60);
		
//////////////////////������////////////////////////////////
		Label blood = new Label("������:");
		blood.setBounds(15, 275, 40, 20);
		CheckboxGroup abo = new CheckboxGroup(); //CheckboxGroup ���� (üũ �ڽ��� ���� ��ư���� ����!)
		Checkbox a = new Checkbox("A", abo, false); //Grouping �� üũ�ڽ� ����
		a.setBounds(75, 275, 30, 20);
		Checkbox b = new Checkbox("B", abo, false);
		b.setBounds(125, 275, 30, 20);
		Checkbox ab = new Checkbox("AB", abo, false);
		ab.setBounds(175, 275, 35, 20);
		Checkbox o = new Checkbox("O", abo, false);
		o.setBounds(230, 275, 30, 20);
		
//////////////////////��ư////////////////////////////////		
		Button send = new Button("���"); //��ư ����
		send.setBounds(35, 310, 80, 20);
		Button cancel = new Button("���");
		cancel.setBounds(180, 310, 80, 20);
		
		
		
		f.add(signup); //frame�� ������Ʈ ����
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
		
		f.setMenuBar(mb); //�޴��� ����
		f.setVisible(true); //ȭ�鿡 ���� 
	}
}
