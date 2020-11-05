/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smarthomesystem;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.Timer;
import static smarthomesystem.MainForm.BACKGROUND_COLOR;

/**
 *
 * @author Manel
 */
public class SHSPanel extends JPanel {
    
    //<editor-fold desc="Variabile si definiri de timere" defaultstate="collapsed">
    private int brightness;
    /**
     * Daca cumva vezi ca nu merge animatia de fade in cand schimbi panele, pune variabila asta pe false
     */
    public static boolean NETBEANS_DRAWING=true;
    
    private Timer fadeInTimer = new Timer(25, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent ae) {
            brightness += 25;
            if (brightness > 255) {
                brightness = 255;
                repaint();
                fadeInTimer.stop();
            } else {
                repaint();
            }
        }
    });
    @Deprecated
    private Timer fadeOutTimer = new Timer(20,new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent ae) {
            brightness-=5;
            if (brightness<0){
                brightness=0;
                repaint();
                fadeOutTimer.stop();
                setVisible(false);
            }else{
                repaint();
            }
        }
    });
    
    //</editor-fold>
    
    @Override
    public void paint(Graphics g) {
        //<editor-fold desc="body" defaultstate="collapsed">
        super.paint(g);
        if (!NETBEANS_DRAWING) {
            g.setColor(new Color(BACKGROUND_COLOR.getRed(), BACKGROUND_COLOR.getGreen(), BACKGROUND_COLOR.getBlue(), 255 - brightness));
            g.fillRect(0, 0, getWidth(), getHeight());
        }
        //</editor-fold>
    }
    
    public void fadeIn() {
        //<editor-fold desc="body" defaultstate="collapsed">
        if (fadeInTimer.isRunning()) {
            throw new IllegalStateException("Timer-ul fadeIn inca merge");
        }
        brightness = 0;
        repaint();
        setVisible(true);
        fadeInTimer.start();
        //</editor-fold>
    }

    public void fadeOut() {
        //<editor-fold desc="body" defaultstate="collapsed">
        brightness=0;
        setVisible(false);
        fadeInTimer.stop();
        //</editor-fold>
    }
}
