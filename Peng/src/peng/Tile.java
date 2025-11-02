package peng;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

class Tile extends Rectangle {


	/**
	 * 
	 */
	private static final long serialVersionUID = 3026276251341236434L;

	/**
	 * Blocks player and enemy's path
	 * 
	 * @param int x
	 * @param int y
	 */
	protected Tile(int x, int y) {
		super(x,y,Game.SIZE,Game.SIZE);
	}
	
	/**
	 * Renders each world tile individually
	 * 
	 * @param Graphics g
	 */
	protected void render(Graphics g) {
		g.setColor(Color.gray);
		g.fillRect(x, y, width, height);
		g.setColor(Color.black);
		g.drawRect(x, y, width, height);
	}
	
}
