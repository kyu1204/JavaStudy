import java.awt.*; //awt import
public class ScrollbarTest 
{
	public static void main(String[] args) 
	{
		Frame f = new Frame("Scrollbar"); //Frame생성 타이틀(Scrollbar)
		f.setBounds(500, 500, 300, 200); //Frame의 위치,크기 설정
		f.setLayout(null); //Layout 미설정
		
		Scrollbar hor = new Scrollbar(Scrollbar.HORIZONTAL,0,50,0,100); //Scrollbar 생성(수평),초기값 ,스크롤버튼 크기, 최소값, 최대값
		hor.setBounds(150, 50, 100, 15); //위치 및 크기 설정
		f.add(hor);
		
		Scrollbar ver = new Scrollbar(Scrollbar.VERTICAL,0,50,0,100);//Scrollbar 생성(수직)
		ver.setBounds(50, 50, 15, 100);
		f.add(ver);
		
		f.setVisible(true);
	}

}
