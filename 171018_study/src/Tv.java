public class Tv extends Product //Product 클래스를 상속받는 Tv 클래스 정의
{
	Tv()
	{
		super(100); //Product에 기본생성자가 없기때문에 인자를 넘겨줘야함
	}
	
	public String toString() //오버라이딩(Object클래스 메소드)
	{
		return "Tv";
	}
}
