package tankAttack;

import java.awt.Graphics2D;

import javax.swing.JButton;

public abstract class Screen {
	
	Graphics2D g2d;
	int SCREENWIDTH, SCREENHEIGHT;
	public int state;
	Game g;
	final int GAME_MENU = 0;
	final int GAME_RUNNING = 1;
	final int GAME_OVER = 2;
	
	public Screen(Game g, Graphics2D g2d){
		this.g =g;
		this.g2d = g2d;
		SCREENWIDTH = g.getWidth();
		SCREENHEIGHT = g.getHeight();
	}
	
	public void printSimpleString(String s, int width, int XPos, int YPos) {
		int stringLen = (int) g2d.getFontMetrics().getStringBounds(s, g2d)
				.getWidth();
		int start = width / 2 - stringLen / 2;
		g2d.drawString(s, start + XPos, YPos);
	}
	
	public void add(JButton c){
		g.add(c);
	}
	
	public void initiate(){
		
	}
	
	public void update(){
		
	}
	
	public int getState(){
		return state;
	}
	
	public void setState(int s){
		state = s;
	}
	
	public Graphics2D graphics(){
		return g2d;
	}
}
