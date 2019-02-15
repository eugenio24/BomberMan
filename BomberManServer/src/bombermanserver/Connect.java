
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
import java.util.logging.Level;
import java.util.logging.Logger;

//Classe che gestisce la singola connessione verso un client del gioco
class Connect extends Thread {

    private Connect player2;
    private Socket client = null;
    private ObjectOutputStream outToClient;
    private ObjectInputStream inFromClient;

    public Connect(Socket clienSocket) {
        client = clienSocket;
        try {
            outToClient = new ObjectOutputStream(client.getOutputStream());
            inFromClient = new ObjectInputStream(client.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(Connect.class.getName()).log(Level.SEVERE, null, ex);
            try {
                client.close();
            } catch (IOException e) {
                Logger.getLogger(Connect.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }
    
    public void addPlayer2(Connect player2){
        this.player2 = player2;
        try {
            outToClient.writeUnshared(new Message(MessageType.CLIENT_CONNECTED));
        } catch (IOException ex) {
            Logger.getLogger(Connect.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }

    @Override
    public void run() {  //Thread di lettura
        while (true) {//aggiungere condizione di uscita
            try {
                Object obj = inFromClient.readUnshared();

                if (obj instanceof Message) {
                    switch (((Message) obj).getMessageType()) {
//                        case "close": //Se arriva un messaggio di chiusura connessione
//                            closeConection(0);
//                            break;
                        default:
                            player2.sendObject(obj);
                            break;
                    }
                }                    
            }
            catch(IOException | ClassNotFoundException e){
                Logger.getLogger(Connect.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }

    public void sendObject(Object msg) {
        try {
            outToClient.writeUnshared(msg);
        } catch (IOException ex) {
            Logger.getLogger(Connect.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    // 0 closed by myself , 1 closed by other Player
    public synchronized void closeConection(int state) {
        try {
            if(state == 1){
                outToClient.writeUnshared("close");
            }
            player2.closeConection(1);
            outToClient.close();
            inFromClient.close();
            client.close();
        } catch (IOException ex) {
            Logger.getLogger(Connect.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
