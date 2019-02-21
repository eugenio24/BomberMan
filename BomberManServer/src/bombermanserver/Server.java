
package bombermanserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author matteo.devigili
 */
public class Server extends Thread{
    private final ServerSocket Server;
    private Connect player1, player2;
    
    public Server() throws Exception {
        Server = new ServerSocket(4000);
    }
    
    @Override
    public void run() {
        
        System.out.println("Server started on port 4000");
        while(true){
            try {
                Socket client1 = Server.accept();
                player1 = new Connect(client1);
                System.out.println(client1.getInetAddress() + " is trying to connect");

                Socket client2 = Server.accept();
                player2 = new Connect(client2);
                System.out.println(client2.getInetAddress() + " is trying to connect");
                
                
                player1.addPlayer2(player2);
                player2.addPlayer2(player1);
                
                player1.start();
                player2.start();
                player1.join();
                player2.join();
            } catch (IOException | InterruptedException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
