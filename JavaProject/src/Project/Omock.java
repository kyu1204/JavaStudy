package Project;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import sun.java2d.loops.DrawRect;

public class Omock extends Frame {

	List<List<Stone>> table = new ArrayList<List<Stone>>();
	private final Frame my = this;
	Stone flag = Stone.BLACK;

	class MouseEventHandle extends MouseAdapter {
		@Override
		public void mouseMoved(MouseEvent e) {
			int x = e.getX();
			int y = e.getY();
			my.setTitle(x+","+y);
		}
		@Override
		public void mousePressed(MouseEvent e) {
			int x = (e.getX() - 15) / 30;
			int y = (e.getY() - 45) / 30;
			if (x < (getWidth() - 10) / 30 && y < (getHeight() - 60) / 30) {
				if (flag == Stone.BLACK && table.get(y).get(x) == Stone.NONE) {
					table.get(y).set(x, Stone.BLACK);

					repaint();
					if (Rull(x, y)) {
						new OmockVictory(my, flag);
					} 
					else
						flag = Stone.WHITE;
				} 
				else if (flag == Stone.WHITE && table.get(y).get(x) == Stone.NONE) {
					table.get(y).set(x, Stone.WHITE);

					repaint();
					if (Rull(x, y)) {
						new OmockVictory(my, flag);
					}
					flag = Stone.BLACK;
				}
			}
		}
	}

	Omock() {
		super("오목");
		setSize(690, 720);
		setResizable(false);
		setIconImage(new ImageIcon("5mock.jpg").getImage());
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize();
		setLocation(screenSize.width/2-getWidth()/2, screenSize.height/2-getHeight()/2);
		setBackground(Color.ORANGE);

		for (int i = 0; i < (getHeight() - 30) / 30; ++i) {
			table.add(new ArrayList<Stone>());
			for (int j = 0; j < (getWidth() - 10) / 30; ++j) // 30 크기의 셀+위에 메뉴바 크기만큼 빼기
			{
				table.get(i).add(Stone.NONE);
			}
		}
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});
		addMouseListener(new MouseEventHandle());
		addMouseMotionListener(new MouseEventHandle());

		setVisible(true);
	}

	public boolean Rull(int x,int y)
	{
		if (table.get(y).get(x) != Stone.NONE)
		{
			if(Search(x, y, 0,Pointer.LEFTTOP)==5)
				return true;
			else if(Search(x, y, 0,Pointer.TOP)==5)
				return true;
			else if(Search(x, y, 0,Pointer.RIGHTTOP)==5)
				return true;
			else if(Search(x, y, 0,Pointer.RIGHT)==5)
				return true;
			else if(Search(x, y, 0,Pointer.RIGHTBOTTOM)==5)
				return true;
			else if(Search(x, y, 0,Pointer.BOTTOM)==5)
				return true;
			else if(Search(x, y, 0,Pointer.LEFTBOTTOM)==5)
				return true;
			else if(Search(x, y, 0,Pointer.LEFT)==5)
				return true;
			else
				return false;
		}
		return false;
	}

	public int Search(int x, int y, int c, Pointer p) {
		int count = c;
		if (x >= 0 && y >= 0 && x < (getWidth() - 10) / 30 && y<(getHeight() - 30) / 30) 
		{
			switch (p) {
			case LEFTTOP:
				if (table.get(y).get(x) == flag) {
					count++;
					count = Search(--x, --y, count, Pointer.LEFTTOP);
					return count;
				} 
				else {
					return count;
				}
			case TOP:
				if (table.get(y).get(x) == flag) {
					count++;
					count = Search(x, --y, count, Pointer.TOP);
					return count;
				} 
				else {
					return count;
				}
			case RIGHTTOP:
				if (table.get(y).get(x) == flag) {
					count++;
					count = Search(++x, --y, count, Pointer.RIGHTTOP);
					return count;
				} 
				else {
					return count;
				}
			case RIGHT:
				if (table.get(y).get(x) == flag) {
					count++;
					count = Search(++x, y, count, Pointer.RIGHT);
					return count;
				} 
				else {
					return count;
				}
			case RIGHTBOTTOM:
				if (table.get(y).get(x) == flag) {
					count++;
					count = Search(++x, ++y, count, Pointer.RIGHTBOTTOM);
					return count;
				} 
				else {
					return count;
				}
			case BOTTOM:
				if (table.get(y).get(x) == flag) {
					count++;
					count = Search(x, ++y, count, Pointer.BOTTOM);
					return count;
				} 
				else {
					return count;
				}
			case LEFTBOTTOM:
				if (table.get(y).get(x) == flag) {
					count++;
					count = Search(--x, ++y, count, Pointer.LEFTBOTTOM);
					return count;
				} 
				else {
					return count;
				}
			case LEFT:
				if (table.get(y).get(x) == flag) {
					count++;
					count = Search(--x, y, count, Pointer.LEFT);
					return count;
				} 
				else {
					return count;
				}
			default:
				return 0;
			}
		}
		else
			return count;
	}

	///////// 그리기///////////////
	@Override
	public void paint(Graphics g) {
		DrawLine(g);
	}

	private void DrawStone(Graphics g) {
		for (int i = 0; i < (getHeight() - 30) / 30; ++i) {
			for (int j = 0; j < (getWidth() - 10) / 30; ++j) {
				if (table.get(i).get(j) == Stone.BLACK) {
					g.setColor(Color.black);
					g.fillOval((j * 30) + 15, (i * 30) + 45, 30, 30);
				} else if (table.get(i).get(j) == Stone.WHITE) {
					g.setColor(Color.white);
					g.fillOval((j * 30) + 15, (i * 30) + 45, 30, 30);
				}
			}
		}
	}

	private void DrawLine(Graphics g) {
		g.setColor(Color.black);
		g.clearRect(0, 0, getWidth(), getHeight());
		for (int cor = 0; cor < this.getWidth(); cor += 30) {
			g.drawLine(cor, 0, cor, this.getHeight());
		}
		for (int row = 0; row < this.getHeight(); row += 30) {
			g.drawLine(0, row, this.getWidth(), row);
		}
		DrawStone(g);
	}
}
