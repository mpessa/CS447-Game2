package game;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

public class WorldCat extends Entity {
	private Vector velocity;
	private int worldX = 30, worldY = 30;
	private int screenX, screenY;
	public WorldCat(final float x, final float y, final float vx, final float vy) throws SlickException{
		super(x,y);
		addImageWithBoundingBox(ResourceManager.getImage(DogWarriors.worldImages[0]));
	}
	public void setVelocity(final Vector v){
		this.velocity = v;
	}

	public Vector getVelocity(){
		return this.velocity;
	}
	
	public void render(final float sx, final float sy, Graphics g){
		this.setX(worldX - screenX);
		this.setY(worldY - screenY);
		System.out.println("x = " + this.getX() + ", y =" + this.getY());
		if(this.getX() > sx && this.getX() < sx + DogWarriors.ScreenWidth){
			if(this.getY() > sy && this.getY() < sy + DogWarriors.ScreenHeight){
				this.render(g);
			}
		}
	}

	public void update(final int delta, final float sx, final float sy) {
		screenX = (int)sx;
		screenY = (int)sy;
			
		//translate(velocity.scale(delta));
	}

}
