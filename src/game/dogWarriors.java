package game;

import jig.Collision;
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
	public static int ScreenHeight, ScreenWidth, gameState;
	private static final int PLATFORM = 2;
	private static final int WORLD = 1;
	private static final int START_UP = 0;
	private PlatformWorld.Background back;
	public static PlatformWorld.ground g1;
	public static PlatformWorld.platform p1, p2;
	public static PlatformWorld.tower t1, t2;
	public PlatformWorld.Dog spike;
	public PlatformWorld world;

	public dogWarriors(String title, int width, int height){
		super(title);
		ScreenHeight = height;
		ScreenWidth = width;

		Entity.setCoarseGrainedCollisionBoundary(Entity.AABB);
	}
	

	@Override
	public void render(GameContainer container, Graphics g)
			throws SlickException {
		switch(gameState){
			case WORLD:
				//back.render(g);
				break;
			case PLATFORM:
				back.render(g);
				t1.render(g);
				t2.render(g);
				p1.render(g);
				p2.render(g);
				g1.render(g);
				spike.render(g);
				break;
		}
		
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		ResourceManager.loadImage("resource/ground800.png");
		ResourceManager.loadImage("resource/platform300.png");
		ResourceManager.loadImage("resource/tower300x100.png");
		ResourceManager.loadImage("resource/tower300x200.png");
		ResourceManager.loadImage("resource/sky2.jpg");
		
		world = new PlatformWorld();
		world.chooseLevel(0);
		spike = world.new Dog(ScreenWidth / 2, ScreenHeight - 70);
		back = world.new Background(ScreenWidth / 2, ScreenHeight / 2);
		startUp(container);
	}
	
	public void startUp(GameContainer container) {
		gameState = START_UP;
		container.setSoundOn(false);
	}
	
	public void newGame(GameContainer container) {
		gameState = PLATFORM;
		container.setSoundOn(true);
	}

	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {
		Input input = container.getInput();

		if(gameState == START_UP){
			if(input.isKeyDown(Input.KEY_ENTER)){
				gameState = PLATFORM;
			}
		}
		if(gameState == PLATFORM){
			if(input.isKeyDown(Input.KEY_D)){
				spike.setVelocity(new Vector(0.2f, spike.speed.getY()));
			}
			if(input.isKeyDown(Input.KEY_A)){
				spike.setVelocity(new Vector(-0.2f, spike.speed.getY()));
			}
			if(input.isKeyPressed(Input.KEY_W) && (spike.onP1 || spike.onP2 || spike.onGround)){
				spike.setVelocity(new Vector(spike.speed.getX(), -0.38f));
				spike.onP1 = false;
				spike.onP2 = false;
				spike.onGround = false;
			}
			if(input.isKeyDown(Input.KEY_S) && (spike.onP1 || spike.onP2) && !spike.onGround){
				spike.time = 300;
				spike.onP1 = false;
				spike.onP2 = false;
			}
			if(spike.getCoarseGrainedMinX() < 0 && input.isKeyDown(Input.KEY_A)){
				spike.setVelocity(new Vector(0.0f, spike.speed.getY()));
			}
			if(spike.getCoarseGrainedMaxX() > ScreenWidth && input.isKeyDown(Input.KEY_D)){
				spike.setVelocity(new Vector(0f, spike.speed.getY()));
			}
			if(!input.isKeyDown(Input.KEY_D) && !input.isKeyDown(Input.KEY_A)){
				spike.setVelocity(new Vector(0f, spike.speed.getY()));
			}
			if(spike.collides(g1) != null && spike.speed.getY() > 0){
				spike.setVelocity(new Vector(0f, 0f));
				spike.onGround = true;
			}
			if(spike.collides(p1) != null && spike.speed.getY() > 0 && spike.time <= 0 &&
					spike.getCoarseGrainedMaxY() >= p1.getY() - 15 && spike.getCoarseGrainedMaxY() <= p1.getY()){
				spike.setVelocity(new Vector(0f, 0f));
				spike.onP1 = true;
			}
			if(spike.collides(p2) != null && spike.speed.getY() > 0 && spike.time <= 0 &&
					spike.getCoarseGrainedMaxY() >= p2.getY() - 15 && spike.getCoarseGrainedMaxY() <= p2.getY()){
				spike.setVelocity(new Vector(0f, 0f));
				spike.onP2 = true;
			}
			if((spike.getCoarseGrainedMaxX() <= p1.getCoarseGrainedMinX() || spike.getCoarseGrainedMinX() >= p1.getCoarseGrainedMaxX())
					&& !spike.onGround && spike.onP1){
				spike.onP1 = false;
			}
			if((spike.getCoarseGrainedMaxX() <= p2.getCoarseGrainedMinX() || spike.getCoarseGrainedMinX() >= p2.getCoarseGrainedMaxX())
					&& !spike.onGround && spike.onP2){
				spike.onP2 = false;
			}
			if(!spike.onGround && !spike.onP1 && !spike.onP2){
				spike.setVelocity(spike.speed.add(new Vector(0f, 0.01f)));
			}
			spike.update(delta);
		}
		
	}
	
	public static void main(String[] args) throws Exception{
		AppGameContainer app;
		try {
			app = new AppGameContainer(new dogWarriors("Dog Warrior: Spike's Revenge", 800, 700));
			app.setDisplayMode(800, 700, false);
			app.setVSync(true);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}

	}

}
