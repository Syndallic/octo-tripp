package tankAttack.collision;

import math.geom2d.Point2D;
import math.geom2d.Vector2D;

public class RigidRect extends RigidShape {

	public RigidRect(int x, int y, int width, int height) {
		super(x, y);

		double w = width / 2;
		double h = height / 2;

		p = new Point2D[4];

		p[0] = new Point2D(-w, -h);
		p[1] = new Point2D(+w, -h);
		p[2] = new Point2D(+w, +h);
		p[3] = new Point2D(-w, +h);

	}

	public Vector2D[] findProjectionAxis() {
		Vector2D[] v = {
				new Vector2D(p[0].plus(c1), p[1].plus(c1)).normalize(),
				new Vector2D(p[1].plus(c1), p[2].plus(c1)).normalize() };
		return v;
	}

}
