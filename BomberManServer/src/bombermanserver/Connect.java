
package bombermanserver;

/**
 *
 * @author matteo.devigili
 */
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

//Classe che gestisce la singola connessione verso un client del gioco
class Connect extends Thread {

    Connect player2;
    private Socket client = null;
    private ObjectOutputStream outToClient;
    private ObjectInputStream inFromClient;
    private String host;
    private Server s;

    public Connect(Socket clienSocket, Server s) {
        this.s = s;
        client = clienSocket;
        try {
            outToClient = new ObjectOutputStream(client.getOutputStream());
            inFromClient = new ObjectInputStream(client.getInputStream());
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
            try {
                client.close();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }
    
    public void addPlayer2(Connect player2){
        this.player2 = player2;
        try {
            outToClient.writeUnshared("connected");
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }

    @Override
    public void run() {  //Thread di lettura
        while (true) {//aggiungere condizione di uscita
            try {
                Object obj = inFromClient.readUnshared();

                if (obj instanceof String) {
                    switch ((String) obj) {
                        case "close": //Se arriva un messaggio di chiusura connessione
                            closeConection(0);
                            break;
                        default:
                            player2.sendObject(obj);
                            break;
                    }
                }                    
            }
            catch(IOException | ClassNotFoundException e){
                System.err.println(e.getMessage());
            }
        }
    }

    public void sendObject(Object gameObject) {
        try {
            outToClient.writeUnshared(gameObject);
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
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
            System.err.println(ex.getMessage());
        }
    }

}
