package game;

import org.newdawn.slick.Animation;
import org.newdawn.slick.SpriteSheet;

import jig.Entity;
import jig.ResourceManager;

class Bang extends Entity {
	private Animation explode;
	//private SpriteSheet explosion;

	public Bang(final float x, final float y) {
		super(x, y);
		explode = new Animation(ResourceManager.getSpriteSheet(
				DogWarriors.explosionImage[0], 64, 64), 0, 0, 22, 0, true, 50,
				true);
		addAnimation(explode);
		explode.setLooping(false);
		ResourceManager.getSound(DogWarriors.platformExplosion[0]).play();
	}

	public boolean isActive() {
		return !explode.isStopped();
	}
}