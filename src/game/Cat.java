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
	public int level, maxHP, currentHP;
	public boolean onP1,onP2, onGround, change, canFire, canKick, canShield;
	public int time, fTime, kTime, sTime, hitTime;
	private SpriteSheet jumping, walkingR, walkingL, shootingR, shootingL;
	public Animation jump, walkR, walkL, walk, shoot;
	public Shape normal;
	
	public Cat(int x, int y, int l) throws SlickException{
		super(x, y);
		normal = new ConvexPolygon(15f, 42f);
		this.addShape(normal, new Vector(-8f, -3f), null, Color.black);
		level = l;
		maxHP = 100 * l;
		if(level == 4)
			canShield = true;
		else
			canShield = false;
		if(level >= 3)
			canFire = true;
		else
			canFire = false;
		if(level >= 2)
			canKick = true;
		else
			canKick = false;
		currentHP = maxHP;
		change = false;
		if(level == 1){
			jumping = new SpriteSheet(ResourceManager.getImage("resource/catJump.png"), 38, 45);
			walkingR = new SpriteSheet(ResourceManager.getImage("resource/catWalkR.png"), 38, 45);
			walkingL = new SpriteSheet(ResourceManager.getImage("resource/catWalkL.png"), 38, 45);
		}
		if(level == 2){
			jumping = new SpriteSheet(ResourceManager.getImage("resource/catJumpG.png"), 38, 45);
			walkingR = new SpriteSheet(ResourceManager.getImage("resource/catWalkRG.png"), 38, 45);
			walkingL = new SpriteSheet(ResourceManager.getImage("resource/catWalkLG.png"), 38, 45);
		}
		if(level == 3){
			jumping = new SpriteSheet(ResourceManager.getImage("resource/catJumpR.png"), 38, 45);
			walkingR = new SpriteSheet(ResourceManager.getImage("resource/catWalkRR.png"), 38, 45);
			walkingL = new SpriteSheet(ResourceManager.getImage("resource/catWalkLR.png"), 38, 45);
			shootingR = new SpriteSheet(ResourceManager.getImage("resource/catShootRR.png"), 38, 45);
			shootingL = new SpriteSheet(ResourceManager.getImage("resource/catShootLR.png"), 38, 45);
		}
		if(level == 4){
			jumping = new SpriteSheet(ResourceManager.getImage("resource/catJumpY.png"), 38, 45);
			walkingR = new SpriteSheet(ResourceManager.getImage("resource/catWalkRY.png"), 38, 45);
			walkingL = new SpriteSheet(ResourceManager.getImage("resource/catWalkLY.png"), 38, 45);
			shootingR = new SpriteSheet(ResourceManager.getImage("resource/catShootRY.png"), 38, 45);
			shootingL = new SpriteSheet(ResourceManager.getImage("resource/catShootLY.png"), 38, 45);
		}
		walkR = new Animation(walkingR, 150);
		walkL = new Animation(walkingL, 150);
		walk = walkR;
		hitTime = 0;
		jump = new Animation(jumping, 150);
		speed = new Vector(0f, 0f);
		onP1 = false;
		onP2 = false;
		onGround = false;
	}
	public void setVelocity(final Vector v){
		speed = v;
	}
	public Vector getVelocity(){
		return speed;
	}
	public void update(int delta){
		time -= delta;
		hitTime -= delta;
		if(change){
			change = false;
			if(this.speed.getX() > 0){
				walk = walkR;
			}
			else if(this.speed.getX() < 0){
				walk = walkL;
			}
		}
		translate(speed.scale(delta));
	}
}
