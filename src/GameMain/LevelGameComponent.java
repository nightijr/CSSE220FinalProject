package GameMain;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;

/**
 * Class: LevelGameComponent
 * 
 * @author F23-A-403 <br>
 *         Purpose: Uses keylistener to handle switching levels Creates all
 *         objects when called and attaches them to the frame Updates objects
 *         every cycle <br>
 *         Restrictions: Only one instance that stores all objects
 */

public class LevelGameComponent extends JComponent implements KeyListener {

	private static final int LEVEL_Y = 460;
	private static final int LEVEL_X = 400;
	private static final int LIVES_Y = 460;
	private static final int LIVES_X = 200;
	private static final int SCORE_Y = 460;
	private static final int SCORE_X = 0;
	private static final int HUD_FONT_SIZE = 20;
	private Level level;
	private ArrayList<GameObject> objects;
	private ArrayList<GameObject> toRemove;
	private ArrayList<GameObject> toAdd;
	private ArrayList<GameObject> powerUps;
	private ArrayList<GameObject> toRemovePU;
	private Hero hero;
	private int currentLevel;
	private int score;
	private static final int RESPAWNING_UPWARD_VELOCITY = 1;
	private static final int NUM_LEVELS = 5;
	private static final int INITIAL_LEVEL = 1;

	/**
	 * Creates a new LevelGameComponent and initializes all the lists
	 */
	public LevelGameComponent() {
		objects = new ArrayList<GameObject>();
		toRemove = new ArrayList<GameObject>();
		toAdd = new ArrayList<GameObject>();
		powerUps = new ArrayList<GameObject>();
		toRemovePU = new ArrayList<GameObject>();
		hero = new Hero(this);
		currentLevel = INITIAL_LEVEL;

		loadLevel(currentLevel);

	}

	/**
	 * Loads the specified level number
	 * 
	 * @param levelNum number of the level to be loaded
	 */
	public void loadLevel(int levelNum) {
		if (level != null) {
			objects.clear();
		}
		if (checkIfNextLevelIsValid(levelNum)) {
			Level temp = new Level(this, levelNum);
			temp.loadFile();
			temp.addPlatforms();
			level = temp;
		} else {
			Level temp = new Level(this, currentLevel);
			temp.loadFile();
			temp.addPlatforms();
			level = temp;
		}

	}

	/**
	 * Checks to make sure the next level to be loaded is valid before trying to
	 * load it
	 * 
	 * @param levelNum number of the level to be checked
	 * @return if the next level is valid
	 */
	public boolean checkIfNextLevelIsValid(int levelNum) {
		if (levelNum <= NUM_LEVELS) {
			return true;
		}
		return false;
	}

	/**
	 * Adds the platforms from the list to the objects list
	 * 
	 * @param toAdd list of platforms to be added
	 */
	public void addPlatforms(ArrayList<Platform> toAdd) {
		objects.addAll(toAdd);
	}

	/**
	 * paints all of the objects to the screen
	 */
	public void paintComponent(Graphics g) {
		drawBackground(g);

		for (GameObject obj : objects) {
			obj.drawOn((Graphics2D) g);
		}

		g.setColor(Color.WHITE);
		g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, HUD_FONT_SIZE));
		g.drawString("Score: " + score, SCORE_X, SCORE_Y);
		g.drawString("Lives: " + hero.getLives(), LIVES_X, LIVES_Y);
		g.drawString("Level: " + currentLevel, LEVEL_X, LEVEL_Y);
	}

	/**
	 * Updates the state of all the objects in the game
	 */
	public void updateState() {

		for (GameObject obj : objects) {
			obj.updateState();
		}

		for (GameObject obj : powerUps) {
			obj.updateState();
		}

		for (GameObject obj1 : objects) {
			for (GameObject obj2 : objects) {
				if (obj1 != obj2 && obj1.overlaps(obj2)) {
					obj1.collidesWith(obj2);
				}
			}
		}
		objects.removeAll(toRemove);
		toRemove.clear();
		objects.addAll(toAdd);
		toAdd.clear();
		powerUps.removeAll(toRemovePU);
		toRemovePU.clear();

		int count = 0;
		// TODO: type-predicated code needs fixing
		for (GameObject obj : objects) {
			if (obj.getClass().getSimpleName().equals("Enemy") || obj.getClass().getSimpleName().equals("Egg")
					|| obj.getClass().getSimpleName().equals("ShooterEnemy")
					|| obj.getClass().getSimpleName().equals("TrackingEnemy")) {
				count++;
			}
		}

		if (count == 0) {
			System.out.println("You Win!!");
			loadLevel(currentLevel + 1);
		}
	}

	/**
	 * Draws the background to the screen
	 * 
	 * @param g graphics context to draw to
	 */
	private void drawBackground(Graphics g) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("images/background.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		g.drawImage(img, 0, 0, this);

	}

	public void drawScreen() {
		this.repaint();
	}

	public void addToToRemove(GameObject obj) {
		toRemove.add(obj);
	}

	public void addToToAdd(GameObject obj) {
		toAdd.add(obj);
	}

	public void createEnemy(int x, int y) {
		Enemy enemy = new Enemy(this, x, y);
		objects.add(enemy);
	}

	public void createEnemy(int x, int y, int randTickRange, int ticksBeforeUpdate, String type) {
		Enemy enemy = new Enemy(this, x, y, randTickRange, ticksBeforeUpdate, type);
		objects.add(enemy);
	}

	public void createTrackingEnemy(int x, int y) {
		TrackingEnemy enemy = new TrackingEnemy(this, x, y);
		objects.add(enemy);
	}

	public void createShootingEnemy(int x, int y) {
		ShooterEnemy enemy = new ShooterEnemy(this, x, y);
		objects.add(enemy);
	}

	public void createProjectile(double x, double y, int moveDirection) {
		Projectile bullet = new Projectile(this, x, y);
		if (moveDirection > 0) {
			bullet.setXVelo(bullet.getMoveSpeed());
		} else {
			bullet.setXVelo(-bullet.getMoveSpeed());
		}
		toAdd.add(bullet);
	}

	public void createLava(int x, int y) {
		Lava lava = new Lava(this, x, y);
		objects.add(lava);
	}

	public void setHeroPos(int x, int y) {
		hero.setXSpawnPoint(x);
		hero.setYSpawnPoint(y);
		hero.resetObjectToNewPos(x, y);
		objects.add(hero);
	}

	public void convertEnemyToEgg(Enemy enemy) {
		toRemove.add(enemy);
		Egg egg = new Egg(this, enemy.xPos, enemy.yPos, 0, enemy);

		toAdd.add(egg);
	}

	public void removeEgg(Egg egg) {
		toRemove.add(egg);
	}

	public void removeHero() {
		toRemove.add(hero);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		if (e.getKeyChar() == 'u') {
			if (currentLevel < NUM_LEVELS) {
				currentLevel++;
				loadLevel(currentLevel);
			}
		} else if (e.getKeyChar() == 'd') {
			if (currentLevel > 1) {
				currentLevel--;
				loadLevel(currentLevel);
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	public void addToFrame(JFrame frame) {
		frame.add(this);
		frame.addKeyListener(this);
		frame.addKeyListener(hero);

	}

	public Hero getHero() {
		return hero;
	}

	public void addScore(int score) {
		this.score += score;
	}

	public void convertEggToEnemy(Egg egg, Enemy enemy) {
		toRemove.add(egg);
		enemy.resetObjectToNewPos(egg.xPos, egg.yPos);
		toAdd.add(enemy);
		enemy.yVelo = RESPAWNING_UPWARD_VELOCITY;

	}

	public void createPowerUp(int characterX, int characterY) {
		PowerUp invincibility = new PowerUp(this, characterX, characterY, 10, 10, "Invincible");
		powerUps.add(invincibility);
	}

	public void addPowerUp(GameObject powerUp) {
		toAdd.add(powerUp);
	}

	public void addToToRemovePU(GameObject obj) {
		toRemovePU.add(obj);
	}

}
