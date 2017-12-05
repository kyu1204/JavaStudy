import java.awt.*;
import java.awt.event.*;

public class GraphicesEx2 extends Frame implements MouseMotionListener{

	int x=0,y=0;
	
	public static void main(String[] args) {
		new GraphicesEx2("GraphicesEx2");
	}
	
	public GraphicesEx2(String title)
	{
		super(title);
		
		addMouseMotionListener(this);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0); //���α׷� ����
			}
		});
		
		setBounds(100,100,500,500);
		setVisible(true);
	}
	//��� ������Ʈ�� Graphics ��ü�� ����!, getGraphices()�� ���� �� ����!
	//������Ʈ�� �׸��׸��� ���ؼ��� paint() �������̵� �ؾ� ��!
	//paint(): ������Ʈ�� �׸��� �׸��� ����
	
	@Override
	public void paint(Graphics g) {
		g.drawString("���콺�� ������������.", 10, 50);
		g.drawString("*", x, y);
	}
	//repaint(): AWT�����忡�� ȭ�� ���� ��û(0.1�ʸ��� Ȯ�� �� ��û������ update()ȣ��)
	//->update():ȭ�� ����� paint()ȣ��
	//->paint():ȭ�� ����
	
	@Override
	public void update(Graphics g) {
		paint(g);
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		x = e.getX();
		y = e.getY();
		repaint(); 
	}

	@Override
	public void mouseDragged(MouseEvent e) {}

}
