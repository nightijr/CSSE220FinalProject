package GameMain;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import rendering.SpriteSheet;

/**
 * Class: Hero
 * 
 * @author F23-A-403 <br>
 *         Purpose: Used to store required information for the hero(user
 *         controlled player) as well as handle the velocity updates when a key
 *         is pressed <br>
 *         Restrictions: Only one instance (as of right now)
 */

public class Hero extends Character implements KeyListener {

	private static final int TICK_COUNTER = 250;
	private int lives;
	private boolean rightKeyPressed;
	private boolean leftKeyPressed;
	private boolean upKeyPressed;
	private int tickCount;
	private int xSpawnPoint;
	private int ySpawnPoint;
	private boolean invulnerable;

	private static final int DEFAULT_X_POS = 250;
	private static final int DEFAULT_Y_POS = 250;
	private static final int INVINCIBILITY_FRAMES = 50;
	private static final double HERO_VELOCITY_CAP = 5.5;

	/**
	 * Creates a new hero at the default position
	 * 
	 * @param gComponent parent LevelGameComponent the hero will be contained in
	 */
	public Hero(LevelGameComponent gComponent) {
		super(gComponent, DEFAULT_X_POS, DEFAULT_Y_POS);
		xSpawnPoint = DEFAULT_X_POS;
		ySpawnPoint = DEFAULT_Y_POS;
		this.lives = 3;
		rightKeyPressed = false;
		leftKeyPressed = false;
		upKeyPressed = false;
		tickCount = 0;
		invulnerable = false;
	}

	/**
	 * creates a new hero at the specified position
	 * 
	 * @param gComponent parent LevelGameComponent the hero will be contained in
	 * @param x          initial x position
	 * @param y          initial y position
	 */
	public Hero(LevelGameComponent gComponent, int x, int y) {
		super(gComponent, x, y);
		xSpawnPoint = x;
		ySpawnPoint = y;
		this.lives = 3;
		rightKeyPressed = false;
		leftKeyPressed = false;
		upKeyPressed = false;
		tickCount = 0;
		invulnerable = false;
	}

	@Override
	public void increaseXVelo(double toIncrease) {
		setXVelo(getXVelo() + toIncrease);
		checkVelocityCap();
	}

	@Override
	public void checkVelocityCap() {
		if (getXVelo() < -HERO_VELOCITY_CAP) {
			setXVelo(-HERO_VELOCITY_CAP);
		} else if (getXVelo() > HERO_VELOCITY_CAP) {
			setXVelo(HERO_VELOCITY_CAP);
		}
	}

	@Override
	public SpriteSheet getSpriteSheet(String state) {
		SpriteSheet ss = new SpriteSheet("images/Hero/" + state + ".png");
		return ss;
	}

	public void setXSpawnPoint(int xPos) {
		xSpawnPoint = xPos;
	}

	public void setYSpawnPoint(int yPos) {
		ySpawnPoint = yPos;
	}

	@Override
	protected int getMoveDirection() {
		if (rightKeyPressed) {
			return 1;
		} else if (leftKeyPressed) {
			return -1;
		}
		return 0;
	}

	@Override
	public void updateState() {
		tick();
		checkKeyPresses();
		super.updateState();

	}

	/**
	 * updates the velocities of the hero based on the current keys being pressed
	 */
	public void checkKeyPresses() {
		if (rightKeyPressed) {
			updateXVelocity(true);
		} else if (leftKeyPressed) {
			updateXVelocity(false);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			rightKeyPressed = true;
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			leftKeyPressed = true;
		} else if (e.getKeyCode() == KeyEvent.VK_UP && !upKeyPressed) {
			updateYVelocity(true);
			upKeyPressed = true;
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		/*
		 * Checks for all key events with regard to the hero
		 */
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			rightKeyPressed = false;
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			leftKeyPressed = false;
		} else if (e.getKeyCode() == KeyEvent.VK_UP && upKeyPressed) {
			upKeyPressed = false;
		}
	}

	@Override
	public void collidesWith(GameObject other) {
		other.collideWithHero(this);
	}

	public void killHero(boolean lava) {
		if (!invulnerable || lava) {
			lives--;
			if (lives <= 0) {
				gComponent.removeHero();
				return;
			}
			respawn();
		}
	}

	private void respawn() {
		tickCount = INVINCIBILITY_FRAMES;
		invulnerable = true;
		this.resetObjectToNewPos(xSpawnPoint, ySpawnPoint);
	}

	@Override
	public void collideWithHero(Hero hero) {

	}

	@Override
	public void collideWithLava(Lava lava) {
		killHero(true);
	}

	/**
	 * Adds the passed powerup to the hero
	 * 
	 * @param powerUp the power up to be added to the hero
	 */
	public void addPowerUp(PowerUp powerUp) {
		if (powerUp.getPowerUpType().equals("Invincible")) {
			invulnerable = true;
			tickCount = TICK_COUNTER;
		}
	}

	/**
	 * Waits an amount of time before making the hero no longer invulnerable
	 */
	public void tick() {
		if (tickCount > 0) {
			tickCount--;
		}
		if (tickCount == 0) {
			invulnerable = false;
		}
	}

	public int getLives() {
		return lives;
	}
}
