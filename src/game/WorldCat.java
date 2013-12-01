package game;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Random;

public class WorldCat extends Entity {
	public Point NW, NE, SW, SE;
	private Vector velocity;
	private int worldX = 30, worldY = 30;
	private int screenX, screenY;
	private float destinationX = 0;
	private float destinationY = 0;
	private int changePath = 1000;
	private int movePath = 30;
	private int randomInt;
	Random rand = new Random();
	
	public WorldCat(final float x, final float y, final float vx, final float vy) throws SlickException{
		super(x,y);
		this.velocity = new Vector(0.0f, 0.0f);
		this.initialize();
		
		
	}
	public void setVelocity(final Vector v){
		this.velocity = v;
	}

	public Vector getVelocity(){
		return this.velocity;
	}
	
	public void render(final float sx, final float sy, Graphics g){
		screenX = (int)sx;
		screenY = (int)sy;
		this.setX(worldX - screenX);
		this.setY(worldY - screenY);

		System.out.println("x = " + this.getX() + ", y =" + this.getY());
		//Renders the cat if it is currently on screen
		if(this.getX() > sx && this.getX() < sx + DogWarriors.ScreenWidth){
			if(this.getY() > sy && this.getY() < sy + DogWarriors.ScreenHeight){
				this.render(g);
			}
		}
		
	}

	public void update(final int delta, final float sx, final float sy) {
		screenX = (int)sx;
		screenY = (int)sy;
		//int i = 0;//rand.nextInt(4);
		this.setX(worldX - screenX );
		this.setY(worldY - screenY);
		changePath -= delta;
		System.out.println("changePath= " + changePath);
		if(changePath <= 0){
			randomInt = rand.nextInt(4);
			changePath = 2000;
			System.out.println(randomInt + " is selected \n");
		}
		
		if(randomInt == 0){	
			if(movePath >= 0 && worldX <= 30*32 - 10){
				worldX += 1;
				movePath -= 1;
			}
			else{
				changePath = 0;
				movePath = 30;
			}
		}
		
		else if(randomInt ==1 && worldX >= 0 +10){
			if(movePath >= 0){
				worldX -= 1;
				movePath -= 1;
				}
			else{
				changePath = 0;
				movePath = 30;
			}
		}
		
		else if(randomInt == 2 ){
			if(movePath >= 0 && worldY <= 30 * 32 - 13){
				worldY += 1;
				movePath -= 1;
			}
			else{
				changePath = 0;
				movePath = 30;
			}
		}
		
		else if(randomInt == 3 ){
			if(movePath >= 0 && worldY > 0 + 13){
				worldY -= 1;
				movePath -= 1;
			}
			else{
				changePath = 0;
				movePath = 30;
			}
		}
	
		//	destinationX = getWorldValue();		
			//destinationY = (int)Math.random()*200 +(worldX - screenX)/100;
			System.out.print("DestinationX is " + destinationX + " DestinationY is " + destinationY + "\n");
			//worldX += 1;
			//velocity.setY(getY() + destinationY).scale(delta);
			//changePath = 1000;
			
		//}
		//if(worldX <= destinationX + worldX && worldX <= 30*32 - 10){
			//worldX += 1;
			 
		//}
		//translate(velocity.scale(delta));		
	}
	public int getWorldValue(){
		return rand.nextInt(10*32);
	}
	
	private void initialize() {
		this.addImageWithBoundingBox(ResourceManager.getImage(DogWarriors.worldImages[0]));
		this.NW = new Point((int) this.getCoarseGrainedMinX(), (int) this.getCoarseGrainedMinY());
		this.NE = new Point((int) this.getCoarseGrainedMaxX(), (int) this.getCoarseGrainedMinY());
		this.SW = new Point((int) this.getCoarseGrainedMinX(), (int) this.getCoarseGrainedMaxY());
		this.SE = new Point((int) this.getCoarseGrainedMaxX(), (int) this.getCoarseGrainedMaxY());
	}
	
	public boolean isOnscreen(Rectangle screen) {
		boolean onscreen = false;
		onscreen = (screen.contains(NW) || screen.contains(NE) || screen.contains(SW) || screen.contains(SE));
		return onscreen;
	}
	
}
