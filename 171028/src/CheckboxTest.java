import java.awt.*; //awt import
public class CheckboxTest 
{
	public static void main(String[] args) 
	{
		Frame f = new Frame("Questions"); //Frame 객체 생성 타이틀(Questions)
		f.setSize(305, 280); //크기설정
		f.setLayout(new FlowLayout());
		
		Label q1 = new Label(" 1.당신의 관심 분야는? (여러개 선택가능) ",Label.CENTER); //라벨설정 (가운데 정렬)
		Checkbox news = new Checkbox("뉴스",true); //Checkbox 선택 되어있는 상태로 생성
		Checkbox sports = new Checkbox("스포츠"); //Checkbox 선택 되어 있지 않은 상태로 생성
		Checkbox movies = new Checkbox("영화");
		Checkbox music = new Checkbox("음악");
		f.add(q1); f.add(news); f.add(sports); f.add(movies); f.add(music); //Frame에 등록
		
		Label q2 = new Label(" 2.얼마나 자주 극장에 가십니까? ",Label.CENTER);
		CheckboxGroup group1 = new CheckboxGroup(); //CheckboxGroup 생성 (라디오버튼으로 묶기위해)
		Checkbox movie1 = new Checkbox("한 달에 한 번 갑니다.   ",group1,true); //Checkbox 선택되어있는 상태로 생성(라디오 버튼)
		Checkbox movie2 = new Checkbox("일주일에 한 번 갑니다. ",group1,false);
		Checkbox movie3 = new Checkbox("일주일에 두 번 갑니다. ",group1,false);
		f.add(q2); f.add(movie1); f.add(movie2); f.add(movie3);
		
		Label q3 = new Label(" 3.하루에 얼머나 컴퓨터를 사용하십니까? ",Label.CENTER);
		CheckboxGroup group2 = new CheckboxGroup();
		Checkbox com1 = new Checkbox("5시간 이하",group2,true);
		Checkbox com2 = new Checkbox("10시간 이하",group2,false);
		Checkbox com3 = new Checkbox("15시간 이상",group2,false);
		f.add(q3); f.add(com1); f.add(com2); f.add(com3);
		
		f.setVisible(true); //Frame을 보이게 설정
	}
}
