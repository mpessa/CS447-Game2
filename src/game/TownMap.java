package game;

import java.util.ArrayList;

import jig.Vector;

/**
 * A container for holding information about the individual maps in-game
 * 
 * @author Mitchel Pulley
 */
public class TownMap {

	// Tile Types
	public static final int GRASS = 0;
	public static final int WATER = 1;
	public static final int ROAD = 2;
	public static final int BUILDING = 3;
	public static final int FENCE = 4;
	public static final int SHRUB = 5;
	public static final int WALL = 6;
	// Types 32-63 reserved for different building anchor types
	
	public static final int TILESIZE = 32; // size in pixels of each tile in the world
	public static final int WIDTH = 30;
	public static final int HEIGHT = 30;

	 // "dormant" data to be expanded by OverworldState loading the map.
	public Integer[][] tiledata = new Integer[TownMap.HEIGHT][TownMap.WIDTH];
	
	// Special points in the current world (in tile coordinates)
	public ArrayList<Vector> catSpawns; // spawn-points of CAT "Combative-Action Trigger" Objects
	public Vector dogSpawn; // spawn point of the dog
	
	/**
	 * Generates a new TownMap, with the specified exits.<br>
	 * Exit parameters must be between 1 and 28, inclusive, else no exit will be created.
	 * @param north - How far east (in tiles) is the northern exit?
	 * @param east - How far south (in tiles) is the eastern exit?
	 * @param south - How far east (in tiles) is the southern exit?
	 * @param west - How far south (in tiles) is the western exit?
	 * @param roads - Generate roads? (won't block movement)
	 * @param veg - Generate this many trees/shrubs (render over characters, won't block movement)
	 * @param structures - Generate this many buildings (will block movement)
	 * @param cats - Generate this many cat spawn-points
	 */
	public TownMap(int north, int east, int south, int west,
				   boolean roads, int veg, int buildings, int cats) {
		// generate grass tiles and solid border for entire map
		for (int j = 0; j < TownMap.WIDTH; j++) {
			tiledata[0][j] = TownMap.WALL;
			tiledata[TownMap.HEIGHT-1][j] = TownMap.WALL;
		}
		for (int i = 1; i < TownMap.HEIGHT-1; i++) {
			tiledata[i][0] = TownMap.WALL;
			for (int j = 1; j < TownMap.WIDTH-1; j++) {
				tiledata[i][j] = TownMap.GRASS;
			}
			tiledata[i][TownMap.WIDTH-1] = TownMap.WALL;
		}
		
		catSpawns = new ArrayList<Vector>();
		dogSpawn = new Vector((int) (TownMap.WIDTH / 2), (int) (TownMap.HEIGHT / 2));
	}

}
