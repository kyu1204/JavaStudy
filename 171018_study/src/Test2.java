
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
			System.out.println("main �޼ҵ��� catch�Դϴ�.");
		}
	}
	
	static void method1() throws Exception//method1���� �߻������� ���ܴ� Exception!
	{
		try
		{
			throw new Exception(); //throw�� ������ ���ܹ߻���Ŵ!
		}
		catch(Exception e)
		{
			System.out.println("method1 �޼ҵ��� catch�Դϴ�.");
			throw e;
		}
	}
}
