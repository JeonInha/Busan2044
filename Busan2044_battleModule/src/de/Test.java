package de;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class Test extends JFrame {
	
	JButton a = new ColorButton("연습");
	JButton b = new ColorButton("연습", Design.LSkyblue, Design.SBlack);
	JButton c = new ColorButton("연습", Design.LSkyblue, Design.SBlack, Design.SBlue);
	JTextArea ab = new MyTextArea();
	
	JPanel pnl = new TSPanel();
	
	MouseAdapter aaa = new MouseAdapter() {
		@Override
		public void mousePressed(MouseEvent e) {
			JButton btn = (JButton) e.getSource();
			
			if (btn.isEnabled()) {
				btn.setEnabled(false);
			} else {
				btn.setEnabled(true);
			}
		}	
	};
	
	Test() {
	pnl.add(a);
	pnl.add(b);
	pnl.add(c);
	pnl.add(ab);
	ab.setText("테스트\n테스트\n킹갓제너럴예슬의짱짱지문");
	add(pnl);
	
	a.addMouseListener(aaa);
	b.addMouseListener(aaa);
	c.addMouseListener(aaa);
	
	setSize(500, 500);
	}
	
	public static void main(String[] args) {
		new Test().setVisible(true);
	}

}
