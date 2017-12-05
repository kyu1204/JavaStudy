import java.awt.*;

public class ScrollPaneTest 
{
	public static void main(String[] args) 
	{
		Frame f = new Frame("ScrollPaneTest");
		f.setBounds(500,500,300,200);
		
		ScrollPane sp = new ScrollPane();
		//ScrollPane �ϳ��� ������Ʈ�� ��ϰ����� �������� �����̳�
		//���ѵ� ������ ū ������Ʈ�� ȭ�鿡 �����ٶ� ���
		Panel p = new Panel(); //Panel ����
		p.setBackground(Color.green);
		p.add(new Button("ù��°")); //��ư�� �гο� ���
		p.add(new Button("�ι�°"));
		p.add(new Button("����°"));
		p.add(new Button("�׹�°"));
		
		sp.add(p); //Panel�� ScrollPane �� ���
		f.add(sp); //ScrollPane�� Frame �� ���
		f.setVisible(true);
	}
}
