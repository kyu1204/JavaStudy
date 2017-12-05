import java.awt.*;
public class DialogTest
{
	public static void main(String[] args) 
	{
		Frame f = new Frame("Parent");
		f.setBounds(500,500,300,200);
		
		Dialog info = new Dialog(f,"Information",true);
		//Dialog 독립적인 컨테이너 주로 메세지창으로 사용
		//인자로 parent-어느 Frame에 속할것인지, title, modal-모달 or 모달리스
		info.setBounds(50, 50, 140, 110); //위치 및 크기
		info.setLayout(new FlowLayout());
		
		Label msg = new Label("This is modal Dialog",Label.CENTER);
		Button ok = new Button("OK");
		info.add(msg); info.add(ok); //Dialog 에 등록(독립적인 컨테이너)
		
		f.setVisible(true);
		info.setVisible(true); //Dialog를 바로 띄운다
	}
}
