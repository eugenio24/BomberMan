
package bomberman;

import javax.swing.JFrame;

/**
 *
 * @author Eugenio
 */
public class BomberMan {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Game game = new Game();
        new Thread(game).start();
    }
}
