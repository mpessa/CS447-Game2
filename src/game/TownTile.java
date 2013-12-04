package game;

import java.awt.Point;
import java.awt.Rectangle;

import org.newdawn.slick.Graphics;

import jig.ConvexPolygon;
import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

/**
 * A basic unit of the environment. Contains fields and methods shared by all tiles in the game.
 * 
 * @author Mitchel Pulley
 */
public class TownTile extends Entity {

	// Tile Types
	public static final int GRASS = 0;
	public static final int WATER = 1;
	public static final int ROAD = 2;
	public static final int BUILDING = 3;
	public static final int FENCE = 4;
	public static final int SHRUB = 5;
	public static final int WALL = 6;
	public static final int EXIT_ROAD = 7;
	public static final int EXIT_GRASS = 8;
	// Types 32-63 reserved for different building anchor types?
	
	public Point NW, NE, SW, SE; // Corners of this Tile
	public int type;
	public int exitType; // optional field for exits, 0 = N, 1 = E, 2 = S, 3 = W
	
	/**
	 * Create a new Tile of the specified type at the specified Vector position.
	 * @param position - Position in World Coordinate pixels of this Tile
	 * @param type - Tile Type of this Tile (see above)
	 * @param index - Index into images array for this Tile (will be a random int)
	 */
	public TownTile(Vector position, int type, int index) {
		super(position);
		this.type = type;
		this.initialize(index);
	}

	/**
	 * Create a new Tile of the specified type at the specified coordinates.
	 * @param x - Position in World Coordinate pixels of this Tile
	 * @param y - Position in World Coordinate pixels of this Tile
	 * @param type - Tile Type of this Tile (see above)
	 * @param index - Index into images array for this Tile (will be a random int)
	 */
	public TownTile(float x, float y, int type, int index) {
		super(x, y);
		this.type = type;
		this.initialize(index);
	}
	
	/**
	 * Create the Tile's corners, and give it an image.
	 * @param type
	 */
	private void initialize(int index) {
		this.NW = new Point((int) this.getCoarseGrainedMinX(), (int) this.getCoarseGrainedMinY());
		this.NE = new Point((int) this.getCoarseGrainedMaxX(), (int) this.getCoarseGrainedMinY());
		this.SW = new Point((int) this.getCoarseGrainedMinX(), (int) this.getCoarseGrainedMaxY());
		this.SE = new Point((int) this.getCoarseGrainedMaxX(), (int) this.getCoarseGrainedMaxY());
		switch (this.type) {
		case (TownTile.GRASS):
			this.addImage(ResourceManager.getImage(DogWarriors.grassImages[index]));
			break;
		case (TownTile.WALL):
			this.addImageWithBoundingBox(ResourceManager.getImage(DogWarriors.wallImages[index]));
			break;
		case (TownTile.EXIT_ROAD):
			this.addImage(ResourceManager.getImage(DogWarriors.roadImages[index]));
			this.addShape(new ConvexPolygon(16.0f));
			break;
		case (TownTile.EXIT_GRASS):
			this.addImage(ResourceManager.getImage(DogWarriors.grassImages[index]));
			this.addShape(new ConvexPolygon(16.0f, 16.0f));
			break;
		case (TownTile.ROAD):
			this.addImage(ResourceManager.getImage(DogWarriors.roadImages[index]));
			break;
		case (TownTile.SHRUB):
			this.addImage(ResourceManager.getImage(DogWarriors.worldImages[index]), 
					new Vector(0.0f, -16.0f));
			this.addShape(new ConvexPolygon(16.0f, 16.0f));
			break;
		}
	}
	
	/**
	 * Test whether one of this Tile's corners falls within the specified Rectangle.
	 * @param screen - the Rectangle to test this Tile for containment in.
	 * @return true if at least one of this Tile's corners falls within the specified Rectangle, false
	 * otherwise.
	 */
	public boolean isOnscreen(Rectangle screen) {
		boolean onscreen = false;
		onscreen = (screen.contains(NW) || screen.contains(NE) || screen.contains(SW) || screen.contains(SE));
		return onscreen;
	}
	
	/**
	 * Draws this Tile in offset screen coordinates
	 * @param offsetX - Offset of screen coordinates from world coordinates
	 * @param offsetY - Offset of screen coordinates from world coordinates
	 * @param g - the Graphics context
	 */
	public void renderTile(float offsetX, float offsetY, Graphics g) {
		this.translate(-1.0f * offsetX, -1.0f * offsetY);
		this.render(g);
		this.translate(offsetX, offsetY);
	}

}
