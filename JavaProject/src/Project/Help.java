package Project;
import java.awt.*;
import java.awt.event.*;

public class Help extends Frame {
	public Help() 
	{
		super("도움말이지롱~"); // 프레임 상속
		addWindowListener( new WindowAdapter() { //익명클래스
			public void windowClosing(WindowEvent we) {
				dispose();
				}
			});
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize();
		setLocation(screenSize.width/2-getWidth()/2, screenSize.height/2-getHeight()/2);
		setSize(500,460);
		setVisible(true); //프레임 화면에 보이기
	}
	public void paint(Graphics g)
	{
		Image img = Toolkit.getDefaultToolkit().getImage("help.png");
		g.drawImage(img, 0, 20, this);
	} 
}
