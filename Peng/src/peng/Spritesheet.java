package peng;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

class Spritesheet {
	
	/**
	 * Sprite sheet
	 */
	protected BufferedImage spritesheet;
	
	/**
	 * Player images
	 */
	protected BufferedImage[] player_front, player_right, player_left, player_back;
	
	/**
	 * Enemy images
	 */
	protected BufferedImage[] enemy_front, enemy_right, enemy_left, enemy_back;
	
	/**
	 * Item sprite
	 */
	protected BufferedImage bouncePlus, speedPlus, shieldPlus, bulletPlus;
	
	/**
	 * UI icons
	 */
	protected BufferedImage bounce, speed, shield, bullet;
	
	/**
	 * Game logo
	 */
	protected BufferedImage logo;
	
	/**
	 * Loads game images
	 */
	protected Spritesheet() {
		
		//Loading sprite sheet
		try {
			spritesheet = ImageIO.read(getClass().getResource("/spritesheet.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//loading individual sprite
		
		//Player images
		player_front = new BufferedImage[4];
		player_front[0] = this.getSprite(0, 0, 32, 32);
		player_front[1] = this.getSprite(32, 0, 32, 32);
		player_front[2] = player_front[0];
		player_front[3] = this.getSprite(64, 0, 32, 32);
		
		player_right = new BufferedImage[4];
		player_right[0] = this.getSprite(0, 32, 32, 32);
		player_right[1] = this.getSprite(32, 32, 32, 32);
		player_right[2] = player_right[0];
		player_right[3] = player_right[1];
		
		player_left = new BufferedImage[4];
		player_left[0] = this.getSprite(32, 64, 32, 32);
		player_left[1] = this.getSprite(0, 64, 32, 32);
		player_left[2] = player_left[0];
		player_left[3] = player_left[1];
	
		player_back = new BufferedImage[4];
		player_back[0] = this.getSprite(64, 96, 32, 32);
		player_back[1] = this.getSprite(32, 96, 32, 32);
		player_back[2] = player_back[0];
		player_back[3] = this.getSprite(0, 96, 32, 32);
		
		//Enemy images
		enemy_front = new BufferedImage[4];
		enemy_front[0] = this.getSprite(0, 128, 32, 32);
		enemy_front[1] = this.getSprite(32, 128, 32, 32);
		enemy_front[2] = enemy_front[0];
		enemy_front[3] = this.getSprite(64, 128, 32, 32);
		
		enemy_right = new BufferedImage[4];
		enemy_right[0] = this.getSprite(0, 160, 32, 32);
		enemy_right[1] = this.getSprite(32, 160, 32, 32);
		enemy_right[2] = enemy_right[0];
		enemy_right[3] = enemy_right[1];
		
		enemy_left = new BufferedImage[4];
		enemy_left[0] = this.getSprite(32, 192, 32, 32);
		enemy_left[1] = this.getSprite(0, 192, 32, 32);
		enemy_left[2] = enemy_left[0];
		enemy_left[3] = enemy_left[1];
	
		enemy_back = new BufferedImage[4];
		enemy_back[0] = this.getSprite(0, 224, 32, 32);
		enemy_back[1] = this.getSprite(32, 224, 32, 32);
		enemy_back[2] = enemy_back[0];
		enemy_back[3] = this.getSprite(0, 224, 32, 32);
		
		//Item images
		bouncePlus = this.getSprite(64, 32, 32, 32);
		speedPlus = this.getSprite(64, 64, 32, 32);
		shieldPlus = this.getSprite(64, 160, 32, 32);
		bulletPlus = this.getSprite(64, 192, 32, 32);
		
		//UI images
		bounce = this.getSprite(0, 256, 16, 16);
		speed = this.getSprite(16, 256, 16, 16);
		shield = this.getSprite(0, 272, 16, 16);
		bullet = this.getSprite(16, 272, 16, 16);
		
		//Logo
		logo = this.getSprite(32, 256, 64, 32);
		
	}
	
	/**
	 * Gets an image from sprite sheet
	 * 
	 * @param int x
	 * @param int y
	 * @param int width
	 * @param int height
	 * @return BufferedImage
	 */
	protected BufferedImage getSprite(int x, int y, int width, int height) {
		return spritesheet.getSubimage(x, y, width, height);
	}
	
	

}
