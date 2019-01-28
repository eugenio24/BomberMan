/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bomberman;

import java.awt.event.MouseEvent;

/**
 *
 * @author matteo
 */
public class MouseListener implements java.awt.event.MouseListener{
    EscMenu escMenu;

    public MouseListener(EscMenu escMenu) {
        this.escMenu = escMenu;
    }
    

    @Override
    public void mouseClicked(MouseEvent me) {
        //PER DEBUG: System.out.println("X: " + me.getX() + " Y: " + me.getY());
        
        //Se si clicca nell'area del pulsante di exit (e il menù è mostrato), richiama il metodo di escMenu
        if(escMenu.isIsMenuDisplayed()){
            if(me.getX() < 596 && me.getX() > 366){
                if(me.getY() < 554 && me.getY() > 490){
                    escMenu.exitClicked();
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent me) {}

    @Override
    public void mouseReleased(MouseEvent me) {}

    @Override
    public void mouseEntered(MouseEvent me) {}

    @Override
    public void mouseExited(MouseEvent me) {}
    
}
