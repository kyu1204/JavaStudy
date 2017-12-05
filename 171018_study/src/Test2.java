
public class Test2 
{
	public static void main(String[] args) 
	{
		try
		{
			method1();
		}
		catch(Exception e)
		{
			System.out.println("main 메소드의 catch입니다.");
		}
	}
	
	static void method1() throws Exception//method1에서 발생가능한 예외는 Exception!
	{
		try
		{
			throw new Exception(); //throw는 강제로 예외발생시킴!
		}
		catch(Exception e)
		{
			System.out.println("method1 메소드의 catch입니다.");
			throw e;
		}
	}
}
