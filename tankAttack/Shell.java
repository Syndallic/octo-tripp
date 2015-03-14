package tankAttack;

import java.awt.Graphics2D;

public class Shell extends Bullet {

	ImageEntity shellImage;
	final int SHELL_SPEED = 10;
	final int SPRITE_SHELL = 100;

	
	public Shell(Game g, Graphics2D g2d, Tank tank) {
		super(g, g2d);
		shellImage = new ImageEntity(g, "shell.png");
		fire(g, tank);
	}
	
	public void fire(Game g, Tank tank){
		// create the new shell sprite
		Bullet shell = new Bullet(g, g.graphics());
		shell.setImage(shellImage.getImage());
		shell.setFrameWidth(shellImage.width());
		shell.setFrameHeight(shellImage.height());
		shell.setSpriteType(SPRITE_SHELL);
		shell.setAlive(true);
		shell.setLifespan(200);
		shell.setFaceAngle(tank.faceAngle());
		shell.setMoveAngle(tank.faceAngle() - 90);

		// set the shell's starting position
		double x = tank.center().X() - shell.imageWidth() / 2;
		double y = tank.center().Y() - shell.imageHeight() / 2;
		shell.setPosition(new Point2D(x, y));

		// set the shell's velocity
		double angle = shell.moveAngle();
		double svx = calcAngleMoveX(angle) * SHELL_SPEED;
		double svy = calcAngleMoveY(angle) * SHELL_SPEED;
		shell.setVelocity(new Point2D(svx, svy));

		// record which tank fired the shell
		shell.setTankFired(tank, g.sprites());

		// add shell to the sprite list
		g.sprites().add(shell);
	}
	
	
	protected double calcAngleMoveX(double angle) {
		return (double) (Math.cos(angle * Math.PI / 180));
	}

	protected double calcAngleMoveY(double angle) {
		return (double) (Math.sin(angle * Math.PI / 180));
	}

}
