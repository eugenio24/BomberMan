
package bombermanserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author matteo.devigili
 */
public class Server extends Thread{
    private final ServerSocket Server;
    private ArrayList<Match> matchs = new ArrayList<>();
    
    public Server() throws Exception {
        Server = new ServerSocket(4000);
    }
    
    @Override
    public void run() {        
        System.out.println("Server started on port "+Server.getLocalPort());
        
        while(true){            
            try {
                Socket client1 = Server.accept();
                Player player1 = new Player(client1);
                System.out.println(client1.getInetAddress() + " is trying to connect");

                Socket client2 = Server.accept();
                Player player2 = new Player(client2);
                System.out.println(client2.getInetAddress() + " is trying to connect");
                
                this.matchs.add(new Match(player1, player2)); 
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
