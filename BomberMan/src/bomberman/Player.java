
package bomberman;
/**
 *
 * @author Eugenio
 */
public class Player implements GameObject{
    private Rectangle playerRect;
    int speed = 3;
    Direction direction = Direction.DOWN;
    SpriteSheet sheet;
    
    public Player(SpriteSheet sheet){
        this.playerRect = new Rectangle(0, 0, 16, 16);
        playerRect.generateGraphics(0xFF00FF90); 
        
        this.sheet = sheet;
        this.sheet.loadSprites(16, 16);
    }

    @Override
    public void render(RenderHandler renderer, int xZoom, int yZoom) {    
        Sprite sprite;
        switch(direction){
            case UP:
                sprite = sheet.getSprite(1, 0);
                break;
            case DOWN:
                sprite = sheet.getSprite(0, 0);
                break;
            case LEFT:
                sprite = sheet.getSprite(3, 0);
                break;
            case RIGHT:
                sprite = sheet.getSprite(2, 0);
                break;
            default:
                sprite = sheet.getSprite(0, 0);
                break;
        }
               
        renderer.renderSprite(sprite, playerRect.getX(), playerRect.getY(), xZoom, yZoom);
    }
    

    @Override
    public void update(Game game) {
        KeyboardListener keyListener = game.getKeyListener();
        
        int precX = playerRect.getX();
        int precY = playerRect.getY();
        
        if(keyListener.up()){
            direction = Direction.UP;
            playerRect.setY(playerRect.getY()-speed);
            if(playerRect.getY()<0){
                playerRect.setY(0);
            }
        }
        if(keyListener.down()){
            playerRect.setY(playerRect.getY()+speed);
            direction = Direction.DOWN;
            if(playerRect.getY() > game.getContentPane().getHeight()-playerRect.getH()*3){
                playerRect.setY(game.getContentPane().getHeight()-playerRect.getH()*3); 
            }
        }
        if(keyListener.left()){
            playerRect.setX(playerRect.getX()-speed);
            direction = Direction.LEFT;
            if(playerRect.getX() < 0){
                playerRect.setX(0);
            }
        }
        if(keyListener.right()){
            playerRect.setX(playerRect.getX()+speed);
            direction = Direction.RIGHT;
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
    
    public enum Direction{
        UP, DOWN, LEFT, RIGHT
    }
}
