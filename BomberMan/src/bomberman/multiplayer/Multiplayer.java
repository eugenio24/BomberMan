
package bomberman.multiplayer;

import bomberman.Game;
import bomberman.entities.Bomb;
import bomberman.entities.GameObject;
import bomberman.entities.Player;
import bomberman.graphics.SpriteSheet;
import bomberman.graphics.RenderHandler;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author matteo
 */
public class Multiplayer {
    
    private RenderHandler renderer;
    private Game game;
    private Player player;
    private ArrayList<GameObject> gameObjects;
    
    private boolean isMultiplayerConnected = false;
    private ArrayList<GameObject> enemyObjects = new ArrayList<>();
    private Player enemyPlayer;
    private Semaphore sMulti = new Semaphore(1);
    private final MultiplayerConnection multiplayerConnection;
    private SpriteSheet sheet;

    public Multiplayer(Game game) {
        this.game = game;
        this.renderer = game.getRenderer();
        this.sheet = game.getSpriteSheet();
        this.player = game.getPlayer();
        this.gameObjects = game.getGameObjects();
        
        multiplayerConnection = new MultiplayerConnection(this);
        enemyPlayer = new Player(new SpriteSheet(game.loadImage("assets/playerSpriteSheet.png")), sheet, true);
    }
    
    public void connectToServer(){
        multiplayerConnection.startConnection("127.0.0.1", 4000);
    }
    
    public void connected(){
        isMultiplayerConnected = true;
    }
    
    public void disconnected(){
        enemyPlayer = null;
        enemyObjects = null;
        isMultiplayerConnected = false;
    }
    
    public synchronized void receive(String obj){
        try {
            sMulti.acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        String type = obj.split("-")[0];
        String data = obj.split("-")[1];
        
        System.out.println("DATI RICEVUTI \n TIPO: " + type);
        System.out.println(" DATA: " + data);
        
        switch(type){
            case "playerPos":
                enemyPlayer.getPlayerRect().setX(Integer.parseInt(data.split(",")[0]));
                enemyPlayer.getPlayerRect().setY(Integer.parseInt(data.split(",")[1]));
                enemyPlayer.setDirection(Player.Direction.valueOf(data.split(",")[2]));
                break;
            case "bomb":
                game.addEnemyBomb(new Bomb(sheet, Integer.parseInt(data.split(",")[0]), Integer.parseInt(data.split(",")[1])), Boolean.parseBoolean(data.split(",")[2]));                
                break;
        }
        
        sMulti.release();
    }
    
    public synchronized void render(){
        if(isMultiplayerConnected){
                try {
                    sMulti.acquire();
                } catch (InterruptedException ex) {
                    Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                enemyPlayer.render(renderer, 3, 3);
                
                enemyObjects.forEach((obj) -> {
                    obj.render(renderer, 3, 3);
                });
                
                sMulti.release();
        }
    }
    
    public void send(){
        if(isMultiplayerConnected){            
            String playerToSend = "playerPos-" + player.getPlayerRect().getX() + "," + player.getPlayerRect().getY() + "," + String.valueOf(player.getDirection());
            multiplayerConnection.sendObject(playerToSend);
        }
    }
    
    public void sendBomb(Bomb bomb, Boolean hasPowerUp){
        if(isMultiplayerConnected){
            String bombToSend = "bomb-" + bomb.getBombRect().getX() + "," + bomb.getBombRect().getY() + "," + hasPowerUp.toString();
            multiplayerConnection.sendObject(bombToSend);
        }
    }
    
    public void closeConnection(){
        multiplayerConnection.closeConection(0);
    }
    
    public void closeGame(){
        game.closeGame();
    }
}


