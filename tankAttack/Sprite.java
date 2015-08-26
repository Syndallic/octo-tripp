package tankAttack;

import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.JPanel;

import math.geom2d.Point2D;
import math.geom2d.Vector2D;
import tankAttack.collision.RigidShape;

public class Sprite extends Object {
	private ImageEntity entity;
	protected Point2D pos;
	protected Point2D vel;
	protected double rotRate;
	protected int currentState;
	protected int sprType;
	protected boolean _collided;
	protected int _lifespan, _lifeage;
	protected int _health;
	protected int _score;
	protected RigidShape r;
	protected Game g;

	// constructor
	Sprite(Game a, Graphics2D g2d) {
		g = a;
		entity = new ImageEntity(a);
		entity.setGraphics(g2d);
		entity.setAlive(false);
		pos = new Point2D(0, 0);
		vel = new Point2D(0, 0);
		rotRate = 0.0;
		currentState = 0;
		_collided = false;
		_lifespan = 0;
		_lifeage = 0;
		_health = 0;
		_score = 0;
	}

	// load bitmap file
	public void load(String filename) {
		entity.load(filename);
	}

	public void setCollisionShape(RigidShape r) {
		this.r = r;
	}

	public void drawBounds() {
		r.draw(entity.g2d);
	}

	// perform affine transformations
	public void transform() {
		r.setCenter(center());
		r.setRotation((2 * Math.PI * faceAngle()) / 360);
		testCollision();
		
		entity.setX(pos.x());
		entity.setY(pos.y());
		entity.transform();

	}
	
	public void translate(Vector2D v) {
		pos = pos.plus(v);
	}
	
	public void translate(Point2D p){
		pos = pos.plus(p);
	}

	// draw the image
	public void draw() {
		entity.g2d.drawImage(entity.getImage(), entity.at, entity.panel);
		drawBounds();
	}

	// update the position based on velocity
	public void updatePosition() {
		translate(vel);
	}

	// methods related to automatic rotation factor
	public double rotationRate() {
		return rotRate;
	}

	public void setRotationRate(double rate) {
		rotRate = rate;
	}

	public void updateRotation() {
//		setFaceAngle(faceAngle() + rotRate);
//		if (faceAngle() < 0)
//			setFaceAngle(360 - rotRate);
//		else if (faceAngle() > 360)
//			setFaceAngle(rotRate);

	}

	// generic sprite state variable (alive, dead, collided, etc)
	public int state() {
		return currentState;
	}

	public void setState(int state) {
		currentState = state;
	}

	// returns a bounding rectangle
	public RigidShape getBounds() {
		return r;
	}

	// sprite position
	public Point2D position() {
		return pos;
	}

	public void setPosition(Point2D pos) {
		this.pos = pos;
	}

	// sprite movement velocity
	public Point2D velocity() {
		return vel;
	}

	public void setVelocity(Point2D vel) {
		this.vel = vel;
	}

	// returns the center of the sprite as a Point2D
	public Point2D center() {
		return (new Point2D(pos.x() + imageWidth()/2, pos.y() + imageHeight()/2));
	}

	// generic variable for selectively using sprites
	public boolean alive() {
		return entity.isAlive();
	}

	public void setAlive(boolean alive) {
		entity.setAlive(alive);
	}

	// face angle indicates which direction sprite is facing (degrees)
	public double faceAngle() {
		return entity.getFaceAngle();
	}

	public void setFaceAngle(double angle) {
		entity.setFaceAngle(angle);
	}

	public void setFaceAngle(float angle) {
		entity.setFaceAngle((double) angle);
	}

	public void setFaceAngle(int angle) {
		entity.setFaceAngle((double) angle);
	}

	// move angle indicates direction sprite is moving
	public double moveAngle() {
		return entity.getMoveAngle();
	}

	public void setMoveAngle(double angle) {
		entity.setMoveAngle(angle);
	}

	public void setMoveAngle(float angle) {
		entity.setMoveAngle((double) angle);
	}

	public void setMoveAngle(int angle) {
		entity.setMoveAngle((double) angle);
	}

	// returns the source image width/height
	public int imageWidth() {
		return entity.width();
	}

	public int imageHeight() {
		return entity.height();
	}

	public JPanel panel() {
		return entity.panel;
	}

	public Graphics2D graphics() {
		return entity.g2d;
	}

	public Image image() {
		return entity.image;
	}

	public void setImage(Image image) {
		entity.setImage(image);
	}
	
	/**
	 * Returns a number that associates to a type of sprite
	 * @return 	1 for Tank
	 * 			10 for 
	 * 			100 for Shell
	 */
	public int spriteType() {
		return sprType;
	}

	public void setSpriteType(int type) {
		sprType = type;
	}

	public boolean collided() {
		return _collided;
	}

	public void setCollided(boolean collide) {
		_collided = collide;
	}

	public int lifespan() {
		return _lifespan;
	}

	public void setLifespan(int life) {
		_lifespan = life;
	}

	public int lifeage() {
		return _lifeage;
	}

	public void setLifeage(int age) {
		_lifeage = age;
	}

	public void updateLifetime() {
		// if lifespan is used, it must be > 0
		if (_lifespan > 0) {
			_lifeage++;
			if (_lifeage > _lifespan) {
				setAlive(false);
				_lifeage = 0;
			}
		}
	}

	public void remove() {
		g.entityList.remove(this);
	}

	public void add() {
		g.entityList.add(this);
	}

	/**
	 * Tests the collision of this sprite against the other sprites in the
	 * ArrayList
	 * 
	 * @return true if collision (and performs a translation)
	 */

	public boolean testCollision() {
		boolean b = false;
		for (int i = 0; i < g.entityList.size(); i++) {
			Sprite spr = g.entityList.get(i);
			Vector2D a = this.getBounds().detectCollision(spr.getBounds());
			if (a != null) {
				collision(a, spr);
				b = true;
			}
		}
		return b;
	}
	
	public void collision(Vector2D a, Sprite spr){
		System.out.println("sprite");
	}

	public int health() {
		return _health;
	}

	public void setHealth(int health) {
		_health = health;
	}

	public void changeHealth(int change) {
		_health += change;
	}

	public int score() {
		return _score;
	}

	public void setScore(int score) {
		_score = score;
	}

	public void changeScore(int change) {
		_score += change;
	}

}
