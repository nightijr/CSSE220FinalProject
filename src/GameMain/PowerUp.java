package GameMain;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Class for the powerups in the game that give the Hero special boosts
 * 
 * @author F23-A-403
 *
 */
public class PowerUp extends GameObject {

	private String powerUpType;
	private int tickCount;
	private static final int TIME_BEFORE_POWERUP = 100;

	/**
	 * Creates a new PowerUp at the x position, y position, width, height, and
	 * specifed type of the power up
	 * 
	 * @param gComponent parent LevelGameComponent
	 * @param xPos       initial x position
	 * @param yPos       initial y position
	 * @param xSize      width
	 * @param ySize      height
	 * @param type       type of the power up
	 */
	public PowerUp(LevelGameComponent gComponent, double xPos, double yPos, double xSize, double ySize, String type) {
		super(gComponent, xPos, yPos, xSize, ySize);
		powerUpType = type;
		tickCount = 0;
	}

	public String getPowerUpType() {
		return powerUpType;
	}

	@Override
	public void drawOn(Graphics2D g) {
		try {
			BufferedImage img = ImageIO.read(new File("images/star.png"));
			g.drawImage(img, (int) xPos, (int) yPos, gComponent);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void collidesWith(GameObject other) {

	}

	@Override
	public void collideWithPlatform(Platform p) {

	}

	@Override
	public void collideWithLava(Lava lava) {

	}

	@Override
	public void collideWithCharacter(Character character) {

	}

	@Override
	public void collideWithHero(Hero hero) {
		hero.addPowerUp(this);
		gComponent.addToToRemove(this);
		gComponent.addToToRemovePU(this);

	}

	private boolean shouldAppear() {
		return tickCount == TIME_BEFORE_POWERUP;
	}

	public void tick() {
		if (tickCount < 200) {
			tickCount++;
		}
		if (shouldAppear()) {
			gComponent.addPowerUp(this);
		}
	}

	@Override
	public void updateState() {
		tick();
	}

}
