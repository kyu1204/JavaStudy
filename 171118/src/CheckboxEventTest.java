import java.awt.*;
import java.awt.event.*;

public class CheckboxEventTest extends Frame {
	Label q1; //Label
	Label q2;
	Label score;
	Checkbox q1cb1, q1cb2, q1cb3, q1cb4;
	Checkbox q2cb1, q2cb2, q2cb3, q2cb4;
	CheckboxGroup group; //CheckboxGroup
	Button end; //Button
	
	public CheckboxEventTest(String title) {
		super(title);//�������� ��ӹ޾� �������� �Ű������ִ� ������ ȣ��
		
		q1 = new Label("1. ���� �� ActionEvent�� actionPerformed�޼��尡 ȣ��Ǵ� ����? (��� ������.)"); //Label ��ü ����
		q1cb1 = new Checkbox("Button�� ������ ��"); //checkbox ��ü ����
		q1cb2 = new Checkbox("TextField���� EnterŰ�� ������ ��");
		q1cb3 = new Checkbox("MenuItem�� Ŭ������ ��");
		q1cb4 = new Checkbox("List���� ����Ŭ������ item�� �������� ��");
		
		q2 = new Label("2. Frame�� �⺻ LayoutManager��? (�ϳ��� ������.)");
		group = new CheckboxGroup(); //checkboxgroup ����
		q2cb1 = new Checkbox("FlowLayout", group, false); //grouping �� checkbox ����
		q2cb2 = new Checkbox("GridLayout", group, false);
		q2cb3 = new Checkbox("BorderLayout", group, false);
		q2cb4 = new Checkbox("CardLayout", group, false);
		
		score = new Label("* ���: ");
		
		end = new Button("�� ��ư�� �����ø� ����� �� �� �ֽ��ϴ�.");//��ư ����
		end.addActionListener(new ActionListener() {
		//��ư�� �̺�Ʈ ������ ��� �� ����Ŭ������ �ڵ鷯 �ٷ� ����	
			@Override
			public void actionPerformed(ActionEvent e) {
				float totalScore =0; //��ư�� ���������� �ൿ
				if(q1cb1.getState()) //üũ�ڽ���ư�� üũ�Ǹ�
					totalScore += 12.5;
				if(q1cb2.getState())
					totalScore += 12.5;
				if(q1cb3.getState())
					totalScore += 12.5;
				if(q1cb4.getState())
					totalScore += 12.5;
				if(q2cb3.getState())
					totalScore += 50;
				score.setText("* ���: ����� ������ "+totalScore+"�� �Դϴ�."); //score�� text����
			}
		});
		
		
		add(q1); //�����ӿ� ������Ʈ�� �߰�
		add(q1cb1); add(q1cb2); add(q1cb3); add(q1cb4);
		add(new Label("")); //�� �� �ϳ� ����
		add(q2);
		add(q2cb1); add(q2cb2); add(q2cb3); add(q2cb4);
		add(end);
		add(score);
		
		//�ݱ� ��ư ������ ���� �ϰ� �ϱ� ���� �����츮���� �߰� �� ����Ŭ������ WindowAdapter ���� �� 
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0); //�ݱ��ư ���� �� ����
			}
		});
		setLayout(new GridLayout(13, 1));//Layout -> Grid
		setSize(600,400); //������ ������ ����
		setVisible(true); //������ ���̱�
	}
	public static void main(String[] args) {
		CheckboxEventTest mainWin = new CheckboxEventTest("CheckboxEventTest - Quiz");

	}

}
