/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smarthomesystem;

/**
 *
 * @author Manel
 */
public interface LEDStateChangeListener {
    public void onSelected();
    public void onDeselected();
    
    public void onMouseEnteredEnclosure();
    public void onMouseExitedEnclosure();
    
    public void onMouseEnteredLED();
    public void onMouseExitedLED();
}
