package game;

import org.newdawn.slick.Color;

import jig.ConvexPolygon;
import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

public class waterBall extends Entity{
	private Vector velocity;
	public boolean exists;
	
	public waterBall(final float x, final float y){
		super(x,y);
		exists = false;
		this.addImage(ResourceManager.getImage("resource/waterball.png"));
		this.addShape(new ConvexPolygon(10, 30), new Vector(0f, 0f), null, Color.black);
		velocity = new Vector(0f, 0f);
	}

	public void setVelocity(final Vector v) {
		velocity = v;
	}

	public Vector getVelocity() {
		return velocity;
	}

	public void update(final int delta){
		translate(velocity.scale(delta));
	}
}
