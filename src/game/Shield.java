package game;

import jig.ConvexPolygon;
import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

/**
 * Shield used by enemy cats
 * 
 * @author Matthew Pessa
 *
 */
public class Shield extends Entity{
	
	public static final int numSides = 30;
	public int type;
		
	public Shield(final float x, final float y, int flavor) {
		super(x, y);
		this.type = flavor;
		
		if(type == 0){ // Fire shield for Cat
			this.addImage(ResourceManager.getImage(DogWarriors.battleImages[2]));
			this.addShape(new ConvexPolygon(22.0f, Shield.numSides),
						  new Vector(0.0f, 5.0f));
		}
	}
}