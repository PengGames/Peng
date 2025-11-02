package peng;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Random;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.security.SecureRandom;

import javax.swing.JFrame;

class Game extends Canvas implements Runnable, KeyListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3554758868235631520L;

	/**
	 * Screen and sprite size
	 */
	protected static final int SCALE = 2, WIDTH = 480 * SCALE, HEIGHT = 480 * SCALE, SIZE = 32 * SCALE;
	
	/**
	 * Player
	 */
	protected static Player player;
	
	/**
	 * Game world
	 */
	protected World world;
	
	/**
	 * Game images
	 */
	protected static Spritesheet sprite;
	
	
	/**
	 * Game items
	 */
	protected static ArrayList<Item> items = new ArrayList<Item>();
	
	/**
	 * Game bullets
	 */
	protected static ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	
	/**
	 * Game enemies
	 */
	protected static ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	
	/**
	 * Randomizer
	 */
	protected static Random random = new Random();
	
	/**
	 * User Interface
	 */
	private UI ui;
	
	/**
	 * Game state
	 */
	static String gameState = "title";
	
	/**
	 * Game score
	 */
	static long score = 0, lastScore = score, highscore = 0;
	
	/**
	 * Highscore system constants and variables
	 */
	private static final String DATA_FOLDER = "data";
	private static final String HIGHSCORE_FILE = "highscore";
	private static final String KEY_FILE = "gamekey.dat";
	private static final String ENCRYPTION_ALGORITHM = "AES";
	private static final String ENCRYPTION_TRANSFORMATION = "AES";
	
	/**
	 * Main class
	 */
	protected Game() {
		//Adding key events to this class
		this.addKeyListener(this);
		
		//Setting screen size
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		
		//Initialize highscore system
		initializeHighscoreSystem();
		
		//Creating game images
		sprite = new Spritesheet();
		
		//Game UI
		ui = new UI();
		
		//Creating the player
		player = new Player(SIZE, SIZE);
		
		//Creating game world
		world = new World();
	}
	
	/**
	 * Initialize the highscore system - create folders, load existing highscore
	 */
	private void initializeHighscoreSystem() {
		try {
			// Create data folder if it doesn't exist
			File dataDir = new File(DATA_FOLDER);
			if (!dataDir.exists()) {
				dataDir.mkdirs();
			}
			
			// Load existing highscore
			loadHighscore();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Generate or load encryption key
	 */
	private static SecretKey getEncryptionKey() throws Exception {
		File keyFile = new File(DATA_FOLDER, KEY_FILE);
		
		if (keyFile.exists()) {
			// Load existing key
			byte[] keyBytes = Files.readAllBytes(keyFile.toPath());
			return new SecretKeySpec(keyBytes, ENCRYPTION_ALGORITHM);
		} else {
			// Generate new key
			KeyGenerator keyGen = KeyGenerator.getInstance(ENCRYPTION_ALGORITHM);
			keyGen.init(128, new SecureRandom());
			SecretKey secretKey = keyGen.generateKey();
			
			// Save key to file
			try (FileOutputStream fos = new FileOutputStream(keyFile)) {
				fos.write(secretKey.getEncoded());
			}
			
			return secretKey;
		}
	}
	
	/**
	 * Encrypt data using AES
	 */
	private static String encrypt(String data) throws Exception {
		SecretKey key = getEncryptionKey();
		Cipher cipher = Cipher.getInstance(ENCRYPTION_TRANSFORMATION);
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] encryptedBytes = cipher.doFinal(data.getBytes("UTF-8"));
		return Base64.getEncoder().encodeToString(encryptedBytes);
	}
	
	/**
	 * Decrypt data using AES
	 */
	private String decrypt(String encryptedData) throws Exception {
		SecretKey key = getEncryptionKey();
		Cipher cipher = Cipher.getInstance(ENCRYPTION_TRANSFORMATION);
		cipher.init(Cipher.DECRYPT_MODE, key);
		byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);
		byte[] decryptedBytes = cipher.doFinal(decodedBytes);
		return new String(decryptedBytes, "UTF-8");
	}
	
	/**
	 * Load highscore from encrypted file
	 */
	private void loadHighscore() {
		try {
			File highscoreFile = new File(DATA_FOLDER, HIGHSCORE_FILE);
			
			if (highscoreFile.exists()) {
				// Read encrypted content
				String encryptedContent = new String(Files.readAllBytes(highscoreFile.toPath()), "UTF-8");
				
				// Decrypt and parse highscore
				String decryptedContent = decrypt(encryptedContent);
				highscore = Long.parseLong(decryptedContent.trim());
				
			} else {
				// No highscore file exists, start with 0
				highscore = 0;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			// If there's any error, default to 0
			highscore = 0;
		}
	}
	
	/**
	 * Save highscore to encrypted file
	 */
	private static void saveHighscore() {
		try {
			File highscoreFile = new File(DATA_FOLDER, HIGHSCORE_FILE);
			
			// Encrypt the highscore value
			String encryptedContent = encrypt(String.valueOf(highscore));
			
			// Write encrypted content to file
			try (FileOutputStream fos = new FileOutputStream(highscoreFile)) {
				fos.write(encryptedContent.getBytes("UTF-8"));
			}
						
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Game logic
	 */
	protected void update() {
		
		if(gameState.equals("playing")) {
		
			//World logic
			world.update();
			
			//Player logic
			player.update();
			
			//Item logic
			for (int i = 0; i < items.size(); i++) {
				items.get(i).update();
			}
			
			//Bullet logic
			for (int i = 0; i < bullets.size(); i++) {
				bullets.get(i).update();
			}
			
			//Enemy logic
			for (int i = 0; i < enemies.size(); i++) {
				enemies.get(i).update();
			}
			
		}
		
	}
	
	/**
	 * Game images
	 */
	protected void render() {
		BufferStrategy bs = this.getBufferStrategy();
		
		//Rendering optimization
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		
		//Background
		g.setColor(Color.black);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		//Elements
		if(gameState.equals("playing") || gameState.equals("paused")) {
			
			//World
			world.render(g);
			
			//Player
			player.render(g);
			
			//Items
			for (int i = 0; i < items.size(); i++) {
				items.get(i).render(g);
			}
			
			//Bullets
			for (int i = 0; i < bullets.size(); i++) {
				bullets.get(i).render(g);
			}
			
			//Enemies
			for (int i = 0; i < enemies.size(); i++) {
				enemies.get(i).render(g);
			}
			
			if( gameState.equals("paused")) {
				g.setColor(new Color(0,0,0,155));
				g.fillRect(0, 0, WIDTH, HEIGHT);
			}
				

		}
		
		//Game User Interface
		ui.render(g);
		
		bs.show();
		
	}
	
	/**
	 * Starts the program
	 * 
	 * @param String[] arguments
	 */
	public static void main(String[] args) {
		Game game = new Game();
		JFrame jframe = new JFrame();
		
		//Adding this class to the Java frame
		jframe.add(game);
		
		//Game title
		jframe.setTitle("Peng");
		
		//Prevent resizing and fullscreen
		jframe.setResizable(false);
		
		//Apply the screen size values
		jframe.pack();

		//Makes the Java frame appears in the center
		jframe.setLocationRelativeTo(null);
		
		//Making sure the application ends after closing the Java frame
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
		//Showing the game window to the user
		jframe.setVisible(true);
		
		//Starting the game loop
		new Thread(game).start();
	}
	

	@Override
	public void run() {
		
		while(true) {
			update();
			render();
			
			//60 FPS Cap
			try {
				Thread.sleep(1000/60);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}


	@Override
	public void keyPressed(KeyEvent e) {
		
		if(gameState == "playing") {
		
			if (e.getKeyCode() == KeyEvent.VK_A) {
				player.left = true;
			} else if (e.getKeyCode() == KeyEvent.VK_D) {
				player.right = true;
			}
			
			if (e.getKeyCode() == KeyEvent.VK_W) {
				player.up = true;
			} else if (e.getKeyCode() == KeyEvent.VK_S) {
				player.down = true;
			}
			
			if(e.getKeyCode() == KeyEvent.VK_SPACE) {
				player.shoot = true;
			}
			
			if (e.getKeyCode() == KeyEvent.VK_ENTER) gameState = "paused";
			
		}else if(gameState == "title") {
			
			if (e.getKeyCode() == KeyEvent.VK_ENTER) gameState = "playing";
			
		}else if (gameState == "paused") {
			
			if (e.getKeyCode() == KeyEvent.VK_ENTER) gameState = "playing";
		}
	}


	@Override
	public void keyReleased(KeyEvent e) {
		if(gameState == "playing") {
			if (e.getKeyCode() == KeyEvent.VK_A) {
				player.left = false;
			} else if (e.getKeyCode() == KeyEvent.VK_D) {
				player.right = false;
			}
			
			if (e.getKeyCode() == KeyEvent.VK_W) {
				player.up = false;
			} else if (e.getKeyCode() == KeyEvent.VK_S) {
				player.down = false;
			}
		}
	}
	
	/**
	 * Restart the game
	 */
	protected static void restart() {
		gameState = "title";
		bullets.clear();
		enemies.clear();
		items.clear();
		player = new Player(SIZE, SIZE);
		lastScore = score;
		
		// Check if current score beats the highscore
		if (score > highscore) {
			highscore = score;
			Game.saveHighscore();
		}
		
		score = 0;
	}

}