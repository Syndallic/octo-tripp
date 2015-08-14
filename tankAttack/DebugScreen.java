package tankAttack;

import java.awt.Color;
import java.awt.event.KeyEvent;

import math.geom2d.Vector2D;
import tankAttack.collision.DetectCollision;
import tankAttack.collision.RigidRect;
import tankAttack.collision.RigidShape;
import tankAttack.collision.RigidTriangle;

public class DebugScreen extends Screen {

	RigidShape r1, r2;
	double speed = 5;
	double acc = 1;
	double r = 0;
	double theta = 0;
	Vector2D velocity;
	Vector2D translation;

	boolean left, right, up, down;

	public DebugScreen(Game g) {
		super(g);
	}

	@Override
	void initiate() {
		r1 = new RigidRect(500, 500, 50, 100);
		r2 = new RigidTriangle(300, 300, 100);
		velocity = new Vector2D(0, 0);
	}

	@Override
	void update() {

		r1.draw(g2d);
		r2.draw(g2d);

		input();
		r1.translate(velocity);
		r1.rotate(r);
		r=0;
		drawLine(r1.getCenter(), velocity.times(25.0), Color.BLUE);

		Vector2D translation = DetectCollision.findTranslation(r1, r2);
		if (translation != null) {
			drawLine(r1.getCenter(), translation.times(25), Color.RED);
			r1.translate(translation);
		}
	}

	@Override
	void resetScreen() {
		// TODO Auto-generated method stub

	}

	public void keyPressed(int keyCode) {
		switch (keyCode) {
		case KeyEvent.VK_A:
			left = true;
			break;
		case KeyEvent.VK_D:
			right = true;
			break;
		case KeyEvent.VK_W:
			up = true;
			break;
		case KeyEvent.VK_S:
			down = true;
			break;
		}

		if (keyCode == KeyEvent.VK_ESCAPE) {
			g.stop();
		}
	}

	public void keyReleased(int keyCode) {
		switch (keyCode) {
		case KeyEvent.VK_A:
			left = false;
			break;
		case KeyEvent.VK_D:
			right = false;
			break;
		case KeyEvent.VK_W:
			up = false;
			break;
		case KeyEvent.VK_S:
			down = false;
			break;
		}
	}

	public void input() {
		double v = 0;
		if (right) {
			r = 0.1;
		}
		if (left) {
			r = -0.1;
		}
		if (up) {
			v = -speed;
		}
		if (down) {
			v = speed * 0.5;
		}
		velocity = new Vector2D(0, v).rotate(theta +=r);
	}

}
