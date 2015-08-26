package tankAttack;
import java.applet.Applet;
import java.awt.Graphics2D;

import math.geom2d.Vector2D;
import math.geom2d.Point2D;

public class AI extends Object{
	
	// constructor
	public AI() {
		super();
	}
	
	/**
	 * Method to find the direction to fire in to hit enemy sprite
	 * 
	 * @param AI			The bot
	 * @param target		The sprite being aimed at
	 * @param _shellspeed	Speed of the shell being fired
	 * @return				Direction vector to fire in to hit enemy sprite
	 */
	public Vector2D findAimDirection(AnimatedSprite AI, AnimatedSprite target, double _shellspeed){
		double SHELL_SPEED = _shellspeed;
		
		
		// method credit to Kain Shin
		
		
		// construct math.geom2d Point2D objects of each sprite position
		Point2D targetpos = new Point2D(target.center().x(), target.center().y());
		Point2D AIpos = new Point2D(AI.center().x(), AI.center().y());
		
		Vector2D distdiff = new Vector2D(AIpos, targetpos);
//		distdiff = distdiff.normalize();

		Vector2D zero = new Vector2D();
		
		if (target.velocity().x() != 0 && distdiff != zero){
		Vector2D targetvel = new Vector2D(target.velocity().x(), target.velocity().y());
//		targetvel = targetvel.normalize();
		
		// dot product part
		double costheta = Vector2D.dot(distdiff.normalize(), targetvel.normalize());
		
		// set up terms for quadratic equation used to find t
		double a = Math.pow(SHELL_SPEED, 2) - Math.pow(targetvel.norm(), 2);
		double b = 2*distdiff.norm()*costheta;
		double c = -Math.pow(distdiff.norm(), 2);
		
		// use quadratic formula to solve for t
		double t1 = (-b - Math.sqrt( Math.pow(b, 2) - 4*a*c )) / (2*a);
		double t2 = (-b + Math.sqrt( Math.pow(b, 2) - 4*a*c )) / (2*a);
		
		// find which value of t to use
		double t;
		if (t1 == t2){ t = t1;}
		else if (t1 < 0){ t = t2;}
		else if (t2 < 0){ t = t1;}
		else if (t1 < t2){ t = t1;}
		else if (t2 < t1){ t = t2;}
		else{
			System.out.println("Logic failure somewhere in findAimDirection");
			t = 1;
		}
		
		// find end direction vector
		t = 1/t;
		Vector2D aimDirection = distdiff.times(t);
		aimDirection = aimDirection.plus(targetvel);
		
		return aimDirection;
	}
		else{
			return distdiff;
		}
	}
	
	/**
	 * Method to move AI into a position where it can make a good shot, and judge when to fire
	 * 
	 * @param AI
	 * 						bot tank
	 * @param direction
	 * 						direction vector found by findAimDirection
	 * @param tolerance 
	 * 						equal to TANK_ROTATION, the increment of rotation per tick
	 * @return
	 * 						0 - rotate left
	 * 						1 - rotate right
	 * 						2 - fire a shell!
	 * 						3 - something has gone wrong
	 */
	public int moveToAimDirection(AnimatedSprite AI, Vector2D direction, double tolerance){
		double moveAngle = 0;
		double angle = direction.angle();
		angle = Math.toDegrees(angle) + 90;
		if (angle >= 360){
			angle -= 360;
		}
		double oangle = AI.faceAngle();
		
		double diff = angle - oangle;
		
		if (diff > 180){
			moveAngle = 360 - diff;
		}
		else if (diff < -180){
			moveAngle = -360 - diff;
		}
		else{
			moveAngle = -diff;
		}
		
//		System.out.println(moveAngle);
		if (Math.abs(moveAngle) >= 0.5*tolerance){
			if (moveAngle > 0){
				return 0;
			}
			else if (moveAngle < 0){
				return 1;
			}
		}
		else{
			return 2;
		}
		
		return 3;
		
	}
	
	
	/**
	 * Method that applies the necessary action to the AI tank
	 * 
	 * @param redTank
	 * @param blueTank
	 */
	public void checkAIInput(Tank redTank, Tank blueTank) {
	Vector2D direction = findAimDirection(blueTank, redTank, redTank.SHELL_SPEED);

	// System.out.println("x: " + g.x());
	// System.out.println("y: " + g.y());

	int i = moveToAimDirection(blueTank, direction, blueTank.TANK_ROTATION);

	switch (i) {
	case 0:
		blueTank.tankLeft();
		break;
	case 1:
		blueTank.tankRight();
		break;
	case 2:
		// fire shell from the tank if reloaded
		if (System.currentTimeMillis() > blueTank.startTime + 1000 * blueTank.SHELL_RELOAD) {
			blueTank.fireShell();
			blueTank.startTime = System.currentTimeMillis();
		}
		break;
	case 3:
		System.out.println("Houston we have a problem");
		break;
	}
}

}
