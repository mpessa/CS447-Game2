package game;

import org.newdawn.slick.Color;

import jig.ConvexPolygon;
import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

public class WaterShield extends Entity{
	
	public static final Color defaultColor = Color.black;
	public static final float defaultRadius = 37.5f;
	public static final int numSides = 30;
	
	public static float radius = defaultRadius;
	public static Color color = defaultColor;
	
	public boolean exists;
	
	// refactor this into a Shield class, with different types!
	public WaterShield(final float x, final float y) {
		super(x, y);
		
		this.exists = false;
		
		this.addImage(ResourceManager.getImage(DogWarriors.battleImages[4]));
		this.addShape(new ConvexPolygon(WaterShield.radius, WaterShield.numSides),
					  new Vector(0.0f, 0.0f), null, WaterShield.color);
	}
}
