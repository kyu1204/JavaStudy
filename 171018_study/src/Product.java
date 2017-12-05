public class Product 
{
	int price; //제품가격 (멤버변수,인스턴스 변수 선언)
	int bonusPoint; //보너스점수
	
	Product(int price) //매개변수 있는 생성자(초기화부분) 정의
	{
		this.price = price;
		bonusPoint = (int)(price/10.0); //가격의 10% 부분
	}

}
