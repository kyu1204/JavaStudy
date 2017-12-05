import java.awt.*; //������Ʈ ����� ����
import java.awt.event.*; //�̺�Ʈ ó���� ����


public class MouseEventTest extends Frame { //Frame�� ��ӹ޾� ���

	Label location; //�� 
	public MouseEventTest(String title) 
	{
		super(title); //�Ű����� �ִ� �����ڷ� Ÿ��Ʋ�� �޾Ƽ� ����Ŭ������ Frame�� �Ű����� �ִ� ������ ȣ��
		
		location = new Label("Mouse Pointer Location: "); //�� ��ü ����
		location.setBounds(10, 35, 195, 15); //label ��ġ �� ũ�� ����
		location.setBackground(Color.yellow); //bg�÷� Yellow
		add(location); //�����̳ʿ� �߰�

		addMouseMotionListener(new MouseEventHandle());
		
		addWindowListener(new MouseEventHandle());
		setLayout(null); //Layout ��� x
		setSize(400,300); //�������� ������
		setVisible(true); //������ ���̱�
	}
	
	public static void main(String[] args) {
		MouseEventTest mt = new MouseEventTest("MouseEventTest");

	}
	
	private class MouseEventHandle extends WindowAdapter implements MouseMotionListener {
		
		public void windowClosing(WindowEvent e) {
			System.exit(0);
		}
		@Override
		public void mouseDragged(MouseEvent e) {}

		@Override
		public void mouseMoved(MouseEvent e) {
			location.setText("Mouse Pointer Location: ("+e.getX()+", "+e.getY()+")");
			
		}
		
	}

}
