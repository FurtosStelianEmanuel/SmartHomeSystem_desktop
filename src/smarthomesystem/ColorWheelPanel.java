/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smarthomesystem;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import static smarthomesystem.ColorSlider.POINTER_SIZE;
import static smarthomesystem.ColorSlider.STROKE;
import static smarthomesystem.ColorSlider.TYPE_BLUE_SLIDER;
import static smarthomesystem.ColorSlider.TYPE_GREEN_SLIDER;
import static smarthomesystem.ColorSlider.TYPE_RED_SLIDER;
import static smarthomesystem.ColorSlider.innerSpacing;

/**
 *
 * @author Manel
 */
public class ColorWheelPanel extends javax.swing.JPanel {

    //<editor-fold desc="Variabile" defaultstate="collapsed">
    public int WHEEL_DIAMETER = 350;
    public int mouseX = 0, mouseY = 0;
    BufferedImage wheelCanvas;
    private Color SELECTED_COLOR=Color.BLACK;
    //ColorListener listener;
    MainFormActionListener mainFormActionListener;
    ColorPickerActionListener colorPickerListener;
    ColorSliderPanel brightnessSliderPanel;
    ColorSliderPanel redSliderPanel;
    ColorSliderPanel greenSliderPanel;
    ColorSliderPanel blueSliderPanel;
    //</editor-fold>

    public Color getWheelColor(){
        return SELECTED_COLOR;
    }

    public void mouseEvent() {
        //<editor-fold desc="body" defaultstate="collapsed">
        int a = mouseX - getWidth() / 2;
        int b = mouseY - getHeight() / 2;
        if (Math.sqrt(a * a + b * b) > WHEEL_DIAMETER / 2) {
            double unghi = Math.atan2((double) mouseY - getHeight() / 2, (double) mouseX - getWidth() / 2);
            int x = (int) ((WHEEL_DIAMETER / 2) * Math.cos(unghi)) + (getWidth()) / 2;
            int y = (int) ((WHEEL_DIAMETER / 2) * Math.sin(unghi)) + (getHeight()) / 2;
            mouseX = x;
            mouseY = y;
        }
        repaint();
        SELECTED_COLOR=getRawColor();
        colorWheelEvent(SELECTED_COLOR);
        //</editor-fold>
    }

    public void setBrightnessSlider(ColorSliderPanel brightnessSliderPanel) {
        //<editor-fold desc="body" defaultstate="collapsed">
        this.brightnessSliderPanel = brightnessSliderPanel;
        this.brightnessSliderPanel.slider.wheel = this;
        this.brightnessSliderPanel.slider.setListener(new InterfaceUpdater(ColorWheelPanel.class, "sliderChangeEvent", this, Integer.class));
        //</editor-fold>
    }

    public void setRedSlider(ColorSliderPanel redSliderPanel) {
        //<editor-fold desc="body" defaultstate="collapsed">
        this.redSliderPanel = redSliderPanel;
        this.redSliderPanel.slider.wheel = this;
        this.redSliderPanel.slider.setType(TYPE_RED_SLIDER);
        this.redSliderPanel.slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent ce) {

            }
        });
        //</editor-fold>
    }

    public void setGreenSlider(ColorSliderPanel greenSliderPanel) {
        //<editor-fold desc="body" defaultstate="collapsed">
        this.greenSliderPanel = greenSliderPanel;
        this.greenSliderPanel.slider.wheel = this;
        this.greenSliderPanel.slider.setType(TYPE_GREEN_SLIDER);
        this.greenSliderPanel.slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent ce) {
            }
        });
        //</editor-fold>
    }

    public void setBlueSlider(ColorSliderPanel blueSliderPanel) {
        //<editor-fold desc="body" defaultstate="collapsed">
        this.blueSliderPanel=blueSliderPanel;
        this.blueSliderPanel.slider.wheel=this;
        this.blueSliderPanel.slider.setType(TYPE_BLUE_SLIDER);
        //</editor-fold>
    }
    
    /**
     * Functie apelata de {@link InterfaceUpdater}
     * @param value 
     */
    public void sliderChangeEvent(Integer value){
        //<editor-fold desc="body" defaultstate="collapsed">
        double procent=(double)value/(double)brightnessSliderPanel.slider.getMaximum();
        int red=(int) (SELECTED_COLOR.getRed()*procent);
        int green=(int) (SELECTED_COLOR.getGreen()*procent);
        int blue=(int) (SELECTED_COLOR.getBlue()*procent);
        colorChangeEvent(new Color(red,green,blue));
        //</editor-fold>
    }
    
    public void colorWheelEvent(Color selectedColor) {
        //<editor-fold desc="body" defaultstate="collapsed">
        Color color = selectedColor;
        if (brightnessSliderPanel != null) {
            brightnessSliderPanel.repaint();
            double procent = (double) brightnessSliderPanel.slider.getValue() / (double) brightnessSliderPanel.slider.getMaximum();
            int red = (int) (SELECTED_COLOR.getRed() * procent);
            int green = (int) (SELECTED_COLOR.getGreen() * procent);
            int blue = (int) (SELECTED_COLOR.getBlue() * procent);
            color = new Color(red, green, blue);
        }
        colorChangeEvent(color);
        //</editor-fold>
    }

    public void colorChangeEvent(Color c) {
        //<editor-fold desc="body" defaultstate="collapsed">
        if (redSliderPanel != null) {
            redSliderPanel.slider.setValue(c.getRed());
        }
        if (greenSliderPanel!=null){
            greenSliderPanel.slider.setValue(c.getGreen());
        }
        if (blueSliderPanel != null) {
            blueSliderPanel.slider.setValue(c.getBlue());
        }
        if (mainFormActionListener != null) {
            mainFormActionListener.onColorMove(c);
        }
        if (colorPickerListener !=null){
            colorPickerListener.onColorMove(c);
        }
        //</editor-fold>
    }

    public void setMainFormListener(MainFormActionListener listener) {
        this.mainFormActionListener = listener;
    }

    public void setColorPickerListener(ColorPickerActionListener listener){
        this.colorPickerListener=listener;
    }
    
    public ColorWheelPanel() {
        //<editor-fold desc="Constructor body" defaultstate="collapsed">
        initComponents();
        SELECTED_COLOR = Color.WHITE;
        mouseX = (int) (getPreferredSize().getWidth() / 2);
        mouseY = (int) (getPreferredSize().getHeight() / 2);
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
                mouseEvent();
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
                mouseEvent();
            }
        });  
        //</editor-fold>
    }

    private Color getRawColor(int x, int y) {
        //<editor-fold desc="body" defaultstate="collapsed">
        int i = x, j = y;
        if (i >= wheelCanvas.getWidth()) {
            i = wheelCanvas.getWidth() - 1;
        }
        if (j >= wheelCanvas.getHeight()) {
            j = wheelCanvas.getHeight() - 1;
        }
        try {
            Color c = new Color(wheelCanvas.getRGB(i, j));
            int r=c.getRed(),g=c.getGreen(),b=c.getBlue();
            if (r<3){
                r=0;
            }
            if (g<3){
                g=0;
            }
            if (b<3){
                b=0;
            }
            c=new Color(r,g,b);
            return c;
        } catch (ArrayIndexOutOfBoundsException ex) {

            App.print(i + " " + j + " maxima e " + wheelCanvas.getWidth() + " " + wheelCanvas.getHeight(), ColorWheelPanel.class);
            return Color.BLACK;
        }
//</editor-fold>
    }

    private Color getRawColor() {
        return getRawColor(mouseX, mouseY);
    }

    BufferedImage getPicker() {
        //<editor-fold desc="body" defaultstate="collapsed">
        BufferedImage image = new BufferedImage((int) WHEEL_DIAMETER, (int) WHEEL_DIAMETER, BufferedImage.TYPE_INT_ARGB);
        int[] row = new int[(int) WHEEL_DIAMETER];
        float size = (float) WHEEL_DIAMETER;
        float radius = size / 2f;

        for (int yidx = 0; yidx < (int) WHEEL_DIAMETER; yidx++) {
            float y = yidx - size / 2f;
            for (int xidx = 0; xidx < (int) WHEEL_DIAMETER; xidx++) {
                float x = xidx - size / 2f;
                double theta = Math.atan2(y, x) - 3d * Math.PI / 2d;
                if (theta < 0) {
                    theta += 2d * Math.PI;
                }
                double r = Math.sqrt(x * x + y * y);
                float hue = (float) (theta / (2d * Math.PI));
                float sat = Math.min((float) (r / radius), 1f);
                float bri = 1f;
                row[xidx] = Color.HSBtoRGB(hue, sat, bri);
            }
            image.getRaster().setDataElements(0, yidx, (int) WHEEL_DIAMETER, 1, row);
        }
        return image;
        //</editor-fold>
    }

    public Color getColor() {
        //<editor-fold desc="body" defaultstate="collapsed">
        double procent = 1d;
        if (brightnessSliderPanel != null) {
            procent = (double) brightnessSliderPanel.slider.getValue() / (double) brightnessSliderPanel.slider.getMaximum();
        }
        int red = (int) (SELECTED_COLOR.getRed() * procent);
        int green = (int) (SELECTED_COLOR.getGreen() * procent);
        int blue = (int) (SELECTED_COLOR.getBlue() * procent);
        return new Color(red, green, blue);
        //</editor-fold>
    }

    @Override
    public void paintComponent(Graphics g) {
        //<editor-fold desc="body" defaultstate="collapsed">
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        GraphicsConfiguration gc = g2.getDeviceConfiguration();
        wheelCanvas = gc.createCompatibleImage((int) WHEEL_DIAMETER, (int) WHEEL_DIAMETER, Transparency.TRANSLUCENT);
        Graphics2D g2d = wheelCanvas.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
        BufferedImage colorWheelImage = getPicker();
        g2d.setComposite(AlphaComposite.Src);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.fill(new Ellipse2D.Float(0f, 0f, (int) WHEEL_DIAMETER, (int) WHEEL_DIAMETER));

        //g2d.setComposite(AlphaComposite.SrcAtop); //daca bagi asta face el cercul dar apar niste mici erori in unele regiuni
        g2d.drawImage(colorWheelImage, 0, 0, null);
        g2d.dispose();

        g2.drawImage(wheelCanvas, null, (getWidth() - (int) WHEEL_DIAMETER) / 2, (getHeight() - (int) WHEEL_DIAMETER) / 2);

        Area cercMare = new Area(new Ellipse2D.Double(mouseX - POINTER_SIZE.getWidth() / 2, mouseY - POINTER_SIZE.getHeight() / 2,
                POINTER_SIZE.getWidth(), POINTER_SIZE.getHeight()));
        Area cercMic = new Area(new Ellipse2D.Double(mouseX - POINTER_SIZE.getWidth() / 2 + innerSpacing, mouseY - POINTER_SIZE.getHeight() / 2 + innerSpacing,
                POINTER_SIZE.getWidth() - 2 * innerSpacing, POINTER_SIZE.getHeight() - 2 * innerSpacing));
        Area toroid = cercMare;
        toroid.subtract(cercMic);

        g2.setColor(SELECTED_COLOR);
        g2.fill(cercMic);
        
        g2.setColor(Color.WHITE);
        g2.fill(toroid);

        g2.setStroke(STROKE);
        g2.setColor(Color.BLACK);
        g2.draw(cercMare);

        int cenX = wheelCanvas.getWidth() / 2, cenY = wheelCanvas.getHeight() / 2;

        for (int i = 0; i < getWidth(); i++) {
            for (int j = 0; j < getHeight(); j++) {
                int a = cenX - i, b = cenY - j;
                float dist = (float) Math.sqrt(a * a + b * b);
                g2.setColor(getBackground());
                if (dist > 175) {
                    g2.fillRect(i, j, 1, 1);
                }
            }
        }

        g2.dispose();
        //</editor-fold>
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setPreferredSize(new java.awt.Dimension(350, 350));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
