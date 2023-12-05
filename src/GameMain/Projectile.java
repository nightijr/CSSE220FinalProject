package GameMain;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import rendering.SpriteSheet;

/**
 * Class containing everything for a projectile to be fired from the shooter
 * enemy
 * 
 * @author F23-A-403
 *
 */
public class Projectile extends GameObject {

	private static final double PROJECTILE_MOVE_SPEED = 7.5;
	private static final int PROJECTILE_SIZE = 10;
	private static final int PROJECTILE_LIFETIME = 150;
	private int tickCounter;

	/**
	 * Creates a new projectile at the specifed x and y positions
	 * 
	 * @param gComponent parent LevelGameComponent
	 * @param xPos       initial x position
	 * @param yPos       initial y position
	 */
	public Projectile(LevelGameComponent gComponent, double xPos, double yPos) {
		super(gComponent, xPos, yPos, PROJECTILE_SIZE, PROJECTILE_SIZE, PROJECTILE_MOVE_SPEED, 0);
		tickCounter = 0;
	}

	@Override
	public void drawOn(Graphics2D g) {
		try {
			BufferedImage img = ImageIO.read(new File("images/ShooterEnemy/Bullet.png"));
			if (xVelo > 0) {
				img = SpriteSheet.flipImage(img);
			}
			g.drawImage(img, (int) xPos, (int) yPos, gComponent);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public double getMoveSpeed() {
		return PROJECTILE_MOVE_SPEED;
	}

	@Override
	public void collidesWith(GameObject other) {

	}

	@Override
	public void updateState() {
		super.updateState();
		tick();
	}

	private void tick() {
		tickCounter++;

		if (tickCounter >= PROJECTILE_LIFETIME) {
			gComponent.addToToRemove(this);
		}

	}

	@Override
	public void collideWithPlatform(Platform p) {
		gComponent.addToToRemove(this);
	}

	@Override
	public void collideWithCharacter(Character character) {

	}

	@Override
	public void collideWithHero(Hero hero) {
		hero.killHero(false);
		gComponent.addToToRemove(this);
	}

	@Override
	public void collideWithLava(Lava lava) {

	}

}
