/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bomberman;

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
    
    private boolean isMenuDisplayed = false;
    BufferedImage convertedImg;

    public EscMenu() {
        try {
            BufferedImage bufImg = ImageIO.read(getClass().getResource("menuBackground.jpg"));
            convertedImg = new BufferedImage(bufImg.getWidth(), bufImg.getHeight(), BufferedImage.TYPE_INT_RGB);
            convertedImg.getGraphics().drawImage(bufImg, 0, 0, null);
        } catch (IOException ex) {
            Logger.getLogger(EscMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void render(RenderHandler renderer, int xZoom, int yZoom) {
        if(isMenuDisplayed){
            renderer.renderImage(convertedImg, 0, 0, 0, 0);
            System.out.println("MENU IN SHOW");
        }
    }

    @Override
    public void update(Game game) {
        KeyboardListener keyListener = game.getKeyListener();

        if(keyListener.esc()){
            if(isMenuDisplayed == true){
                isMenuDisplayed = false;
                System.out.println("MENU OFF");
            }else{
                isMenuDisplayed = true;
                System.out.println("MENU ON");
            }
        }
    }
}
