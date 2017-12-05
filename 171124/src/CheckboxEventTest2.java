import java.awt.*;
import java.awt.event.*;

public class CheckboxEventTest2 extends Frame{

	CheckboxGroup group;
	Checkbox cb1;
	Checkbox cb2;
	Checkbox cb3;
	
	public CheckboxEventTest2(String title) {
		super(title);
		//üũ�ڽ� ���� �ϸ鼭 üũ�ڽ��׷����� �׷�ȭ ��Ŵ
		group = new CheckboxGroup();
		cb1 = new Checkbox("red", group, true);
		cb2 = new Checkbox("green", group, false);
		cb3 = new Checkbox("blue", group, false); //������ư(üũ �ȵ� ����), blue ��� ����
		
		cb1.addItemListener(new EventHandle()); //�̺�Ʈ ������(������)
		cb2.addItemListener(new EventHandle());
		cb3.addItemListener(new ItemListener() { //üũ�ڽ��� ���� ����� ȣ���!{
			@Override
			public void itemStateChanged(ItemEvent e) { //�͸�Ŭ������ �����ʿ� ó���� �ޱ�
				Checkbox cb = (Checkbox)e.getSource(); //�̺�Ʈ �߻��� �κ��� üũ�ڽ� ���� �������
				String color = cb.getLabel(); //�̺�Ʈ �߻��� �κ��� üũ�ڽ� �̸� ��������
				
				if(color.equals("red")) //���õ� üũ�ڽ� �̸� Ȯ��
				{
					setBackground(Color.red); //�������� ���� ����
					cb1.setBackground(Color.red); //üũ�ڽ����� ���� ����
					cb2.setBackground(Color.red);
					cb3.setBackground(Color.red);
				}
				else if(color.equals("green"))
				{
					setBackground(Color.green);
					cb1.setBackground(Color.green);
					cb2.setBackground(Color.green);
					cb3.setBackground(Color.green);
				}
				else
				{
					setBackground(Color.blue);
					cb1.setBackground(Color.blue);
					cb2.setBackground(Color.blue);
					cb3.setBackground(Color.blue);
				}
				
			}
		});
		
		
		addWindowListener(new WindowAdapter() { //�͸�Ŭ������ �������� �ޱ�
			public void windowClosing(WindowEvent we) //�ݱ��ư ������ �߻�
			{
				System.exit(0); //����
			}
		});
		setLayout(new FlowLayout());
		add(cb1);
		add(cb2);
		add(cb3);
		setBackground(Color.red);
		setSize(400,200);
		setVisible(true);
	}
	
	public static void main(String[] args) {
		CheckboxEventTest2 mainWin = new CheckboxEventTest2("CheckboxEventTest2");

	}
	class EventHandle implements ItemListener //üũ�ڽ��� ���� ����� ȣ���!
	{
		@Override
		public void itemStateChanged(ItemEvent e) {
			Checkbox cb = (Checkbox)e.getSource(); //�̺�Ʈ �߻��� �κ��� üũ�ڽ� ���� �������
			String color = cb.getLabel(); //�̺�Ʈ �߻��� �κ��� üũ�ڽ� �̸� ��������
			
			if(color.equals("red")) //���õ� üũ�ڽ� �̸� Ȯ��
			{
				setBackground(Color.red); //�������� ���� ����
				cb1.setBackground(Color.red); //üũ�ڽ����� ���� ����
				cb2.setBackground(Color.red);
				cb3.setBackground(Color.red);
			}
			else if(color.equals("green"))
			{
				setBackground(Color.green);
				cb1.setBackground(Color.green);
				cb2.setBackground(Color.green);
				cb3.setBackground(Color.green);
			}
			else
			{
				setBackground(Color.blue);
				cb1.setBackground(Color.blue);
				cb2.setBackground(Color.blue);
				cb3.setBackground(Color.blue);
			}
			
		}
	}

}
