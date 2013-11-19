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

public class Cat extends Entity{
	public Vector speed;
	public int level, maxHP, maxSlobber, currentHP, currentSlobber;
	public boolean onP1,onP2, onGround, change;
	public int time, direction;
	private SpriteSheet jumping, walkingR, walkingL;
	public Animation jump, walkR, walkL, walk;
	public Shape normal, inAir;
	
	public Cat(int x, int y) throws SlickException{
		super(x, y);
		normal = new ConvexPolygon(15f, 42f);
		inAir = new ConvexPolygon(17, 30);
		this.addShape(inAir, new Vector(0f, 3f), null, Color.black);
		direction = 1;
		level = 1;
		maxHP = 100;
		maxSlobber = 10;
		currentHP = maxHP;
		currentSlobber = maxSlobber;
		change = false;
		jumping = new SpriteSheet(ResourceManager.getImage("resource/catJump.png"), 38, 45);
		walkingR = new SpriteSheet(ResourceManager.getImage("resource/catWalkR.png"), 38, 45);
		walkingL = new SpriteSheet(ResourceManager.getImage("resource/catWalkL.png"), 38, 45);
		walkR = new Animation(walkingR, 150);
		walkL = new Animation(walkingL, 150);
		walk = walkR;
		jump = new Animation(jumping, 150);
		speed = new Vector(0f, 0f);
		onP1 = false;
		onP2 = false;
		onGround = false;
	}
	public void hitGround(){
		this.addShape(normal, new Vector(-7f, -3f), null, Color.black);
		this.removeShape(inAir);
	}
	public void jump(){
		this.addShape(inAir, new Vector(0f, 0f), null, Color.black);
		this.removeShape(normal);
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
