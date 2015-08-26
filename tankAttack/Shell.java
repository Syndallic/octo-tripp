package tankAttack;

import java.awt.Graphics2D;

import math.geom2d.Point2D;
import math.geom2d.Vector2D;
import tankAttack.collision.RigidRect;

public class Shell extends Projectile {

	ImageEntity shellImage;
	Tank tank;
	final int SHELL_SPEED = 10;
	final int SPRITE_SHELL = 100;
	final int SHELL_DAMAGE = -20;

	public Shell(Game g, Graphics2D g2d, Tank tank) {
		super(g, g2d);
		this.tank = tank;
		shellImage = new ImageEntity(g, "shell.png");
		fire();
	}
	
	public Tank getSource(){
		return tank;
	}

	public void fire() {
		// create the new shell sprite
		Projectile shell = new Projectile(g, g.graphics());
		shell.setImage(shellImage.getImage());
		shell.setFrameWidth(shellImage.width());
		shell.setFrameHeight(shellImage.height());
		shell.setSpriteType(SPRITE_SHELL);
		shell.setAlive(true);
		shell.setLifespan(200);
		shell.setFaceAngle(tank.faceAngle());
		shell.setMoveAngle(tank.faceAngle() - 90);

		// set the shell's starting position
		double x = tank.center().x() - shell.imageWidth() / 2;
		double y = tank.center().y() - shell.imageHeight() / 2;
		shell.setPosition(new Point2D(x, y));
		setCollisionShape(new RigidRect((int)shell.center().x(), (int)shell.center().y(), shell.imageWidth(), shell.imageHeight()));

		// set the shell's velocity
		double angle = shell.moveAngle();
		double svx = calcAngleMoveX(angle) * SHELL_SPEED;
		double svy = calcAngleMoveY(angle) * SHELL_SPEED;
		shell.setVelocity(new Point2D(svx, svy));
	}

	protected double calcAngleMoveX(double angle) {
		return (double) (Math.cos(angle * Math.PI / 180));
	}

	protected double calcAngleMoveY(double angle) {
		return (double) (Math.sin(angle * Math.PI / 180));
	}
	
	public void collision(Vector2D a, Sprite spr){
		switch(spr.spriteType()){
		case 1:
			setAlive(false);
			spr.changeHealth(SHELL_DAMAGE);
			tank.changeScore(tank.KILLPOINTS);
			break;
		}
	}
}
