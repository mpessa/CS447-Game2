package game;

import jig.ResourceManager;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
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
	private TiledMap worldMap; // a world map
	
	public final int tileWidth = 32;
	
	public final int worldWidth = 30; // size of world in tiles
	public final int worldHeight = 30;
	public int viewWidth; // size of viewport in world tiles
	public int viewHeight;
	
	private int charSpawnX = 15; // spawn point of character in world coordinates (tiles)
	private int charSpawnY = 11;
	
	private int shiftX; // offset from top left of screen to character (pixels)
	private int shiftY;
	private int charX; // current location of character in world coordinates (tiles)
	private int charY;
	
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
		this.worldMap = new TiledMap(DogWarriors.worldLevels[0], DogWarriors.rDir);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		int screenX = charX - shiftX/tileWidth;
		int screenY = charY - shiftY/tileWidth;
		worldMap.render(0, 0, screenX, screenY, viewWidth, viewHeight);
		dog.render(g);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		Input input = container.getInput();
		processKeyInput(input);
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
		
		int objectLayer = worldMap.getLayerIndex("object");
		
		if (input.isKeyPressed(DogWarriors.CONTROLS_RIGHT)) {
			if (charX < 29 && worldMap.getTileId(charX + 1, charY, objectLayer) == 0) {
				charX += 1;
			}
		}
		
		if (input.isKeyPressed(DogWarriors.CONTROLS_LEFT)) {
			if (charX > 0 && worldMap.getTileId(charX - 1, charY, objectLayer) == 0) {
				charX -= 1;
			}
		}
		
		if (input.isKeyPressed(DogWarriors.CONTROLS_DOWN)) {
			if (charY < 29 && worldMap.getTileId(charX, charY + 1, objectLayer) == 0) {
				charY += 1;
			}
		}
		
		if (input.isKeyPressed(DogWarriors.CONTROLS_UP)) {
			if (charY > 0 && worldMap.getTileId(charX, charY - 1, objectLayer) == 0) {
				charY -= 1;
			}
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
