package tankAttack.collision;

public class Projection {

	double min, max;

	public Projection(double min, double max) {
		this.min = min;
		this.max = max;
	}

	/**
	 * Method to find overlap of two projections on the same plane
	 * 
	 * @param p2
	 *            is the second projection
	 * @return null if no overlap
	 */
	public double getOverlap(Projection b) {
		Projection a = this;
		
		// Check if lines overlap at all
		if(a.max < b.min || a.min > b.max){ return 0.0; }
		
		// Check if one line is completely inside other line
		if(a.min > b.min && a.max < b.max){ return a.max - a.min; }
		if(b.min > a.min && b.max < a.max){ return b.max - b.min; }
		
		// Find overlap
		return Math.min(a.max - b.min, b.max - a.min);
	}
}
