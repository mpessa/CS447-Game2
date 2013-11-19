package game;

import org.newdawn.slick.Color;

import jig.ConvexPolygon;
import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

public class Fireball extends Entity{
	private Vector velocity;
	public boolean exists;
	
	public Fireball(final float x, final float y, final float vx, final float vy, int dir){
		super(x,y);
		exists = false;
		if(dir == 0)
			this.addImage(ResourceManager.getImage("resource/fireballR.png"));
		if(dir == 1)
			this.addImage(ResourceManager.getImage("resource/fireballL.png"));
		this.addShape(new ConvexPolygon(10, 30), new Vector(0f, 0f), null, Color.black);
		velocity = new Vector(vx, vy);
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