import java.awt.*;
import java.awt.event.*;


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
		
		Label lnick = new Label("�г���:");
		lnick.setBounds(30, 120, 40, 20);
		TextField tnick = new TextField(20);
		tnick.setBounds(80, 120, 100, 20);
		
		Button blogin = new Button("Login");
		blogin.setBounds(200,70,50,50);
		blogin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String ip = tip.getText();
				String port = tport.getText();
				String nick = tnick.getText();
				try {
					Client c = new Client(ip,port,nick);
				}
				catch(Exception e)
				{}
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
		add(lnick);
		add(tnick);
		setResizable(false);
		setSize(300,160);
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize();
		setLocation(screenSize.width/2-getWidth()/2, screenSize.height/2-getHeight()/2);
		setVisible(true);
	}

	public static void main(String[] args) {
		new LoginClient();
	}

}
