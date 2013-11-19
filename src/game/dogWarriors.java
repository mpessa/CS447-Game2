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
import org.newdawn.slick.tiled.TiledMap;

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
	public worldDog dog;
	private TiledMap worldMap;
	private int shiftX;
	private int shiftY;
	private int charX;
	private int charY;
	
	
	

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
				//get the screen position by subtracting where the character is by 
				//where the screen is
				int screenX = charX - shiftX/32;
				int screenY = charY - shiftY/32;
				
				//render the map according to the screen x and y 
				worldMap.render(0, 0, screenX, screenY, 30, 30);
				g.fillRect(shiftX, shiftY, 32, 32);
				//g.drawString("shiftX value: " + shiftX + "\nshiftY value: " + shiftY, 10, 80);
				//g.drawString("THe value of screen width " + screenX/32 + " the value of screen height " + screenY/32, 10, 120 );
				dog.render(g);
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
		ResourceManager.loadImage("resource/platfor300.png");
		ResourceManager.loadImage("resource/tower300x100.png");
		ResourceManager.loadImage("resource/tower300x200.png");
		ResourceManager.loadImage("resource/sky2.jpg");
		ResourceManager.loadImage("resource/j.png");
		//find the half way point for the x and y coordinates
		//multiply by 32 to prevent rounding errors.
		shiftX = 32*((ScreenWidth/2)/32);
		shiftY = 32*((ScreenHeight/2)/32);
		//get the starting position for the character
		charX = 15;
		charY = 11;
		world = new PlatformWorld();
		world.chooseLevel(0);
		
		spike = world.new Dog(ScreenWidth / 2, ScreenHeight - 70);
		back = world.new Background(ScreenWidth / 2, ScreenHeight / 2);
		worldMap = new TiledMap("game/resource/demo.tmx", "game/resource");
		dog = new worldDog(shiftX, shiftY, 0.0f, 0.0f);
		startUp(container);
	}
	
	public void startUp(GameContainer container) {
		gameState = WORLD;
		container.setSoundOn(false);
	}
	
	public void newGame(GameContainer container) {
		gameState = WORLD;
		
		container.setSoundOn(true);
	}

	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {
		Input input = container.getInput();

		if(gameState == START_UP){
			if(input.isKeyDown(Input.KEY_ENTER)){
				gameState = WORLD;
				
			}
		}
		if(gameState == WORLD){
			
			int objectLayer = worldMap.getLayerIndex("object");
			
			if(input.isKeyPressed(Input.KEY_D)  ) {
				if(charX < 29 && worldMap.getTileId(charX + 1, charY, objectLayer) == 0)
					charX += 1;
				
				//if(charX == 28 && worldMap.getTileId(charX + 1, charY, objectLayer) == 0)
					//charX += 1;
			}
			if(input.isKeyPressed(Input.KEY_A)){ 
				if(charX > 0 && worldMap.getTileId(charX - 1, charY, objectLayer) == 0)
					charX -= 1;
				//if(charX == 1 && worldMap.getTileId(charX - 1, charY, objectLayer) == 0)
					//charX -= 1;
			}
			if(input.isKeyPressed(Input.KEY_W)){
				if(charY > 0 && worldMap.getTileId(charX, charY - 1, objectLayer) == 0)
					charY -= 1;
				//if(charY == 1 && worldMap.getTileId(charX, charY - 1, objectLayer) == 0)
					//charY -= 1;
			}
			if(input.isKeyPressed(Input.KEY_S)){
				if(charY < 29  && worldMap.getTileId(charX, charY + 1, objectLayer) == 0)
					charY += 1;
				//if(charY == 28  && worldMap.getTileId(charX, charY + 1, objectLayer) == 0)
					//charY += 1;
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
					spike.getCoarseGrainedMaxY() >= p1.getY() - 10 && spike.getCoarseGrainedMaxY() <= p1.getY()){
				spike.setVelocity(new Vector(0f, 0f));
				spike.onP1 = true;
			}
			if(spike.collides(p2) != null && spike.speed.getY() > 0 && spike.time <= 0 &&
					spike.getCoarseGrainedMaxY() >= p2.getY() - 10 && spike.getCoarseGrainedMaxY() <= p2.getY()){
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
	public worldDog getDog() {
		return dog;
	}

	public void setDog(worldDog dog) {
		this.dog = dog;
	}	
	
}
