package tankAttack;

/*****************************************************
 * Beginning Java Game Programming, 3rd Edition
 * by Jonathan S. Harbour
 * Applet Game Engine class
 *****************************************************/

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import math.geom2d.Vector2D;
import tankAttack.collision.DetectCollision;

abstract class Game extends JPanel implements Runnable, KeyListener,
		MouseListener, MouseMotionListener {

	// the main game loop thread
	private Thread gameloop;

	private JFrame f;
	private String title = "";

	static int SCREENWIDTH = 1200;
	static int SCREENHEIGHT = 800;
	// internal list of sprites
	ArrayList entityList;

	public MainMenu main;
	public ControlsMenu controls;
	public GameOver gOver;
	public Solo pvai;
	public Versus pvp;
	public Screen screen;
	public DebugScreen bug;

	public void addSprite(AnimatedSprite e) {
		entityList.add(e);
	}

	public ArrayList sprites() {
		return entityList;
	}

	// screen and double buffer related variables
	private BufferedImage backbuffer;
	private Graphics2D g2d;
	private int screenWidth, screenHeight;

	// keep track of mouse position and buttons
	private Point2D mousePos = new Point2D(0, 0);
	private boolean mouseButtons[] = new boolean[4];

	// frame rate counters and other timing variables
	private int _frameCount = 0;
	private int _frameRate = 0;
	private long startTime = System.currentTimeMillis();

	// game pause state
	private boolean _gamePaused = false;
	int gameState;

	public boolean gamePaused() {
		return _gamePaused;
	}

	public void pauseGame() {
		_gamePaused = true;
	}

	public void resumeGame() {
		_gamePaused = false;
	}

	// declare the game event methods that sub-class must implement
	abstract void gameStartup();

	abstract void gameTimedUpdate();

	abstract void gameRefreshScreen();

	abstract void gameShutdown();

	abstract void gameKeyDown(int keyCode);

	abstract void gameKeyUp(int keyCode);

	abstract void gameMouseDown();

	abstract void gameMouseUp();

	abstract void gameMouseMove();

	abstract void spriteUpdate(AnimatedSprite sprite);

	abstract void spriteDraw(AnimatedSprite sprite);

	abstract void spriteDying(AnimatedSprite sprite);

	abstract void spriteCollision(AnimatedSprite spr1, AnimatedSprite spr2);

	/*****************************************************
	 * constructor
	 *****************************************************/
	public Game(JFrame f, int frameRate, String title) {
		this.f = f;

		f.addKeyListener(this);
		f.addMouseListener(this);
		f.addMouseMotionListener(this);

		screenWidth = f.getWidth();
		screenHeight = f.getHeight();

		this.title = title;

		entityList = new ArrayList<AnimatedSprite>();

		init();
		start();
	}

	public void setGameState(int state) {
		gameState = state;
	}

	// return g2d object so sub-class can draw things
	public Graphics2D graphics() {
		return g2d;
	}

	// current frame rate
	public int frameRate() {
		return _frameRate;
	}

	// mouse buttons and movement
	public boolean mouseButton(int btn) {
		return mouseButtons[btn];
	}

	public Point2D mousePosition() {
		return mousePos;
	}

	/*****************************************************
	 * applet init event method
	 *****************************************************/
	public void init() {
		// create the back buffer and drawing surface
		backbuffer = new BufferedImage(screenWidth, screenHeight,
				BufferedImage.TYPE_INT_RGB);
		g2d = backbuffer.createGraphics();

		gameStartup();
	}

	/*****************************************************
	 * applet update event method
	 *****************************************************/
	public void update(Graphics g) {
		// calculate frame rate
		_frameCount++;
		if (System.currentTimeMillis() > startTime + 1000) {
			startTime = System.currentTimeMillis();
			_frameRate = _frameCount;
			_frameCount = 0;

			// once every second all dead sprites are deleted
			purgeSprites();
		}
		// draw the internal list of sprites
		gameRefreshScreen();

		if (!gamePaused()) {
			updateSprites();
			testCollisions();
		}

		paint(g);
	}

	public void render() {

		// refresh the screen
		repaint();
	}

	/*****************************************************
	 * applet window paint event method
	 *****************************************************/
	public void paint(Graphics g) {
		g.drawImage(backbuffer, 0, 0, this);
	}

	/*****************************************************
	 * thread start event - start the game loop running
	 *****************************************************/
	public void start() {
		gameloop = new Thread(this);
		gameloop.start();
	}

	/*****************************************************
	 * thread run event (game loop)
	 *****************************************************/
	public void run() {
		Thread t = Thread.currentThread();
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();

		final double ns = 1000000000.0 / 60.0;

		double delta = 0;

		int frames = 0;
		int updates = 0;

		requestFocus();

		// Game loop
		while (t == gameloop) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;

			while (delta >= 1) {
				update(g2d);
				render();
				updates++;
				delta--;
			}

			frames++;

			if (System.currentTimeMillis() - timer > 500) {
				timer += 1000;
				f.setTitle(title + " | " + updates + " fps");
				// f.setTitle(title + "  |  " + updates + " ups, " + frames +
				// " fps");
				updates = 0;
				frames = 0;
			}
		}
		stop();
	}

	/*****************************************************
	 * thread stop event
	 *****************************************************/
	public void stop() {
		// kill the game loop
		gameloop = null;
		f.dispatchEvent(new WindowEvent(f, WindowEvent.WINDOW_CLOSING));
		// this method implemented by sub-class
		gameShutdown();
	}

	/*****************************************************
	 * key listener events
	 *****************************************************/
	public void keyTyped(KeyEvent k) {
	}

	public void keyPressed(KeyEvent k) {
		gameKeyDown(k.getKeyCode());
	}

	public void keyReleased(KeyEvent k) {
		gameKeyUp(k.getKeyCode());
	}

	/*****************************************************
	 * checkButtons stores the state of the mouse buttons
	 *****************************************************/
	private void checkButtons(MouseEvent e) {
		switch (e.getButton()) {
		case MouseEvent.BUTTON1:
			mouseButtons[1] = true;
			mouseButtons[2] = false;
			mouseButtons[3] = false;
			break;
		case MouseEvent.BUTTON2:
			mouseButtons[1] = false;
			mouseButtons[2] = true;
			mouseButtons[3] = false;
			break;
		case MouseEvent.BUTTON3:
			mouseButtons[1] = false;
			mouseButtons[2] = false;
			mouseButtons[3] = true;
			break;
		}
	}

	/*****************************************************
	 * mouse listener events
	 *****************************************************/
	public void mousePressed(MouseEvent e) {
		checkButtons(e);
		mousePos.setX(e.getX());
		mousePos.setY(e.getY());
		gameMouseDown();
	}

	public void mouseReleased(MouseEvent e) {
		checkButtons(e);
		mousePos.setX(e.getX());
		mousePos.setY(e.getY());
		gameMouseUp();
	}

	public void mouseMoved(MouseEvent e) {
		checkButtons(e);
		mousePos.setX(e.getX());
		mousePos.setY(e.getY());
		gameMouseMove();
	}

	public void mouseDragged(MouseEvent e) {
		checkButtons(e);
		mousePos.setX(e.getX());
		mousePos.setY(e.getY());
		gameMouseDown();
		gameMouseMove();
	}

	public void mouseEntered(MouseEvent e) {
		mousePos.setX(e.getX());
		mousePos.setY(e.getY());
		gameMouseMove();
	}

	public void mouseExited(MouseEvent e) {
		mousePos.setX(e.getX());
		mousePos.setY(e.getY());
		gameMouseMove();
	}

	// this event is not needed
	public void mouseClicked(MouseEvent e) {
	}

	/*****************************************************
	 * X and Y velocity calculation functions
	 *****************************************************/
	protected double calcAngleMoveX(double angle) {
		return (double) (Math.cos(angle * Math.PI / 180));
	}

	protected double calcAngleMoveY(double angle) {
		return (double) (Math.sin(angle * Math.PI / 180));
	}

	/*****************************************************
	 * update the sprite list from the game loop thread
	 *****************************************************/
	protected void updateSprites() {
		for (int n = 0; n < entityList.size(); n++) {
			AnimatedSprite spr = (AnimatedSprite) entityList.get(n);
			if (spr.alive()) {
				spr.updatePosition();
				spr.updateRotation();
				spr.updateAnimation();
				spriteUpdate(spr);
				spr.updateLifetime();
				if (!spr.alive()) {
					spriteDying(spr);
				}
			}
		}
	}

	/*****************************************************
	 * perform collision testing of all active sprites
	 *****************************************************/
	protected void testCollisions() {
		// iterate through the sprite list, test each sprite against
		// every other sprite in the list
		for (int first = 0; first < entityList.size(); first++) {

			for (int second = 1; second < entityList.size() + 1 - first; second++) {

				Vector2D trans = DetectCollision.findTranslation(((AnimatedSprite) entityList
						.get(first)).getBindingBox(),
						((AnimatedSprite) entityList.get(second))
								.getBindingBox());
			}

			// get the first sprite to test for collision
			// AnimatedSprite spr1 = (AnimatedSprite) entityList.get(first);
			// if (spr1.alive()) {
			//
			// // look through all sprites again for collisions
			// for (int second = 0; second < entityList.size(); second++) {
			//
			// // make sure this isn't the same sprite
			// if (first != second) {
			//
			// // get the second sprite to test for collision
			// AnimatedSprite spr2 = (AnimatedSprite) entityList
			// .get(second);
			// if (spr2.alive()) {
			// if (spr2.collidesWith(spr1)) {
			// spriteCollision(spr1, spr2);
			// break;
			// } else
			// spr1.setCollided(false);
			//
			// }
			// }
			// }
			// }
		}
	}

	/*****************************************************
	 * draw all active sprites in the sprite list sprites lower in the list are
	 * drawn on top
	 *****************************************************/
	protected void drawSprites() {
		// draw sprites in reverse order (reverse priority)
		for (int n = 0; n < entityList.size(); n++) {
			AnimatedSprite spr = (AnimatedSprite) entityList.get(n);
			if (spr.alive()) {
				spr.updateFrame();
				spr.transform();
				spr.draw();
				spriteDraw(spr);
			}
		}
	}

	/*****************************************************
	 * once every second during the frame update, this method is called to
	 * remove all dead sprites from the linked list
	 *****************************************************/
	private void purgeSprites() {
		for (int n = 0; n < entityList.size(); n++) {
			AnimatedSprite spr = (AnimatedSprite) entityList.get(n);
			if (!spr.alive()) {
				entityList.remove(n);
			}
		}
	}

}
