package tankAttack;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.JFrame;

import math.geom2d.Point2D;

public class TankAttack extends Game {
	// constants

	private static final long serialVersionUID = 1L;
	// static because they're passed to a constructor
	static int FRAMERATE = 60;


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
	final int MAIN_MENU = 0;
	final int PLAYER_VS_AI = 1;
	final int PLAYER_VS_PLAYER = 2;
	final int GAME_OVER = 4;

	// various toggles
	static boolean showBounds = false;
	static boolean collisionTesting = true;
	boolean AI = false;

	// define the images used in the game
	ImageEntity background;
	ImageEntity shellImage;
	ImageEntity[] explosions;

	// create a random number generator
	Random rand = new Random();

	// used to make tank temporarily invulnerable
	long collisionTimer = 0;

	// some key input tracking variables
	boolean redLeft, redRight, redUp, redDown, redFire, blueLeft, blueRight,
			blueUp, blueDown, blueFire, keyB, keyC;

	public TankAttack(JFrame f, String title) {

		// call base Game class' constructor
		super(f, FRAMERATE, title);
		gameState = MAIN_MENU;
	}

	/**
	 * Load all images and spawn tanks
	 */
	void gameStartup() {
		
		main = new MainMenu(this);
		controls = new ControlsMenu(this);
		pvai = new Solo(this);
		pvp = new Versus(this);
		gOver = new GameOver(this);
		bug = new DebugScreen(this);
		
		explosions = new ImageEntity[1];
		explosions[0] = new ImageEntity(this, "explosion.png");
		
		// main.makeCurrent();
		
		pvp.makeCurrent();
		// For creating the debug screen
		
		// start off in pause mode
		pauseGame();
	}


	/**
	 * Draw background and HUD
	 */
	void gameRefreshScreen() {
		Graphics2D g2d = graphics();
		// draw the background
		g2d.setColor(Color.LIGHT_GRAY);
		g2d.fill(new Rectangle2D.Double(0, 0, SCREENWIDTH, SCREENHEIGHT));

		// if (showBounds) {
		// g2d.setColor(Color.GREEN);
		// g2d.drawString("BOUNDING BOXES", SCREENWIDTH - 150, 10);
		// }
		// if (collisionTesting) {
		// g2d.setColor(Color.GREEN);
		// g2d.drawString("COLLISION TESTING", SCREENWIDTH - 150, 25);
		
		screen.update();
	}

	void gameShutdown() {
		// nothing here yet...

		// stop MIDI sequences and the like here if they are added
	}

	public void gameKeyDown(int keyCode) {
		if (screen != null) {
			screen.keyPressed(keyCode);
		}
		// miscellaneous keys

		// case KeyEvent.VK_B:
		// // toggle bounding rectangles
		// showBounds = !showBounds;
		// break;
		// case KeyEvent.VK_C:
		// // toggle collision testing
		// collisionTesting = !collisionTesting;
		// break;
//		case KeyEvent.VK_X:
//			// toggle AI for blue Tank
//			AI = !AI;
//			break;
//		}
//		if (!AI) {
//			switch (keyCode) {
//			// blue keys
//			case KeyEvent.VK_A:
//				blueLeft = true;
//				break;
//			case KeyEvent.VK_D:
//				blueRight = true;
//				break;
//			case KeyEvent.VK_W:
//				blueUp = true;
//				break;
//			case KeyEvent.VK_S:
//				blueDown = true;
//				break;
//			case KeyEvent.VK_CONTROL:
//				blueFire = true;
//				break;
//			}
	}

	public void gameKeyUp(int keyCode) {
		if (screen != null) {
			screen.keyReleased(keyCode);
		}
//		switch (keyCode) {
//		// red keys
//		case KeyEvent.VK_LEFT:
//			redLeft = false;
//			break;
//		case KeyEvent.VK_RIGHT:
//			redRight = false;
//			break;
//		case KeyEvent.VK_UP:
//			redUp = false;
//			break;
//		case KeyEvent.VK_DOWN:
//			redDown = false;
//			break;
//		case KeyEvent.VK_ENTER:
//			redFire = false;
//			break;
//		}
//		if (!AI) {
//			switch (keyCode) {
//			// blue keys
//			case KeyEvent.VK_A:
//				blueLeft = false;
//				break;
//			case KeyEvent.VK_D:
//				blueRight = false;
//				break;
//			case KeyEvent.VK_W:
//				blueUp = false;
//				break;
//			case KeyEvent.VK_S:
//				blueDown = false;
//				break;
//			case KeyEvent.VK_CONTROL:
//				blueFire = false;
//				break;
//			}
//		}
	}

	public void gameMouseDown() {
	}

	public void gameMouseUp() {
	}

	public void gameMouseMove() {
	}

	/**
	 * Deals with collisions
	 */
//	public void spriteCollision(AnimatedSprite spr1, AnimatedSprite spr2) {
//		
//		// jump out quickly if collisions are off
//		if (!collisionTesting)
//			return;
//		
//		switch (spr1.spriteType()) {
//		case SPRITE_TANK:
//			// did the tank hit the other tank?
//			if (spr2.spriteType() == SPRITE_TANK) {
//				spr1.setCollided(true);
//				spr2.setCollided(true);
//
//				double diffx = spr1.position().x() - spr2.position().x();
//				double diffy = spr1.position().y() - spr2.position().y();
//
//				double x = spr1.imageWidth() - diffx;
//				double y = spr1.imageHeight() - diffy;
//
//				double a = 0.75 * TANK_SPEED;
//
//				if (x < y) {
//					spr1.setPosition(new Point2D(spr1.position().x() + a, spr1
//							.position().y()));
//					spr2.setPosition(new Point2D(spr2.position().x() - a, spr2
//							.position().y()));
//				} else {
//					spr1.setPosition(new Point2D(spr1.position().x(), spr1
//							.position().y() + a));
//					spr2.setPosition(new Point2D(spr2.position().x(), spr2
//							.position().y() - a));
//				}
//
//				// was the tank hit by a shell?
//			} else if (spr2.spriteType() == SPRITE_SHELL) {
//				try {
//					if (((Bullet) spr2).getTankFired() == sprites().indexOf(
//							spr1))
//						return;
//				} catch (Exception e) {
//					System.out
//							.println("<<Error>>. Best guess: spr2 has been labelled SPRITE_SHELL incorrectly");
//				}
//
//				if (spr1.state() == STATE_NORMAL) {
//					spr1.setCollided(true);
//					spr2.setCollided(true);
//
//					spr2.setAlive(false);
//					spr1.changeHealth(SHELL_DAMAGE);
//
//					// kill function assumes only cause of death is bullets
//					if (spr1.health() <= 0) {
//						double x = spr1.position().x();
//						double y = spr1.position().y();
//						startBigExplosion(new Point2D(x, y));
//						killTank(spr1, ((Bullet) spr2).getTankFired());
//						// spr1.setState(STATE_EXPLODING);
//
//						x = spr1.position().x();
//						y = spr1.position().y();
//						spr1.setPosition(new Point2D(x, y));
//
//						// collisionTimer = System.currentTimeMillis();
//						// }
//						// }
//						// else if (spr1.state() == STATE_EXPLODING) {
//						// if (collisionTimer + 3000 <
//						// System.currentTimeMillis()) {
//						// spr1.setState(STATE_NORMAL);
//						// }
//						// }
//					}
//					break;
//				}
//			}
//		}
//	}



//	public void checkAIInput() {
//		// the red tank is always the first sprite in the linked list
//		// the blue tank is always the second sprite in the linked list
//
//		AI dave = new AI();
//		Vector2D g = dave.findAimDirection(blueTank, redTank,
//				(double) SHELL_SPEED);
//
//		// System.out.println("x: " + g.x());
//		// System.out.println("y: " + g.y());
//
//		int i = dave.moveToAimDirection(blueTank, g, TANK_ROTATION);
//
//		switch (i) {
//		case 0:
//			tankLeft(blueTank);
//			break;
//		case 1:
//			tankRight(blueTank);
//			break;
//		case 2:
//			// fire shell from the tank if reloaded
//			if (System.currentTimeMillis() > bluestartTime + 1000
//					* SHELL_RELOAD) {
//				fireShell(blueTank);
//				bluestartTime = System.currentTimeMillis();
//			}
//			break;
//		case 3:
//			System.out.println("Houston we have a problem");
//			break;
//		}
//	}

	/**
	 * In this case, the method prevents sprites from wrapping around the screen
	 */
//	public void wrap(AnimatedSprite sprite) {
//		// create some shortcut variables
//		int w = sprite.frameWidth() - 1;
//		int h = sprite.frameHeight() - 1;
//
//		// wrap the sprite around the screen edges
//		if (sprite.position().x() < 0)
//			if (sprite.spriteType() == SPRITE_SHELL) {
//				sprite.setAlive(false);
//			} else {
//				sprite.position()
//			}
//		else if (sprite.position().x() > SCREENWIDTH - w)
//			if (sprite.spriteType() == SPRITE_SHELL) {
//				sprite.setAlive(false);
//			} else {
//				sprite.position().setX(SCREENWIDTH - w);
//			}
//		if (sprite.position().y() < 0)
//			if (sprite.spriteType() == SPRITE_SHELL) {
//				sprite.setAlive(false);
//			} else {
//				sprite.position().setY(0);
//			}
//		else if (sprite.position().y() > SCREENHEIGHT - h)
//			if (sprite.spriteType() == SPRITE_SHELL) {
//				sprite.setAlive(false);
//			} else {
//				sprite.position().setY(SCREENHEIGHT - h);
//			}
//	}

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

	@Override
	void gameTimedUpdate() {
		// TODO Auto-generated method stub
		
	}

}
