package GameMain;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import rendering.SpriteSheet;

/**
 * Class: Character
 * 
 * @author F23-A-403 <br>
 *         Purpose: Creates a basic for all moving characters, hero or enemy, to
 *         control basic movements and gravity. <br>
 *         Restrictions: Must be passed a color from subclass constructors
 */

public abstract class Character extends GameObject {

	private static final int X_OFFSET = -5;
	private static final int Y_OFFSET = -3;
	private static final int SPRITE_WIDTH = 32;
	private static final double BUFFER_VALUE = 0.1;
	private static final double FALL_RATE = -0.1;
	private static final double JUMP_RATE = 1.5;
	protected SpriteSheet spriteSheet;
	public static final double X_SIZE = 21;
	public static final double Y_SIZE = 29;
	private static final double LEFT_VELOCITY_INCREASE_VALUE = -0.1;
	private static final double RIGHT_VELOCITY_INCREASE_VALUE = 0.1;

	/**
	 * Creates a new character with the parent game component, specified x and y
	 * 
	 * @param gComponent the parent LevelGameComponent the character is contained in
	 * @param x          the xPos to create the character
	 * @param y          the yPos to create the character
	 */
	public Character(LevelGameComponent gComponent, double x, double y) {
		super(gComponent, x, y, X_SIZE, Y_SIZE);
	}

	/**
	 * Creates a new character with the parent game component, specified x and y,
	 * xSize and ySize
	 * 
	 * @param gComponent the parent LevelGameComponent the character is contained in
	 * @param x          the xPos to create the character
	 * @param y          the yPos to create the character
	 * @param xSize      the width of the hitbox of the character
	 * @param ySize      the height of the hitbox of the character
	 */
	public Character(LevelGameComponent gComponent, double x, double y, double xSize, double ySize) {
		super(gComponent, x, y, xSize, ySize);
	}

	/**
	 * Increases or decreases the x velocity by the increment value based on which
	 * way the character is moving
	 * 
	 * @param toIncrease should the velocity be increased or decreased
	 */
	public void updateXVelocity(boolean toIncrease) {
		if (toIncrease) {
			this.increaseXVelo(RIGHT_VELOCITY_INCREASE_VALUE);
		} else {
			this.increaseXVelo(LEFT_VELOCITY_INCREASE_VALUE);
		}
	}

	/**
	 * Increases or decreases the y velocity by the increment value based on which
	 * way the character is moving
	 * 
	 * @param toIncrease should the velocity be increased or decreased
	 */
	public void updateYVelocity(boolean toIncrease) {
		if (toIncrease) {
			this.increaseYVelo(JUMP_RATE);
		} else {
			this.increaseYVelo(FALL_RATE);
		}
	}

	/**
	 * Updates the sprite sheet based on the state of the character
	 */
	public void updateSpriteSheet() {
		SpriteSheet newSpriteSheet = this.getSpriteSheet("Idle");
		if (yVelo > 0) {
			newSpriteSheet = this.getSpriteSheet("Jump");
		} else if (yVelo < 0) {
			newSpriteSheet = this.getSpriteSheet("Fall");
		} else if (xVelo > BUFFER_VALUE || xVelo < -BUFFER_VALUE) {
			newSpriteSheet = this.getSpriteSheet("Run");
		}
		if (spriteSheet == null || !spriteSheet.equals(newSpriteSheet)) {
			spriteSheet = newSpriteSheet;
		}
	}

	@Override
	public void updateState() {
		updateYVelocity(false);
		super.updateState();

	}

	@Override
	public void drawOn(Graphics2D g) {
		updateSpriteSheet();
		BufferedImage img = spriteSheet.grabNextImage(this.getSpriteWidth());
		if (this.getMoveDirection() < 0) {

			img = SpriteSheet.flipImage(img);
		} else if (this.getMoveDirection() == 0 && this.xVelo < 0) {
			img = SpriteSheet.flipImage(img);
		}
		g.drawImage(img, (int) (xPos + this.getXOffset()), (int) (yPos + this.getYOffset()), gComponent);
	}

	public int getSpriteWidth() {
		return SPRITE_WIDTH;
	}

	public double getYOffset() {
		return Y_OFFSET;
	}

	public double getXOffset() {
		return X_OFFSET;
	}

	protected abstract int getMoveDirection();

	public abstract SpriteSheet getSpriteSheet(String state);

	@Override
	public void collidesWith(GameObject other) {
		other.collideWithCharacter(this);
	}

	@Override
	public void collideWithLava(Lava lava) {
		gComponent.addToToRemove(this);
	}

	@Override
	public void collideWithPlatform(Platform p) {
		bounce(p);
	}

	@Override
	public void collideWithCharacter(Character character) {
		this.bounce(character);
	}

}
