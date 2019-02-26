
package bomberman.entities;

import bomberman.Game;
import bomberman.KeyboardListener;
import bomberman.graphics.SpriteSheet;
import bomberman.graphics.Sprite;
import bomberman.graphics.RenderHandler;

/**
 *
 * @author Eugenio
 */
public class Player implements GameObject{
    private Rectangle playerRect;
    private SpriteSheet sheet;
    private SpriteSheet sheetBomb;
    
    private int speed = 3;
    private Direction direction = Direction.DOWN;    
    
    private boolean isEnemy;
    
    /**
     * Constructor 
     * @param sheet SpriteSheet player 
     * @param sheetBomb SpriteSheet bomb
     * @param isEnemy Boolean true if enemy
     */
    public Player(SpriteSheet sheet, SpriteSheet sheetBomb, boolean isEnemy){
        this.direction = Direction.DOWN;
        this.playerRect = new Rectangle(0, 0, 16, 16);        
        
        this.sheet = sheet;
        this.sheet.loadSprites(16, 16);
        this.sheetBomb = sheetBomb;
        
        this.isEnemy = isEnemy;
    }
    
    /**
     * Constructor
     * @param player Player
     */
    public Player(Player player){
        this.direction = player.direction;
        this.isEnemy = player.isEnemy;
        this.playerRect = new Rectangle(player.playerRect);        
        this.sheet = player.sheet;        
        this.sheetBomb = player.sheetBomb;
        this.speed = player.speed;
    }

    @Override
    public void render(RenderHandler renderer, int xZoom, int yZoom) {    
        Sprite sprite;
        int y = isEnemy ? 1 : 0;
        
        switch(direction){
            case UP:
                sprite = sheet.getSprite(1, y);
                break;
            case DOWN:
                sprite = sheet.getSprite(0, y);
                break;
            case LEFT:
                sprite = sheet.getSprite(3, y);
                break;
            case RIGHT:
                sprite = sheet.getSprite(2, y);
                break;
            default:
                sprite = sheet.getSprite(0, y);
                break;
        }
               
        renderer.renderSprite(sprite, playerRect.getX(), playerRect.getY(), xZoom, yZoom);
    }
    
    /**
     * Metodo che calcola la posizione delle bombe in base alla posizione del player
     * @return Array -> Array[0] x, Array[1] y
     */
    public int[] calculateBombPos(){
        int x = (int)Math.round((playerRect.getX()/64.0)-0.25)*64;
        int y = (int)Math.round((playerRect.getY()/64.0)-0.25)*64;
        
                
        return new int[] {x, y};
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
            if(playerRect.getY() > game.getContentPane().getHeight()-playerRect.getH()*4){
                playerRect.setY(game.getContentPane().getHeight()-playerRect.getH()*4); 
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
            if(playerRect.getX() > game.getContentPane().getWidth()-playerRect.getW()*4){
                playerRect.setX(game.getContentPane().getWidth()-playerRect.getW()*4); 
            }
        }
        
        if(keyListener.space()){
            int[] bombPos = calculateBombPos();
            
            game.addBomb(new Bomb(sheetBomb, bombPos[0], bombPos[1]), false);
        }

        if(game.getMap().collideIndestructibleBlock(new java.awt.Rectangle(playerRect.getX()+7, playerRect.getY()+7, 16*4-7, 16*4-7))){
            playerRect.setX(precX);
            playerRect.setY(precY);
        }
        
        if(game.collideExplosion(new java.awt.Rectangle(playerRect.getX(), playerRect.getY(), 16*4, 16*4))){
            game.loseGame();
        }
        
        if(game.getMap().collideBush(new java.awt.Rectangle(playerRect.getX()+7, playerRect.getY()+7, 16*4-7, 16*4-7))){
            playerRect.setX(precX);
            playerRect.setY(precY);
        }
    }
    
    
    // GETTER - SETTER
    
    public Rectangle getPlayerRect(){
        return this.playerRect;
    }
    
    public void setX(int value){
        this.playerRect.setX(value);
    }
    
    public int getX(){
        return this.playerRect.getX();
    }
    
    public void setY(int value){
        this.playerRect.setY(value);
    }
    
    public int getY(){
        return this.playerRect.getY();
    }
    
    public Direction getDirection(){
        return this.direction;
    }
    
    public void setDirection(Direction value){
        this.direction = value;
    }   
}
