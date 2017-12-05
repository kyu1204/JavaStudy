import java.awt.*;
public class DialogTest
{
	public static void main(String[] args) 
	{
		Frame f = new Frame("Parent");
		f.setBounds(500,500,300,200);
		
		Dialog info = new Dialog(f,"Information",true);
		//Dialog �������� �����̳� �ַ� �޼���â���� ���
		//���ڷ� parent-��� Frame�� ���Ұ�����, title, modal-��� or ��޸���
		info.setBounds(50, 50, 140, 110); //��ġ �� ũ��
		info.setLayout(new FlowLayout());
		
		Label msg = new Label("This is modal Dialog",Label.CENTER);
		Button ok = new Button("OK");
		info.add(msg); info.add(ok); //Dialog �� ���(�������� �����̳�)
		
		f.setVisible(true);
		info.setVisible(true); //Dialog�� �ٷ� ����
	}
}
