package game;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

import jig.Collision;
import jig.ResourceManager;
import jig.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.EmptyTransition;
import org.newdawn.slick.state.transition.HorizontalSplitTransition;
import org.newdawn.slick.state.transition.RotateTransition;

/**
 * Game state involving a set of top-down scrolling maps, on which the player character
 * "Spike" moves freely, colliding with entities. If the player character collides with
 * a cat entity, the game transitions to a platforming combat stage.
 * 
 * @author Abel Hoxeng
 * @author Mitchel Pulley
 */
public class OverworldState extends BasicGameState {

	DogWarriors game; // game we are a state of
	GameContainer container; // gameContainer owned by the game
	
	private ArrayList<ArrayList<TownMap>> mapList; // list of all generated maps in-game
	private TownMap currentMap; // the map we are currently on/rendering
	
	private int mapIndexX = 0; // index of current map in the mapList\
	private int mapIndexY = 0;
	
	private int numMaps = 0; // number of maps generated
	
	private int numRenders = 0;
	private int numCDs = 0;
	
	private int randomX;
	private int randomY;
	
	// size of world map in pixels
	private final int mapWidth = TownMap.TILESIZE * TownMap.WIDTH;
	private final int mapHeight = TownMap.TILESIZE * TownMap.HEIGHT;
	
	// screen dimensions
	private float screenWidth;
	private float screenHeight;
	private float screenHalfWidth;
	private float screenHalfHeight;
	private Rectangle screen = new Rectangle();
	
	// offset of screen coordinate system from world coordinates. Will be made equal to the coordinates
	// of the player character minus half the screen size.
	private float offsetX;
	private float offsetY;
	
	// Objects in the current world
	private WorldDog dog; // player character sprite
	private WorldCat cat;
	private ArrayList<WorldCat> cats;
	private ArrayList<TownTile> grassTiles;
	private ArrayList<TownTile> roadTiles;
	private ArrayList<TownTile> exitTiles; // Special tiles that teleport the player when entered
	private ArrayList<Building> buildings; // Rectangular entities blocking dog movement
	private ArrayList<TownTile> walls;
	//private ArrayList<Fence> fences;
	//private ArrayList<Powerup> powerups;
	private ArrayList<TownTile> shrubs; // drawn above Spike
	
	private Random rand;
	private boolean hit = false;
	
	private int frontierX = 0;
	private int frontierY = 0;
	
	// minimap-related
	private boolean mapDisplay = false;
	private boolean flash = false;
	private int flashTimer = 0;
	private int flashPeriod = 25;
	
	public OverworldState(GameContainer container, StateBasedGame game) {
		// THIS SPACE INTENTIONALLY LEFT BLANK
	}

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		System.out.println("Initializing OVERWORLD state...");
		
		System.out.println("Loading Graphics...");
		for (String s : DogWarriors.worldImages) {
			ResourceManager.loadImage(s);
		}
		for (String s : DogWarriors.dogImages) {
			ResourceManager.loadImage(s);
		}
		for (String s : DogWarriors.wallImages) {
			ResourceManager.loadImage(s);
		}
		for (String s : DogWarriors.grassImages) {
			ResourceManager.loadImage(s);
		}
		for (String s : DogWarriors.roadImages) {
			ResourceManager.loadImage(s);
		}
		
		this.game = (DogWarriors) game;
		this.container = container;
		
		this.screenWidth = container.getWidth();
		this.screenHeight = container.getHeight();
		this.screenHalfWidth = screenWidth / 2.0f;
		this.screenHalfHeight = screenHeight / 2.0f;
		
		this.mapList = new ArrayList<ArrayList<TownMap>>();
		this.cats = new ArrayList<WorldCat>();
		this.grassTiles = new ArrayList<TownTile>();
		this.roadTiles = new ArrayList<TownTile>();
		this.exitTiles = new ArrayList<TownTile>();
		this.buildings = new ArrayList<Building>();
		this.walls = new ArrayList<TownTile>();
		//this.fences = new ArrayList<Fence>();
		//this.powerups = new ArrayList<Powerups>();
		this.shrubs = new ArrayList<TownTile>();
		
		this.rand = new Random();
        this.hit = false;
		
		TownMap m = new TownMap(14, 14, 14, 14, true, 0, 0, 0);
		m.highwayEW = true;
		m.highwayNS = true;
		ArrayList<TownMap> col0 = new ArrayList<TownMap>();
		col0.add(m);
		mapList.add(col0);
		this.numMaps = 1;
		
		this.changeLevel(0, 0);
		this.dog = new WorldDog(currentMap.dogSpawn.scale(TownMap.TILESIZE));
		WorldCat c = new WorldCat(250, 250);
        c.setVelocity(new Vector(.0f, .2f));
        cats.add(c);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		numRenders = 0;
		g.setColor(Color.white);
		
		for (TownTile t : this.grassTiles) {
			if (t.isOnscreen(screen)) {
				numRenders++;
				t.renderTile(offsetX, offsetY, g);
			}
		}
		
		for (TownTile e : this.exitTiles) {
			if (e.isOnscreen(screen)) {
				numRenders++;
				e.renderTile(offsetX, offsetY, g);
			}
		}
		
		for (TownTile r : this.roadTiles) {
			if (r.isOnscreen(screen)) {
				numRenders++;
				r.renderTile(offsetX, offsetY, g);
			}
		}
		
		dog.translate(-1.0f *offsetX, -1.0f *offsetY);
		dog.render(g);
		dog.translate(offsetX, offsetY);
		
		for (TownTile s : this.shrubs) {
			if (s.isOnscreen(screen)) {
				numRenders++;
				s.renderTile(offsetX, offsetY, g);
			}
		}
		
		for (TownTile w : this.walls) {
			if (w.isOnscreen(screen)) {
				numRenders++;
				w.renderTile(offsetX, offsetY, g);
			}
		}
		
		for (WorldCat c : this.cats) {
        	//if(c.isOnscreen(screen)){
        		numRenders++;
        		c.translate(-1*offsetX, -1*offsetY);
        		c.render(g);
        		c.translate(offsetX, offsetY);
        		
        	//}
        }
		
		g.drawString("Number of Renders: " + numRenders, 10, 30);
		g.drawString("Number of Collision Detections: " + numCDs, 10, 50);
		
		Vector realCoords = computeRealCoords(mapIndexX, mapIndexY);
		int realX = (int) realCoords.getX();
		int realY = (int) realCoords.getY();
		
		g.drawString("Map Coordinates: " + realX + ", " + realY, 10, 70);
		g.drawString("Maps Generated: " + numMaps, 10, 90);
		
		if (mapDisplay) drawMinimap(g, flash);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		flashTimer ++;
		if (flashTimer >= flashPeriod) {
			flash = !flash;
			flashTimer = 0;
		}
		
		// Input
		Input input = container.getInput();
		processKeyInput(input);
		
		// Movement
		dog.update(delta);
		for (WorldCat c : cats) {
        	c.update(delta);
        }
        
		// Collision Detection
		numCDs = 0;
		
		for (WorldCat cat : this.cats) {
			numCDs ++;
        	Collision d = cat.collides(dog);
        	if (d != null) {
        		game.enterState(DogWarriors.STATES_PLATFORM, new EmptyTransition(), new RotateTransition());
        	}    	
        }
		
		processWallCollisions();
		processCatCollisions();
		processExitCollisions();
		
		// Update screen coordinates to center on dog's position
		offsetX = dog.getX() - screenHalfWidth;
		offsetY = dog.getY() - screenHalfHeight;
		int w = TownMap.TILESIZE;
		this.screen.setBounds((int) Math.floor(offsetX - w), (int) Math.floor(offsetY - w),
							  (int) Math.ceil(screenWidth + 2.0 * w), (int) Math.ceil(screenHeight + 2.0 * w));
		
	}

	@Override
	public int getID() {
		return 3;
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException {
		container.getInput().clearKeyPressedRecord();
		switch((((DogWarriors) game).getPrevState())) {
		case(0): // the game is starting, and we need a new game
			break;
		case(1): // the game was un-paused, and we should do nothing
			break;
		case(2): // the game is returning from transition (To Be Defined)
			break;
		case(4): // the game is returning from platform world
			break;
		}
	}

	@Override
	public void leave(GameContainer container, StateBasedGame game) {
		container.getInput().clearKeyPressedRecord();
		((DogWarriors) game).setPrevState(this.getID());
	}
	
	/**
	 * Generates a new TownMap object, taking into account coordinates and neighboring maps
	 * @return a newly generated TownMap object
	 */
	private TownMap generateMap(int destX, int destY) {
		TownMap t = null;
		int wallFactor = 25;
		int n, e, s, w;
		int start = TownMap.EXIT_WIDTH + 1;
		int rangeX = TownMap.WIDTH - start + wallFactor;
		int rangeY = TownMap.HEIGHT - start + wallFactor;
		// get north border from south border of mapList[destX][destY-2]
		TownMap b = getNeighbor(destX, destY, 0);
		if (b != null) n = b.exitSlocation;
		else {
			//System.out.println("North neighbor null, generating new exit");
			n = start + (int) (Math.random() * rangeX);
		}
		
		// get east border from west border of mapList[destX+2][destY]
		b = getNeighbor(destX, destY, 1);
		if (b != null) e = b.exitWlocation;
		else {
			//System.out.println("East neighbor null, generating new exit");
			e = start + (int) (Math.random() * rangeY);
		}
		
		// get south border from north border of mapList[destX][destY-2]
		b = getNeighbor(destX, destY, 2);
		if (b != null) s = b.exitNlocation;
		else {
			//System.out.println("South neighbor null, generating new exit");
			s = start + (int) (Math.random() * rangeX);
		}
		
		// get west border from east border of mapList[destX-2][destY]
		b = getNeighbor(destX, destY, 3);
		if (b != null) w = b.exitElocation;
		else {
			//System.out.println("West neighbor null, generating new exit");
			w = start + (int) (Math.random() * rangeY);
		}
		
		// yes generate roads
		// generate random amount of bushes, buildings, cat spawns, etc..
		int generateShrubs = (int) (Math.random() * 2);
		int shrubs = 0;
		if (generateShrubs == 1) {
			shrubs = (int) (Math.random() * 100);
		}
		t = new TownMap(n, e, s, w, true, shrubs, 0, 0);
		numMaps ++;
		return t;
	}
	
	private void drawMinimap(Graphics g, boolean flash) {
		for (int i = 0; i <= frontierX; i++) {
			for (int j = 0; j <= frontierY; j++) {
				TownMap m = mapList.get(i).get(j);
				if (m == null) continue;
				Vector r = computeRealCoords(i, j);
				int rX = (int) r.getX();
				int rY = (int) r.getY();
				int point = 0x0020; // space
				if ((i == mapIndexX && j == mapIndexY) && (flash)) {
					point = 0x0040;
				} else {
					if (m.exitNorth) {
						//point = 0x2575; // north
						point = 0x006F;
						if (m.exitEast) {
							//point = 0x2514; // north and east
							point = 0x004C;
							if (m.exitWest) {
								//point = 0x2534; // north and east and west
								point = 0x005E;
								if (m.exitSouth) {
									//point = 0x253C; // north and east and west and south
									point = 0x002B;
								}
							} else if (m.exitSouth) {
								//point = 0x251C; // north and east and south
								point = 0x007D;
							}
						} else if (m.exitWest) {
							//point = 0x2518; // north and west
							point = 0x004A;
							if (m.exitSouth) {
								//point = 0x2524; // north and west and south
								point = 0x007B;
							}
						} else if (m.exitSouth) {
							//point = 0x2502; // north and south
							point = 0x007C;
						}
					} else if (m.exitEast) {
						//point = 0x2576; // east
						point = 0x006F;
						if (m.exitWest) {
							//point = 0x2500; // east and west
							point = 0x002D;
							if (m.exitSouth) {
								//point = 0x252C; // east and west and south
								point = 0x0076;
							}
						} else if (m.exitSouth) {
							//point = 0x250C; // east and south
							point = 0x0072;
						}
					} else if (m.exitWest) {
						//point = 0x2574; // west
						point = 0x006F;
						if (m.exitSouth) {
							//point = 0x2510; // west and south
							point = 0x0037;
						}
					} else if (m.exitSouth) {
						//point = 0x2577; // south
						point = 0x006F;
					}
				}
				g.drawString(Character.toString((char) point), screenHalfWidth + 10 * rX, screenHalfHeight + 200 + 12 * rY);
			}
		}
	}
	
	private TownMap getNeighbor(int x, int y, int dir) {
		TownMap t = null;
		int ix = 0;
		int iy = 0;
		switch(dir) {
		case(0): // get neighbor on north
			ix = x;
			if (y == 0) { iy = 1; }
			else if (y % 2 == 0) { iy = y - 2; }
			else { iy = y + 2; }
			break;
		case(1): // east
			if (x == 1) { ix = 0; }
			else if (x % 2 == 0) { ix = x + 2; }
			else { ix = x - 2; }
			iy = y;
			break;
		case(2): // south
			ix = x;
			if (y == 1) { iy = 0; }
			else if (y % 2 == 0) { iy = y + 2; }
			else { iy = y - 2; }
			break;
		case(3): // west
			if (x == 0) { ix = 1; }
			else if (x % 2 == 0) { ix = x - 2; }
			else { ix = x + 2; }
			iy = y;
			break;
		}
		//System.out.println("DestX = " + x + " DestY = " + y);
		//System.out.println("ix = " + ix + ", iy = " + iy);
		if (mapList.size() > ix) {
			if (mapList.get(ix).size() > iy) t = mapList.get(ix).get(iy);
		}
		return t;
	}
	
	/**
	 * Switches to the specified level in the mapList. Populates the
	 * new level with its objects, and places the dog in its appropriate
	 * position (differs depending on how the level is entered)
	 * 
	 * @param destX
	 * @param destY
	 */
	private void changeLevel(int destX, int destY) {
		// save information from previous level
		int outgoingX = mapIndexX;
		int outgoingY = mapIndexY;
		cats.clear();
		grassTiles.clear();
		exitTiles.clear();
		roadTiles.clear();
		buildings.clear();
		walls.clear();
		shrubs.clear();
		// switch to or generate new level
		if (mapList.get(destX).get(destY) == null) {
			TownMap t = generateMap(destX, destY);
			mapList.get(destX).set(destY, t);
			currentMap = t;
		} else {
			currentMap = mapList.get(destX).get(destY);
		}
		mapIndexX = destX;
		mapIndexY = destY;
		// generate objects
		for (int i = 0; i < TownMap.HEIGHT; i++) {
			for (int j = 0; j < TownMap.WIDTH; j++) {
				Vector p = new Vector(j, i).scale(TownMap.TILESIZE);
				int img = 0;
				switch (currentMap.tiledata[i][j]) {
				case (TownTile.GRASS):
					img = (int) (Math.random()*DogWarriors.grassImages.length);
					grassTiles.add(new TownTile(p, TownTile.GRASS, img));
					break;
				case (TownTile.EXIT_ROAD):
					img = (int) (Math.random()*DogWarriors.roadImages.length);
					TownTile e = new TownTile(p, TownTile.EXIT_ROAD, img);
					assignExitType(e, i, j);
					exitTiles.add(e);
					break;
				case (TownTile.EXIT_GRASS):
					img = (int) (Math.random()*DogWarriors.grassImages.length);
					TownTile g = new TownTile(p, TownTile.EXIT_GRASS, img);
					assignExitType(g, i, j);
					exitTiles.add(g);
					break;
				case (TownTile.WALL):
					img = (int) (Math.random()*DogWarriors.wallImages.length);
					walls.add(new TownTile(p, TownTile.WALL, img));
					break;
				case (TownTile.ROAD):
					img = (int) (Math.random()*DogWarriors.roadImages.length);
					roadTiles.add(new TownTile(p, TownTile.ROAD, img));
					break;
				case (TownTile.SHRUB):
					grassTiles.add(new TownTile(p, TownTile.GRASS, img));
					walls.add(new TownTile(p, TownTile.SHRUB, 7));
					break;
				}
			}
		}
	}
	
	private Vector computeRealCoords(int x, int y) {
		Vector v = new Vector(0, 0);
		if (x % 2 == 0) { v = v.setX(x / 2); }
		else {
			v = v.setX(-1 * ((x + 1) / 2));
		}
		if (y % 2 == 0) { v = v.setY(y / 2); }
		else {
			v = v.setY(-1 * ((y + 1) / 2));
		}
		return v;
	}
	
	/**
	 * Add "padding" to the map world structure to allow new maps to be generated and set.
	 * @param xAmount
	 * @param yAmount
	 */
	private void incrementFrontier(int xAmount, int yAmount) {
		frontierX += xAmount;
		frontierY += yAmount;
		if (xAmount == 0) { // increment y frontier
			for (ArrayList<TownMap> a : mapList) {
				for (int i = 0; i < yAmount; i++) {
					a.add(null);
				}
			}
		} else { // increment x frontier
			for (int i = 0; i < xAmount; i++) {
				ArrayList<TownMap> a = new ArrayList<TownMap>();
				mapList.add(a);
				for (int j = 0; j <= frontierY; j++) {
					a.add(null);
				}
			}
		}
	}
	
	private void assignExitType(TownTile e, int i, int j) {
		if (i == 0) {
			e.exitType = 0;
		} else if (i == TownMap.HEIGHT - 1) {
			e.exitType = 2;
		}
		if (j == 0) {
			e.exitType = 3;
		} else if (j == TownMap.WIDTH - 1) {
			e.exitType = 1;
		}
	}
	
	/**
	 * Do something based on what keys have been pressed.
	 */
	private void processKeyInput(Input input) {
		if (input.isKeyDown(DogWarriors.CONTROLS_RIGHT)) {
			dog.setVelocity(dog.getVelocity().add(new Vector(dog.getAcceleration(), 0.0f)));
		}
		
		if (input.isKeyDown(DogWarriors.CONTROLS_LEFT)) {
			dog.setVelocity(dog.getVelocity().add(new Vector(-1.0f*dog.getAcceleration(), 0.0f)));
		}
		
		if (input.isKeyDown(DogWarriors.CONTROLS_DOWN)) {
			dog.setVelocity(dog.getVelocity().add(new Vector(0.0f, dog.getAcceleration())));
		}
		
		if (input.isKeyDown(DogWarriors.CONTROLS_UP)) {
			dog.setVelocity(dog.getVelocity().add(new Vector(0.0f, -1.0f*dog.getAcceleration())));
		}
		
		if (input.isKeyPressed(DogWarriors.CONTROLS_CHEAT_1)) { // instantly enter platform mode
			game.enterState(DogWarriors.STATES_PLATFORM, new EmptyTransition(), new RotateTransition());
		}
		
		if (input.isKeyPressed(DogWarriors.CONTROLS_PAUSE)) { // pause the game
			game.enterState(DogWarriors.STATES_PAUSED, new EmptyTransition(), new HorizontalSplitTransition());
		}
		
		if (input.isKeyPressed(DogWarriors.CONTROLS_QUIT)) { // return to the startup state
			game.enterState(DogWarriors.STATES_STARTUP, new EmptyTransition(), new HorizontalSplitTransition());
		}
		
		if (input.isKeyPressed(DogWarriors.CONTROLS_MAP)) {
			mapDisplay = !mapDisplay;
		}
	}
	
	/**
	 * Check for collisions between the dog and nearby wall tiles.
	 */
	private void processWallCollisions() {
		// dog vs. wall collisions
		for (TownTile w : walls) {
			if (!(dog.isNear(w))) continue; // ignore walls that are too far away to collide.
			numCDs ++;
			Collision c = dog.collides(w);
			if (c != null) {
				Vector p = c.getMinPenetration();
				
				if (p.getX() < 0) { // move dog west
					dog.setVelocity(new Vector(0.0f, dog.getVelocity().getY()));
					float dx = dog.getCoarseGrainedMaxX() - w.getCoarseGrainedMinX();
					dog.translate(new Vector(-1.0f*dx, 0.0f));
					
				} else if (p.getX() > 0) { // move dog east
					dog.setVelocity(new Vector(0.0f, dog.getVelocity().getY()));
					float dx = w.getCoarseGrainedMaxX() - dog.getCoarseGrainedMinX();
					dog.translate(new Vector(dx, 0.0f));
				}
				
				if (p.getY() < 0) { // move dog north
					dog.setVelocity(new Vector(dog.getVelocity().getX(), 0.0f));
					float dy = dog.getCoarseGrainedMaxY() - w.getCoarseGrainedMinY();
					dog.translate(new Vector(0.0f, -1.0f*dy));
					
				} else if (p.getY() > 0) { // move dog south
					dog.setVelocity(new Vector(dog.getVelocity().getX(), 0.0f));
					float dy = w.getCoarseGrainedMaxY() - dog.getCoarseGrainedMinY();
					dog.translate(new Vector(0.0f, dy));
				}
			}
		}
	}
	
	/**
	 * Check for collisions between the dog and nearby exit tiles.
	 */
	private void processExitCollisions() {
		for (TownTile e : exitTiles) {
			if (!(dog.isNear(e))) continue; // ignore exits that are too far away to collide.
			numCDs ++;
			Collision c = dog.collides(e);
			if (c != null) {
				int destX = 0;
				int destY = 0;
				int n = 2; // number of frontier increments
				switch (e.exitType) {
				case(0): // this is a north exit
					destX = mapIndexX;
					if (mapIndexY == 0) { destY = 1; n = 1; }
					else if (mapIndexY % 2 == 1) { destY = mapIndexY + 2; }
					else { destY = mapIndexY - 2; }
					dog.setY((float) (mapHeight - 2.5 * TownMap.TILESIZE));
					if (destY > frontierY) incrementFrontier(0, n);
					break;
				case(1): // this is an east exit
					if (mapIndexX == 1) { destX = 0; n = 1;}
					else if (mapIndexX % 2 == 1) { destX = mapIndexX - 2; }
					else { destX = mapIndexX + 2; }
					destY = mapIndexY;
					dog.setX((float) (2.5 * TownMap.TILESIZE));
					if (destX > frontierX) incrementFrontier(n, 0);
					break;
				case(2): // this is a south exit
					destX = mapIndexX;
					if (mapIndexY == 1) { destY = 0; n = 1;}
					else if (mapIndexY % 2 == 1) { destY = mapIndexY - 2; }
					else { destY = mapIndexY + 2; }
					dog.setY((float) (2.5 * TownMap.TILESIZE));
					if (destY > frontierY) incrementFrontier(0, n);
					break;
				case(3): // this is a west exit
					if (mapIndexX == 0) { destX = 1; n = 1;}
					else if (mapIndexX % 2 == 1) { destX = mapIndexX + 2; }
					else { destX = mapIndexX - 2; }
					destY = mapIndexY;
					dog.setX((float) (mapWidth - 2.5 * TownMap.TILESIZE));
					if (destX > frontierX) incrementFrontier(n, 0);
					break;
				}
				changeLevel(destX, destY);
				break;
			}
		}
	}
	
	private void processCatCollisions() {
		for (WorldCat cat : this.cats) {
        	for (TownTile w : walls) {
        		Collision catCollide = cat.collides(w);
        		//randomX = rand.nextInt(90) - 10;
        		//randomY = rand.nextInt(10);
        		if (w.collides(cat) != null) {
        			Vector p = catCollide.getMinPenetration();
        			//System.out.println("We have a collision");
        			//System.out.println("The coordinates are x: " + w.getX() + " y: " + w.getY());
        			//Cat has made contact with right wall
        			if (p.getX() < 0) {
        				cat.setX(cat.getX() - 5);
        				cat.bounce(80);
        				//System.out.println("The coordinates are x: " + w.getX() + " y: " + w.getY());
        			}
        			//cat has made contact with left wall
        			else if (p.getX() > 0) {
        				cat.setX(cat.getX() + 5);
        				cat.bounce(80);
        				//System.out.println("The coordinates are x: " + w.getX() + " y: " + w.getY());
        			}
        			//cat has made contact with bottom of screen
        			else if (p.getY()  < 0) {
        				cat.setY(cat.getY() - 5);
        				cat.bounce(10);
        				//System.out.println("The coordinates are x: " + w.getX() + " y: " + w.getY());
        			}
        			//cat has top of screen
        			else if (p.getY()  > 0) {
        				cat.setY(cat.getY() + 5);
        				cat.bounce(10);
        				//System.out.println("The coordinates are x: " + w.getX() + " y: " + w.getY());
        			}
        		}
        	}
        }
	}
}
