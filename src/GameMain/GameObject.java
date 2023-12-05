package GameMain;

import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * Class: GameObject
 * 
 * @author F23-A-403 <br>
 *         Purpose: Handle velocities as well as interactions, like collisions,
 *         between objects in this class
 */

public abstract class GameObject {
	private static final double HORIZONTAL_BOUNCE_VALUE = -0.75;
	private static final double VERTICAL_BOUNCE_VALUE = -0.25;
	protected double xPos;
	protected double yPos;
	protected double xSize;
	protected double ySize;
	protected double xVelo;
	protected double yVelo;
	protected LevelGameComponent gComponent;
	private Rectangle.Double boundingRect;
	private static final double VELOCITY_CAP = 5;

	/**
	 * Creates a new GameObject with parent gComponent, x position, y position,
	 * width, height, x velocity, and y velocity
	 * 
	 * @param gComponent parent LevelGameComponent
	 * @param xPos       x position for the object to be created at
	 * @param yPos       y position for the object to be created at
	 * @param xSize      width of the hitbox of the object
	 * @param ySize      height of the hitbox of the object
	 * @param xVelo      initial x velocity for the object
	 * @param yVelo      initial y velocity for the object
	 */
	public GameObject(LevelGameComponent gComponent, double xPos, double yPos, double xSize, double ySize, double xVelo,
			double yVelo) {
		this.gComponent = gComponent;
		this.xPos = xPos;
		this.yPos = yPos;
		this.xSize = xSize;
		this.ySize = ySize;
		this.setXVelo(xVelo);
		this.yVelo = yVelo;
		boundingRect = new Rectangle.Double(xPos, yPos, xSize, ySize);
	}

	/**
	 * Creates a new GameObject with parent gComponent, x position, y position,
	 * width, height, with 0 initial velocity
	 * 
	 * @param gComponent parent LevelGameComponent
	 * @param xPos       x position for the object to be created at
	 * @param yPos       y position for the object to be created at
	 * @param xSize      width of the hitbox of the object
	 * @param ySize      height of the hitbox of the object
	 */
	public GameObject(LevelGameComponent gComponent, double xPos, double yPos, double xSize, double ySize) {
		this.gComponent = gComponent;
		this.xPos = xPos;
		this.yPos = yPos;
		this.xSize = xSize;
		this.ySize = ySize;
		this.setXVelo(0);
		this.yVelo = 0;
		boundingRect = new Rectangle.Double(xPos, yPos, xSize, ySize);
	}

	/**
	 * Increases the x velocity by the amount specified
	 * 
	 * @param toIncrease double for the amount to increase the x velo by
	 */
	public void increaseXVelo(double toIncrease) {
		setXVelo(getXVelo() + toIncrease);
		checkVelocityCap();
	}

	/**
	 * Increases the y velocity by the amount specified
	 * 
	 * @param toIncrease double for the amount to increase the y velo by
	 */
	public void increaseYVelo(double toIncrease) {
		yVelo += toIncrease;
		checkVelocityCap();
	}

	/**
	 * Checks and updates the velocity if it becomes more than the velocity cap
	 */
	public void checkVelocityCap() {
		if (getXVelo() < -VELOCITY_CAP) {
			setXVelo(-VELOCITY_CAP);
		} else if (getXVelo() > VELOCITY_CAP) {
			setXVelo(VELOCITY_CAP);
		}
	}

	/**
	 * Bounces the object in the vertical direction
	 */
	public void verticalBounce() {
		yVelo *= VERTICAL_BOUNCE_VALUE;
	}

	/**
	 * updates the object's position based on the current x velocity
	 */
	public void updateXPos() {
		xPos += getXVelo();
	}

	/**
	 * updates the object's position based on the current y velocity
	 */
	public void updateYPos() {
		yPos -= yVelo;
	}

	/**
	 * Resets the object to a new position on the screen and resets the velocities
	 * 
	 * @param xPos2 x position to reset to
	 * @param yPos2 y position to reset to
	 */
	public void resetObjectToNewPos(double xPos2, double yPos2) {
		xPos = xPos2;
		yPos = yPos2;
		setXVelo(0);
		yVelo = 0;
	}

	/**
	 * Checks where and object collides with something and then bounces them the
	 * opposite direction
	 * 
	 * @param obj the GameObject to be bounced
	 */
	public void bounce(GameObject obj) {
		Rectangle.Double thisRect = this.getPreviousBoundingBox();
		double thisTopEdge = thisRect.y;
		double thisBottomEdge = thisRect.y + thisRect.height;
		double thisRightEdge = thisRect.x + thisRect.width;
		double thisLeftEdge = thisRect.x;
		Rectangle.Double otherRect = obj.getBoundingBox();
		double otherTopEdge = otherRect.y;
		double otherBottomEdge = otherRect.y + otherRect.height;
		double otherRightEdge = otherRect.x + otherRect.width;
		double otherLeftEdge = otherRect.x;
		boolean leftBound = thisRightEdge <= otherLeftEdge;
		boolean rightBound = thisLeftEdge >= otherRightEdge;
		if (getXVelo() > 0 && leftBound) {
			xPos = obj.xPos - this.xSize;
			horizontalBounce();
		} else if (getXVelo() < 0 && rightBound) {
			xPos = obj.xPos + obj.xSize;
			horizontalBounce();
		} else if (yVelo < 0 && thisBottomEdge <= otherTopEdge && !leftBound && !rightBound) {
			yPos = obj.yPos - this.ySize;
			yVelo = 0;
		} else if (yVelo > 0 && thisTopEdge >= otherBottomEdge && !leftBound && !rightBound) {
			yPos = obj.yPos + obj.ySize;
			verticalBounce();
		}
	}

	/**
	 * Uses the current velocities to calculate what the previous bounding box was:
	 * useful for detecting collisions
	 * 
	 * @return
	 */
	public Rectangle.Double getPreviousBoundingBox() {
		return new Rectangle.Double(boundingRect.x - xVelo, boundingRect.y + yVelo, boundingRect.width,
				boundingRect.height);
	}

	/**
	 * Bounces the current object in the horizontal direction
	 */
	public void horizontalBounce() {
		setXVelo(getXVelo() * HORIZONTAL_BOUNCE_VALUE);
	}

	/**
	 * Checks and updates the object based on the screen bounds
	 */
	public void handleScreenBounds() {
		if (this.getBoundingBox().getCenterX() > gComponent.getWidth()) {
			this.xPos = 0 - xSize / 2;
		} else if (this.getBoundingBox().getCenterX() < 0) {
			this.xPos = gComponent.getWidth() - xSize / 2;
		}

		if (this.yPos < 0) {
			this.yPos = 0;
			verticalBounce();
		}
	}

	/**
	 * Updates the hitbox of the object based on the new x position and y position
	 */
	public void updateBoundingBox() {
		boundingRect = new Rectangle.Double(xPos, yPos, xSize, ySize);
	}

	public Rectangle.Double getBoundingBox() {
		return this.boundingRect;
	}

	public abstract void drawOn(Graphics2D g);

	/**
	 * Called every frame and handles everything for updating the state of the
	 * object
	 */
	public void updateState() {
		updateXPos();
		updateYPos();
		updateBoundingBox();
		handleScreenBounds();
	}

	public abstract void collidesWith(GameObject other);

	public abstract void collideWithPlatform(Platform p);

	public abstract void collideWithLava(Lava lava);

	/**
	 * Determines if the two objects are overlapping
	 * 
	 * @param obj the other object to overlap with
	 * @return if the current object overlaps the other object
	 */
	public boolean overlaps(GameObject obj) {
		return getBoundingBox().intersects(obj.getBoundingBox());
	}

	public abstract void collideWithCharacter(Character character);

	public double getXVelo() {
		return xVelo;
	}

	public void setXVelo(double xVelo) {
		this.xVelo = xVelo;
	}

	public abstract void collideWithHero(Hero hero);
}
