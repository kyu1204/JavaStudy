import java.awt.*; //������Ʈ ����� ����
import java.awt.event.*; //�̺�Ʈ ó���� ����

public class TextComponentEventTest extends Frame { //Frame�� ��ӹ޾� ���
	TextField tf = new TextField(); //TextField
	TextArea ta = new TextArea(); //TextArea
	
	private class WindowHandle extends WindowAdapter{
		@Override
		public void windowClosing(WindowEvent e) {
			System.exit(0);
		}
	}
	
	public TextComponentEventTest(String title)
	{
		super(title); //�Ű����� �ִ� �����ڸ� ȣ��(title)
		
		tf = new TextField(); //TextField ����
		ta = new TextArea(); //TextArea ����
		ta.setEnabled(false); //TextArea �����Ұ��ϰ� ����
		add(ta,"Center"); //�����̳ʿ� ������Ʈ �߰�
		add(tf,"South");
		
		tf.addActionListener(new ActionListener() {
			@Override //TextField�� �̺�Ʈ������ �߰� �� ����Ŭ������ �̺�Ʈ�ڵ鷯 ����
			public void actionPerformed(ActionEvent e) { //TextField�� Enter �Է½� �߻�
				ta.append(tf.getText()+"\n"); //TextArea�� TextField�� ������ �ؽ�Ʈ �߰�
				tf.setText(""); //TextField ����
				tf.requestFocus(); //Focus �̵�
			}
		});
		
		addWindowListener(new WindowHandle()); //Window.Closing�� ���� ������ ����
		setSize(500,300); //������ ũ�� ����
		setVisible(true); //������ ���̱�
		tf.requestFocus(); //��Ŀ�� TextField��
	}
	public static void main(String[] args) {
		TextComponentEventTest mainWin = 
				new TextComponentEventTest("TextComponentEventTest");

	}

}
