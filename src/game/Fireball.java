package game;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.SpriteSheet;

import jig.ConvexPolygon;
import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

public class Fireball extends Entity{
	private Vector velocity;
	public SpriteSheet fireball;
	public Animation fire;
	
	public Fireball(final float x, final float y, final float vx, final float vy, int dir){
		super(x,y);
		if(dir == 0){
			this.fireball = new SpriteSheet(ResourceManager.getImage("resource/fireballR.png"), 32, 25);
		}
		if(dir == 1)
			this.fireball = new SpriteSheet(ResourceManager.getImage("resource/fireballL.png"), 32, 25);
		this.fire = new Animation(fireball, 150);
		this.addShape(new ConvexPolygon(10, 30), new Vector(17f, 13f), null, Color.black);
		this.velocity = new Vector(vx, vy);
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