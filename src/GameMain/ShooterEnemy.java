package GameMain;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import rendering.SpriteSheet;

/**
 * Class containing everything needed for the ShooterEnemy, extends the Enemy
 * class
 * 
 * @author F23-A-403
 *
 */
public class ShooterEnemy extends Enemy {

	private static final int SPRITE_WIDTH = 64;
	private static final int FIRING_INTERVAL = 1;
	private int tickCount;

	/**
	 * Creates a new shooter enemy at the x and y position
	 * 
	 * @param gComponent parent LevelGameComponent
	 * @param x          initial x position
	 * @param y          initial y position
	 */
	public ShooterEnemy(LevelGameComponent gComponent, double x, double y) {
		super(gComponent, x, y, "Shooter");
		tickCount = 0;
	}

	@Override
	public SpriteSheet getSpriteSheet(String state) {
		if (state.equals("Jump") || state.equals("Fall")) {
			return super.getSpriteSheet("Idle");
		}
		return super.getSpriteSheet(state);
	}

	@Override
	public int getSpriteWidth() {
		return SPRITE_WIDTH;
	}

	@Override
	public void drawOn(Graphics2D g) {
		updateSpriteSheet();
		BufferedImage img = spriteSheet.grabNextImage(this.getSpriteWidth());
		if (this.getMoveDirection() > 0) {

			img = SpriteSheet.flipImage(img);
		} else if (this.getMoveDirection() == 0 && this.xVelo > 0) {
			img = SpriteSheet.flipImage(img);
		}
		g.drawImage(img, (int) (xPos + this.getXOffset()), (int) (yPos + this.getYOffset()), gComponent);
	}

	@Override
	public void tick() {
		super.tick();
		tickCount++;
		if (tickCount >= FIRING_INTERVAL) {
			shoot();
			tickCount = 0;
		}
	}

	@Override
	public double getXOffset() {
		return -20;
	}

	private void shoot() {
		gComponent.createProjectile(super.xPos, super.yPos, this.getMoveDirection());
	}

}
