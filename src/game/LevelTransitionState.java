package game;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.EmptyTransition;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import org.newdawn.slick.state.transition.VerticalSplitTransition;

public class LevelTransitionState extends BasicGameState {

	DogWarriors game;
	GameContainer container;
	
	public LevelTransitionState(GameContainer container, StateBasedGame game) {
		// THIS SPACE INTENTIONALLY LEFT BLANK
	}

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		System.out.println("Initializing TRANSITION state...");
		
		this.game = (DogWarriors) game;
		this.container = container;

	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		
		g.drawString("Returning to Overworld...", (container.getWidth()/2) - 50, (container.getHeight()/2) - 10);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		
		Input input = container.getInput();
		processKeyInput(input);
	}

	@Override
	public int getID() {
		return 2;
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
		if (input.isKeyDown(DogWarriors.CONTROLS_START)) { // continue to overworld State (for now)
			game.enterState(DogWarriors.STATES_OVERWORLD, new EmptyTransition(), new VerticalSplitTransition());
			
		} else if (input.isKeyDown(DogWarriors.CONTROLS_QUIT)) { // return to startup state
			game.enterState(DogWarriors.STATES_STARTUP, new FadeOutTransition(), new FadeInTransition());
		}
	}
}
