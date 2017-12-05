import java.awt.*;
public class CanvasTest 
{
	public static void main(String[] args) 
	{
		Frame f = new Frame("CanvasTest"); //Frame 생성
		f.setBounds(500, 500, 300, 200);
		f.setLayout(null);
		
		Canvas c = new Canvas(); //Canvas 생성
		c.setBackground(Color.pink); //bgColor Pink로 설정
		c.setBounds(50, 50, 150, 100); //위치 및 크기 지정
		
		f.add(c);
		f.setVisible(true);
	}
}
