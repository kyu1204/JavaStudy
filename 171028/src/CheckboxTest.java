import java.awt.*; //awt import
public class CheckboxTest 
{
	public static void main(String[] args) 
	{
		Frame f = new Frame("Questions"); //Frame ��ü ���� Ÿ��Ʋ(Questions)
		f.setSize(305, 280); //ũ�⼳��
		f.setLayout(new FlowLayout());
		
		Label q1 = new Label(" 1.����� ���� �оߴ�? (������ ���ð���) ",Label.CENTER); //�󺧼��� (��� ����)
		Checkbox news = new Checkbox("����",true); //Checkbox ���� �Ǿ��ִ� ���·� ����
		Checkbox sports = new Checkbox("������"); //Checkbox ���� �Ǿ� ���� ���� ���·� ����
		Checkbox movies = new Checkbox("��ȭ");
		Checkbox music = new Checkbox("����");
		f.add(q1); f.add(news); f.add(sports); f.add(movies); f.add(music); //Frame�� ���
		
		Label q2 = new Label(" 2.�󸶳� ���� ���忡 ���ʴϱ�? ",Label.CENTER);
		CheckboxGroup group1 = new CheckboxGroup(); //CheckboxGroup ���� (������ư���� ��������)
		Checkbox movie1 = new Checkbox("�� �޿� �� �� ���ϴ�.   ",group1,true); //Checkbox ���õǾ��ִ� ���·� ����(���� ��ư)
		Checkbox movie2 = new Checkbox("�����Ͽ� �� �� ���ϴ�. ",group1,false);
		Checkbox movie3 = new Checkbox("�����Ͽ� �� �� ���ϴ�. ",group1,false);
		f.add(q2); f.add(movie1); f.add(movie2); f.add(movie3);
		
		Label q3 = new Label(" 3.�Ϸ翡 ��ӳ� ��ǻ�͸� ����Ͻʴϱ�? ",Label.CENTER);
		CheckboxGroup group2 = new CheckboxGroup();
		Checkbox com1 = new Checkbox("5�ð� ����",group2,true);
		Checkbox com2 = new Checkbox("10�ð� ����",group2,false);
		Checkbox com3 = new Checkbox("15�ð� �̻�",group2,false);
		f.add(q3); f.add(com1); f.add(com2); f.add(com3);
		
		f.setVisible(true); //Frame�� ���̰� ����
	}
}
