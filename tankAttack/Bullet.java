package tankAttack;

import java.awt.Graphics2D;
import java.util.LinkedList;

import javax.swing.JPanel;

import gameEngine.AnimatedSprite;

public class Bullet extends AnimatedSprite {

	private int tankSource;
	public int speed;

	// constructor
	public Bullet(JPanel panel, Graphics2D g2d, int speed) {
		super(panel, g2d);
		this.speed = speed;
	}
	
	public int getSpeed(){
		return speed;
	}

	public int getTankFired() {
		return tankSource;
	}

	public void setTankFired(AnimatedSprite tank, LinkedList sprites) {
		int t = sprites.indexOf(tank);
		tankSource = t;
	}

}
