
package bomberman;

import bomberman.graphics.Map;
import bomberman.entities.Bomb;
import bomberman.entities.Explosion;
import bomberman.entities.GameObject;
import bomberman.entities.Player;
import bomberman.graphics.SpriteSheet;
import bomberman.graphics.EscMenu;
import bomberman.graphics.Tiles;
import bomberman.graphics.RenderHandler;
import bomberman.multiplayer.MultiplayerConnection;
import bombermanserver.messages.BombMessage;
import bombermanserver.messages.PlayerMessage;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author Eugenio
 */
public class Game extends JFrame implements Runnable {
    private boolean thRunning = true;
    private final Canvas canvas = new Canvas();
    private final RenderHandler renderer;

    private final SpriteSheet sheet;
    private final Tiles tiles;
    private final EscMenu escMenu;
    
    private final KeyboardListener keyListener = new KeyboardListener();
    private MouseListener mouseListener;
    
    private final Map map;
    private final Player player;
    private Player enemy;
    
    private boolean isMultiplayer = false;
    private MultiplayerConnection multiplayerHandler = null;
    
    private final ArrayList<GameObject> gameObjects = new ArrayList<>();
    private final ArrayList<GameObject> objectsToAdd = new ArrayList<>();
    
    private final ArrayList<GameObject> enemyObjects = new ArrayList<>();
    
    public Game() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        setMinimumSize(new Dimension(978, 879));
        setPreferredSize(new Dimension(978, 879));   

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                disconnect();
                e.getWindow().dispose();
            }
        });        
        
        add(canvas);
        setVisible(true);
        canvas.createBufferStrategy(3);
                       
        pack();        
        setLocationRelativeTo(null);        
        
        renderer = new RenderHandler(getContentPane().getWidth(), getContentPane().getHeight());
        sheet = new SpriteSheet(loadImage("/bomberman/assets/SpritesGame.png"));
        
        sheet.loadSprites(16, 16);
        
        tiles = new Tiles("/bomberman/assets/testTiles.txt", sheet);

        map = new Map(tiles, 4, 4, getWidth(), getHeight());
        
        player = new Player(new SpriteSheet(loadImage("/bomberman/assets/playerSpriteSheet.png")), sheet, false);
        
        escMenu = new EscMenu(this);
        escMenu.addMouseListener(mouseListener);
        

        gameObjects.add(player);
        gameObjects.add(escMenu);
        
        mouseListener = new MouseListener(escMenu);
        canvas.addKeyListener(keyListener);
        canvas.addMouseListener(mouseListener);
        canvas.requestFocus();
     
        this.multiplayerHandler = new MultiplayerConnection("127.0.0.1", 4000, new SpriteSheet(loadImage("/bomberman/assets/playerSpriteSheet.png")), sheet);
        this.isMultiplayer = this.multiplayerHandler.isConnected();    
        this.multiplayerHandler.update(this);
    }
       
    /**
     * Metodo per gestire la logica
     */
    public void update() {
        if(isMultiplayer){
            this.multiplayerHandler.update(this);          
            multiplayerHandler.sendMessage(new PlayerMessage(player.getX(), player.getY(), player.getDirection()));
        }
        
        gameObjects.addAll(objectsToAdd);
        objectsToAdd.clear();
        
        removeExplosedBomb();
        removeEndedExplosion();
        
        enemyObjects.forEach((obj) -> {            
            obj.update(this);
        });
        
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
        
        if(isMultiplayer){
            enemy.render(renderer, 3, 3);
        }
        
        enemyObjects.forEach((obj) -> {            
            obj.render(renderer, 4, 4);
        });        
        
        gameObjects.forEach((obj) -> {
            obj.render(renderer, 4, 4);
        });                
                
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
    private BufferedImage loadImage(String path) {
        try {
            BufferedImage loadedImage = ImageIO.read(getClass().getResource(path));                        
            BufferedImage formattedImage = new BufferedImage(loadedImage.getWidth(), loadedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
            formattedImage.getGraphics().drawImage(loadedImage, 0, 0, null);

            return formattedImage;
        }
        catch(IOException ex) {
            System.err.println(ex.getMessage());
            return null;
        }
    }
    
    /**
     * Metodo che fa l'update dell'avversario
     * @param en Player nemico aggiornato
     */
    public void updateEnemy(Player en){
        this.enemy = en;
    }
    
    /**
     * Metodo per aggiungere un'esplosione
     * @param explosion 
     */
    public void addExplosion(Explosion explosion){
        objectsToAdd.add(explosion);
    }
    
    public void addBomb(Bomb bomb, boolean havePowerUp){
        if(!havePowerUp){
            if(!(gameObjects.stream().anyMatch((obj) -> obj instanceof Bomb) || objectsToAdd.stream().anyMatch((obj) -> obj instanceof Bomb))){
                this.objectsToAdd.add(bomb);
                if(isMultiplayer)
                    this.multiplayerHandler.sendMessage(new BombMessage(bomb.getBombRect().getX(), bomb.getBombRect().getY()));
            }
        }else{            
            this.objectsToAdd.add(bomb);
            if(isMultiplayer)
                this.multiplayerHandler.sendMessage(new BombMessage(bomb.getBombRect().getX(), bomb.getBombRect().getY()));
        }
    }   
    
    /**
     * Metodo per aggiungere le bombe del nemico
     * @param bombs ArrayList of Bombs
     */
    public void addEnemyBombs(ArrayList<Bomb> bombs){
        this.enemyObjects.addAll(bombs);
    }
    
    /**
     * Metodo che rimuove le bombe esplose
     */
    private void removeExplosedBomb(){
        ArrayList<Bomb> toRemove = new ArrayList<>();
        
        gameObjects.forEach(obj -> {
            if(obj instanceof Bomb){
                if(((Bomb) obj).isEsplosed())
                    toRemove.add((Bomb)obj);
            }
        });
        
        gameObjects.removeAll(toRemove);
        
        toRemove.clear();
        enemyObjects.forEach(obj -> {
            if(obj instanceof Bomb){
                if(((Bomb) obj).isEsplosed())
                    toRemove.add((Bomb)obj);
            }
        });
        
        enemyObjects.removeAll(toRemove);
    }
    
    /**
     * Metodo che rimuove le esplosioni finite
     */
    public void removeEndedExplosion(){
        ArrayList<Explosion> toRemove = new ArrayList<>();
        
        gameObjects.forEach(obj -> {
            if(obj instanceof Explosion){
                if(((Explosion) obj).isEnded())
                    toRemove.add((Explosion)obj);
            }
        });
        
        gameObjects.removeAll(toRemove);
        
        toRemove.clear();
    }

    @Override
    public void run() {        
        BufferStrategy bufferStrategy = canvas.getBufferStrategy();
        
        if(isMultiplayer){
            System.out.println("multiplayer");            
            Image loading = new ImageIcon(getClass().getResource("/bomberman/assets/loading.gif")).getImage();      

            while(!multiplayerHandler.isReady()){                     
                Graphics graphics = bufferStrategy.getDrawGraphics();
                graphics.drawImage(loading, 0, 0, this);
                graphics.dispose();
                bufferStrategy.show();
            }
        }else{
            System.out.println("singleplayer");
        }
        
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
        this.dispose();
    }    
    
    /**
     * Metodo per effettuare la disconnessione dal server
     */
    public void disconnect(){
        if(isMultiplayer){
            multiplayerHandler.lose();
        }
    }
    
    public void closeGame(){
        disconnect();
        thRunning = false;
    }
    
    
    // GETTERs
    
    public KeyboardListener getKeyListener() {
        return keyListener;
    }    
    
    public Map getMap(){
        return this.map;
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
