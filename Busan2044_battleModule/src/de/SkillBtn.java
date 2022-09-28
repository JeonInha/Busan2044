package de;

import java.awt.*;
import javax.swing.JButton;

public class SkillBtn extends JButton {
	Color fontColor = Design.SBlack;
	Color bgColor = Design.LWhite;
	Color borderColor = Design.LWhite;

	// 기본생성자
	public SkillBtn(String text) {
		super(text);
		this.setFont(Design.MSFont);
	}

	protected void decorate() {
		setBorderPainted(false);
		setOpaque(false);
	}

	@Override
	protected void paintComponent(Graphics g) {
		// 버튼크기
		int width = getWidth();
		int height = getHeight();

		Graphics2D graphics = (Graphics2D) g;
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// 버튼색 채우기
		graphics.setColor(bgColor);
		graphics.fillRect(0, 0, width, height);

		FontMetrics fontMetrics = graphics.getFontMetrics();
		Rectangle stringBounds = fontMetrics.getStringBounds(this.getText(), graphics).getBounds();

		// 가로세로
		int textX = (width - stringBounds.width) / 2;
		int textY = (height - stringBounds.height) / 2 + fontMetrics.getAscent();

		// 테두리 그리기
		graphics.setColor(borderColor);
		graphics.drawRect(0, 0, width - 1, height - 1);

		if (getModel().isRollover()) {
			graphics.setColor(Design.SRed);
		} else {
			graphics.setColor(fontColor);
		}

		graphics.setFont(getFont());
		graphics.drawString(getText(), textX, textY);
		graphics.dispose();

		super.paintComponent(g);
	}
}