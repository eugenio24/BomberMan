
package bombermanserver;

/**
 *
 * @author matteo.devigili
 */
public class BomberManServer {
    
    Server server;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {        
        BomberManServer bmserver = new BomberManServer();
        bmserver.launchServer();
    }
    
    public void launchServer(){
        try {
            server = new Server();
            server.start();
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }
    
}
