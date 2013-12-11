package game;

import jig.ConvexPolygon;
import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

/**
 * Projectile created by player character.
 * 
 * @author Matthew Pessa
 *
 */
public class WaterBall extends Entity {
	
	public static final float defaultRadius = 10.0f;
	public static final int numSides = 30;
	public int type;
	
	public static float radius = defaultRadius;
	
	public boolean exists;
	private Vector velocity;

	public WaterBall(final float x, final float y) {	
		super(x,y);


		this.addImage(ResourceManager.getImage(DogWarriors.battleImages[3]));
		this.addShape(new ConvexPolygon(Projectile.radius, Projectile.numSides), new Vector(0.0f, 0.0f));


		this.velocity = new Vector(0f, 0f);
		this.exists = false;
		
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