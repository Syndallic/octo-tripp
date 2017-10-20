package screens;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import tankAttack.GameMode;
import tankAttack.TankAttack;

public class GameOver extends Screen {

	double redscore, bluescore;
	private GameMode returnScreen;
	
	public GameOver(TankAttack g, Graphics2D g2d) {
		super(g, g2d);
	}
	
	public void setScores(int rs, int bs){
		redscore = rs;
		bluescore = bs;
	}
	
	public void setReturnScreen(GameMode gameMode){
		returnScreen = gameMode;
	}
	
	public void update(){
		g2d.setFont(new Font("Verdana", Font.BOLD, 36));
		g2d.setColor(new Color(200, 30, 30));

		if (redscore > bluescore) {
			printSimpleString("RED WINS!", SCREENWIDTH, 0, SCREENHEIGHT / 2);
		} else if (bluescore > redscore) {
			printSimpleString("BLUE WINS!", SCREENWIDTH, 0,
					SCREENHEIGHT / 2);
		} else {
			printSimpleString("DRAW!", SCREENWIDTH, 0, SCREENHEIGHT / 2);
		}

		g2d.setFont(new Font("Ariel", Font.CENTER_BASELINE, 24));
		g2d.setColor(Color.WHITE);
		printSimpleString("Press SPACE to restart", SCREENWIDTH, 0,
				(int) (0.8 * SCREENHEIGHT));
	}
	
	public void keyPressed(int keyCode){
		if(keyCode == KeyEvent.VK_ESCAPE){
			g.main.makeCurrent();
		}
		
		if(keyCode == KeyEvent.VK_SPACE){
			g.resumeGame();
			
			// returns to the previous screen
			if (returnScreen == GameMode.PLAYER_VS_AI){
				g.pvai.resetScreen();
				g.pvai.makeCurrent();
			}
			else if (returnScreen == GameMode.PLAYER_VS_PLAYER){
				g.pvp.resetScreen();
				g.pvp.makeCurrent();
		}
			else{
				System.out.println("Return screen is out of bounds.");
			}
	}

}
}
