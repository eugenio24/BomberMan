
package bomberman;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Eugenio
 */
public class Tiles {
    private SpriteSheet spriteSheet;
    private ArrayList<Tile> tileList = new ArrayList<Tile>();
    
    public Tiles(File tilesFile, SpriteSheet spriteSheet){
        this.spriteSheet = spriteSheet;
        try {
            Scanner scanner = new Scanner(tilesFile);
            
            while(scanner.hasNextLine()){
                String line = scanner.nextLine();
                
                String[] splittedLine = line.split("-");
                String tileName = splittedLine[0];
                int spriteX = Integer.parseInt(splittedLine[1]);
                int spriteY = Integer.parseInt(splittedLine[2]);                
                
                tileList.add(new Tile(tileName, spriteSheet.getSprite(spriteX, spriteY)));
            }
        } catch (FileNotFoundException ex) {
            System.err.println(ex.getMessage());
        }
    }
    
    public void renderTile(int tileIndex, RenderHandler renderer, int xPos, int yPos, int xZoom, int yZoom){
        if(tileIndex >= 0 && tileIndex < tileList.size()){
            renderer.renderSprite(tileList.get(tileIndex).getSprite(), xPos, yPos, xZoom, yZoom);
        }
    }
}
