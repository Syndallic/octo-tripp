package tankAttack;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.JFrame;
// if this message appears in the repository then I pushed correctly :D
import math.geom2d.Vector2D;

public class TankAttack extends Game {
	// constants

	// static because they're passed to a constructor
	static int FRAMERATE = 60;
	static int SCREENWIDTH = 1200;
	static int SCREENHEIGHT = 800;

	// general global constants
	final int SHELL_SPEED = 10;
	final double SHELL_RELOAD = 0.4;
	final int SHELL_DAMAGE = -20; // negative to incur damage
	final int TANK_HEALTH = 100;
	final double TANK_SPEED = 5;
	final double TANK_ROTATION = 5.0;
	final int KILLPOINTS = 100;
	final int KILLCAP = 15;

	// sprite state values
	final int STATE_NORMAL = 0;
	final int STATE_COLLIDED = 1;
	final int STATE_EXPLODING = 2;

	// sprite types
	final int SPRITE_TANK = 1;
	final int SPRITE_SHELL = 100;
	final int SPRITE_EXPLOSION = 200;

	// game states
	final int GAME_MENU = 0;
	final int GAME_RUNNING = 1;
	final int GAME_OVER = 2;

	int gameState = GAME_MENU;

	// various toggles
	boolean showBounds = false;
	boolean collisionTesting = true;
	boolean AI = false;

	// timers
	private long redstartTime = System.currentTimeMillis();
	private long bluestartTime = System.currentTimeMillis();

	// define the images used in the game
	ImageEntity background;
	ImageEntity shellImage;
	ImageEntity redHealthBar;
	ImageEntity blueHealthBar;
	ImageEntity[] explosions;
	ImageEntity[] redTankImage;
	ImageEntity[] blueTankImage;

	// create a random number generator
	Random rand = new Random();

	// used to make tank temporarily invulnerable
	long collisionTimer = 0;

	// some key input tracking variables
	boolean redLeft, redRight, redUp, redDown, redFire, blueLeft, blueRight,
			blueUp, blueDown, blueFire, keyB, keyC;
	
	Tank redTank, blueTank;

	public TankAttack(JFrame f, String title) {
		
		// call base Game class' constructor
		super(f, FRAMERATE, title);
	}

	/**
	 * Load all images and spawn tanks
	 */
	void gameStartup() {
		explosions = new ImageEntity[1];
		redTankImage = new ImageEntity[2];
		blueTankImage = new ImageEntity[2];
		
		// load the background image
		background = new ImageEntity(this, "yellowbackground.png");

		// create red tank first in sprite list
		redTankImage[0] = new ImageEntity(this, "redtank.png");
		redTankImage[1] = new ImageEntity(this, "redtank2.png");


		redTank = new Tank(this, graphics(), "redtank.png", "redtank2.png");
		redTank.setPosition(new Point2D(SCREENWIDTH * Math.random(),
				SCREENHEIGHT * Math.random()));
		sprites().add(redTank);
		

		// create blue tank second in sprite list
		blueTankImage[0] = new ImageEntity(this, "bluetank.png");
		blueTankImage[1] = new ImageEntity(this, "bluetank2.png");

		blueTank = new Tank(this, graphics(), "bluetank.png", "bluetank2.png");
		blueTank.setPosition(new Point2D(SCREENWIDTH * Math.random(),
				SCREENHEIGHT * Math.random()));
		sprites().add(blueTank);

		// load explosion image
		explosions[0] = new ImageEntity(this, "explosion.png");

		// load the shell sprite image
		shellImage = new ImageEntity(this, "shell.png");

		// load the meter images
		redHealthBar = new ImageEntity(this, "redhealth.png");
		blueHealthBar = new ImageEntity(this, "bluehealth.png");

		// start off in pause mode
		pauseGame();
	}

	private void resetGame() {
		// save tanks
		AnimatedSprite redTank = (AnimatedSprite) sprites().get(0);
		AnimatedSprite blueTank = (AnimatedSprite) sprites().get(1);

		// wipe sprite list to start over
		sprites().clear();

		// add tanks to sprite list
		redTank.setPosition(new Point2D(SCREENWIDTH * Math.random(),
				SCREENHEIGHT * Math.random()));
		redTank.setAlive(true);
		redTank.setVelocity(new Point2D(0, 0));
		sprites().add(redTank);

		blueTank.setPosition(new Point2D(SCREENWIDTH * Math.random(),
				SCREENHEIGHT * Math.random()));
		blueTank.setAlive(true);
		blueTank.setVelocity(new Point2D(0, 0));
		sprites().add(blueTank);

		// reset variables
		redTank.setScore(0);
		redTank.setHealth(TANK_HEALTH);
		redTank.setState(STATE_NORMAL);
		blueTank.setScore(0);
		blueTank.setHealth(TANK_HEALTH);
		blueTank.setState(STATE_NORMAL);

	}

	/**
	 * Check for input updates every time run thread loops
	 */
	void gameTimedUpdate() {
		checkRedInput();
		if (AI) {
			checkAIInput();
		} else {
			checkBlueInput();
		}

		boolean limit = false;
		if (redTank.score() >= KILLCAP * KILLPOINTS
				|| blueTank.score() >= KILLCAP * KILLPOINTS) {
			limit = true;
		}

		if (!gamePaused() && limit) {
			gameState = GAME_OVER;
			pauseGame();
		}
	}

	/**
	 * Draw background and HUD
	 */
	void gameRefreshScreen() {
		Graphics2D g2d = graphics();

		// draw the background
		g2d.drawImage(background.getImage(), 0, 0, SCREENWIDTH - 1,
				SCREENHEIGHT - 1, this);

		g2d.setFont(new Font("Dialog", Font.PLAIN, 12));
		// if (showBounds) {
		// g2d.setColor(Color.GREEN);
		// g2d.drawString("BOUNDING BOXES", SCREENWIDTH - 150, 10);
		// }
		// if (collisionTesting) {
		// g2d.setColor(Color.GREEN);
		// g2d.drawString("COLLISION TESTING", SCREENWIDTH - 150, 25);
		// }

		g2d.setColor(Color.WHITE);
		g2d.drawString("FPS: " + frameRate(), 5, 10);

		// find info for red tank
		AnimatedSprite redTank = (AnimatedSprite) sprites().get(0);
		// find info for blue tank
		AnimatedSprite blueTank = (AnimatedSprite) sprites().get(1);

		if (gameState == GAME_MENU) {
			g2d.setFont(new Font("Verdana", Font.BOLD, 36));
			g2d.setColor(Color.BLACK);
			printSimpleString("TANK ATTACK", SCREENWIDTH, -2,
					SCREENHEIGHT / 3 - 2);
			g2d.setColor(new Color(200, 30, 30));
			printSimpleString("TANK ATTACK", SCREENWIDTH, 0, SCREENHEIGHT / 3);

			int x = SCREENWIDTH / 4, y = SCREENHEIGHT / 2;
			g2d.setFont(new Font("Times New Roman", Font.ITALIC | Font.BOLD, 30));
			g2d.setColor(Color.GRAY);
			printSimpleString("CONTROLS:", SCREENWIDTH, 0, y);
			g2d.setFont(new Font("Times New Roman", Font.ITALIC | Font.BOLD, 20));
			printSimpleString("END GAME - Escape", SCREENWIDTH, 0, y + 100);
			g2d.setColor(Color.RED);
			printSimpleString("ROTATE - Left/Right Arrows", SCREENWIDTH, x,
					y += 40);
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

			g2d.setFont(new Font("Ariel", Font.BOLD, 24));
			g2d.setColor(Color.WHITE);
			printSimpleString("Press SPACE to start", SCREENWIDTH, 0,
					(int) (0.8 * SCREENHEIGHT));
		}

		else if (gameState == GAME_RUNNING) {
			g2d.setFont(new Font("Verdana", Font.BOLD, 30));
			printSimpleString("Score", SCREENWIDTH, 0, 40);
			g2d.setColor(Color.RED);
			printSimpleString(redTank.score(), SCREENWIDTH, 80, 100);
			g2d.setColor(Color.BLUE);
			printSimpleString(blueTank.score(), SCREENWIDTH, -80, 100);

			g2d.setFont(new Font("Verdana", Font.BOLD, 20));
			g2d.setColor(Color.RED);
			g2d.drawString("HP", SCREENWIDTH - 40, 80);
			// health image is 1 pixel wide
			for (int n = 0; n < 2.5 * (int) redTank.health(); n++) {
				int dx = SCREENWIDTH - 50 - n;
				g2d.drawImage(redHealthBar.getImage(), dx, 60, this);

			}
			g2d.setColor(Color.BLUE);
			g2d.drawString("HP", 10, 80);
			// health image is 1 pixel wide
			for (int n = 0; n < 2.5 * (int) blueTank.health(); n++) {
				int dx = 50 + n;
				g2d.drawImage(blueHealthBar.getImage(), dx, 60, this);

			}
			// g2d.setFont(new Font("Verdana", Font.PLAIN, 12));
			// g2d.setColor(Color.RED);
			// g2d.drawString("STATE: " + redTank.state(), 10, 40);
			// g2d.setColor(Color.BLUE);
			// g2d.drawString("faceAngle: " + blueTank.faceAngle(), 10, 50);

		} else if (gameState == GAME_OVER) {
			g2d.setFont(new Font("Verdana", Font.BOLD, 36));
			g2d.setColor(new Color(200, 30, 30));

			if (redTank.score() > blueTank.score()) {
				printSimpleString("RED WINS!", SCREENWIDTH, 0, SCREENHEIGHT / 2);
			} else if (blueTank.score() > redTank.score()) {
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
	}

	void gameShutdown() {
		// nothing here yet...

		// stop MIDI sequences and the like here if they are added
	}

	public void gameKeyDown(int keyCode) {
		switch (keyCode) {
		// red keys
		case KeyEvent.VK_LEFT:
			redLeft = true;
			break;
		case KeyEvent.VK_RIGHT:
			redRight = true;
			break;
		case KeyEvent.VK_UP:
			redUp = true;
			break;
		case KeyEvent.VK_DOWN:
			redDown = true;
			break;
		case KeyEvent.VK_ENTER:
			redFire = true;
			break;

		// miscellaneous keys
		case KeyEvent.VK_SPACE:
			if (gameState == GAME_MENU || gameState == GAME_OVER) {
				resetGame();
				resumeGame();
				gameState = GAME_RUNNING;
			}
			break;
		case KeyEvent.VK_ESCAPE:
			if (gameState == GAME_RUNNING) {
				pauseGame();
				gameState = GAME_OVER;
			}
			break;

		// case KeyEvent.VK_B:
		// // toggle bounding rectangles
		// showBounds = !showBounds;
		// break;
		// case KeyEvent.VK_C:
		// // toggle collision testing
		// collisionTesting = !collisionTesting;
		// break;
		case KeyEvent.VK_X:
			// toggle AI for blue Tank
			AI = !AI;
			break;
		}
		if (!AI) {
			switch (keyCode) {
			// blue keys
			case KeyEvent.VK_A:
				blueLeft = true;
				break;
			case KeyEvent.VK_D:
				blueRight = true;
				break;
			case KeyEvent.VK_W:
				blueUp = true;
				break;
			case KeyEvent.VK_S:
				blueDown = true;
				break;
			case KeyEvent.VK_CONTROL:
				blueFire = true;
				break;
			}
		}
	}

	public void gameKeyUp(int keyCode) {
		switch (keyCode) {
		// red keys
		case KeyEvent.VK_LEFT:
			redLeft = false;
			break;
		case KeyEvent.VK_RIGHT:
			redRight = false;
			break;
		case KeyEvent.VK_UP:
			redUp = false;
			break;
		case KeyEvent.VK_DOWN:
			redDown = false;
			break;
		case KeyEvent.VK_ENTER:
			redFire = false;
			break;
		}
		if (!AI) {
			switch (keyCode) {
			// blue keys
			case KeyEvent.VK_A:
				blueLeft = false;
				break;
			case KeyEvent.VK_D:
				blueRight = false;
				break;
			case KeyEvent.VK_W:
				blueUp = false;
				break;
			case KeyEvent.VK_S:
				blueDown = false;
				break;
			case KeyEvent.VK_CONTROL:
				blueFire = false;
				break;
			}
		}
	}

	public void gameMouseDown() {
	}

	public void gameMouseUp() {
	}

	public void gameMouseMove() {
	}

	/**
	 * For checking for screen wrapping and updating animations
	 */
	public void spriteUpdate(AnimatedSprite sprite) {
		switch (sprite.spriteType()) {
		case SPRITE_TANK:
			// checkSpriteHealth(sprite);
			wrap(sprite);
			break;
		case SPRITE_SHELL:
			wrap(sprite);
			break;
		case SPRITE_EXPLOSION:
			if (sprite.currentFrame() == sprite.totalFrames() - 1) {
				sprite.setAlive(false);
			}
			break;
		}
	}

	/**
	 * Deals with sprites when their health decreases below zero
	 */
	// public void checkSpriteHealth(AnimatedSprite sprite){
	// if (sprite.health() <= 0) { sprite.setAlive(false); }
	// }

	/**
	 * Provides an opportunity to manipulate the sprite after it's drawn to the
	 * screen - currently used to draw bounding rectangles if showBounds is true
	 */
	public void spriteDraw(AnimatedSprite sprite) {
		if (showBounds) {
			if (sprite.collided())
				sprite.drawBounds(Color.RED);
			else
				sprite.drawBounds(Color.BLUE);
		}
	}

	/**
	 * Provides an opportunity to save a sprite from dying
	 */
	public void spriteDying(AnimatedSprite sprite) {
		// nothing yet
	}

	/**
	 * Deals with collisions
	 */
	public void spriteCollision(AnimatedSprite spr1, AnimatedSprite spr2) {
		// jump out quickly if collisions are off
		if (!collisionTesting)
			return;

		switch (spr1.spriteType()) {
		case SPRITE_TANK:
			// did the tank hit the other tank?
			if (spr2.spriteType() == SPRITE_TANK) {
				spr1.setCollided(true);
				spr2.setCollided(true);

				double diffx = spr1.position().X() - spr2.position().X();
				double diffy = spr1.position().Y() - spr2.position().Y();

				double x = spr1.imageWidth() - diffx;
				double y = spr1.imageHeight() - diffy;

				double a = 0.75 * TANK_SPEED;

				if (x < y) {
					spr1.setPosition(new Point2D(spr1.position().X() + a, spr1
							.position().Y()));
					spr2.setPosition(new Point2D(spr2.position().X() - a, spr2
							.position().Y()));
				} else {
					spr1.setPosition(new Point2D(spr1.position().X(), spr1
							.position().Y() + a));
					spr2.setPosition(new Point2D(spr2.position().X(), spr2
							.position().Y() - a));
				}

				// was the tank hit by a shell?
			} else if (spr2.spriteType() == SPRITE_SHELL) {
				try {
					if (((bullet) spr2).getTankFired() == sprites().indexOf(
							spr1))
						return;
				} catch (Exception e) {
					System.out
							.println("<<Error>>. Best guess: spr2 has been labelled SPRITE_SHELL incorrectly");
				}

				if (spr1.state() == STATE_NORMAL) {
					spr1.setCollided(true);
					spr2.setCollided(true);

					spr2.setAlive(false);
					spr1.changeHealth(SHELL_DAMAGE);

					// kill function assumes only cause of death is bullets
					if (spr1.health() < 0) {
						double x = spr1.position().X();
						double y = spr1.position().Y();
						startBigExplosion(new Point2D(x, y));
						killTank(spr1, ((bullet) spr2).getTankFired());
						// spr1.setState(STATE_EXPLODING);

						x = spr1.position().X();
						y = spr1.position().Y();
						spr1.setPosition(new Point2D(x, y));

						// collisionTimer = System.currentTimeMillis();
						// }
						// }
						// else if (spr1.state() == STATE_EXPLODING) {
						// if (collisionTimer + 3000 <
						// System.currentTimeMillis()) {
						// spr1.setState(STATE_NORMAL);
						// }
						// }
					}
					break;
				}
			}
		}
	}

	/**
	 * Handle input from the red user via the key booleans
	 */
	public void checkRedInput() {
		// the red tank is always the first sprite in the linked list
//		AnimatedSprite redTank = (AnimatedSprite) sprites().get(0);

		if (redLeft) {
			// left arrow rotates tank left 5 degrees
			tankLeft(redTank);

		} else if (redRight) {
			// right arrow rotates tank right 5 degrees
			tankRight(redTank);
		}

		if (redUp) {
			// up arrow gives tank a set velocity
			tankUp(redTank);

			redTank.animate();
		}

		else if (redDown) {
			// down arrow gives tank a set velocity
			tankDown(redTank);

			redTank.animate();
		}

		else if (!redUp || !redDown) {
			// set velocity to zero if up/down keys aren't being pressed
			tankStop(redTank);
		}

		if (redFire) {
			// fire shell from the tank if reloaded
			if (System.currentTimeMillis() > redstartTime + 1000 * SHELL_RELOAD) {
				fireShell((AnimatedSprite) sprites().get(0));
				redstartTime = System.currentTimeMillis();
			}
		}
	}

	/**
	 * Handle input from the blue user via the key booleans
	 */
	public void checkBlueInput() {
		// the blue tank is always the second sprite in the linked list

		if (blueLeft) {
			// left arrow rotates tank left 5 degrees
			tankLeft(blueTank);

		} else if (blueRight) {
			// right arrow rotates tank right 5 degrees
			tankRight(blueTank);
		}

		if (blueUp) {
			// up arrow gives tank a set velocity
			tankUp(blueTank);

			blueTank.animate();
		}

		else if (blueDown) {
			// down arrow gives tank a set velocity
			tankDown(blueTank);

			blueTank.animate();
		}

		else if (!blueUp || !blueDown) {
			// set velocity to zero if up/down keys aren't being pressed
			tankStop(blueTank);
		}

		if (blueFire) {
			// fire shell from the tank if reloaded
			if (System.currentTimeMillis() > bluestartTime + 1000
					* SHELL_RELOAD) {
				fireShell((AnimatedSprite) sprites().get(1));
				bluestartTime = System.currentTimeMillis();
			}
		}

	}

	public void checkAIInput() {
		// the red tank is always the first sprite in the linked list
		// the blue tank is always the second sprite in the linked list

		AI dave = new AI();
		Vector2D g = dave.findAimDirection(blueTank, redTank,
				(double) SHELL_SPEED);

		// System.out.println("x: " + g.x());
		// System.out.println("y: " + g.y());

		int i = dave.moveToAimDirection(blueTank, g, TANK_ROTATION);

		switch (i) {
		case 0:
			tankLeft(blueTank);
			break;
		case 1:
			tankRight(blueTank);
			break;
		case 2:
			// fire shell from the tank if reloaded
			if (System.currentTimeMillis() > bluestartTime + 1000
					* SHELL_RELOAD) {
				fireShell(blueTank);
				bluestartTime = System.currentTimeMillis();
			}
			break;
		case 3:
			System.out.println("Houston we have a problem");
			break;
		}
	}

	public void tankLeft(AnimatedSprite tank) {
		// left arrow rotates tank left 5 degrees
		tank.setFaceAngle(tank.faceAngle() - TANK_ROTATION);
		if (tank.faceAngle() < 0)
			tank.setFaceAngle(360 - TANK_ROTATION);
	}

	public void tankRight(AnimatedSprite tank) {
		// right arrow rotates tank right 5 degrees
		tank.setFaceAngle(tank.faceAngle() + TANK_ROTATION);
		if (tank.faceAngle() > 360)
			tank.setFaceAngle(TANK_ROTATION);
	}

	public void tankUp(AnimatedSprite tank) {
		// up arrow gives tank a set velocity
		tank.setMoveAngle(tank.faceAngle() - 90);

		// calculate the X and Y velocity based on angle
		double velx = calcAngleMoveX(tank.moveAngle()) * TANK_SPEED;
		double vely = calcAngleMoveY(tank.moveAngle()) * TANK_SPEED;

		tank.setVelocity(new Point2D(velx, vely));
	}

	public void tankDown(AnimatedSprite tank) {
		// down arrow gives tank a set velocity
		tank.setMoveAngle(tank.faceAngle() - 90);

		// calculate the X and Y velocity based on angle
		double velx = -calcAngleMoveX(tank.moveAngle()) * TANK_SPEED;
		double vely = -calcAngleMoveY(tank.moveAngle()) * TANK_SPEED;

		tank.setVelocity(new Point2D(velx, vely));
	}

	public void tankStop(AnimatedSprite tank) {
		// set velocity to zero if up/down keys aren't being pressed
		tank.setVelocity(new Point2D(0, 0));
	}

	/**
	 * Creates a shell sprite and fires it from the tank
	 */
	public void fireShell(AnimatedSprite tank) {
		// create the new shell sprite
		bullet shell = new bullet(this, graphics());
		shell.setImage(shellImage.getImage());
		shell.setFrameWidth(shellImage.width());
		shell.setFrameHeight(shellImage.height());
		shell.setSpriteType(SPRITE_SHELL);
		shell.setAlive(true);
		shell.setLifespan(200);
		shell.setFaceAngle(tank.faceAngle());
		shell.setMoveAngle(tank.faceAngle() - 90);

		// set the shell's starting position
		double x = tank.center().X() - shell.imageWidth() / 2;
		double y = tank.center().Y() - shell.imageHeight() / 2;
		shell.setPosition(new Point2D(x, y));

		// set the shell's velocity
		double angle = shell.moveAngle();
		double svx = calcAngleMoveX(angle) * SHELL_SPEED;
		double svy = calcAngleMoveY(angle) * SHELL_SPEED;
		shell.setVelocity(new Point2D(svx, svy));

		// record which tank fired the shell
		shell.setTankFired(tank, sprites());

		// add shell to the sprite list
		sprites().add(shell);
	}

	/**
	 * In this case, the method prevents sprites from wrapping around the screen
	 */
	public void wrap(AnimatedSprite sprite) {
		// create some shortcut variables
		int w = sprite.frameWidth() - 1;
		int h = sprite.frameHeight() - 1;

		// wrap the sprite around the screen edges
		if (sprite.position().X() < 0)
			if (sprite.spriteType() == SPRITE_SHELL) {
				sprite.setAlive(false);
			} else {
				sprite.position().setX(0);
			}
		else if (sprite.position().X() > SCREENWIDTH - w)
			if (sprite.spriteType() == SPRITE_SHELL) {
				sprite.setAlive(false);
			} else {
				sprite.position().setX(SCREENWIDTH - w);
			}
		if (sprite.position().Y() < 0)
			if (sprite.spriteType() == SPRITE_SHELL) {
				sprite.setAlive(false);
			} else {
				sprite.position().setY(0);
			}
		else if (sprite.position().Y() > SCREENHEIGHT - h)
			if (sprite.spriteType() == SPRITE_SHELL) {
				sprite.setAlive(false);
			} else {
				sprite.position().setY(SCREENHEIGHT - h);
			}
	}

	/**
	 * Helper function from coderanch to center text
	 * 
	 * @param s
	 *            The actual string to be drawn
	 * @param width
	 *            Width to center within (i.e. the Screen width)
	 * @param XPos
	 *            x coordinate offset
	 * @param YPos
	 *            y coordinate offset
	 */
	public void printSimpleString(String s, int width, int XPos, int YPos) {
		Graphics2D g2d = graphics();
		int stringLen = (int) g2d.getFontMetrics().getStringBounds(s, g2d)
				.getWidth();
		int start = width / 2 - stringLen / 2;
		g2d.drawString(s, start + XPos, YPos);
	}

	/**
	 * Overload helper function from coderanch to center integers as strings
	 * 
	 * @param s
	 *            The actual string to be drawn
	 * @param width
	 *            Width to center within (i.e. the Screen width)
	 * @param XPos
	 *            x coordinate offset
	 * @param YPos
	 *            y coordinate offset
	 */
	public void printSimpleString(int _s, int width, int XPos, int YPos) {
		Graphics2D g2d = graphics();
		String s = String.valueOf(_s);
		int stringLen = (int) g2d.getFontMetrics().getStringBounds(s, g2d)
				.getWidth();
		int start = width / 2 - stringLen / 2;
		g2d.drawString(s, start + XPos, YPos);
	}

	/**
	 * Function to handle a sprite's death -Increases score of killer -Restores
	 * health of killed sprite -Resets dead sprites position
	 * 
	 * @param sprite
	 *            The killed sprite
	 * @param bulletSource
	 *            The index number of the killer sprite
	 */
	public void killTank(AnimatedSprite loser, int bulletSource) {
		AnimatedSprite pro = (AnimatedSprite) sprites().get(bulletSource);
		pro.changeScore(KILLPOINTS);
		loser.setHealth(TANK_HEALTH);
		loser.setPosition(new Point2D(SCREENWIDTH * Math.random(), SCREENHEIGHT
				* Math.random()));
		loser.setFaceAngle(0);
	}

	/*****************************************************
	 * launch a big explosion at the passed location
	 *****************************************************/
	public void startBigExplosion(Point2D point) {
		// create a new explosion at the passed location
		AnimatedSprite expl = new AnimatedSprite(this, graphics());
		expl.setSpriteType(SPRITE_EXPLOSION);
		expl.setAlive(true);
		expl.setAnimImage(explosions[0].getImage());
		expl.setTotalFrames(16);
		expl.setColumns(4);
		expl.setFrameWidth(96);
		expl.setFrameHeight(96);
		expl.setFrameDelay(2);
		expl.setPosition(point);

		// add the new explosion to the sprite list
		sprites().add(expl);
	}

}
