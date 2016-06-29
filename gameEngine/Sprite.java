package gameEngine;

/*********************************************************
 * Beginning Java Game Programming, 3rd Edition
 * by Jonathan S. Harbour
 * Sprite class
 **********************************************************/
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.JPanel;

import math.geom2d.Point2D;
import math.geom2d.Vector2D;
import tankAttack.AI;

public class Sprite extends Object {
	private ImageEntity entity;
	protected EnginePoint2D pos;
	protected EnginePoint2D vel;
	protected Box box;
	protected double rotRate;
	protected int currentState;
	protected int sprType;
	protected boolean _collided;
	protected int _lifespan, _lifeage;
	protected int _health;
	protected int _score;

	// constructor
	Sprite(JPanel a, Graphics2D g2d) {
		entity = new ImageEntity(a);
		entity.setGraphics(g2d);
		entity.setAlive(false);
		pos = new EnginePoint2D(0, 0);
		vel = new EnginePoint2D(0, 0);
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

	// perform affine transformations
	public void transform() {
		entity.setX(pos.X());
		entity.setY(pos.Y());
		entity.transform();
	}

	// draw the image
	public void draw() {
		entity.g2d.drawImage(entity.getImage(), entity.at, entity.panel);
	}

	// draw bounding rectangle around sprite
	public void drawBounds(Color c) {
		entity.g2d.setColor(c);
		if (box != null) {
			box.render(entity.g2d, new Point2D(center().X(), center().Y()));
		}
	}

	// Sets the collision box of the sprite
	public void setBox(Box b) {
		box = b;
	}

	public Box getBox() {
		return box;
	}

	// update the position based on velocity
	public void updatePosition() {
		pos.setX(pos.X() + vel.X());
		pos.setY(pos.Y() + vel.Y());
	}

	// methods related to automatic rotation factor
	public double rotationRate() {
		return rotRate;
	}

	public void setRotationRate(double rate) {
		rotRate = rate;
	}

	public void updateRotation() {
		setFaceAngle(faceAngle() + rotRate);
		if (faceAngle() < 0)
			setFaceAngle(360 - rotRate);
		else if (faceAngle() > 360)
			setFaceAngle(rotRate);
		if (box != null) {
			box.rotate(faceAngle());
		}
	}

	// generic sprite state variable (alive, dead, collided, etc)
	public int state() {
		return currentState;
	}

	public void setState(int state) {
		currentState = state;
	}

	// sprite position
	public EnginePoint2D position() {
		return pos;
	}

	public void setPosition(EnginePoint2D pos) {
		this.pos = pos;
	}

	// sprite movement velocity
	public EnginePoint2D velocity() {
		return vel;
	}

	public void setVelocity(EnginePoint2D vel) {
		this.vel = vel;
	}

	// returns the center of the sprite as a Point2D
	public EnginePoint2D center() {
		return (new EnginePoint2D(entity.getCenterX(), entity.getCenterY()));
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
