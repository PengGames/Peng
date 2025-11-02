package peng;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

class Bullet extends Rectangle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2729097101691741813L;
	
	/**
	 * Bullet property
	 */
	private int direction = 1, speed = 12, ricochet = 0, maxRicochet = 0, ricochetDirection = Math.random() < 0.5 ? -1 : 1;
	
	/**
	 * Tells if bullet will run along the xAxis
	 */
	private boolean xAxis = true;
	
	/**
	 * Bullet type constants
	 */
	protected static final int PLAYER_BULLET = 0, ENEMY_BULLET = 1;
	
	/**
	 * Determine type of bullet (0 is for player and 1 is enemy)
	 */
	protected int type = PLAYER_BULLET;
	
	/**
	 * Bullet
	 * 
	 * @param int x
	 * @param int y
	 * @param int direction (-1 or 1)
	 * @param boolean xAxis
	 * @param int maxRicochet
	 * @param int type (0 = Player, 1 = Enemy)
	 */
	protected Bullet(int x, int y, int direction, boolean xAxis, int maxRicochet, int type) {
		super(x+16, y+16, 1, 1);
		this.direction = direction;
		this.xAxis = xAxis;
		this.type = type;
		this.maxRicochet = maxRicochet;
	}
	
	/**
	 * Updates logic
	 */
	protected void update() {
		//Bullet movement
		if(this.xAxis) {
			x+=speed*direction;
			if(ricochet > 0) {
				y+=(speed/2)*ricochetDirection;
			}
			
		} else {
			y+=speed*direction;
			if(ricochet > 0) {
				x+=(speed/2)*ricochetDirection;
			}
		}
		
		
		if(x > Game.WIDTH || x < 0 || y > Game.HEIGHT || y < 0) {
			//Bullet out of bounds
			Game.bullets.remove(this);
			Game.player.bulletsFired--;
		}else {
			//Bullet collision
			checkCollision();
		}
	}
	
	/**
	 * Renders bullet
	 * 
	 * @param Graphics g
	 */
	protected void render(Graphics g) {
		if(this.type == 0)
			g.setColor(Color.CYAN);
		else
			g.setColor(Color.RED);
		g.fillOval(x,y,Game.SIZE/2,Game.SIZE/2);
	}
	
	/**
	 * Checks if bullet collided with tiles
	 * 
	 * @param int x
	 * @param int y
	 * @return boolean
	 */
	private void checkCollision() {
		
		for(int i = 0; i < World.tiles.size(); i++) {
			Tile current = World.tiles.get(i);
			if(current.intersects(new Rectangle(x,y,width,height))) {
				ricochetDirection = Math.random() < 0.5 ? -1 : 1;
				direction = (direction == 1) ? -1 : 1;
				ricochet++;
				if(ricochet > maxRicochet) {
					Game.bullets.remove(this);
					if (type == 0)
						Game.player.bulletsFired--;

				}
			}
		}
		
	}

}
