package screens;

import java.awt.Graphics2D;

import tankAttack.AI;
import tankAttack.BaseGameplay;
import tankAttack.TankAttack;

public class Solo extends BaseGameplay {

	AI hal;

	public Solo(TankAttack g, Graphics2D g2d) {
		super(g, g2d);
		gamemodecode = TankAttack.PLAYER_VS_AI;
		localmultiplayer = false;
	}
	
	public void initiate(){
		super.initiate();
		hal = new AI(g);
	}

	@Override
	public void checkInputs() {
		redTank.checkInputs();
		hal.checkAIInput(redTank, blueTank);
	}
}
