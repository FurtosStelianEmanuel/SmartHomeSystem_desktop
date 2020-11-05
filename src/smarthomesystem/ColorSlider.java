/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smarthomesystem;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import static smarthomesystem.App.map;

/**
 *
 * @author Manel
 */
public class ColorSlider extends JSlider {

    //<editor-fold desc="Variabile" defaultstate="collapsed">
    static Dimension POINTER_SIZE = new Dimension(25, 25);
    Color background = new Color(127, 127, 127, 255);
    static int innerSpacing = 5;
    static BasicStroke STROKE = new BasicStroke(1.5f);
    ColorWheelPanel wheel;
    InterfaceUpdater listener;
    static final int TYPE_RED_SLIDER = 0;
    static final int TYPE_GREEN_SLIDER = 1;
    static final int TYPE_BLUE_SLIDER = 2;
    static final int TYPE_BRIGHTNESS_SLIDER = 3;
    static final int TYPE_GENERAL_SLIDER = 4;
    int type = TYPE_BRIGHTNESS_SLIDER;
    boolean triggerEvent = true;
    Color generalColor = Color.WHITE;
    int minimumValue = 0;
    int maximumValue = 255;
    //</editor-fold>

    public ColorSlider() {
        //<editor-fold desc="Constructor body" defaultstate="collapsed">
        setFocusable(false);
        removeMouseListener(getMouseListeners()[0]);
        setValue(getMaximum());
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (isEnabled()) {
                    Point p = e.getPoint();
                    double percent = p.x / ((double) getWidth());
                    int range = getMaximum() - getMinimum();
                    double newVal = range * percent;
                    int result = (int) (getMinimum() + newVal);
                    if (result >= minimumValue && result <= maximumValue) {
                        setValue(result);
                    } else {
                        if (result < minimumValue) {
                            setValue(minimumValue);
                        } else if (result > maximumValue) {
                            setValue(maximumValue);
                        }
                    }
                }
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (isEnabled()) {
                    int val = (int) map(e.getX(), 0, getWidth(), 0, getMaximum());
                    if (val >= minimumValue && val <= maximumValue) {
                        setValue(val);
                    }
                }
            }
        });
        addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent ce) {
                if (listener != null) {
                    try {
                        listener.notifyChanges(getValue());
                    } catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                        Logger.getLogger(ColorSlider.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        //</editor-fold>
    }

    public void setListener(InterfaceUpdater listener) {
        this.listener = listener;
    }

    public void setGeneralColor(Color generalColor) {
        this.generalColor = generalColor;
        repaint();
    }

    /**
     * {@link #minimumValue} si {@link #maximumValue} sunt folosite pentru a
     * seta limite valorilor slider-ului fara sa modifice vizual sliderul,
     * atunci cand utilizatorul vrea o valoarea in afara limitelor, thumb-ul
     * slider-ului va ramane in valoarea limita, daca foloseam {@link #setMaximum(int)
     * } sau {@link #setMinimum(int) } vedeam ca thumb-ul isi schimba pozitia in
     * functie de limite, ceea ce nu arata foarte bine si e putin dezorientant
     *
     * @param min
     */
    public void setSoftMinimum(int min) {
        this.minimumValue = min;
    }

    public void setSoftMaximum(int max) {
        this.maximumValue = max;
    }

    public void setType(int type) {
        //<editor-fold desc="body" defaultstate="collapsed">
        this.type = type;
        if (type == TYPE_BRIGHTNESS_SLIDER || type == TYPE_GENERAL_SLIDER) {
            setMaximum(100);
        } else {
            setMaximum(255);
        }
        //</editor-fold>
    }

    @Override
    public void paintComponent(Graphics g) {
        //<editor-fold desc="body" defaultstate="collapsed">
        //super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.setStroke(new BasicStroke(5f));
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        int pointerLocation = map(getValue(), getMinimum(), getMaximum(), 0, getWidth());
        if (wheel != null) {
            switch (type) {
                case TYPE_BRIGHTNESS_SLIDER:
                    Color c = wheel.getWheelColor();
                    if (c != null) {
                        for (int i = 0; i < getWidth(); i++) {
                            Color culoareAleasa = c;
                            double procent = i / (double) (getWidth() - 1);
                            int red = (int) (culoareAleasa.getRed() * procent);
                            int green = (int) (culoareAleasa.getGreen() * procent);
                            int blue = (int) (culoareAleasa.getBlue() * procent);
                            g2.setColor(new Color(red, green, blue));
                            g2.drawLine(i, 0, i, getHeight());
                        }
                    }
                    break;
                case TYPE_RED_SLIDER: {

                    g2.setColor(Color.RED);
                    g2.fillRect(0, 0, pointerLocation, getHeight());
                    break;
                }
                case TYPE_GREEN_SLIDER: {
                    g2.setColor(Color.GREEN);
                    g2.fillRect(0, 0, pointerLocation, getHeight());
                    break;
                }
                case TYPE_BLUE_SLIDER: {
                    g2.setColor(Color.BLUE);
                    g2.fillRect(0, 0, pointerLocation, getHeight());
                    break;
                }

                default:
                    break;
            }
        } else {
            switch (type) {
                case TYPE_GENERAL_SLIDER:
                    if (generalColor != null) {
                        for (int i = 0; i < getWidth(); i++) {
                            Color culoareAleasa = generalColor;
                            double procent = i / (double) (getWidth() - 1);
                            int red = (int) (culoareAleasa.getRed() * procent);
                            int green = (int) (culoareAleasa.getGreen() * procent);
                            int blue = (int) (culoareAleasa.getBlue() * procent);
                            g2.setColor(new Color(red, green, blue));
                            g2.drawLine(i, 0, i, getHeight());
                        }
                    }
                    break;
            }
        }
        int x = (int) map(getValue(), 0, getMaximum(), 0, getWidth());
        Area cercMare = new Area(new Ellipse2D.Double(x - POINTER_SIZE.getWidth() / 2, getHeight() / 2 - POINTER_SIZE.getHeight() / 2,
                POINTER_SIZE.getWidth(), POINTER_SIZE.getHeight()));
        Area cercMic = new Area(new Ellipse2D.Double(x - POINTER_SIZE.getWidth() / 2 + innerSpacing, getHeight() / 2 - POINTER_SIZE.getHeight() / 2 + innerSpacing,
                POINTER_SIZE.getWidth() - 2 * innerSpacing, POINTER_SIZE.getHeight() - 2 * innerSpacing));
        Area toroid = new Area(cercMare);
        toroid.subtract(cercMic);
        g2.setStroke(new BasicStroke());
        if (type == TYPE_BRIGHTNESS_SLIDER || type == TYPE_GENERAL_SLIDER) {
            g2.setColor(Color.WHITE);
            g2.fill(toroid);

            g2.setStroke(STROKE);
            g2.setColor(Color.BLACK);
            g2.draw(cercMare);
            g2.draw(cercMic);
        } else {
            switch (type) {
                case TYPE_RED_SLIDER:
                    g2.setColor(Color.RED);
                    break;
                case TYPE_GREEN_SLIDER:
                    g2.setColor(Color.GREEN);
                    break;
                case TYPE_BLUE_SLIDER:
                    g2.setColor(Color.BLUE);
                    break;
                default:
                    break;
            }

            g2.fill(cercMare);
            g2.setStroke(STROKE);
            g2.setColor(Color.BLACK);
            g2.draw(cercMare);

            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Times New Roman", Font.PLAIN, 12));
            String text = getValue() + "";
            while (text.length() < 3) {
                text = " " + text;
            }
            g2.translate(getWidth() / 2 - 1, getHeight() / 2 + 5);
            g2.drawString(text, 0, 0);

        }
        //</editor-fold>
    }

}
