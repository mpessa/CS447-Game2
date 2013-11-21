package game;

import java.util.ArrayList;

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
		
		this.cats = new ArrayList<Cat>(5);
		this.fire = new ArrayList<Fireball>(5);
		this.ball = new WaterBall(screenCenterX, screenCenterY);
		this.shield = new WaterShield(screenCenterX, screenCenterY);
		this.world = new PlatformWorld();
		this.back = world.new Background(screenCenterX, screenCenterY);
		this.spike = new Dog(screenCenterX, screenHeight - 70);
		
		this.chooseLevel(0);
		this.addCats();

	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		
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
					ninja.getCoarseGrainedMaxY() <= p1.getY()) {
				
				ninja.setVelocity(new Vector(ninja.speed.getX(), 0f));
				ninja.jump.stop();
				ninja.onP1 = true;
			}
			
			if(ninja.collides(p2) != null && ninja.speed.getY() > 0 && 
					ninja.getCoarseGrainedMaxY() >= p2.getY() - 20 &&
					ninja.getCoarseGrainedMaxY() <= p2.getY()) {
				
				ninja.setVelocity(new Vector(ninja.speed.getX(), 0f));
				ninja.jump.stop();
				ninja.onP2 = true;
			}
			if(ninja.time <= 0 && ninja.level <= 2) {
				ninja.setVelocity(new Vector(ninja.speed.getX(), -0.38f));
				ninja.jump.restart();
				ninja.time = 2000;
				ninja.onP1 = false;
				ninja.onP2 = false;
				ninja.onGround = false;
			}
			
			if(ninja.time <= 0 && ninja.level == 3) {
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
			
			if(ninja.time <= 0 && ninja.level == 4) {
				if(ninja.speed.getX() < 0){
					ninja.shoot = ninja.shootL;
					fBall = new Fireball(ninja.getX() - 20, ninja.getY(), -0.1f, 0f, 1);
				}
				if(ninja.speed.getX() > 0) {
					ninja.shoot = ninja.shootR;
					fBall = new Fireball(ninja.getX(), ninja.getY(), 0.1f, 0f, 0);
				}
				
				fire.add(fBall);
				ninja.setVelocity(new Vector(0f, ninja.speed.getY()));
				ninja.time = 1000;
				ninja.sTime = 450;
				ninja.shoot.restart();
			}
			
			if ((ninja.getCoarseGrainedMaxX() <= p1.getCoarseGrainedMinX() 
					|| ninja.getCoarseGrainedMinX() >= p1.getCoarseGrainedMaxX())
					&& !ninja.onGround && ninja.onP1){
				ninja.onP1 = false;
			}
			
			if ((ninja.getCoarseGrainedMaxX() <= p2.getCoarseGrainedMinX() 
					|| ninja.getCoarseGrainedMinX() >= p2.getCoarseGrainedMaxX())
					&& !ninja.onGround && ninja.onP2){
				ninja.onP2 = false;
			}
			
			if (ninja.getCoarseGrainedMinX() <= 0 || ninja.getCoarseGrainedMaxX() >= screenWidth) {
				ninja.setVelocity(new Vector(-ninja.speed.getX(), ninja.speed.getY()));
				ninja.change = true;
			}
			
			if(ninja.level == 4 && fShield.exists) {
				fShield.setPosition(ninja.getX() - 6, ninja.getY() - 12);
			}
		}
		
		//Cat and dog collisions
		for(int i = 0; i < cats.size(); i++) {
			ninja = cats.get(i);
			if(spike.collides(ninja) != null && ninja.hitTime <= 0) {
				if(spike.getCoarseGrainedMaxY() > ninja.getY() - 10) {
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
		if(!spike.onGround && !spike.onP1 && !spike.onP2) {
			spike.setVelocity(spike.speed.add(new Vector(0f, 0.01f)));
		}
		for(int i = 0; i < cats.size(); i++) {
			ninja = cats.get(i);
			if(!ninja.onGround && !ninja.onP1 && !ninja.onP2) {
				ninja.setVelocity(ninja.speed.add(new Vector(0f, 0.01f)));
			}
			ninja.update(delta);
		}
		
		// Dog special abilities
		if (spike.kicking && spike.kTime <= 0) {
			spike.kicking = false;
			spike.endKick();
		}
		
		if (ball.exists && spike.shot && spike.sTime > 0) {
			if(spike.direction == 1) {
				ball.setPosition(spike.getX() - 30, spike.getY());
			}
			if(spike.direction == 0) {
				ball.setPosition(spike.getX() + 10, spike.getY());
			}
		}
		if (ball.exists && spike.shot && spike.sTime <= 0) {
			spike.shot = false;
			if(spike.direction == 0) {
				ball.setVelocity(new Vector(0.5f, 0f));
			}
			if(spike.direction == 1) {
				ball.setVelocity(new Vector(-0.5f, 0f));
			}
		}
		for (int i = 0; i < fire.size(); i++) {
			// System.out.println(i);
			fBall = fire.get(i);
			fBall.update(delta);
		}
		if (shield.exists) {
			shield.setPosition(spike.getX() - 5, spike.getY());
		}
		if (ball.exists) {
			ball.update(delta);
		}			
		spike.update(delta);
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
		if (input.isKeyDown(DogWarriors.CONTROLS_RIGHT)) {
			spike.setVelocity(new Vector(0.2f, spike.speed.getY()));
			if(spike.direction == 1) {
				spike.change = true;
			}
			spike.walk.start();
		}
		
		if (input.isKeyDown(DogWarriors.CONTROLS_LEFT)) {
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
		
		if(input.isKeyPressed(DogWarriors.CONTROLS_KICK) && spike.cooldown <= 0 && spike.level >= 2) {
			spike.kick.restart();
			spike.startKick();
			spike.kicking = true;
			spike.kTime = 1000;
			spike.cooldown = 1500;
		}
		
		if(input.isKeyPressed(DogWarriors.CONTROLS_SHOOT) && !spike.shot && spike.cooldown <= 0 && spike.level >= 3) {
			if(spike.direction == 0) {
				spike.shoot = spike.shootR;
			}
			if(spike.direction == 1) {
				spike.shoot = spike.shootL;
			}
			spike.shoot.restart();
			spike.shot = true;
			ball.exists = true;
			spike.sTime = 800;
			spike.cooldown = 2000;
		}
		
		if(input.isKeyPressed(DogWarriors.CONTROLS_SHIELD) && spike.cooldown <= 0 && spike.level >= 4) {
			shield.exists = true;
			spike.cooldown = 1500;
		}
		
		if(spike.getCoarseGrainedMinX() < 0 && input.isKeyDown(DogWarriors.CONTROLS_LEFT)){
			spike.setVelocity(new Vector(0.0f, spike.speed.getY()));
		}
		
		if(spike.getCoarseGrainedMaxX() > screenWidth && input.isKeyDown(DogWarriors.CONTROLS_RIGHT)){
			spike.setVelocity(new Vector(0f, spike.speed.getY()));
		}
		
		if(!input.isKeyDown(DogWarriors.CONTROLS_RIGHT) && !input.isKeyDown(DogWarriors.CONTROLS_LEFT)){
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
		ninja = new Cat(3 * screenWidth / 4, screenHeight - 70, 1);
		ninja.setVelocity(new Vector(-0.2f, 0f));
		cats.add(ninja);
		ninja = new Cat(screenWidth / 4, screenHeight - 70, 2);
		ninja.setVelocity(new Vector(0.2f, 0f));
		cats.add(ninja);
		ninja = new Cat(screenWidth / 3, screenHeight - 70, 3);
		ninja.setVelocity(new Vector(-0.2f, 0f));
		cats.add(ninja);
		ninja = new Cat(screenWidth / 4, screenHeight - 200, 4);
		ninja.setVelocity(new Vector(0.2f, 0f));
		fShield = new FireShield(ninja.getX(), ninja.getY());
		cats.add(ninja);
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
				t2Y = this.screenHeight - 190;
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
				p1Y = this.screenHeight - 130;
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
