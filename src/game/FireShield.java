package game;

import org.newdawn.slick.Color;

import jig.ConvexPolygon;
import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

public class FireShield extends Entity{
	
	public static final Color defaultColor = Color.black;
	public static final float defaultRadius = 22.0f;
	public static final int numSides = 30;
	
	public static float radius = defaultRadius;
	public static Color color = defaultColor;
	
	public boolean exists;
	
	// refactor this into a Shield class, with different types!
	public FireShield(final float x, final float y) {
		super(x, y);
		
		this.exists = true;
		
		this.addImage(ResourceManager.getImage(DogWarriors.battleImages[2]));
		this.addShape(new ConvexPolygon(FireShield.radius, FireShield.numSides),
					  new Vector(0.0f, 5.0f), null, FireShield.color);
	}
}
