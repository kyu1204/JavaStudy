import java.awt.*;
public class TextAreaTest 
{
	public static void main(String[] args) 
	{
		Frame f = new Frame("Comments");
		f.setBounds(500,500,400,220);
		f.setLayout(new FlowLayout());
		
		//TextArea (넣을 텍스트, row, column) scrollbar는 모두 사용-default
		TextArea comments = new TextArea("하고 싶은 말을 적으세요.",10,50);
		comments.selectAll(); //TextArea에 있는 모든텍스트 드래그(선택)
		
		f.add(comments);
		f.setVisible(true);
	}
}
