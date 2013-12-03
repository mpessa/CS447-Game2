package game;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.SpriteSheet;

import jig.ConvexPolygon;
import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

public class Projectile extends Entity {
	
	public static final Color defaultOutlineColor = Color.black;
	public static final float defaultRadius = 10.0f;
	public static final int numSides = 30;
	public int type;
	
	public static float radius = defaultRadius;
	public static Color outlineColor = defaultOutlineColor;
	
	//public boolean exists;
	private Vector velocity;
	
	public SpriteSheet ball;
	public Animation fire;
	
	// refactor this into a Projectile class, with different types!
	public Projectile(final float x, final float y, final float vx, final float vy, int flavor, int dir) {	
		super(x,y);
		type = flavor;
		if(type == 0){
			if (dir == 0) { // this is a right-facing fireball
				this.ball = new SpriteSheet(ResourceManager.getImage(DogWarriors.battleImages[1]), 32, 25);
			} else if (dir == 1) { // this is a left-facing fireball
				this.ball = new SpriteSheet(ResourceManager.getImage(DogWarriors.battleImages[0]), 32, 25);
			}
			
			this.fire = new Animation(ball, 150);
			
			//this.addAnimation(fire);
			this.addShape(new ConvexPolygon(Projectile.radius, Projectile.numSides),
					new Vector(17.0f, 13.0f), null, Projectile.outlineColor);
		}
		
		if(type == 1){
			this.addImage(ResourceManager.getImage(DogWarriors.battleImages[3]));
			this.addShape(new ConvexPolygon(Projectile.radius, Projectile.numSides),
						  new Vector(0.0f, 0.0f), null, Projectile.outlineColor);
		}
		
		if(type == 2){
			if(dir == 0){ // right facing fist
				this.addImage(ResourceManager.getImage(DogWarriors.battleImages[6]));
				this.addShape(new ConvexPolygon(8, 30), new Vector(0f, 0f), null, Projectile.outlineColor);
			}
			else if(dir == 1){ // left facing fist
				this.addImage(ResourceManager.getImage(DogWarriors.battleImages[5]));
				this.addShape(new ConvexPolygon(8, 30), new Vector(0f, 0f), null, Projectile.outlineColor);
			}
		}
		
		this.velocity = new Vector(vx, vy);
		//this.exists = true;
		
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