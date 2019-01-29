
package bomberman;

import java.awt.image.BufferedImage;

/**
 *
 * @author Eugenio
 */
public class SpriteSheet {
    private int[] pixels;
    private final BufferedImage image;
    public final int SIZE_X;
    public final int SIZE_Y;
    private Sprite[] loadedSprites = null;
    private boolean spritesLoaded = false;
       
    private int spriteSizeX;

    public SpriteSheet(BufferedImage sheetImage) {
        this.image = sheetImage;
        this.SIZE_X = sheetImage.getWidth();
        this.SIZE_Y = sheetImage.getHeight();
        
        this.pixels = new int[SIZE_X*SIZE_Y];
        this.pixels = sheetImage.getRGB(0, 0, SIZE_X, SIZE_Y, pixels, 0, SIZE_X);
    }

    public void loadSprites(int spriteSizeX, int spriteSizeY) {
        this.spriteSizeX = spriteSizeX;
        this.loadedSprites = new Sprite[(SIZE_X / spriteSizeX) * (SIZE_Y / spriteSizeY)];

        int spriteID = 0;
        for(int y = 0; y < SIZE_Y; y += spriteSizeY) {
            for(int x = 0; x < SIZE_X; x += spriteSizeX) {
                loadedSprites[spriteID] = new Sprite(this, x, y, spriteSizeX, spriteSizeY);
                spriteID++;
            }
        }

        spritesLoaded = true;                
    }

    public Sprite getSprite(int x, int y) {
        if(spritesLoaded) {
            int spriteID = x + y * (SIZE_X / spriteSizeX);
            if(spriteID < loadedSprites.length) 
                return loadedSprites[spriteID];                
        }
        return null;
    }

    public int[] getPixels() {
        return pixels;
    }

    public BufferedImage getImage() {
        return image;
    }
}
