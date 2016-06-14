package gameEngine;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import screens.Screen;

public class Button {

	public static final int ACTIVE = 0;
	public static final int SELECTED = 1;
	public static final int CLICKED = 2;
	public static final int DEACTIVATED = 3;

	Font font;
	Graphics2D g2d;
	String string = "";
	public int state;
	Color color;
	int x, y;
	int width, height;
	int event = -1;

	/**
	 * Creates a button which has four available states to help with logic:
	 * ACTIVE, SELECTED, CLICKED, DEACTIVATED. Must first be initialised inside the desired screen.
	 * Then set the text to be displayed using setString() and the centre coordinates with
	 * setCentre() and finally include the event number which returns when the button is
	 * activated. If none, set button to deactivated() which makes it unclickable.
	 * 
	 * @param screen
	 *            The screen the button is added to
	 */

	public Button(Screen screen) {
		this.g2d = screen.g2d;
		normal();
	}

	
	/**
	 * Set the string to be displayed by the button
	 * 
	 * @param string
	 */
	
	public void setString(String string) {
		this.string = string;
	}

	public void update() {
		g2d.setFont(font);
		g2d.setColor(color);
		checkDimensions();
		g2d.drawString(string, x - (width / 2), y - (height / 2));
	}
	
	/**
	 * Method called when the button is selected by the user
	 */
	
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
	
	/**
	 * Method called when button is clicked, although not very useful because 
	 * the screen change is so fast, it is not possible to see the change in font/color 
	 */
	
	public void clicked() {
		if (state != DEACTIVATED) {
			color = Color.GREEN;
			state = CLICKED;
			
		}
	}
	
	/**
	 * Method called when button in an otherwise uninteracted state
	 */
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
	
	/**
	 * Sets an integer number associated with the button which can differentiate between
	 * certain desirable outcomes
	 * @param event
	 */
	
	public void setEvent(int event) {
		this.event = event;
	}

	/**
	 * Find the dimensions of the text in order to keep it centred on the screen and
	 * keep the bounds updated if button is clickable with a mouse
	 */
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
