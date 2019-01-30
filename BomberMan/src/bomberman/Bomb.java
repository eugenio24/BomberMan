/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bomberman;

import javax.swing.Timer;

/**
 *
 * @author alessandro.vescera
 */
public class Bomb implements GameObject {
    private Rectangle bombRect;
    SpriteSheet sheet;
    private int control;
    
    public Bomb(SpriteSheet sheet){
        this.bombRect = new Rectangle(0, 0, 16, 16);
        bombRect.generateGraphics(0xFF00FF90); 
        
        this.sheet = sheet;
    }

    @Override
    public void render(RenderHandler renderer, int xZoom, int yZoom) {
        
        switch(control){
            case 1:
                renderer.renderSprite(sheet.getSprite(4, 2), bombRect.getX(), bombRect.getY(), xZoom, yZoom);
                break;
            case 2:
                renderer.renderSprite(sheet.getSprite(4, 3), bombRect.getX(), bombRect.getY(), xZoom, yZoom);      
                break;
            case 3:
                renderer.renderSprite(sheet.getSprite(4, 4), bombRect.getX(), bombRect.getY(), xZoom, yZoom);     
                break;
        }
    }

    @Override
    public void update(Game game) {
        int[] time = new int[3000];
        
        
        if(control == 1){
        for(int i=0;i<time.length;i++){
            if(i == 1000){
                control = 2;
            }
            if(i == 2000){
                control = 3;
            }
        } 
        }
    }

    public void setControl(int control) {
        this.control = control;
    }
        
}
