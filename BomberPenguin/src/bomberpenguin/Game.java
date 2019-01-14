
package bomberpenguin;

import java.awt.Canvas;
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
    private Canvas canvas = new Canvas();
    private RenderHandler renderer;

    private SpriteSheet sheet;
    private Sprite sprite;
    private Tiles tiles;
    
    private Player player;
    
    private KeyboardListener keyListener = new KeyboardListener();
    
    private ArrayList<GameObject> gameObjects = new ArrayList<>();
    
    public Game() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(0,0, 1000, 800);
        setLocationRelativeTo(null);
        add(canvas);
        setVisible(true);
        canvas.createBufferStrategy(3);

        renderer = new RenderHandler(getWidth(), getHeight());
        
        sheet = new SpriteSheet(loadImage("testSprite.png"));
        sheet.loadSprites(16, 16);
        sprite = sheet.getSprite(4,1);
        tiles = new Tiles(new File("src/bomberpenguin/testTiles.txt"), sheet);
        
        player = new Player();
        gameObjects.add(player);
        
        canvas.addKeyListener(keyListener);
        canvas.requestFocus();
    }
    
    /**
     * Metodo per gestire la logica
     */
    public void update() {
        for(GameObject obj: gameObjects){
            obj.update(this);
        }
    }

    /**
     * Metodo per visualizzare il gioco
     */
    public void render() {
        BufferStrategy bufferStrategy = canvas.getBufferStrategy();
        Graphics graphics = bufferStrategy.getDrawGraphics();
        
        renderer.renderSprite(sprite, 0, 0, 5, 5);
        tiles.renderTile(0, renderer, 100, 10, 10, 10);

        for(GameObject obj: gameObjects){
            obj.render(renderer, 3, 3);
        }
        
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
    
}