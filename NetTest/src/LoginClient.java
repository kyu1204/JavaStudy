import java.awt.*;
import java.awt.event.*;
import java.net.InetAddress;
import java.net.UnknownHostException;


public class LoginClient extends Frame{
	private final Frame my = this;
	
	public LoginClient() {
		super("Login");
		setLayout(null);
		Label lip = new Label("IP:");
		lip.setBounds(50, 60, 20, 20);
		TextField tip = new TextField(20);
		tip.setBounds(80, 60, 100, 20);
		Label lport = new Label("Port:");
		lport.setBounds(40, 90, 30, 20);
		TextField tport = new TextField(20);
		tport.setBounds(80, 90, 100, 20);
		Button blogin = new Button("Login");
		blogin.setBounds(200,60,50,50);
		blogin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String ip = tip.getText();
				String port = tport.getText();
				try {
					OmokClient c = new OmokClient();
					c.startClient(ip, port);
				}
				catch(Exception e)
				{
					Dialog alram = new Dialog(my, "WARING!", true);
					Button ok = new Button("확인");
					ok.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							alram.dispose();
						}
					});
					Panel bPanel = new Panel();
					bPanel.setLayout(new FlowLayout());
					bPanel.add(ok);
					alram.add(new Label("     서버가 꺼져 있습니다!"),"Center");
					alram.add(bPanel,"South");
					alram.addWindowListener(new WindowAdapter() {
						@Override
						public void windowClosing(WindowEvent e) {
							alram.dispose();
						}
					});
					alram.setSize(150, 150);
					Toolkit tk = Toolkit.getDefaultToolkit();
					Dimension screenSize = tk.getScreenSize();
					alram.setLocation(screenSize.width/2-alram.getWidth()/2, screenSize.height/2-alram.getHeight()/2);
					alram.setVisible(true);
				}
				finally {
					my.dispose();
				}
			}
		});
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				System.exit(0);
			}
		});
		add(lip);
		add(tip);
		add(lport);
		add(tport);
		add(blogin);
		setResizable(false);
		setSize(300,150);
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize();
		setLocation(screenSize.width/2-getWidth()/2, screenSize.height/2-getHeight()/2);
		setVisible(true);
	}

	public static void main(String[] args) {
		new LoginClient();
	}

}
