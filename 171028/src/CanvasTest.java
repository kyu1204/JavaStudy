import java.awt.*;
public class CanvasTest 
{
	public static void main(String[] args) 
	{
		Frame f = new Frame("CanvasTest"); //Frame ����
		f.setBounds(500, 500, 300, 200);
		f.setLayout(null);
		
		Canvas c = new Canvas(); //Canvas ����
		c.setBackground(Color.pink); //bgColor Pink�� ����
		c.setBounds(50, 50, 150, 100); //��ġ �� ũ�� ����
		
		f.add(c);
		f.setVisible(true);
	}
}
