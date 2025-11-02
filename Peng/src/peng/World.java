package peng;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class World {
	
	/**
	 * Stores world tiles
	 */
	protected static List<Tile> tiles = new ArrayList<Tile>();
	
	/**
	 * Controls the frequency enemies spawns in
	 */
	private int timer = 0, maxTimer = 60*3;
	
	/**
	 * Possible enemy speed
	 */
	private List<Integer> options = Arrays.asList(2, 4, 6);

	/**
	 * Builds game's world
	 */
	protected World() {
		
		//Superior horizontal barrier
		for(int x = 0; x < Game.WIDTH/Game.SIZE; x++) {
			tiles.add(new Tile(x*Game.SIZE,0));
		}
		
		//Inferior horizontal barrier
		for(int x = 0; x < Game.WIDTH/Game.SIZE; x++) {
			tiles.add(new Tile(x*Game.SIZE, (Game.HEIGHT - Game.SIZE)));
		}
		
		//Left vertical barrier
		for(int y = 0; y < Game.HEIGHT/Game.SIZE; y++) {
			tiles.add(new Tile(0,y*Game.SIZE));
		}
		
		//Right vertical barrier
		for(int y = 0; y < Game.HEIGHT/Game.SIZE; y++) {
			tiles.add(new Tile(Game.HEIGHT - Game.SIZE,y*Game.SIZE));
		}
		
	}
	
	/**
	 * Renders the game world
	 * @param Graphics g
	 */
	protected void render(Graphics g) {
		//Render every block
		for(int i = 0; i < tiles.size(); i++) {
			tiles.get(i).render(g);
		}
		
	}
	
	/**
	 * World logic
	 */
	protected void update() {
		timer++;
		if(timer > maxTimer) {
			timer = 0;
			
			//Creating the enemies
			if(Game.enemies.size() < 10) {
				
				int randomIndex = Game.random.nextInt(options.size());
		        int speed = options.get(randomIndex);
		        
				int x = Game.random.nextInt(Game.WIDTH/Game.SIZE);
				int y = Game.random.nextInt(Game.HEIGHT/Game.SIZE);
				
				x = (x == 0 || x == (Game.WIDTH/Game.SIZE)-1) ? 1 : x;
				y = (y == 0 || y == (Game.HEIGHT/Game.SIZE)-1) ? 1 : y;
				
				Game.enemies.add(new Enemy(Game.SIZE*x, Game.SIZE*y, speed));
			}
		}
		
	}

}
