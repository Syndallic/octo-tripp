package tankAttack.collision;

import math.geom2d.Vector2D;

public class DetectCollision {

	/**
	 * Finds the minimum translation vector to move apart two rigid shapes that have collided
	 * @param a being the moving shape
	 * @param b being the stationary shape
	 * @return null if no collision
	 */
	
	public static Vector2D findTranslation(RigidShape a, RigidShape b) {
		Vector2D[] v = a.findProjectionAxis();
		Vector2D smallest = new Vector2D(0,0);
		double overlap = 100;
		
		for(int i = 0; i < v.length; i++){
			Projection p0 = a.projectShape(v[i]);
			Projection p1 = b.projectShape(v[i]);
			
			double o = p0.getOverlap(p1);
			
			if (o == 0.0){
				return null;
			}
			
			if(o < overlap){
				overlap = o;
				smallest = v[i];
			}
			
		}
		
		v = b.findProjectionAxis();
		
		for(int i = 0; i < v.length; i++){
			Projection p0 = a.projectShape(v[i]);
			Projection p1 = b.projectShape(v[i]);
			
			double o = p0.getOverlap(p1);

			if (o == 0.0){
				return null;
			}
			if(o < overlap){
				overlap = o;
				smallest = v[i];
			}
			
		}
		Vector2D centreLinking = new Vector2D(a.getCenter(), b.getCenter());
		Vector2D translation = smallest.times(overlap+=0.5);
		double dot = centreLinking.dot(translation);
		if (dot < 0) {
			
		} else if (dot == 0) {
			translation = new Vector2D(0,0);
		} else {
			translation = translation.times(-1.0);
		}
		return translation;
	}
	
	
	/**
	 * This collision detection uses SAT which states if a line can be drawn
	 * between two shapes, they cannot be in contact. Not optimised
	 * 
	 * @param a
	 *            First rigidshape to be tested
	 * @param b
	 *            Second rigidshape to be tested
	 * @return true if they have collided
	 */
//	public static boolean detectCollision(RigidShape a, RigidShape b) {
//
//		double aLength;
//		double bLength;
//		double cLength;
//
//		double overlap;
//
//		Vector2D centreJoiningVector = new Vector2D(a.getCenter(),
//				b.getCenter());
//
//		// screen.drawLine(a.getCenter(), centreJoiningVector);
//
//		Vector2D projectionAxis = a.getWidthAxis().normalize();
//
//		bLength = getLength(b, projectionAxis);
//		aLength = a.width;
//		cLength = projection(centreJoiningVector, projectionAxis);
//		overlap = (aLength + bLength + 2) / 2 - cLength;
//		if (overlap < 0) {
//			return false;
//		}
//
//		projectionAxis = a.getHeightAxis().normalize();
//
//		bLength = getLength(b, projectionAxis);
//		aLength = a.height;
//		cLength = projection(centreJoiningVector, projectionAxis);
//		overlap = (aLength + bLength + 2) / 2 - cLength;
//		if (overlap < 0) {
//			return false;
//		}
//
//		projectionAxis = b.getWidthAxis().normalize();
//
//		bLength = getLength(a, projectionAxis);
//		aLength = b.width;
//		cLength = projection(centreJoiningVector, projectionAxis);
//		overlap = (aLength + bLength + 2) / 2 - cLength;
//
//		if (overlap < 0) {
//			return false;
//		}
//
//		projectionAxis = b.getHeightAxis().normalize();
//
//		bLength = getLength(a, projectionAxis);
//		aLength = b.height;
//		cLength = projection(centreJoiningVector, projectionAxis);
//		overlap = (aLength + bLength + 2) / 2 - cLength;
//
//		if (overlap < 0) {
//			return false;
//		}
//
//		return true;
//
//	}
	
}
