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
	private float dampingFactor = 0.25f; // dog slows down due to friction
	private float defaultAcceleration = 0.2f;
	private float acceleration; // amount this dog moves per update
	private float maxSpeed = 0.5f;
	private Vector velocity;
	private int worldX = 30, worldY = 30;
	private int screenX, screenY;
	private float destinationX = 0;
	private float destinationY = 0;
	private int changePath = 1000;
	private int movePath = 30;
	private int randomInt;
	Random rand = new Random();
	
	public WorldCat(final float x, final float y) throws SlickException{
		super(x,y);
		this.velocity = new Vector(0.1f, 0.1f);
		this.initialize();
		
		
	}
	
	public WorldCat(final Vector v) {
		super(v.getX(), v.getY());
		this.initialize();
	}
	
	public void setVelocity(final Vector v){
		this.velocity = v.clampLength(0.0f, maxSpeed);
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
	
}
