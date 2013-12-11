package game;

import jig.ConvexPolygon;
import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

/**
 * Water shield used by player
 * @author Matthew Pessa
 *
 */
public class WaterShield extends Entity{
	
	public static final int numSides = 30;
	public boolean exists;
		
	public WaterShield(final float x, final float y) {
		super(x, y);
		this.exists = false;

		this.addImage(ResourceManager.getImage(DogWarriors.battleImages[4]));
		this.addShape(new ConvexPolygon(37.5f, Shield.numSides),
					  new Vector(0.0f, 0.0f));
	}
}
