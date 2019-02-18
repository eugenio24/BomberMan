
package bomberman.graphics;

import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Eugenio
 */
public class Tiles {
    private SpriteSheet spriteSheet;
    private ArrayList<Tile> tileList = new ArrayList<Tile>();
    
    public Tiles(String tilesPath, SpriteSheet spriteSheet){
        this.spriteSheet = spriteSheet;    
        
        Scanner scanner = new Scanner(getClass().getResourceAsStream(tilesPath));

        while(scanner.hasNextLine()){
            String line = scanner.nextLine();

            String[] splittedLine = line.split("-");
            String tileName = splittedLine[0];
            int spriteX = Integer.parseInt(splittedLine[1]);
            int spriteY = Integer.parseInt(splittedLine[2]);                
            
            tileList.add(new Tile(tileName, spriteSheet.getSprite(spriteX, spriteY)));
        }
    }
    
    public void renderTile(int tileIndex, RenderHandler renderer, int xPos, int yPos, int xZoom, int yZoom){
        if(tileIndex >= 0 && tileIndex < tileList.size()){
            renderer.renderSprite(tileList.get(tileIndex).getSprite(), xPos, yPos, xZoom, yZoom);
        }
    }
}
