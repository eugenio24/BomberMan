package bomberman.entities;

import bomberman.Game;
import bomberman.graphics.SpriteSheet;
import bomberman.graphics.Sprite;
import bomberman.graphics.RenderHandler;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author alessandro.vescera
 */
public class Bomb implements GameObject {
    private Rectangle bombRect;
    private SpriteSheet sheet;
    private Sprite sprite;
    
    private final Timer timer = new Timer();
    private int control = 0;
    
    private boolean isExplosed = false;
    
    /**
     * Constructor
     * @param sheet SpriteSheet
     * @param x int x
     * @param y int y
     */
    public Bomb(SpriteSheet sheet, int x, int y){
        this.bombRect = new Rectangle(x, y, 16, 16);
        bombRect.generateGraphics(0xFF00FF90); 
        
        this.sheet = sheet;
        this.sheet.loadSprites(16, 16);
        gestioneTimer();
    }
    
    public Bomb(Bomb bomb){
        this.bombRect = new Rectangle(bomb.bombRect);
        bombRect.generateGraphics(0xFF00FF90);
                       
        this.sheet = bomb.sheet;
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
                    sprite = sheet.getSprite(4, 4);
                    break;
            }
        renderer.renderSprite(sprite, bombRect.getX(), bombRect.getY(), xZoom, yZoom);
    }

    @Override
    public void update(Game game) {  
        if(control >= 3){
            System.out.println("booom");
            isExplosed = true;
        }
    }

    public void setControl(int control) {
        this.control = control;
    }
    
    private void gestioneTimer(){
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(control >= 3){
                    timer.cancel();
                    timer.purge();
                }else{
                    control++;
                }
            }
        },1000 ,1000 );
    }
    
    public Rectangle getBombRect() {
        return bombRect;
    }
    
    public boolean isEsplosed(){
        return isExplosed;
    }
}
