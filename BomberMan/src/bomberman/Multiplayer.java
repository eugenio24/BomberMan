/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bomberman;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

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
    private MultiplayerConnection multiplayerConnection;
    private SpriteSheet sheet;

    public Multiplayer(Game game, SpriteSheet sheet, RenderHandler renderer, Player player, ArrayList<GameObject> gameObjects) {
        this.renderer = renderer;
        this.game = game;
        this.sheet = sheet;
        this.player = player;
        this.gameObjects = gameObjects;
        
        multiplayerConnection = new MultiplayerConnection(this);
        enemyPlayer = new Player(new SpriteSheet(game.loadImage("playerSpriteSheet.png")), sheet);
    }
    
    public void connectToServer(){
        multiplayerConnection.startConnection("localhost", 4000);
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
                enemyPlayer.playerRect.setX(Integer.parseInt(data.split(",")[0]));
                enemyPlayer.playerRect.setY(Integer.parseInt(data.split(",")[1]));
                enemyPlayer.direction = Player.Direction.valueOf(data.split(",")[2]);
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
            
            String playerToSend = "playerPos-" + player.playerRect.getX() + "," + player.playerRect.getY() + "," + String.valueOf(player.direction);
            
            multiplayerConnection.sendObject(playerToSend);
        }
    }
    
    public void closeConnection(){
        multiplayerConnection.closeConection(0);
    }
    
    public void closeGame(){
        game.closeGame();
    }
}


