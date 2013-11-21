package game;

import org.newdawn.slick.Color;
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

public class PausedState extends BasicGameState {
	
	DogWarriors game; // game we are a state of
	GameContainer container; // gameContainer owned by the game
	
	public PausedState(GameContainer container, StateBasedGame game) {
		// THIS SPACE INTENTIONALLY LEFT BLANK
	}

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		System.out.println("Initializing PAUSED state...");
		
		this.game = (DogWarriors) game;
		this.container = container;
		
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		g.setColor(Color.white);
		
		g.drawString("-- Paused --", (container.getWidth()/2) - 50, (container.getHeight()/2) - 10);
		
		if (((DogWarriors) game).getPrevState() == DogWarriors.STATES_OVERWORLD) {
			g.drawString(DogWarriors.worldControls, 10, (container.getHeight() - 230));
		} else {
			g.drawString(DogWarriors.platformControls, 10, (container.getHeight() - 230));
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		
		Input input = container.getInput();
		processKeyInput(input);

	}

	@Override
	public int getID() {
		return 1;
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
		if (input.isKeyDown(DogWarriors.CONTROLS_PAUSE)) { // return to previous state
			game.enterState(game.getPrevState(), new EmptyTransition(), new VerticalSplitTransition());
			
		} else if (input.isKeyDown(DogWarriors.CONTROLS_QUIT)) { // return to startup state
			game.enterState(DogWarriors.STATES_STARTUP, new FadeOutTransition(), new FadeInTransition());
		}
	}
	
}
