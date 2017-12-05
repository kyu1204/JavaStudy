import java.awt.*; //컴포넌트 사용을 위해
import java.awt.event.*; //이벤트 처리를 위해

public class TextComponentEventTest extends Frame { //Frame을 상속받아 사용
	TextField tf = new TextField(); //TextField
	TextArea ta = new TextArea(); //TextArea
	
	private class WindowHandle extends WindowAdapter{
		@Override
		public void windowClosing(WindowEvent e) {
			System.exit(0);
		}
	}
	
	public TextComponentEventTest(String title)
	{
		super(title); //매개변수 있는 생성자를 호출(title)
		
		tf = new TextField(); //TextField 생성
		ta = new TextArea(); //TextArea 생성
		ta.setEnabled(false); //TextArea 수정불가하게 설정
		add(ta,"Center"); //컨테이너에 컴포넌트 추가
		add(tf,"South");
		
		tf.addActionListener(new ActionListener() {
			@Override //TextField에 이벤트리스너 추가 및 무명클래스로 이벤트핸들러 생성
			public void actionPerformed(ActionEvent e) { //TextField에 Enter 입력시 발생
				ta.append(tf.getText()+"\n"); //TextArea에 TextField에 쓰여진 텍스트 추가
				tf.setText(""); //TextField 비우기
				tf.requestFocus(); //Focus 이동
			}
		});
		
		addWindowListener(new WindowHandle()); //Window.Closing을 위해 리스너 설정
		setSize(500,300); //프레임 크기 설정
		setVisible(true); //프레임 보이기
		tf.requestFocus(); //포커스 TextField로
	}
	public static void main(String[] args) {
		TextComponentEventTest mainWin = 
				new TextComponentEventTest("TextComponentEventTest");

	}

}
