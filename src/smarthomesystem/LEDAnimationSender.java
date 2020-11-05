/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smarthomesystem;

import java.awt.Color;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import static smarthomesystem.SmartHomeSystem.getByte;
import static smarthomesystem.SmartHomeSystem.getSmartHomeSystem;

/**
 *
 * @author Manel
 */
public class LEDAnimationSender {

    public synchronized void sendAnimationCommand(byte[] command) {
        //System.out.println("trimit " + Arrays.toString(command));
        getSmartHomeSystem().send(command);
        try {
            //System.out.println("Asteptam");
            wait();
            // System.out.println("Gata asteptarea");
        } catch (InterruptedException ex) {
            Logger.getLogger(SettingsForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendUpdateCommand() {
        sendAnimationCommand(new byte[]{getByte(255)});
    }

    public void sendTurnOffAndUpdateCommand() {
        sendAnimationCommand(new byte[]{getByte(0), Frame.to8BitColor(Color.BLACK), getByte(74)});
    }

    public void initialise() {
        getSmartHomeSystem().send(new byte[]{'A', '1', '\r'});
    }

    public void terminate() {
        sendAnimationCommand(new byte[]{getByte(255), getByte(255), getByte(255), getByte(255), getByte(255), getByte(255)});
    }
}
