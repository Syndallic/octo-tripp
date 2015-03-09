package tankAttack;

import java.awt.Graphics2D;

public class Versus extends Screen{
	
	Tank redTank, blueTank;
	
	public Versus(Game g, Graphics2D g2d, Tank tank1, Tank tank2){
		super(g, g2d);
		this.redTank = tank1;
		this.blueTank = tank2;
	}
}
