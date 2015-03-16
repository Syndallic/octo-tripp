package tankAttack;

import java.awt.Graphics2D;

import javax.swing.JPanel;

public class Turret extends AnimatedSprite{
	
	ImageEntity turretImage;
	
	final int TURRET_HEALTH = 100;
	
	private Game g;
	
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
		setPosition(new Point2D(400, 400));
		setAlive(true);
		setHealth(TURRET_HEALTH);
	}
	
	/**
	 * Method to ensure the turret is always at the same position as the tank
	 */
	public void updateTurret(Tank tank){
		double x = tank.center().X() - imageWidth() / 2;
		double y = tank.center().Y() - imageHeight() / 2;
		setPosition(new Point2D(x, y));
	}

}
