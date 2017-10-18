package gameEngine;

import math.geom2d.Point2D;

public class Rectangle extends Box {
	int width, height;
	public Rectangle(int width, int height) {
		this.width=width;
		this.height=height;
		init();
	}

	@Override
	public void init() {
		vrtx = new Point2D[4];
		vrtx[0] = new Point2D(-width/2, -height/2);
		vrtx[1] = new Point2D(width/2, -height/2);
		vrtx[2] = new Point2D(width/2, height/2);
		vrtx[3] = new Point2D(-width/2, height/2);		
	}

}
