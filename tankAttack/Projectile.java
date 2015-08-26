package tankAttack;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JPanel;

public class Projectile extends AnimatedSprite {

	private int tankSource;

	// constructor
	public Projectile(Game panel, Graphics2D g2d) {
		super(panel, g2d);
	}

	public int getTankFired() {
		return tankSource;
	}

	public void setTankFired(AnimatedSprite tank, ArrayList<Sprite> arrayList) {
		int t = arrayList.indexOf(tank);
		tankSource = t;
	}

}
