package gameEngine;

import math.geom2d.Point2D;
import math.geom2d.Vector2D;

/**
 * Class to contain useful maths methods with a horrendous spelling for solidarity with the Math class and to reduce the
 * number of letters typed when accessing
 * 
 * @author Peter
 *
 */
public class MathHelp {
	
    /**
     * Returns a normalised Vector2D with the angle (degrees) passed as the argument
     * 
     * @return
     */
    public static Vector2D getVector2D(double angle){
    	double a = angle - 90;
    	a = a%360;
    	if (a<0){
    		a+= 360;
    	}
    	Vector2D direction = Vector2D.createPolar(1.0, Math.toRadians(a));
    	return direction;
    }
    
	/**
	 * Returns angle of given vector in degrees, adjusted so 0 degrees is North rather than East
	 * 
	 * @param vector
	 * @return
	 */
	public static double getVector2DDegrees(Vector2D vector) {
		double angle = vector.angle();
		angle = Math.toDegrees(angle) + 90;
		angle = angle % 360;
		return angle;
	}
	
	public static Vector2D findVectorBetween(Sprite a, Sprite b){
		Point2D apos = new Point2D(a.center().X(), a.center().Y());
		Point2D bpos = new Point2D(b.center().X(), b.center().Y());
		return new Vector2D(apos, bpos);
	}
	
	/**
	 * Returns the perpendicular to a vector
	 * @param v
	 * @return v perpendicular
	 */
	
	public static Vector2D perp(Vector2D v){
		return new Vector2D(v.y(), -v.x());
	}
}
