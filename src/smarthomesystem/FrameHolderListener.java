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
public interface FrameHolderListener {

    public void onPress(MouseEvent e);
    
    public void onMouseEnter(MouseEvent e);
    
    public void onMouseHover(MouseEvent e);
    
    public void onMouseExit(MouseEvent e);
}
