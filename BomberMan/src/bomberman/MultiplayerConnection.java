/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bomberman;

/**
 *
 * @author matteo.devigili
 */

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

//Classe che gestisce la connessione verso il server
public class MultiplayerConnection {

    private final Multiplayer mp;
    private ObjectOutputStream outToServer;
    private ObjectInputStream inFromServer;
    private Socket socket;
    private Thread Tr;

    private boolean conectionOpened;

    public MultiplayerConnection(Multiplayer mp) {
        this.mp = mp;
        conectionOpened = false;
    }

    /**
     *
     * @param IP
     * @param port
     * @return true if the connection has been established
     */
    public boolean startConnection(String IP, int port) { //default localhost 4000

        try {
            socket = new Socket(IP, port);
            outToServer = new ObjectOutputStream(socket.getOutputStream());
            inFromServer = new ObjectInputStream(socket.getInputStream());
            conectionOpened = true;
            
            Tr = new Thread(new reader(this, mp));//creo e avvio un tread responsabile della lettura
            Tr.start();
            
            return true;
//            }
        } catch (IOException ex) {
            conectionOpened = false;
            return false;
        }
    }

    private boolean isConectionOpened() {
        return conectionOpened;
    }

    /**
     *
     * @param gameObject to send to the server
     */
    public void sendObject(Object gameObject) {
        if (conectionOpened) {
            try {
                outToServer.writeUnshared(gameObject);
            } catch (IOException ex) {
                Logger.getLogger(MultiplayerConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     *
     * @param state 0 Closing request from client | 1 Closing request from server | -1 closing for
     * error
     *
     */
    public void closeConection(int state) {
        if (conectionOpened) {
            try {
                conectionOpened = false;
                if (state == 0) {
                    outToServer.writeUnshared("close"); //Mando il messaggio per chiudere la parte server
                } else {
                    mp.closeGame();
                }
                Tr.stop();//fermo il thread che legge i messaggi (soluzione non elegante) (Dovrebbe fermarsi da solo)
                outToServer.close();//chiudo il resto
                inFromServer.close();
                socket.close();

            } catch (IOException ex) {
            }
        }

    }
    
    public class reader implements Runnable { //Thread responsabile della lettura dei messaggi inviati dal server

        MultiplayerConnection mc;
        Multiplayer mp;

        public reader(MultiplayerConnection mc, Multiplayer mp) {
            this.mc = mc;
            this.mp = mp;
        }

        @Override
        public void run() {
            while (mc.isConectionOpened()) {
                try {
                    Object obj = mc.inFromServer.readUnshared();
                    if (obj.getClass() == String.class) {
                        switch ((String) obj) {
                            case "close": //Se arriva un messaggio di chiusura connessione
                                mc.closeConection(1);
                                mp.disconnected();  //Informo il gioco
                                break;
                            case "connected":   //Se arriva un messaggio di connessione avvenuta
                                mp.connected();   //Informo il gioco
                                break;
                            default:
                                mp.receive((String) obj);
                                break;
                        }
                    }
                } catch (IOException ex) {
                    Logger.getLogger(MultiplayerConnection.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(MultiplayerConnection.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
    }
}
