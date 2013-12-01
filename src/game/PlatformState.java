
package game;

import java.util.ArrayList;
import java.util.Random;

import jig.ResourceManager;
import jig.Vector;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.EmptyTransition;
import org.newdawn.slick.state.transition.HorizontalSplitTransition;
import org.newdawn.slick.state.transition.RotateTransition;

public class PlatformState extends BasicGameState {

	DogWarriors game; // game we are a state of
	GameContainer container; // gameContainer owned by the game
	
	public PlatformWorld world;
	private PlatformWorld.Background back;
	
	private int screenWidth, screenHeight;
	private int screenCenterX, screenCenterY;
	private int level, expEarned, deadCats;
	
	private boolean levelOver;
	
	private Random random;
	
	public PlatformWorld.Ground g1;
	public PlatformWorld.Platform p1, p2;
	public PlatformWorld.Tower t1, t2;
	
	public Dog spike;
	public Cat ninja;
	public ArrayList<Cat> cats;
	
	public WaterBall ball;
	public WaterShield shield;
	public FireShield fShield;
	public Fireball fBall;
	public ArrayList<Fireball> fire;
	
	public PlatformState(GameContainer container, StateBasedGame game) {
		// THIS SPACE INTENTIONALLY LEFT BLANK
	}

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		System.out.println("Initializing PLATFORM state...");
		
		System.out.println("Loading Graphics...");
		for (String s : DogWarriors.catImages) {
			ResourceManager.loadImage(s);
		}
		for (String s : DogWarriors.dogImages) {
			ResourceManager.loadImage(s);
		}
		for (String s : DogWarriors.battleImages) {
			ResourceManager.loadImage(s);
		}

		this.game = (DogWarriors) game;
		this.container = container;
			
		this.screenWidth = DogWarriors.ScreenWidth;
		this.screenHeight = DogWarriors.ScreenHeight;
		this.screenCenterX = screenWidth / 2;
		this.screenCenterY = screenHeight / 2;
		
		this.levelOver = false;
		
		this.cats = new ArrayList<Cat>(5);
		this.fire = new ArrayList<Fireball>(5);
		this.ball = new WaterBall(screenCenterX, screenCenterY);
		this.shield = new WaterShield(screenCenterX, screenCenterY);
		this.world = new PlatformWorld();
		this.back = world.new Background(screenCenterX, screenCenterY);
		this.spike = new Dog(screenCenterX, screenHeight - 70);
		
		level = randomLevel();
		this.expEarned = 0;
		this.deadCats = 0;
		this.chooseLevel(level);
		this.addCats();

	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		
		back.render(g);
		g.drawString("HP: " + spike.currentHP + "/" + spike.maxHP, 0, 30);
		g.drawString("Slobber: " + spike.currentSlobber + "/" + spike.maxSlobber, 0, 50);
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
			if(ninja.dead){
				ninja.die.draw(ninja.getX() - 24, ninja.getY() - 24);
			}
			else if(ninja.canKick && ninja.kTime > 0){
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
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		
		Input input = container.getInput();
		processKeyInput(input);
		
		// Spike Vs. The Platforms
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
		
		if((spike.getX() + 3 <= p1.getCoarseGrainedMinX() || spike.getCoarseGrainedMinX() >= p1.getCoarseGrainedMaxX())
				&& !spike.onGround && spike.onP1){
			spike.jump();
			spike.onP1 = false;
		}
		
		if((spike.getX() + 3 <= p2.getCoarseGrainedMinX() || spike.getCoarseGrainedMinX() >= p2.getCoarseGrainedMaxX())
				&& !spike.onGround && spike.onP2){
			spike.jump();
			spike.onP2 = false;
		}
		
		// Cat movement
		for(int i = 0; i < cats.size(); i++) {
			ninja = cats.get(i);
			if(ninja.collides(g1) != null && ninja.speed.getY() > 0) {
				ninja.setVelocity(new Vector(ninja.speed.getX(), 0f));
				ninja.jump.stop();
				ninja.onGround = true;
			}
			
			if (ninja.collides(p1) != null && ninja.speed.getY() > 0 && 
					ninja.getCoarseGrainedMaxY() >= p1.getY() - 20 &&
					ninja.getCoarseGrainedMaxY() <= p1.getY() && ninja.drop <= 0) {
				
				ninja.setVelocity(new Vector(ninja.speed.getX(), 0f));
				ninja.jump.stop();
				//System.out.println("Ninja:" + i + " on P1");
				if(ninja.level == 1 && !ninja.onP1){
					ninja.platTime = 7000;
				}
				if(ninja.level == 2 && !ninja.onP1){
					ninja.platTime = 5000;
				}
				if(ninja.level == 3 && !ninja.onP1){
					ninja.platTime = 3000;
				}
				ninja.onP1 = true;
			}
			if(ninja.collides(p2) != null && ninja.speed.getY() > 0 && 
					ninja.getCoarseGrainedMaxY() >= p2.getY() - 20 &&
					ninja.getCoarseGrainedMaxY() <= p2.getY()) {
				
				ninja.setVelocity(new Vector(ninja.speed.getX(), 0f));
				ninja.jump.stop();
				if(ninja.level == 1 && !ninja.onP2){
					System.out.println("On P2");
					ninja.platTime = 7000;
				}
				if(ninja.level == 2 && !ninja.onP2){
					ninja.platTime = 5000;
				}
				if(ninja.level == 3 && !ninja.onP2){
					ninja.platTime = 3000;
				}
				ninja.onP2 = true;
			}
			if(ninja.getX() >= screenWidth || ninja.getX() - 10 <= 0){
				ninja.setVelocity(new Vector(-1 * ninja.speed.getX(), ninja.speed.getY()));
				ninja.change = true;
			}
			
			// Level 1 AI
			if(ninja.level == 1 && !ninja.dead) {
				//System.out.println("platTime= " + ninja.platTime);
				if((ninja.onGround || ninja.onP1 || ninja.onP2) && ninja.time <= 0){
					if(jump() == 1){
						ninja.setVelocity(new Vector(ninja.speed.getX(), -0.4f));
						ninja.jump.restart();
						ninja.onGround = false;
						ninja.onP1 = false;
						ninja.onP2 = false;
					}
					ninja.time = 4000;
				}
				if((ninja.onP1 || ninja.onP2) && ninja.platTime <= 0){
					//System.out.println("Drop down, platTime= " + ninja.platTime);
					ninja.onP1 = false;
					ninja.onP2 = false;
					ninja.drop = 300;
				}
				if(ninja.onP1){
					//System.out.println("On P1");
					if(ninja.getCoarseGrainedMaxX() >= p1.getCoarseGrainedMaxX() ||
							ninja.getCoarseGrainedMinX() <= p1.getCoarseGrainedMinX()){
						ninja.change = true;
						ninja.setVelocity(ninja.speed.negate());
						System.out.println("Change dir");
					}
				}
				if(ninja.onP2){
					if(ninja.getCoarseGrainedMaxX() >= p2.getCoarseGrainedMaxX() ||
							ninja.getCoarseGrainedMinX() <= p2.getCoarseGrainedMinX()){
						ninja.change = true;
						ninja.setVelocity(ninja.speed.negate());
					}
				}
			}
		
			// Level 2 AI
			if(ninja.level == 2 && !ninja.dead) {
				//If Dog not on same level as Cat
				if(spike.getY() > ninja.getY() + 10 || spike.getY() < ninja.getY() - 10){
					if((ninja.onGround || ninja.onP1 || ninja.onP2) && ninja.time <= 0){
						if(jump() == 1){
							ninja.setVelocity(new Vector(ninja.speed.getX(), -0.4f));
							ninja.jump.restart();
							ninja.onGround = false;
							ninja.onP1 = false;
							ninja.onP2 = false;
						}
						ninja.time = 4000;
					}
					if((ninja.onP1 || ninja.onP2) && ninja.platTime <= 0){
						ninja.onP1 = false;
						ninja.onP2 = false;
						ninja.drop = 300;
					}
					if(ninja.onP1){
						if(ninja.getCoarseGrainedMaxX() >= p1.getCoarseGrainedMaxX() ||
								ninja.getCoarseGrainedMinX() <= p1.getCoarseGrainedMinX()){
							ninja.change = true;
							ninja.setVelocity(ninja.speed.negate());
						}
					}
					if(ninja.onP2){
						if(ninja.getCoarseGrainedMaxX() >= p2.getCoarseGrainedMaxX() ||
								ninja.getCoarseGrainedMinX() <= p2.getCoarseGrainedMinX()){
							ninja.change = true;
							ninja.setVelocity(ninja.speed.negate());
						}
					}
					if(spike.onP1 && ninja.onGround && ninja.getX() + 3 <= p1.getCoarseGrainedMaxX() &&
							ninja.getX() >= p1.getCoarseGrainedMinX()){
						ninja.setVelocity(new Vector(0f, -0.4f));
						ninja.onGround = false;
					}
					ninja.chase = false;
				}
				//If Dog on same level
				else{
					if(ninja.getX() - spike.getX() < 0 && !ninja.chase){
						ninja.chase = true;
						ninja.setVelocity(new Vector(0.2f, ninja.speed.getY()));
						ninja.change = true;
					}
					if(ninja.getX() - spike.getX() > 0 && !ninja.chase){
						ninja.chase = true;
						ninja.setVelocity(new Vector(-0.2f, ninja.speed.getY()));
						ninja.change = true;
					}
					if((ninja.getX() + 3 <= p1.getCoarseGrainedMinX() || ninja.getCoarseGrainedMinX() >= p1.getCoarseGrainedMaxX())
							&& !ninja.onGround && ninja.onP1){
						ninja.onP1 = false;
					}				
					if((ninja.getX() + 3 <= p2.getCoarseGrainedMinX() || ninja.getCoarseGrainedMinX() >= p2.getCoarseGrainedMaxX())
							&& !ninja.onGround && ninja.onP2){
						ninja.onP2 = false;
					}
					//Attack if close enough
					if(Math.abs(ninja.getX() - spike.getX()) <= 150 && ninja.kTime <= 0 && ninja.cooldown <= 0){
						System.out.println("Kick");
						if(ninja.speed.getX() < 0)
							ninja.kick = ninja.kickL;
						if(ninja.speed.getX() > 0)
							ninja.kick = ninja.kickR;
						ninja.kTime = 450;
						ninja.cooldown = 1500;
						ninja.kick.restart();
						ninja.inAir = true;
						ninja.setVelocity(new Vector(ninja.speed.getX() * 2, 0f));
					}
				}
			}
			
			//Level 3 AI
			if(ninja.level == 3 && !ninja.dead) {
				//If Dog not on same level as Cat
				if(spike.getY() > ninja.getY() + 100 || spike.getY() < ninja.getY() - 100){
					if((ninja.onGround || ninja.onP1 || ninja.onP2) && ninja.time <= 0){
						if(jump() == 1){
							ninja.setVelocity(new Vector(ninja.speed.getX(), -0.4f));
							ninja.jump.restart();
							ninja.onGround = false;
							ninja.onP1 = false;
							ninja.onP2 = false;
						}
						ninja.time = 4000;
					}
					if((ninja.onP1 || ninja.onP2) && ninja.platTime <= 0){
						ninja.onP1 = false;
						ninja.onP2 = false;
						ninja.drop = 300;
					}
					if(ninja.onP1){
						if(ninja.getCoarseGrainedMaxX() >= p1.getCoarseGrainedMaxX() ||
								ninja.getCoarseGrainedMinX() <= p1.getCoarseGrainedMinX()){
							ninja.change = true;
							ninja.setVelocity(ninja.speed.negate());
						}
					}
					if(ninja.onP2){
						if(ninja.getCoarseGrainedMaxX() >= p2.getCoarseGrainedMaxX() ||
								ninja.getCoarseGrainedMinX() <= p2.getCoarseGrainedMinX()){
							ninja.change = true;
							ninja.setVelocity(ninja.speed.negate());
						}
					}
					if(spike.onP1 && ninja.onGround && ninja.getX() + 3 <= p1.getCoarseGrainedMaxX() &&
							ninja.getX() >= p1.getCoarseGrainedMinX()){
						ninja.setVelocity(new Vector(0f, -0.4f));
						ninja.onGround = false;
					}
					ninja.chase = false;
					if(ninja.sTime <= 0 && ninja.speed.getX() == 0){
						if(ninja.walk == ninja.walkR){
							ninja.setVelocity(new Vector(0.2f, ninja.speed.getY()));
						}
						if(ninja.walk == ninja.walkL){
							ninja.setVelocity(new Vector(-0.3f, ninja.speed.getY()));
						}
					}
				}
				//If Dog on same level, wider view than level 2
				else{
					if(ninja.getX() - spike.getX() < 0 && !ninja.chase){
						ninja.chase = true;
						ninja.setVelocity(new Vector(0.2f, ninja.speed.getY()));
						ninja.change = true;
					}
					if(ninja.getX() - spike.getX() > 0 && !ninja.chase){
						ninja.chase = true;
						ninja.setVelocity(new Vector(-0.2f, ninja.speed.getY()));
						ninja.change = true;
					}
					if((ninja.getX() + 3 <= p1.getCoarseGrainedMinX() || ninja.getCoarseGrainedMinX() >= p1.getCoarseGrainedMaxX())
							&& !ninja.onGround && ninja.onP1){
						ninja.onP1 = false;
					}				
					if((ninja.getX() + 3 <= p2.getCoarseGrainedMinX() || ninja.getCoarseGrainedMinX() >= p2.getCoarseGrainedMaxX())
							&& !ninja.onGround && ninja.onP2){
						ninja.onP2 = false;
					}
					if(ninja.sTime <= 0 && ninja.speed.getX() == 0){
						if(ninja.walk == ninja.walkR){
							ninja.setVelocity(new Vector(0.2f, ninja.speed.getY()));
						}
						if(ninja.walk == ninja.walkL){
							ninja.setVelocity(new Vector(-0.2f, ninja.speed.getY()));
						}
					}
					//Attack if close enough
					if(Math.abs(ninja.getX() - spike.getX()) <= 150 && ninja.kTime <= 0 && ninja.cooldown <= 0){
						System.out.println("Kick");
						if(ninja.speed.getX() < 0)
							ninja.kick = ninja.kickL;
						if(ninja.speed.getX() > 0)
							ninja.kick = ninja.kickR;
							ninja.kTime = 450;
							ninja.cooldown = 1500;
							ninja.kick.restart();
							ninja.inAir = true;
							ninja.setVelocity(new Vector(ninja.speed.getX() * 2, 0f));
						}
					//Attack from range
					else{
						if(ninja.cooldown <= 0 && (spike.getY() > ninja.getY() + 10 || spike.getY() < ninja.getY() - 10)){
							System.out.println("Shoot");
							if(ninja.speed.getX() < 0 || ninja.shoot == ninja.shootL){
								ninja.shoot = ninja.shootL;
								fBall = new Fireball(ninja.getX(), ninja.getY() - 10, -0.3f, 0f, 1);
							}
							if(ninja.speed.getX() > 0 || ninja.shoot == ninja.shootR){
								ninja.shoot = ninja.shootR;
								fBall = new Fireball(ninja.getX(), ninja.getY() - 10, 0.3f, 0f, 0);
							}
							fire.add(fBall);
							ninja.sTime = 450;
							ninja.cooldown = 1500;
							ninja.shoot.restart();
							ninja.setVelocity(new Vector(0f, ninja.speed.getY()));
							
						}
					}
				}
			}
			
			//Level 4 AI
			if(ninja.level == 4 && !ninja.dead) {
				if(fShield.exists){
					fShield.setPosition(ninja.getX(), ninja.getY() - 10);
				}
				if(ninja.cooldown <= 0 && (spike.getY() < ninja.getY() + 20 || spike.getY() > ninja.getY() - 20)){
					if((!ninja.canShield && Math.abs(ninja.getX() - spike.getX()) >= 150) ||
							(ninja.canShield && Math.abs(ninja.getX() - spike.getX()) >= 200)){
						if(ninja.speed.getX() < 0 || ninja.walk == ninja.walkR){
							ninja.shoot = ninja.shootR;
							fBall = new Fireball(ninja.getX(), ninja.getY() - 10, 0.3f, 0f, 0);
						}
						if(ninja.speed.getX() > 0 || ninja.walk == ninja.walkL){
							ninja.shoot = ninja.shootL;
							fBall = new Fireball(ninja.getX(), ninja.getY() - 10, -0.3f, 0f, 1);
						}
						fire.add(fBall);
						ninja.sTime = 450;
						ninja.cooldown = 1500;
						ninja.shoot.restart();
						ninja.setVelocity(new Vector(0f, ninja.speed.getY()));	
					}
				}
				if(Math.abs(ninja.getX() - spike.getX()) <= 300 && !ninja.inAir){
					if(ninja.getX() < p1.getCoarseGrainedMaxX() && ninja.getX() > p1.getCoarseGrainedMinX() &&
							!spike.onP1 && !spike.onP2 && ninja.onGround){
						ninja.setVelocity(new Vector(ninja.speed.getX(), -0.4f));
						ninja.jump.restart();
						ninja.onGround = false;
						ninja.onP1 = false;
						ninja.onP2 = false;
					}
					//Attack if close enough and no shield
					if(Math.abs(ninja.getX() - spike.getX()) <= 150 && ninja.kTime <= 0 
							&& ninja.cooldown <= 0 && !ninja.canShield && !fShield.exists){
						if(ninja.speed.getX() < 0)
							ninja.kick = ninja.kickR;
						if(ninja.speed.getX() > 0)
							ninja.kick = ninja.kickL;
							ninja.kTime = 450;
							ninja.cooldown = 1500;
							ninja.kick.restart();
							ninja.inAir = true;
							if(ninja.speed.getX() == 0){
								ninja.setVelocity(new Vector(0.4f, ninja.speed.getY() + 0.2f));
							}
							else
								ninja.setVelocity(new Vector(ninja.speed.getX() * -2, 0f));
					}
					else if(Math.abs(ninja.getX() - spike.getX()) <= 150 &&
							ninja.cooldown <= 0 && ninja.canShield){
						fShield.setPosition(ninja.getX(), ninja.getY() - 10);
						fShield.exists = true;
						ninja.canShield = false;
						ninja.cooldown = 1000;
					}
					else if(Math.abs(ninja.getX() - spike.getX()) <= 150 && !ninja.chase){
						ninja.setVelocity(new Vector(-1 * ninja.speed.getX(), ninja.speed.getX()));
						ninja.chase = true;
					}
					else if(ninja.getX() - spike.getX() < 0){
						if(Math.abs(ninja.getX() - spike.getX()) >= 150 && ninja.sTime <= 0
								&& ninja.kTime <= 0){
							if((ninja.sTime <= 0 && ninja.getX() >= 25)){
								System.out.println("x = " + ninja.getX());
								ninja.setVelocity(new Vector(-0.2f, ninja.speed.getY()));
								ninja.change = true;
							}
							else if(ninja.getX() <= 20 && ninja.time <= 0){
								ninja.setVelocity(new Vector(0.2f, ninja.speed.getY()));
								ninja.change = true;
								ninja.time = 300;
							}
							else{
								ninja.setVelocity(new Vector(0f, ninja.speed.getY()));
							}
						}
					}
					else if(ninja.getX() - spike.getX() > 0){
						if(Math.abs(ninja.getX() - spike.getX()) >= 150 && ninja.sTime <= 0
								&& ninja.kTime <= 0){
							if((ninja.sTime <= 0 && ninja.getX() <= screenWidth - 25)){
								ninja.setVelocity(new Vector(0.2f, ninja.speed.getY()));
								ninja.change = true;
							}
							else if(ninja.getX() <= 20 && ninja.time <= 0){
								ninja.setVelocity(new Vector(-0.2f, ninja.speed.getY()));
								ninja.change = true;
								ninja.time = 300;
							}
							else{
								ninja.setVelocity(new Vector(0f, ninja.speed.getY()));
							}
						}
					}
					
					if((ninja.onP1 && spike.onP1) || (ninja.onP2 && spike.onP2)){
						ninja.onP1 = false;
						ninja.onP2 = false;
						ninja.drop = 300;
					}
				if((ninja.getX() + 3 <= p1.getCoarseGrainedMinX() || ninja.getCoarseGrainedMinX() >= p1.getCoarseGrainedMaxX())
						&& !ninja.onGround && ninja.onP1){
					ninja.onP1 = false;
				}				
				if((ninja.getX() + 3 <= p2.getCoarseGrainedMinX() || ninja.getCoarseGrainedMinX() >= p2.getCoarseGrainedMaxX())
						&& !ninja.onGround && ninja.onP2){
					ninja.onP2 = false;
				}
				if(ninja.kTime <= 0 && ninja.inAir){
					ninja.inAir = false;			
				}
				
				}
				
				else{
					if(ninja.kTime <= 0){
						System.out.println("Set speed to 0");
						ninja.setVelocity(new Vector(0f, ninja.speed.getY()));
					}
				}
			}
		}
		
		//Cat and dog collisions
		for(int i = 0; i < cats.size(); i++) {
			ninja = cats.get(i);
			if(spike.collides(ninja) != null && ninja.hitTime <= 0 &&
				spike.getCoarseGrainedMaxY() < ninja.getY() - 10 && spike.speed.getY() >= 0 && !ninja.dead) {
				//play cat hurt sound
				spike.setVelocity(spike.speed.negate());
				ninja.setVelocity(new Vector(0f, 0f));
				ninja.currentHP -= spike.attPwr;
				ninja.hitTime = 300;
				if(ninja.currentHP <= 0 && !ninja.dead){
					ninja.time = 600;
					ninja.dead = true;
					expEarned += ninja.exp;
					deadCats++;
					if(ninja.speed.getX() <= 0){
						ninja.die = ninja.dieL;
					}
				}
			}
			else if(spike.collides(ninja) != null && ninja.hitTime <= 0 && spike.kTime > 0){
				//play cat hurt sound
				ninja.setVelocity(new Vector(0f, 0f));
				ninja.currentHP -= spike.spPwr;
				if(ninja.currentHP <= 0 && !ninja.dead){
					ninja.time = 600;
					ninja.dead = true;
					expEarned += ninja.exp;
					deadCats++;
				}
				ninja.hitTime = 300;
			}
			else if(spike.collides(ninja) != null && ninja.hitTime <= 0){
				//play spike hurt sound
				spike.setVelocity(new Vector(0f, 0f));
				spike.currentHP -= ninja.attPwr;
				ninja.hitTime = 300;
				if(spike.currentHP <= 0)
					System.out.println("Kill Spike");
			}
			if(ninja.dead && ninja.time <= 0){
				ninja.kill();
			}
			if(ball.collides(ninja) != null && ball.exists){
				//insert sound
				ball.exists = false;
				ball.setPosition(0, 0);
				ninja.setVelocity(new Vector(0f, ninja.speed.getY()));
				ninja.currentHP -= spike.spPwr * 1.5;
				if(ninja.currentHP <= 0 && !ninja.dead){
					ninja.time = 600;
					ninja.dead = true;
					expEarned += ninja.exp;
					deadCats++;
				}
			}
			if(shield.collides(ninja) != null && shield.exists){
				//insert sound
				shield.exists = false;
				ninja.setVelocity(new Vector(0f, ninja.speed.getY()));
				ninja.currentHP -= spike.spPwr;
				if(ninja.currentHP <= 0 && !ninja.dead){
					ninja.time = 600;
					ninja.dead = true;
					expEarned += ninja.exp;
					deadCats++;
				}
			}
		}
		for(int i = 0; i < fire.size(); i++){
			fBall = fire.get(i);
			if(fBall.collides(ball) != null && ball.exists){
				ball.exists = false;
				ball.setPosition(0, 0);
				//play animation and sound
				fire.remove(i);
			}
			if(fBall.collides(shield) != null && shield.exists){
				//play animation and sound
				fire.remove(i);
				shield.exists = false;
			}
			if(fBall.collides(spike) != null){
				fire.remove(i);
				spike.currentHP -= 20;
				//play sound
				if(fBall.getX() > spike.getX()){
					spike.setVelocity(new Vector(-0.1f, spike.speed.getY()));
					spike.time = 200;
				}
				if(fBall.getX() < spike.getX()){
					spike.setVelocity(new Vector(0.1f, spike.speed.getY()));
					spike.time = 200;
				}
			}
		}
		if(ball.collides(fShield) != null && ball.exists && fShield.exists){
			ball.exists = false;
			//play sound and animation
			ball.setPosition(0, 0);
			fShield.exists = false;
			fShield.setPosition(0, 0);
		}
		if(fShield.collides(shield) != null && shield.exists && fShield.exists){
			shield.exists = false;
			fShield.exists = false;
			fShield.setPosition(0, 0);
			//play sound and animation
		}
		if(spike.collides(fShield) != null && fShield.exists){
			//play sound
			spike.setVelocity(new Vector(-2 * spike.speed.getX(), -2 * spike.speed.getY()));
			spike.time = 200;
			spike.currentHP -= 50;
		}
		
		
		//Gravity for cats and dog
		if(!spike.onGround && !spike.onP1 && !spike.onP2) {
			spike.setVelocity(spike.speed.add(new Vector(0f, 0.01f)));
		}
		for(int i = 0; i < cats.size(); i++) {
			ninja = cats.get(i);
			if(!ninja.onGround && !ninja.onP1 && !ninja.onP2 && !ninja.done && !ninja.inAir) {
				ninja.setVelocity(ninja.speed.add(new Vector(0f, 0.01f)));
			}
			if(ninja.dead && ninja.done){
				ninja.setVelocity(new Vector(0f, 0f));
			}
			ninja.update(delta);
		}
		
		// Dog special abilities
		if (spike.kicking && spike.kTime <= 0) {
			spike.kicking = false;
			spike.endKick();
		}
		
		if (spike.shot && spike.sTime > 0) {
			if(spike.direction == 1) {
				ball.setPosition(spike.getX() - 30, spike.getY());
			}
			if(spike.direction == 0) {
				ball.setPosition(spike.getX() + 10, spike.getY());
			}
		}
		if (spike.shot && spike.sTime <= 0) {
			spike.shot = false;
			if(spike.direction == 0) {
				ball.setVelocity(new Vector(0.5f, 0f));
			}
			if(spike.direction == 1) {
				ball.setVelocity(new Vector(-0.5f, 0f));
			}
		}
		for (int i = 0; i < fire.size(); i++) {
			fBall = fire.get(i);
			fBall.update(delta);
			if(fBall.getCoarseGrainedMinX() > screenWidth || fBall.getCoarseGrainedMaxX() < 0){
				fire.remove(i);
			}
		}
		if (shield.exists) {
			shield.setPosition(spike.getX() - 5, spike.getY());
		}
		if (ball.exists) {
			ball.update(delta);
		}			
		spike.update(delta);
		if(cats.size() == deadCats && !levelOver){
			spike.currentExp += expEarned;
			levelOver = true;
			System.out.println("Experience earned = " + expEarned);
			spike.levelUp();
			leave(container, game);
		}
	}

	@Override
	public int getID() {
		return 4;
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException {
		container.getInput().clearKeyPressedRecord();
		switch((((DogWarriors) game).getPrevState())) {
		case(0): // the game is starting, and we need a new game
			break;
		case(1): // the game was un-paused, and we should do nothing
			break;
		case(2): // the game is returning from transition (To Be Defined)
			break;
		case(3): // the game is entering from overworld, and we need a new game
			break;
		}
	}

	@Override
	public void leave(GameContainer container, StateBasedGame game) {
		container.getInput().clearKeyPressedRecord();
		((DogWarriors) game).setPrevState(this.getID());
	}
	
	private void processKeyInput(Input input) {
		if (input.isKeyDown(DogWarriors.CONTROLS_RIGHT) && spike.time <= 0) {
			spike.setVelocity(new Vector(0.2f, spike.speed.getY()));
			if(spike.direction == 1) {
				spike.change = true;
			}
			spike.walk.start();
		}
		
		if (input.isKeyDown(DogWarriors.CONTROLS_LEFT) && spike.time <= 0) {
			spike.setVelocity(new Vector(-0.2f, spike.speed.getY()));
			if(spike.direction == 0) {
				spike.change = true;
			}
			spike.walk.start();
		}
		
		if (input.isKeyPressed(DogWarriors.CONTROLS_UP) && (spike.onP1 || spike.onP2 || spike.onGround)) {
			spike.setVelocity(new Vector(spike.speed.getX(), -0.38f));
			if(!spike.kicking) {
				spike.jump();
			}
			spike.onP1 = false;
			spike.onP2 = false;
			spike.onGround = false;
		}
		
		if(input.isKeyDown(DogWarriors.CONTROLS_DOWN) && (spike.onP1 || spike.onP2) && !spike.onGround) {
			spike.time = 300;
			spike.jump();
			spike.onP1 = false;
			spike.onP2 = false;
		}
		
		if(input.isKeyPressed(DogWarriors.CONTROLS_KICK) && spike.cooldown <= 0 && spike.level >= 2
				&& spike.currentSlobber >= 1) {
			spike.currentSlobber -= 1;
			spike.kick.restart();
			spike.startKick();
			//play sound
			spike.kicking = true;
			spike.kTime = 1000;
			spike.cooldown = 1500;
		}
		
		if(input.isKeyPressed(DogWarriors.CONTROLS_SHOOT) && !spike.shot && spike.cooldown <= 0 && 
				spike.level >= 3 && spike.currentSlobber >= 2) {
			spike.currentSlobber -= 2;
			if(spike.direction == 0) {
				spike.shoot = spike.shootR;
			}
			if(spike.direction == 1) {
				spike.shoot = spike.shootL;
			}
			spike.shoot.restart();
			//play sound
			spike.shot = true;
			ball.exists = true;
			spike.sTime = 800;
			spike.cooldown = 2000;
		}
		
		if(input.isKeyPressed(DogWarriors.CONTROLS_SHIELD) && spike.cooldown <= 0 && 
				spike.level >= 4 && spike.currentSlobber >= 4) {
			spike.currentSlobber -= 4;
			shield.exists = true;
			//play sound
			spike.cooldown = 1500;
		}
		
		if(spike.getCoarseGrainedMinX() < 0 && input.isKeyDown(DogWarriors.CONTROLS_LEFT)){
			spike.setVelocity(new Vector(0.0f, spike.speed.getY()));
		}
		
		if(spike.getCoarseGrainedMaxX() > screenWidth && input.isKeyDown(DogWarriors.CONTROLS_RIGHT)){
			spike.setVelocity(new Vector(0f, spike.speed.getY()));
		}
		
		if(!input.isKeyDown(DogWarriors.CONTROLS_RIGHT) && !input.isKeyDown(DogWarriors.CONTROLS_LEFT)
				&& spike.time <= 0){
			spike.setVelocity(new Vector(0f, spike.speed.getY()));
			spike.walk.stop();
		}
		
		if (input.isKeyPressed(DogWarriors.CONTROLS_CHEAT_1)) { // instantly enter overworld mode
			game.enterState(DogWarriors.STATES_OVERWORLD, new EmptyTransition(), new RotateTransition());
		}
		
		if (input.isKeyPressed(DogWarriors.CONTROLS_PAUSE)) { // pause the game
			game.enterState(DogWarriors.STATES_PAUSED, new EmptyTransition(), new HorizontalSplitTransition());
		}
		
		if(input.isKeyPressed(DogWarriors.CONTROLS_QUIT)) { // return to startup state
			game.enterState(DogWarriors.STATES_STARTUP, new EmptyTransition(), new HorizontalSplitTransition());
		}
	}
	
	private void addCats() throws SlickException {
		int x = numberOfCats();
		fShield = new FireShield(0, 0);
		switch(x){
		case 2:
			ninja = new Cat(screenWidth / 4, screenHeight - 70, setCatLevel(spike.level));
			ninja.setVelocity(new Vector(-0.2f, 0f));
			cats.add(ninja);
			ninja = new Cat(3 * screenWidth / 4, screenHeight - 70, setCatLevel(spike.level));
			ninja.setVelocity(new Vector(0.2f, 0f));
			cats.add(ninja);
			break;
		case 3:
			ninja = new Cat(screenWidth / 3, screenHeight - 70, setCatLevel(spike.level));
			ninja.setVelocity(new Vector(0.2f, 0f));
			cats.add(ninja);
			ninja = new Cat(2 * screenWidth / 3, screenHeight - 70, setCatLevel(spike.level));
			ninja.setVelocity(new Vector(-0.2f, 0f));
			cats.add(ninja);
			ninja = new Cat(screenWidth / 2, screenHeight - 200, setCatLevel(spike.level));
			ninja.setVelocity(new Vector(0.2f, 0f));
			cats.add(ninja);
			break;
		case 4:
			ninja = new Cat(screenWidth / 4, screenHeight - 70, setCatLevel(spike.level));
			ninja.setVelocity(new Vector(0.2f, 0f));
			cats.add(ninja);
			ninja = new Cat(3 * screenWidth / 4, screenHeight - 70, setCatLevel(spike.level));
			ninja.setVelocity(new Vector(-0.2f, 0f));
			cats.add(ninja);
			ninja = new Cat(screenWidth / 4, screenHeight - 200, setCatLevel(spike.level));
			ninja.setVelocity(new Vector(-0.2f, 0f));
			cats.add(ninja);
			ninja = new Cat(3 * screenWidth / 4, screenHeight - 200, setCatLevel(spike.level));
			ninja.setVelocity(new Vector(0.2f, 0f));
			cats.add(ninja);
			break;
		default:
			ninja = new Cat(screenWidth / 3, screenHeight - 70, setCatLevel(spike.level));
			ninja.setVelocity(new Vector(-0.2f, 0f));
			cats.add(ninja);
			ninja = new Cat(2 * screenWidth / 3, screenHeight - 70, setCatLevel(spike.level));
			ninja.setVelocity(new Vector(0.2f, 0f));
			cats.add(ninja);
			ninja = new Cat(screenWidth / 4, screenHeight - 200, setCatLevel(spike.level));
			ninja.setVelocity(new Vector(0.2f, 0f));
			cats.add(ninja);
			ninja = new Cat(3 * screenWidth / 4, screenHeight - 200, setCatLevel(spike.level));
			ninja.setVelocity(new Vector(-0.2f, 0f));
			cats.add(ninja);
			ninja = new Cat(screenWidth / 2, screenHeight - 200, setCatLevel(spike.level));
			ninja.setVelocity(new Vector(-0.2f, 0f));
			cats.add(ninja);
		}
	}
	
	private int randomLevel(){
		random = new Random();
		int x = (int)(random.nextFloat() * 100);
		System.out.println(x);
		if(x % 3 == 0 || x % 7 == 0)
			return 2;
		else if(x % 5 == 0 || x % 2 == 0){
			return 1;
		}
		else
			return 0;
	}
	
	private int numberOfCats(){
		random = new Random();
		float x = random.nextFloat();
		if(x <= 0.25)
			return 2;
		if(x > 0.25 && x <= 0.5)
			return 3;
		if(x > 0.5 && x <= 0.75)
			return 4;
		else
			return 5;
	}
	private int setCatLevel(int level){
		random = new Random();
		float x = random.nextFloat();
		switch(level){
		case 1:
			if(x <= 0.8){
				return 1;
			}
			else
				return 2;
		case 2:
			if(x <= 0.2){
				return 1;
			}
			if(x > 0.2 && x <= 0.9){
				return 2;
			}
			else
				return 3;
		case 3:
			if(x <= 0.3){
				return 2;
			}
			if(x > 0.3 && x <= 0.85){
				return 3;
			}
			else
				return 4;
		default:
			if(x <= 0.5){
				return 3;
			}
			else
				return 4;
		}
		
	}
	
	private int jump(){
		random = new Random();
		float x = random.nextFloat();
		System.out.println(x);
		if(x >= 0.5)
			return 0;
		else
			return 1;
	}
	
	public void chooseLevel(int i){
		
		int groundX, groundY;
		int t1X, t1Y;
		int t2X, t2Y;
		int p1X, p1Y;
		int p2X, p2Y;
		
		groundX = this.screenCenterX;
		
		switch (i) {
			case (0):
				groundY = this.screenHeight - 20;
				t1X = this.screenWidth / 4;
				t1Y = this.screenHeight - 70;
				t2X = 3 * this.screenWidth / 4;
				t2Y = this.screenHeight - 70;
				p1X = this.screenWidth / 4;
				p1Y = this.screenHeight - 130;
				p2X = 3 * this.screenWidth / 4;
				p2Y = this.screenHeight - 170;
				break;
			case (1):
				groundY = this.screenHeight - 20;
				t1X = this.screenWidth / 2;
				t1Y = this.screenHeight - 70;
				t2X = this.screenWidth / 2;
				t2Y = this.screenHeight - 160;
				p1X = this.screenWidth / 2;
				p1Y = this.screenHeight - 130;
				p2X = this.screenWidth / 2;
				p2Y = this.screenHeight - 250;
				break;
			default:
				groundY = this.screenHeight - 20;
				t1X = this.screenWidth / 3;
				t1Y = this.screenHeight - 70;
				t2X = this.screenWidth / 2;
				t2Y = this.screenHeight - 70;
				p1X = this.screenWidth / 3;
				p1Y = this.screenHeight - 120;
				p2X = this.screenWidth / 2;
				p2Y = this.screenHeight - 170;
		}
		
		this.g1 = world.new Ground(groundX, groundY);
		this.t1 = world.new Tower(t1X, t1Y, 0);
		this.t2 = world.new Tower(t2X, t2Y, 1);
		this.p1 = world.new Platform(p1X, p1Y);
		this.p2 = world.new Platform(p2X, p2Y);
	}
}
