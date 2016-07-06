package screens;

import java.awt.Graphics2D;

import gameEngine.Map;
import tankAttack.BaseGameplay;
import tankAttack.TankAttack;

public class Versus extends BaseGameplay {

	public Versus(TankAttack g, Graphics2D g2d) {
		super(g, g2d);
		gamemodecode = TankAttack.PLAYER_VS_PLAYER;
		localmultiplayer = true;
	}
	
	public void initiate(){
		super.initiate();
	}

	@Override
	public void checkInputs() {
		redTank.checkInputs();
		blueTank.checkInputs();
	}
}
