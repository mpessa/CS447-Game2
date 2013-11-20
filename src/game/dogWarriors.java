package game;

import java.util.ArrayList;

//import jig.Collision;
import jig.ConvexPolygon;
import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

import org.newdawn.slick.Animation;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
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
	public Dog spike;
	public ArrayList<Cat> cats;
	public Cat ninja;
	public PlatformWorld world;
	public waterBall ball;
	public WaterShield shield;
	public FireShield fShield;
	public Fireball fBall;
	public ArrayList<Fireball> fire;

	public dogWarriors(String title, int width, int height){
		super(title);
		ScreenHeight = height;
		ScreenWidth = width;

		Entity.setCoarseGrainedCollisionBoundary(Entity.AABB);
		cats = new ArrayList<Cat>(5);
		fire = new ArrayList<Fireball>(5);
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
				for(int i = 0; i < cats.size(); i++){
					ninja = cats.get(i);
					ninja.render(g);
				}
				if(spike.shot){
					spike.shoot.draw(spike.getX() - 24, spike.getY() - 24);
				}
				else if(spike.kTime > 0 && !spike.shot){
					spike.kick.draw(spike.getX() - 24, spike.getY() - 24);
				}
				else if((spike.onGround || spike.onP1 || spike.onP2) && spike.kTime <= 0 && !spike.shot){
					spike.walk.draw(spike.getX() - 24, spike.getY() - 24);
				}
				else if(!spike.onGround && !spike.onP1 && !spike.onP2 && spike.kTime <= 0 && !spike.shot){
					spike.jump.draw(spike.getX() - 24, spike.getY() - 24);
				}
				for(int i = 0; i < cats.size(); i++){
					ninja = cats.get(i);
					if(ninja.canKick && ninja.kTime > 0){
						ninja.kick.draw(ninja.getX() - 24, ninja.getY() - 24);
					}
					else if(ninja.canFire && ninja.sTime > 0){
						ninja.shoot.draw(ninja.getX() - 24, ninja.getY() - 24);
					}
					else if(!ninja.onGround && !ninja.onP1 && !ninja.onP2){
						ninja.jump.draw(ninja.getX() - 24, ninja.getY() - 24);
					}
					else if(ninja.onGround || ninja.onP1 || ninja.onP2){
						ninja.walk.draw(ninja.getX() - 24, ninja.getY() - 24);
					}
				}
				for(int i = 0; i < fire.size(); i++){
					fBall = fire.get(i);
					fBall.render(g);
					fBall.fire.draw(fBall.getX(), fBall.getY());
				}
				if(ball.exists){
					ball.render(g);
				}
				if(shield.exists){
					shield.render(g);
				}
				if(fShield.exists){
					fShield.render(g);
				}
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
		ResourceManager.loadImage("resource/catJump.png");
		ResourceManager.loadImage("resource/catWalkL.png");
		ResourceManager.loadImage("resource/catWalkR.png");
		ResourceManager.loadImage("resource/dogJump.png");
		ResourceManager.loadImage("resource/dogKick.png");
		ResourceManager.loadImage("resource/dogShootL.png");
		ResourceManager.loadImage("resource/dogShootR.png");
		ResourceManager.loadImage("resource/dogWalkL.png");
		ResourceManager.loadImage("resource/dogWalkR.png");
		ResourceManager.loadImage("resource/waterball.png");
		ResourceManager.loadImage("resource/waterShield.png");
		
		world = new PlatformWorld();
		world.chooseLevel(0);
		spike = new Dog(ScreenWidth / 2, ScreenHeight - 70);
		ninja = new Cat(3 * ScreenWidth / 4, ScreenHeight - 70, 1);
		ninja.setVelocity(new Vector(-0.2f, 0f));
		cats.add(ninja);
		ninja = new Cat(ScreenWidth / 4, ScreenHeight - 70, 2);
		ninja.setVelocity(new Vector(0.2f, 0f));
		cats.add(ninja);
		ninja = new Cat(ScreenWidth / 3, ScreenHeight - 70, 3);
		ninja.setVelocity(new Vector(-0.2f, 0f));
		cats.add(ninja);
		ninja = new Cat(ScreenWidth / 4, ScreenHeight - 200, 4);
		ninja.setVelocity(new Vector(0.2f, 0f));
		fShield = new FireShield(ninja.getX(), ninja.getY());
		cats.add(ninja);
		back = world.new Background(ScreenWidth / 2, ScreenHeight / 2);
		ball = new waterBall(ScreenWidth / 2, ScreenHeight / 2);
		shield = new WaterShield(ScreenWidth / 2, ScreenHeight / 2);
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
				if(spike.direction == 1){
					spike.change = true;
				}
				spike.walk.start();
			}
			if(input.isKeyDown(Input.KEY_A)){
				spike.setVelocity(new Vector(-0.2f, spike.speed.getY()));
				if(spike.direction == 0){
					spike.change = true;
				}
				spike.walk.start();
			}
			if(input.isKeyPressed(Input.KEY_W) && (spike.onP1 || spike.onP2 || spike.onGround)){
				spike.setVelocity(new Vector(spike.speed.getX(), -0.38f));
				if(!spike.kicking){
					spike.jump();
				}
				spike.onP1 = false;
				spike.onP2 = false;
				spike.onGround = false;
			}
			if(input.isKeyDown(Input.KEY_S) && (spike.onP1 || spike.onP2) && !spike.onGround){
				spike.time = 300;
				spike.jump();
				spike.onP1 = false;
				spike.onP2 = false;
			}
			if(input.isKeyPressed(Input.KEY_J) && spike.cooldown <= 0 && spike.level >= 2){
				spike.kick.restart();
				spike.startKick();
				spike.kicking = true;
				spike.kTime = 1000;
				spike.cooldown = 1500;
			}
			if(input.isKeyPressed(Input.KEY_K) && !spike.shot && spike.cooldown <= 0 && spike.level >= 3){
				if(spike.direction == 0){
					spike.shoot = spike.shootR;
				}
				if(spike.direction == 1){
					spike.shoot = spike.shootL;
				}
				spike.shoot.restart();
				spike.shot = true;
				ball.exists = true;
				spike.sTime = 800;
				spike.cooldown = 2000;
			}
			if(input.isKeyPressed(Input.KEY_L) && spike.cooldown <= 0 && spike.level >= 4){
				shield.exists = true;
				spike.cooldown = 1500;
			}
			if(spike.getCoarseGrainedMinX() < 0 && input.isKeyDown(Input.KEY_A)){
				spike.setVelocity(new Vector(0.0f, spike.speed.getY()));
			}
			if(spike.getCoarseGrainedMaxX() > ScreenWidth && input.isKeyDown(Input.KEY_D)){
				spike.setVelocity(new Vector(0f, spike.speed.getY()));
			}
			if(!input.isKeyDown(Input.KEY_D) && !input.isKeyDown(Input.KEY_A)){
				spike.setVelocity(new Vector(0f, spike.speed.getY()));
				spike.walk.stop();
			}
			if(input.isKeyPressed(Input.KEY_X)){
				container.exit();
			}
			if(spike.collides(g1) != null && spike.speed.getY() > 0){
				spike.setVelocity(new Vector(0f, 0f));
				spike.hitGround();
				spike.onGround = true;
			}
			if(spike.collides(p1) != null && spike.speed.getY() > 0 && spike.time <= 0 &&
					spike.getCoarseGrainedMaxY() >= p1.getY() - 20 && spike.getCoarseGrainedMaxY() <= p1.getY()){
				spike.setVelocity(new Vector(0f, 0f));
				spike.hitGround();
				spike.onP1 = true;
			}
			if(spike.collides(p2) != null && spike.speed.getY() > 0 && spike.time <= 0 &&
					spike.getCoarseGrainedMaxY() >= p2.getY() - 20 && spike.getCoarseGrainedMaxY() <= p2.getY()){
				spike.setVelocity(new Vector(0f, 0f));
				spike.hitGround();
				spike.onP2 = true;
			}
			if((spike.getCoarseGrainedMaxX() <= p1.getCoarseGrainedMinX() || spike.getCoarseGrainedMinX() >= p1.getCoarseGrainedMaxX())
					&& !spike.onGround && spike.onP1){
				spike.jump();
				spike.onP1 = false;
			}
			if((spike.getCoarseGrainedMaxX() <= p2.getCoarseGrainedMinX() || spike.getCoarseGrainedMinX() >= p2.getCoarseGrainedMaxX())
					&& !spike.onGround && spike.onP2){
				spike.jump();
				spike.onP2 = false;
			}
			//Cat movement
			for(int i = 0; i < cats.size(); i++){
				ninja = cats.get(i);
				if(ninja.collides(g1) != null && ninja.speed.getY() > 0){
					ninja.setVelocity(new Vector(ninja.speed.getX(), 0f));
					ninja.jump.stop();
					ninja.onGround = true;
				}
				if(ninja.collides(p1) != null && ninja.speed.getY() > 0 && 
						ninja.getCoarseGrainedMaxY() >= p1.getY() - 20 && ninja.getCoarseGrainedMaxY() <= p1.getY()){
					ninja.setVelocity(new Vector(ninja.speed.getX(), 0f));
					ninja.jump.stop();
					ninja.onP1 = true;
				}
				if(ninja.collides(p2) != null && ninja.speed.getY() > 0 && 
						ninja.getCoarseGrainedMaxY() >= p2.getY() - 20 && ninja.getCoarseGrainedMaxY() <= p2.getY()){
					ninja.setVelocity(new Vector(ninja.speed.getX(), 0f));
					ninja.jump.stop();
					ninja.onP2 = true;
				}
				if(ninja.time <= 0 && ninja.level <= 2){
					ninja.setVelocity(new Vector(ninja.speed.getX(), -0.38f));
					ninja.jump.restart();
					ninja.time = 2000;
					ninja.onP1 = false;
					ninja.onP2 = false;
					ninja.onGround = false;
				}
				if(ninja.time <= 0 && ninja.level == 3){
					if(ninja.onGround || ninja.onP1 || ninja.onP2)
						ninja.setVelocity(new Vector(ninja.speed.getX(), -0.38f));
					if(ninja.speed.getX() < 0)
						ninja.kick = ninja.kickL;
					if(ninja.speed.getX() > 0)
						ninja.kick = ninja.kickR;
					ninja.time = 3000;
					ninja.kTime = 450;
					ninja.kick.restart();
					ninja.onP1 = false;
					ninja.onP2 = false;
					ninja.onGround = false;
				}
				if(ninja.time <= 0 && ninja.level == 4){
					if(ninja.speed.getX() < 0){
						ninja.shoot = ninja.shootL;
						fBall = new Fireball(ninja.getX() - 20, ninja.getY(), -0.1f, 0f, 1);
					}
					if(ninja.speed.getX() > 0){
						ninja.shoot = ninja.shootR;
						fBall = new Fireball(ninja.getX(), ninja.getY(), 0.1f, 0f, 0);
					}
					fire.add(fBall);
					ninja.setVelocity(new Vector(0f, ninja.speed.getY()));
					ninja.time = 1000;
					ninja.sTime = 450;
					ninja.shoot.restart();
				}
				if((ninja.getCoarseGrainedMaxX() <= p1.getCoarseGrainedMinX() || ninja.getCoarseGrainedMinX() >= p1.getCoarseGrainedMaxX())
					&& !ninja.onGround && ninja.onP1){
					ninja.onP1 = false;
					}
				if((ninja.getCoarseGrainedMaxX() <= p2.getCoarseGrainedMinX() || ninja.getCoarseGrainedMinX() >= p2.getCoarseGrainedMaxX())
						&& !ninja.onGround && ninja.onP2){
					ninja.onP2 = false;
				}
				if(ninja.getCoarseGrainedMinX() <= 0 || ninja.getCoarseGrainedMaxX() >= ScreenWidth){
					ninja.setVelocity(new Vector(-ninja.speed.getX(), ninja.speed.getY()));
					ninja.change = true;
				}
				if(ninja.level == 4 && fShield.exists){
					fShield.setPosition(ninja.getX() - 6, ninja.getY() - 12);
				}
			}
			//Cat and dog collisions
			for(int i = 0; i < cats.size(); i++){
				ninja = cats.get(i);
				if(spike.collides(ninja) != null && ninja.hitTime <= 0){
					if(spike.getCoarseGrainedMaxY() > ninja.getY() - 10){
						spike.setVelocity(spike.speed.negate());
						ninja.setVelocity(new Vector(0f, 0f));
						ninja.currentHP -= spike.attPwr;
						if(ninja.currentHP <= 0)
							cats.remove(i);
						ninja.hitTime = 300;
					}
				}
			}
			//Gravity for cats and dog
			if(!spike.onGround && !spike.onP1 && !spike.onP2){
				spike.setVelocity(spike.speed.add(new Vector(0f, 0.01f)));
			}
			for(int i = 0; i < cats.size(); i++){
				ninja = cats.get(i);
				if(!ninja.onGround && !ninja.onP1 && !ninja.onP2){
					ninja.setVelocity(ninja.speed.add(new Vector(0f, 0.01f)));
				}
				ninja.update(delta);
			}
			//Dog special abilities
			if(spike.kicking && spike.kTime <= 0){
				spike.kicking = false;
				spike.endKick();
			}
			if(ball.exists && spike.shot && spike.sTime > 0){
				if(spike.direction == 1){
					ball.setPosition(spike.getX() - 30, spike.getY());
				}
				if(spike.direction == 0){
					ball.setPosition(spike.getX() + 10, spike.getY());
				}
			}
			if(ball.exists && spike.shot && spike.sTime <= 0){
				spike.shot = false;
				if(spike.direction == 0){
					ball.setVelocity(new Vector(0.5f, 0f));
				}
				if(spike.direction == 1){
					ball.setVelocity(new Vector(-0.5f, 0f));
				}
			}
			for(int i = 0; i < fire.size(); i++){
				System.out.println(i);
				fBall = fire.get(i);
				fBall.update(delta);
			}
			if(shield.exists){
				shield.setPosition(spike.getX() - 5, spike.getY());
			}
			if(ball.exists){
				ball.update(delta);
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
