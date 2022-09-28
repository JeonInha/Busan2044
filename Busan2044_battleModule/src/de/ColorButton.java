package de;
import java.awt.*;
import javax.swing.JButton;

public class ColorButton extends JButton {
	Color fontColor = Design.SBlack;
	Color bgColor = Design.LBeige;
	Color borderColor = Design.LBeige;
	
	// 기본생성자
	public ColorButton(String text) {
		super(text);
		this.setFont(Design.MSFont);
	}
	
	// 테두리없는 생성자
	public ColorButton(String text, Color bgColor, Color fontColor) {
		super(text);
		this.bgColor = bgColor;
		this.fontColor = fontColor;
		this.borderColor = bgColor;
		this.setFont(Design.MSFont);
	}

	// 테두리있는 생성자
	public ColorButton(String text, Color bgColor, Color fontColor, Color borderColor) {
		super(text);
		this.bgColor = bgColor;
		this.fontColor = fontColor;
		this.borderColor = borderColor;
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
		
		// 활성화시
		if (getModel().isEnabled()) {
			graphics.setColor(bgColor);
			
		// 비활성화시
		} else if (!(getModel().isEnabled())) {
			graphics.setColor(Color.LIGHT_GRAY);
		}
		// 호버
		if (getModel().isRollover()) {
			graphics.setColor(bgColor.darker());
		}
		// 버튼색 채우기
		graphics.fillRect(0, 0, width, height);


		FontMetrics fontMetrics = graphics.getFontMetrics();
		Rectangle stringBounds = fontMetrics.getStringBounds(this.getText(), graphics).getBounds();

		// 가로세로
		int textX = (width - stringBounds.width) / 2;
		int textY = (height - stringBounds.height) / 2 + fontMetrics.getAscent();

		// 테두리 그리기
		if (getModel().isEnabled())
			graphics.setColor(borderColor);
		else
			graphics.setColor(Color.gray);
		graphics.drawRect(0, 0, width - 1, height - 1);

		// 글자 그리기
		if (getModel().isEnabled())
			graphics.setColor(fontColor);
		else
			graphics.setColor(Color.gray);
		
		graphics.setFont(getFont());
		graphics.drawString(getText(), textX, textY);
		graphics.dispose();

		super.paintComponent(g);
	}
}
