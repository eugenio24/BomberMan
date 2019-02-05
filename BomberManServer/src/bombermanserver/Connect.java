/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bombermanserver;

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
            try {
                client.close();
            } catch (Exception e) {
            }
        }
    }
    
    public void addPlayer2(Connect player2){
        this.player2 = player2;
        try {
            outToClient.writeUnshared("connected");
        } catch (IOException ex) {
            Logger.getLogger(Connect.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {  //Thread di lettura

            while (true) {//aggiungere condizione di uscita
                try {
                    Object obj = inFromClient.readUnshared();

                    if (obj.getClass() == String.class) {
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
                catch(IOException e){
                }   
                catch (ClassNotFoundException ex) {
                    Logger.getLogger(Connect.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
    }

    public void sendObject(Object gameObject) {
        try {
            outToClient.writeUnshared(gameObject);
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
