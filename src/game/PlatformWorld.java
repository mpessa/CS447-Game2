package game;

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

public class PlatformWorld extends Entity{
	
	public PlatformWorld(){

	}

	public void chooseLevel(int i){
		if(i == 0){
			dogWarriors.g1 = new ground(dogWarriors.ScreenWidth / 2, dogWarriors.ScreenHeight - 20);
			dogWarriors.t1 = new tower(dogWarriors.ScreenWidth / 4, dogWarriors.ScreenHeight - 70, 0);
			dogWarriors.t2 = new tower(3 * dogWarriors.ScreenWidth / 4, dogWarriors.ScreenHeight - 70, 1);
			dogWarriors.p1 = new platform(dogWarriors.ScreenWidth / 4, dogWarriors.ScreenHeight - 130);
			dogWarriors.p2 = new platform(3 * dogWarriors.ScreenWidth / 4, dogWarriors.ScreenHeight - 170);
		}
		if(i == 1){
			dogWarriors.g1 = new ground(dogWarriors.ScreenWidth / 2, dogWarriors.ScreenHeight - 20);
			dogWarriors.t1 = new tower(dogWarriors.ScreenWidth / 2, dogWarriors.ScreenHeight - 70, 0);
			dogWarriors.t2 = new tower(dogWarriors.ScreenWidth / 2, dogWarriors.ScreenHeight - 190, 0);
			dogWarriors.p1 = new platform(dogWarriors.ScreenWidth / 2, dogWarriors.ScreenHeight - 130);
			dogWarriors.p2 = new platform(dogWarriors.ScreenWidth / 2, dogWarriors.ScreenHeight - 250);
		}
		if(i == 2){
			dogWarriors.g1 = new ground(dogWarriors.ScreenWidth / 2, dogWarriors.ScreenHeight - 20);
			dogWarriors.t1 = new tower(dogWarriors.ScreenWidth / 3, dogWarriors.ScreenHeight - 70, 0);
			dogWarriors.t2 = new tower(dogWarriors.ScreenWidth / 2, dogWarriors.ScreenHeight - 70, 1);
			dogWarriors.p1 = new platform(dogWarriors.ScreenWidth / 3, dogWarriors.ScreenHeight - 130);
			dogWarriors.p2 = new platform(dogWarriors.ScreenWidth / 2, dogWarriors.ScreenHeight - 170);
		}
	}
	
	class Background extends Entity{
		public Background(final float x, final float y){
			super(x,y);
			addImage(ResourceManager.getImage("resource/sky2.jpg"));
		}
	}
	
	public class ground extends Entity{
		
		public ground(int x, int y){
			super(x,y);
			this.addImageWithBoundingBox(ResourceManager.getImage("resource/ground800.png"));
		}
	}
	
	public class platform extends Entity{
		public platform(int x, int y){
			super(x, y);
			this.addImageWithBoundingBox(ResourceManager.getImage("resource/platform300.png"));
		}
	}
	
	public class tower extends Entity{
		public tower(int x, int y, int size){
			super(x, y);
			if(size == 0){
				this.addImage(ResourceManager.getImage("resource/tower300x100.png"));
			}
			if(size == 1){
				this.addImage(ResourceManager.getImage("resource/tower300x200.png"));
			}
		}
	}
	
	public class Dog extends Entity{
		public Vector speed;
		public boolean onP1,onP2, onGround;
		public int time;
		
		public Dog(int x, int y){
			super(x, y);
			this.addImageWithBoundingBox(ResourceManager.getImage("resource/brick2.png"));
			speed = new Vector(0f, 0f);
			onP1 = false;
			onP2 = false;
			onGround = false;
		}
		public void setVelocity(final Vector v){
			speed = v;
		}
		public Vector getVelocity(){
			return speed;
		}
		public void update(int delta){
			time -= delta;
			translate(speed.scale(delta));
		}
	}
}
