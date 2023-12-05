package GameMain;

import java.awt.Color;
import java.util.Random;

import rendering.SpriteSheet;

/**
 * Class: Enemy
 * 
 * @author F23-A-403 <br>
 *         Purpose: Using the basis create by the Character class, extends that
 *         basic with sole regard to all enemies
 */

public class Enemy extends Character {
	private static final int VERTICAL_ELIMINATION_VALUE = 4;
	private static final int STARTING_RAND_TICKS = 50;
	private static final int RIGHT_MOVEMENT = 1;
	private static final int LEFT_MOVEMENT = -1;
	private static final int DEFAULT_RANDOM_TICK_RANGE = 25;
	private static final int DEFAULT_TICKS_BEFORE_RANDOM_UPDATE = 150;

	private int numRandTicks;
	private int numJumpTicks;
	private int moveDirection;
	private int randomTickRange;
	private int ticksBeforeUpdate;
	private String type;

	/**
	 * Creates an enemy at the specified location
	 * 
	 * @param gComponent parent LevelGameComponent
	 * @param x          x position to be created at
	 * @param y          y position to be created at
	 */
	public Enemy(LevelGameComponent gComponent, double x, double y) {
		super(gComponent, x, y);
		numRandTicks = STARTING_RAND_TICKS;
		numJumpTicks = STARTING_RAND_TICKS;
		randomTickRange = DEFAULT_RANDOM_TICK_RANGE;
		ticksBeforeUpdate = DEFAULT_TICKS_BEFORE_RANDOM_UPDATE;
		this.type = "Basic";
	}

	/**
	 * Creates an enemy at the specified location
	 * 
	 * @param gComponent parent LevelGameComponent
	 * @param x          x position to be created at
	 * @param y          y position to be created at
	 * @param type       the type of the enemy being created
	 */
	public Enemy(LevelGameComponent gComponent, double x, double y, String type) {
		super(gComponent, x, y);
		numRandTicks = STARTING_RAND_TICKS;
		numJumpTicks = STARTING_RAND_TICKS;
		randomTickRange = DEFAULT_RANDOM_TICK_RANGE;
		ticksBeforeUpdate = DEFAULT_TICKS_BEFORE_RANDOM_UPDATE;
		this.type = type;
	}

	/**
	 * Creates an enemy at the specified location
	 * 
	 * @param gComponent      parent LevelGameComponent
	 * @param x               x position to be created at
	 * @param y               y position to be created at
	 * @param randomTickRange the tick range for the random update of the enemy
	 * @param ticksBefore     ticks before the random update
	 * @param type            the type of the enemy being created
	 */
	public Enemy(LevelGameComponent gComponent, double x, double y, int randomTickRange, int ticksBefore, String type) {
		super(gComponent, x, y);
		numRandTicks = STARTING_RAND_TICKS;
		numJumpTicks = STARTING_RAND_TICKS;
		this.ticksBeforeUpdate = ticksBefore;
		this.randomTickRange = randomTickRange;
		this.type = type;
	}

	@Override
	public void updateState() {
		tick();
		super.updateState();
	}

	/**
	 * Called every tick for the ai movement
	 */
	public void tick() {
		if (numJumpTicks < 0) {
			updateYVelocity(true);
			numJumpTicks = (int) (Math.random() * randomTickRange);
		}
		numJumpTicks--;

		if (numRandTicks > ticksBeforeUpdate) {
			this.randomizeMovement();
			numRandTicks = 0;
		}
		numRandTicks++;

		if (moveDirection == LEFT_MOVEMENT) {
			updateXVelocity(false);
		} else if (moveDirection == RIGHT_MOVEMENT) {
			updateXVelocity(true);
		}
	}

	@Override
	public SpriteSheet getSpriteSheet(String state) {
		SpriteSheet ss = new SpriteSheet("images/" + this.type + "Enemy/" + state + ".png");
		return ss;
	}

	@Override
	protected int getMoveDirection() {
		return moveDirection;
	}

	/**
	 * Randomizes the movement for the enemy
	 */
	public void randomizeMovement() {
		Random r = new Random();
		r.doubles();
		double randDouble = r.nextDouble();
		if (randDouble < 0.5) { // should move left half the time and right half the time
			moveDirection = LEFT_MOVEMENT;
		} else {
			moveDirection = RIGHT_MOVEMENT;
		}
	}

	@Override
	public void collideWithHero(Hero hero) {
		if (hero.yPos < this.yPos - VERTICAL_ELIMINATION_VALUE) {
			gComponent.convertEnemyToEgg(this);
		} else if (hero.yPos > this.yPos + VERTICAL_ELIMINATION_VALUE) {
			hero.killHero(false);
			this.bounce(hero);
		} else {
			this.bounce(hero);
		}
	}
}
