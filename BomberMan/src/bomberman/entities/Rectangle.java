
package bomberman.entities;

/**
 *
 * @author Eugenio
 */
public class Rectangle {
    private int x;
    private int y;
    private int w;
    private int h;
    private int[] pixels;

    /**
     * Constructor
     * @param x int x
     * @param y int y
     * @param w int width
     * @param h int height
     */
    public Rectangle(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    /**
     * Constructor
     */
    public Rectangle() {
        this(0,0,0,0);
    }
    
    /**
     * Constructor
     * @param rect Rectangle 
     */
    public Rectangle(Rectangle rect){
        this.pixels = rect.pixels;
        this.h = rect.h;
        this.w = rect.w;
        this.x = rect.x;
        this.y = rect.y;
    }

    public void generateGraphics(int color) {
        pixels = new int[w*h];
        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = color;
        }
    }

    public int[] getPixels() {
        return pixels;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }
    
}
