package tankAttack;

import java.applet.Applet;
import java.awt.Graphics2D;
import java.util.*;

public class bullet extends AnimatedSprite {
	
	private int tankSource;
	
	// constructor
	public bullet(Applet applet, Graphics2D g2d) {
		super(applet, g2d);
	}
	
	public int getTankFired() {return tankSource;}
	public void setTankFired(AnimatedSprite tank, LinkedList sprites) {
		int t = sprites.indexOf(tank);
		tankSource = t;
	}

}
