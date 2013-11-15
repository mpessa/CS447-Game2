package game;

//import game.PlatformWorld.Background;
import game.PlatformWorld.ground;
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
	private Background back;
	public static ground g1;
	public static PlatformWorld.platform p1, p2;
	public static PlatformWorld.tower t1, t2;
	public PlatformWorld.simpleEnt guy;
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
				guy.render(g);
				break;
		}
		
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		world = new PlatformWorld();
		world.chooseLevel(2);
		guy = world.new simpleEnt(ScreenWidth / 2, ScreenHeight - 70);
		back = new Background(ScreenWidth / 2, ScreenHeight / 2);
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
				guy.setVelocity(new Vector(0.2f, guy.speed.getY()));
			}
			if(input.isKeyDown(Input.KEY_A)){
				guy.setVelocity(new Vector(-0.2f, guy.speed.getY()));
			}
			if(input.isKeyPressed(Input.KEY_W)){
				guy.setVelocity(new Vector(guy.speed.getX(), -0.5f));
				guy.onSomething = false;
			}
			if(input.isKeyDown(Input.KEY_S) && guy.onSomething){
				guy.onSomething = false;
			}
			if(guy.getCoarseGrainedMinX() < 0 && input.isKeyDown(Input.KEY_A)){
				guy.setVelocity(new Vector(0.0f, 0f));
			}
			if(guy.getCoarseGrainedMaxX() > ScreenWidth && input.isKeyDown(Input.KEY_D)){
				guy.setVelocity(new Vector(0f, 0f));
			}/*
			if(guy.getCoarseGrainedMaxY() <= p1.getCoarseGrainedMinY() &&
					guy.getCoarseGrainedMaxX() >= p1.getCoarseGrainedMinX() &&
					guy.getCoarseGrainedMinX() <= p1.getCoarseGrainedMaxX()){
				guy.setVelocity(new Vector(guy.speed.getX(), 0f));
				System.out.println("should be hitting");
			}*/
			if(guy.collides(g1) != null){
				Collision collision2 = guy.collides(g1);
				Vector collVector2 = collision2.getMinPenetration();
				if(collVector2.getY() <= -1.0 && !guy.onSomething){
					System.out.println("coll y= " + collVector2.getY());
					if(guy.speed.getY() != 0)
						guy.setVelocity(new Vector(guy.speed.getX(), 0f));
					guy.onSomething = true;
				}
			}
			if(!guy.onSomething){
				guy.setVelocity(guy.speed.add(new Vector(0f, 0.01f)));
			}
			guy.update(delta);
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

	class Background extends Entity{
		public Background(final float x, final float y){
			super(x,y);
			addImage(ResourceManager.getImage("resource/blueBack.png"));
		}
	}

}
