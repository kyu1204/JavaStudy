package Project;
import java.awt.*;
import java.awt.event.*;

public class Help extends Frame {
	public Help() 
	{
		super("����������~"); // ������ ���
		addWindowListener( new WindowAdapter() { //�͸�Ŭ����
			public void windowClosing(WindowEvent we) {
				dispose();
				}
			});
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize();
		setLocation(screenSize.width/2-getWidth()/2, screenSize.height/2-getHeight()/2);
		setSize(500,460);
		setVisible(true); //������ ȭ�鿡 ���̱�
	}
	public void paint(Graphics g)
	{
		Image img = Toolkit.getDefaultToolkit().getImage("help.png");
		g.drawImage(img, 0, 20, this);
	} 
}
