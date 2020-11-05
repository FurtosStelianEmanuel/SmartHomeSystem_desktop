/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smarthomesystem;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Manel
 */
@Deprecated
public class ColorPicker extends JPanel {

    //<editor-fold desc="Variabile si clase ajutatoare" defaultstate="collapsed">
    Slider s;
    int inc = 0;
    double angle = Math.PI / 2;
    Color COLOR = Color.MAGENTA;
    int px = 0, py = 0;
    double rad = 400d;
    Color TRANSPARENT = new Color(0, 0, 0, 0);
    float intensitate = 1;
    int mouseX, mouseY;

    class Slider extends JSlider {

        Slider() {
            //<editor-fold desc="Constructor body" defaultstate="collapsed">
            super();
            removeMouseListener(getMouseListeners()[0]);
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    Point p = e.getPoint();
                    double percent = p.x / ((double) getWidth());
                    int range = getMaximum() - getMinimum();
                    double newVal = range * percent;
                    int result = (int) (getMinimum() + newVal);
                    setValue(result);
                }
            });
            addMouseMotionListener(new MouseMotionListener() {
                @Override
                public void mouseDragged(MouseEvent me) {
                    setValue((int) map(me.getX(), 0, getWidth(), 0, getMaximum()));
                }

                @Override
                public void mouseMoved(MouseEvent me) {

                }
            });
            addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent ce) {
                    
                }
            });
            //</editor-fold>
        }

        @Override
        public void paint(Graphics g) {
            //<editor-fold desc="body" defaultstate="collapsed">
            super.paint(g);
            g.setColor(COLOR);
            int x = (int) map(getValue(), 0, getMaximum(), 0, getWidth());
            g.fillOval(x - 25, 0, 50, 15);
            //</editor-fold>
        }

    }

    ComponentListener oar = new ComponentListener() {
        @Override
        public void componentResized(ComponentEvent ce) {
            rad = getSize().getWidth();
            s.setSize((int) (rad) - 16, 15);
            s.setLocation(8, (int) rad + 20);
            repaint();
        }

        @Override
        public void componentMoved(ComponentEvent ce) {
        }

        @Override
        public void componentShown(ComponentEvent ce) {
        }

        @Override
        public void componentHidden(ComponentEvent ce) {
        }
    };

    //</editor-fold>
    
    public static double map(double value, double istart, double istop, double ostart, double ostop) {
        return ostart + (ostop - ostart) * ((value - istart) / (istop - istart));
    }
    
    /**
     * Constructor apelat de JFrameFormEditor
     */
    public ColorPicker() {
        //<editor-fold desc="Constructor body" defaultstate="collapsed">
        s = new Slider();
        setLayout(null);
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                COLOR = getColor(e.getX(), e.getY());
                px = e.getX();
                py = e.getY();
                repaint();

            }
        });
        s.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent ce) {
                intensitate = s.getValue() / 100.0f;
                COLOR = getColor(px, py);
                repaint();
            }
        });
        addComponentListener(oar);
        add(s);
        //</editor-fold>
    }
    
        /**
     * Acest constructor este folosit cand nu ai acces la JFrameForm, cand ai acces la JFrameForm poti sa dai 
     * direct drag and drop la clasa {@link ColorPicker} pe JFrame-ul tau
     * Aceasta clasa extinde JPanel, asa ca daca vrei sa o adaugi fara JFrameForm editor o adaugi cu pe orice alt 
     * {@link JPanel} cu functia {@link JPanel#add(java.awt.Component) }
     * @param width
     * @param height 
     */
    @Deprecated
    public ColorPicker(int width, int height) {
        //<editor-fold desc="Constructor body" defaultstate="collapsed">
        int l = width;
        if (width != height) {
            App.print("Dimensiuni diferite, alegem cea mai mica", ColorPicker.class);
            l = width < height ? width : height;
        }
        rad = l;
        //</editor-fold>
    }
  
    
    /**
     * Functie folosita in principal ca sa afle culoarea la care se afla cursorul in respect cu x si y
     * ia in considerare si cand cursorul iese din interiorul colorWheel-ului, cursorul fiind 
     * repozitionat pe circumferinta ColorWheel-ului
     * @param x
     * @param y
     * @return {@link Color}
     */
    Color getColor(int x, int y) {
        //<editor-fold desc="body" defaultstate="collapsed">
        int centerX = (int) rad / 2;
        int centerY = (int) rad / 2;
        int radius = (int) (rad / 2) * (int) (rad / 2);

        // Red Source is (RIGHT, MIDDLE)
        int redX = (int) rad;
        int redY = (int) rad / 2;
        int redRad = (int) rad * (int) rad;

        // Green Source is (LEFT, MIDDLE)
        int greenX = 0;
        int greenY = (int) rad / 2;
        int greenRad = (int) rad * (int) rad;

        // Blue Source is (MIDDLE, BOTTOM)
        int blueX = (int) rad / 2;
        int blueY = (int) rad;
        int blueRad = (int) rad * (int) rad;

        int a = x - centerX;
        int b = y - centerY;

        int distance = a * a + b * b;

        if (distance < radius) {
            int rdx = x - redX;
            int rdy = y - redY;
            int redDist = (rdx * rdx + rdy * rdy);
            int redVal = (int) (255 - ((redDist / (float) redRad) * 256));
            int gdx = x - greenX;
            int gdy = y - greenY;
            int greenDist = (gdx * gdx + gdy * gdy);
            int greenVal = (int) (255 - ((greenDist / (float) greenRad) * 256));

            int bdx = x - blueX;
            int bdy = y - blueY;
            int blueDist = (bdx * bdx + bdy * bdy);
            int blueVal = (int) (255 - ((blueDist / (float) blueRad) * 256));

            Color c = new Color(redVal, greenVal, blueVal, 255);

            float hsbVals[] = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
            mouseX = x;
            mouseY = y;
            Color highlight = Color.getHSBColor(hsbVals[0], hsbVals[1], intensitate);
            return new Color(highlight.getRed(), highlight.getGreen(), highlight.getBlue(), 255);
        } else {
            //y=r*sin(q)
            //x=r*cos(q)
            double r = Math.sqrt(distance);
            double unghi = Math.atan2((double) y - centerY, (double) x - centerX);
            int px = x, py = y;
            x = (int) ((rad / 2) * Math.cos(unghi)) + centerX;
            y = (int) ((rad / 2) * Math.sin(unghi)) + centerY;
            mouseX = x;
            mouseY = y;
            double dist = (x - centerX) * (x - centerX) + (y - centerY) * (y - centerY);

            int rdx = x - redX;
            int rdy = y - redY;
            int redDist = (rdx * rdx + rdy * rdy);
            int redVal = (int) (255 - ((redDist / (float) redRad) * 256));
            int gdx = x - greenX;
            int gdy = y - greenY;
            int greenDist = (gdx * gdx + gdy * gdy);
            int greenVal = (int) (255 - ((greenDist / (float) greenRad) * 256));

            int bdx = x - blueX;
            int bdy = y - blueY;
            //System.out.println(rdx+" "+gdx+" "+bdx+" "+rdy+" "+gdy+" "+bdy);

            int blueDist = (bdx * bdx + bdy * bdy);
            int blueVal = (int) (255 - ((blueDist / (float) blueRad) * 256));
            //System.out.format("%d %d %d\n",redVal,greenVal,blueVal);
            Color c = new Color(Math.abs(redVal), Math.abs(greenVal), Math.abs(blueVal), 255);

            float hsbVals[] = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);

            Color highlight = Color.getHSBColor(hsbVals[0], hsbVals[1], intensitate);
            return new Color(highlight.getRed(), highlight.getGreen(), highlight.getBlue(), 255);
        }


        /*
        BufferedImage img = new BufferedImage((int)rad, (int)rad, BufferedImage.TYPE_INT_ARGB);

        // Center Point (MIDDLE, MIDDLE)
        int centerX = img.getWidth() / 2;
        int centerY = img.getHeight() / 2;
        int radius = (img.getWidth() / 2) * (img.getWidth() / 2);

        // Red Source is (RIGHT, MIDDLE)
        int redX = img.getWidth();
        int redY = img.getHeight() / 2;
        int redRad = img.getWidth() * img.getWidth();

        // Green Source is (LEFT, MIDDLE)
        int greenX = 0;
        int greenY = img.getHeight() / 2;
        int greenRad = img.getWidth() * img.getWidth();

        // Blue Source is (MIDDLE, BOTTOM)
        int blueX = img.getWidth() / 2;
        int blueY = img.getHeight();
        int blueRad = img.getWidth() * img.getWidth();

        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                int a = i - centerX;
                int b = j - centerY;

                int distance = a * a + b * b;
                if (distance < radius) {
                    int rdx = i - redX;
                    int rdy = j - redY;
                    int redDist = (rdx * rdx + rdy * rdy);
                    int redVal = (int) (255 - ((redDist / (float) redRad) * 256));

                    int gdx = i - greenX;
                    int gdy = j - greenY;
                    int greenDist = (gdx * gdx + gdy * gdy);
                    int greenVal = (int) (255 - ((greenDist / (float) greenRad) * 256));

                    int bdx = i - blueX;
                    int bdy = j - blueY;
                    int blueDist = (bdx * bdx + bdy * bdy);
                    int blueVal = (int) (255 - ((blueDist / (float) blueRad) * 256));

                    Color c = new Color(redVal, greenVal, blueVal,255);

                    float hsbVals[] = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);

                    Color highlight = Color.getHSBColor(hsbVals[0], hsbVals[1], intensitate);
                    
                    highlight=new Color(highlight.getRed(),highlight.getGreen(),highlight.getBlue(),255);
                    
                    img.setRGB(i, j, RGBtoHEX(highlight));
                } else {
                    img.setRGB(i, j, RGBtoHEX(TRANSPARENT));
                    
                }
            }
        }
        return img;
         */
        //</editor-fold>
    }
    
    public static int RGBtoHEX(Color color) {
        return color.getRGB();
    }

    /**
     * Functia ce returneaza ColorWheel-ul
     * @return {@link BufferedImage}
     */
    BufferedImage getPicker() {
        //<editor-fold desc="body" defaultstate="collapsed">
        BufferedImage img = new BufferedImage((int) rad, (int) rad, BufferedImage.TYPE_INT_ARGB);
        int radius = (img.getWidth() / 2) * (img.getWidth() / 2);
        int centerX = img.getWidth() / 2;
        int centerY = img.getHeight() / 2;
        
        int redX = img.getWidth();
        int redY = img.getHeight() / 2;
        int redRad = img.getWidth() * img.getWidth();

        int greenX = 0;
        int greenY = img.getHeight() / 2;
        int greenRad = img.getWidth() * img.getWidth();

        int blueX = img.getWidth() / 2;
        int blueY = img.getHeight();
        int blueRad = img.getWidth() * img.getWidth();

        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                int a = i - centerX;
                int b = j - centerY;

                int distance = a * a + b * b;
                if (distance < radius) {
                    int rdx = i - redX;
                    int rdy = j - redY;
                    int redDist = (rdx * rdx + rdy * rdy);
                    int redVal = (int) (255 - ((redDist / (float) redRad) * 256));

                    int gdx = i - greenX;
                    int gdy = j - greenY;
                    int greenDist = (gdx * gdx + gdy * gdy);
                    int greenVal = (int) (255 - ((greenDist / (float) greenRad) * 256));

                    int bdx = i - blueX;
                    int bdy = j - blueY;
                    int blueDist = (bdx * bdx + bdy * bdy);
                    int blueVal = (int) (255 - ((blueDist / (float) blueRad) * 256));

                    Color c = new Color(redVal, greenVal, blueVal,255);

                    float hsbVals[] = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);

                    Color highlight = Color.getHSBColor(hsbVals[0], hsbVals[1], intensitate);
                    
                    highlight=new Color(highlight.getRed(),highlight.getGreen(),highlight.getBlue(),255);
                    
                    img.setRGB(i, j, RGBtoHEX(highlight));
                } else {
                    img.setRGB(i, j, RGBtoHEX(TRANSPARENT));
                    
                }
            }
        }
        return img;
        //</editor-fold>
    }
    
    @Override
    public void paintComponent(Graphics g){
        //<editor-fold desc="body" defaultstate="collpased">
        super.paintComponent(g);
        Graphics2D g2=(Graphics2D)g;
        AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER);
        g2.setComposite(composite);
        g2.drawImage(getPicker(), 0, 0, this);
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(3f));
        g2.drawOval(mouseX-11, mouseY-11, 22, 22);
        g2.setColor(getColor(mouseX, mouseY));
        g2.fillOval(mouseX-11, mouseY-11, 22, 22);
        //</editor-fold >
    }

}
