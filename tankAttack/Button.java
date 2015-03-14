package tankAttack;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class Button {

	static final int ACTIVE = 0;
	static final int SELECTED = 1;
	static final int CLICKED = 2;
	static final int DEACTIVATED = 3;

	Font font;
	Graphics2D g2d;
	String string = "";
	int state;
	Color color;
	int x, y;
	int width, height;
	int event = -1;

	/**
	 * Creates a button which has four available states to help with logic:
	 * ACTIVE, SELECTED, CLICKED, DEACTIVATED
	 * 
	 * @param screen
	 *            The screen the button is added to
	 */

	public Button(Screen screen) {
		this.g2d = screen.g2d;
		normal();
	}

	public void setString(String string) {
		this.string = string;
	}

	public void update() {
		g2d.setFont(font);
		g2d.setColor(color);
		checkDimensions();
		g2d.drawString(string, x - (width / 2), y - (height / 2));
	}

	public void selected() {

		if (state == DEACTIVATED) {
			color = Color.DARK_GRAY;
		} else {
			font = new Font("Verdana", Font.BOLD, 34);
			state = SELECTED;
			color = Color.GREEN;
		}
	}

	public void setCentre(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void clicked() {
		if (state != DEACTIVATED) {
			color = Color.GREEN;
			state = CLICKED;
			
		}
	}

	public void normal() {
		font = new Font("Verdana", Font.BOLD, 36);
		if (state == DEACTIVATED) {
			color = Color.GRAY;
		} else {
			color = Color.BLACK;
			state = ACTIVE;
			
		}
	}

	/**
	 * Deactivates the button, changing the text to GRAY
	 */

	public void deactivated() {
		state = DEACTIVATED;
		color = Color.GRAY;
	}

	public void setEvent(int event) {
		this.event = event;
	}

	public void checkDimensions() {
		width = (int) g2d.getFontMetrics().getStringBounds(string, g2d)
				.getWidth();
		height = (int) g2d.getFontMetrics().getStringBounds(string, g2d)
				.getHeight();
	}

	public int getEvent() {
		return event;

	}
}
