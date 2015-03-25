package tankAttack;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

public class GameOver extends Screen {

	double rS, bS;
	
	public GameOver(Game g, Graphics2D g2d) {
		super(g, g2d);
	}
	
	public void setScores(int rs, int bs){
		rS = rs;
		bS = bs;
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
			g.pvp.resetScreen();
			g.pvp.makeCurrent();
		}
	}

}
