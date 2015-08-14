package tankAttack;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import org.omg.CORBA.Bounds;

public class GameOver extends Screen {

	double rS, bS;
	private int returnScreen;
	
	public GameOver(Game g) {
		super(g);
	}
	
	public void setScores(int rs, int bs){
		rS = rs;
		bS = bs;
	}
	
	public void setReturnScreen(int mode){
		returnScreen = mode;
	}
	
	public void update(){
		g2d.setFont(new Font("Verdana", Font.BOLD, 36));
		g2d.setColor(new Color(200, 30, 30));

		if (rS > bS) {
			printSimpleString("RED WINS!", SCREENWIDTH, 0, SCREENHEIGHT / 2);
		} else if (bS > rS) {
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
			
			if (returnScreen == 1){
				g.pvai.resetScreen();
				g.pvai.makeCurrent();
			}
			else if (returnScreen == 2){
				g.pvp.resetScreen();
				g.pvp.makeCurrent();
		}
			else{
				System.out.println("Return screen is out of Bounds.class Check gamemode class");
			}
	}

}

	@Override
	void initiate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	void resetScreen() {
		// TODO Auto-generated method stub
		
	}
}
