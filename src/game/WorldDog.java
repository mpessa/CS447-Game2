package game;

import java.awt.Rectangle;

import org.newdawn.slick.Animation;
import org.newdawn.slick.SpriteSheet;

import jig.ConvexPolygon;
import jig.Entity;
import jig.ResourceManager;
import jig.Vector;
import jig.Shape;

/**
 * Player Character used in the Overworld game
 * 
 * @author Abel Hoxeng
 * @author Mitchel Pulley
 */
public class WorldDog extends Entity{
	
	private float dampingFactor = 0.25f; // dog slows down due to friction
	private float defaultAcceleration = 0.2f;
	private float acceleration; // amount this dog moves per update
	private float maxSpeed = 0.25f;
	public boolean change;
	public Vector velocity;
	public int switchTimerA, switchTimerW, switchTimerS, switchTimerD;
	public int direction;
	public Shape normal;
	public SpriteSheet walkingR, walkingL;
	public Animation walkR, walkL, walk;
	
	/**
	 * Creates a new WorldDog at the specified Vector coordinates
	 * @param v
	 */
	public WorldDog(final Vector v) {
		super(v.getX(), v.getY());
		this.initialize();
	}
	
	/**
	 * Creates a new WorldDog at the specified coordinates
	 * @param x
	 * @param y
	 */
	public WorldDog(final float x, final float y){
		super(x,y);
		this.initialize();
	}
	
	public void initialize() {
		this.switchTimerS = 200;
		this.switchTimerA = 200;
		this.switchTimerW = 200;
		this.switchTimerD = 200;
		this.direction = 1;
		this.change = false;
		this.moveDog();
		this.velocity = new Vector(0.0f, 0.0f);
		this.setAcceleration(defaultAcceleration);
		this.addShape(new ConvexPolygon(32f, 32f));
	}
	
	public void setVelocity(final Vector v){
		this.velocity = v.clampLength(0.0f, maxSpeed);
	}

	public Vector getVelocity(){
		return this.velocity;
	}

	public void update(final int delta) {
		if (change) {
			change = false;
			if (this.direction == 1) {
				walk = walkR;
				this.direction = 0;
			}
			else if(this.direction == 0) {
				walk = walkL;
				this.direction = 1;
			}
		}
		translate(velocity.scale(delta));
		velocity = velocity.scale(1.0f-dampingFactor);
	}
	
	public float getAcceleration() {
		return acceleration;
	}

	public void setAcceleration(float acceleration) {
		this.acceleration = acceleration;
	}
	
	public void moveDog(){
		this.walkingR = new SpriteSheet(ResourceManager.getImage(DogWarriors.worldImages[9]), 27, 32);
		this.walkingL = new SpriteSheet(ResourceManager.getImage(DogWarriors.worldImages[8]), 27, 32);		
		this.walkR = new Animation(walkingR, 150);
		this.walkL = new Animation(walkingL, 150);
		this.walk = walkR;
	}
	
	/**
	 * Return true if this Dog is close enough to the other entity to warrant a collision detection
	 * @param other
	 * @return true if the other Entity is close enough to the Dog for a collision to possibly happen
	 */
	public boolean isNear(Entity other) {
		boolean near = false;
		float r = TownMap.TILESIZE * 2.0f;
		if (other.getPosition().distance(this.getPosition()) <= r) near = true;
		return near;
	}

}
