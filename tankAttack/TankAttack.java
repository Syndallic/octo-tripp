package tankAttack;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;

import gameEngine.AnimatedSprite;
import gameEngine.EnginePoint2D;
import gameEngine.Game;
import gameEngine.ImageEntity;
import screens.ControlsMenu;
import screens.GameOver;
import screens.MainMenu;
import screens.Solo;
import screens.Versus;

public class TankAttack extends Game {
	public MainMenu main;
	public ControlsMenu controls;
	public GameOver gameover;
	public Solo pvai;
	public Versus pvp;	
	
	// constants

	// static because they're passed to a constructor
	private static int FRAMERATE = 60;

	// sprite state values
	final int STATE_NORMAL = 0;
	final int STATE_COLLIDED = 1;
	final int STATE_EXPLODING = 2;

	// sprite types
	final int SPRITE_TANK = 1; //can we not test types instead?
	final int SPRITE_SHELL = 100;
	final int SPRITE_EXPLOSION = 200;

	// game states
	public final static int MAIN_MENU = 0;
	public final static int PLAYER_VS_AI = 1;
	public final static int PLAYER_VS_PLAYER = 2;
	public final static int CONTROLS_MENU = 3;
	public final static int GAME_OVER = 4; //what the heck is 5??
	public final static int RESET = 6;

	// various toggles
	private static boolean showBounds = false;
	private static boolean collisionTesting = true;

	// define the images used in the game
	ImageEntity background;
	ImageEntity shellImage;
	ImageEntity[] explosions;

	// create a random number generator
	Random rand = new Random();

	// used to make tank temporarily invulnerable
	long collisionTimer = 0;

	/**
	 * Begin at main menu
	 * 
	 * @param f
	 * @param title
	 */
	public TankAttack(JFrame f, String title) {
		super(f, FRAMERATE, title);
		gameState = MAIN_MENU;
	}

	public static void toggleShowBounds(){
		showBounds = !showBounds;
	}
	
	public static void toggleCollisionTesting(){
		collisionTesting = !collisionTesting;
	}
	
	/**
	 * Load all images and begin at main menu
	 */
	protected void gameStartup() {

		main = new MainMenu(this, graphics());
		controls = new ControlsMenu(this, graphics());
		pvai = new Solo(this, graphics());
		pvp = new Versus(this, graphics());
		gameover = new GameOver(this, graphics());

		explosions = new ImageEntity[1];
		explosions[0] = new ImageEntity(this, "explosion.png");

		main.makeCurrent();
		// start off in pause mode
		pauseGame();
	}
	


	/**
	 * Check for input updates every time run thread loops
	 */
	protected void gameTimedUpdate() {

	}

	/**
	 * Draw background and HUD
	 */
	protected void gameRefreshScreen() {
		Graphics2D g2d = graphics();

		// draw the background
		g2d.setColor(Color.LIGHT_GRAY);
		g2d.fill(new Rectangle2D.Double(0, 0, getSCREENWIDTH(), getSCREENHEIGHT()));

		if (showBounds) {
			g2d.setFont(new Font("Dialog", Font.PLAIN, 12));
			g2d.setColor(Color.GREEN);
			g2d.drawString("BOUNDING BOXES", getSCREENWIDTH() - 150, 10);
		}
		if (collisionTesting) {
			g2d.setFont(new Font("Dialog", Font.PLAIN, 12));
			g2d.setColor(Color.GREEN);
			g2d.drawString("COLLISION TESTING", getSCREENWIDTH() - 150, 25);
		}

		screen.update();
	}

	protected void gameShutdown() {
		// nothing here yet...

		// stop MIDI sequences and the like here if they are added
	}

	/**
	 * Passes keyCode to the current screen
	 */
	protected void gameKeyDown(int keyCode) {
		if (screen != null) {
			screen.keyPressed(keyCode);
		}
	}

	/**
	 * Passes keyCode to the current screen
	 */
	protected void gameKeyUp(int keyCode) {
		if (screen != null) {
			screen.keyReleased(keyCode);
		}
	}

	protected void gameMouseDown() {
	}

	protected void gameMouseUp() {
	}

	protected void gameMouseMove() {
	}

	/**
	 * For checking for screen wrapping and updating animations
	 */
	protected void spriteUpdate(AnimatedSprite sprite) {
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
	 * Provides an opportunity to manipulate the sprite after it's drawn to the screen - currently used to draw bounding
	 * rectangles if showBounds is true
	 */
	protected void spriteDraw(AnimatedSprite sprite) {
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
	protected void spriteDying(AnimatedSprite sprite) {
		// nothing yet
	}

	/**
	 * Deals with collisions
	 */
	protected void spriteCollision(AnimatedSprite spr1, AnimatedSprite spr2) {
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

				double a = 0.75 * Tank.TANK_SPEED;

				if (x < y) {
					spr1.setPosition(new EnginePoint2D(spr1.position().X() + a, spr1.position().Y()));
					spr2.setPosition(new EnginePoint2D(spr2.position().X() - a, spr2.position().Y()));
				} else {
					spr1.setPosition(new EnginePoint2D(spr1.position().X(), spr1.position().Y() + a));
					spr2.setPosition(new EnginePoint2D(spr2.position().X(), spr2.position().Y() - a));
				}

				// was the tank hit by a shell?
			} else if (spr2.spriteType() == SPRITE_SHELL) {
				try {
					if (((Bullet) spr2).getTankFired() == sprites().indexOf(spr1))
						return;
				} catch (Exception e) {
					System.out.println("<<Error>>. Best guess: spr2 has been labelled SPRITE_SHELL incorrectly");
				}

				if (spr1.state() == STATE_NORMAL) {
					spr1.setCollided(true);
					spr2.setCollided(true);

					spr2.setAlive(false);
					spr1.changeHealth(Shell.SHELL_DAMAGE);

					// kill function assumes only cause of death is bullets
					if (spr1.health() <= 0) {
						double x = spr1.position().X();
						double y = spr1.position().Y();
						startBigExplosion(new EnginePoint2D(x, y));
						killTank((Tank) spr1, ((Bullet) spr2).getTankFired());
						// spr1.setState(STATE_EXPLODING);

						x = spr1.position().X();
						y = spr1.position().Y();
						spr1.setPosition(new EnginePoint2D(x, y));

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
		else if (sprite.position().X() > getSCREENWIDTH() - w)
			if (sprite.spriteType() == SPRITE_SHELL) {
				sprite.setAlive(false);
			} else {
				sprite.position().setX(getSCREENWIDTH() - w);
			}
		if (sprite.position().Y() < 0)
			if (sprite.spriteType() == SPRITE_SHELL) {
				sprite.setAlive(false);
			} else {
				sprite.position().setY(0);
			}
		else if (sprite.position().Y() > getSCREENHEIGHT() - h)
			if (sprite.spriteType() == SPRITE_SHELL) {
				sprite.setAlive(false);
			} else {
				sprite.position().setY(getSCREENHEIGHT() - h);
			}
	}

	/**
	 * Function to handle a sprite's death -Increases score of killer -Restores health of killed sprite -Resets dead
	 * sprites position
	 * 
	 * @param sprite
	 *            The killed sprite
	 * @param bulletSource
	 *            The index number of the killer sprite
	 */
	public void killTank(Tank loser, int bulletSource) {
		AnimatedSprite pro = (AnimatedSprite) sprites().get(bulletSource);
		pro.changeScore(100); //should be KILLPOINTS
		loser.respawn();
	}

	/*****************************************************
	 * launch a big explosion at the passed location
	 *****************************************************/
	public void startBigExplosion(EnginePoint2D point) {
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
	
	public ArrayList<Bullet> getFiredBullets(Tank tank){
		ArrayList<Bullet> fired = new ArrayList<Bullet>();
		int index = sprites().indexOf(tank);
		
		for (AnimatedSprite spr : sprites()){
			if (spr instanceof Bullet){
				if (((Bullet) spr).getTankFired() == index){
					fired.add((Bullet) spr);
				}
			}
		}
		return fired;
	}

}
