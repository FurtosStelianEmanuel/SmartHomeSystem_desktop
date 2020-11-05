/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smarthomesystem;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author Manel
 */
public class SplashScreen {

    //<editor-fold desc="Variabile si inner classes" defaultstate="collapsed">
    JFrame f;
    float vizibilitate = 0f;
    Icoana label;
    JLabel titlu;
    JLabel mesaj;
    float viteza = 0.02f;
    boolean runAnim = true;

    class Icoana extends JLabel {

        public Icoana(Icon icon) {
            super(icon);
        }

        @Override
        public void paintComponent(Graphics g) {
            AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, vizibilitate);
            Graphics2D anus=(Graphics2D)g;
            anus.setComposite(ac);
            super.paintComponent(anus);
        }
    }
    //</editor-fold>
    
    public SplashScreen() {
        //<editor-fold desc="Constructor body" defaultstate="collapsed">
        f = new JFrame();
        f.setSize(285, 350);
        f.setIconImage(new ImageIcon(getClass().getResource("/resurse/icon_connected.png")).getImage());
        f.setLayout(null);
        label = new Icoana(new ImageIcon(getClass().getResource("/resurse/icon_connected.png")));
        label.setForeground(Color.yellow);
        label.setSize(285, 285);
        label.setLocation(0, 0);
        f.setUndecorated(true);
        f.add(label);
        f.setBackground(new Color(1.0f, 1.0f, 1.0f, 0.0f));
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        f.setLocation((int) screenSize.getWidth() / 2 - f.getWidth() / 2,
                (int) screenSize.getHeight() / 2 - f.getHeight() / 2 - 20);
        titlu = new JLabel("Smart Home System");
        titlu.setForeground(new Color(0, 0, 0, 0));
        titlu.setFont(new Font("Times New Roman", Font.BOLD, 30));
        titlu.setLocation(0, 276);
        titlu.setSize(f.getWidth(), 40);
        titlu.setHorizontalAlignment(JLabel.CENTER);
        f.add(titlu);

        mesaj = new JLabel("by Furtos Stelian");
        mesaj.setForeground(new Color(0, 0, 0, 0));
        mesaj.setFont(new Font("Times New Roman", Font.BOLD, 20));
        mesaj.setLocation(0, 300);
        mesaj.setSize(f.getWidth(), 40);
        mesaj.setHorizontalAlignment(JLabel.CENTER);
        f.add(mesaj);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //</editor-fold>
    }
   
    void start() {
        //<editor-fold desc="body" defaultstate="collapsed">
        f.setVisible(true);
        while (runAnim) {
            vizibilitate += viteza;
            if (vizibilitate > 1) {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException ex) {
                    Logger.getLogger(SplashScreen.class.getName()).log(Level.SEVERE, null, ex);
                }
                viteza=-Math.abs(viteza);
                vizibilitate=1;
            }else if (vizibilitate<0)
            {
                runAnim=false;
                vizibilitate=0;
            }
            label.repaint();
            titlu.setForeground(new Color(1f, 0f, 0f, vizibilitate));
            mesaj.setForeground(new Color(1f, 0f, 0f, vizibilitate));
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(SplashScreen.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        f.dispose();
        //</editor-fold>
    }

}
