import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class LoginServer extends Frame{
	private final Frame my = this;
	public LoginServer() {
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
				new Server(ip,port);
				my.dispose();
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
		setVisible(true);
	}

	public static void main(String[] args) {
		new LoginServer();
	}

}
