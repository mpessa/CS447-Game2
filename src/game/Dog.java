package game;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import jig.ConvexPolygon;
import jig.Entity;
import jig.ResourceManager;
import jig.Shape;
import jig.Vector;

public class Dog extends Entity{
	public Vector speed;
	public int level, maxHP, maxSlobber, currentHP, currentSlobber;
	public boolean onP1,onP2, onGround, change;
	public int time, direction;
	private SpriteSheet jumping, walkingR, walkingL;
	public Animation jump, walkR, walkL, walk;
	public Shape normal, inAir;
	
	public Dog(int x, int y) throws SlickException{
		super(x, y);
		final ConvexPolygon normal = new ConvexPolygon(15f, 42f);
		final ConvexPolygon inAir = new ConvexPolygon(12, 30);
		//this.addShape(new ConvexPolygon(15f, 42f), new Vector(-7f, -3f), null, Color.black);
		this.addShape(normal, new Vector(-7f, -3f), null, Color.black);
		direction = 1;
		level = 1;
		maxHP = 100;
		maxSlobber = 10;
		currentHP = maxHP;
		currentSlobber = maxSlobber;
		change = false;
		jumping = new SpriteSheet(ResourceManager.getImage("resource/dogJump.png"), 48, 48);
		walkingR = new SpriteSheet(ResourceManager.getImage("resource/dogWalkR.png"), 38, 45);
		walkingL = new SpriteSheet(ResourceManager.getImage("resource/dogWalkL.png"), 38, 45);
		walkR = new Animation(walkingR, 150);
		walkL = new Animation(walkingL, 150);
		walk = walkR;
		jump = new Animation(jumping, 100);
		speed = new Vector(0f, 0f);
		onP1 = false;
		onP2 = false;
		onGround = false;
	}
	public void hitGround(){
		this.removeShape(inAir);
		this.addShape(normal, new Vector(-7f, -3f), null, Color.black);
	}
	public void jump(){
		this.removeShape(normal);
		this.addShape(inAir, new Vector(0f, 0f), null, Color.black);
	}
	public void setVelocity(final Vector v){
		speed = v;
	}
	public Vector getVelocity(){
		return speed;
	}
	public void update(int delta){
		time -= delta;
		if(change){
			change = false;
			if(this.direction == 1){
				walk = walkR;
				this.direction = 0;
			}
			else if(this.direction == 0){
				walk = walkL;
				this.direction = 1;
			}
		}
		translate(speed.scale(delta));
	}
	
}
