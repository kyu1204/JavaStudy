public class Buyer 
{
	int money = 1000; //소유 금액
	int bonusPoint = 0;//보너스 점수
	
	//void buy(Tv p){} 이런식으로 구현가능하지만
	//매개변수를 조상타입으로 정의하면 외부에서 인수가 자식타입인 경우
	//자동형변환(up-casting)되어 에러나지 않음
	
	void buy(Product p) //멤버 메소드(인스턴스 메소드:객체 생성해야만 접근 가능)
	{
		if(money < p.price)
		{
			System.out.println("잔액 부족!");
			return;
		}
		
		money -= p.price;
		bonusPoint += p.bonusPoint;
		System.out.println(p+"을/를 구입하였습니다.");
		//p는 참조변수라 toString()을 호출함
		
	}
}
