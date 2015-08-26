package tankAttack;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import math.geom2d.Vector2D;

public abstract class Screen {
	
	Graphics2D g2d;
	int SCREENWIDTH, SCREENHEIGHT;
	Game g;
	final int MAIN_MENU = 0;
	final int PLAYER_VS_AI = 1;
	final int PLAYER_VS_PLAYER = 2;
	final int CONTROLS_MENU = 3;
	final int GAME_OVER = 4;
	
	/**
	 * This class is abstract, and should be extended if a new screen for the game
	 * is made. 
	 * @param g
	 * 		Current game object
	 * @param g2d
	 * 		Current graphics object
	 */
	
	public Screen(Game g){
		this.g =g;
		this.g2d = g.graphics();

		SCREENWIDTH = Game.SCREENWIDTH;
		SCREENHEIGHT = Game.SCREENHEIGHT;
		initiate();
	}
	
	/**
	 * Method that contains anything that needs initialising to start, like setting values and finding images
	 */
	
	abstract void initiate();
	abstract void update();
	abstract void resetScreen();
	
	public void printSimpleString(String s, int width, int XPos, int YPos) {
		int stringLen = (int) g2d.getFontMetrics().getStringBounds(s, g2d)
				.getWidth();
		int start = width / 2 - stringLen / 2;
		g2d.drawString(s, start + XPos, YPos);
	}
	
	
	public void makeCurrent(){
		g.screen = this;
		resetScreen();
	}
	
	public Screen findCurrent(){
		return g.screen;
	}
	
	/**
	 * Method that must be inside the gameloop and keeps the screen updated
	 */
	
	
	public void mouse(){
		
	}
	
	public Graphics2D graphics(){
		return g2d;
	}
	
	/**
	 * Method that handles logic when keys are pressed. Should be called by the keyListener
	 */
	
	public void keyPressed(int keyCode){
		if(keyCode == KeyEvent.VK_ESCAPE){
			g.stop();
		}
	}
	
	
	/**
	 * Method that handles logic when keys are released. Should be called by the keyListener
	 */
	public void keyReleased(int keyCode){
		
	}
	
	public void drawLine(math.geom2d.Point2D p, Vector2D v){
		g2d.drawLine((int)p.x(), (int)(p.y()), (int)(p.x() + v.x()), (int)(p.y()+ v.y()));
	}
	
	public void drawLine(math.geom2d.Point2D p, Vector2D v, Color color){
		g2d.setColor(color);
		g2d.drawLine((int)p.x(), (int)(p.y()), (int)(p.x() + v.x()), (int)(p.y()+ v.y()));
	}
	
}
