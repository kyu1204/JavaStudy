import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class ServerStart {
	// ================
	// 변수들.
	// ================
	public static HashMap<String, SelectionKey> LoginMap; // 로그인할때 Channel 값저장.
	public static HashMap<Long, LinkedList<String>> GroupMap; // 그룹화할때 사용자들 그룹.
	public static JSFrame Frame = null; // GUI.
	private long GroupCnt = 0; // 그룹번호.
	private long GroupCheck = 0; // 3명이상올라갔을시 다음 그룹번호로.

	public ServerStart() {
	}

	public void ServerOn() {
		new JNetServer();
		LoginMap = new HashMap<String, SelectionKey>();
		GroupMap = new HashMap<Long, LinkedList<String>>();
	}

	// 받은 이벤트처리
	protected void RegMessage(SelectionKey NetId, ByteBuffer RecvBuf, JNetServer JServer) {
		if (RecvBuf.limit() == 0) {
			/* 재전송 */}
		// 패킷생성.
		JNetPacket RecvPacket = new JNetPacket();
		// 버퍼 포지션값 초기화->처음부터읽을수 있도록.
		RecvBuf.flip();
		JSFrame.RegCnt();
		String Msg_1, Msg_2, Msg_3, Msg_4, Msg_5 = "";
		switch (RecvBuf.getShort()) {
		case JNetPacket.LOGIN:
			Msg_1 = JNetPacket.RecvOut(RecvBuf);
			LoginMap.put(Msg_1, NetId);
			RecvPacket.Type(JNetPacket.LOGININFOR);
			RecvPacket.Add(LoginMap.size());
			LoginState(RecvPacket);
			JServer.SendAll(RecvPacket);
			break;
		case JNetPacket.CHAT:
			Msg_1 = JNetPacket.RecvOut(RecvBuf);
			Msg_2 = JNetPacket.RecvOut(RecvBuf);
			RecvPacket.Type(JNetPacket.CHAT);
			RecvPacket.Add(Msg_1);
			RecvPacket.Add(Msg_2);
			// 로그인한 사람들에게 보내기
			JServer.SendAll(RecvPacket);
			break;
		case JNetPacket.TESTPACKET:
			Msg_1 = JNetPacket.RecvOut(RecvBuf);
			Msg_2 = JNetPacket.RecvOut(RecvBuf);
			RecvPacket.Type(JNetPacket.TESTPACKET);
			RecvPacket.Add(Msg_1);
			RecvPacket.Add(Msg_2);
			// 자기자신만 보내는 부분.
			JServer.Send(RecvPacket, NetId);
			break;
		case JNetPacket.GROUPWAIT:
			// 그룹대기,이름
			Msg_1 = JNetPacket.RecvOut(RecvBuf);
			LinkedList<String> List = new LinkedList<String>();
			// null일경우(값을 처음 넣을 경우.
			if (GroupMap.get(GroupCnt) == null) {
				List.add(Msg_1);
				GroupMap.put(GroupCnt, List);
			} else if (GroupMap.get(GroupCnt) != null) {
				List.addAll(GroupMap.get(GroupCnt));
				List.add(Msg_1);
				GroupMap.put(GroupCnt, List);
			}
			GroupCheck++;
			// 3명이상 올라갈시 다음 그룹으로 넘어감.
			if (GroupCheck % 3 == 0) {
				GroupCnt++;
			}
			JServer.RecvSend(NetId);
			break;
		case JNetPacket.GROUPSEND:
			// 그룹전송
			Msg_1 = JNetPacket.RecvOut(RecvBuf);
			Msg_2 = JNetPacket.RecvOut(RecvBuf);
			RecvPacket.Type(JNetPacket.GROUPSEND);
			RecvPacket.Add(Msg_1);
			RecvPacket.Add(Msg_2);
			// 그룹으로 전송
			JServer.SendGroup(RecvPacket, Msg_1);
			break;
		case JNetPacket.DISCONNECT:
			// 연결해제
			JServer.disConnect(NetId);
			break;
		default:
			JServer.RecvSend(NetId);
			break;
		}
	}

	// 로그인할때마다 로그인한 사용자들이 누군지 알려주기위함
	private void LoginState(JNetPacket Packet) {
		Iterator<String> itr = LoginMap.keySet().iterator();
		while (itr.hasNext()) {
			Packet.Add((String) itr.next());
		}
	}

	public static void main(String args[]) {
		Frame = new JSFrame();
	}
}

// GUI부분
@SuppressWarnings("serial")
class JSFrame extends JFrame implements ActionListener, Runnable {
	// ================
	// 변수들.
	// ================
	JLabel conectTitle, conectLabel;
	JLabel regTitle, regLabel;
	JLabel disTitle, disLabel;
	JLabel memoryTitle, memoryLabel;
	JLabel nameLabel;
	JLabel ipLabel;
	JLabel timeLabel;
	JButton startButton;
	JButton endButton;
	public static long conectCnt = 0; // 연결한클라이언트Count
	public static long regCnt = 0; // 클라이언트간의 송수신메시지 Count
	public static long disconetCnt = 0; // 연결해제된 클라이언트 Count
	private long memoryUse = 0; // 메모리 사용량
	private short useTimeMin = 0; // 서버가 가동된 시간(분)
	private short useTimeSec = 0; // 서버가 가동된 시간(초)
	private JFrame MainFrame = new JFrame("-JServer 2.0 version-");
	private Container contentPane = MainFrame.getContentPane();
	// Server Class
	private ServerStart SerOn = null;

	public JSFrame() {
		Font font = new Font("궁서", Font.ITALIC, 20);
		Font font1 = new Font("돋움", Font.BOLD, 15);

		Color grayColor = new Color(62, 57, 64);
		Color orangeColor = new Color(255, 221, 207);
		Color blueColor = new Color(74, 197, 232);
		Color greenColor = new Color(58, 232, 67);

		JPanel northPanel = new JPanel(new GridLayout(2, 1));
		nameLabel = new JLabel("JSieun Server OFF..", JLabel.CENTER);
		ipLabel = new JLabel("ip..", JLabel.CENTER);
		nameLabel.setForeground(Color.white);
		ipLabel.setForeground(Color.WHITE);
		northPanel.add(nameLabel);
		northPanel.add(ipLabel);
		northPanel.setOpaque(false);

		JPanel centerPanel = new JPanel();
		centerPanel.setOpaque(false);

		JPanel statePanel = new JPanel(new GridLayout(4, 2));
		statePanel.setPreferredSize(new Dimension(300, 200));
		statePanel.setBackground(orangeColor);

		conectTitle = new JLabel("-Connect : ", JLabel.LEFT);
		conectLabel = new JLabel("" + conectCnt, JLabel.CENTER);
		disTitle = new JLabel("-DisConect : ", JLabel.LEFT);
		disLabel = new JLabel("" + disconetCnt, JLabel.CENTER);
		regTitle = new JLabel("-RegMsg : ", JLabel.LEFT);
		regLabel = new JLabel("" + regCnt, JLabel.CENTER);
		memoryTitle = new JLabel("-MemUse : ", JLabel.LEFT);
		memoryLabel = new JLabel("" + memoryUse, JLabel.CENTER);
		timeLabel = new JLabel("..start time..", JLabel.CENTER);

		conectTitle.setForeground(grayColor);
		conectLabel.setForeground(grayColor);
		disTitle.setForeground(Color.red);
		disLabel.setForeground(Color.RED);
		regTitle.setForeground(greenColor);
		regLabel.setForeground(greenColor);
		memoryTitle.setForeground(Color.BLUE);
		memoryLabel.setForeground(Color.BLUE);
		timeLabel.setForeground(Color.WHITE);

		conectTitle.setFont(font);
		conectLabel.setFont(font);
		disTitle.setFont(font);
		disLabel.setFont(font);
		regTitle.setFont(font);
		regLabel.setFont(font);
		memoryTitle.setFont(font);
		memoryLabel.setFont(font);
		timeLabel.setFont(font1);

		statePanel.add(conectTitle);
		statePanel.add(conectLabel);
		statePanel.add(disTitle);
		statePanel.add(disLabel);
		statePanel.add(regTitle);
		statePanel.add(regLabel);
		statePanel.add(memoryTitle);
		statePanel.add(memoryLabel);

		centerPanel.add(statePanel, "Center");
		centerPanel.add(timeLabel, "South");

		JPanel southPanel = new JPanel();
		startButton = new JButton("START");
		endButton = new JButton("EXIT");

		startButton.setBackground(blueColor);
		startButton.setForeground(Color.white);
		endButton.setBackground(blueColor);
		endButton.setForeground(Color.white);

		southPanel.setOpaque(false);
		startButton.addActionListener(this);
		endButton.addActionListener(this);
		endButton.setEnabled(false);

		southPanel.add(startButton);
		southPanel.add(endButton);

		MainFrame.add(northPanel, "North");
		MainFrame.add(centerPanel, "Center");
		MainFrame.add(southPanel, "South");
		MainFrame.setBackground(grayColor);

		// 아이콘 넣기
		Toolkit kit = MainFrame.getToolkit();
		Image icon = kit.createImage("icon/icon.png");
		MainFrame.setIconImage(icon);
		// 적절한GUI크기가 나오게끔 정렬.
		MainFrame.pack();
		MainFrame.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		Dimension dmen = kit.getScreenSize();
		contentPane.setBackground(grayColor);
		MainFrame.setLocation((int) ((dmen.width - getSize().width) / 8), (int) ((dmen.height - getSize().height) / 8));
		MainFrame.setSize(300, 350);
		MainFrame.setResizable(false);
		MainFrame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object o = e.getSource();
		if (o.equals(startButton)) {
			startButton.setEnabled(false);
			endButton.setEnabled(true);
			nameLabel.setText("JSieun Server ON..");
			// 메인서버 실행
			SerOn = new ServerStart();
			// 뼈대서버실행 & 맵들 생성
			SerOn.ServerOn();
			// GUI 쓰레드 실행
			Thread outPut = new Thread(this);
			outPut.start();
			try {
				// IP와 포트번호 보여주기
				// ipLabel.setText("" + JNetServer.LocalIP());
				ipLabel.setText("-IP보호용입니다.-");
			} catch (Exception ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}
		} else if (o.equals(endButton)) {
			JLabel content = new JLabel("게임서버를 종료시키겠습니까?!");
			int confirm = JOptionPane.showConfirmDialog(null, content, "종료확인", JOptionPane.YES_NO_OPTION,
					JOptionPane.WARNING_MESSAGE);
			// 예를 눌렀을경우 종료
			if (confirm == 0) {
				System.exit(0);
			} else {
			}
		}
	}

	@Override
	public void run() {
		Runtime Memory = Runtime.getRuntime();
		DecimalFormat format = new DecimalFormat("###,###,###.##");
		while (true) {
			try {
				// 1초마다 Connect , disConnect , regMessage , 사용시간 정보 갱신
				Thread.sleep(1000);
				regLabel.setText("" + regCnt);
				conectLabel.setText("" + conectCnt);
				disLabel.setText("" + disconetCnt);
				memoryLabel.setText("" + format.format(Memory.totalMemory() - Memory.freeMemory()));
				timeLabel.setText("UseTime : " + useTimeMin + ":" + String.format("%02d", useTimeSec) + " Sec");
				useTimeSec++;
				if (useTimeSec == 60) {
					useTimeMin++;
					useTimeSec = 0;
				}
			} catch (Exception ex) {
			}
		}

	}

	public static void ConectCnt() {
		++conectCnt;
	}

	public static void RegCnt() {
		++regCnt;
	}

	public static void DisConectCnt() {
		++disconetCnt;
	}
}
