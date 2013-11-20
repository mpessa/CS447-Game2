package game;

import org.newdawn.slick.Color;

import jig.ConvexPolygon;
import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

public class WaterShield extends Entity{
	public boolean exists;
	
	public WaterShield(final float x, final float y){
		super(x, y);
		exists = false;
		addImage(ResourceManager.getImage("resource/waterShield.png"));
		this.addShape(new ConvexPolygon(37.5f, 30), new Vector(0f, 0f), null, Color.black);
	}
}
