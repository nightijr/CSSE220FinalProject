package GameMain;

import rendering.SpriteSheet;

/**
 * Class for the egg that is created when an enemy is killed by the player
 * 
 * @author F23-A-403
 *
 */
public class Egg extends Character {
	private static final int XY_OFFSET = -9;

	private static final int SCORE_INCREASE_NUM = 1000;

	private static final int RESPAWN_DELAY = 500;

	private static final double X_SIZE = 14;
	private static final double Y_SIZE = 14;
	private int tickCount;
	private Enemy enemy;

	/**
	 * Creates a new Egg at the specified position with the enemy that was killed to
	 * create it
	 * 
	 * @param gComponent parent LevelGameComponent that the egg is contained in
	 * @param x          x position that the egg is located at
	 * @param y          y position that the egg is located at
	 * @param xVelo      initial x velocity for the egg
	 * @param enemy      the enemy that was killed to create the enemy
	 */
	public Egg(LevelGameComponent gComponent, double x, double y, double xVelo, Enemy enemy) {
		super(gComponent, x, y, X_SIZE, Y_SIZE);
		this.setXVelo(0);
		this.tickCount = 0;
		this.enemy = enemy;
	}

	@Override
	public void collideWithHero(Hero hero) {
		gComponent.addScore(SCORE_INCREASE_NUM);
		gComponent.removeEgg(this);

	}

	@Override
	public void updateState() {
		super.updateState();
		this.tickCount++;
		if (this.tickCount >= RESPAWN_DELAY) {
			gComponent.convertEggToEnemy(this, enemy);
		}

	}

	@Override
	public SpriteSheet getSpriteSheet(String state) {
		SpriteSheet ss = new SpriteSheet("images/Egg.png");
		return ss;
	}

	@Override
	protected int getMoveDirection() {
		return 0;
	}

	@Override
	public double getXOffset() {
		return XY_OFFSET;
	}

	@Override
	public double getYOffset() {
		return XY_OFFSET;
	}
}
