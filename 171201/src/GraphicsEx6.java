import java.awt.*;
import java.awt.event.*;

public class GraphicsEx6 extends Frame {

	Image img = null;
	public GraphicsEx6(String title)
	{
		super(title);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0); //프로그램 종료
			}
		});
		
		Toolkit tk = Toolkit.getDefaultToolkit();
		img = tk.getImage("java.jpg"); //이미지 파일명
		
		setBounds(100,100,400,300);
		setVisible(true);
	}
	
	public static void main(String[] args) {
		new GraphicsEx6("GraphicsEx6");
	}
	
	public void paint(Graphics g)
	{
		if(img == null)
			return;
		int imgWidth = img.getWidth(this);
		int imgHeight = img.getHeight(this);
		
		g.drawImage(img, (getWidth()-imgWidth)/2, (getHeight() - imgHeight)/2, this);
	}

}
