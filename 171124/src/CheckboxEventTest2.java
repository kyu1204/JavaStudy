import java.awt.*;
import java.awt.event.*;

public class CheckboxEventTest2 extends Frame{

	CheckboxGroup group;
	Checkbox cb1;
	Checkbox cb2;
	Checkbox cb3;
	
	public CheckboxEventTest2(String title) {
		super(title);
		//체크박스 생성 하면서 체크박스그룹으로 그룹화 시킴
		group = new CheckboxGroup();
		cb1 = new Checkbox("red", group, true);
		cb2 = new Checkbox("green", group, false);
		cb3 = new Checkbox("blue", group, false); //라디오버튼(체크 안된 상태), blue 라고 보임
		
		cb1.addItemListener(new EventHandle()); //이벤트 감지기(리스너)
		cb2.addItemListener(new EventHandle());
		cb3.addItemListener(new ItemListener() { //체크박스의 상태 변경시 호출됨!{
			@Override
			public void itemStateChanged(ItemEvent e) { //익명클래스로 리스너에 처리기 달기
				Checkbox cb = (Checkbox)e.getSource(); //이벤트 발생된 부분의 체크박스 상태 가지고옴
				String color = cb.getLabel(); //이벤트 발생된 부분의 체크박스 이름 가져오기
				
				if(color.equals("red")) //선택된 체크박스 이름 확인
				{
					setBackground(Color.red); //프레임의 배경색 변경
					cb1.setBackground(Color.red); //체크박스들의 배경색 변경
					cb2.setBackground(Color.red);
					cb3.setBackground(Color.red);
				}
				else if(color.equals("green"))
				{
					setBackground(Color.green);
					cb1.setBackground(Color.green);
					cb2.setBackground(Color.green);
					cb3.setBackground(Color.green);
				}
				else
				{
					setBackground(Color.blue);
					cb1.setBackground(Color.blue);
					cb2.setBackground(Color.blue);
					cb3.setBackground(Color.blue);
				}
				
			}
		});
		
		
		addWindowListener(new WindowAdapter() { //익명클래스로 윈도우어뎁터 달기
			public void windowClosing(WindowEvent we) //닫기버튼 눌릴시 발생
			{
				System.exit(0); //종료
			}
		});
		setLayout(new FlowLayout());
		add(cb1);
		add(cb2);
		add(cb3);
		setBackground(Color.red);
		setSize(400,200);
		setVisible(true);
	}
	
	public static void main(String[] args) {
		CheckboxEventTest2 mainWin = new CheckboxEventTest2("CheckboxEventTest2");

	}
	class EventHandle implements ItemListener //체크박스의 상태 변경시 호출됨!
	{
		@Override
		public void itemStateChanged(ItemEvent e) {
			Checkbox cb = (Checkbox)e.getSource(); //이벤트 발생된 부분의 체크박스 상태 가지고옴
			String color = cb.getLabel(); //이벤트 발생된 부분의 체크박스 이름 가져오기
			
			if(color.equals("red")) //선택된 체크박스 이름 확인
			{
				setBackground(Color.red); //프레임의 배경색 변경
				cb1.setBackground(Color.red); //체크박스들의 배경색 변경
				cb2.setBackground(Color.red);
				cb3.setBackground(Color.red);
			}
			else if(color.equals("green"))
			{
				setBackground(Color.green);
				cb1.setBackground(Color.green);
				cb2.setBackground(Color.green);
				cb3.setBackground(Color.green);
			}
			else
			{
				setBackground(Color.blue);
				cb1.setBackground(Color.blue);
				cb2.setBackground(Color.blue);
				cb3.setBackground(Color.blue);
			}
			
		}
	}

}
