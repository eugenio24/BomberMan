
package bomberman;

/**
 *
 * @author Eugenio
 */
public class BomberMan {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //Istanzia la classe
        BomberMan launcher = new BomberMan();
        launcher.launchMenu();
    }
    
    //Mostra il menù
    public void launchMenu(){
        MenuJFrame menuFrame = new MenuJFrame(this);
        menuFrame.setVisible(true);
    }
    
    //Lancia il gioco
    public void launchGame(){
        Game game = new Game();
        new Thread(game).start();
    }
}
