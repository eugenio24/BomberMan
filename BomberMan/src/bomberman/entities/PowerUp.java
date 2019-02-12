
package bomberman.entities;

import bomberman.Game;
import bomberman.graphics.SpriteSheet;
import bomberman.graphics.Sprite;
import bomberman.graphics.RenderHandler;

/**
 *
 * @author Eugenio
 */
public class PowerUp implements GameObject{
    
    private Rectangle rect;
    private SpriteSheet sheet;
    private PowerUpType type;
    
    public PowerUp(int x, int y, SpriteSheet sheet, PowerUpType type){
        this.rect = new Rectangle(x, y, 16, 16);
        this.type = type;        
        this.sheet = sheet;
        this.sheet.loadSprites(16, 16);
    }

    @Override
    public void render(RenderHandler renderer, int xZoom, int yZoom) {        
        renderer.renderSprite(getSpriteByType(), rect.getX(), rect.getY(), xZoom, yZoom);
    }

    @Override
    public void update(Game game) {
        
    }
    
    private Sprite getSpriteByType(){
        switch(this.type){
            case ACCELERATION:
                return sheet.getSprite(2, 13);
            default:
                return sheet.getSprite(16, 16);
        }
    }
    
    public enum PowerUpType{
        ACCELERATION
    }
    
}
