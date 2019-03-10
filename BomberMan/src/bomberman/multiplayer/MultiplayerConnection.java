
package bomberman.multiplayer;

import bomberman.Game;
import bomberman.GameStatus;
import bomberman.entities.Bomb;
import bomberman.entities.Player;
import bomberman.graphics.SpriteSheet;
import bombermanserver.messages.BombMessage;
import bombermanserver.messages.InitialPositionMessage;
import bombermanserver.messages.Message;
import bombermanserver.messages.MessageType;
import bombermanserver.messages.PlayerMessage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Eugenio
 */
public class MultiplayerConnection extends Thread{
    private Socket serverSocket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    
    private final SpriteSheet bombSpriteSheet;
    
    private final Player player;
    private Semaphore playerSemaphore = new Semaphore(1);
    
    private ArrayList<Bomb> bombs = new ArrayList<>();
    private Semaphore bombsSemaphore = new Semaphore(1);
   
    private boolean online = false;
    private boolean ready = false;
    private Semaphore readySemaphore = new Semaphore(1);
    private GameStatus gameStatus = GameStatus.PLAYING;
    private Semaphore statusSemaphore = new Semaphore(1);
    
    private int[] myPosition = null;
    private Semaphore myPosSemaphore = new Semaphore(1);
    
    /**
     * Constructor
     * @param IP String server IP
     * @param port int server port
     * @param playerSheet SpriteSheet player
     * @param bombSheet SpriteSheet bomb
     */
    public MultiplayerConnection(String IP, int port, SpriteSheet playerSheet, SpriteSheet bombSheet){
        this.online = true;
        
        try {
            this.serverSocket = new Socket(InetAddress.getByName(IP), port);
            this.outputStream = new ObjectOutputStream(serverSocket.getOutputStream());
            this.inputStream = new ObjectInputStream(serverSocket.getInputStream());
        } catch (UnknownHostException ex) {
            this.online = false;
            Logger.getLogger(MultiplayerConnection.class.getName()).log(Level.SEVERE, "Errore connessione iniziale al server", ex);
        } catch (IOException ex) {
            this.online = false;
            Logger.getLogger(MultiplayerConnection.class.getName()).log(Level.SEVERE, "Errore connessione iniziale al server", ex);
        }
        
        this.player = new Player(playerSheet, bombSheet, true, 0, 0);    
        this.bombSpriteSheet = bombSheet;
                     
        if(online){            
            this.start();
        }
    }
    
    /**
     * Metodo per controllare se la connessione è andata a buon fine
     * @return true se connesso
     */
    public boolean isConnected(){
        return this.online;
    }
    
    /**
     * Metodo per controllare se c'è l'avversario
     * @return true se è pronto
     */
    public boolean isReady(){
        boolean temp = false;
        
        try {            
            readySemaphore.acquire();
            temp = this.ready;
            readySemaphore.release();            
        } catch (InterruptedException ex) {
            Logger.getLogger(MultiplayerConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return temp;
    }
    
    /**
     * GameStatus GETTER
     * @return GameStatus
     */
    public GameStatus getGameStatus(){
        GameStatus temp = null;
        
        try {            
            statusSemaphore.acquire();
            temp = this.gameStatus;
            statusSemaphore.release();            
        } catch (InterruptedException ex) {
            Logger.getLogger(MultiplayerConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return temp;
    }
    
    /**
     * GameStatus Setter
     * @param value GameStatus value
     */
    private void setGameStatus(GameStatus value){
        try {            
            statusSemaphore.acquire();
            this.gameStatus = value;
            statusSemaphore.release();            
        } catch (InterruptedException ex) {
            Logger.getLogger(MultiplayerConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * MyPosition GETTER
     * @return int [x, y]
     */
    public int[] getMyPosition(){
        int[] temp = null;
        
        try {            
            myPosSemaphore.acquire();
            temp = this.myPosition;
            myPosSemaphore.release();            
        } catch (InterruptedException ex) {
            Logger.getLogger(MultiplayerConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return temp;
    }
    
    /**
     * MyPosition Setter
     * @param x x Position
     * @param y y Position
     */
    private void setMyPosition(int x, int y){
        try {            
            myPosSemaphore.acquire();
            this.myPosition = new int[] {x, y};
            myPosSemaphore.release();            
        } catch (InterruptedException ex) {
            Logger.getLogger(MultiplayerConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Metodo per mandare un messaggio al server
     * @param msg Message messaggio da mandare
     */
    public void sendMessage(Message msg){
        try {
            this.outputStream.writeUnshared(msg);
        } catch (IOException ex) {
            Logger.getLogger(MultiplayerConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }   
    
    /**
     * Player Getter
     * @return A New Player Enemy Updated
     */
    private Player getPlayer(){
        Player p = null;
        try {
            playerSemaphore.acquire();
            p = new Player(this.player);
            playerSemaphore.release();
        } catch (InterruptedException ex) {
            Logger.getLogger(MultiplayerConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return p;
    }
    
    /**
     * Bombs Getter
     * @return ArrayList of Bomb
     */
    public ArrayList<Bomb> getAndRemoveBombs(){
        ArrayList<Bomb> temp = new ArrayList<>();
        try {
            bombsSemaphore.acquire();
            bombs.forEach((elem)->{
                temp.add(new Bomb(elem));
            });
            bombs.clear();
            bombsSemaphore.release();
        } catch (InterruptedException ex) {
            Logger.getLogger(MultiplayerConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return temp;
    }
    
    /**
     * Metodo per modificare i dati del Player
     * @param msg PlayerMessage
     */
    private void updatePlayer(PlayerMessage msg){
        try {
            this.playerSemaphore.acquire();
            this.player.setX(msg.getX());
            this.player.setY(msg.getY());
            this.player.setDirection(msg.getDirection());
        } catch (InterruptedException ex) {
            Logger.getLogger(MultiplayerConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.playerSemaphore.release();
    }
    
    /**
     * Metodo per aggiungere una bomba
     * @param msg BombMessage
     */
    private void updateBomb(BombMessage msg){
        try {
            this.bombsSemaphore.acquire();
            this.bombs.add(new Bomb(this.bombSpriteSheet, msg.getX(), msg.getY()));
        } catch (InterruptedException ex) {
            Logger.getLogger(MultiplayerConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.bombsSemaphore.release();
    }
    
    /**
     * Metodo che aggiorna il gioco con le modifice dell'altro giocatore
     * @param game Game
     */
    public void update(Game game){
        game.updateEnemy(this.getPlayer());
        game.addEnemyBombs(this.getAndRemoveBombs());
    }
    
    @Override
    public void run(){
        while(online){
            try {
                Object msg = inputStream.readUnshared();
                
                if(msg instanceof PlayerMessage){
                    updatePlayer((PlayerMessage)msg);
                }
                
                if(msg instanceof BombMessage){
                    updateBomb((BombMessage)msg);
                }
                
                if(msg instanceof InitialPositionMessage){
                    setMyPosition(((InitialPositionMessage)msg).getX(), ((InitialPositionMessage)msg).getY());
                }                       
                
                if(msg instanceof Message){
                    switch(((Message) msg).getMessageType()){                        
                        case CLIENT_CONNECTED:
                            readySemaphore.acquire();
                            ready = true;
                            readySemaphore.release();
                            break;
                        case WIN:
                            setGameStatus(GameStatus.WIN);
                            System.out.println("Vinto"); 
                            return;
                        case LOSE:
                            setGameStatus(GameStatus.LOSE);
                            System.out.println("Lose");
                            return;
                        default: break;
                    }
                }
                
            } catch (IOException | ClassNotFoundException | InterruptedException ex) {
                Logger.getLogger(MultiplayerConnection.class.getName()).log(Level.SEVERE, null, ex);
            }          
        }
    }
}
