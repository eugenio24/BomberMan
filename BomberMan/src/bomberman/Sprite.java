
package bomberman;

import java.awt.image.BufferedImage;

/**
 * 
 * @author Eugenio
 */
public class Sprite {
    private int width;
    private int height;
    private int[] pixels;

    public Sprite(SpriteSheet sheet, int startX, int startY, int width, int height) {
        this.width = width;
        this.height = height;

        pixels = new int[width*height];
        sheet.getImage().getRGB(startX, startY, width, height, this.pixels, 0, width);
    }

    public Sprite(BufferedImage image) {
        this.width = image.getWidth();
        this.height = image.getHeight();

        this.pixels = new int[width*height];
        image.getRGB(0, 0, width, height, pixels, 0, width);
    }


    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int[] getPixels() {
        return this.pixels;
    }
}
