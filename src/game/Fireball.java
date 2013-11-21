package game;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.SpriteSheet;

import jig.ConvexPolygon;
import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

public class Fireball extends Entity {
	
	public static final Color defaultOutlineColor = Color.black;
	public static final float defaultRadius = 10.0f;
	public static final int numSides = 30;
	
	public static float radius = defaultRadius;
	public static Color outlineColor = defaultOutlineColor;
	
	public boolean exists;
	private Vector velocity;
	
	public SpriteSheet fireball;
	public Animation fire;
	
	// refactor this into a Projectile class, with different types!
	public Fireball(final float x, final float y, final float vx, final float vy, int dir) {	
		super(x,y);
		
		if (dir == 0) { // this is a right-facing fireball
			this.fireball = new SpriteSheet(ResourceManager.getImage(DogWarriors.battleImages[1]), 32, 25);
		} else if (dir == 1) { // this is a left-facing fireball
			this.fireball = new SpriteSheet(ResourceManager.getImage(DogWarriors.battleImages[0]), 32, 25);
		}
		
		this.fire = new Animation(fireball, 150);
		this.velocity = new Vector(vx, vy);
		this.exists = true;
		
		//this.addAnimation(fire);
		this.addShape(new ConvexPolygon(Fireball.radius, Fireball.numSides),
				new Vector(17.0f, 13.0f), null, Fireball.outlineColor);
	}

	public void setVelocity(final Vector v) {
		velocity = v;
	}

	public Vector getVelocity() {
		return velocity;
	}

	public void update(final int delta){
		this.translate(velocity.scale(delta));
	}
}