package screens;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import gameEngine.Button;
import tankAttack.TankAttack;

public class MainMenu extends Screen {

	// All the buttons including the null button
	// The null button is just an invisible button that the cursor reaches
	// select further than the last button, before it loops. Purely asthetical
	Button b1, b2, b3, b4, bn;
	
	// Button array to keep all the buttons
	Button[] b;
	
	// Store current button index from array
	int n = 0;
	
	Font f1, f2, f3, f4;
	
	// Well, I'll let your creative mind think of what this does
	int butNum;
	
	
	/**
	 * Extension of the screen class, contains the methods for rendering the
	 * main main screen reached by the client first
	 * 
	 * @param g
	 *            the current game that is running
	 * @param g2d
	 *            the current graphics2D that is being used by the game
	 */
	public MainMenu(TankAttack g, Graphics2D g2d) {
		super(g, g2d);
	}

	public void initiate() {
		butNum = 5;
		
		b1 = new Button(this);
		b2 = new Button(this);
		b3 = new Button(this);
		b4 = new Button(this);
		bn = new Button(this);
		
		b1.setString("SOLO PLAY");
		b2.setString("DUO PLAY");
		b3.setString("ONLINE");
		b4.setString("CONTROLS");
		
		b1.setCentre(SCREENWIDTH / 2, 500);
		b2.setCentre(SCREENWIDTH / 2, 550);
		b3.setCentre(SCREENWIDTH / 2, 600);
		b4.setCentre(SCREENWIDTH / 2, 650);
		
		
		b1.setEvent(PLAYER_VS_AI);
		b2.setEvent(PLAYER_VS_PLAYER);
		b3.deactivated();
		b4.setEvent(CONTROLS_MENU);

		b1.selected();
		
		// All buttons added to a button array
		b = new Button[butNum];
		b[0] = b1;
		b[1] = b2;
		b[2] = b3;
		b[3] = b4;
		b[4] = bn;

	}

	public void update() {

		g2d.setFont(new Font("Verdana", Font.BOLD, 70));
		g2d.setColor(Color.BLACK);
		printSimpleString("TANK ATTACK", SCREENWIDTH, -2, SCREENHEIGHT / 3 - 2);
		g2d.setColor(new Color(200, 30, 30));
		printSimpleString("TANK ATTACK", SCREENWIDTH, 0, SCREENHEIGHT / 3);

		for (int i = 0; i < b.length; i++) {
			b[i].update();
		}
	}

	public void keyPressed(int keyCode) {

		// Cycling through the buttons
		
		if (keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_S) {
			b[n].normal();
			n = (n + 1) % butNum;
			b[n].selected();
		}

		if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_W) {
			b[n].normal();
			n = (n - 1) % butNum;
			if (n == -1) {
				n = butNum -1;
			}
			b[n].selected();
		}

		// Handling when button is activated
		
		if (keyCode == KeyEvent.VK_ENTER) {
			if (b[n].state != Button.DEACTIVATED) {
				switch(b[n].getEvent()){
				case 1:
					g.pvai.resetScreen();
					g.pvai.makeCurrent();
					g.resumeGame();
					break;
				case 2:
					g.pvp.resetScreen();
					g.pvp.makeCurrent();
					g.resumeGame();
					break;
				case 3:
					g.controls.makeCurrent();
					break;
				}
				
			}
		}
		
		// Exit JFrame if esc is hit from the main menu
		if(keyCode == KeyEvent.VK_ESCAPE){
			g.stop();
		}
	}

}
