package game;

import jig.ConvexPolygon;
import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;

/**
 * Different powerups that can appear in the platform world.
 * 
 * @author Matthew Pessa
 *
 */
public class Powerup extends Entity{
	
	public Vector speed; // Velocity of the powerup, used to allow it to drop
	
	public int type; // Indicates what type of powerup this is
	
	public boolean onGround; // True if the powerup is on a platform or the ground

	public Powerup(final float x, final float y, int flavor) throws SlickException{
		super(x, y);
		
		this.type = flavor;
		this.speed = new Vector(0f, 0f);
		this.onGround = false;
		
		if(type == 0){ // Slobber replacement
			this.addImage(ResourceManager.getImage(DogWarriors.powerupImages[0]));
			this.addShape(new ConvexPolygon(10, 30), new Vector(0f, 0f), null, Color.black);
		}
		if(type == 1){ // Health increase
			this.addImage(ResourceManager.getImage(DogWarriors.powerupImages[1]));
			this.addShape(new ConvexPolygon(50f, 12f), new Vector(0f, 0f), null, Color.black);
		}
		if(type == 2){ // Chewtoy to end the game
			this.addImage(ResourceManager.getImage(DogWarriors.powerupImages[2]));
			this.addShape(new ConvexPolygon(50f, 12f), new Vector(0f, 0f));
		}
	}
	
	public void setVelocity(Vector v){
		this.speed = v;
	}
	
	public Vector getVelocity(){
		return this.speed;
	}
	
	public void update(int delta){
		translate(speed.scale(delta));
	}
}
