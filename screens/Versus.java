package screens;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;

import gameEngine.Button;
import gameEngine.Game;
import gameEngine.ImageEntity;
import tankAttack.Point2D;
import tankAttack.Tank;
import tankAttack.TankAttack;
import tankAttack.Turret;

public class Versus extends Screen {

	Tank redTank, blueTank;
	Turret redTurret, blueTurret;
	ImageEntity shellImage;
	ImageEntity[] explosions;

	final int RESET = 6;
	int butNum;
	int n = 0;

	Button b1, b2, bn;
	Button[] b;

	final int KILLPOINTS = 100;

	// Set low for debugging purposes
	final int KILLCAP = 3;

	public Versus(Game g, Graphics2D g2d) {
		super(g, g2d);
	}

	public void initiate() {
		butNum = 3;

		b1 = new Button(this);
		b2 = new Button(this);
		bn = new Button(this);

		b1.setString("RESET");
		b2.setString("BACK TO MAIN");

		b1.setCentre(SCREENWIDTH / 2, SCREENHEIGHT / 2 + 100);
		b2.setCentre(SCREENWIDTH / 2, SCREENHEIGHT / 2 + 150);

		b1.setEvent(RESET);
		b2.setEvent(MAIN_MENU);

		b1.selected();

		b = new Button[butNum];
		b[0] = b1;
		b[1] = b2;
		b[2] = bn;

		explosions = new ImageEntity[1];
		// create red tank first in sprite list

		redTank = new Tank(g, graphics(), "redtank.png", "redtank2.png", "redhealth.png");
		redTank.setPosition(new Point2D(SCREENWIDTH * Math.random(), SCREENHEIGHT * Math.random()));
		add(redTank);

		// create blue tank second in sprite list
		blueTank = new Tank(g, graphics(), "bluetank.png", "bluetank2.png", "bluehealth.png");
		blueTank.setPosition(new Point2D(SCREENWIDTH * Math.random(), SCREENHEIGHT * Math.random()));
		add(blueTank);

		// load explosion image
		explosions[0] = new ImageEntity(g, "explosion.png");

		// load the shell sprite image
		shellImage = new ImageEntity(g, "shell.png");
	}

	public void update() {
		g2d.setFont(new Font("Verdana", Font.BOLD, 30));
		printSimpleString("Score", SCREENWIDTH, 0, 40);
		g2d.setColor(Color.RED);
		printSimpleString("" + redTank.score(), SCREENWIDTH, 80, 100);
		g2d.setColor(Color.BLUE);
		printSimpleString("" + blueTank.score(), SCREENWIDTH, -80, 100);

		g2d.setFont(new Font("Verdana", Font.BOLD, 20));
		g2d.setColor(Color.RED);
		g2d.drawString("HP", SCREENWIDTH - 40, 80);
		// health image is 1 pixel wide

		redTank.drawHealthBar(g2d, g, SCREENWIDTH - 400, 60);

		g2d.setColor(Color.BLUE);
		g2d.drawString("HP", 10, 80);
		// health image is 1 pixel wide
		blueTank.drawHealthBar(g2d, g, 100, 60);

		redTank.checkInputs();
		blueTank.checkInputs();

		g.drawSprites();

		if (g.gamePaused()) {
			g2d.setFont(new Font("Verdana", Font.BOLD, 30));
			g2d.setColor(new Color(0, 0, 0, 120));
			g2d.fill(new Rectangle2D.Double(0, 0, SCREENWIDTH, SCREENHEIGHT));
			g2d.setColor(Color.WHITE);
			printSimpleString("GAME PAUSED", SCREENWIDTH, 0, SCREENHEIGHT / 2);
			for (int i = 0; i < b.length; i++) {
				b[i].update();
			}

		}
		if (redTank.score() >= KILLCAP * KILLPOINTS || blueTank.score() >= KILLCAP * KILLPOINTS) {
			g.gOver.setReturnScreen(PLAYER_VS_PLAYER);
			g.gOver.setScores(redTank.score(), blueTank.score());
			g.gOver.makeCurrent();
		}
	}

	public void keyPressed(int keyCode) {
		if (!g.gamePaused()) {
			switch (keyCode) {
			// miscellaneous keys

			case KeyEvent.VK_B:
				// toggle bounding rectangles
				TankAttack.toggleShowBounds();
				break;
			case KeyEvent.VK_C:
				// toggle collision testing
				TankAttack.toggleCollisionTesting();
				break;

			// Red Tank controls
			case KeyEvent.VK_LEFT:
				redTank.setLeft(true);
				break;
			case KeyEvent.VK_RIGHT:
				redTank.setRight(true);
				break;
			case KeyEvent.VK_UP:
				redTank.setUp(true);
				break;
			case KeyEvent.VK_DOWN:
				redTank.setDown(true);
				break;
			case KeyEvent.VK_ENTER:
				redTank.setFire(true);
				break;

			// Blue Tank controls
			case KeyEvent.VK_A:
				blueTank.setLeft(true);
				break;
			case KeyEvent.VK_D:
				blueTank.setRight(true);
				break;
			case KeyEvent.VK_W:
				blueTank.setUp(true);
				break;
			case KeyEvent.VK_S:
				blueTank.setDown(true);
				break;
			case KeyEvent.VK_CONTROL:
				blueTank.setFire(true);
				break;
			}
		} else {

			if (keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_S) {
				b[n].normal();
				n = (n + 1) % butNum;
				b[n].selected();
			}

			if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_W) {
				b[n].normal();
				n = (n - 1) % butNum;
				if (n == -1) {
					n = butNum - 1;
				}
				b[n].selected();
			}

			// Handling when button is activated

			if (keyCode == KeyEvent.VK_ENTER) {
				if (b[n].state != Button.DEACTIVATED) {
					switch (b[n].getEvent()) {
					case 0:
						resetScreen();
						g.main.makeCurrent();
						break;
					case 6:
						resetScreen();
						resume();
						break;
					}

				}
			}
		}
		if (keyCode == KeyEvent.VK_ESCAPE) {
			if (g.gamePaused()) {
				resume();
			} else {
				pause();
			}
		}

	}

	public void keyReleased(int keyCode) {
		switch (keyCode) {
		// Red Tank controls
		case KeyEvent.VK_LEFT:
			redTank.setLeft(false);
			break;
		case KeyEvent.VK_RIGHT:
			redTank.setRight(false);
			break;
		case KeyEvent.VK_UP:
			redTank.setUp(false);
			break;
		case KeyEvent.VK_DOWN:
			redTank.setDown(false);
			break;
		case KeyEvent.VK_ENTER:
			redTank.setFire(false);
			break;

		// Blue Tank controls
		case KeyEvent.VK_A:
			blueTank.setLeft(false);
			break;
		case KeyEvent.VK_D:
			blueTank.setRight(false);
			break;
		case KeyEvent.VK_W:
			blueTank.setUp(false);
			break;
		case KeyEvent.VK_S:
			blueTank.setDown(false);
			break;
		case KeyEvent.VK_CONTROL:
			blueTank.setFire(false);
			break;
		}
	}

	public void resetScreen() {
		g.sprites().clear();

		// add tanks to sprite list
		redTank.setPosition(new Point2D(SCREENWIDTH / 2 + 50, SCREENHEIGHT / 2));
		redTank.setFaceAngle(90);
		redTank.setAlive(true);
		redTank.setVelocity(new Point2D(0, 0));
		redTank.resetControls();
		add(redTank);

		blueTank.setPosition(new Point2D(SCREENWIDTH / 2 - 150, SCREENHEIGHT / 2));
		blueTank.setFaceAngle(270);
		blueTank.setAlive(true);
		blueTank.setVelocity(new Point2D(0, 0));
		blueTank.resetControls();
		add(blueTank);

		// reset variables
		redTank.init();
		blueTank.init();
	}

	public void pause() {
		g.pauseGame();
	}

	public void resume() {
		g.resumeGame();
	}
}
