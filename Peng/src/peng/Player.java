package peng;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

class Player extends Rectangle{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8111368797795931077L;
	
	/**
	 * Player speed
	 */
	protected int spd = 6;

	/**
	 * Player direction
	 */
	private int direction = 1;
	
	/**
	 * Button variables
	 */
	protected boolean right, up, down, left, shoot;
	
	/**
	 * Animation variables
	 */
	private int currentIndex = 0, currentFrames = 0, maximumFrames = spd, maximumIndex = 3;

	/**
	 * Animation control and bullet axis control
	 */
	private boolean moving = false, xAxis = true;
	
	/**
	 * Player sprite
	 */
	private BufferedImage[] playerSprite = Game.sprite.player_right;
	
	/**
	 * How many times your bullets bounces
	 */
	protected int maxRicochet = 0;

	/**
	 * How many shots the player can take
	 */
	protected int shield = 0;

	/**
	 * How many bullets can the player fire at once
	 */
	protected int bulletCount = 1, bulletsFired = 0;
	
	/**
	 * The one you control
	 * @param int x
	 * @param int y
	 */
	protected Player(int x, int y) {
		super(x,y,Game.SIZE,Game.SIZE);
	}
	
	/**
	 * Updates logic
	 */
	protected void update() {
		//Bullet check
		checkBulletCollision();
		
		//Player horizontal movement
		if(right && isPathFree(x+spd, y)) {
			x+=spd;
			moving = true;
			direction = 1;
			playerSprite = Game.sprite.player_right;
			xAxis = true;
		}else if(left && isPathFree(x-spd, y)) {
			x-=spd;
			moving = true;
			direction = -1;
			playerSprite = Game.sprite.player_left;
			xAxis = true;
		}
		//Player vertical movement
		else if(down && isPathFree(x, y+spd)) {
			y+=spd;
			moving = true;
			direction = 1;
			playerSprite = Game.sprite.player_front;
			xAxis = false;
		}else if(up && isPathFree(x, y-spd)) {
			y-=spd;
			moving = true;
			direction = -1;
			playerSprite = Game.sprite.player_back;
			xAxis = false;
		}else {
			moving = false;
		}
		
		//Combat
		if(bulletsFired < 0) bulletsFired = 0;
		
		if (shoot && bulletsFired < bulletCount) {
			bulletsFired++;
			shoot = false;
			Bullet bullet = new Bullet(x,y,direction,xAxis, maxRicochet, Bullet.PLAYER_BULLET);
			Game.bullets.add(bullet);
		}
	}
	
	/**
	 * Renders the player
	 * 
	 * @param Graphics g
	 */
	protected void render(Graphics g) {
		animate();
		g.drawImage(playerSprite[currentIndex], x, y, width, height, null);
		
		if(shield < 0) shield = 0;
		
		if (shield >= 50) {
			if(shield > 250) shield = 250;
			g.setColor(new Color(0,0,255,shield));
			g.fillOval(x, y, width, height);
		}
	}
	
	/**
	 * Checks if player's path is free!
	 * 
	 * @param int x
	 * @param int y
	 * @return boolean
	 */
	private boolean isPathFree(int x, int y) {
		
		for(int i = 0; i < World.tiles.size(); i++) {
			Tile current = World.tiles.get(i);
			if(current.intersects(new Rectangle(x,y,width,height))) {
				return false;
			}
		}
		
		
		return true;
	}
	
	/**
	 * Animates the player
	 */
	private void animate() {
		
		if(moving) {
			currentFrames++;
			if(currentFrames == maximumFrames) {
				currentIndex++;
				currentFrames = 0;
				if(currentIndex > maximumIndex) {
					currentIndex = 0;
				}
			}
		}else {
			currentIndex = 0;
		}
	}
	
	/**
	 * Checks if bullet hits
	 */
	private void checkBulletCollision() {
		for(int i = 0; i < Game.bullets.size(); i++) {
			Bullet current = Game.bullets.get(i);
			if(current.type == 1 && current.intersects(new Rectangle(x,y,width,height))) {
				Game.bullets.remove(current);
				shield-=50;
				if (shield <= -50) Game.restart();
			}
		}
	}

}
