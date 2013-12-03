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
 * Enemy character used in the Platform World. Likes fire.
 * 
 * @author Matthew Pessa
 */
public class Cat extends Entity{	
	public boolean onP1, onP2, onGround; // Maybe just have onPlatform instead of specific platforms
	public boolean change; // Indicates that a change in direction is needed
	public boolean dead; // True if this Cat is dead
	public boolean done; // True if this Cat is dead and has hit the ground
	public boolean canFire, canKick, canShield; // Abilities
	public boolean shielded; // True if this Cat has a shield
	public boolean chase; // True if this Cat is chasing the Dog
	public boolean inAir; // True if this Cat is kicking at the Dog
	public int fTime, kTime, sTime; // Ability timers
	public int cooldown; // Time between ability uses
	public int time; // General timer for Cat AI
	public int platTime; // Amount of time on a platform
	public int drop; // Timer to allow Cat to drop through platforms
	public int hitTime; // Timer between possible hits
	public int level; // Higher level = More powerful Cat
	public int maxHP, currentHP; // Hit points of Cat
	public int attPwr; //Amount of damage that the Cat deals
	public int exp; // Amount of experience earned for defeating this Cat
	
	public Vector speed; // Speed of this Cat
	public Shape normal; // Fine grained bounding box created for cat
	private SpriteSheet jumping, walkingR, walkingL, shootingR, shootingL, kickingR, kickingL, dyingR, dyingL;
	public Animation jump, walkR, walkL, walk, shootR, shootL, shoot, kickR, kickL, kick, dieR, dieL, die;
	
	/**
	 * Makes a new Cat of the specified level, at the specified position.
	 * @param x - the Cat's X position
	 * @param y - the Cat's Y position
	 * @param l - the Cat's level
	 * @throws SlickException
	 */
	public Cat(int x, int y, int l) throws SlickException{
		super(x, y);
		this.level = l;
		this.maxHP = 100 * l;
		this.currentHP = maxHP;
		this.hitTime = 0;
		this.speed = new Vector(0.0f, 0.0f);
		this.attPwr = 10; //Scale by level later
		this.change = false;
		this.dead = false;
		this.onGround = false;
		this.onP1 = false; // Does the Cat need to know which platform he's on?
		this.onP2 = false; //
		// this.onPlatform = false
		this.chase = false;
		this.inAir = false;
		this.shielded = false;
		this.platTime = 0;
		this.drop = 0;
		this.normal = new ConvexPolygon(15f, 42f);
		
		this.addShape(normal, new Vector(-8f, -3f), null, Color.black);
		this.bestowAbilities(level);
	}
	
	public void kill() {
		if(this.onGround || this.onP1 || this.onP2){
			this.setVelocity(new Vector(0f, 0f));
			this.removeShape(normal);
			this.done = true;
		}
		this.die.stop();
	}
	
	public void setVelocity(final Vector v) {
		speed = v;
	}
	
	public Vector getVelocity() {
		return speed;
	}
	
	public void update(int delta) {
		time -= delta;
		hitTime -= delta;
		kTime -= delta;
		sTime -= delta;
		platTime -= delta;
		drop -= delta;
		cooldown -= delta;
		if(change && this.level != 4) {
			change = false;
			if(this.speed.getX() > 0){
				walk = walkR;
			}
			else if(this.speed.getX() < 0){
				walk = walkL;
			}
		}
		if(change && this.level == 4){
			change = false;
			if(this.speed.getX() > 0){
				walk = walkL;
			}
			if(this.speed.getX() <0){
				walk = walkR;
			}
		}
		if(kTime <= 0 && inAir){
			inAir = false;
			setVelocity(new Vector(this.speed.getX() / 2, this.speed.getY()));
		}
		translate(speed.scale(delta));
	}
	
	/**
	 * Gives this Cat animated abilities based on its level.
	 * @param level - the level of this Cat.
	 */
	private void bestowAbilities(int level) {
		this.canFire = false;
		this.canShield = false;
		this.canKick = false;
		
		switch (level) {
		case(1):
			this.jumping = new SpriteSheet(ResourceManager.getImage(DogWarriors.catImages[8]), 38, 45);
			this.walkingR = new SpriteSheet(ResourceManager.getImage(DogWarriors.catImages[26]), 38, 45);
			this.walkingL = new SpriteSheet(ResourceManager.getImage(DogWarriors.catImages[22]), 38, 45);
			this.dyingR = new SpriteSheet(ResourceManager.getImage(DogWarriors.catImages[4]), 38, 45);
			this.dyingL = new SpriteSheet(ResourceManager.getImage(DogWarriors.catImages[0]), 38, 45);
			this.exp = 25;
			break;
		case(2):
			this.jumping = new SpriteSheet(ResourceManager.getImage(DogWarriors.catImages[9]), 38, 45);
			this.walkingR = new SpriteSheet(ResourceManager.getImage(DogWarriors.catImages[27]), 38, 45);
			this.walkingL = new SpriteSheet(ResourceManager.getImage(DogWarriors.catImages[23]), 38, 45);
			this.kickingR = new SpriteSheet(ResourceManager.getImage(DogWarriors.catImages[15]), 49, 45);
			this.kickingL = new SpriteSheet(ResourceManager.getImage(DogWarriors.catImages[12]), 49, 45);
			this.dyingR = new SpriteSheet(ResourceManager.getImage(DogWarriors.catImages[5]), 38, 45);
			this.dyingL = new SpriteSheet(ResourceManager.getImage(DogWarriors.catImages[1]), 38, 45);
			this.canKick = true;
			this.exp = 50;
			break;
		case(3):
			this.jumping = new SpriteSheet(ResourceManager.getImage(DogWarriors.catImages[10]), 38, 45);
			this.walkingR = new SpriteSheet(ResourceManager.getImage(DogWarriors.catImages[28]), 38, 45);
			this.walkingL = new SpriteSheet(ResourceManager.getImage(DogWarriors.catImages[24]), 38, 45);
			this.shootingR = new SpriteSheet(ResourceManager.getImage(DogWarriors.catImages[20]), 38, 45);
			this.shootingL = new SpriteSheet(ResourceManager.getImage(DogWarriors.catImages[18]), 38, 45);
			this.kickingR = new SpriteSheet(ResourceManager.getImage(DogWarriors.catImages[16]), 49, 45);
			this.kickingL = new SpriteSheet(ResourceManager.getImage(DogWarriors.catImages[13]), 49, 45);
			this.dyingR = new SpriteSheet(ResourceManager.getImage(DogWarriors.catImages[6]), 38, 45);
			this.dyingL = new SpriteSheet(ResourceManager.getImage(DogWarriors.catImages[2]), 38, 45);
			this.canKick = true;
			this.canFire = true;
			this.exp = 100;
			break;
		default: // maximum level cat!
			this.jumping = new SpriteSheet(ResourceManager.getImage(DogWarriors.catImages[11]), 38, 45);
			this.walkingR = new SpriteSheet(ResourceManager.getImage(DogWarriors.catImages[29]), 38, 45);
			this.walkingL = new SpriteSheet(ResourceManager.getImage(DogWarriors.catImages[25]), 38, 45);
			this.shootingR = new SpriteSheet(ResourceManager.getImage(DogWarriors.catImages[21]), 38, 45);
			this.shootingL = new SpriteSheet(ResourceManager.getImage(DogWarriors.catImages[19]), 38, 45);
			this.kickingR = new SpriteSheet(ResourceManager.getImage(DogWarriors.catImages[17]), 49, 45);
			this.kickingL = new SpriteSheet(ResourceManager.getImage(DogWarriors.catImages[14]), 49, 45);
			this.dyingR = new SpriteSheet(ResourceManager.getImage(DogWarriors.catImages[7]), 38, 45);
			this.dyingL = new SpriteSheet(ResourceManager.getImage(DogWarriors.catImages[3]), 38, 45);
			this.canKick = true;
			this.canFire = true;
			this.canShield = true;
			this.exp = 150;
		}
		
		this.walkR = new Animation(walkingR, 150);
		this.walkL = new Animation(walkingL, 150);
		this.walk = walkR;
		this.dieR = new Animation(dyingR, 200);
		this.dieL = new Animation(dyingL, 200);
		this.die = dieR;
		this.jump = new Animation(jumping, 150);
	
		if (this.canKick) {
			this.kickR = new Animation(kickingR, 150);
			this.kickL = new Animation(kickingL, 150);
			this.kick = kickR;
		}
		
		if (this.canFire) {
			this.shootR = new Animation(shootingR, 150);
			this.shootL = new Animation(shootingL, 150);
			this.shoot = shootR;
		}
	}
}
