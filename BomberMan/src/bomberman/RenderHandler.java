
package bomberman;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

/**
 *
 * @author Eugenio
 */
public class RenderHandler {
    private BufferedImage view;
    private int[] pixels;

    public RenderHandler(int width, int height) {
        //Create a BufferedImage that will represent our view.
        view = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        //Create an array for pixels
        pixels = ((DataBufferInt) view.getRaster().getDataBuffer()).getData();
    }

    public void render(Graphics graphics) {
        graphics.drawImage(view, 0, 0, view.getWidth(), view.getHeight(), null);
    }
    
    public void renderRectangle(Rectangle rectangle, int xZoom, int yZoom)
    {
        int[] rectanglePixels = rectangle.getPixels();
        if(rectanglePixels != null)
            renderArray(rectanglePixels, rectangle.getW(), rectangle.getH(), rectangle.getX(), rectangle.getY(), xZoom, yZoom);	
    }
    
    public void renderImage(BufferedImage image, int xPosition, int yPosition, int xZoom, int yZoom)
    {
        int[] imagePixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        renderArray(imagePixels, image.getWidth(), image.getHeight(), xPosition, yPosition, xZoom, yZoom);
    }

    public void renderSprite(Sprite sprite, int xPosition, int yPosition, int xZoom, int yZoom) {
        renderArray(sprite.getPixels(), sprite.getWidth(), sprite.getHeight(), xPosition, yPosition, xZoom, yZoom);
    }

    public void renderArray(int[] renderPixels, int renderWidth, int renderHeight, int xPosition, int yPosition, int xZoom, int yZoom) 
    {
        for(int y = 0; y < renderHeight; y++)
            for(int x = 0; x < renderWidth; x++)
                for(int yZoomPosition = 0; yZoomPosition < yZoom; yZoomPosition++)
                    for(int xZoomPosition = 0; xZoomPosition < xZoom; xZoomPosition++)
                        setPixel(renderPixels[x + y * renderWidth], (x * xZoom) + xPosition + xZoomPosition, ((y * yZoom) + yPosition + yZoomPosition));
    }

    private void setPixel(int pixel, int x, int y) {
        int pixelIndex = x + y * view.getWidth();
        if(pixelIndex < pixels.length && pixelIndex >= 0)
            pixels[pixelIndex] = pixel;
    }
    
    public void clear(){
        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = 0;
        }
    }
}
