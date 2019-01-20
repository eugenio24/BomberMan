
package bomberman;

import java.util.ArrayList;

/**
 *
 * @author Eugenio
 */
public class Map {
    private ArrayList<MappedTile> backgroundTiles = new ArrayList<>();
    private ArrayList<MappedTile> indistructibleTiles = new ArrayList<>();
    
    private Tiles tileSet;
    private int xZoom;
    private int yZoom;
    
    public Map(Tiles tileSet, int xZoom, int yZoom, int screenX, int screenY){
        this.tileSet = tileSet;
        this.xZoom = xZoom;
        this.yZoom = yZoom;
        
        fillBackgrond(screenX, screenY);
        
        fillIndestructibleBlock(screenX, screenY);
    }
    
    private void fillBackgrond(int screenX, int screenY){
        for(int i = 0; i<screenY; i+=16*yZoom){
            for(int j = 0; j<screenX; j+=16*xZoom){  
                backgroundTiles.add(new MappedTile(1, j, i)); 
            }
        }
    }
    
    private void fillIndestructibleBlock(int screenX, int screenY){
        backgroundTiles.add(new MappedTile(2, 16*xZoom, 16*yZoom));
    }
    
    public void render(RenderHandler renderer){
        for(MappedTile t: backgroundTiles){            
            tileSet.renderTile(t.tileID, renderer, t.x, t.y, xZoom, yZoom);
        }
    }
    
    private class MappedTile {
        private int tileID;
        private int x;
        private int y;
        
        public MappedTile(int tile, int x, int y){
            this.tileID = tile;
            this.x = x;
            this.y = y;
        }
    }
}
