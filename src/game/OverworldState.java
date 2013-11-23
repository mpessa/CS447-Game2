package game;



import java.util.ArrayList;
import java.util.Iterator;

import jig.ResourceManager;
import jig.Vector;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.EmptyTransition;
import org.newdawn.slick.state.transition.HorizontalSplitTransition;
import org.newdawn.slick.state.transition.RotateTransition;
import org.newdawn.slick.tiled.TiledMap;

public class OverworldState extends BasicGameState {

	DogWarriors game; // game we are a state of
	GameContainer container; // gameContainer owned by the game
	
	public WorldDog dog; // player character sprite
	public ArrayList<WorldCat> cat;
	private Image worldMap; // a world map
	
	public final float tileWidth = 32;
	
	public final float worldWidth = 30; // size of world in tiles
	public final float worldHeight = 30;
	public float viewWidth; // size of viewport in world tiles
	public float viewHeight;
	//private static ArrayList<WorldCat> cats;
	
	private float charSpawnX = 15*tileWidth; // spawn point of character in world coordinates (tiles)
	private float charSpawnY = 11*tileWidth;
	
	private float shiftX = DogWarriors.ScreenWidth/2.0f; // offset from top left of screen to character (pixels)
	private float shiftY = DogWarriors.ScreenHeight/2.0f;
	private float charX; // current location of character in world coordinates (tiles)
	private float charY;
	
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

		this.viewWidth = (int) Math.ceil(DogWarriors.ScreenWidth / DogWarriors.TileWidth);
		this.viewHeight = (int) Math.ceil(DogWarriors.ScreenHeight / DogWarriors.TileWidth);
		
		this.game = (DogWarriors) game;
		this.container = container;

		int t = DogWarriors.TileWidth;
		this.shiftX = t * ((DogWarriors.ScreenWidth/2) / t);
		this.shiftY = t * ((DogWarriors.ScreenHeight/2) / t);
		this.charX = this.charSpawnX;
		this.charY = this.charSpawnY;
		
		this.dog = new WorldDog(shiftX + t/2, shiftY + t/2, 0.0f, 0.0f);
		cat = new ArrayList<WorldCat>(1);
		cat.add(new WorldCat(0, 0, 0.0f, 0.0f));
		this.worldMap = new Image("game/resource/demo.png");
		//this.worldMap = new TiledMap(DogWarriors.worldLevels[0], DogWarriors.rDir);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		//the start coordinates of where the world gets drawn in relation to the screen.
		float screenX = charX - shiftX;
		float screenY = charY - shiftY;
		
		//worldMap.render(0, 0, (int)screenX, (int)screenY, (int)viewWidth, (int)viewHeight);
		//where the actual 0,0 coordinates are for the world.
		worldMap.draw(-screenX, -screenY);//, DogWarriors.ScreenWidth, DogWarriors.ScreenHeight);
		//Sets the the dog in the center of the visible window.
		dog.setX(shiftX);
		dog.setY(shiftY);
		dog.render(g);
		for(Iterator<WorldCat> i = cat.iterator(); i.hasNext();){
			WorldCat tempCat = i.next();
			tempCat.render(charX - shiftX, charY - shiftY, g); 
			
		}
		
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		Input input = container.getInput();
		processKeyInput(input);
		for(Iterator<WorldCat> i = cat.iterator(); i.hasNext();){
			WorldCat tempCat = i.next();
			tempCat.update(delta, charX - shiftX, charY - shiftY);
		}
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
	 * Do something based on what keys have been pressed.
	 */
	private void processKeyInput(Input input) {
		
		//int objectLayer = worldMap.getLayerIndex("object");
		
		if (input.isKeyDown(DogWarriors.CONTROLS_RIGHT)) {
			if (charX < worldWidth*tileWidth - dog.getCoarseGrainedWidth()/2){// && worldMap.getTileId((int)(charX/tileWidth) + 1, (int)(charY/tileWidth), objectLayer) == 0) {
				if(worldWidth*32 <= (charX - shiftX) + DogWarriors.ScreenWidth){
					shiftX += 1;
				}
				if(shiftX < DogWarriors.ScreenWidth/2){
					shiftX +=1;
				}
				charX += 1;
				//shiftX++;
				//if(shiftX - DogWarriors.ScreenWidth/2.0 > 3*tileWidth)
					//shiftX -= 3*tileWidth;
			//}
			}
		}
		
		if (input.isKeyDown(DogWarriors.CONTROLS_LEFT)) {
			if (charX > 0 + dog.getCoarseGrainedWidth()/2 ){
				// && worldMap.getTileId((int)(charX/tileWidth) - 1, (int)(charY/tileWidth), objectLayer) == 0) {
				if(0 >= (charX - shiftX)){
					shiftX -=1;
				}
				if(shiftX > DogWarriors.ScreenWidth/2){
					shiftX -= 1;
				}
				charX -= 1;
			}
		
				//shiftX--;
				//if(shiftX - DogWarriors.ScreenWidth/2.0 < -3*tileWidth)
					//shiftX += 3*tileWidth;
			//}
		}
		
		if (input.isKeyDown(DogWarriors.CONTROLS_DOWN)) {
			if (charY < worldHeight*tileWidth - dog.getCoarseGrainedWidth()/2){// && worldMap.getTileId((int)(charX/tileWidth), (int)(charY/tileWidth) + 1, objectLayer) == 0) {
				//check to ensure that the screen does not fall of the face of the world. 
				if(worldHeight*32 <= (charY - shiftY) + DogWarriors.ScreenHeight){
					shiftY += 1;
				}
				//Puts the character back into the center of the screen.
				if(shiftY < DogWarriors.ScreenHeight/2){
					shiftY +=1;
				}
				charY += 1;
			
			
				//shiftY++;
				//if(shiftY - DogWarriors.ScreenHeight/2.0 > 3*tileWidth)
					//shiftY -= 3*tileWidth;
			//}
			}
		}
		
		if (input.isKeyDown(DogWarriors.CONTROLS_UP)) {
			if (charY > 0 + dog.getCoarseGrainedWidth()/2){// && worldMap.getTileId((int)(charX/tileWidth), (int)(charY/tileWidth) - 1, objectLayer) == 0) {
				if(0 >= (charY - shiftY)){
					shiftY -= 1;
				}
				if(shiftY > DogWarriors.ScreenHeight/2){
					shiftY -= 1;
				}
				charY -= 1;
			}
				//shiftY--;
				//if(shiftY - DogWarriors.ScreenHeight/2.0 < -3*tileWidth)
					//shiftY += 3*tileWidth;
			//}
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
	private WorldDog getWorldDog() {
		// TODO Auto-generated method stub
		return dog;
	}

	//Add a certain amount of distance on each updated to add to players current position
	private float LERP(float start, float finish, float transition){
		return start + (finish - start) * transition;
	}

}
