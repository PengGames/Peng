package peng;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

class Enemy extends Rectangle{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2554192775983827545L;
	
	/**
	 * Enemy speed & direction
	 */
	private int speed = 4, direction = 1;
	
	/**
	 * Movement & combat variables
	 */
	protected boolean right, up, down, left, shoot, xAxis = false;
	
	/**
	 * Animation variables
	 */
	private int currentIndex = 0, currentFrames = 0, maximumFrames = 60, maximumIndex = 3;

	/**
	 * Animation control
	 */
	private boolean moving = false;
	
	/**
	 * Enemy sprite
	 */
	private BufferedImage[] enemySprite = Game.sprite.enemy_front;
	
	/**
	 * Shooting interval
	 */
	private int timer = 0, maxTimer = 60;
	
	/**
	 * How many times will the bullet bounce
	 */
	private int maxRicochet = 0;
	
	/**
	 * "protected enemy" lmao
	 * @param int x
	 * @param int y
	 */
	protected Enemy(int x, int y, int speed) {
		super(x,y,Game.SIZE,Game.SIZE);
		this.speed = speed;
		
		maxRicochet = Game.random.nextInt(10);
	}
	
	/**
	 * Updates logic
	 */
	protected void update() {
		checkBulletCollision();
		chasePlayer();

		//Movement
		if(right && isPathFree(x+speed, y)) {
			x+=speed;
			moving = true;
			enemySprite = Game.sprite.enemy_right;
			direction = 1;
			xAxis = true;
		}else if(left && isPathFree(x-speed, y)) {
			x-=speed;
			moving = true;
			enemySprite = Game.sprite.enemy_left;
			direction = -1;
			xAxis = true;
		}
		else if(down && isPathFree(x, y+speed)) {
			y+=speed;
			moving = true;
			enemySprite = Game.sprite.enemy_front;
			direction = 1;
			xAxis = false;
		}else if(up && isPathFree(x, y-speed)) {
			y-=speed;
			moving = true;
			enemySprite = Game.sprite.enemy_back;
			direction = -1;
			xAxis = false;
		}else {
			moving = false;
			direction = 1;
			xAxis = false;	
		}
		
		shootBullet();
		
	}
	
	/**
	 * Renders the player
	 * 
	 * @param Graphics g
	 */
	protected void render(Graphics g) {
		animate();
		
		g.drawImage(enemySprite[currentIndex], x, y, width, height, null);
	}
	
	/**
	 * Checks if enemy's path is free!
	 * 
	 * @param int x
	 * @param int y
	 * @return boolean
	 */
	private boolean isPathFree(int x, int y) {
		
		if(this.intersects(Game.player)) return false;
		
		for(int i = 0; i < Game.enemies.size(); i++) {
			Enemy current = Game.enemies.get(i);
			if(current != this && current.intersects(new Rectangle(x,y,width,height))) {
				return false;
			}
		}
		
		for(int i = 0; i < World.tiles.size(); i++) {
			Tile current = World.tiles.get(i);
			if(current.intersects(new Rectangle(x,y,width,height))) {
				return false;
			}
		}
		
		
		return true;
	}
	
	
	/**
	 * The enemy chases the player
	 */
	private void chasePlayer() {
	    right = false;
	    left = false;
	    up = false;
	    down = false;

	    if(x < Game.player.x) {
	        right = true;
	    } else if (y < Game.player.y) {
	        down = true;
	    } else if (x > Game.player.x) {
	        left = true;
	    } else if (y > Game.player.y) {
	        up = true;
	    }
	}
	
	/**
	 * Animates the enemy
	 */
	private void animate() {
		
		if(moving) {
			currentFrames+=speed;
			if(currentFrames >= maximumFrames) {
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
	 * Shoots bullets
	 */
	private void shootBullet() {
		timer++;
		if(timer > maxTimer) {
			timer = 0;
			shoot = true;
		}
		
		if(shoot) {
			shoot = false;
			Bullet bullet = new Bullet(x,y,direction,xAxis, maxRicochet, Bullet.ENEMY_BULLET);
			Game.bullets.add(bullet);
		}
	}
	
	/**
	 * Checks if bullet hits
	 */
	private void checkBulletCollision() {
		for(int i = 0; i < Game.bullets.size(); i++) {
			Bullet current = Game.bullets.get(i);
			if(current.type == 0 && current.intersects(new Rectangle(x,y,width,height))) {
				Game.score += 5*speed;
				if(Game.random.nextInt(100) <= 50) {
					int type = Game.random.nextInt(4);
					Item item = new Item(x,y,type);
					Game.items.add(item);
				}
				Game.enemies.remove(this);
			}
		}
	}

}
