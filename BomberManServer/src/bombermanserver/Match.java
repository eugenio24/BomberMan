
package bombermanserver;

import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Eugenio
 */
public class Match {
    private Player player1;
    private Player player2;
    
    private boolean end = false;
    private Semaphore endSemaphore = new Semaphore(1);
    
    /**
     * Constructor
     * @param p1 Player 1
     * @param p2 Player 2
     */
    public Match(Player p1, Player p2){
        this.player1 = p1;
        this.player2 = p2;
        
        this.player1.setMatch(this);
        this.player1.setOtherPlayer(player2);
        this.player2.setMatch(this);
        this.player2.setOtherPlayer(player1);
        
        this.player1.start();
        this.player2.start();
    }
    
    public boolean isEnded(){
        boolean temp = false;
        try {
            endSemaphore.acquire();
            temp = end;
            endSemaphore.release();
        } catch (InterruptedException ex) {
            Logger.getLogger(Match.class.getName()).log(Level.SEVERE, null, ex);
        }
        return temp;
    }
    
    private void endMatch(){
        try {
            endSemaphore.acquire();
            end = true;
            endSemaphore.release();
        } catch (InterruptedException ex) {
            Logger.getLogger(Match.class.getName()).log(Level.SEVERE, null, ex);
        }
    }   
    
    public void playerLose(Player p){
        try{
            if(p.equals(player1)){
                player2.win();      
                player2.join();
                player1.lose(); 
                player1.join();
            }else{
                player1.win(); 
                player1.join();
                player2.lose();      
                player2.join();
            }             
        } catch (InterruptedException ex) {
            Logger.getLogger(Match.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        endMatch();
    }
}
