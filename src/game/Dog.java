package game;

import org.newdawn.slick.Animation;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import jig.ConvexPolygon;
import jig.Entity;
import jig.ResourceManager;
import jig.Shape;
import jig.Vector;

/**
 * Player character used in the Platform World. Likes to slobber.
 * 
 * @author Matthew Pessa
 */
public class Dog extends Entity {
	public boolean onP1, onP2, onGround; // Maybe just have onPlatform instead of specific platforms.
	public boolean change; // Boolean value to indicate a change of direction
	public boolean shot, kicking; // True if this Dog is executing these abilities
	public boolean levelUp; // True if Dog levels up
	public int kTime, sTime; // Ability timers
	public int cooldown; // Time before another ability can be used
	public int time; // Timer to remove player ability to move after a hit
	public int slobberRegen; // Time before slobber increases
	public int level; // Level of this Dog
	public int maxHP, currentHP; // Hit points (Life) remaining
	public int maxSlobber, currentSlobber; // Amount of slobber remaining, used to power special abilities
	public int attPwr, spPwr; // Damage dealt by this dog to enemy Cats.
	public int direction; // Indicates which direction Dog is currently facing
	public int currentExp; // Amount of experience earned so far
	public int nextLevel; // Amount of experience required for the next level

	public Vector speed; // Speed of this Dog
	public Shape normal, inAir, leg; // Collision boundaries
	public SpriteSheet jumping, walkingR, walkingL, cyclone, shootingR, shootingL;
	public Animation jump, walkR, walkL, walk, kick, shoot, shootR, shootL;
	
	public Dog(int x, int y) throws SlickException {
		super(x, y);
		this.speed = new Vector(0f, 0f);
		this.direction = 1;
		this.level = 1;
		this.currentExp = 0;
		this.nextLevel = 250;
		this.maxHP = 200;
		this.currentHP = maxHP;
		this.maxSlobber = 2;
		this.currentSlobber = maxSlobber;
		this.slobberRegen = 5000;
		this.attPwr = 50;
		this.spPwr = 100;
		this.sTime = 0;
		this.cooldown = 0;
		this.kTime = 0;
		this.levelUp = false;
		this.change = false;
		this.shot = false;
		this.onP1 = false;
		this.onP2 = false;
		this.onGround = false;
		
		this.normal = new ConvexPolygon(17f, 42f);
		this.inAir = new ConvexPolygon(17, 30);
		this.leg = new ConvexPolygon(40f, 8f);
		//this.addShape(inAir, new Vector(0f, 5f), null, Color.black);
		this.bestowAbilities();
	}
	
	/**
	 * Gives this Dog animated abilities.
	 */
	public void bestowAbilities() {
		this.jumping = new SpriteSheet(ResourceManager.getImage(DogWarriors.dogImages[0]), 48, 48);
		this.walkingR = new SpriteSheet(ResourceManager.getImage(DogWarriors.dogImages[5]), 38, 45);
		this.walkingL = new SpriteSheet(ResourceManager.getImage(DogWarriors.dogImages[4]), 38, 45);
		this.cyclone = new SpriteSheet(ResourceManager.getImage(DogWarriors.dogImages[1]), 47, 45);
		this.shootingR = new SpriteSheet(ResourceManager.getImage(DogWarriors.dogImages[3]), 38, 45);
		this.shootingL = new SpriteSheet(ResourceManager.getImage(DogWarriors.dogImages[2]), 38, 45);
		
		this.walkR = new Animation(walkingR, 150);
		this.walkL = new Animation(walkingL, 150);
		this.walk = walkR;
		this.jump = new Animation(jumping, 100);
		this.kick = new Animation(cyclone, 75);
		this.shootR = new Animation(shootingR, 200);
		this.shootL = new Animation(shootingL, 200);
		this.shoot = shootR;
	}
	
	public void hitGround() {
		this.addShape(normal, new Vector(-7f, -3f));
		this.removeShape(inAir);
	}
	
	public void jump() {
		this.addShape(inAir, new Vector(0f, 5f));
		this.removeShape(normal);
	}
	
	public void startKick() {
		this.addShape(leg, new Vector(0f, 8f));
		if (!onGround && !onP1 && !onP2) {
			this.removeShape(inAir);
			this.addShape(normal, new Vector(-7f, -3f));
		}
	}
	
	public void endKick(){
		this.removeShape(leg);
		this.removeShape(normal);
		if (!onGround && !onP1 && !onP2) {
			jump();
		}
	}
	public void clearShapes(){
		this.removeShape(leg);
		this.removeShape(normal);
		this.removeShape(inAir);
	}
	
	public void setVelocity(final Vector v) {
		speed = v;
	}
	
	public Vector getVelocity() {
		return speed;
	}
	
	public void levelUp(){
		if(this.currentExp >= this.nextLevel){
			this.level++;
			this.currentExp -= this.nextLevel;
			this.nextLevel *= 2.5;
			this.maxHP *= 2;
			this.currentHP = this.maxHP;
			this.maxSlobber *= 2;
			this.currentSlobber = this.maxSlobber;
			this.attPwr += 25;
			this.spPwr += 25;
			this.levelUp = true;
		}
	}
	
	public void update(int delta) {
		time -= delta;
		kTime -= delta;
		sTime -= delta;
		cooldown -= delta;
		slobberRegen -= delta;
		if(slobberRegen <= 0){
			slobberRegen = 5000;
			if(currentSlobber < maxSlobber)
				currentSlobber += 1;
		}
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
		translate(speed.scale(delta));
	}
	
}
