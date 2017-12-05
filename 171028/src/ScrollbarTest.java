import java.awt.*; //awt import
public class ScrollbarTest 
{
	public static void main(String[] args) 
	{
		Frame f = new Frame("Scrollbar"); //Frame���� Ÿ��Ʋ(Scrollbar)
		f.setBounds(500, 500, 300, 200); //Frame�� ��ġ,ũ�� ����
		f.setLayout(null); //Layout �̼���
		
		Scrollbar hor = new Scrollbar(Scrollbar.HORIZONTAL,0,50,0,100); //Scrollbar ����(����),�ʱⰪ ,��ũ�ѹ�ư ũ��, �ּҰ�, �ִ밪
		hor.setBounds(150, 50, 100, 15); //��ġ �� ũ�� ����
		f.add(hor);
		
		Scrollbar ver = new Scrollbar(Scrollbar.VERTICAL,0,50,0,100);//Scrollbar ����(����)
		ver.setBounds(50, 50, 15, 100);
		f.add(ver);
		
		f.setVisible(true);
	}

}
