package game;

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
import org.newdawn.slick.state.transition.RotateTransition;
import org.newdawn.slick.state.transition.VerticalSplitTransition;

/**
 * State containing a cut scene after the player has beaten the game.
 * 
 * @author Matthew Pessa
 *
 */
public class WinState extends BasicGameState {

	DogWarriors game; // game we are a state of
	GameContainer container; // gameContainer owned by the game
	
	int timer = 0; // Timer for animation
	int screenWidth, screenHeight;
	
	private Dog wDog;
	private Powerup bone;
	
	public PlatformWorld.Ground g1;
	public PlatformWorld world;
	private PlatformWorld.Background back;
	
	
	public WinState(GameContainer container, StateBasedGame game) {
		// THIS SPACE INTENTIONALLY LEFT BLANK
	}

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		System.out.println("Initializing WIN state...");
		
		for(String s : DogWarriors.music){
			ResourceManager.loadSound(s);
		}
		
		for(String s : DogWarriors.sounds){
			ResourceManager.loadSound(s);
		}
		
		this.game = (DogWarriors) game;
		this.container = container;
		
		this.screenWidth = DogWarriors.ScreenWidth;
		this.screenHeight = DogWarriors.ScreenHeight;
		
		this.world = new PlatformWorld();
		this.back = world.new Background(screenWidth / 2, screenHeight / 2);
		this.g1 = world.new Ground(screenWidth / 2, screenHeight - 20);
		this.wDog = new Dog(screenWidth + 40, screenHeight - 60);
		this.bone = new Powerup(screenWidth + 40, screenHeight - 45, 2);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		g.setColor(Color.black);
		back.render(g);
		g1.render(g);
		
		wDog.walk.draw(wDog.getX() - 12, wDog.getY() - 12);
		bone.render(g);
		g.drawString("Congratulations!", screenWidth / 3 + 40, screenHeight / 3);

		if(timer > 250 && timer < 600){
			g.drawString("You have won the most epic of games", screenWidth / 3, screenHeight / 2);
			g.drawString("And retrieved the Bone of Utter Deliciousness", screenWidth / 4, (screenHeight / 2) + 20);
		}
		
		if(timer > 600){
			g.drawString("Or have you?", screenWidth / 3, screenHeight / 2);
		}
		
		if(timer > 700){
			g.drawString("Stay tuned for the continuing fate of Spike", screenWidth / 4, (screenHeight / 2) + 30);
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		
		Input input = container.getInput();
		processKeyInput(input);
		
		if(timer == 0){
			this.wDog.walk = this.wDog.walkL;
			wDog.setVelocity(new Vector(-0.1f, 0f));
			bone.setVelocity(new Vector(-0.1f, 0f));
		}
		
		if(timer == 250){
			wDog.setVelocity(new Vector(0f, 0f));
			bone.setVelocity(new Vector(0f, 0f));
			wDog.walk.stop();
		}
		
		if(timer == 550){
			ResourceManager.getSound(DogWarriors.sounds[0]).play();
		}
		if(timer == 610){
			bone.setPosition(-100, 0);
		}
		
		if(timer == 1000){
			game.enterState(DogWarriors.STATES_STARTUP, new EmptyTransition(), new RotateTransition());
		}
		wDog.update(delta);
		bone.update(delta);
		timer++;
	}

	@Override
	public int getID() {
		return 7;
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException {
		container.getInput().clearKeyPressedRecord();
		ResourceManager.getSound(DogWarriors.music[3]).play();
		timer = 0;
	}

	@Override
	public void leave(GameContainer container, StateBasedGame game) {
		container.getInput().clearKeyPressedRecord();
		((DogWarriors) game).setPrevState(this.getID());
	}
	
	public void processKeyInput(Input input) {
		if (input.isKeyDown(Input.KEY_RETURN)) { // start the Beginning state
			game.enterState(DogWarriors.STATES_BEGINNING, new EmptyTransition(), new VerticalSplitTransition());
			
		} else if (input.isKeyDown(Input.KEY_ESCAPE)) { // quit the game
			container.exit();
		}
	}
}
