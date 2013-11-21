package game;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

public class WorldDog extends Entity{
	
	private Vector velocity;
	public int switchTimerA, switchTimerW, switchTimerS, switchTimerD;
	
	public WorldDog(final float x, final float y, final float vx, final float vy){
		super(x,y);
		
		this.switchTimerS = 200;
		this.switchTimerA = 200;
		this.switchTimerW = 200;
		this.switchTimerD = 200;
		this.velocity = new Vector(0.0f, 0.0f);
		
		this.addImageWithBoundingBox(ResourceManager.getImage(DogWarriors.worldImages[0]));	
	}
	
	public void setVelocity(final Vector v){
		this.velocity = v;
	}

	public Vector getVelocity(){
		return this.velocity;
	}

	public void update(final int delta) {
		translate(velocity.scale(delta));
	}

}
