import java.awt.*;

public class ScrollPaneTest 
{
	public static void main(String[] args) 
	{
		Frame f = new Frame("ScrollPaneTest");
		f.setBounds(500,500,300,200);
		
		ScrollPane sp = new ScrollPane();
		//ScrollPane 하나의 컴포넌트만 등록가능한 종속적인 컨테이너
		//제한된 공간에 큰 컴포넌트를 화면에 보여줄때 사용
		Panel p = new Panel(); //Panel 생성
		p.setBackground(Color.green);
		p.add(new Button("첫번째")); //버튼을 패널에 등록
		p.add(new Button("두번째"));
		p.add(new Button("세번째"));
		p.add(new Button("네번째"));
		
		sp.add(p); //Panel을 ScrollPane 에 등록
		f.add(sp); //ScrollPane을 Frame 에 등록
		f.setVisible(true);
	}
}
