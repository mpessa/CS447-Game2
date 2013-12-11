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

/*
* Resources
* Splash sound provided by: Michel Baradari at http://opengameart.org/content/lava-splash
* Boom sound provided by: dKlon http://opengameart.org/content/atari-booms
* Kick sound for dog provided by: kddekadenz http://opengameart.org/content/break-pumpkin
* OverWorld items provided by: Jetrel, Daniel Cook, Zabin http://opengameart.org/content/2d-lost-garden-tileset-transition-to-jetrels-wood-tileset
* explosion provided by: qubodup http://opengameart.org/content/bomb-explosion-animation
* explosion provided by: DJ Chronos:http://www.freesound.org/people/DJ%20Chronos/sounds/123236/
* Bang class provided by: Dr. Wallace from bounce tutorial.
* cat fighter add on by: dogchicken http://opengameart.org/content/cat-fighter-addon1-energy-force-master-kit 
* cat fighter sprite sheet by: dogchicken http://opengameart.org/content/cat-fighter-sprite-sheet 
* cat fighter remix by: dogChicken http://opengameart.org/content/dog-fighter-cat-fighter-remix-base-add-on-one
* cloudy sky provided by: MiniBjorn http://opengameart.org/content/cloudy-sky
* slobber ball provided by: thomaswp http://opengameart.org/content/2d-object-pack
* fireballs provided by: bart http://opengameart.org/content/pixel-art-flame-icon
* possum provided by: Redshrike http://opengameart.org/content/the-awesome-possum-ultimate-smash-friends
* slobber potion provided by: Rafaelchm http://opengameart.org/content/potion-bottles 
* bone provided by: bart http://opengameart.org/content/pixel-art-femur 
* cat sounds provided by Mitch
* dog ouch sound provided by Dr Wallace
* dog bark provided by: panikko http://www.freesound.org/people/panikko/sounds/130030/
* intro music provided by: mikeUSA http://opengameart.org/content/exportbass2
* platform level music provided by: yd http://opengameart.org/content/oriented
* Overworld level music provided by: james_longley http://freesound.org/people/james_longley/sounds/172921/
* Win screen music provided by: Retimer http://opengameart.org/content/wgs-music-41-vs-tom-3-loops
* Thunder sound provided by: nicStage http://freesound.org/people/nicStage/sounds/64458/
* Doh sound provided by: n3b http://opengameart.org/content/doh
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
	
	public static final String[] possumImages = {
		rDir + "possumJump.png",
		rDir + "possumKickL.png",
		rDir + "possumKickR.png",
		rDir + "possumShootL.png",
		rDir + "possumShootR.png",
		rDir + "possumWalkL.png",
		rDir + "possumWalkR.png"
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
		rDir + "fistL.png",
		rDir + "fistR.png"
	};
	
	public static final String[] powerupImages = {
		rDir + "potion.png",
		rDir + "femur.png",
		rDir + "goldBone.png"
	};
	
	public static final String[] explosionImage = {
		rDir + "explosion.png"
	};
	
	// Images used in Overworld
	public static final String[] worldImages = {
		rDir + "worldDog.png",
		rDir + "ground800.png",
		rDir + "platform300.png",
		rDir + "sky2.jpg",
		rDir + "tower300x100.png",
		rDir + "tower300x200.png",
		rDir + "wood_tileset_3.png",
		rDir + "tree.png",
		rDir + "worldDogWalkL.png",
		rDir + "worldDogWalkR.png"
	};
	
	public static final String[] wallImages = {
		rDir + "wall_vines0.png",
		rDir + "wall_vines1.png",
		rDir + "wall_vines2.png",
		rDir + "wall_vines3.png",
		rDir + "wall_vines4.png",
		rDir + "wall_vines5.png",
		rDir + "wall_vines6.png"
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
		rDir + "grass_11.png"
	};
	
	public static final String[] roadImages = {
		rDir + "dirt0.png",
		rDir + "dirt1.png",
		rDir + "dirt2.png"
	};
	
	// Sounds
	public static final String[] platformCatSounds = {
		rDir + "Miffed.wav",
		rDir + "MiffedHigh.wav",
		rDir + "MiffedHighShort.wav",
		rDir + "MiffedLow.wav",
		rDir + "Cry.wav",
		rDir + "Meow1.wav",
		rDir + "Meow2.wav",
		rDir + "Mrow.wav",
		rDir + "No.wav",
		rDir + "Ooomh.wav",
		rDir + "Puff.wav",
		rDir + "Puff2.wav",
		rDir + "Trill.wav",
		rDir + "What.wav",
		rDir + "Whur.wav",
	};
	
	public static final String[] platformBossSound = {
		rDir + "doh_wav_cut.wav"
	};
	
	public static final String[] dogSounds = {
		rDir + "bark.wav"
	};
	
	public static final String[] platformSplashSound = {
		rDir + "splash.wav"
	};
	
	public static final String[] platformDogHitSound = {
		rDir + "ouch.wav"
	};
	
	public static final String[] platformBoomSound = {
		rDir + "atari_boom.wav",
		rDir + "atari_boom2.wav"
	};
	
	public static final String[] platformExplosion = {
		rDir + "explosion.wav"
	};
	
	public static final String[] platformDogKick = {
		rDir + "pumpkin_break_01_0.wav"
	};
	
	public static final String[] sounds = {
		rDir + "thunder.wav"
	};
	
	public static final String[] music = {
		rDir + "exportbass2.wav",
		rDir + "Oriented.wav",
		rDir + "over.wav",
		rDir + "win.wav"
	};
	
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
			+ "M - Toggle minimap display\n"
			+ "1 - Enter a cat battle\n"
			+ "ESC - Quit the game\n";
	
	public static final String platformControls = "CONTROLS:\n"
			+ "WASD - Move Spike\n"
			+ "P - Pause the game\n"
			+ "J - Shoot\n"
			+ "K - Kick\n"
			+ "L - Deploy Slime Shield\n"
			+ "1 - Instantly leave battle\n"
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
	public static final int CONTROLS_MAP = Input.KEY_M;
	public static final int CONTROLS_CHEAT_2 = Input.KEY_2;
	
	// States Mapping
	public static final int STATES_STARTUP = 0;
	public static final int STATES_PAUSED = 1;
	public static final int STATES_TRANSITION = 2;
	public static final int STATES_OVERWORLD = 3;
	public static final int STATES_PLATFORM = 4;
	public static final int STATES_BEGINNING = 5;
	public static final int STATES_GAMEOVER = 6;
	public static final int STATES_WIN = 7;
	
	// Game-level variables
	public static int ScreenHeight, ScreenWidth;
	public static int TileWidth = 32;
	
	private int prevState; // save the previous state
	public int currentState; // keep the current state
	
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
		this.addState(new BeginningState(container, this));
		this.addState(new GameOverState(container, this));
		this.addState(new WinState(container, this));
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
	
	public void setCurrentState(int p){
		this.currentState = p;
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
