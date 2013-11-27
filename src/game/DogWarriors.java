package game;

import jig.Entity;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Main game class. Holds resource references, and binds together
 * the game states.<br>
 * <br>
 * <b><i> Is really fun to play. </i></b>
 * 
 * @author Abel Hoxeng
 * @author Matthew Pessa
 * @author Mitchel Pulley
 */
public class DogWarriors extends StateBasedGame {

	// Location of game resources
	public static final String rDir = "game/resource/";
	
	// Images used in Platform Game
	public static final String[] catImages = {
		rDir + "catDieL.png", // 0 
		rDir + "catDieLG.png", // 1 
		rDir + "catDieLR.png", // 2 
		rDir + "catDieLY.png", // 3 
		rDir + "catDieR.png", // 4
		rDir + "catDieRG.png", // 5 
		rDir + "catDieRR.png", // 6
		rDir + "catDieRY.png", // 7
		rDir + "catJump.png", // 8
		rDir + "catJumpG.png", // 9
		rDir + "catJumpR.png", // 10
		rDir + "catJumpY.png", // 11
		rDir + "catKickLG.png", // 12
		rDir + "catKickLR.png", // 13
		rDir + "catKickLY.png", // 14
		rDir + "catKickRG.png", // 15
		rDir + "catKickRR.png", // 16
		rDir + "catKickRY.png", // 17
		rDir + "catShootLR.png", // 18
		rDir + "catShootLY.png", // 19
		rDir + "catShootRR.png", // 20
		rDir + "catShootRY.png", // 21
		rDir + "catWalkL.png", // 22
		rDir + "catWalkLG.png", // 23
		rDir + "catWalkLR.png", // 24
		rDir + "catWalkLY.png", // 25
		rDir + "catWalkR.png", // 26
		rDir + "catWalkRG.png", // 27
		rDir + "catWalkRR.png", // 28
		rDir + "catWalkRY.png" // 29
	};
	
	public static final String[] dogImages = {
		rDir + "dogJump.png",
		rDir + "dogKick.png",
		rDir + "dogShootL.png",
		rDir + "dogShootR.png",
		rDir + "dogWalkL.png",
		rDir + "dogWalkR.png",
	};
	
	public static final String[] battleImages = {
		rDir + "fireballL.png",
		rDir + "fireballR.png",
		rDir + "flame.png",
		rDir + "waterball.png",
		rDir + "waterShield.png",
	};
	
	// Images used in Overworld
	public static final String[] worldImages = {
		rDir + "worldDog.png",
		rDir + "ground800.png",
		rDir + "platform300.png",
		rDir + "sky2.jpg",
		rDir + "tower300x100.png",
		rDir + "tower300x200.png",
		rDir + "wood_tileset_3.png"
	};
	
	public static final String[] wallImages = {
		rDir + "wall_vines0.png",
		rDir + "wall_vines1.png",
		rDir + "wall_vines2.png",
		rDir + "wall_vines3.png",
		rDir + "wall_vines4.png",
		rDir + "wall_vines5.png",
		rDir + "wall_vines6.png",
	};
	
	public static final String[] grassImages = {
		rDir + "grass_0.png",
		rDir + "grass_1.png",
		rDir + "grass_2.png",
		rDir + "grass_3.png",
		rDir + "grass_4.png",
		rDir + "grass_5.png",
		rDir + "grass_6.png",
		rDir + "grass_7.png",
		rDir + "grass_8.png",
		rDir + "grass_9.png",
		rDir + "grass_10.png",
		rDir + "grass_11.png",
	};
	
	// Sounds
	// There are no sounds yet. :(
	
	// Overworld Level Data
	public static final String[] worldLevels = {
		rDir + "demo.tmx"
	};
	
	// Text
	public static final String authors = "BY:\n"
			+ "Abel Hoxeng\n"
			+ "Matthew Pessa\n"
			+ "Mitchel Pulley";
	
	public static final String worldControls = "CONTROLS:\n"
			+ "WASD - Move Spike\n"
			+ "P - Pause the game\n"
			+ "ESC - Quit the game\n";
	
	public static final String platformControls = "CONTROLS:\n"
			+ "WASD - Move Spike\n"
			+ "P - Pause the game\n"
			+ "J - Shoot\n"
			+ "K - Kick\n"
			+ "L - Deploy Slime Shield\n"
			+ "ESC - Quit the game\n";
	
	// Controls Mapping
	public static final int CONTROLS_UP = Input.KEY_W;
	public static final int CONTROLS_DOWN = Input.KEY_S;
	public static final int CONTROLS_LEFT = Input.KEY_A;
	public static final int CONTROLS_RIGHT = Input.KEY_D;
	public static final int CONTROLS_PAUSE = Input.KEY_P;
	public static final int CONTROLS_SHOOT = Input.KEY_J;
	public static final int CONTROLS_KICK = Input.KEY_K;
	public static final int CONTROLS_SHIELD = Input.KEY_L;
	public static final int CONTROLS_CHEAT_1 = Input.KEY_1;
	public static final int CONTROLS_QUIT = Input.KEY_ESCAPE;
	public static final int CONTROLS_START = Input.KEY_RETURN;
	
	// States Mapping
	public static final int STATES_STARTUP = 0;
	public static final int STATES_PAUSED = 1;
	public static final int STATES_TRANSITION = 2;
	public static final int STATES_OVERWORLD = 3;
	public static final int STATES_PLATFORM = 4;
	
	// Game-level variables
	public static int ScreenHeight, ScreenWidth;
	public static int TileWidth = 32;
	
	private int prevState; // save the previous state
	
	//private Boolean newGameRequested = false;
	//private int hiScore = 0;
	
	public DogWarriors(String title, int width, int height) {
		super(title);
		ScreenHeight = height;
		ScreenWidth = width;

		Entity.setCoarseGrainedCollisionBoundary(Entity.AABB);
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		this.addState(new StartupState(container, this));
		this.addState(new PausedState(container, this));
		this.addState(new LevelTransitionState(container, this));
		this.addState(new OverworldState(container, this));
		this.addState(new PlatformState(container, this));
	}
	
	@Override
	protected void preRenderState(GameContainer container, Graphics g) throws SlickException {
		// NO-OP
	}
	
	@Override
	protected void postRenderState(GameContainer container, Graphics g) throws SlickException {
		// NO-OP
	}
	
	@Override
	protected void preUpdateState(GameContainer container, int delta) throws SlickException {
		// NO-OP
	}
	
	@Override
	protected void postUpdateState(GameContainer container, int delta) throws SlickException {
		// NO-OP
	}
	
	public void setPrevState(int p) {
		this.prevState = p;
	}
	
	public int getPrevState() {
		return this.prevState;
	}
	
	public static void main(String[] args) throws Exception{
		AppGameContainer app;
		try {
			app = new AppGameContainer(new DogWarriors("Dog Warrior: Spike's Revenge", 800, 700));
			app.setDisplayMode(800, 700, false);
			app.setVSync(true);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}

	}
	
}
