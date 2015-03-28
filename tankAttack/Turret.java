package tankAttack;

import java.awt.Graphics2D;
import java.util.LinkedList;

import javax.swing.JPanel;

public class Turret extends AnimatedSprite{
	
	ImageEntity turretImage;
	
	final int TURRET_HEALTH = 100;
	final double TURRET_ROTATION = 5.0;
	final double SHELL_RELOAD = 0.4;
	private int assignedTank;

	private long startTime;
	
	boolean turretleft, turretright, fire;
	
	private Game g;
	Graphics2D g2d;
	
	public Turret(Game g, Graphics2D g2d, String path) {
		super(g, g2d);
		this.g = g;
		turretImage = new ImageEntity(g, path);
		
		init();
	}
	
	public void init(){
		setImage(turretImage.getImage());
		setFrameWidth(turretImage.width());
		setFrameHeight(turretImage.height());
		setAlive(true);
		setHealth(TURRET_HEALTH);
		
		startTime = System.currentTimeMillis();
	}
	
	public int getTank() {
		return assignedTank;
	}

	public void setTank(AnimatedSprite tank) {
		int t = g.sprites().indexOf(tank);
		assignedTank = t;
	}
	
	/**
	 * Method to ensure the turret is always at the same position as the tank
	 */
	public void updateTurret(Tank tank){
		double x = tank.center().X() - imageWidth() / 2;
		double y = tank.center().Y() - imageHeight() / 2;
		setPosition(new Point2D(x, y));
	}
	
	public void checkTurretInputs(){
		if (turretleft){
			turretLeft();
		}
		if (turretright){
			turretRight();
		}
		if (fire) {
			// fire shell from the tank if reloaded
			if (System.currentTimeMillis() > startTime + 1000 * SHELL_RELOAD) {
//				fireShell();
				startTime = System.currentTimeMillis();
			}
		}
	}

	public void turretLeft() {
		setFaceAngle(faceAngle() - TURRET_ROTATION);
		if (faceAngle() < 0)
			setFaceAngle(360 - TURRET_ROTATION);
	}
	
	public void turretRight(){
		setFaceAngle(faceAngle() + TURRET_ROTATION);
		if (faceAngle() > 360)
			setFaceAngle(TURRET_ROTATION);
	}
	
//	public void fireShell(){
//		new Shell(g, g2d, (AnimatedSprite) g.sprites().get(getTank()), this);
//	}
	
	/**
	 * Resets all booleans handling movement to prevent commands transferring after death or after screen reset
	 */
	public void resetControls(){
		turretleft = false;
		turretright = false;
		fire = false;
	}
}
