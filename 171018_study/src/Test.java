import java.util.*; //Random 클래스 사용하기 위해

//예외처리
//random()을 사용하여 0~10 사이의 숫자를 n2에 대입하고
//n/n2의 값을 result에 대입하여 실행하기
//예외 발생할 수 도 있고 안할 수 도 있음!
public class Test 
{

	public static void main(String[] args) 
	{
		int result, n = 10, n2, n3;
		n2 =(int)(Math.random()*11);//0~10 랜덤수 발생
		
		Random r = new Random();//Random클래스 타입의 r 선언 및 초기화(객체생성)
		n3 = r.nextInt(10);//nextInt():난수 범위를 인자로 전달하면 0~전달인자 까지 난수 생성
						   //즉 0~10까지 난수 발생
						   //1~10까지 하고 싶으면 n3=r.nextInt(9)+1;로 작성
		
		try // 예외 발생 가능성 있는 코드 작성
		{
			result = n / n2;
			System.out.println(n+"/"+n2+"="+result);
		}
		catch(ArithmeticException e) //산술적 예외 발생시 처리
		{
			System.out.println("산술 예외 발생");
		}
		catch(Exception e) //try에서 예외 발생시 catch문 실행(예외처리 최고조상 클래스:Exception)
		{
			System.out.println("기타 예외 발생");
		}
		finally//예외가 발생해도 안해도 꼭 처리해야 하는 부분 작성
		{
			System.out.println("finally...");
		}
		System.out.println("종료합니다..");
		
	}

}
