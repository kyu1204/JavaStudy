
public class Test3 
{
	public static void main(String[] args) 
	{
		Buyer b = new Buyer(); //객체생성
		Tv tv = new Tv(); //객체생성
		Computer com = new Computer(); //객체생성
		
		b.buy(tv);
		b.buy(com);
		
		System.out.println("남은 돈:"+b.money+"만 원");
		System.out.println("현재 보너스 점수:"+b.bonusPoint+"점");
	}
}
