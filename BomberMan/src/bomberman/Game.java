
package bomberman;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.UIManager;

/**
 *
 * @author Eugenio
 */
public class Game extends JFrame implements Runnable {
    private Canvas canvas = new Canvas();
    private RenderHandler renderer;

    private SpriteSheet sheet;
    private Tiles tiles;
    
    private Map map;
    private Player player;
    
    private KeyboardListener keyListener = new KeyboardListener();
    
    private ArrayList<GameObject> gameObjects = new ArrayList<>();
    
    public Game() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);        
             
        setMinimumSize(new Dimension(978, 879));
        setPreferredSize(new Dimension(978, 879));      
//        setResizable(false);
        
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
        
        player = new Player(sheet.getSprite(1, 4));
        gameObjects.add(player);
        
        canvas.addKeyListener(keyListener);
        canvas.requestFocus();
    }
       
    /**
     * Metodo per gestire la logica
     */
    public void update() {
        gameObjects.forEach((obj) -> {
            obj.update(this);
        });
    }

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
        
        renderer.render(graphics);

        graphics.dispose();
        bufferStrategy.show();
        renderer.clear();
    }
    
    private BufferedImage loadImage(String path) {
        try 
        {
            BufferedImage loadedImage = ImageIO.read(Game.class.getResource(path));
            BufferedImage formattedImage = new BufferedImage(loadedImage.getWidth(), loadedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
            formattedImage.getGraphics().drawImage(loadedImage, 0, 0, null);

            return formattedImage;
        }
        catch(IOException ex) 
        {
            System.err.println(ex.getMessage());
            return null;
        }
    }

    @Override
    public void run() {
        BufferStrategy bufferStrategy = canvas.getBufferStrategy();

        long lastTime = System.nanoTime();
        double nanoSecondConversion = 1000000000.0 / 60; //60 frames per second
        double changeInSeconds = 0;

        // GAME LOOP
        while(true) {
            long now = System.nanoTime();

            changeInSeconds += (now - lastTime) / nanoSecondConversion;
            while(changeInSeconds >= 1) {
                update();
                changeInSeconds--;
            }

            render();
            lastTime = now;
        }
    }

    public KeyboardListener getKeyListener() {
        return keyListener;
    }    
    
    public Map getMap(){
        return this.map;
    }
    
}