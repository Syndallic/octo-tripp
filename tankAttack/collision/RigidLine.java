package tankAttack.collision;

import math.geom2d.Point2D;

public class RigidLine extends RigidShape {

	public RigidLine(int x, int y, Point2D b) {
		super(x, y);

		double dx, dy;
		dx = b.x()/2;
		dy = b.y()/2;
		p = new Point2D[2];

		p[0] = new Point2D(-dx, -dy);
		p[1] = new Point2D(dx, dy);
	}

}
