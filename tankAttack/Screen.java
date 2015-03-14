package tankAttack;

import java.awt.Graphics2D;

public abstract class Screen {
	
	Graphics2D g2d;
	int SCREENWIDTH, SCREENHEIGHT;
	Game g;
	final int MAIN_MENU = 0;
	final int GAME_OVER = 2;
	final int PLAYER_VS_PLAYER = 3;
	
	/**
	 * This class is abstract, and should be extended if a new screen for the game
	 * is made. 
	 * @param g
	 * 		Current game object
	 * @param g2d
	 * 		Current graphics object
	 */
	
	public Screen(Game g, Graphics2D g2d){
		this.g =g;
		this.g2d = g2d;

		SCREENWIDTH = Game.SCREENWIDTH;
		SCREENHEIGHT = Game.SCREENHEIGHT;
		initiate();
	}
	
	public void printSimpleString(String s, int width, int XPos, int YPos) {
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
		g.sprites().add(a);
	}

	public void resetScreen() {
		
	}
}
