
package bomberman.graphics;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JOptionPane;

/**
 *
 * @author Eugenio
 */
public class Map {
    private ArrayList<MappedTile> backgroundTiles = new ArrayList<>();
    private ArrayList<MappedTile> indistructibleTiles = new ArrayList<>();
    private ArrayList<MappedTile> bushTiles = new ArrayList<>();
    
    private Tiles tileSet;
    private final int xZoom;
    private final int yZoom;
    
    public Map(Tiles tileSet, int xZoom, int yZoom, int screenX, int screenY){
        this.tileSet = tileSet;
        this.xZoom = xZoom;
        this.yZoom = yZoom;
        
        fillBackgrond(screenX, screenY);
        
        fillIndestructibleBlock(screenX, screenY);
        
        fillBush();
    }
    
    private void fillBackgrond(int screenX, int screenY){
        for(int i = 0; i<screenY; i+=16*yZoom){
            for(int j = 0; j<screenX; j+=16*xZoom){  
                backgroundTiles.add(new MappedTile(1, j, i)); 
            }
        }
    }
    
    private void fillIndestructibleBlock(int screenX, int screenY){
        for(int y = 16*yZoom; y<screenY-16*yZoom; y+=16*yZoom*2){
            for(int x = 16*xZoom; x<screenX-16*xZoom; x+=16*xZoom*2){                
                indistructibleTiles.add(new MappedTile(2, x, y));
            }
        }        
    }
    
    private void fillBush(){        
        Scanner scan = new Scanner(getClass().getResourceAsStream("/bomberman/assets/map.txt"));

        while(scan.hasNext()){
            String[] splitted = scan.nextLine().split("-");
            int x = Integer.parseInt(splitted[0]);
            int y = Integer.parseInt(splitted[1]);
            
            bushTiles.add(new MappedTile(3, x, y));                
        }                     
    }
    
    /**
     * Renderizza la mappa
     * @param renderer RenderHandler
     */
    public void render(RenderHandler renderer){
        backgroundTiles.forEach((t) -> {
            tileSet.renderTile(t.tileID, renderer, t.x, t.y, xZoom, yZoom);
        });
        
        indistructibleTiles.forEach((t) -> {
            tileSet.renderTile(t.tileID, renderer, t.x, t.y, xZoom, yZoom);
        });
        
        bushTiles.forEach((t) -> {
            tileSet.renderTile(t.tileID, renderer, t.x, t.y, xZoom, yZoom);
        });
    }
    
    public boolean collideIndestructibleBlock(java.awt.Rectangle playerRect){
        return indistructibleTiles.stream().anyMatch((blockTile) -> (playerRect.intersects(blockTile.getBounds())));
    }
    
    public boolean collideBush(java.awt.Rectangle playerRect){
        return bushTiles.stream().anyMatch((blockTile) -> (playerRect.intersects(blockTile.getBounds())));
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
        
        public java.awt.Rectangle getBounds(){
            return new java.awt.Rectangle(x, y, 16*xZoom, 16*yZoom);
        }
    }
}
