import java.awt.*;
import java.awt.event.*;

public class CheckboxEventTest extends Frame {
	Label q1; //Label
	Label q2;
	Label score;
	Checkbox q1cb1, q1cb2, q1cb3, q1cb4;
	Checkbox q2cb1, q2cb2, q2cb3, q2cb4;
	CheckboxGroup group; //CheckboxGroup
	Button end; //Button
	
	public CheckboxEventTest(String title) {
		super(title);//프레임을 상속받아 프레임의 매개변수있는 생성자 호출
		
		q1 = new Label("1. 다음 중 ActionEvent의 actionPerformed메서드가 호출되는 경우는? (모두 고르세요.)"); //Label 객체 생성
		q1cb1 = new Checkbox("Button을 눌렀을 때"); //checkbox 객체 생성
		q1cb2 = new Checkbox("TextField에서 Enter키를 눌렀을 때");
		q1cb3 = new Checkbox("MenuItem을 클릭했을 때");
		q1cb4 = new Checkbox("List에서 더블클릭으로 item을 선택했을 때");
		
		q2 = new Label("2. Frame의 기본 LayoutManager는? (하나만 고르세요.)");
		group = new CheckboxGroup(); //checkboxgroup 생성
		q2cb1 = new Checkbox("FlowLayout", group, false); //grouping 된 checkbox 생성
		q2cb2 = new Checkbox("GridLayout", group, false);
		q2cb3 = new Checkbox("BorderLayout", group, false);
		q2cb4 = new Checkbox("CardLayout", group, false);
		
		score = new Label("* 결과: ");
		
		end = new Button("이 버튼을 누르시면 결과를 알 수 있습니다.");//버튼 생성
		end.addActionListener(new ActionListener() {
		//버튼에 이벤트 리스너 등록 및 무명클래스로 핸들러 바로 생성	
			@Override
			public void actionPerformed(ActionEvent e) {
				float totalScore =0; //버튼이 눌렸을때에 행동
				if(q1cb1.getState()) //체크박스버튼이 체크되면
					totalScore += 12.5;
				if(q1cb2.getState())
					totalScore += 12.5;
				if(q1cb3.getState())
					totalScore += 12.5;
				if(q1cb4.getState())
					totalScore += 12.5;
				if(q2cb3.getState())
					totalScore += 50;
				score.setText("* 결과: 당신의 점수는 "+totalScore+"점 입니다."); //score의 text변경
			}
		});
		
		
		add(q1); //프레임에 컴포넌트들 추가
		add(q1cb1); add(q1cb2); add(q1cb3); add(q1cb4);
		add(new Label("")); //빈 줄 하나 삽입
		add(q2);
		add(q2cb1); add(q2cb2); add(q2cb3); add(q2cb4);
		add(end);
		add(score);
		
		//닫기 버튼 누를시 종료 하게 하기 위해 윈도우리스너 추가 및 무명클래스로 WindowAdapter 생성 후 
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0); //닫기버튼 눌릴 시 종료
			}
		});
		setLayout(new GridLayout(13, 1));//Layout -> Grid
		setSize(600,400); //프레임 사이즈 설정
		setVisible(true); //프레임 보이기
	}
	public static void main(String[] args) {
		CheckboxEventTest mainWin = new CheckboxEventTest("CheckboxEventTest - Quiz");

	}

}
