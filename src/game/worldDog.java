package game;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

public class worldDog extends Entity{
	
	private Vector velocity;
	public int switchTimerA, switchTimerW, switchTimerS, switchTimerD;
	private int i,j;
	
	public worldDog(final float x, final float y, final float vx, final float vy){
		super(x,y);
		switchTimerS = 200;
		switchTimerA = 200;
		switchTimerW = 200;
		switchTimerD = 200;
		i = 0;
		
		addImageWithBoundingBox(ResourceManager.getImage("resource/j.png"));
		velocity = new Vector(0, 0);
		
	}
	
	public void setVelocity(final Vector v){
		velocity = v;
	}

	public Vector getVelocity(){
		return velocity;
	}

	public void update(final int delta) {
		translate(velocity.scale(delta));
	}

}
