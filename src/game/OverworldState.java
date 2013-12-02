package game;



import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

import jig.Collision;
import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

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
    
    private ArrayList<TownMap> mapList; // list of all generated maps in-game
    private TownMap currentMap; // the map we are currently on/rendering
    
    private int mapIndex = 0; // index of current map in the mapList
    private int numMaps = 0; // number of maps generated
    
    private int numRenders = 0;
    private int numCDs = 0;
    
    // size of world map in pixels
    private final int mapWidth = TownMap.TILESIZE * TownMap.WIDTH;
    private final int mapHeight = TownMap.TILESIZE * TownMap.HEIGHT;
    
    // screen dimensions
    private float screenWidth;
    private float screenHeight;
    private float screenHalfWidth;
    private float screenHalfHeight;
    private Rectangle screen = new Rectangle();
    private boolean collisionHappened;
    
    // offset of screen coordinate system from world coordinates. Will be made equal to the coordinates
    // of the player character minus half the screen size.
    private float offsetX;
    private float offsetY;
    
    // Objects in the current world
    private WorldDog dog; // player character sprite
    private WorldCat cat;
    private ArrayList<WorldCat> cats;
    private ArrayList<GrassTile> grassTiles;
    private ArrayList<Building> buildings; // Rectangular entities blocking dog movement
    private ArrayList<Wall> walls;
    private Random rand;
    private boolean  hit;
    //private ArrayList<Fence> fences;
    //private ArrayList<Powerup> powerups;
    //private ArrayList<Shrub> shrubs; // drawn above Spike
    
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
        for (String s : DogWarriors.wallImages) {
            ResourceManager.loadImage(s);
        }
        for (String s : DogWarriors.grassImages) {
            ResourceManager.loadImage(s);
        }
        
        this.game = (DogWarriors) game;
        this.container = container;
        
        this.screenWidth = container.getWidth();
        this.screenHeight = container.getHeight();
        this.screenHalfWidth = screenWidth / 2;
        this.screenHalfHeight = screenHeight / 2;
        
        this.mapList = new ArrayList<TownMap>();
        this.cats = new ArrayList<WorldCat>(6);
        this.grassTiles = new ArrayList<GrassTile>();
        this.buildings = new ArrayList<Building>();
        this.walls = new ArrayList<Wall>();
        this.collisionHappened = false;
        this.rand = new Random();
        this.hit = false;
        //cats.add(new WorldCat(50, 50));
        //cats.add(new WorldCat(100, 100));
        this.cat = new WorldCat(50, 50);
        cat.setVelocity(new Vector(.1f, .0f));
        cats.add(cat);
        this.cat = new WorldCat (100, 100);
        cat.setVelocity(new Vector(0.0f, -.1f));
        cats.add(cat);
       // for (WorldCat c : cats) {
       // 	c.setVelocity(new Vector(.1f, .0f));
        //}
        
        
        
        //this.fences = new ArrayList<Fence>();
        //this.powerups = new ArrayList<Powerups>();
        //this.shrubs = new ArrayList<Shrub>();
        
        TownMap m = new TownMap(4, 8, 12, 16, false, 0, 0, 0);
        mapList.add(m);
        this.numMaps = 1;
        
        this.changeLevel(0, 0);
        
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g)
            throws SlickException {
        // update offset to center on dog's position
        offsetX = dog.getX() - screenHalfWidth;
        offsetY = dog.getY() - screenHalfHeight;
        this.screen.setBounds((int) offsetX, (int) offsetY, (int) screenWidth, (int) screenHeight);
        
        numRenders = 0;
        
        for (GrassTile t : this.grassTiles) {
            if (t.isOnscreen(screen)) {
                numRenders++;
                t.translate(-1*offsetX, -1*offsetY);
                t.render(g);
                t.translate(offsetX, offsetY);
            }
        }
        
        dog.translate(-1*offsetX, -1*offsetY);
        dog.render(g);
        dog.translate(offsetX, offsetY);
                
        for (Wall w : this.walls) {
            if (w.isOnscreen(screen)) {
                numRenders++;
                w.translate(-1*offsetX, -1*offsetY);
                w.render(g);
                w.translate(offsetX, offsetY);
            }
        }
        
        for(WorldCat c : this.cats){
        	//if(c.isOnscreen(screen)){
        		numRenders++;
        		c.translate(-1*offsetX, -1*offsetY);
        		c.render(g);
        		c.translate(offsetX, offsetY);
        		
        	//}
        }
        
        g.drawString("Number of Renders: " + numRenders, 10, 30);
        g.drawString("Number of Collision Detections: " + numCDs, 10, 50);
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta)
            throws SlickException {
        Input input = container.getInput();
        processKeyInput(input);
        dog.update(delta);
        
        numCDs = 0;
        
        // dog vs. wall collisions
        for (Wall w : walls) {
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
        
        for(WorldCat c : cats){
        	c.update(delta);
        	//c.setVelocity(c.getVelocity().add(new Vector(.1f, .0f)));
        		//c.setVelocity(c.getVelocity());
        	//c.setVelocity(new Vector(.1f, 0f));
        	//c.setVelocity(new Vector());
        }
        
            
      
        for(WorldCat cat : this.cats){
        	Collision d = cat.collides(dog);
        	if(d != null){
        		game.enterState(DogWarriors.STATES_PLATFORM, new EmptyTransition(), new RotateTransition());
        	}    	
        }
        
        for(WorldCat cat : this.cats){
        	for(Wall w : walls){
        		Collision catCollide = cat.collides(w);
        		if(catCollide != null){	
        			Vector p = catCollide.getMinPenetration();
        			int random = rand.nextInt(4);
        			if(p.getX() < 0){
        			        				
        				if(random != 0){
        					System.out.println("The random number is " + random);
        					System.out.println("We have a collision with the cat and the wall");
        					cat.setX(cat.getX() - 10);
        					cat.setVelocity(new Vector(0.0f, cat.getVelocity().getY()));
        					cat.setVelocity(new Vector(randomVector(random)));
        				}
        			}
        			else if (p.getX() > 0) { 
        			
        				if(random != 1){
        					System.out.println("The random number is " + random);
        					System.out.println("We have a collision with the cat and the wall");
        					cat.setX(cat.getX() + 10);
        					cat.setVelocity(new Vector(0.0f, cat.getVelocity().getY()));
        					cat.setVelocity(new Vector(randomVector(random)));		
        				}       				
        			}
        			if (p.getY() < 0) { 
        				
        				if(random != 2){
        					System.out.println("The random number is " + random);
        					System.out.println("We have a collision with the cat and the wall");
        					cat.setY(cat.getY() - 10);
        					cat.setVelocity(new Vector(cat.getVelocity().getX(), 0.0f));
        					cat.setVelocity(new Vector(randomVector(random)));
        				}
                    } else if (p.getY() > 0 ) { 
                    	
                    	if(random != 3){
                    		System.out.println("The random number is " + random);
                    		System.out.println("We have a collision with the cat and the wall");
                    		cat.setY(cat.getY() + 10);
                    		cat.setVelocity(new Vector(cat.getVelocity().getX(), 0.0f));
                    		cat.setVelocity(new Vector(randomVector(random)));
                    	}
                    }
        		}
        		
        	}
        }
    }
    
    public Vector randomVector(int random){
    	Vector randomVector = null;
    	switch(random){
    	case 0:
    		randomVector = new Vector(.1f, .0f);
    		break;
    	case 1:
    		randomVector = new Vector(-.1f, .0f);
    		break;
    	case 2:
    		randomVector = new Vector(0.0f, .1f);
    		break;
    	case 3:
    		randomVector = new Vector(0.0f, -.1f);
    		break;
    	}
    	return randomVector;
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
     * Switches to the specified level in the mapList. Populates the
     * new level with its objects, and places the dog in its appropriate
     * position (differs depending on how the level is entered)
     * 
     * @param level - index in mapList to load
     * @param entryDir - 0 if this is the first call (we need to generate a dog), 1-4 = N, E, S, W
     */
    private void changeLevel(int level, int entryDir) {
        // save information from previous level
        int outgoing = mapIndex;
        // switch to the new level
        mapIndex = level;
        currentMap = mapList.get(mapIndex);
        // generate objects
        for (int i = 0; i < TownMap.HEIGHT; i++) {
            for (int j = 0; j < TownMap.WIDTH; j++) {
                Vector p = new Vector(j, i).scale(TownMap.TILESIZE);
                int img = 0;
                switch (currentMap.tiledata[i][j]) {
                case (TownMap.GRASS):
                    img = (int) (Math.random()*DogWarriors.grassImages.length);
                    grassTiles.add(new GrassTile(p, img));
                    break;
                case (TownMap.WALL):
                    img = (int) (Math.random()*DogWarriors.wallImages.length);
                    walls.add(new Wall(p, img));
                    break;
                }
            }
        }
        // replace or generate the dog
        switch (entryDir) {
        case (1): // dog is entering the map from the North
            dog.setY((float) (1.5 * TownMap.TILESIZE)); 
            break;
        case (2): // dog is entering the map from the East
            dog.setX((float) (mapWidth - 1.5 * TownMap.TILESIZE));
            break;
        case (3): // dog is entering the map from the South
            dog.setY((float) (mapHeight - 1.5 * TownMap.TILESIZE));
            break;
        case (4): // dog is entering the map from the West
            dog.setX((float) (1.5 * TownMap.TILESIZE));
            break;
        default: // dog is respawning at the map's spawn point
            this.dog = new WorldDog(currentMap.dogSpawn.scale(TownMap.TILESIZE));
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
        
        if(input.isKeyPressed(DogWarriors.CONTROLS_QUIT)) { // return to the startup state
            game.enterState(DogWarriors.STATES_STARTUP, new EmptyTransition(), new HorizontalSplitTransition());
        }
    }
}