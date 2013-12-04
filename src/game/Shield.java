package game;

import org.newdawn.slick.Color;

import jig.ConvexPolygon;
import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

public class Shield extends Entity{
	
	public static final Color defaultColor = Color.black;
	public static final int numSides = 30;
	public int type;
	
	public static Color color = defaultColor;
	
	//public boolean exists;
	
	public Shield(final float x, final float y, int flavor) {
		super(x, y);
		this.type = flavor;
		//this.exists = true;
		
		if(type == 0){ // Fire shield for Cat
			this.addImage(ResourceManager.getImage(DogWarriors.battleImages[2]));
			this.addShape(new ConvexPolygon(22.0f, Shield.numSides),
						  new Vector(0.0f, 5.0f), null, Shield.color);
		}
		if(type == 1){ // Water shield for Dog
			this.addImage(ResourceManager.getImage(DogWarriors.battleImages[4]));
			this.addShape(new ConvexPolygon(37.5f, Shield.numSides),
					  new Vector(0.0f, 0.0f), null, Shield.color);
		}
	}
}