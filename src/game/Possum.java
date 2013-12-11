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

/**
 * Boss character used in the platform world.
 * 
 * @author Matthew Pessa
 */
public class Possum extends Entity{

	public boolean onP1, onP2, onGround; // Indicates if the Possum is on a platform or ground
	public boolean change; // Indicates that a change in direction is needed
	public boolean shooting, kicking; // Indicates if these abilities are being used
	public boolean exists; // Indicates if the Possum is supposed to exist
	public boolean dead; // True if the Possum is now dead
	public boolean facing; // True if the Possum is facing the Dog
	
	public int cooldown; // Time between ability uses
	public int time; // Time between hits
	public int sTime, kTime; // Duration of movements for shooting and kicking
	public int maxHP, currentHP; // Hit points of Possum
	public int attPwr; //Amount of damage that the Possum deals
	
	public Vector speed; // Speed of the Possum
	public Shape normal, inAir, kick1, kick2; // Fine grained bounding boxes created for Possum
	
	private SpriteSheet jumping, walkingR, walkingL, shootingR, shootingL, kickingR, kickingL;//, dyingR, dyingL;
	public Animation jump, walkR, walkL, walk, shootR, shootL, shoot, kickR, kickL, kick, dieR, dieL, die;
	
	public Possum(final float x, final float y) throws SlickException{
		super(x, y);
		this.maxHP = 1000;
		this.currentHP = maxHP;
		this.cooldown = 0;
		this.sTime = 0;
		this.kTime = 0;
		this.time = 0;
		this.attPwr = 30;
		this.change = false;
		this.onP1 = false;
		this.onP2 = false;
		this.onGround = false;
		this.facing = true;
		this.kicking = false;
		this.shooting = false;
		this.exists = false;
		this.dead = false;
		this.speed = new Vector(0f, 0f);
		this.normal = new ConvexPolygon(38f, 58f);
		this.inAir = new ConvexPolygon(22, 30);
		this.kick1 = new ConvexPolygon(27f, 58f);
		this.kick2 = new ConvexPolygon(65f, 12f);
		this.addShape(inAir, new Vector(6f, 15f));
		this.bestowAbilities();
	}
	
	public void hitGround() {
		this.addShape(normal, new Vector(7f, 7f));
		this.removeShape(inAir);
	}
	
	public void jump() {
		this.addShape(inAir, new Vector(6f, 15f));
		this.removeShape(normal);
		this.jump.restart();
		this.onGround = false;
		this.onP1 = false;
		this.onP2 = false;
	}
	
	public void startKick() {
		this.addShape(kick1, new Vector(9f, 7f));
		this.addShape(kick2, new Vector(9f, 6f));
		if (!onGround && !onP1 && !onP2) {
			this.removeShape(inAir);
		}
		else
			this.removeShape(normal);
	}
	
	public void endKick(){
		this.removeShape(kick1);
		this.removeShape(kick2);
		if (!onGround && !onP1 && !onP2) {
			jump();
		}
		else
			this.addShape(normal, new Vector(7f, 7f));
	}
	
	public void setVelocity(Vector v){
		this.speed = v;
	}
	
	public Vector getVelocity(){
		return this.speed;
	}
	
	public void update(int delta){
		this.cooldown -= delta;
		this.kTime -= delta;
		this.sTime -= delta;
		this.time -= delta;
		if(change){
			change = false;
			if(walk == walkR)
				walk = walkL;
			if(walk == walkL)
				walk = walkR;
		}
		translate(speed.scale(delta));
	}
	
	private void bestowAbilities(){
		this.jumping = new SpriteSheet(ResourceManager.getImage(DogWarriors.possumImages[0]), 64, 64);
		this.kickingL = new SpriteSheet(ResourceManager.getImage(DogWarriors.possumImages[1]), 64, 72);
		this.kickingR = new SpriteSheet(ResourceManager.getImage(DogWarriors.possumImages[2]), 64, 72);
		this.shootingL = new SpriteSheet(ResourceManager.getImage(DogWarriors.possumImages[3]), 48, 64);
		this.shootingR = new SpriteSheet(ResourceManager.getImage(DogWarriors.possumImages[4]), 48, 64);
		this.walkingL = new SpriteSheet(ResourceManager.getImage(DogWarriors.possumImages[5]), 64, 64);
		this.walkingR = new SpriteSheet(ResourceManager.getImage(DogWarriors.possumImages[6]), 64, 64);
		
		this.walkR = new Animation(walkingR, 150);
		this.walkL = new Animation(walkingL, 150);
		this.walk = walkL;
		
		this.shootR = new Animation(shootingR, 150);
		this.shootL = new Animation(shootingL, 150);
		this.shoot = shootL;
		
		this.kickR = new Animation(kickingR, 200);
		this.kickL = new Animation(kickingL, 200);
		this.kick = kickL;
		
		this.jump = new Animation(jumping, 100);
	}
}
