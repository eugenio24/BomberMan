
package bomberman;

import com.sun.glass.events.KeyEvent;

/**
 *
 * @author Eugenio
 */
public class Player implements GameObject{
    private Rectangle playerRect;
    int speed = 3;
    String control = "";
    SpriteSheet sheet;
    
    public Player(SpriteSheet sheet){
        this.playerRect = new Rectangle(0, 0, 16, 16);
        playerRect.generateGraphics(0xFF00FF90); 
        
        this.sheet = sheet;
    }

    @Override
    public void render(RenderHandler renderer, int xZoom, int yZoom) {
        renderer.renderSprite(sheet.getSprite(1, 4), playerRect.getX(), playerRect.getY(), xZoom, yZoom);
        switch(control){
            case "up":
                renderer.renderSprite(sheet.getSprite(0, 4), playerRect.getX(), playerRect.getY(), xZoom, yZoom);
                break;
            case "down":
                renderer.renderSprite(sheet.getSprite(1, 4), playerRect.getX(), playerRect.getY(), xZoom, yZoom);
                break;
        }
               
    }
    

    @Override
    public void update(Game game) {
        KeyboardListener keyListener = game.getKeyListener();
        
        int precX = playerRect.getX();
        int precY = playerRect.getY();
        
        if(keyListener.up()){
            control = "up";
            playerRect.setY(playerRect.getY()-speed);
            if(playerRect.getY()<0){
                playerRect.setY(0);
            }
        }
        if(keyListener.down()){
            playerRect.setY(playerRect.getY()+speed);
            control = "down";
            if(playerRect.getY() > game.getContentPane().getHeight()-playerRect.getH()*3){
                playerRect.setY(game.getContentPane().getHeight()-playerRect.getH()*3); 
            }
        }
        if(keyListener.left()){
            playerRect.setX(playerRect.getX()-speed);
            if(playerRect.getX() < 0){
                playerRect.setX(0);
            }
        }
        if(keyListener.right()){
            playerRect.setX(playerRect.getX()+speed);
            if(playerRect.getX() > game.getContentPane().getWidth()-playerRect.getW()*3){
                playerRect.setX(game.getContentPane().getWidth()-playerRect.getW()*3); 
            }
        }
        
        if(keyListener.space()){
            
        }

        if(game.getMap().collideIndestructibleBlock(new java.awt.Rectangle(playerRect.getX(), playerRect.getY(), 16*3, 16*3))){
            playerRect.setX(precX);
            playerRect.setY(precY);
        }
        
        if(game.getMap().collideBush(new java.awt.Rectangle(playerRect.getX(), playerRect.getY(), 16*3, 16*3))){
            playerRect.setX(precX);
            playerRect.setY(precY);
        }
    }
}
