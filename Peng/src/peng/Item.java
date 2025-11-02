package peng;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

class Item extends Rectangle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4601633829207169404L;
	
	/**
	 * Item type constants
	 */
	private static final int BOUNCE_PLUS = 0, SPEED_PLUS = 1, SHIELD_PLUS = 2, BULLET_PLUS = 3;
	
	/**
	 * Item type
	 */
	private int type = BOUNCE_PLUS;
	
	/**
	 * 
	 * @param int x
	 * @param int y
	 * @param int type
	 */
	protected Item(int x, int y, int type) {
		super(x,y,Game.SIZE,Game.SIZE);
		this.type = type;
	}

	/**
	 * Item images
	 * @param Graphics g
	 */
	protected void render(Graphics g) {
		BufferedImage sprite = null;
		if(type == BOUNCE_PLUS)
			sprite = Game.sprite.bouncePlus;
		else if (type == SPEED_PLUS)
			sprite = Game.sprite.speedPlus;
		else if (type == SHIELD_PLUS)
			sprite = Game.sprite.shieldPlus;
		else if (type == BULLET_PLUS)
			sprite = Game.sprite.bulletPlus;
		
		g.drawImage(sprite, x, y, width, height, null);
	}

	/**
	 * Item logic
	 */
	protected void update() {
		if(this.intersects(Game.player)) {
			if(type == BOUNCE_PLUS)
				Game.player.maxRicochet++;
			else if (type == SPEED_PLUS)
				Game.player.spd++;
			else if (type == SHIELD_PLUS)
				Game.player.shield+=50;
			else if (type == BULLET_PLUS)
				Game.player.bulletCount++;
			
				
			
			Game.items.remove(this);
		}
		
	}

}
