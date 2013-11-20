package game;

import org.newdawn.slick.Color;

import jig.ConvexPolygon;
import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

public class FireShield extends Entity{
	public boolean exists;
	
	public FireShield(final float x, final float y){
		super(x, y);
		exists = true;
		addImage(ResourceManager.getImage("resource/flame.png"));
		this.addShape(new ConvexPolygon(22f, 30), new Vector(0f, 5f), null, Color.black);
	}
}
