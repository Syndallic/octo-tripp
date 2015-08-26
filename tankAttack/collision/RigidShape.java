package tankAttack.collision;

import java.awt.Color;
import java.awt.Graphics2D;

import math.geom2d.Point2D;
import math.geom2d.Vector2D;

/**
 * Abstract class which handles all shapes that can collide, and their inherited
 * characteristics such as moving, rotating and rendering. To create a new
 * shape, populate the array of points in a new constructor, so that it forms the
 * outline of the shape.
 * 
 * @author Jules
 * 
 */
public abstract class RigidShape {

	Point2D c1;
	Point2D[] p;
	double theta = 0.0;
	double mass = 1.0;

	public RigidShape(int x, int y) {
		c1 = new Point2D(x, y);
	}

	public Projection projectShape(Vector2D axis) {
		double min = axis.dot(new Vector2D(p[0].plus(c1)));
		double max = min;

		for (int i = 1; i < p.length; i++) {
			double d = axis.dot(new Vector2D(p[i].plus(c1)));
			if (d < min) {
				min = d;
			} else if (d > max) {
				max = d;
			}
		}

		return new Projection(min, max);
	}

	/**
	 * Method to find the axis to project on, which should be a vector
	 * perpendicular to each edge of the shape. This method handles any shape,
	 * but it may be worth writing a unique method for optimisation purposes as
	 * this method will sometimes produce repeated vectors, or to handle special
	 * conditions.
	 * 
	 * @return Vector2D array of all the projection axis
	 */
	public Vector2D[] findProjectionAxis() {
		Vector2D[] v = new Vector2D[p.length];
		for (int i = 0; i < p.length; i++) {
			v[i] = perp(new Vector2D(p[i], p[(i + 1) % p.length]).normalize());
		}

		return v;
	}

	public double getMass() {
		return mass;
	}

	public void setMass(double m) {
		mass = m;
	}

	public Point2D getCenter() {
		return c1;
	}

	public void setCenter(Point2D c) {
		c1 = c;
	}

	public void update() {

	}

	/**
	 * Performs a rotation transformation of all the points that make up the
	 * rectangle
	 * 
	 * @param angle
	 *            in radians
	 */

	public void rotate(double angle) {
		for (int i = 0; i < p.length; i++) {
			p[i] = p[i].rotate(angle);
		}
		theta += angle;
	}

	public void setRotation(double angle) {

		if (theta < 0) {
			theta = (2 * Math.PI) - theta;
		} else if (theta >= 2 * Math.PI) {
			theta = theta - 2 * Math.PI;
		}

		double a = angle - theta;
		for (int i = 0; i < p.length; i++) {
			p[i] = p[i].rotate(a);
		}
		theta = angle;

	}

	public double angle() {
		return theta;
	}

	/**
	 * Links all the points in the points array and draws it onto a graphics2D
	 * 
	 * @param g2d
	 *            Graphics buffer
	 */
	public void draw(Graphics2D g2d) {
		g2d.setColor(Color.BLACK);
		for (int i = 0; i < p.length; i++) {
			g2d.drawLine((int) (p[i].x() + c1.x()), (int) (p[i].y() + c1.y()),
					(int) (p[(i + 1) % p.length].x() + c1.x()), (int) (p[(i + 1) % p.length].y() + c1.y()));
		}
	}

	public void translate(int dx, int dy) {
		c1 = new Point2D(c1.x() + dx, c1.y() + dy);
	}

	public void translate(double dx, int dy) {
		c1 = new Point2D(c1.x() + dx, c1.y() + dy);
	}

	public void translate(Vector2D v) {
		c1 = new Point2D(Math.round(c1.x() + v.x()), Math.round(c1.y() + v.y()));
	}

	public Vector2D perp(Vector2D v) {
		return new Vector2D(v.y(), -v.x());
	}

	public Vector2D detectCollision(RigidShape a) {
		return DetectCollision.findTranslation(this, a);
	}
}