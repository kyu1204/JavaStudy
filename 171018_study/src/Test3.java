
public class Test3 
{
	public static void main(String[] args) 
	{
		Buyer b = new Buyer(); //��ü����
		Tv tv = new Tv(); //��ü����
		Computer com = new Computer(); //��ü����
		
		b.buy(tv);
		b.buy(com);
		
		System.out.println("���� ��:"+b.money+"�� ��");
		System.out.println("���� ���ʽ� ����:"+b.bonusPoint+"��");
	}
}
