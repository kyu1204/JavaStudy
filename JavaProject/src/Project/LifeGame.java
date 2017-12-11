package Project;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

import com.sun.org.apache.xml.internal.serializer.SerializerTrace;

public class LifeGame extends Frame {
	private Cell c;
	public Timer t;
	public Color cellColor = Color.BLACK;
	public boolean check = false;
	private static int generation = 0;
	private final Frame f = this;
	Image img_buffer =null;
	Graphics buffer = null;
	
	class MouseEventHandle extends MouseAdapter {
		@Override
		public void mousePressed(MouseEvent e) {
			int x = e.getX() / 10;
			int y = e.getY() / 10;
			if (e.getButton() == MouseEvent.BUTTON1) {
				if (c.cell.get(y).get(x) == true)
					c.cell.get(y).set(x, false);
				else {
					c.cell.get(y).set(x, true);
					c.CreateChecklist(y, x);
				}
			}

			if (e.getButton() == MouseEvent.BUTTON3) {
				if (t.isRunning())
					t.stop();
				else
					t.start();
			}
			repaint();
		}
	}

	class ResizeEventHandle extends ComponentAdapter {
		@Override
		public void componentResized(ComponentEvent e) {
			if (c == null)
				return;
			if (c.cell.size() != 0) {
				int h = getHeight() - c.ori_height;
				int w = getWidth() - c.ori_width;

				if (h > 0) {
					for (int i = h; i > 0; --i) {
						c.cell.add(new ArrayList<Boolean>());
						for (int j = 0; j < c.ori_width; ++j) {
							c.cell.get(getHeight() - i).add(false);
						}
					}
				}
				if (w > 0) {
					for (int i = 0; i < getHeight(); ++i) {
						for (int j = 0; j < w; ++j) {
							c.cell.get(i).add(false);
						}
					}
				}
			}
			c.ori_height = getHeight();
			c.ori_width = getWidth();

			repaint();
		}
	}

	LifeGame() {
		super(Integer.toString(generation) + " 세대");
		setSize(500, 500);
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize();
		setLocation(screenSize.width/2-getWidth()/2, screenSize.height/2-getHeight()/2);
		setIconImage(new ImageIcon("cell.jpg").getImage());

		c = new Cell(getWidth(), getHeight());

		addMouseListener(new MouseEventHandle());
		addComponentListener(new ResizeEventHandle());
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});
		MenuBar mb = new MenuBar();
		Menu mColor = new Menu("Color");
		MenuItem miColor = new MenuItem("Select Color",new MenuShortcut('C'));
		miColor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SelectColor cd = new SelectColor(f,"Select Color",true);
				cd.setVisible(true);
				if(check == true)
					repaint();
			}
		});
		Menu mHelp = new Menu("Help");
		MenuItem miHelp = new MenuItem("Help",new MenuShortcut('H'));
		miHelp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new LifeGameHelp(f,"Life Game Help",true).setVisible(true);
			}
		});
		Menu mSpeed = new Menu("Speed");
		MenuItem miSpeed = new MenuItem("Rate of growth",new MenuShortcut('S'));
		miSpeed.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Speed(f,"Rate of growth",false).setVisible(true);;
			}
		});
		
		mSpeed.add(miSpeed);
		mColor.add(miColor);
		mHelp.add(miHelp);
		mb.add(mSpeed);
		mb.add(mColor);
		mb.add(mHelp);
		setMenuBar(mb);
		

		t = new Timer(100, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				NextGeneration();
			}
		});

		setVisible(true);
	}

	////// 핵심 코드 (세포 룰)
	private void NextGeneration() {
		c.AliveCell();
		++generation;
		this.setTitle(Integer.toString(generation) + " 세대");
		repaint();
	}

	////// 그리기 작업
	@Override
	public void paint(Graphics g) {
		//더블버퍼링
		if(getWidth() == 0 || getHeight() == 0)
			return;
		img_buffer = createImage(getWidth(), getHeight());
		buffer = img_buffer.getGraphics();
		DrawRec(buffer);
		g.drawImage(img_buffer, 0, 0, this);
	}
	@Override
	public void update(Graphics g) {
		paint(g);
	}

	public void DrawRec(Graphics g) {
		g.setColor(cellColor);
		g.clearRect(0, 0, getWidth(), getHeight());
		for (int i = 0; i < getHeight(); ++i) {
			for (int j = 0; j < getWidth(); ++j) {
				if (c.cell.get(i).get(j)) {
					g.fillRect((j * 10), (i * 10), 10, 10);
				}
			}
		}

		DrawLine(g);
	}

	public void DrawLine(Graphics g) {
		g.setColor(Color.gray);
		for (int cor = 10; cor < this.getWidth(); cor += 10) {
			g.drawLine(cor, 0, cor, this.getHeight());
		}
		for (int row = 10; row < this.getHeight(); row += 10) {
			g.drawLine(0, row, this.getWidth(), row);
		}
	}

}
