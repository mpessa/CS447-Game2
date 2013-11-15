package dogWarriors;

import jig.ConvexPolygon;
import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

import org.newdawn.slick.Animation;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class dogWarriors extends BasicGame{

	public dogWarriors(String title, int width, int height){
		super(title);
		ScreenHeight = height;
		ScreenWidth = width;
		
		Entity.setCoarseGrainedCollisionBoundary(Entity.CIRCLE);
	}
	
	public void init(GameContainer container) throws SlickException {
		startUp(container);
	}
	
	public void startUp(GameContainer container) {
		gameState = START_UP;
		container.setSoundOn(true);
	}
	
	public void newGame(GameContainer container) {
		gameState = PLAYING;
		container.setSoundOn(true);
	}
	
	public void gameOver() {
		gameState = GAME_OVER;
	}
	
	public void render(GameContainer container, Graphics g)
			throws SlickException {
		switch (gameState) {
		case PLAYING:
			break;
		case START_UP:
			break;
		case GAME_OVER:
			break;
		case PAUSE:
			break;
		case TRANSITION:
			break;
		case WIN:
			break;
			}
	}

	public void update(GameContainer container, int delta)
			throws SlickException {

		if (gameState == GAME_OVER) {
			gameOverTimer -= delta;
			if (gameOverTimer <= 0)
				startUp(container);
		} else {
			// get user input
			Input input = container.getInput();

			if (gameState == START_UP) {
				if(input.isKeyDown(Input.KEY_X)){
					gameState = EXIT;
				}
			} else if(gameState == PAUSE){
				if(input.isKeyDown(Input.KEY_ENTER)){
					gameState = PLAYING;
				}
				if(input.isKeyDown(Input.KEY_X)){
					gameState = EXIT;
				}
			} else if(gameState == TRANSITION){
				//playerShip.update(delta);
			} else if(gameState == WIN){
				if(input.isKeyDown(Input.KEY_N)){
					startUp(container);
				}
				if(input.isKeyDown(Input.KEY_X)){
					gameState = EXIT;
				}
			} else if(gameState == EXIT){
				container.exit();
			} else {
				if(input.isKeyDown(Input.KEY_A)){
					//playerShip.setVelocity(new Vector(-0.25f, 0f));
				}
				if(input.isKeyDown(Input.KEY_D)){
					//playerShip.setVelocity(new Vector(0.25f, 0f));
				}
				if (input.isKeyDown(Input.KEY_W)) {
					//playerShip.setVelocity(new Vector(0f, -0.25f));
				}
				if (input.isKeyDown(Input.KEY_S)) {
					//playerShip.setVelocity(new Vector(0f, 0.25f));
				}
				if(input.isKeyDown(Input.KEY_P)){
					gameState = PAUSE;
				}
			}
		}
	}

	public static void main(String[] args) throws Exception{
		AppGameContainer app;
		try {
			app = new AppGameContainer(new dogWarriors("Dog Warriors", 800, 700));
			app.setDisplayMode(800, 700, false);
			app.setVSync(true);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}

	}
}
