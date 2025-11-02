package peng;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.io.IOException;
import java.io.InputStream;

class UI {
	
	/**
	 * Game font
	 */
	private Font font;
	
	/**
	 * User Interface
	 */
	protected UI() {
		//Game font
		InputStream is = Game.class.getResourceAsStream("/font.ttf");
		
		//Loading game font
        try {
        	font = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(32f);
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	protected void render(Graphics g) {
		g.setColor(Color.white);
		g.setFont(font);
		
		if(Game.gameState.equals("title")) {
			
			g.drawImage(Game.sprite.logo, Game.WIDTH/2 - (128*Game.SCALE)/2, Game.HEIGHT/2 - (64*Game.SCALE)/2, 128*Game.SCALE, 64*Game.SCALE, null);
			g.drawString("LAST SCORE: " + Game.lastScore, (Game.WIDTH/2) - (56*Game.SCALE), (Game.HEIGHT/2)-(64*Game.SCALE));
			g.drawString("HIGH SCORE: " + Game.highscore, (Game.WIDTH/2) - (56*Game.SCALE), (Game.HEIGHT/2)+(64*Game.SCALE));
			
		} else if (Game.gameState.equals("paused")){
			
			g.drawString("PAUSED", (Game.WIDTH/2) - (32*Game.SCALE), (Game.HEIGHT/2));
			
		} else {
			g.drawString("SCORE: " + Game.score, 0, Game.SIZE/2);
			
			g.drawImage(Game.sprite.bounce, Game.SIZE*3, 0, 16*Game.SCALE, 16*Game.SCALE, null);
			g.drawString(Integer.toString(Game.player.maxRicochet), Game.SIZE*3 + 16*Game.SCALE, Game.SIZE/2);
			
			g.drawImage(Game.sprite.speed, Game.SIZE*6, 0,  16*Game.SCALE, 16*Game.SCALE, null);
			g.drawString(Integer.toString(Game.player.spd - 6), Game.SIZE*6 + 16*Game.SCALE, Game.SIZE/2);
			
			g.drawImage(Game.sprite.shield, Game.SIZE*9, 0,  16*Game.SCALE, 16*Game.SCALE, null);
			g.drawString(Integer.toString(Game.player.shield/50), Game.SIZE*9 + 16*Game.SCALE, Game.SIZE/2);
			
			g.drawImage(Game.sprite.bullet, Game.SIZE*12, 0,  16*Game.SCALE, 16*Game.SCALE, null);
			g.drawString(Integer.toString(Game.player.bulletCount), Game.SIZE*12 + 16*Game.SCALE, Game.SIZE/2);
		}
	}

}
