package GameMain;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import Exceptions.InvalidFileColLengthException;
import Exceptions.InvalidFileRowLengthException;
import Exceptions.InvalidLevelLoadingException;

/**
 * Class: Level
 * 
 * @author F23-A-403 <br>
 *         Purpose: Reads and stores the level files and handles
 *         creation/movement of the things notated within the text file for each
 *         level <br>
 *         Restrictions: Only one instance (as of right now)
 */

public class Level {

	private static final int TICKS_BEFORE_UPDATE = 50;
	private static final int RANDOM_TICK_RANGE = 25;
	private static final char TRACKING_ENEMY_CHAR = 'T';
	private static final char VOLATILE_CHAR = 'V';
	private static final char HERO_CHAR = 'H';
	private static final char ENEMY_CHAR = 'E';
	private ArrayList<Platform> platforms;
	private int levelNum;
	private LevelGameComponent gComponent;
	private static final char POWER_UP_CHAR = '+';
	private static final char LAVA_CHAR = 'L';
	private static final char SHOOTING_ENEMY_CHAR = 'S';
	private static final int FILE_ROW_LENGTH = 10;
	private static final int FILE_COL_LENGTH = 10;
	private static final char PLATFORM_CHAR = 'P';
	public static final int FILE_TO_PIXEL_RATIO = 50;

	/**
	 * Creates a new level with the specified level number
	 * 
	 * @param gComponent parent LevelGameComponent for the level
	 * @param levelNum   number of the level being created
	 */
	public Level(LevelGameComponent gComponent, int levelNum) {
		this.levelNum = levelNum;
		this.gComponent = gComponent;
		platforms = new ArrayList<Platform>();
	}

	/**
	 * Loads the file of the current level number
	 */
	public void loadFile() {
		String initialFile = "Levels/level" + levelNum + ".txt";

		try {
			createPlatforms(initialFile);
		} catch (InvalidFileColLengthException e) {
			System.err.println("ERROR: FILE COL LENGTH INVALID");
		} catch (InvalidFileRowLengthException e) {
			System.err.println("ERROR: FILE ROW LENGTH INVALID");
		} catch (InvalidLevelLoadingException e) {
			System.err.println("ERROR: FILE " + initialFile + " NOT FOUND");
		}
	}

	/**
	 * Creates all the platforms and other objects contained within a level
	 * 
	 * @param s path of the level to be loaded
	 * @throws InvalidLevelLoadingException if the file cannot be read
	 */
	private void createPlatforms(String s) throws InvalidLevelLoadingException {
		Scanner s1;
		try {
			s1 = new Scanner(new File(s));
		} catch (FileNotFoundException e) {
			throw new InvalidLevelLoadingException();
		}

		int count = 0;

		int fileY = 0;
		while (s1.hasNextLine()) {
			String charRow = s1.nextLine();

			if (charRow.length() != FILE_COL_LENGTH) {
				platforms.clear();
				throw new InvalidFileColLengthException();
			}

			count++;

			for (int i = 0; i < charRow.length(); i++) {
				char curChar = charRow.charAt(i);
				int fileX = i * FILE_TO_PIXEL_RATIO;
				if (curChar == PLATFORM_CHAR) {
					i += platformLength(fileX, fileY, charRow.substring(i));
					continue;
				} else if (curChar == ENEMY_CHAR) {
					gComponent.createEnemy(getCharacterX(fileX), getCharacterY(fileY));
				} else if (curChar == HERO_CHAR) {
					gComponent.setHeroPos(getCharacterX(fileX), getCharacterY(fileY));
				} else if (curChar == VOLATILE_CHAR) {
					gComponent.createEnemy(getCharacterX(fileX), getCharacterY(fileY), RANDOM_TICK_RANGE,
							TICKS_BEFORE_UPDATE, "Volatile");
				} else if (curChar == TRACKING_ENEMY_CHAR) {
					gComponent.createTrackingEnemy(getCharacterX(fileX), getCharacterY(fileY));
				} else if (curChar == SHOOTING_ENEMY_CHAR) {
					gComponent.createShootingEnemy(getCharacterX(fileX), getCharacterY(fileY));
				} else if (curChar == LAVA_CHAR) {
					gComponent.createLava(getCharacterX(fileX), getCharacterY(fileY));
				} else if (curChar == POWER_UP_CHAR) {
					gComponent.createPowerUp(getCharacterX(fileX), getCharacterY(fileY));
				}
			}

			fileY += FILE_TO_PIXEL_RATIO;
		}

		if (count != FILE_ROW_LENGTH) {
			platforms.clear();
			throw new InvalidFileRowLengthException();
		}

	}

	/**
	 * Translates the information from the file into a y coordinate
	 * 
	 * @param fileY the integer from the file
	 * @return a y coordinate for the character to spawn
	 */
	private int getCharacterY(int fileY) {
		return fileY + FILE_TO_PIXEL_RATIO - (int) Character.Y_SIZE;
	}

	/**
	 * Translates the information from the file into a x coordinate
	 * 
	 * @param fileX the integer from the file
	 * @return a x coordinate for the character to spawn
	 */
	private int getCharacterX(int fileX) {
		return fileX + FILE_TO_PIXEL_RATIO / 2 - (int) Character.X_SIZE / 2;
	}

	/**
	 * Finds the length of the platform in the file and creates that platform
	 * 
	 * @param x     current x coordinate in the file
	 * @param y     current y coordinate in the file
	 * @param chars current chars
	 * @return the length of the platform
	 */
	private int platformLength(int x, int y, String chars) {
		int length = platformLengthHelper(0, 0, chars);
		platforms.add(new Platform(gComponent, x, y, length));
		return length;
	}

	/**
	 * Recursive helper method for the platformLength method
	 * 
	 * @param i      current index
	 * @param length current length
	 * @param chars  current chars
	 * @return the length of the platform
	 */
	private int platformLengthHelper(int i, int length, String chars) {
		if (i == chars.length() || chars.charAt(i) != PLATFORM_CHAR) {
			return length;
		}

		return platformLengthHelper(i + 1, length + 1, chars);
	}

	public void addPlatforms() {
		gComponent.addPlatforms(platforms);
	}

	public int getLevelNum() {
		return levelNum;
	}
}
