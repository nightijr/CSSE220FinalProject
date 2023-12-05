package GameMain;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Class: Platform
 * 
 * @author F23-A-403 <br>
 *         Purpose: Handles creation of platforms as well as interactions with
 *         the hero in cases of clipping
 */

public class Platform extends GameObject {

	private static final int PLATFORM_HEIGHT = 20;
	private int platformLength;

	public Platform(LevelGameComponent gComponent, int x, int y, int length) {
		super(gComponent, x, y, length * Level.FILE_TO_PIXEL_RATIO, PLATFORM_HEIGHT);
		platformLength = length * Level.FILE_TO_PIXEL_RATIO;
	}

	public void drawOn(Graphics2D g) {
		try {
			BufferedImage img = ImageIO.read(new File("images/Box.png"));
			int x = (int) xPos;
			int y = (int) yPos;
			for (int i = 0; i < platformLength; i += Level.FILE_TO_PIXEL_RATIO) {
				g.drawImage(img, x + i, y, x + i + Level.FILE_TO_PIXEL_RATIO, y + PLATFORM_HEIGHT, 4, 2, 24, 22,
						gComponent);
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	@Override
	public void collidesWith(GameObject other) {
		other.collideWithPlatform(this);
	}

	@Override
	public void collideWithPlatform(Platform p) {
	}

	@Override
	public void collideWithCharacter(Character character) {
		character.collideWithPlatform(this);
	}

	@Override
	public void collideWithHero(Hero hero) {
	}

	@Override
	public void collideWithLava(Lava lava) {

	}
}
