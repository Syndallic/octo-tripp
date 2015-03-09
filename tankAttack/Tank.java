package tankAttack;

import java.awt.Graphics2D;

import javax.swing.JPanel;

public class Tank extends AnimatedSprite {

	ImageEntity healthBar;
	ImageEntity[] tank;

	final int TANK_HEALTH = 100;
	final double TANK_SPEED = 5;
	final double TANK_ROTATION = 5.0;

	final int STATE_NORMAL = 0;
	final int STATE_COLLIDED = 1;
	final int STATE_EXPLODING = 2;

	final int SPRITE_TANK = 1;
	
	// TODO add in any more code associated with tanks
	// TODO make tanks any color, specified by the constructor
	// TODO respawning invincibility

	/**
	 * Constructor to create a tank, which extends the AnimateSprite class
	 * 
	 * @param p
	 *            JPanel associated with observing the image
	 * @param g2d
	 *            Graphics2D for place to draw sprites
	 * @param path1
	 *            Path to first of two tank sprites
	 * @param path2
	 *            Path to second of two tank sprites
	 * @param path3
	 *			  Path to pixel wide health bar
	 */
	
	public Tank(JPanel p, Graphics2D g2d, String path1, String path2, String path3) {
		super(p, g2d);
		tank = new ImageEntity[2];
		tank[0] = new ImageEntity(p, path1);
		tank[1] = new ImageEntity(p, path2);
		healthBar = new ImageEntity(p, path3);
		init();
	}
	
	public void init() {
		setSpriteType(SPRITE_TANK);
		setImage(tank[0].getImage());
		setFrameWidth(tank[0].width());
		setFrameHeight(tank[0].height());
		setPosition(new Point2D(0, 0));
		// Tank set to alive state
		setAlive(true);
		// Tank set to normal
		setState(STATE_NORMAL);
		// Health set
		setHealth(TANK_HEALTH);
	}
	
	/**
	 * 	Method that alternates between the two given images parsed through the tank constructor
	 */
	
	public void animate(){
		if (image() == tank[0].getImage()) {
			setImage(tank[1].getImage());
		} else {
			setImage(tank[0].getImage());
		}
	}
	
	public void drawHealthBar(Graphics2D g2d, JPanel p, int x, int y){
		for (int n = (int) 3 * (TANK_HEALTH - health()) / 2; n < (int) 3 * (TANK_HEALTH + health()) /2; n++) {
			g2d.drawImage(healthBar.getImage(), x +  n, y , p);

		}
	}
	
}
