
package bomberman.graphics;

import bomberman.Game;
import bomberman.entities.GameObject;
import bomberman.KeyboardListener;
import bomberman.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author matteo
 */
public class EscMenu implements GameObject{
    
    private Game game;
    private MouseListener mouseListener;
    private boolean isMenuDisplayed = false;
    private BufferedImage convertedImg;

    public EscMenu(Game gm) { 
        game = gm;
        try {
            //Carica l'immagine del menù esc
            BufferedImage bufImg = ImageIO.read(getClass().getResource("/bomberman/assets/escMenu.jpg"));
            convertedImg = new BufferedImage(bufImg.getWidth(), bufImg.getHeight(), BufferedImage.TYPE_INT_RGB);
            convertedImg.getGraphics().drawImage(bufImg, 0, 0, null);
        } catch (IOException ex) {
            Logger.getLogger(EscMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void render(RenderHandler renderer, int xZoom, int yZoom) {
        if(isMenuDisplayed){
            //Se il menù è aperto, chiede di stamparlo a video al renderer
            renderer.renderImage(convertedImg, 177, 240, 1, 1);
        }
    }

    @Override
    public void update(Game game) {
        //Controlla se il tasto esc è stato premuto, assegna il valore (true o false) alla variabile privata isMenuDisplayed
        KeyboardListener keyListener = game.getKeyListener();
        isMenuDisplayed = keyListener.esc();
    }
    
    public void addMouseListener(MouseListener ml){
        mouseListener = ml;
    }
    
    public void exitClicked(){
        game.closeGame();
    }
    
    public void optionsClicked(){
        
    }

    public boolean isIsMenuDisplayed() {
        return isMenuDisplayed;
    }
}
