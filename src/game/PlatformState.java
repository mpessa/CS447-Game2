
package game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import java.awt.Font;

import jig.ResourceManager;
import jig.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.EmptyTransition;
import org.newdawn.slick.state.transition.HorizontalSplitTransition;
import org.newdawn.slick.state.transition.RotateTransition;

/**
 * State for the combat portion. Player character has various abilities based on it's
 * level and encounters enemies with similar abilities based on their levels. State 
 * exits once all enemy entities are killed or once the player character is killed.
 * 
 * @author Matthew Pessa
 *
 */
public class PlatformState extends BasicGameState {

	DogWarriors game; // game we are a state of
	GameContainer container; // gameContainer owned by the game
	
	public PlatformWorld world;
	private PlatformWorld.Background back;
	
	private int screenWidth, screenHeight;
	private int screenCenterX, screenCenterY;
	private int catX, catY;
	private int level, expEarned, deadCats, overTimer;
	
	private Font font1;
	private TrueTypeFont uFont1;
	
	private boolean levelOver;
	private boolean active;
	
	private Random random;
	
	public PlatformWorld.Ground g1;
	public PlatformWorld.Platform p1, p2;
	public PlatformWorld.Tower t1, t2;
	
	public Dog spike;
	public Cat ninja;
	public Possum boss;
	public ArrayList<Cat> cats;
	
	public Projectile fBall;
	public WaterBall ball;
	public Shield shield, fShield;
	public WaterShield wShield;
	public Powerup powerup;
	public ArrayList<Projectile> fire;
	public ArrayList<Shield> shields;
	public ArrayList<Powerup> powerups;
	public ArrayList<Bang> explode;
	
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
		for (String s : DogWarriors.possumImages) {
			ResourceManager.loadImage(s);
		}
		for (String s : DogWarriors.powerupImages) {
			ResourceManager.loadImage(s);
		}
		for (String s : DogWarriors.explosionImage) {
			ResourceManager.loadImage(s);
		}
		
		System.out.println("Loading Sounds...");
		for  (String s : DogWarriors.platformCatSounds){
			ResourceManager.loadSound(s);
		}
		for (String s : DogWarriors.platformSplashSound){
			ResourceManager.loadSound(s);
		}
		for (String s : DogWarriors.platformDogHitSound){
			ResourceManager.loadSound(s);
		}
		for (String s : DogWarriors.platformBoomSound){
			ResourceManager.loadSound(s);
		}
		for (String s : DogWarriors.platformDogKick){
			ResourceManager.loadSound(s);
		}
		for (String s : DogWarriors.platformExplosion){
			ResourceManager.loadSound(s);
		}
		for (String s : DogWarriors.music){
			ResourceManager.loadSound(s);
		}
		System.out.println("Done");
		
		this.font1 = new Font("Dialog", Font.BOLD, 36);
		this.uFont1 = new TrueTypeFont(font1, false);

		this.game = (DogWarriors) game;
		this.container = container;
			
		this.screenWidth = DogWarriors.ScreenWidth;
		this.screenHeight = DogWarriors.ScreenHeight;
		this.screenCenterX = screenWidth / 2;
		this.screenCenterY = screenHeight / 2;
		this.catX = 600;
		this.catY = 30;
		
		this.levelOver = false;
		this.active = false;
		
		this.cats = new ArrayList<Cat>(5);
		this.fire = new ArrayList<Projectile>(5);
		this.shields = new ArrayList<Shield>(5);
		this.powerups = new ArrayList<Powerup>(5);
		this.explode = new ArrayList<Bang>(10);
		this.world = new PlatformWorld();
		this.back = world.new Background(screenCenterX, screenCenterY);
		this.spike = new Dog(screenCenterX, screenHeight - 70);
		this.boss = new Possum(4 * screenWidth / 5, screenHeight - 150);
		this.ball = new WaterBall(screenWidth / 2, screenHeight / 2);
		this.wShield = new WaterShield(spike.getX(), spike.getY());
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		g.setColor(Color.black);
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
			g.drawString("Cat " + i + " HP: " + ninja.currentHP, catX, catY + (20 * i));
		}
		boss.render(g);
		if(spike.shot){
			spike.shoot.draw(spike.getX() - 24, spike.getY() - 24);
		}
		else if(spike.kTime > 0){
			spike.kick.draw(spike.getX() - 24, spike.getY() - 24);
		}
		else if((spike.onGround || spike.onP1 || spike.onP2)){
			spike.walk.draw(spike.getX() - 24, spike.getY() - 24);
		}
		else if(!spike.onGround && !spike.onP1 && !spike.onP2){
			spike.jump.draw(spike.getX() - 24, spike.getY() - 24);
		}
		if(boss.exists){
			if(boss.shooting){
				boss.shoot.draw(boss.getX() - 24, boss.getY() - 24);
			}
			else if(boss.kicking){
				boss.kick.draw(boss.getX() - 24, boss.getY() - 30);
			}
			else if(boss.onGround || boss.onP1 || boss.onP2){
				boss.walk.draw(boss.getX() - 24, boss.getY() - 24);
			}
			else if(!boss.onGround && !boss.onP1 && !boss.onP2){
				boss.jump.draw(boss.getX() - 24, boss.getY() - 10);
			}
		}
		for(int i = 0; i < cats.size(); i++){
			if(!boss.exists){
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
		}
		for(int i = 0; i < fire.size(); i++){
			fBall = fire.get(i);
			fBall.render(g);
			if(fBall.type == 0)
				fBall.fire.draw(fBall.getX(), fBall.getY());
		}
		if(ball.exists){
			ball.render(g);
		}
		if(wShield.exists){
			wShield.render(g);
		}
		for(int i = 0; i < shields.size(); i++){
			shield = shields.get(i);
			shield.render(g);
		}
		for(int i = 0; i < powerups.size(); i++){
			powerup = powerups.get(i);
			powerup.render(g);
		}
		g.drawString("HP: " + spike.currentHP + "/" + spike.maxHP, 0, 30);
		g.drawString("Slobber: " + spike.currentSlobber + "/" + spike.maxSlobber, 0, 50);
		g.drawString("Current Level: " + spike.level, 0, 70);
		g.drawString("Exp: " + spike.currentExp + "/" + spike.nextLevel, 0, 90);
		if(boss.exists){
			g.drawString("Boss HP: " + boss.currentHP, 600, 30);
		}
		if(levelOver){
			if(spike.levelUp){
				uFont1.drawString(screenCenterX / 3 + 20, screenHeight / 3 + 40, "You have gained a level!", Color.black);
				uFont1.drawString(screenCenterX / 3 + 50, screenHeight / 3 + 80, "You are now level " + spike.level, Color.black);
				if(spike.level == 2){
					g.drawString("You can now kick at your enemies. Hit 'K' to do a spinning kick", screenWidth / 7, screenHeight / 3 + 140);
				}
				if(spike.level == 3){
					g.drawString("You can now shoot slobber balls.", screenWidth / 3, screenHeight / 3 + 140);
					g.drawString("Hit 'J' to shoot a slobber ball in the direction you're facing", screenWidth / 6, screenHeight / 3 + 160);
				}
				if(spike.level == 4){
					g.drawString("You can now shield yourself with slobber. Hit 'L' to surround yourself with slobber", screenWidth / 7, screenHeight + 140);
				}
			}
			uFont1.drawString(screenCenterX / 2 - 30, screenHeight / 3, "Experience Earned: " + expEarned, Color.black);
		}
		for (Bang b : explode)
			b.render(g);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		
		Input input = container.getInput();
		processKeyInput(input);
		
		//Play the platform music
		if(!ResourceManager.getSound(DogWarriors.music[1]).playing() && !levelOver && active){
			ResourceManager.getSound(DogWarriors.music[1]).play();
		}
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
		
		for(int i = 0; i < powerups.size(); i++){
			powerup = powerups.get(i);
			if(powerup.collides(p1) != null || powerup.collides(p2) != null || powerup.collides(g1) != null){
				powerup.onGround = true;
				powerup.setVelocity(new Vector(0f, 0f));
			}
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
							if(ninja.speed.getX() < 0 || ninja.shoot == ninja.shootL){
								ninja.shoot = ninja.shootL;
								fBall = new Projectile(ninja.getX(), ninja.getY() - 10, -0.3f, 0f, 0, 1);
							}
							if(ninja.speed.getX() > 0 || ninja.shoot == ninja.shootR){
								ninja.shoot = ninja.shootR;
								fBall = new Projectile(ninja.getX(), ninja.getY() - 10, 0.3f, 0f, 0, 0);
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
				if(ninja.cooldown <= 0 && (spike.getY() < ninja.getY() + 20 || spike.getY() > ninja.getY() - 20)){
					if((!ninja.canShield && Math.abs(ninja.getX() - spike.getX()) >= 150) ||
							(ninja.canShield && Math.abs(ninja.getX() - spike.getX()) >= 200)){
						if(ninja.speed.getX() < 0 || ninja.walk == ninja.walkR){
							ninja.shoot = ninja.shootR;
							fBall = new Projectile(ninja.getX(), ninja.getY() - 10, 0.3f, 0f, 0, 0);
						}
						if(ninja.speed.getX() > 0 || ninja.walk == ninja.walkL){
							ninja.shoot = ninja.shootL;
							fBall = new Projectile(ninja.getX(), ninja.getY() - 10, -0.3f, 0f, 0, 1);
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
							&& ninja.cooldown <= 0 && !ninja.canShield){// && !fShield.exists){
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
						shield = new Shield(ninja.getX(), ninja.getY() - 10, 0);
						shields.add(shield);
						ninja.shielded = true;
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
						ninja.setVelocity(new Vector(0f, ninja.speed.getY()));
					}
				}
			}
		}
		
		//Boss movement
		if(boss.exists){
			if(boss.collides(g1) != null && boss.speed.getY() > 0) {
				boss.setVelocity(new Vector(boss.speed.getX(), 0f));
				if(!boss.kicking)
					boss.hitGround();
				boss.onGround = true;
			}
			
			if (boss.collides(p1) != null && boss.speed.getY() > 0 && 
					boss.getCoarseGrainedMaxY() >= p1.getY() - 20 &&
					boss.getCoarseGrainedMaxY() <= p1.getY()) {
				
				boss.setVelocity(new Vector(boss.speed.getX(), 0f));
				if(!boss.kicking)
					boss.hitGround();
				boss.onP1 = true;
			}
			if(boss.collides(p2) != null && boss.speed.getY() > 0 && 
					boss.getCoarseGrainedMaxY() >= p2.getY() - 20 &&
					boss.getCoarseGrainedMaxY() <= p2.getY()) {
				
				boss.setVelocity(new Vector(boss.speed.getX(), 0f));
				if(!boss.kicking)
					boss.hitGround();
				boss.onP2 = true;
			}
			if(boss.onP1){
				if(boss.getX() + 20 >= p1.getCoarseGrainedMaxX() || boss.getX() - 10 <= p1.getCoarseGrainedMinX()){
					boss.onP1 = false;
					boss.jump();
				}
			}
			if(boss.onP2){
				if(boss.getX() + 20 >= p2.getCoarseGrainedMaxX() || boss.getX() - 10 <= p2.getCoarseGrainedMinX()){
					boss.onP2 = false;
					boss.jump();
				}
			}
			if(boss.kTime <= 0 && boss.kicking){
				boss.kicking = false;
				boss.endKick();
			}
			if(boss.sTime <= 0 && boss.shooting){
				boss.shooting = false;
			}
			if(boss.getX() >= screenWidth || boss.getX() - 30 <= 0){
				boss.setVelocity(new Vector(-1 * boss.speed.getX(), boss.speed.getY()));
				boss.change = true;
			}
			if((boss.getX() - spike.getX()) <= 0 && boss.time <= 0 && !boss.shooting){
				boss.setVelocity(new Vector(0.25f, boss.speed.getY()));
				if(boss.walk != boss.walkR)
					boss.change = true;
			}
			if((boss.getX() - spike.getX()) > 0 && boss.time <= 0 && !boss.shooting){
				boss.setVelocity(new Vector(-0.25f, boss.speed.getY()));
				if(boss.walk != boss.walkL)
					boss.change = true;
			}
			if(Math.abs(boss.getX() - spike.getX()) <= 200 && spike.getY() < boss.getY() && (boss.onGround ||
					boss.onP1 || boss.onP2)){
				System.out.println("Jumping");
				boss.setVelocity(new Vector(boss.speed.getX(), -0.4f));
				boss.jump();
			}
			if(Math.abs(boss.getX() - spike.getX()) <= 150 && boss.cooldown <= 0){
				System.out.println("Kick");
				boss.cooldown = 2000;
				boss.kicking = true;
				boss.startKick();
				boss.kTime = 800;
				if(boss.walk == boss.walkR)
					boss.kick = boss.kickR;
				if(boss.walk == boss.walkL)
					boss.kick = boss.kickL;
				boss.kick.restart();
			}
			if(Math.abs(boss.getX() - spike.getX()) > 200 && boss.cooldown <= 0){
				System.out.println("Shoot");
				boss.cooldown = 2500;
				boss.shooting = true;
				boss.sTime = 900;
				boss.setVelocity(new Vector(0f, boss.speed.getY()));
				if(boss.walk == boss.walkR){
					boss.shoot = boss.shootR;
					fBall = new Projectile(boss.getX(), boss.getY(), 0.3f, 0f, 2, 0);
				}
				if(boss.walk == boss.walkL){
					boss.shoot = boss.shootL;
					fBall = new Projectile(boss.getX(), boss.getY(), -0.3f, 0f, 2, 1);
				}
				fire.add(fBall);
				boss.shoot.restart();
			}
		}
		
		//Cat and dog collisions
		for(int i = 0; i < cats.size(); i++) {
			ninja = cats.get(i);
			if(spike.collides(ninja) != null && ninja.hitTime <= 0 &&
				spike.getCoarseGrainedMaxY() < ninja.getY() - 10 && spike.speed.getY() >= 0 && !ninja.dead) {
				int sIndex =(int) Math.floor((Math.random() * 4));
				ResourceManager.getSound(DogWarriors.platformCatSounds[sIndex]).play();
				spike.setVelocity(spike.speed.negate());
				ninja.setVelocity(new Vector(0f, 0f));
				ninja.currentHP -= spike.attPwr;
				ninja.hitTime = 300;
				if(ninja.currentHP <= 0 && !ninja.dead){
					ninja.currentHP = 0;
					ninja.time = 570;
					ninja.dead = true;
					expEarned += ninja.exp;
					deadCats++;
					powerup = new Powerup(ninja.getX(), ninja.getY(), dropPowerup());
					if(powerup.type != -1)
						powerups.add(powerup);
					if(ninja.speed.getX() <= 0){
						ninja.die = ninja.dieL;
					}
				}
			}
			else if(spike.collides(ninja) != null && ninja.hitTime <= 0 && spike.kTime > 0 && !ninja.dead){
				int sIndex = 4 + (int) (Math.floor(Math.random() * 11));
				ResourceManager.getSound(DogWarriors.platformCatSounds[sIndex]).play();
				ninja.setVelocity(new Vector(0f, 0f));
				ninja.currentHP -= (0.5f * spike.spPwr);
				if(ninja.currentHP <= 0 && !ninja.dead){
					ninja.currentHP = 0;
					ninja.time = 570;
					ninja.dead = true;
					expEarned += ninja.exp;
					deadCats++;
					powerup = new Powerup(ninja.getX(), ninja.getY(), dropPowerup());
					if(powerup.type != -1)
						powerups.add(powerup);
				}
				ninja.hitTime = 300;
			}
			else if(spike.collides(ninja) != null && ninja.hitTime <= 0 && !ninja.dead){
				ResourceManager.getSound(DogWarriors.platformDogHitSound[0]).play();
				spike.setVelocity(new Vector(0f, 0f));
				spike.currentHP -= ninja.attPwr;
				ninja.hitTime = 300;
				if(spike.currentHP <= 0){
					killSpike();
					game.enterState(DogWarriors.STATES_GAMEOVER, new EmptyTransition(), new RotateTransition());
				}
			}
			if(ninja.dead && ninja.time <= 0){
				ninja.kill();
			}

			if(ball.collides(ninja) != null && ball.exists){
				ResourceManager.getSound(DogWarriors.platformSplashSound[0]).play();
				ninja.setVelocity(new Vector(0f, ninja.speed.getY()));
				ninja.currentHP -= spike.spPwr * 1.5;
				ball.exists = false;
				if(ninja.currentHP <= 0 && !ninja.dead){
					ninja.currentHP = 0;
					ninja.time = 570;
					ninja.dead = true;
					expEarned += ninja.exp;
					deadCats++;
					powerup = new Powerup(ninja.getX(), ninja.getY(), dropPowerup());
					if(powerup.type != -1)
						powerups.add(powerup);
				}
			}

			if(wShield.collides(ninja) != null && wShield.exists){
				ResourceManager.getSound(DogWarriors.platformSplashSound[0]).play();
				ninja.setVelocity(new Vector(0f, ninja.speed.getY()));
				ninja.currentHP -= spike.spPwr;
				wShield.exists = false;
				if(ninja.currentHP <= 0 && !ninja.dead){
					ninja.currentHP = 0;
					ninja.time = 570;
					ninja.dead = true;
					expEarned += ninja.exp;
					deadCats++;
					powerup = new Powerup(ninja.getX(), ninja.getY(), dropPowerup());
					if(powerup.type != -1)
						powerups.add(powerup);
				}
			}
		}
		if(boss.exists){
			if(boss.collides(spike) != null && spike.getY() < boss.getY() && !boss.kicking && boss.time <= 0){
				System.out.println("should be here");
				boss.currentHP -= spike.attPwr;
				spike.setVelocity(new Vector(-2 * spike.speed.getX(), -1f * spike.speed.getY()));
				boss.time = 300;
				boss.setVelocity(new Vector(0f, boss.speed.getY()));
				if(boss.currentHP <= 0){
					boss.exists = false;
					powerup = new Powerup(boss.getX(), boss.getY(), 2);
					powerups.add(powerup);
				}
			}
			else if(boss.collides(spike) != null && !boss.kicking && boss.time <= 0 && 
					spike.time <= 0 && !spike.kicking){
				System.out.println("Shouldn't be here");
				spike.setVelocity(new Vector(-2 * spike.speed.getX(), -2 * spike.speed.getY()));
				spike.time = 300;
				boss.time = 300;
				spike.currentHP -= boss.attPwr;
				if(spike.currentHP <= 0){
					killSpike();
					game.enterState(DogWarriors.STATES_GAMEOVER, new EmptyTransition(), new RotateTransition());
				}
			}
			else if(boss.collides(spike) != null && boss.time <= 0 && spike.kicking
					&& !boss.kicking){
				spike.setVelocity(new Vector(-2 * spike.speed.getX(), spike.speed.getY()));
				boss.currentHP -= spike.spPwr;
				boss.time = 500;
				if(boss.currentHP <= 0){
					boss.exists = false;
					powerup = new Powerup(boss.getX(), boss.getY(), 2);
					powerups.add(powerup);
				}
			}
			else if(boss.collides(spike) != null && boss.kicking && !spike.kicking &&
					boss.time <= 0 && spike.time <= 0){
				spike.setVelocity(new Vector(-2 * spike.speed.getX(), -2 * spike.speed.getY()));
				spike.time = 300;
				boss.time = 300;
				spike.currentHP -= (1.5 * boss.attPwr);
				if(spike.currentHP <= 0){
					//System.out.println("Kill Spike");
					killSpike();
					game.enterState(DogWarriors.STATES_GAMEOVER, new EmptyTransition(), new RotateTransition());
				}
			}
			else if(boss.collides(spike) != null && boss.kicking && spike.kicking &&
					boss.time <= 0 && spike.time <= 0){
				spike.time = 400;
				boss.time = 500;
				spike.setVelocity(new Vector(-2 * spike.speed.getX(), 0f));
				boss.setVelocity(new Vector(-2 * boss.speed.getX(), 0f));
			}

			if(ball.exists){
				if(boss.collides(ball) != null && ball.exists){
					boss.time = 400;
					ball.exists = false;
					boss.setVelocity(new Vector(0f, boss.speed.getY()));
					boss.currentHP -= (1.5 * spike.spPwr);
					if(boss.currentHP <= 0){
						boss.exists = false;
						powerup = new Powerup(boss.getX(), boss.getY(), 2);
						powerups.add(powerup);
					}
				}
			}

			if(boss.collides(wShield) != null && wShield.exists){
				boss.time = 400;
				wShield.exists = false;
				boss.setVelocity(new Vector(0f, boss.speed.getY()));
				boss.currentHP -= (1.5 * spike.spPwr);
				if(boss.currentHP <= 0){
					boss.exists = false;
				}
			}

			if(spike.getX() < 0 || spike.getX() > screenWidth){
				boss.currentHP = boss.maxHP;
				boss.setPosition(4 * screenWidth / 5, screenHeight - 150);
				boss.jump();
				game.enterState(DogWarriors.STATES_OVERWORLD, new EmptyTransition(), new RotateTransition());
			}
			boss.update(delta);
		}
		for(int i = 0; i < fire.size(); i++){
			fBall = fire.get(i);
			if(ball.collides(fBall) != null && ball.exists){
				ball.exists = false;
				ball.setVelocity(new Vector(0f, 0f));
				explode.add(new Bang(fBall.getX(), fBall.getY()));// adding explosion
				fire.remove(i);
			}
			if(fBall.collides(wShield) != null && wShield.exists){
				explode.add(new Bang(wShield.getX(), wShield.getY()));// adding explosion
				fire.remove(i);
				wShield.exists = false;
			}
			for(int j = 0; j < shields.size(); j++){
				shield = shields.get(j);		
				if(ball.collides(shield) != null && ball.exists && shield.type == 0){
					ball.exists = false;
					explode.add(new Bang(shield.getX(), shield.getY()));
					shields.remove(j);
				}
			}
			if(fBall.collides(spike) != null){
				fire.remove(i);
				spike.currentHP -= 20;
				ResourceManager.getSound(DogWarriors.platformBoomSound[0]).play();
				if(spike.currentHP <= 0){
					killSpike();
					game.enterState(DogWarriors.STATES_GAMEOVER, new EmptyTransition(), new RotateTransition());
				}
				if(ball.getX() > spike.getX()){
					spike.setVelocity(new Vector(-0.1f, spike.speed.getY()));
					spike.time = 200;
				}
				if(ball.getX() < spike.getX()){
					spike.setVelocity(new Vector(0.1f, spike.speed.getY()));
					spike.time = 200;
				}
			}
		}
		for(int i = 0; i < shields.size(); i++){
			shield = shields.get(i);
			if(wShield.collides(shield) != null && wShield.exists){
				shields.remove(i);
				wShield.exists = false;
				explode.add(new Bang(shield.getX(), shield.getY()));// adding explosion
			}

			if(spike.collides(shield) != null && shield.type != 1){
				ResourceManager.getSound(DogWarriors.platformBoomSound[1]).play();
				spike.setVelocity(new Vector(-2 * spike.speed.getX(), -2 * spike.speed.getY()));
				spike.time = 200;
				spike.currentHP -= 50;
				if(spike.currentHP <= 0){
					killSpike();
					game.enterState(DogWarriors.STATES_GAMEOVER, new EmptyTransition(), new RotateTransition());
				}
			}
		}
		for(int i = 0; i < powerups.size(); i++){
			powerup = powerups.get(i);
			if(spike.collides(powerup) != null){
				if(powerup.type == 0){
					spike.currentSlobber = spike.maxSlobber;
					powerups.remove(i);
				}
				if(powerup.type == 1){
					spike.currentHP += 50;
					if(spike.currentHP > spike.maxHP)
						spike.currentHP = spike.maxHP;
					powerups.remove(i);
				}
				if(powerup.type == 2){
					game.enterState(DogWarriors.STATES_WIN, new EmptyTransition(), new RotateTransition());
				}
			}
			powerup.update(delta);
			
			if(spike.getX() < 0 || spike.getX() > screenWidth){
				game.enterState(DogWarriors.STATES_OVERWORLD, new EmptyTransition(), new RotateTransition());
			}
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
		if(boss.exists && !boss.onGround && !boss.onP1 && !boss.onP2){
			boss.setVelocity(boss.speed.add(new Vector(0f, 0.01f)));
		}
		for(int i = 0; i < powerups.size(); i++){
			powerup = powerups.get(i);
			if(!powerup.onGround)
				powerup.setVelocity(powerup.speed.add(new Vector(0f, 0.01f)));
		}
		
		// Dog special abilities
		if (spike.kicking && spike.kTime <= 0) {
			spike.kicking = false;
			spike.endKick();
		}
		if (spike.shot && spike.sTime <= 40 && spike.sTime >= 20) {
			if(spike.direction == 1) {
				ball.setPosition(spike.getX(), spike.getY());
				ball.exists = true;
			}
			if(spike.direction == 0) {
				ball.setPosition(spike.getX() + 10, spike.getY());
				ball.exists = true;
			}
		}
		if (spike.shot && spike.sTime <= 0) {
			spike.shot = false;
			if(spike.shoot == spike.shootR) {
				ball.setVelocity(new Vector(0.5f, 0f));
			}
			if(spike.shoot == spike.shootL) {
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
		ball.update(delta);
		for(int i = 0; i < shields.size(); i++){
			shield = shields.get(i);
			for(int j = 0; j < cats.size(); j++){
				ninja = cats.get(j);
				if(shield.type == 0 && ninja.shielded){
					shield.setPosition(ninja.getX(), ninja.getY() - 10);
				}
			}
		}
		wShield.setPosition(spike.getX() - 5, spike.getY());
		spike.update(delta);
		if(cats.size() == deadCats && !levelOver && !boss.exists){
			ResourceManager.getSound(DogWarriors.music[1]).stop();
			spike.currentExp += expEarned;
			levelOver = true;
			System.out.println("Experience earned = " + expEarned);
			spike.levelUp();
		}
		if(levelOver){
			overTimer -= delta;
			if(overTimer <= 0){
				game.enterState(DogWarriors.STATES_OVERWORLD, new EmptyTransition(), new RotateTransition());
			}
		}
		
		for (Iterator<Bang> i = explode.iterator(); i.hasNext();) {
			if (!i.next().isActive()) {
				i.remove();
			}
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
			level = randomLevel();
			expEarned = 0;
			deadCats = 0;
			spike.onGround = false;
			spike.onP1 = false;
			spike.onP2 = false;
			spike.clearShapes();
			spike.jump();
			this.cats.clear();
			this.fire.clear();
			chooseLevel(level);
			this.levelOver = false;
			spike.levelUp = false;
			this.active = true;
			boss.exists = false;
			if(checkForBoss(spike.level) == 1){
				boss.exists = true;
			}
			if(!boss.exists){
				spike.setPosition(screenCenterX, screenHeight - 100);
				addCats();
			}
			else{
				spike.setPosition(screenWidth / 5, screenHeight - 100);
			}
			overTimer = 5000;
			break;
		}
	}

	@Override
	public void leave(GameContainer container, StateBasedGame game) {
		container.getInput().clearKeyPressedRecord();
		this.active = false;
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
			ResourceManager.getSound(DogWarriors.platformDogKick[0]).play();
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
			ResourceManager.getSound(DogWarriors.platformSplashSound[0]).play();
			spike.shot = true;
			spike.sTime = 800;
			spike.cooldown = 2000;
		}
		
		if(input.isKeyPressed(DogWarriors.CONTROLS_SHIELD) && spike.cooldown <= 0 && 
				spike.level >= 4 && spike.currentSlobber >= 4) {
			spike.currentSlobber -= 4;
			wShield.exists = true;
			ResourceManager.getSound(DogWarriors.platformSplashSound[0]).play();
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
		
		if (input.isKeyPressed(DogWarriors.CONTROLS_CHEAT_2)) { // Enter win state(for testing)
			game.enterState(DogWarriors.STATES_WIN, new EmptyTransition(), new RotateTransition());
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
		if(x % 3 == 0 || x % 7 == 0)
			return 2;
		else if(x % 5 == 0 || x % 2 == 0){
			return 1;
		}
		else
			return 0;
	}
	
	private int checkForBoss(int level){
		random = new Random();
		float x = random.nextFloat();
		if(level == 3){
			if(x <= 0.2){
				return 1;
			}
			else{
				return 0;
			}
		}
		else if(level >= 4){
			if(x <= 0.4){
				return 1;
			}
			else{
				return 0;
			}
		}
		else{
			return 0;
		}
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
		if(x >= 0.5)
			return 0;
		else
			return 1;
	}
	
	private int dropPowerup(){
		random = new Random();
		float x = random.nextFloat();
		if(x <= 0.2)
			return choosePowerup();
		else
			return -1;
	}
	
	private int choosePowerup(){
		random = new Random();
		float x = random.nextFloat();
		if(x >= 0.5)
			return 0;
		else
			return 1;
	}
	
	private void killSpike(){
		spike.level = 1;
		spike.maxHP = 100;
		spike.currentHP = spike.maxHP;
		spike.maxSlobber = 2;
		spike.currentSlobber = spike.maxSlobber;
		spike.attPwr = 50;
		spike.spPwr = 100;
		spike.currentExp = 0;
		spike.nextLevel = 250;
		cats.clear();
		powerups.clear();
		fire.clear();
		shields.clear();
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
