/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smarthomesystem;

import java.awt.event.MouseEvent;

/**
 *
 * @author Manel
 */
public interface LEDAnimationButtonEventListener {

    public void mouseMoving(MouseEvent evt);
    
    public void mouseEntered(MouseEvent evt);

    public void mouseExited(MouseEvent evt);

    public void mousePressed(MouseEvent evt);

    public void mouseReleased(MouseEvent evt);

    public void mouseClicked(MouseEvent evt);
}
