import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyledDocument;

@SuppressWarnings("serial")
public class JClient extends JFrame implements ActionListener, AdjustmentListener, ItemListener, Runnable, KeyListener {
	// ================
	// ������.
	// ================
	private JLabel nameLabel;
	private JButton conectButton, disconectButton, packetButton;
	private JButton loginButton, whisperButton, groupWaitButton, groupSendButton;
	// ����â ������
	private JLabel stateLabel;
	private JScrollBar scrollBar;
	private JTextPane statePane;
	private SimpleAttributeSet attriState;
	private StyledDocument docState;
	// �޽��� �Է� ������
	private JTextField editText;
	private JTextField nameText;
	private JButton sendButton;

	// ����ä��
	private Selector Selector = null;
	private SocketChannel Client = null;
	private InetSocketAddress connectAddress;
	private String address = "127.0.0.1";
	private int port = 20000;
	private boolean bStart = true;

	// RecvBuf
	private ByteBuffer RecvBuf;
	// ��Ŷ
	private JNetPacket packet = null;
	// ���� �̸�
	private String UserName = "";

	public JClient() {
		super("J.Sieun Client");
		connectAddress = new InetSocketAddress(address, port);
		packet = new JNetPacket();
		RecvBuf = ByteBuffer.allocate(1000);
	}

	public void initialize() {
		// ����
		JPanel mainPanel = new JPanel();
		nameLabel = new JLabel("Test_Client");
		mainPanel.add(nameLabel);

		// ��ư��
		JPanel buttonPanel = new JPanel(new GridLayout(8, 1));

		conectButton = new JButton("����");
		disconectButton = new JButton("��������");
		loginButton = new JButton("�α���");
		whisperButton = new JButton("�ӼӸ�");
		packetButton = new JButton("��Ŷ����");
		groupWaitButton = new JButton("�׷���");
		groupSendButton = new JButton("�׷�����");

		conectButton.addActionListener(this);
		disconectButton.addActionListener(this);
		loginButton.addActionListener(this);
		whisperButton.addActionListener(this);
		packetButton.addActionListener(this);
		groupWaitButton.addActionListener(this);
		groupSendButton.addActionListener(this);

		buttonPanel.add(conectButton);
		buttonPanel.add(disconectButton);
		buttonPanel.add(loginButton);
		buttonPanel.add(whisperButton);
		buttonPanel.add(packetButton);
		buttonPanel.add(groupWaitButton);
		buttonPanel.add(groupSendButton);
		// ����â
		JPanel statePanel = new JPanel(new BorderLayout());
		stateLabel = new JLabel("\tState Message\t", JLabel.CENTER);

		statePane = new JTextPane();
		statePane.setBorder(BorderFactory.createRaisedBevelBorder());
		statePane.setEditable(false);

		JScrollPane scroll = new JScrollPane(statePane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setOpaque(false);

		scrollBar = scroll.getVerticalScrollBar();
		docState = statePane.getStyledDocument();
		statePanel.add(stateLabel, "North");
		statePanel.add(scroll, "Center");
		// �޽��� �Է�â
		JPanel msgPanel = new JPanel();
		editText = new JTextField(15);
		nameText = new JTextField(8);
		sendButton = new JButton("������");
		sendButton.addActionListener(this);
		msgPanel.add(nameText);
		msgPanel.add(editText);
		msgPanel.add(sendButton);

		// ���� �߰�
		getContentPane().add(mainPanel, "North");
		getContentPane().add(statePanel, "Center");
		getContentPane().add(msgPanel, "South");
		getContentPane().add(buttonPanel, "East");

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		Toolkit kit = getToolkit();
		Dimension dmen = kit.getScreenSize();
		setLocation((int) ((dmen.width - getSize().width) / 2), (int) ((dmen.height - getSize().height) / 2));
		setSize(700, 400);
		setResizable(false);
		setVisible(true);
	}

	@Override
	public void itemStateChanged(ItemEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void adjustmentValueChanged(AdjustmentEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object o = e.getSource();
		if (o.equals(conectButton)) {
			try {
				Connect();
			} catch (Exception ex) {
				stateMsg("�������� ���� : " + ex.getMessage());
			}
			conectButton.setEnabled(false);
		}
		if (o.equals(disconectButton)) {
			packet.Clear();
			packet.Type(JNetPacket.DISCONNECT);
			packet.Add(UserName);
			Send(packet);
			stateMsg("������ �����մϴ�. \n");
			disconectButton.setEnabled(false);
		}
		if (o.equals(packetButton)) {
			packet.Clear();
			packet.Type(JNetPacket.TESTPACKET);
			packet.Add("��Ŷ�׽�Ʈ");
			packet.Add(1001001);
			Send(packet);
		}
		if (o.equals(sendButton)) {
			packet.Clear();
			packet.Type(JNetPacket.CHAT);
			packet.Add(UserName);
			packet.Add("" + editText.getText());
			editText.setText("");
			Send(packet);
		}
		if (o.equals(loginButton)) {
			packet.Clear();
			packet.Type(JNetPacket.LOGIN);
			packet.Add("" + nameText.getText());
			UserName = nameText.getText();
			nameText.setText("");
			nameLabel.setText("" + UserName + " ��");
			Send(packet);
			loginButton.setEnabled(false);
		}
		if (o.equals(whisperButton)) {
			packet.Clear();
			packet.Type(JNetPacket.WHISPER);
			packet.Add(UserName);
			packet.Add("" + nameText.getText());
			packet.Add("" + editText.getText());
			nameText.setText("");
			editText.setText("");
			Send(packet);
		}
		if (o.equals(groupWaitButton)) {
			groupWaitButton.setEnabled(false);
			// �׷���
			packet.Clear();
			packet.Type(JNetPacket.GROUPWAIT);
			packet.Add(UserName);
			Send(packet);
		}
		if (o.equals(groupSendButton)) {
			// �׷�����
			packet.Clear();
			packet.Type(JNetPacket.GROUPSEND);
			packet.Add(UserName);
			packet.Add("" + editText.getText());
			editText.setText("");
			Send(packet);
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		/*
		 * // TODO Auto-generated method stub if(e.getKeyCode() == KeyEvent.VK_ENTER) {
		 * packet.Clear(); packet.Type(JPacket.CHAT); packet.Add(""+editText.getText());
		 * editText.setText(""); Send(packet); }
		 */
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		int keycode = e.getKeyCode();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	private void Connect() {
		try {
			Client = SocketChannel.open();
			// ����ŷ,������ �޴»����� 1000,������ ����ؼ� ����,��ü�� �ٷ� �����͵� ����.
			Client.configureBlocking(false);
			Client.setOption(StandardSocketOptions.SO_RCVBUF, 128 * 1000);
			Client.setOption(StandardSocketOptions.SO_SNDBUF, 128 * 1000);
			Client.setOption(StandardSocketOptions.SO_KEEPALIVE, true);
			Client.setOption(StandardSocketOptions.SO_LINGER, 0);
			// ��û����
			Client.connect(connectAddress);
			stateMsg("����Ϸ� \n");
			Thread thread = new Thread(this);
			thread.start();
		} catch (Exception e) {
			System.out.println("Connect Error : " + e.getMessage());
		}
	}

	public void Send(JNetPacket data) {
		try {
			Client.write(data.getBuf());
			// Thread.sleep(10);
		} catch (Exception e) {
			System.out.println("Send Error : " + e.getMessage());
		}

	}

	public void run() {
		try {
			Selector = java.nio.channels.Selector.open();
			Client.register(Selector, SelectionKey.OP_CONNECT);
		} catch (Exception ex) {
			stateMsg("Run Error : " + ex.getMessage() + "\n");
		}
		while (bStart) {
			try {
				Selector.select();
				Iterator<SelectionKey> keys = Selector.selectedKeys().iterator();
				while (keys.hasNext()) {
					SelectionKey key = keys.next();
					keys.remove();
					if (key.isConnectable()) {
						if (Client.finishConnect()) {
							key.interestOps(SelectionKey.OP_READ);
						} else {
							key.cancel();
						}
					} else if (key.isReadable()) {
						Client.read(RecvBuf);
						RecvEvt();
					}
				}
			} catch (Exception e) {
				stateMsg("Recv Error :" + e.getMessage() + "\n");
				this.bStart = false;
			}
		}
	}

	public void RecvEvt() {
		RecvBuf.flip();
		packet.Clear();
		String ClntMsg_1, ClntMsg_2, ClntMsg_3, ClntMsg_4, ClntMsg_5 = "";
		short Type = RecvBuf.getShort();
		switch (Type) {
		case JNetPacket.LOGIN:
			stateMsg("������ ������ �Ϸ�Ǿ����ϴ�. \n");
			RecvBuf.clear();
			break;
		case JNetPacket.LOGININFOR:
			stateMsg("*****�α��� ����*****\n");
			LoginInfor(RecvBuf);
			RecvBuf.clear();
			break;
		case JNetPacket.DISCONNECT:
			ClntMsg_1 = JNetPacket.RecvOut(RecvBuf);
			stateMsg(ClntMsg_1 + "���� �α׾ƿ��Ͽ����ϴ�. \n");
			RecvBuf.clear();
			break;
		case JNetPacket.CHAT:
			ClntMsg_1 = JNetPacket.RecvOut(RecvBuf);
			ClntMsg_2 = JNetPacket.RecvOut(RecvBuf);
			stateMsg("[" + ClntMsg_1 + "] ���� �� : " + ClntMsg_2 + "\n");
			RecvBuf.clear();
			break;
		case JNetPacket.GROUPSEND:
			ClntMsg_1 = JNetPacket.RecvOut(RecvBuf);
			ClntMsg_2 = JNetPacket.RecvOut(RecvBuf);
			stateMsg("�׷�Msg : [" + ClntMsg_1 + "] ���� �� : " + ClntMsg_2 + "\n");
			RecvBuf.clear();
			break;
		case JNetPacket.WHISPER:
			ClntMsg_1 = JNetPacket.RecvOut(RecvBuf);
			ClntMsg_2 = JNetPacket.RecvOut(RecvBuf);
			stateMsg("[" + ClntMsg_1 + "] ���� �ӼӸ� : " + ClntMsg_2 + "\n");
			RecvBuf.clear();
			break;
		case JNetPacket.TESTPACKET:
			ClntMsg_1 = JNetPacket.RecvOut(RecvBuf);
			ClntMsg_2 = JNetPacket.RecvOut(RecvBuf);
			stateMsg("��Ŷ�׽�Ʈ : " + ClntMsg_1 + " :::: " + ClntMsg_2 + "\n");
			RecvBuf.clear();
			break;
		default:
			RecvBuf.clear();
			break;
		}
	}

	// ���¸޽���
	public void stateMsg(String msg) {
		try {
			attriState = new SimpleAttributeSet();
			statePane.setCaretPosition(docState.getEndPosition().getOffset() - 1);
			docState.insertString(statePane.getCaretPosition(), msg, attriState);
			scrollBar.addAdjustmentListener(this);
		} catch (Exception e) {
			String _errorMsg = "*���¸޽��� �����߻�!!*";
			JOptionPane.showMessageDialog(null, _errorMsg, "JServer", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	// �α��� ����
	public void LoginInfor(ByteBuffer RecvBuf) {
		String strSize = JNetPacket.RecvOut(RecvBuf);
		int Size = Integer.parseInt(strSize);
		for (int i = 0; i < Size; i++) {
			stateMsg((i + 1) + "  " + JNetPacket.RecvOut(RecvBuf) + " �� ");
		}
		stateMsg("\n");
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JClient test = new JClient();
		test.initialize();
	}

}