package gameEngine;

import java.awt.Graphics2D;

import math.geom2d.Point2D;
import math.geom2d.Vector2D;

public abstract class Box {
	// Rotational speed
	double angle = 0;
	// Velocity
	public Vector2D vel;
	// List of vertices
	Point2D[] vrtx;

	/**
	 * Any drawn object on the screen that can move and collide. Must call add() method to add to the screen sprite
	 * list.
	 */
	public Box() {
	}

	public abstract void init();

	/**
	 * Finds the n many edges that make up the sprite
	 * 
	 * @return An array containing edges in Vector2Ds
	 */
	public Vector2D[] getEdges() {
		Vector2D[] e = new Vector2D[vrtx.length];
		for (int i = 0; i < vrtx.length; i++) {
			e[i] = new Vector2D(vrtx[i], vrtx[(i + 1) % (vrtx.length)]);
		}
		return e;
	}

	public void rotate(double theta) {
		double t = theta - angle;
		for (int i = 0; i < vrtx.length; i++) {
			vrtx[i] = vrtx[i].rotate(Math.toRadians(t));
		}
		angle = theta;
	}

	public void velocity(Vector2D v) {
		vel = v;
	}

	public void render(Graphics2D g2d, Point2D cent) {
		for (int i = 0; i < vrtx.length; i++) {
			drawLn(vrtx[i].plus(cent), vrtx[(i + 1) % (vrtx.length)].plus(cent), g2d);
		}
		cent.draw(g2d);
	}

	public static void drawLn(Point2D a, Point2D b, Graphics2D g2d) {
		g2d.drawLine((int) a.x(), (int) a.y(), (int) b.x(), (int) b.y());
	}
}
