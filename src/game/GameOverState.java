package game;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.EmptyTransition;
import org.newdawn.slick.state.transition.RotateTransition;

public class GameOverState extends BasicGameState {

	DogWarriors game; // game we are a state of
	GameContainer container; // gameContainer owned by the game
	
	private Dog dog1;
	
	int timer = 0; // Timer for the state
	private int screenWidth, screenHeight;
	
	public GameOverState(GameContainer container, StateBasedGame game) {
		// THIS SPACE INTENTIONALLY LEFT BLANK
	}

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		System.out.println("Initializing GAMEOVER state...");
		
		this.game = (DogWarriors) game;
		this.container = container;
		
		this.screenWidth = DogWarriors.ScreenWidth;
		this.screenHeight = DogWarriors.ScreenHeight;
		
		this.dog1 = new Dog(screenWidth / 2, screenHeight / 2);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		g.setColor(Color.white);
		
		dog1.jump.draw(dog1.getX() - 12, dog1.getY() - 12);
		g.drawString("GAME OVER", screenWidth / 2 - 20, screenHeight / 3);

	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {

		timer++;
		if(timer == 350){
			game.enterState(DogWarriors.STATES_STARTUP, new EmptyTransition(), new RotateTransition());
		}
	}

	@Override
	public int getID() {
		return 6;
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException {
		container.getInput().clearKeyPressedRecord();
	}

	@Override
	public void leave(GameContainer container, StateBasedGame game) {
		container.getInput().clearKeyPressedRecord();
		((DogWarriors) game).setPrevState(this.getID());
		this.timer = 0;
	}
	
	public void processKeyInput(Input input) {

	}
}
