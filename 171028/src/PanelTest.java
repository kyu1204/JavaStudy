import java.awt.*;
public class PanelTest 
{
	public static void main(String[] args) 
	{
		Frame f = new Frame("Panel"); //Frame����
		f.setBounds(500,500,300,200); //��ġ �� ũ��
		f.setLayout(null); //Layout �̼���
		
		Panel p = new Panel(); //Panel ���� �������� �����̳�, ���������� ���� �Ұ�
								//�׷��� �����̳��̱� ���п� ������Ʈ ��ϰ���
		p.setBackground(Color.green);//bgColor green
		p.setBounds(50,50,100,100);//��ġ �� ũ��
		
		Button ok = new Button("ok");
		
		p.add(ok); //Panel�����̳ʿ� Button������Ʈ ���
		f.add(p); //�������� �����̳ʶ� Frame�� Panel ���
		f.setVisible(true);
	}
}
