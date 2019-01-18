
package bomberman;

/**
 *
 * @author Eugenio
 */
public class Player implements GameObject{
    private Rectangle playerRect;
    int speed = 10;
    
    public Player(){
        this.playerRect = new Rectangle(200, 200, 16, 16);
        playerRect.generateGraphics(0xFF00FF90); 
    }

    @Override
    public void render(RenderHandler renderer, int xZoom, int yZoom) {
        renderer.renderRectangle(playerRect, xZoom, yZoom); 
    }

    @Override
    public void update(Game game) {
        KeyboardListener keyListener = game.getKeyListener();
        if(keyListener.up()){
            playerRect.setY(playerRect.getY()-speed);
            if(playerRect.getY()<0){
                playerRect.setY(0);
            }
        }
        if(keyListener.down()){
            playerRect.setY(playerRect.getY()+speed);                        
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
    }
}
