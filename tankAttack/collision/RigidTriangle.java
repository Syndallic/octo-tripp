package tankAttack.collision;

import math.geom2d.Point2D;

public class RigidTriangle extends RigidShape {

	public RigidTriangle(int x, int y, int length) {
		super(x, y);

		p = new Point2D[3];

		p[0] = new Point2D(0, length);
		p[1] = new Point2D(length * 0.866, -length * 0.5);
		p[2] = new Point2D(-length * 0.866, -length * 0.5);
	}

}
