package tankAttack;

import java.awt.Graphics2D;

import javax.swing.JPanel;

import gameEngine.AnimatedSprite;
import gameEngine.Box;
import gameEngine.EnginePoint2D;
import gameEngine.MathHelp;
import math.geom2d.Point2D;
import math.geom2d.Vector2D;

public class Wall extends AnimatedSprite {

	Graphics2D g2d;
	// Four corners of the map
	Point2D x, y;
	final int SPRITE_WALL = 2;

	public Wall(JPanel a, Graphics2D g2d, Point2D x, Point2D y) {
		super(a, g2d);
		this.g2d = g2d;
		this.x = x;
		this.y = y;
		setBox(new Line(x, y));
		setSpriteType(SPRITE_WALL);
		setAlive(true);
	}

	public void draw() {
		drawLn(x, y);
	}
	public void updateFrame(){};
	public void transform(){};
	
	public EnginePoint2D center() {
		return MathHelp.midpoint(x, y);
	}

	public void drawLn(Point2D a, Point2D b) {
		g2d.drawLine((int) a.x(), (int) a.y(), (int) b.x(), (int) b.y());
	}

	class Line extends Box {

		Point2D length;

		public Line(Point2D x, Point2D y) {
			Vector2D v = new Vector2D(x, y);
			length = new Point2D(v.x(), v.y());
			init();
		}

		public void init() {
			vrtx = new Point2D[2];
			vrtx[0] = length.scale(0.5);
			vrtx[1] = length.scale(-0.5);
		}

	}
}
