
package bombermanserver;

/**
 *
 * @author matteo.devigili
 */
import bombermanserver.messages.Message;
import bombermanserver.messages.MessageType;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

//Classe che gestisce la singola connessione verso un client del gioco
class Player extends Thread {
    private Match match = null;
    private Player otherPlayer;
    private Socket client = null;
    
    private boolean running = true;            
    private Semaphore runnSemaphore = new Semaphore(1);
    
    private ObjectOutputStream outToClient;
    private ObjectInputStream inFromClient;
    private Semaphore outSemaphore = new Semaphore(1);

    /**
     * Constructor
     * @param clientSocket Socket
     */
    public Player(Socket clientSocket) {
        client = clientSocket;
        
        try {
            outToClient = new ObjectOutputStream(client.getOutputStream());
            inFromClient = new ObjectInputStream(client.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
            try {
                client.close();
            } catch (IOException e) {
                Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }
    
    /**
     * Match setter
     * @param value 
     */
    public void setMatch(Match value){
        this.match = value;
    }
    
    /**
     * Metodo che aggiunge l'altro giocatore
     * @param other Player otherPlayer
     */
    public void setOtherPlayer(Player other){
        this.otherPlayer = other;

        sendObject(new Message(MessageType.CLIENT_CONNECTED));
    }

    @Override
    public void run() {  //Thread di lettura
        while (getRunning()) { 
            try {            
                Object obj = inFromClient.readUnshared();

                if (obj instanceof Message) {
                    switch (((Message) obj).getMessageType()) {
                        case WIN:                             
                            match.playerWin(this);           
                            break;
                        case LOSE:                            
                            match.playerLose(this);
                            return;
                        default:
                            otherPlayer.sendObject(obj);
                            break;
                    }
                }                    
            }
            catch(IOException | ClassNotFoundException e){
                Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, e);
            }
        }        
    }

    public void sendObject(Object msg) {
        try {
            outSemaphore.acquire();
            outToClient.writeUnshared(msg);
            outSemaphore.release();
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    /**
     * Metodo per comunicare la vittoria
     */
    public void win(){        
        sendObject(new Message(MessageType.WIN));        
        stopRunning();
    }
    
    public boolean getRunning(){
        boolean temp = false;
        try {
            runnSemaphore.acquire();
            temp = running;
            runnSemaphore.release();
        } catch (InterruptedException ex) {
            Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
        }
        return temp;
    }
    
    public void stopRunning(){
        try {
            runnSemaphore.acquire();            
            this.running = false;
            runnSemaphore.release();
        } catch (InterruptedException ex) {
            Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
