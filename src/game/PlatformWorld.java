package game;

import jig.ConvexPolygon;
import jig.Entity;
import jig.ResourceManager;

/**
 * Now what have we here? A class for holding other classes, I guess...
 * 
 * @author Matthew Pessa
 */
public class PlatformWorld extends Entity {
	
	public PlatformWorld() {
		// THIS SPACE POSSIBLY INTENTIONALLY LEFT BLANK
	}
	
	public class Background extends Entity {
		public Background(final float x, final float y) {
			super(x,y);
			this.addImage(ResourceManager.getImage(DogWarriors.worldImages[3]));
		}
	}
	
	public class Ground extends Entity {
		public Ground(int x, int y) {
			super(x,y);
			this.addImage(ResourceManager.getImage(DogWarriors.worldImages[1]));
			this.addShape(new ConvexPolygon(800f, 30f));
		}
	}
	
	public class Platform extends Entity {
		public Platform(int x, int y) {
			super(x, y);
			this.addImage(ResourceManager.getImage(DogWarriors.worldImages[2]));
			this.addShape(new ConvexPolygon(300f, 30f));
		}
	}
	
	public class Tower extends Entity{
		public Tower(int x, int y, int type){
			super(x, y);
			if (type == 0) {
				this.addImage(ResourceManager.getImage(DogWarriors.worldImages[4]));
			} else if (type == 1) {
				this.addImage(ResourceManager.getImage(DogWarriors.worldImages[5]));
			}
		}
	}
	
}
