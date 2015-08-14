package tankAttack;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JPanel;

public class Bullet extends AnimatedSprite {

	private int tankSource;

	// constructor
	public Bullet(JPanel panel, Graphics2D g2d) {
		super(panel, g2d);
	}

	public int getTankFired() {
		return tankSource;
	}

	public void setTankFired(AnimatedSprite tank, ArrayList arrayList) {
		int t = arrayList.indexOf(tank);
		tankSource = t;
	}

}
