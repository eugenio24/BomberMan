
package bomberman;

import bomberman.PowerUp.PowerUpType;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

/**
 *
 * @author Eugenio
 */
public class Game extends JFrame implements Runnable {
    private boolean thRunning = true;
    private final Canvas canvas = new Canvas();
    private final RenderHandler renderer;

    private SpriteSheet sheet;
    private Tiles tiles;
    
    private Map map;
    private Player player;

    private final EscMenu escMenu;
    
    private final KeyboardListener keyListener = new KeyboardListener();
    private MouseListener mouseListener;
    
    private ArrayList<GameObject> gameObjects = new ArrayList<>();
    public GameObject tempObject;
    public boolean tempObjectToAdd = false;
    
    private Multiplayer multiplayer;
    
    public Game() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        setMinimumSize(new Dimension(978, 879));
        setPreferredSize(new Dimension(978, 879));      

        
        add(canvas);
        setVisible(true);
        canvas.createBufferStrategy(3);
                       
        pack();
        
        setLocationRelativeTo(null);
        
        
        renderer = new RenderHandler(getContentPane().getWidth(), getContentPane().getHeight());
        
        sheet = new SpriteSheet(loadImage("SpritesGame.png"));
        sheet.loadSprites(16, 16);
        
        tiles = new Tiles(new File("src/bomberman/testTiles.txt"), sheet);

        map = new Map(tiles, 4, 4, getWidth(), getHeight());
        
        player = new Player(new SpriteSheet(loadImage("playerSpriteSheet.png")), sheet);
        
        escMenu = new EscMenu(this);
        escMenu.addMouseListener(mouseListener);
        

        gameObjects.add(p);
        

        gameObjects.add(player);
        gameObjects.add(escMenu);
        
        mouseListener = new MouseListener(escMenu);
        canvas.addKeyListener(keyListener);
        canvas.addMouseListener(mouseListener);
        canvas.requestFocus();
        
        multiplayer = new Multiplayer(this);
    }
       
    /**
     * Metodo per gestire la logica
     */
    public void update() {
        if(tempObjectToAdd){
            gameObjects.add(tempObject);
            tempObjectToAdd = false;
        }
        
        gameObjects.forEach((obj) -> {
            obj.update(this);
        });
        multiplayer.send();
    }

    PowerUp p = new PowerUp(0, 0, new SpriteSheet(loadImage("powerUpsSpriteSheet.png")), PowerUpType.ACCELERATION);
    
    /**
     * Metodo per visualizzare il gioco
     */
    public void render() {
        BufferStrategy bufferStrategy = canvas.getBufferStrategy();
        Graphics graphics = bufferStrategy.getDrawGraphics();    
        
        //carico lo sfondo
        map.render(renderer);
        
        gameObjects.forEach((obj) -> {
            obj.render(renderer, 3, 3);
        });                
        
        multiplayer.render();
        
        renderer.render(graphics);

        graphics.dispose();
        bufferStrategy.show();
        renderer.clear();
    }
    
    /**
     * Metodo che carica un immagine
     * @param path Path
     * @return Bufferde Image
     */
    public BufferedImage loadImage(String path) {
        try {
            BufferedImage loadedImage = ImageIO.read(Game.class.getResource(path));
            BufferedImage formattedImage = new BufferedImage(loadedImage.getWidth(), loadedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
            formattedImage.getGraphics().drawImage(loadedImage, 0, 0, null);

            return formattedImage;
        }
        catch(IOException ex) {
            System.err.println(ex.getMessage());
            return null;
        }
    }

    @Override
    public void run() {
        BufferStrategy bufferStrategy = canvas.getBufferStrategy();
        
        multiplayer.connectToServer();

        long lastTime = System.nanoTime();
        double nanoSecondConversion = 1000000000.0 / 60; //60 frames per second
        double changeInSeconds = 0;

        // GAME LOOP
        while(thRunning) {
            long now = System.nanoTime();

            changeInSeconds += (now - lastTime) / nanoSecondConversion;
            while(changeInSeconds >= 1) {
                update();
                changeInSeconds--;
            }

            render();
            lastTime = now;
        }
        multiplayer.closeConnection();
        this.dispose();
    }
    
    // GETTER

    public KeyboardListener getKeyListener() {
        return keyListener;
    }    
    
    public Map getMap(){
        return this.map;
    }
    
    public void closeGame(){
        thRunning = false;
    }
    
    public void multiSendBomb(Bomb bomb){
        multiplayer.sendBomb(bomb);
    }
    
    public RenderHandler getRenderer(){
        return this.renderer;
    }
    
    public SpriteSheet getSpriteSheet(){
        return this.sheet;
    }
    
    public Player getPlayer(){
        return this.player;
    }
    
    public ArrayList<GameObject> getGameObjects(){
        return this.gameObjects;
    }
}
