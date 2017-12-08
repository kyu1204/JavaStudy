package Project;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

public class LifeGame extends Frame {
	private Cell c;
	private Timer t;
	private static int generation = 0;

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

		c = new Cell(getWidth(), getHeight());

		addMouseListener(new MouseEventHandle());
		addComponentListener(new ResizeEventHandle());

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});

		t = new Timer(100, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				NextGeneration();
			}
		});

		setVisible(true);
	}

	private void NextGeneration() {
		c.AliveCell();
		++generation;
		this.setTitle(Integer.toString(generation) + " 세대");
		repaint();
	}

	@Override
	public void paint(Graphics g) {
		DrawRec(g);
	}

	public void DrawRec(Graphics g) {
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
