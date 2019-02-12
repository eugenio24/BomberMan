
package bomberman.entities;

import bomberman.Game;
import bomberman.graphics.RenderHandler;

/**
 *
 * @author Eugenio
 */
public interface GameObject {
    
    public void render(RenderHandler renderer, int xZoom, int yZoom);
    
    public void update(Game game);
    
}
