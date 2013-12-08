package game;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Random;

public class WorldCat extends Entity {
	public Point NW, NE, SW, SE;
	public boolean exists = true;
	
	private float dampingFactor = 0.25f; // dog slows down due to friction
	private float defaultAcceleration = 0.2f;
	private float acceleration; // amount this dog moves per update
	private float maxSpeed = 0.5f;
	public Vector velocity;
	private int worldX = 30, worldY = 30;
	private int screenX, screenY;
	private float destinationX = 0;
	private float destinationY = 0;
	private int changePath = 1000;
	private int movePath = 30;
	private int randomInt;
	public double time = 0;
	public float sightRange = 400.0f;
	public float sightFactor = 0.01f;
	public boolean chase = false;
	public Vector reset = null;
	Random rand = new Random();
	
	public WorldCat(final float x, final float y) throws SlickException{
		super(x,y);
		this.velocity = new Vector(0.0f, 0.0f);
		this.reset = this.velocity;
		this.initialize();
	}
	
	public WorldCat(final Vector v) {
		super(v.getX(), v.getY());
		this.initialize();
	}
	
	public void setVelocity(final Vector v){
		this.velocity = v.clampLength(0.0f, getMaxSpeed());
	}

	public Vector getVelocity(){
		return this.velocity;
	}
	
	public void update(final int delta){//, final float sx, final float sy) {
		
		translate(velocity.scale(delta));
		//velocity = velocity.scale(1.0f-dampingFactor);		
	}
	
	private void initialize() {
		this.setAcceleration(defaultAcceleration);
		this.addImageWithBoundingBox(ResourceManager.getImage(DogWarriors.worldImages[0]));
		this.NW = new Point((int) this.getCoarseGrainedMinX(), (int) this.getCoarseGrainedMinY());
		this.NE = new Point((int) this.getCoarseGrainedMaxX(), (int) this.getCoarseGrainedMinY());
		this.SW = new Point((int) this.getCoarseGrainedMinX(), (int) this.getCoarseGrainedMaxY());
		this.SE = new Point((int) this.getCoarseGrainedMaxX(), (int) this.getCoarseGrainedMaxY());
	}
	
	public boolean isOnscreen(Rectangle screen) {
		boolean onscreen = false;
		onscreen = (screen.contains(NW) || screen.contains(NE) || screen.contains(SW) || screen.contains(SE));
		return onscreen;
	}
	
	public float getAcceleration() {
		return acceleration;
	}

	public void setAcceleration(float acceleration) {
		this.acceleration = acceleration;
	}
	
	public void bounce(float surfaceTangent){
		if(time + 75 < System.currentTimeMillis()){
			velocity = velocity.bounce(surfaceTangent);
			//time = System.currentTimeMillis();
		}
	}

	public float getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(float maxSpeed) {
		this.maxSpeed = maxSpeed;
	}
	
}
