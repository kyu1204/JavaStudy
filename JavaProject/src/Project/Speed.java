package Project;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Speed extends Dialog implements ChangeListener{

	LifeGame parent;
	int speedvalue;
	Label lValue;
	JSlider spSlider;
	Speed(Frame p,String title,boolean modal)
	{
		super(p,title,modal);
		
		parent = (LifeGame)getParent();
		speedvalue = parent.t.getDelay();
		
		setSize(350,200);
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize();
		setLocation(screenSize.width/2-getWidth()/2, screenSize.height/2-getHeight()/2);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});
		
		Panel psp = new Panel();
		psp.setLayout(new BorderLayout());
		Label lSpeed = new Label("성장속도");
		lSpeed.setFont(new Font("Selif",Font.BOLD,25));
		lValue = new Label(String.valueOf(speedvalue));
		spSlider = new JSlider(JSlider.HORIZONTAL, 1, 1000, 100);
		spSlider.addChangeListener(this);
		psp.add(spSlider,"Center");
		psp.add(lSpeed, "North");
		psp.add(lValue, "West");
		
		add(psp,"Center");
		setVisible(true);
	}
	@Override
	public void stateChanged(ChangeEvent e) {
		JSlider ob = (JSlider)e.getSource();
		speedvalue = ob.getValue();
		lValue.setText(String.valueOf(speedvalue));
		parent.t.setDelay(speedvalue);
	}
}
