package tankAttack.collision;

import java.awt.Graphics2D;

import math.geom2d.Vector2D;

public class RigidCircle extends RigidShape{

	int r;
	
	public RigidCircle(int x, int y, int r) {
		super(x, y);
		this.r = r;
		// TODO Auto-generated constructor stub
	}
	
	public Projection projectShape(Vector2D axis){
		return null;
	}
	
	public Vector2D[] findProjectionAxis(){
		return null;
	}
	
	public void rotate(double theta){
		System.out.println("Good luck rotating a circle");
	}
	
	public void draw(Graphics2D g2d){
		g2d.drawOval((int)(c1.x() - (r/2)),(int)(c1.y() - (r/2)), r, r);
	}
}
