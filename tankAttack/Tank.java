package tankAttack;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JPanel;

import math.geom2d.Point2D;
import math.geom2d.Vector2D;
import tankAttack.collision.RigidRect;

public class Tank extends AnimatedSprite {

	ImageEntity healthBar;
	ImageEntity[] tank;
	ArrayList<Projectile> proj;
	
	final int TANK_HEALTH = 100;
	final double TANK_SPEED = 5;
	final double TANK_ROTATION = 5.0;
	final double SHELL_RELOAD = 0.4;
	final double SHELL_SPEED = 10.0;
	final int KILLPOINTS = 100;
	final int KILLCAP = 15;
	
	final int SPRITE_TANK = 1;
	
	long startTime;
	
	boolean left, right, up, down, fire;
	
	Graphics2D g2d;
	
	// TODO add in any more code associated with tanks
	// TODO make tanks any colour, specified by the constructor
	// TODO respawning invincibility

	/**
	 * Constructor to create a tank, which extends the AnimatedSprite class
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
	
	public Tank(Game g, Graphics2D g2d, String path1, String path2, String path3) {
		super(g, g2d);
		proj = new ArrayList<Projectile>();
		tank = new ImageEntity[2];
		tank[0] = new ImageEntity(g, path1);
		tank[1] = new ImageEntity(g, path2);
		healthBar = new ImageEntity(g, path3);
		
		init();
	}
	
	public void init() {
		setSpriteType(SPRITE_TANK);
		setImage(tank[0].getImage());
		setFrameWidth(tank[0].width());
		setFrameHeight(tank[0].height());
		setPosition(new Point2D(0, 0));
		setAlive(true);
		setHealth(TANK_HEALTH);
		setCollisionShape(new RigidRect(0, 0, tank[0].width(), tank[0].height()));
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
		g2d.setColor(Color.BLACK);
		g2d.drawRect(x - 1, y - 1, 301, 21);
		for (int n = (int) 3 * (TANK_HEALTH - health()) / 2; n < (int) 3 * (TANK_HEALTH + health()) /2; n++) {
			g2d.drawImage(healthBar.getImage(), x +  n, y , p);

		}
	}
	
	public void tankLeft() {
		// left arrow rotates tank left 5 degrees
		setFaceAngle(faceAngle() - TANK_ROTATION);
		if (faceAngle() < 0)
			setFaceAngle(360 - TANK_ROTATION);
	}
	

	public void tankRight() {
		// right arrow rotates tank right 5 degrees
		setFaceAngle(faceAngle() + TANK_ROTATION);
		if (faceAngle() > 360)
			setFaceAngle(TANK_ROTATION);
	}

	public void tankUp() {
		// up arrow gives tank a set velocity
		setMoveAngle(faceAngle() - 90);

		// calculate the X and Y velocity based on angle
		double velx = calcAngleMoveX(moveAngle()) * TANK_SPEED;
		double vely = calcAngleMoveY(moveAngle()) * TANK_SPEED;

		setVelocity(new Point2D(velx, vely));
	}

	public void tankDown() {
		// down arrow gives tank a set velocity
		setMoveAngle(faceAngle() - 90);

		// calculate the X and Y velocity based on angle
		double velx = -calcAngleMoveX(moveAngle()) * TANK_SPEED;
		double vely = -calcAngleMoveY(moveAngle()) * TANK_SPEED;

		setVelocity(new Point2D(velx, vely));
	}

	public void tankStop() {
		// set velocity to zero if up/down keys aren't being pressed
		setVelocity(new Point2D(0, 0));
	}
	
	private double calcAngleMoveX(double angle) {
		return (double) (Math.cos(angle * Math.PI / 180));
	}

	private double calcAngleMoveY(double angle) {
		return (double) (Math.sin(angle * Math.PI / 180));
	}
	
	public void checkInputs(){
		if (left) {
			// left arrow rotates tank left 5 degrees
			tankLeft();

		} else if (right) {
			// right arrow rotates tank right 5 degrees
			tankRight();
		}

		if (up) {
			// up arrow gives tank a set velocity
			tankUp();

			animate();
		}

		else if (down) {
			// down arrow gives tank a set velocity
			tankDown();

			animate();
		}

		else if (!up || !down) {
			// set velocity to zero if up/down keys aren't being pressed
			tankStop();
		}
		if (fire) {
			// fire shell from the tank if reloaded
			if (System.currentTimeMillis() > startTime + 1000 * SHELL_RELOAD) {
				fireShell();
				startTime = System.currentTimeMillis();
			}
		}
	}
	public void fireShell(){
		proj.add(new Shell(g, g2d, this));
	}
	
	/**
	 * Resets all booleans handling movement to prevent commands transferring after death or after screen reset
	 */
	
	public void resetControls(){
		left = false;
		right = false;
		up = false;
		down = false;
	}
	
	public void collision(Vector2D a, Sprite spr){
		switch (spr.spriteType()){
		case 1: translate(a.times(0.5));
				spr.translate(a.times(-0.5));
				break;
		case 100: translate(a);
				break;
		default: System.out.println("Defaulted at collision " + spr.spriteType());
		}
	}
	
	public void killTank() {
		setHealth(TANK_HEALTH);
		setPosition(new Point2D(Game.SCREENWIDTH * Math.random(), Game.SCREENHEIGHT
				* Math.random()));
		setFaceAngle(0);
	}
}
