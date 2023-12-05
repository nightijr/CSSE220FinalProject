package GameMain;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Class for the lava object that is at the bottom of the screen
 * 
 * @author F23-A-403
 *
 */
public class Lava extends GameObject {

	/**
	 * Creates a new lava object at the specified position
	 * 
	 * @param gComponent the parent LevelGameComponent
	 * @param x          initial x position
	 * @param y          initial y position
	 */
	public Lava(LevelGameComponent gComponent, int x, int y) {
		super(gComponent, x, y - 50, 50, 400);
	}

	@Override
	public void drawOn(Graphics2D g) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("images/spikeTest.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		g.drawImage(img, (int) xPos + 25, (int) yPos - 5, gComponent);
		g.drawImage(img, (int) xPos + 45, (int) yPos - 5, gComponent);
	}

	@Override
	public void collidesWith(GameObject other) {
		other.collideWithLava(this);
	}

	@Override
	public void collideWithPlatform(Platform p) {

	}

	@Override
	public void collideWithCharacter(Character character) {

	}

	@Override
	public void collideWithHero(Hero hero) {

	}

	@Override
	public void collideWithLava(Lava lava) {

	}

}
