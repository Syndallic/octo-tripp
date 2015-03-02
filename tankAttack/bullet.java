package tankAttack;

import java.awt.Graphics2D;
import java.util.LinkedList;

import javax.swing.JPanel;

public class bullet extends AnimatedSprite {
	
	private int tankSource;
	
	// constructor
	public bullet(JPanel panel, Graphics2D g2d) {
		super(panel, g2d);
	}
	
	public int getTankFired() {return tankSource;}
	public void setTankFired(AnimatedSprite tank, LinkedList sprites) {
		int t = sprites.indexOf(tank);
		tankSource = t;
	}

}
