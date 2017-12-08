package Project;

import java.awt.*;
import java.awt.event.*;

public class CardLayoutEvent extends Frame
{
	Button first, prev, next, last;
	Panel buttons;
	Panel slide;
	Panel card1, card2, card3, card4, card5;
	CardLayout card;
	
	CardLayoutEvent(String title)
	{
		super(title);
		
		slide = new Panel();
		card = new CardLayout();
		slide.setLayout(card);
		
		buttons = new Panel();
		buttons.setLayout(new FlowLayout());
		first = new Button("<<");
		prev = new Button("<");
		next = new Button(">");
		last = new Button(">>");
		buttons.add(first);
		buttons.add(prev);
		buttons.add(next);
		buttons.add(last);
		
		first.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				card.first(slide);
			}
		});
		prev.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				card.previous(slide);
			}
		});
		next.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				card.next(slide);
			}
		});
		last.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				card.last(slide);
			}
		});
		
		card1 = new Panel();
		card1.setBackground(Color.gray);
		card1.add(new Label("첫 번째 페이지입니다."));
		card2 = new Panel();
		card2.setBackground(Color.orange);
		card2.add(new Label("두 번째 페이지입니다."));
		card3 = new Panel();
		card3.setBackground(Color.blue);
		card3.add(new Label("세 번째 페이지입니다."));
		card4 = new Panel();
		card4.setBackground(Color.cyan);
		card4.add(new Label("네 번째 페이지입니다."));
		card5 = new Panel();
		card5.setBackground(Color.pink);
		card5.add(new Label("다섯 번째 페이지입니다."));
		
		slide.add(card1, "1");
		slide.add(card2, "2");
		slide.add(card3, "3");
		slide.add(card4, "4");
		slide.add(card5, "5");
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});

		add("South", buttons);
		add("Center", slide);
		setSize(300, 300);
		// 위치 지정
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize();
		setLocation(screenSize.width / 2 - getWidth() / 2, screenSize.height / 2 - getHeight() / 2);
		setVisible(true);
		card.show(slide, "1");
	
	}
}
