package bomberman;

import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author alessandro.vescera
 */
public class Bomb implements GameObject {
    private Rectangle bombRect;
    private SpriteSheet sheet;
    private Timer time;
    private int control;
    private int i;
    private Sprite sprite;
    
    public Bomb(SpriteSheet sheet, int x, int y){
        this.bombRect = new Rectangle(x, y, 16, 16);
        bombRect.generateGraphics(0xFF00FF90); 
        
        this.sheet = sheet;
        this.sheet.loadSprites(16, 16);
        gestioneTimer();
    }

    @Override
    public void render(RenderHandler renderer, int xZoom, int yZoom) {
        
            switch(control){
                case 0:
                    sprite = sheet.getSprite(4, 2);
                    break;
                case 1:
                    sprite = sheet.getSprite(4, 3);
                    break;
                case 2:
                    sprite = sheet.getSprite(4, 4);
                    break;
                default:
                    sprite = sheet.getSprite(4, 2);
                    break;
            }
        renderer.renderSprite(sprite, bombRect.getX(), bombRect.getY(), xZoom, yZoom);
    }

    @Override
    public void update(Game game) {  
    }

    public void setControl(int control) {
        this.control = control;
    }
    
    public void gestioneTimer(){
        time = new Timer();
        
            i = 0;
            control = 0;
        
            time.schedule(new TimerTask() {

                @Override
                public void run() {
                    if(i >= 2){
                        time.cancel();
                        time.purge();
                    }else{
                        i++;
                        control = i;
                    }
                }
            },1000 ,1000 );
    }
    
    public Rectangle getBombRect() {
        return bombRect;
    }
}
