package Project;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SelectColor extends Dialog implements ChangeListener {
	int r;
	int g;
	int b;
	JSlider sR;
	JSlider sG;
	JSlider sB;
	Panel cPanel;

	public SelectColor(Frame parent, String title, boolean modal) {
		super(parent, title, modal);
		LifeGame par = (LifeGame) getParent();
		r = par.cellColor.getRed();
		g = par.cellColor.getGreen();
		b = par.cellColor.getBlue();

		setSize(270, 250);
		setResizable(false);
		setLayout(new GridLayout(5, 1));
		setIconImage(new ImageIcon("graphic.png").getImage());
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize();
		setLocation(screenSize.width / 2 - getWidth() / 2, screenSize.height / 2 - getHeight() / 2);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});
		Panel pR = new Panel();
		pR.setLayout(new GridLayout(1, 2));
		Label lR = new Label("R:", Label.CENTER);
		sR = new JSlider(JSlider.HORIZONTAL, 0, 255, r);
		sR.addChangeListener(this);
		pR.add(lR);
		pR.add(sR);

		Panel pG = new Panel();
		pG.setLayout(new GridLayout(1, 2));
		Label lG = new Label("G:", Label.CENTER);
		sG = new JSlider(JSlider.HORIZONTAL, 0, 255, g);
		sG.addChangeListener(this);
		pG.add(lG);
		pG.add(sG);

		Panel pB = new Panel();
		pB.setLayout(new GridLayout(1, 2));
		Label lB = new Label("B:", Label.CENTER);
		sB = new JSlider(JSlider.HORIZONTAL, 0, 255, b);
		sB.addChangeListener(this);
		pB.add(lB);
		pB.add(sB);

		Panel bPanel = new Panel();
		bPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 0));
		Button ok = new Button("OK");
		ok.setBounds(50, 200, 50, 30);
		ok.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				RGBcolor rgb = new RGBcolor(r, g, b);

				par.cellColor = rgb.getColor();
				par.check = true;
				dispose();
			}
		});
		Button cancel = new Button("Cancel");
		cancel.setBounds(160, 200, 50, 30);
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		bPanel.add(ok);
		bPanel.add(cancel);

		cPanel = new Panel();
		cPanel.setBackground(new Color(r, g, b));

		add(pR);
		add(pG);
		add(pB);
		add(cPanel);
		add(bPanel);
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		Object ob = e.getSource();
		try {
			if (ob == sR) {
				r = sR.getValue();
			} else if (ob == sG) {
				g = sG.getValue();
			} else if (ob == sB) {
				b = sB.getValue();
			}
		} finally {
			cPanel.setBackground(new Color(r, g, b));
		}
	}

}
