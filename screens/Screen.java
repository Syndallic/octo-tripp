package screens;

import java.awt.Graphics2D;

import gameEngine.AnimatedSprite;
import gameEngine.Game;

public abstract class Screen {
	
	public Graphics2D g2d;
	int SCREENWIDTH, SCREENHEIGHT;
	public Game g;
	final int MAIN_MENU = 0;
	final int PLAYER_VS_AI = 1;
	final int PLAYER_VS_PLAYER = 2;
	final int CONTROLS_MENU = 3;
	final int GAME_OVER = 4;
	
	/**
	 * This class is abstract, and should be extended if a new screen for the game
	 * is made. 
	 * 
	 * @param g
	 * 		Current game object
	 * @param g2d
	 * 		Current graphics object
	 */
	
	public Screen(Game g, Graphics2D g2d){
		this.g = g;
		this.g2d = g2d;

		SCREENWIDTH = Game.getSCREENWIDTH();
		SCREENHEIGHT = Game.getSCREENHEIGHT();
		initiate();
	}
	
	/**
	 * Helper class to draw an x-axis-centred string on the screen
	 * 
	 * 
	 * @param s			The string to be displayed
	 * @param width		The available width to centre in
	 * @param XPos		X-axis offset (0 for centre)
	 * @param YPos		Y-axis offset from top of screen
	 */
	public void printSimpleString(String s, int width, int XPos, int YPos) {
		int stringLen = (int) g2d.getFontMetrics().getStringBounds(s, g2d)
				.getWidth();
		int start = width / 2 - stringLen / 2;
		g2d.drawString(s, start + XPos, YPos);
	}
	
	/**
	 * Overload helper function from coderanch to center integers as strings
	 * 
	 * @param s
	 *            The actual string to be drawn
	 * @param width
	 *            Width to center within (i.e. the Screen width)
	 * @param XPos
	 *            x coordinate offset
	 * @param YPos
	 *            y coordinate offset
	 */
	public void printSimpleString(int _s, int width, int XPos, int YPos) {
		Graphics2D g2d = graphics();
		String s = String.valueOf(_s);
		int stringLen = (int) g2d.getFontMetrics().getStringBounds(s, g2d)
				.getWidth();
		int start = width / 2 - stringLen / 2;
		g2d.drawString(s, start + XPos, YPos);
	}
	
	/**
	 * Method that contains anything that needs initialising to start, like setting values and finding images
	 */
	public void initiate(){
		
	}
	
	/**
	 * Sets the current screen parameter in the Game object to this instance of screen
	 */
	public void makeCurrent(){
		g.screen = this;
	}
	
	public Screen findCurrent(){
		return g.screen;
	}
	
	/**
	 * Method that must be inside the gameloop and keeps the screen updated
	 */
	public void update(){
		
	}
	
	public void mouse(){
		
	}
	
	public Graphics2D graphics(){
		return g2d;
	}
	
	/**
	 * Method that handles logic when keys are pressed. Should be called by the keyListener
	 */
	public void keyPressed(int keyCode){
		
	}
	
	
	/**
	 * Method that handles logic when keys are released. Should be called by the keyListener
	 */
	public void keyReleased(int keyCode){
		
	}
	
	public void add(AnimatedSprite a){
		g.addSprite(a);
	}

	public void resetScreen() {
		
	}
}
