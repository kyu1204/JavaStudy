public class Buyer 
{
	int money = 1000; //���� �ݾ�
	int bonusPoint = 0;//���ʽ� ����
	
	//void buy(Tv p){} �̷������� ��������������
	//�Ű������� ����Ÿ������ �����ϸ� �ܺο��� �μ��� �ڽ�Ÿ���� ���
	//�ڵ�����ȯ(up-casting)�Ǿ� �������� ����
	
	void buy(Product p) //��� �޼ҵ�(�ν��Ͻ� �޼ҵ�:��ü �����ؾ߸� ���� ����)
	{
		if(money < p.price)
		{
			System.out.println("�ܾ� ����!");
			return;
		}
		
		money -= p.price;
		bonusPoint += p.bonusPoint;
		System.out.println(p+"��/�� �����Ͽ����ϴ�.");
		//p�� ���������� toString()�� ȣ����
		
	}
}
