package game;



import java.awt.Point;
import java.awt.Rectangle;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

/**
 * A solid, indestructible object that nothing can pass through
 * 
 * @author Mitchel Pulley
 */
public class Wall extends Entity {

	public Point NW, NE, SW, SE;
	
	/**
	 * Creates a new wall block at the specified position
	 * @param position
	 */
	public Wall(Vector position, int img) {
		super(position);
		this.initialize(img);
	}

	/**
	 * Creates a new wall block at the specified coordinates
	 * @param x
	 * @param y
	 */
	public Wall(float x, float y, int img) {
		super(x, y);
		this.initialize(img);
	}

	private void initialize(int img) {
		this.addImageWithBoundingBox(ResourceManager.getImage(DogWarriors.wallImages[img]));
		this.NW = new Point((int) this.getCoarseGrainedMinX(), (int) this.getCoarseGrainedMinY());
		this.NE = new Point((int) this.getCoarseGrainedMaxX(), (int) this.getCoarseGrainedMinY());
		this.SW = new Point((int) this.getCoarseGrainedMinX(), (int) this.getCoarseGrainedMaxY());
		this.SE = new Point((int) this.getCoarseGrainedMaxX(), (int) this.getCoarseGrainedMaxY());
	}
	
	public boolean isOnscreen(Rectangle screen) {
		boolean onscreen = false;
		onscreen = (screen.contains(NW) || screen.contains(NE) || screen.contains(SW) || screen.contains(SE));
		return onscreen;
	}
}
