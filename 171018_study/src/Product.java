public class Product 
{
	int price; //��ǰ���� (�������,�ν��Ͻ� ���� ����)
	int bonusPoint; //���ʽ�����
	
	Product(int price) //�Ű����� �ִ� ������(�ʱ�ȭ�κ�) ����
	{
		this.price = price;
		bonusPoint = (int)(price/10.0); //������ 10% �κ�
	}

}
