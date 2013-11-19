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
	public boolean onP1,onP2, onGround, change, shot, kicking;
	public int time, direction, kTime, sTime, cooldown;
	public SpriteSheet jumping, walkingR, walkingL, cyclone, shootingR, shootingL;
	public Animation jump, walkR, walkL, walk, kick, shoot, shootR, shootL;
	public Shape normal, inAir, leg;
	
	public Dog(int x, int y) throws SlickException{
		super(x, y);
		normal = new ConvexPolygon(15f, 42f);
		inAir = new ConvexPolygon(17, 30);
		leg = new ConvexPolygon(40f, 8f);
		this.addShape(inAir, new Vector(0f, 5f), null, Color.black);
		direction = 1;
		level = 2;
		maxHP = 100;
		maxSlobber = 10;
		currentHP = maxHP;
		currentSlobber = maxSlobber;
		change = false;
		jumping = new SpriteSheet(ResourceManager.getImage("resource/dogJump.png"), 48, 48);
		walkingR = new SpriteSheet(ResourceManager.getImage("resource/dogWalkR.png"), 38, 45);
		walkingL = new SpriteSheet(ResourceManager.getImage("resource/dogWalkL.png"), 38, 45);
		cyclone = new SpriteSheet(ResourceManager.getImage("resource/dogKick.png"), 47, 45);
		shootingR = new SpriteSheet(ResourceManager.getImage("resource/dogShootR.png"), 38, 45);
		shootingL = new SpriteSheet(ResourceManager.getImage("resource/dogShootL.png"), 38, 45);
		walkR = new Animation(walkingR, 150);
		walkL = new Animation(walkingL, 150);
		walk = walkR;
		jump = new Animation(jumping, 100);
		kick = new Animation(cyclone, 75);
		kTime = 0;
		shootR = new Animation(shootingR, 200);
		shootL = new Animation(shootingL, 200);
		shoot = shootR;
		shot = false;
		sTime = 0;
		cooldown = 0;
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
		this.addShape(inAir, new Vector(0f, 5f), null, Color.black);
		this.removeShape(normal);
	}
	public void startKick(){
		this.addShape(leg, new Vector(0f, 8f), null, Color.black);
		if(!onGround && !onP1 && !onP2){
			this.removeShape(inAir);
		}
		this.addShape(normal, new Vector(-7f, -3f), null, Color.black);
	}
	public void endKick(){
		this.removeShape(leg);
		this.removeShape(normal);
		if(!onGround && !onP1 && !onP2){
			jump();
		}
	}
	public void setVelocity(final Vector v){
		speed = v;
	}
	public Vector getVelocity(){
		return speed;
	}
	public void update(int delta){
		time -= delta;
		kTime -= delta;
		sTime -= delta;
		cooldown -= delta;
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
