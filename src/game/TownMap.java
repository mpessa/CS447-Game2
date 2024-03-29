package game;

import java.util.ArrayList;

import jig.Vector;

/**
 * A container for holding information about the individual maps in-game
 * 
 * @author Mitchel Pulley
 */
public class TownMap {
	
	public static final int TILESIZE = 32; // size in pixels of each tile in the world
	public static final int WIDTH = 30;
	public static final int HEIGHT = 30;
	public static final int EXIT_WIDTH = 2; // number of exit grass tiles adjoining an exit road tile on either side
	
	// road-building strategies
	public static final int ROADS_CW = 0; // extend exit roads straight until co-linear with next clockwise exit point
	public static final int ROADS_SQUARE = 1; // create a rectangle of roads, and link exits to this
	
	 // "dormant" data to be expanded by OverworldState loading the map.
	public Integer[][] tiledata = new Integer[TownMap.HEIGHT][TownMap.WIDTH];
	public int enemiesRemaining = 0; // True if all enemies have been cleared from this Map.
	public boolean itemGotten = false; // True if this room's 'Treasure' has been looted
	public boolean highwayEW = false; // Maps along coordinate axes are not dead ends.
	public boolean highwayNS = false;
	
	public boolean exitNorth = false; // True if these exits exist
	public boolean exitEast = false;
	public boolean exitSouth = false;
	public boolean exitWest = false;
	
	public int exitNlocation = 0;
	public int exitElocation = 0;
	public int exitSlocation = 0;
	public int exitWlocation = 0;
	
	public int worldX = 0; // Coordinates of this Map within the larger world
	public int worldY = 0;
	
	// Special points in the current world (in tile coordinates)
	public Vector dogSpawn; // spawn point of the dog
	
	/**
	 * Generates a new TownMap, with the specified exits.<br>
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
		dogSpawn = new Vector((int) (TownMap.WIDTH / 2), (int) (TownMap.HEIGHT / 2));
		
		generateBase();
		int exits = generateExits(north, east, south, west);
		if (roads) {
			if (exits == 4) {
				if (Math.random() > 0.8) {
					generateRoads(north, east, south, west, TownMap.ROADS_SQUARE);
				} else {
					generateRoads(north, east, south, west, TownMap.ROADS_CW);
				}
			} else {
				generateRoads(north, east, south, west, TownMap.ROADS_SQUARE);
			}
		}
		for (int i = 0; i < veg; i++) {
			generateVeg();
		}
		for (int i = 0; i < buildings; i++) {
			//generateBuilding();
		}
		for (int i = 0; i < cats; i++) {
			generateCat();
		}
	}
	
	/**
	 * Generate grass tiles and solid border for map.
	 */
	private void generateBase() {
		for (int j = 0; j < TownMap.WIDTH; j++) {
			tiledata[0][j] = TownTile.WALL;
			tiledata[TownMap.HEIGHT-1][j] = TownTile.WALL;
		}
		for (int i = 1; i < TownMap.HEIGHT-1; i++) {
			tiledata[i][0] = TownTile.WALL;
			for (int j = 1; j < TownMap.WIDTH-1; j++) {
				tiledata[i][j] = TownTile.GRASS;
			}
			tiledata[i][TownMap.WIDTH-1] = TownTile.WALL;
		}
	}
	
	/**
	 * Generate exit tiles as "holes in the wall" at the specified locations along the outer border.
	 * @param north - How far east (in tiles) is the northern exit?
	 * @param east - How far south (in tiles) is the eastern exit?
	 * @param south - How far east (in tiles) is the southern exit?
	 * @param west - How far south (in tiles) is the western exit?
	 * @return the number of exits created
	 */
	private int generateExits(int north, int east, int south, int west) {
		int x = 0;
		int e = TownMap.EXIT_WIDTH;
		if (north > e && north < TownMap.WIDTH - e - 1) {
			exitNorth = true;
			exitNlocation = north;
			x++;
			for (int i = 0; i < e; i++) tiledata[0][north - 1 - i] = TownTile.EXIT_GRASS;
			tiledata[0][north] = TownTile.EXIT_ROAD;
			for (int i = 0; i < e; i++) tiledata[0][north + 1 + i] = TownTile.EXIT_GRASS;
		}
		if (east > e && east < TownMap.HEIGHT - e - 1) {
			exitEast = true;
			exitElocation = east;
			x++;
			for (int i = 0; i < e; i++) tiledata[east - 1 - i][TownMap.WIDTH - 1] = TownTile.EXIT_GRASS;
			tiledata[east][TownMap.WIDTH-1] = TownTile.EXIT_ROAD;
			for (int i = 0; i < e; i++) tiledata[east + 1 + i][TownMap.WIDTH - 1] = TownTile.EXIT_GRASS;
		}
		if (south > e && south < TownMap.WIDTH - e - 1) {
			exitSouth = true;
			exitSlocation = south;
			x++;
			for (int i = 0; i < e; i++) tiledata[TownMap.HEIGHT-1][south - 1 - i] = TownTile.EXIT_GRASS;
			tiledata[TownMap.HEIGHT-1][south] = TownTile.EXIT_ROAD;
			for (int i = 0; i < e; i++) tiledata[TownMap.HEIGHT-1][south + 1 + i] = TownTile.EXIT_GRASS;
		}
		if (west > e && west < TownMap.HEIGHT - e - 1) {
			exitWest = true;
			exitWlocation = west;
			x++;
			for (int i = 0; i < e; i++) tiledata[west - 1 - i][0] = TownTile.EXIT_GRASS;
			tiledata[west][0] = TownTile.EXIT_ROAD;
			for (int i = 0; i < e; i++) tiledata[west + 1 + i][0] = TownTile.EXIT_GRASS;
		}
		return x;
	}
	
	/**
	 * Connects the two points on the map with a North-South road
	 * @param x
	 * @param y1
	 * @param y2
	 */
	private void NSRoad(int x, int y1, int y2) {
		for (int i = y1; i <= y2; i++) {
			tiledata[i][x] = TownTile.ROAD;
		}
	}
	
	/**
	 * Connects the two points on the map with a West-East road
	 * @param x1
	 * @param x2
	 * @param y
	 */
	private void WERoad(int x1, int x2, int y) {
		for (int i = x1; i <= x2; i++) {
			tiledata[y][i] = TownTile.ROAD;
		}
	}
	
	/**
	 * Generate road tiles connecting the exits on the map.
	 * @param north - How far east (in tiles) is the northern exit?
	 * @param east - How far south (in tiles) is the eastern exit?
	 * @param south - How far east (in tiles) is the southern exit?
	 * @param west - How far south (in tiles) is the western exit?
	 */
	private void generateRoads(int north, int east, int south, int west, int strategy) {
		switch (strategy) {
		case(TownMap.ROADS_CW): // Only use this when all exits exist!
			for (int i = 1; i < east; i++) {
				tiledata[i][north] = TownTile.ROAD;
			}
			for (int i = north; i < TownMap.WIDTH - 1; i++) {
				tiledata[east][i] = TownTile.ROAD;
			}
			for (int i = south; i < TownMap.WIDTH - 1; i++) {
				tiledata[east][i] = TownTile.ROAD;
			}
			for (int i = east + 1; i < TownMap.HEIGHT - 1; i++) {
				tiledata[i][south] = TownTile.ROAD;
			}
			for (int i = 1; i < south; i++) {
				tiledata[west][i] = TownTile.ROAD;
			}
			for (int i = 1; i < north; i++) {
				tiledata[west][i] = TownTile.ROAD;
			}
			break;
		case(TownMap.ROADS_SQUARE):
			int n = 2 + (int) (Math.random() * (TownMap.HEIGHT / 2 - 4));
			int s = TownMap.HEIGHT / 2 + (int) (Math.random() * (TownMap.HEIGHT / 2 - 2));
			int e = TownMap.WIDTH / 2 + (int) (Math.random() * (TownMap.WIDTH / 2 - 2));
			int w = 2 + (int) (Math.random() * (TownMap.WIDTH / 2 - 4));
			for (int j = w; j <= e; j++) {
				tiledata[n][j] = TownTile.ROAD;
				tiledata[s][j] = TownTile.ROAD;
			}
			for (int i = n+1; i < s; i++) {
				tiledata[i][w] = TownTile.ROAD;
				tiledata[i][e] = TownTile.ROAD;
			}
			if (exitNorth) {
				NSRoad(north, 1, n);
				if (north < w) WERoad(north, w, n);
				if (north > e) WERoad(e, north, n);
			}
			if (exitEast) {
				WERoad(e, TownMap.WIDTH-2, east);
				if (east < n) NSRoad(e, east, n);
				if (east > s) NSRoad(e, s, east);
			}
			if (exitSouth) {
				NSRoad(south, s, TownMap.HEIGHT-2);
				if (south < w) WERoad(south, w, s);
				if (south > e) WERoad(e, south, s);
			}
			if (exitWest) {
				WERoad(1, w, west);
				if (west < n) NSRoad(w, west, n);
				if (west > s) NSRoad(w, s, west);
			}
			break;
		}
	}

	/**
	 * Generate a "shrub" tile at a random location. Veggies have a higher probability
	 * of spawning near other veggies.
	 */
	private void generateVeg() {
		int tries = 16;
		while (tries > 0) {
			int x = 1 + (int) (Math.random() * (TownMap.WIDTH - 2));
			int y = 1 + (int) (Math.random() * (TownMap.HEIGHT - 2));
			double p = Math.random();
			float prob = 0.05f;
			if (tiledata[y+1][x] == TownTile.ROAD) break;
			if (tiledata[y+1][x] == TownTile.SHRUB) prob += 0.20f;
			if (tiledata[y-1][x] == TownTile.SHRUB) prob += 0.20f;
			if (tiledata[y][x+1] == TownTile.SHRUB) prob += 0.20f;
			if (tiledata[y][x-1] == TownTile.SHRUB) prob += 0.20f;
			if (p < prob) {
				if (tiledata[y][x] == TownTile.GRASS) {
					tiledata[y][x] = TownTile.SHRUB;
					double n = Math.random();
					double e = Math.random();
					double s = Math.random();
					double w = Math.random();
					float nprob = 0.8f;
					if (n < nprob && (tiledata[y+1][x] == TownTile.GRASS)) tiledata[y+1][x] = TownTile.SHRUB;
					if (e < nprob && (tiledata[y-1][x] == TownTile.GRASS)) tiledata[y-1][x] = TownTile.SHRUB;
					if (w < nprob && (tiledata[y][x+1] == TownTile.GRASS)) tiledata[y][x+1] = TownTile.SHRUB;
					if (s < nprob && (tiledata[y][x-1] == TownTile.GRASS)) tiledata[y][x-1] = TownTile.SHRUB;
					break;
				}
			}
		}
	}
	
	/**
	 * Generate a building of randomized dimensions at a suitable random location.
	 */
	private void generateBuilding() {
		
	}
	
	/**
	 * Generate a "cat spawning" location.
	 */
	private void generateCat() {
		int tries = 16;
		while (tries > 0) {
			int x = 1 + (int) (Math.random() * (TownMap.WIDTH - 2));
			int y = 1 + (int) (Math.random() * (TownMap.HEIGHT - 2));
			if (tiledata[y][x] == TownTile.GRASS) {
				tiledata[y][x] = TownTile.CAT_GRASS;
				enemiesRemaining++;
				break;
			}
		}
	}
}
