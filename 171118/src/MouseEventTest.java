import java.awt.*; //컴포넌트 사용을 위해
import java.awt.event.*; //이벤트 처리를 위해


public class MouseEventTest extends Frame { //Frame을 상속받아 사용

	Label location; //라벨 
	public MouseEventTest(String title) 
	{
		super(title); //매개변수 있는 생성자로 타이틀을 받아서 조상클래스인 Frame에 매개변수 있는 생성자 호출
		
		location = new Label("Mouse Pointer Location: "); //라벨 객체 생성
		location.setBounds(10, 35, 195, 15); //label 위치 및 크기 지정
		location.setBackground(Color.yellow); //bg컬러 Yellow
		add(location); //컨테이너에 추가

		addMouseMotionListener(new MouseEventHandle());
		
		addWindowListener(new MouseEventHandle());
		setLayout(null); //Layout 사용 x
		setSize(400,300); //프레임의 사이즈
		setVisible(true); //프레임 보이기
	}
	
	public static void main(String[] args) {
		MouseEventTest mt = new MouseEventTest("MouseEventTest");

	}
	
	private class MouseEventHandle extends WindowAdapter implements MouseMotionListener {
		
		public void windowClosing(WindowEvent e) {
			System.exit(0);
		}
		@Override
		public void mouseDragged(MouseEvent e) {}

		@Override
		public void mouseMoved(MouseEvent e) {
			location.setText("Mouse Pointer Location: ("+e.getX()+", "+e.getY()+")");
			
		}
		
	}

}
