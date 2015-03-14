package tankAttack;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

public class Versus extends Screen{
	
	Tank redTank, blueTank;
	ImageEntity shellImage;
	ImageEntity[] explosions;
	
	final int KILLPOINTS = 100;
	final int KILLCAP = 15;
	
	public Versus(Game g, Graphics2D g2d){
		super(g, g2d);
		g.setGameState(PLAYER_VS_PLAYER);
	}
	
	public void initiate(){
		explosions = new ImageEntity[1];
		// create red tank first in sprite list

		redTank = new Tank(g, graphics(), "redtank.png", "redtank2.png",
				"redhealth.png");
		redTank.setPosition(new Point2D(SCREENWIDTH * Math.random(),
				SCREENHEIGHT * Math.random()));
		add(redTank);

		// create blue tank second in sprite list
		blueTank = new Tank(g, graphics(), "bluetank.png", "bluetank2.png",
				"bluehealth.png");
		blueTank.setPosition(new Point2D(SCREENWIDTH * Math.random(),
				SCREENHEIGHT * Math.random()));
		add(blueTank);
		// load explosion image
		explosions[0] = new ImageEntity(g, "explosion.png");

		// load the shell sprite image
		shellImage = new ImageEntity(g, "shell.png");
	}
	
	public void update(){
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
		
		redTank.drawHealthBar(g2d, g, SCREENWIDTH -400, 60);
		
		g2d.setColor(Color.BLUE);
		g2d.drawString("HP", 10, 80);
		// health image is 1 pixel wide
		blueTank.drawHealthBar(g2d, g, 100, 60);
		
		redTank.checkInputs();
		blueTank.checkInputs();
	
		if (redTank.score() >= KILLCAP * KILLPOINTS
				|| blueTank.score() >= KILLCAP * KILLPOINTS) {
			g.pauseGame();
			g.screen = new GameOver(g, g2d, redTank.score(), blueTank.score());
		}
	}
	
	public void keyPressed(int keyCode){
		switch (keyCode) {
		// Red Tank controls
		case KeyEvent.VK_LEFT:
			redTank.left = true;
			break;
		case KeyEvent.VK_RIGHT:
			redTank.right = true;
			break;
		case KeyEvent.VK_UP:
			redTank.up = true;
			break;
		case KeyEvent.VK_DOWN:
			redTank.down = true;
			break;
		case KeyEvent.VK_ENTER:
			redTank.fire = true;
			break;
		
		// Blue Tank controls
		case KeyEvent.VK_A:
			blueTank.left = true;
			break;
		case KeyEvent.VK_D:
			blueTank.right = true;
			break;
		case KeyEvent.VK_W:
			blueTank.up = true;
			break;
		case KeyEvent.VK_S:
			blueTank.down = true;
			break;
		case KeyEvent.VK_CONTROL:
			blueTank.fire = true;
			break;
		}
		
		if(keyCode == KeyEvent.VK_ESCAPE){
			g.screen = new GameOver(g, g2d, redTank.score(), blueTank.score());
		}
	}
	
	public void keyReleased(int keyCode){
		switch (keyCode) {
		// Red Tank controls
		case KeyEvent.VK_LEFT:
			redTank.left = false;
			break;
		case KeyEvent.VK_RIGHT:
			redTank.right = false;
			break;
		case KeyEvent.VK_UP:
			redTank.up = false;
			break;
		case KeyEvent.VK_DOWN:
			redTank.down = false;
			break;
		case KeyEvent.VK_ENTER:
			redTank.fire = false;
			break;
		
		// Blue Tank controls
		case KeyEvent.VK_A:
			blueTank.left = false;
			break;
		case KeyEvent.VK_D:
			blueTank.right = false;
			break;
		case KeyEvent.VK_W:
			blueTank.up = false;
			break;
		case KeyEvent.VK_S:
			blueTank.down = false;
			break;
		case KeyEvent.VK_CONTROL:
			blueTank.fire = false;
			break;
		}
	}
	
	public void resetScreen(){
		g.sprites().clear();

		// add tanks to sprite list
		redTank.setPosition(new Point2D(SCREENWIDTH * Math.random(),
				SCREENHEIGHT * Math.random()));
		redTank.setAlive(true);
		redTank.setVelocity(new Point2D(0, 0));
		add(redTank);

		blueTank.setPosition(new Point2D(SCREENWIDTH * Math.random(),
				SCREENHEIGHT * Math.random()));
		blueTank.setAlive(true);
		blueTank.setVelocity(new Point2D(0, 0));
		add(blueTank);

		// reset variables
		redTank.setScore(0);
		redTank.setHealth(redTank.TANK_HEALTH);
		redTank.setState(redTank.STATE_NORMAL);
		blueTank.setScore(0);
		blueTank.setHealth(blueTank.TANK_HEALTH);
		blueTank.setState(blueTank.STATE_NORMAL);
	}
}
