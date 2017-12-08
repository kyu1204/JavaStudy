package Project;

import java.awt.*;
import java.awt.event.*;

public class GraphicesTest extends Frame implements MouseMotionListener{

	int x=0,y=0;
	
	public GraphicesTest(String title)
	{
		super(title);
		
		addMouseMotionListener(this);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose(); //현재 프레임만 닫고 프로그램은 종료하지 않음
				//System.exit(0); //프로그램 종료
			}
		});
		
		setSize(300, 300);
		//위치 지정
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize();
		setLocation(screenSize.width/2-getWidth()/2, screenSize.height/2-getHeight()/2);
		
		setVisible(true);
	}
	//모든 컴포넌트에 Graphics 객체가 있음!, getGraphices()로 얻을 수 있음!
	//컴포넌트에 그림그리기 위해서는 paint() 오버라이딩 해야 함!
	//paint(): 컴포넌트에 그림을 그리기 위함
	
	@Override
	public void paint(Graphics g) {
		g.drawString("마우스를 움직여보세요.", 10, 50);
		g.drawString("*", x, y);
	}
	//repaint(): AWT쓰레드에게 화면 갱신 요청(0.1초마다 확인 후 요청있으면 update()호출)
	//->update():화면 지우고 paint()호출
	//->paint():화면 갱신
	
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

