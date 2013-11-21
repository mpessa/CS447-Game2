package game;

import org.newdawn.slick.Color;

import jig.ConvexPolygon;
import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

/**
 * A Water Ball class that should be refactored into a Projectile class
 * 
 * @author Matthew Pessa
 */
public class WaterBall extends Entity {

	public static final Color defaultColor = Color.black;
	public static final float defaultRadius = 37.5f;
	public static final int numSides = 30;
	
	public static float radius = defaultRadius;
	public static Color color = defaultColor;
	
	public boolean exists;
	private Vector velocity;
	
	public WaterBall(final float x, final float y){
		super(x,y);
		
		this.exists = false;
		this.velocity = new Vector(0f, 0f);
		
		this.addImage(ResourceManager.getImage(DogWarriors.battleImages[3]));
		this.addShape(new ConvexPolygon(WaterBall.radius, WaterBall.numSides),
					  new Vector(0.0f, 0.0f), null, WaterBall.color);
	}

	public void setVelocity(final Vector v) {
		this.velocity = v;
	}

	public Vector getVelocity() {
		return this.velocity;
	}

	public void update(final int delta){
		translate(velocity.scale(delta));
	}
}
