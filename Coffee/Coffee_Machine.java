package Coffee;
import java.awt.*; //awt ���
import java.awt.event.*; //event ���

public class Coffee_Machine extends Frame{ //frame ���

	MenuBar mb; //�޴��� 
	Menu mFile; //�޴�
	MenuItem miNew; //�޴�������
	MenuItem miSave;
	MenuItem miOpen;
	MenuItem miExit;
	
	Menu mView;
	MenuItem miCoffee;
	MenuItem miDessert;
	MenuItem miBrunch;
	
	Menu mHelp;
	MenuItem miHelp;
	
	Label mainLabel; //��
	Label menu;
	Label size;
	
	CheckboxGroup cofg; //üũ�ڽ� �׷�
	Checkbox cof1; //üũ�ڽ�
	Checkbox cof2;
	Checkbox cof3;
	
	CheckboxGroup sizeg;
	Checkbox size1;
	Checkbox size2;
	Checkbox size3;
	
	Button paybutton; //��ư
	
	TextArea bill; //�ؽ�Ʈ������
	public Coffee_Machine(String title) { //�����ڿ��� �ٷ� ������ ����
		super(title); //�Ű������� Ÿ��Ʋ ���� (�Ű������ִ� ������ ������ ȣ��)
		mb = new MenuBar(); //�޴��� ����
		
		mFile = new Menu("File"); //�޴�����
		miNew = new MenuItem("New"); //�޴������� ����
		miSave = new MenuItem("Save");
		miOpen = new MenuItem("Open");
		miExit = new MenuItem("Exit");
		
		miExit.addActionListener(new ActionListener() 
		{ //�޴������ۿ� �̺�Ʈ �����ʵ�� �� ����Ŭ������ �ڵ鷯 ����
			public void actionPerformed(ActionEvent e)  //�޴� ������ Ŭ���� �ش� �޼ҵ� ����
			{
				System.exit(0); //���α׷� ����
			}
		});
		
		mFile.add(miNew); //�޴��� �޴������� ���
		mFile.add(miSave);
		mFile.add(miOpen);
		mFile.addSeparator();
		mFile.add(miExit);
		
		mb.add(mFile); //�޴��ٿ� �޴����
		
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
		mb.setHelpMenu(mHelp); //�ش� �޴��� Help�޴��� ���
		
		mainLabel = new Label("COFFEE",Label.CENTER); //�󺧻��� , ��� ����
		mainLabel.setForeground(Color.orange); //���ڻ� ����
		mainLabel.setFont(new Font("Serif", Font.BOLD, 25)); //��Ʈ ����
		mainLabel.setBounds(150, 40, 100, 60); //��ġ �� ũ�� ����
		
		menu = new Label("Ŀ�Ǹ� �����ϼ���.");
		menu.setFont(new Font("SansSerif", Font.BOLD, 13));
		menu.setBounds(40, 100, 148, 20);
		
		cofg = new CheckboxGroup(); //üũ�ڽ� �׷� ����
		cof1 = new Checkbox(" �Ƹ޸�ī�� - 1000��",cofg,true); //üũ�ڽ� ���� �� �׷���
		cof1.setBounds(40, 130, 130, 25); //ũ�� �� ��ġ ����
		cof2 = new Checkbox(" ī��� - 1500��",cofg,false);
		cof2.setBounds(40, 160, 130, 25);
		cof3 = new Checkbox(" ī��Ḷ���ƶ� - 2000��",cofg,false);
		cof3.setBounds(40, 190, 160, 25);
		
		size = new Label("����� �����ϼ���.");
		size.setFont(new Font("SansSerif", Font.BOLD, 13));
		size.setBounds(40, 230, 148, 20);
		
		sizeg = new CheckboxGroup();
		size1 = new Checkbox(" ����",sizeg,true);
		size1.setBounds(40, 260, 130, 25);
		size2 = new Checkbox(" �̵�� (+500��)",sizeg,false);
		size2.setBounds(40, 290, 130, 25);
		size3 = new Checkbox(" ���� (+1000��)",sizeg,false);
		size3.setBounds(40, 320, 130, 25);

		paybutton = new Button("����ϱ�"); //��ư ����
		paybutton.setBounds(30, 360, 340, 40); //��ġ �� ũ�� ����
		paybutton.addActionListener(new EventHandle()); //�̺�Ʈ������ ��� �� �ڵ鷯 ����(EventHandle Ŭ����)
		
		bill = new TextArea("",10,10,TextArea.SCROLLBARS_NONE); //�ؽ�Ʈ ������ ���� 10�� 10�� ��ũ�ѹ� x
		bill.setEditable(false); //�Է� �Ұ��� �ϰ� ����
		bill.setBounds(30, 405, 340, 80); //��ġ �� ũ�� ����
		bill.setBackground(Color.white); //���� �������

		
		addWindowListener(new WindowAdapter() //�����ӿ� �����츮���� ��� �� ����Ŭ������ �������� ����(�̺�Ʈ �ڵ鷯) 
		{
			public void windowClosing(WindowEvent we) //�ݱ� ��ư ������ �� �ش� �޼ҵ� ����
			{
				System.exit(0); //����
			}
		});
				
		setMenuBar(mb); //������Ʈ ���
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
		setLayout(null); //���̾ƿ� ���� ����
		setSize(400,500); //������ ũ������
		setVisible(true); //ȭ�鿡 ���̱�
	}
	public static void main(String[] args) 
	{
		Coffee_Machine mainWindow = new Coffee_Machine("Coffee_Machine"); //������ ����
	}
	
	class EventHandle implements ActionListener //�̺�Ʈ �ڵ鷯 
	{
		@Override
		public void actionPerformed(ActionEvent e) //��ư Ŭ���� �ش� �޼ҵ� ����
		{
			int result = 0; //������ ���� ����
			String coffee; //�޴��� ���� ����
			String size; //ũ�⸦ ���� ����
			Checkbox selectmenu = cofg.getSelectedCheckbox(); //�ش� üũ�ڽ� �׷쿡�� ���õ� üũ�ڽ� �������� (�޴�)
			Checkbox selectsize = sizeg.getSelectedCheckbox(); //�ش� üũ�ڽ� �׷쿡�� ���õ� üũ�ڽ� �������� (������)
			
			if(selectmenu.getLabel() == cof1.getLabel()) //���õ� �޴��� ���� �ش� �޴��� ������ �˻�
			{
				coffee = "�Ƹ޸�ī��"; //�޴� ���
				result += 1000; //���� ���
			}
			else if(selectmenu.getLabel() == cof2.getLabel())
			{
				coffee = "ī���";
				result += 1500;
			}
			else
			{
				coffee = "ī��Ḷ���߶�";
				result += 2000;
			}
			
			if(selectsize.getLabel() == size2.getLabel()) //���õ� �������� ���� �ش� �������� ������ �˻�
			{
				size = "�̵��"; //������ ���
				result += 500; //�߰� ���� ���
			}
			else if(selectsize.getLabel() == size3.getLabel())
			{
				size = "����";
				result += 1000;
			}
			else
			{
				size = "����";
			}
			
			bill.setText(""); //�ؽ�Ʈ ������ ����
			
			String total = "����: "+coffee+"\n"+"\n������: "+size+"\n"+"\n����: "+result; //�ؽ�Ʈ ����� ������ ��Ʈ�� ���� �޴�,������,���� �ۼ�
			bill.setText(total); //�ؽ�Ʈ����� ��� 
		}
	}
}
