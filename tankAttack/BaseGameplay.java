package tankAttack;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;

import gameEngine.Button;
import gameEngine.ImageEntity;
import gameEngine.EnginePoint2D;
import screens.Screen;

/**
 * Provides code for main gameplay. Classes extending this must implement control of the blue tank (e.g. AI/other
 * player)
 * 
 * @author Peter
 *
 */
public abstract class BaseGameplay extends Screen {

	protected Tank redTank;
	protected Tank blueTank;
	protected Turret redTurret, blueTurret;
	ImageEntity shellImage;
	ImageEntity[] explosions;
	
	protected GameMode gamemodecode; //set to PLAYER_VS_PLAYER for example
	protected boolean localmultiplayer; //to use blue keyboard controls or not

	int butNum;
	int n = 0;

	Button b1, b2, bn;
	Button[] b;

	final int KILLPOINTS = 100;

	// Set low for debugging purposes
	final int KILLCAP = 3;

	public BaseGameplay(TankAttack g, Graphics2D g2d) {
		super(g, g2d);
	}

	public void initiate() {
		initiateButtons();

		explosions = new ImageEntity[1];
		// create red tank first in sprite list

		redTank = new Tank(g, graphics(), "redtank.png", "redtank2.png", "redhealth.png");
		add(redTank);

		// create blue tank second in sprite list
		blueTank = new Tank(g, graphics(), "bluetank.png", "bluetank2.png", "bluehealth.png");
		add(blueTank);

		// load explosion image
		explosions[0] = new ImageEntity(g, "explosion.png");

		// load the shell sprite image
		shellImage = new ImageEntity(g, "shell.png");
	}

	public void initiateButtons() {
		butNum = 3;

		b1 = new Button(this);
		b2 = new Button(this);
		bn = new Button(this);

		b1.setString("RESET");
		b2.setString("BACK TO MAIN");

		b1.setCentre(TankAttack.getSCREENWIDTH() / 2, TankAttack.getSCREENHEIGHT() / 2 + 100);
		b2.setCentre(TankAttack.getSCREENWIDTH() / 2, TankAttack.getSCREENHEIGHT() / 2 + 150);

		b1.setEvent(GameMode.RESET);
		b2.setEvent(GameMode.MAIN_MENU);

		b1.selected();

		b = new Button[butNum];
		b[0] = b1;
		b[1] = b2;
		b[2] = bn;
	}

	public void update() {
		checkInputs();
		drawHUD();

		g.drawSprites();

		if (g.gamePaused()) {
			g2d.setFont(new Font("Verdana", Font.BOLD, 30));
			g2d.setColor(new Color(0, 0, 0, 120));
			g2d.fill(new Rectangle2D.Double(0, 0, TankAttack.getSCREENWIDTH(), TankAttack.getSCREENHEIGHT()));
			g2d.setColor(Color.WHITE);
			printSimpleString("GAME PAUSED", TankAttack.getSCREENWIDTH(), 0, TankAttack.getSCREENHEIGHT() / 2);
			for (int i = 0; i < b.length; i++) {
				b[i].update();
			}

		}
		if (redTank.score() >= KILLCAP * KILLPOINTS || blueTank.score() >= KILLCAP * KILLPOINTS) {
			g.gameover.setReturnScreen(getGameModeCode());
			g.gameover.setScores(redTank.score(), blueTank.score());
			g.gameover.makeCurrent();
		}
	}

	public void drawHUD() {
		g2d.setFont(new Font("Verdana", Font.BOLD, 30));
		printSimpleString("Score", TankAttack.getSCREENWIDTH(), 0, 40);
		g2d.setColor(Color.RED);
		printSimpleString("" + redTank.score(), TankAttack.getSCREENWIDTH(), 80, 100);
		g2d.setColor(Color.BLUE);
		printSimpleString("" + blueTank.score(), TankAttack.getSCREENWIDTH(), -80, 100);

		g2d.setFont(new Font("Verdana", Font.BOLD, 20));
		g2d.setColor(Color.RED);
		g2d.drawString("HP", TankAttack.getSCREENWIDTH() - 40, 80);
		// health image is 1 pixel wide

		redTank.drawHealthBar(g2d, g, TankAttack.getSCREENWIDTH() - 400, 60);

		g2d.setColor(Color.BLUE);
		g2d.drawString("HP", 10, 80);
		// health image is 1 pixel wide
		blueTank.drawHealthBar(g2d, g, 100, 60);
	}
	
	public abstract void checkInputs();

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

			}
			if (localmultiplayer) {
				switch (keyCode) {
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
					case MAIN_MENU:
						resetScreen();
						g.main.makeCurrent();
						break;
					case RESET:
						resetScreen();
						resume();
						break;
					default:
						System.out.println("Button with event " + b[n].getEvent().toString() + " should not exist right now");
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
		}
		if (localmultiplayer) {
			switch (keyCode) {
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

	}

	public void resetScreen() {
		g.sprites().clear();

		// add tanks to sprite list
		redTank.setPosition(new EnginePoint2D(TankAttack.getSCREENWIDTH() / 2 + 50, TankAttack.getSCREENHEIGHT() / 2));
		redTank.setFaceAngle(90);
		redTank.setAlive(true);
		redTank.setVelocity(new EnginePoint2D(0, 0));
		redTank.resetControls();
		add(redTank);

		blueTank.setPosition(new EnginePoint2D(TankAttack.getSCREENWIDTH() / 2 - 150, TankAttack.getSCREENHEIGHT() / 2));
		blueTank.setFaceAngle(270);
		blueTank.setAlive(true);
		blueTank.setVelocity(new EnginePoint2D(0, 0));
		blueTank.resetControls();
		add(blueTank);

		// reset variables
		redTank.init();
		blueTank.init();
	}

	public GameMode getGameModeCode(){
		return gamemodecode;
	}
	
	public void pause() {
		g.pauseGame();
	}

	public void resume() {
		g.resumeGame();
	}
}
