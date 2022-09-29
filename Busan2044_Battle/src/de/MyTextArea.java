package de;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JTextArea;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class MyTextArea extends JTextArea {
	
	public MyTextArea(int arg0, int arg1) {
		super(arg0, arg1);
		// row, column
		this.setBackground(Design.LWhite);
		
		EmptyBorder padding = new EmptyBorder(20, 20, 20, 20);
		LineBorder borderLine = new LineBorder(Design.SBlack, 2);
		CompoundBorder comBorder = new CompoundBorder(borderLine, padding);
		this.setBorder(comBorder);
		
		this.setFont(Design.MSFont);
		
		this.setEditable(false);
	}

	MyTextArea() {
		// 배경색
		this.setBackground(Design.LWhite);
		
		// Border, padding
		EmptyBorder padding = new EmptyBorder(20, 20, 20, 20);
		LineBorder borderLine = new LineBorder(Design.SBlack, 2);
		CompoundBorder comBorder = new CompoundBorder(borderLine, padding);
		this.setBorder(comBorder);
		
		// 폰트
		this.setFont(Design.MSFont);
		
		// 텍스트 편집가능여부
		this.setEditable(false);
	}
}