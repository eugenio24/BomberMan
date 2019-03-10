
package bomberman;

import bomberman.graphics.MenuJFrame;

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
    public static void launchMenu(){
        MenuJFrame menuFrame = new MenuJFrame();
        menuFrame.setVisible(true);
    }
    
    //Lancia il gioco
    public static void launchGame(){
        Game game = new Game();
        new Thread(game).start();
    }
}
