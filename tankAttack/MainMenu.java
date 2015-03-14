package tankAttack;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

public class MainMenu extends Screen {

	Button b1, b2, b3;
	Button[] b;
	int n = 0;
	Font f1, f2, f3, f4;

	/**
	 * Extension of the screen class, contains the methods for rendering the
	 * main main screen reached by the client first
	 * 
	 * @param g
	 *            the current game that is running
	 * @param g2d
	 *            the current graphics2D that is being used by the game
	 */
	public MainMenu(Game g, Graphics2D g2d) {
		super(g, g2d);
		g.setGameState(MAIN_MENU);
	}

	public void initiate() {
		b1 = new Button(this);
		b2 = new Button(this);
		b3 = new Button(this);

		b1.selected();

		b1.setString("SOLO PLAY");
		b2.setString("DUO PLAY");
		b3.setString("ONLINE");
		b1.setCentre(SCREENWIDTH / 2, 500);
		b2.setCentre(SCREENWIDTH / 2, 550);
		b3.setCentre(SCREENWIDTH / 2, 600);
		
		b1.deactivated();
		b2.setEvent(PLAYER_VS_PLAYER);
		b3.deactivated();

		// All buttons added to a button array
		b = new Button[3];
		b[0] = b1;
		b[1] = b2;
		b[2] = b3;

		// All the fonts pre-loaded for the update method
		f1 = new Font("Verdana", Font.BOLD, 36);
		f2 = new Font("Times New Roman", Font.ITALIC | Font.BOLD, 30);
		f3 = new Font("Times New Roman", Font.ITALIC | Font.BOLD, 20);
		f4 = new Font("Ariel", Font.BOLD, 24);
	}

	public void update() {

		g2d.setFont(f1);
		g2d.setColor(Color.BLACK);
		printSimpleString("TANK ATTACK", SCREENWIDTH, -2, SCREENHEIGHT / 3 - 2);
		g2d.setColor(new Color(200, 30, 30));
		printSimpleString("TANK ATTACK", SCREENWIDTH, 0, SCREENHEIGHT / 3);

		int x = SCREENWIDTH / 4, y = SCREENHEIGHT / 2;

		g2d.setFont(f2);
		g2d.setColor(Color.GRAY);
		printSimpleString("CONTROLS:", SCREENWIDTH, 0, y);

		g2d.setFont(f3);
		printSimpleString("END GAME - Escape", SCREENWIDTH, 0, y + 300);
		g2d.setColor(Color.RED);
		printSimpleString("ROTATE - Left/Right Arrows", SCREENWIDTH, x, y += 40);
		printSimpleString("FORWARDS - Up Arrow", SCREENWIDTH, x, y += 30);
		printSimpleString("REVERSE - Down Arrow", SCREENWIDTH, x, y += 30);
		printSimpleString("FIRE - Enter", SCREENWIDTH, x, y += 30);

		y = SCREENHEIGHT / 2;
		g2d.setColor(Color.BLUE);
		printSimpleString("ROTATE - A/D", SCREENWIDTH, -x, y += 40);
		printSimpleString("FORWARDS - W", SCREENWIDTH, -x, y += 30);
		printSimpleString("REVERSE - S", SCREENWIDTH, -x, y += 30);
		printSimpleString("FIRE - Control", SCREENWIDTH, -x, y += 30);
		printSimpleString("Activate AI - X", SCREENWIDTH, -x, y += 30);

//		g2d.setFont(f4);
//		g2d.setColor(Color.WHITE);
//		printSimpleString("Press SPACE to start", SCREENWIDTH, 0,
//				(int) (0.8 * SCREENHEIGHT));

		for (int i = 0; i < b.length; i++) {
			b[i].update();
		}
	}

	public void keyPressed(int keyCode) {

		if (keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_S) {
			b[n].normal();
			n = (n + 1) % 3;
			b[n].selected();
		}

		if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_W) {
			b[n].normal();
			n = (n - 1) % 3;
			if (n == -1) {
				n = 2;
			}
			b[n].selected();
		}

		if (keyCode == KeyEvent.VK_ENTER) {
			if (b[n].state != Button.DEACTIVATED) {
				switch(b[n].getEvent()){
				case 3:
					g.screen = new Versus(g, g2d);
					break;
				}
			}
		}
		
		if(keyCode == KeyEvent.VK_ESCAPE){
			g.stop();
		}
	}

}