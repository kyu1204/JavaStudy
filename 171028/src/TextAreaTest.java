import java.awt.*;
public class TextAreaTest 
{
	public static void main(String[] args) 
	{
		Frame f = new Frame("Comments");
		f.setBounds(500,500,400,220);
		f.setLayout(new FlowLayout());
		
		//TextArea (���� �ؽ�Ʈ, row, column) scrollbar�� ��� ���-default
		TextArea comments = new TextArea("�ϰ� ���� ���� ��������.",10,50);
		comments.selectAll(); //TextArea�� �ִ� ����ؽ�Ʈ �巡��(����)
		
		f.add(comments);
		f.setVisible(true);
	}
}
