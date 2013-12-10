package game;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.EmptyTransition;
import org.newdawn.slick.state.transition.VerticalSplitTransition;

public class BeginningState extends BasicGameState {

	DogWarriors game; // game we are a state of
	GameContainer container; // gameContainer owned by the game
	int timer = 0; // Timer to control animation
	
	public BeginningState(GameContainer container, StateBasedGame game) {
		// THIS SPACE INTENTIONALLY LEFT BLANK
	}

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		System.out.println("Initializing BEGINNING state...");
		
		this.game = (DogWarriors) game;
		this.container = container;
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		//g.setColor(Color.white);
		
		//g.drawString("DOG WARRIORS", (container.getWidth()/2) - 50, (container.getHeight()/2) - 10);
		//g.drawString(DogWarriors.authors, 10, (container.getHeight() - 230));

	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		
		Input input = container.getInput();
		processKeyInput(input);
	}

	@Override
	public int getID() {
		return 0;
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException {
		container.getInput().clearKeyPressedRecord();
	}

	@Override
	public void leave(GameContainer container, StateBasedGame game) {
		container.getInput().clearKeyPressedRecord();
		((DogWarriors) game).setPrevState(this.getID());
	}
	
	public void processKeyInput(Input input) {
		if (input.isKeyDown(Input.KEY_RETURN) && timer >= 3000) { // start the Overworld state
			game.enterState(DogWarriors.STATES_OVERWORLD, new EmptyTransition(), new VerticalSplitTransition());
			
		} else if (input.isKeyDown(Input.KEY_ESCAPE)) { // quit the game
			container.exit();
		}
	}
}
