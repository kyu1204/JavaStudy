import java.awt.*;
public class PanelTest 
{
	public static void main(String[] args) 
	{
		Frame f = new Frame("Panel"); //Frame생성
		f.setBounds(500,500,300,200); //위치 및 크기
		f.setLayout(null); //Layout 미설정
		
		Panel p = new Panel(); //Panel 생성 종속적인 컨테이너, 독립적으로 존재 불가
								//그러나 컨테이너이기 때분에 컴포넌트 등록가능
		p.setBackground(Color.green);//bgColor green
		p.setBounds(50,50,100,100);//위치 및 크기
		
		Button ok = new Button("ok");
		
		p.add(ok); //Panel컨테이너에 Button컴포넌트 등록
		f.add(p); //종속적인 컨테이너라서 Frame에 Panel 등록
		f.setVisible(true);
	}
}
