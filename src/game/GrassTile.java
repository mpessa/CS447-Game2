package game;

import java.awt.Point;
import java.awt.Rectangle;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

/**
 * A grassy tile that can be walked upon.
 * 
 * @author Mitchel Pulley
 */
public class GrassTile extends Entity {

	public Point NW, NE, SW, SE;
	
	public GrassTile(Vector position, int img) {
		super(position);
		this.initialize(img);
	}

	public GrassTile(float x, float y, int img) {
		super(x, y);
		this.initialize(img);
	}
	
	private void initialize(int img) {
		this.addImageWithBoundingBox(ResourceManager.getImage(DogWarriors.grassImages[img]));
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
