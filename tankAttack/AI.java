package tankAttack;

import java.util.ArrayList;

import gameEngine.AnimatedSprite;
import gameEngine.MathHelp;
import math.geom2d.Point2D;
import math.geom2d.Vector2D;

enum State {
	DEFENSE, ATTACK
};

public class AI {

	private TankAttack game;
	private State state = State.ATTACK;
	
	public AI(TankAttack game){
		this.game = game;
	}

	/**
	 * Method to find the vector to fire in to hit enemy sprite
	 * 
	 * @param AI
	 *            The bot
	 * @param target
	 *            The sprite being aimed at
	 * @param _shellspeed
	 *            Speed of the shell being fired
	 * @return Direction vector to fire in to hit enemy sprite
	 */
	private Vector2D findAimVector(AnimatedSprite AI, AnimatedSprite target, double _shellspeed) {
		double SHELL_SPEED = _shellspeed;

		// method credit to Kain Shin

		// construct math.geom2d Point2D objects of each sprite position
		Point2D targetpos = new Point2D(target.center().X(), target.center().Y());
		Point2D AIpos = new Point2D(AI.center().X(), AI.center().Y());

		Vector2D distdiff = new Vector2D(AIpos, targetpos);
		// distdiff = distdiff.normalize();

		Vector2D zero = new Vector2D();

		if (target.velocity().X() != 0 && distdiff != zero) {
			Vector2D targetvel = new Vector2D(target.velocity().X(), target.velocity().Y());
			// targetvel = targetvel.normalize();

			// dot product part
			double costheta = Vector2D.dot(distdiff.normalize(), targetvel.normalize());

			// set up terms for quadratic equation used to find t
			double a = Math.pow(SHELL_SPEED, 2) - Math.pow(targetvel.norm(), 2);
			double b = 2 * distdiff.norm() * costheta;
			double c = -Math.pow(distdiff.norm(), 2);

			// use quadratic formula to solve for t
			double t1 = (-b - Math.sqrt(Math.pow(b, 2) - 4 * a * c)) / (2 * a);
			double t2 = (-b + Math.sqrt(Math.pow(b, 2) - 4 * a * c)) / (2 * a);

			// find which value of t to use
			double t;
			if (t1 == t2) {
				t = t1;
			} else if (t1 < 0) {
				t = t2;
			} else if (t2 < 0) {
				t = t1;
			} else if (t1 < t2) {
				t = t1;
			} else if (t2 < t1) {
				t = t2;
			} else {
				System.out.println("Logic failure somewhere in findAimVector");
				t = 1;
			}

			// find end direction vector
			t = 1 / t;
			Vector2D aimDirection = distdiff.times(t);
			aimDirection = aimDirection.plus(targetvel);

			return aimDirection;
		} else {
			return distdiff;
		}
	}

	/**
	 * Method to work out the quickest way to align one vector with another. Can be used to move AI into a position
	 * where it can make a good shot, and judge when to fire
	 * 
	 * @param toface
	 *            Vector 'facing' is aligning to
	 * @param facing
	 *            The variable vector (e.g. direction a tank is facing)
	 * @param tolerance
	 *            often set equal to TANK_ROTATION, the increment of rotation per tick
	 * @return -1 - rotate left 0 - vectors aligned! 1 - rotate right
	 */
	private int findAimMovement(Vector2D toface, Vector2D facing, double tolerance) {
		double moveAngle = 0;
		double diff = MathHelp.getVector2DDegrees(toface) - MathHelp.getVector2DDegrees(facing);

		if (diff > 180) {
			moveAngle = 360 - diff;
		} else if (diff < -180) {
			moveAngle = -360 - diff;
		} else {
			moveAngle = -diff;
		}
		if (Math.abs(moveAngle) >= 0.5 * Math.abs(tolerance)) {
			return (int) -Math.signum(moveAngle);
		} else {
			return 0;
		}
		// go forward too if too far away?
	}

	/**
	 * Method that applies the necessary action to the AI tank
	 * 
	 * @param redTank
	 * @param blueTank
	 */
	public void checkAIInput(Tank redTank, Tank blueTank) {
		ArrayList<TankAction> actions = new ArrayList<TankAction>();
		switch (state) {
		case ATTACK:
			actions = findAttackActions(redTank, blueTank);
			break;
		case DEFENSE:
			actions = findDefenseActions(redTank, blueTank);
			break;
		}

		for (TankAction a : actions) {
			if (a == TankAction.FIRE) {
				// fire shell from the tank if reloaded
				blueTank.fireShell();
			} else if (a == TankAction.LEFT) {
				blueTank.tankLeft();
			} else if (a == TankAction.RIGHT) {
				blueTank.tankRight();
			} else if (a == TankAction.UP) {
				blueTank.tankUp();
			} else if (a == TankAction.DOWN) {
				blueTank.tankDown();
			} else {
				System.out.println("Unsupported tank action!");
			}
		}

	}

	private ArrayList<TankAction> findDefenseActions(Tank redTank, Tank blueTank) {
		ArrayList<TankAction> actions = new ArrayList<TankAction>();
		Vector2D facing = MathHelp.getVector2D(blueTank.faceAngle());
		Vector2D toface = MathHelp.getVector2D(redTank.faceAngle()).rotate(Math.PI/2);
		
		int i = findAimMovement(toface, facing, Tank.TANK_ROTATION);
		switch (i) {
		case -1:
			actions.add(TankAction.LEFT);
			break;
		case 1:
			actions.add(TankAction.RIGHT);
			break;
		}
		
		i = dodgeDirection(redTank, blueTank);
		
		return actions;
	}

	private int dodgeDirection(Tank redTank, Tank blueTank) {
		ArrayList<Bullet> bullets = game.getFiredBullets(redTank);
		ArrayList<Vector2D> attackvectors = new ArrayList<Vector2D>();
		for (Bullet b : bullets){
			double distance = b.getSpeed()*b.lifespan(); //calculates range properly
			attackvectors.add(MathHelp.getVector2D(b.faceAngle()).times(distance)); //vector is normalised at first
		}
		return 0;
	}

	private ArrayList<TankAction> findAttackActions(Tank redTank, Tank blueTank) {
		ArrayList<TankAction> actions = new ArrayList<TankAction>();
		Vector2D aimvector = findAimVector(blueTank, redTank, Shell.SHELL_SPEED);
		int i = findAimMovement(aimvector, MathHelp.getVector2D(blueTank.faceAngle()), Tank.TANK_ROTATION);
		switch (i) {
		case -1:
			actions.add(TankAction.LEFT);
			break;
		case 0:
			actions.add(TankAction.FIRE);
			break;
		case 1:
			actions.add(TankAction.RIGHT);
			break;
		}

		return actions;
	}

}
