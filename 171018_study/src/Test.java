import java.util.*; //Random Ŭ���� ����ϱ� ����

//����ó��
//random()�� ����Ͽ� 0~10 ������ ���ڸ� n2�� �����ϰ�
//n/n2�� ���� result�� �����Ͽ� �����ϱ�
//���� �߻��� �� �� �ְ� ���� �� �� ����!
public class Test 
{

	public static void main(String[] args) 
	{
		int result, n = 10, n2, n3;
		n2 =(int)(Math.random()*11);//0~10 ������ �߻�
		
		Random r = new Random();//RandomŬ���� Ÿ���� r ���� �� �ʱ�ȭ(��ü����)
		n3 = r.nextInt(10);//nextInt():���� ������ ���ڷ� �����ϸ� 0~�������� ���� ���� ����
						   //�� 0~10���� ���� �߻�
						   //1~10���� �ϰ� ������ n3=r.nextInt(9)+1;�� �ۼ�
		
		try // ���� �߻� ���ɼ� �ִ� �ڵ� �ۼ�
		{
			result = n / n2;
			System.out.println(n+"/"+n2+"="+result);
		}
		catch(ArithmeticException e) //����� ���� �߻��� ó��
		{
			System.out.println("��� ���� �߻�");
		}
		catch(Exception e) //try���� ���� �߻��� catch�� ����(����ó�� �ְ����� Ŭ����:Exception)
		{
			System.out.println("��Ÿ ���� �߻�");
		}
		finally//���ܰ� �߻��ص� ���ص� �� ó���ؾ� �ϴ� �κ� �ۼ�
		{
			System.out.println("finally...");
		}
		System.out.println("�����մϴ�..");
		
	}

}
