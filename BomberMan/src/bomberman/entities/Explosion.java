
package bomberman.entities;

import bomberman.Game;
import bomberman.graphics.Map;
import bomberman.graphics.MappedTile;
import bomberman.graphics.RenderHandler;
import bomberman.graphics.Sprite;
import bomberman.graphics.SpriteSheet;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Eugenio
 */
public class Explosion implements GameObject{
    private ArrayList<MappedExplosion> explosionsRects = new ArrayList<>();
    private SpriteSheet sheet;
    
    private boolean explosionEnded = false;
    
    private final Timer timer = new Timer();
    private int control = 0;
    
    /**
     * Constructor
     * @param sheet SpriteSheet
     * @param x int bomb x
     * @param y int bomb y
     * @param map game map
     */
    public Explosion(SpriteSheet sheet, int x, int y, Map map){
        this.sheet = sheet;
        
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(control >= 1){
                    timer.cancel();
                    timer.purge();
                }else{
                    control++;
                }
            }
        },1000 ,1000 );
        
        addExplosions(x, y, map);
    }
    
    /**
     * Metodo che calcola la posizione delle esplosioni
     * @param x center x
     * @param y center y
     * @param map game map
     */
    public void addExplosions(int x, int y, Map map){
        // centro dell'esplosione
        explosionsRects.add(new MappedExplosion(new Rectangle(x, y, 16*4, 16*4), sheet.getSprite(2, 5)));   
        
        ArrayList<MappedTile> bushToRemove = new ArrayList<>();
        
        //destra
        int result = map.checkTile(x+(16*4), y);
        if(x+(16*4) <= 978 && result != 1){
            explosionsRects.add(new MappedExplosion(new Rectangle(x+(16*4), y, 16*4, 16*4), sheet.getSprite(3, 5)));  
            if(result == 2)
                map.removeBush(new MappedTile(3, x+(16*4), y));
        }   
        
        //sinistra
        result = map.checkTile(x-(16*4), y);
        if(x-(16*4) >= 0 && result != 1){
            explosionsRects.add(new MappedExplosion(new Rectangle(x-(16*4), y, 16*4, 16*4), sheet.getSprite(0, 5)));
            if(result == 2)
                map.removeBush(new MappedTile(3, x-(16*4), y));
        }  
        
        //su
        result = map.checkTile(x, y-(16*4));
        if(y-(16*4) >= 0 && result != 1){
            explosionsRects.add(new MappedExplosion(new Rectangle(x, y-(16*4), 16*4, 16*4), sheet.getSprite(7, 0)));   
            if(result == 2)
                map.removeBush(new MappedTile(3, x, y-(16*4)));
        }  
        
        //giu
        result = map.checkTile(x, y+(16*4));
        if(y+(16*4) <= 879 && result != 1){
            explosionsRects.add(new MappedExplosion(new Rectangle(x, y+(16*4), 16*4, 16*4), sheet.getSprite(7, 2)));    
            if(result == 2)
                map.removeBush(new MappedTile(3, x, y+(16*4)));
        }                                  
    }

    @Override
    public void render(RenderHandler renderer, int xZoom, int yZoom) {
        explosionsRects.forEach(elem -> {
            renderer.renderSprite(elem.sprite, elem.rect.getX(), elem.rect.getY(), xZoom, yZoom);
        });
    }

    @Override
    public void update(Game game) {
        if(control >= 1){
            explosionEnded = true;
        }
    }
    
    public boolean isEnded(){
        return this.explosionEnded;
    }
    
    private class MappedExplosion{
        private Rectangle rect;
        private Sprite sprite;

        /**
         * Constructor
         * @param rect Position Rectangle
         * @param sprite Sprite
         */
        public MappedExplosion(Rectangle rect, Sprite sprite) {
            this.rect = rect;
            this.sprite = sprite;
        }        
    }
}
