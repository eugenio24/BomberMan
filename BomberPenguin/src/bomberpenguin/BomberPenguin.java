
package bomberpenguin;

/**
 *
 * @author Eugenio
 */
public class BomberPenguin {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Game game = new Game();
        new Thread(game).start();
    }
}
