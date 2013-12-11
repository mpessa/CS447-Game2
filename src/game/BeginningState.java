package game;

import java.util.ArrayList;

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
 * State with a beginning cut scene to add info about the basic game concept.
 * 
 * @author Matthew Pessa
 *
 */
public class BeginningState extends BasicGameState {

	DogWarriors game; // game we are a state of
	GameContainer container; // gameContainer owned by the game
	
	public PlatformWorld.Ground g1;
	public PlatformWorld world;
	private PlatformWorld.Background back;
	
	int timer = 0; // Timer to control animation
	int flash = 0;
	private int screenWidth, screenHeight;
	
	private Dog dog1;
	private Cat cat;
	private ArrayList<Cat> cats;
	private Powerup bone;
	
	public BeginningState(GameContainer container, StateBasedGame game) {
		// THIS SPACE INTENTIONALLY LEFT BLANK
	}

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		System.out.println("Initializing BEGINNING state...");
		
		System.out.println("Loading Graphics...");
		for (String s : DogWarriors.worldImages) {
			ResourceManager.loadImage(s);
		}
		
		for (String s : DogWarriors.dogSounds) {
			ResourceManager.loadSound(s);
		}
		
		for (String s : DogWarriors.music) {
			ResourceManager.loadSound(s);
		}
		
		this.game = (DogWarriors) game;
		this.container = container;
		
		this.screenWidth = DogWarriors.ScreenWidth;
		this.screenHeight = DogWarriors.ScreenHeight;
		
		this.world = new PlatformWorld();
		this.back = world.new Background(screenWidth / 2, screenHeight / 2);
		this.g1 = world.new Ground(screenWidth / 2, screenHeight - 20);
		this.bone = new Powerup(-140, screenHeight - 45, 2);
		this.dog1 = new Dog(-30, screenHeight - 60);
		this.cats = new ArrayList<Cat>();	

	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		g.setColor(Color.black);
		back.render(g);
		g1.render(g);
		for(Cat c : cats){
			c.walk.draw(c.getX() - 12, c.getY() - 12);
		}
		bone.render(g);
		dog1.walk.draw(dog1.getX() - 12, dog1.getY() - 12);
		if(timer < 150){
			g.drawString("Dog Warriors: Spike's Revenge", screenWidth / 3, (screenHeight / 3) - 30);
		}
		if(timer >= 50 && timer < 650){
			g.drawString("It was a calm time in Spike's village. He was a gentle dog that enjoyed " +
					"chewing", 50, screenHeight / 3);
			g.drawString("on his golden bone. This bone was the most precious bone in his village", 50,
					(screenHeight / 3) + 20);
			g.drawString("It was no ordinary bone. It was the Bone of Utter Deliciousness. All other", 50,
					(screenHeight / 3) + 40);
			g.drawString("bones paled in comparison. Spike guarded this treasure with his life.", 50,
					(screenHeight / 3) + 60);
		}
		if(timer > 675 && timer < 850){
			g.drawString("But during his nap one day....", screenWidth / 3, screenHeight / 3);
		}
		
		if(timer > 850 && timer < 1200){
			g.drawString("The evil ninja cats of the Kobra Kai Dojo stole the Bone! They wanted to", 50,
					screenHeight / 3);
			g.drawString("use the Bone to lure innocent puppies to their doom.", screenWidth / 5,
					(screenHeight / 3) + 20);
		}
		if(timer > 1220 && timer < 1800){
			g.drawString("Spike woke up and was appalled that he had let the Bone fall into the hands", 50,
					screenHeight / 3);
			g.drawString("of such evil. He gave chase. Now he has a new mission. Get his bone back!", 50,
					(screenHeight / 3) + 20);
		}
		if(timer > 1300 && flash < 30){
			g.drawString("Press Enter to hunt some cats", screenWidth / 3, screenHeight / 4);
		}

	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		
		Input input = container.getInput();
		processKeyInput(input);
	
		//if(!ResourceManager.getSound(DogWarriors.music[0]).playing()){
			//ResourceManager.getSound(DogWarriors.music[0]).play();
		//}
		
		if(timer == 50){
			cat = new Cat(screenWidth, screenHeight - 60, 1);
			cat.setVelocity(new Vector(-0.13f, 0f));
			cat.walk = cat.walkL;
			cats.add(cat);
		}
		if(timer == 60){
			cat = new Cat(screenWidth, screenHeight - 60, 2);
			cat.setVelocity(new Vector(-0.13f, 0f));
			cat.walk = cat.walkL;
			cats.add(cat);
		}
		if(timer == 70){
			cat = new Cat(screenWidth, screenHeight - 60, 3);
			cat.setVelocity(new Vector(-0.13f, 0f));
			cat.walk = cat.walkL;
			cats.add(cat);
		}
		if(timer == 80){
			cat = new Cat(screenWidth, screenHeight - 60, 4);
			cat.setVelocity(new Vector(-0.13f, 0f));
			cat.walk = cat.walkL;
			cats.add(cat);
		}
		
		if(timer == 500){
			for(Cat c : cats){
				c.setVelocity(new Vector(0f, 0f));
			}
		}
		
		if(timer == 625){
			bone.setVelocity(new Vector(0.18f, 0f));
			for(Cat c : cats){
				c.setVelocity(new Vector(0.18f, 0f));
				c.walk = c.walkR;
			}
		}
		
		if(timer == 1200){
			dog1.walk = dog1.walkR;
			dog1.setVelocity(new Vector(0.2f, 0f));
			ResourceManager.getSound(DogWarriors.dogSounds[0]).play();
		}
		if(timer == 1300 || timer == 1400){
			ResourceManager.getSound(DogWarriors.dogSounds[0]).play();
		}
		
		for(Cat c : cats){
			c.update(delta);
		}
		bone.update(delta);
		dog1.update(delta);
		
		timer++;
		flash++;
		if(flash > 60){
			flash = 0;
		}
	}

	@Override
	public int getID() {
		return 5;
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException {
		container.getInput().clearKeyPressedRecord();
		ResourceManager.getSound(DogWarriors.music[0]).play();
	}

	@Override
	public void leave(GameContainer container, StateBasedGame game) {
		container.getInput().clearKeyPressedRecord();
		((DogWarriors) game).setPrevState(this.getID());
		this.cats.clear();
		this.timer = 0;
		this.bone.setPosition(-140, screenHeight - 45);
		this.bone.setVelocity(new Vector(0f, 0f));
		this.dog1.setPosition(-30, screenHeight - 60);
		this.dog1.setVelocity(new Vector(0f, 0f));
	}
	
	public void processKeyInput(Input input) {
		if (input.isKeyDown(Input.KEY_RETURN) && timer >= 1000) { // start the Overworld state
			if(ResourceManager.getSound(DogWarriors.music[0]).playing()){
				ResourceManager.getSound(DogWarriors.music[0]).stop();
			}
			game.enterState(DogWarriors.STATES_OVERWORLD, new EmptyTransition(), new VerticalSplitTransition());
			
			
		}
		if (input.isKeyPressed(DogWarriors.CONTROLS_CHEAT_2)) { // Enter win state(for testing)
			game.enterState(DogWarriors.STATES_WIN, new EmptyTransition(), new RotateTransition());
		}
		else if (input.isKeyDown(Input.KEY_ESCAPE)) { // quit the game
			container.exit();
		}
	}
}
