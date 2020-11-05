/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smarthomesystem;

import auxclasses.ComponentResizer;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.Random;
import javax.swing.ImageIcon;
import static smarthomesystem.FrameHolder.animationMakerPanelReff;
import static smarthomesystem.SmartHomeSystem.getSmartHomeSystem;

/**
 *
 * @author Manel
 */
public class LED implements LEDStateChangeListener {

    static final int RAW_WIDTH = 458;
    static final int RAW_HEIGHT = 205;
    static final double INITIAL_SCALE = 0.2d;
    static final int SOLDER_SPACING = 3;
    public static Color HOVER_LED_COLOR = new Color(127, 127, 127);

    static final int INITIAL_WIDTH = (int) (RAW_WIDTH * INITIAL_SCALE);
    static final int INITIAL_HEIGHT = (int) (RAW_HEIGHT * INITIAL_SCALE);

    static ImageIcon ICON;
    static Dimension SIZE;

    public static double SCALE = INITIAL_SCALE;

    public static int RAW_DIAMETER = 84;
    public static int RAW_LED_X = 140;
    public static int RAW_LED_Y = 60;

    public static boolean CAN_MOVE = false;

    public boolean isHovered = false;
    public boolean isEnclosureHovered = false;

    public boolean isBigHovered = false;

    public enum DrawMode {
        NORMAL, BIG
    }

    public static DrawMode DRAW_MODE = DrawMode.NORMAL;
    public static int BIG_LED_SIZE = 15;

    int index;
    Color color = Color.GRAY;
    Point position;
    Point initialPosition;

    public Color getColor() {
        return color;
    }

    public void setInitialPosition(int x, int y) {
        initialPosition.setLocation(x, y);
    }

    public int getInitialX() {
        return (int) initialPosition.getX();
    }

    public int getInitialY() {
        return (int) initialPosition.getY();
    }

    public LED(int index, Point position) {
        this.index = index;
        this.position = position;
        this.initialPosition = new Point(position);
        setSize(INITIAL_SCALE);
        color = new Color(0, 0, 0);
    }

    public LED(LED led) {
        this.index = led.index;
        this.position = led.position;
        this.initialPosition = led.initialPosition;
        setSize(LED.SCALE);
        color = led.color;
        this.bigLocation = led.bigLocation;
        this.isBigHovered = led.isBigHovered;
        this.isEnclosureHovered = led.isEnclosureHovered;
        this.isHovered = led.isHovered;

    }

    public static final void setSize(double scale) {
        LED.SCALE = scale;
        if (scale < 0.01d) {
            scale = 0.01d;
        }
        LED.SIZE = new Dimension((int) (RAW_WIDTH * scale), (int) (RAW_HEIGHT * scale));
        LED.ICON = SmartHomeSystem.resize("led.png", (int) (SIZE.getWidth()), (int) (SIZE.getHeight()));
    }

    public int getX() {
        return (int) position.getX();
    }

    public int getY() {
        return (int) position.getY();
    }

    public static int getWidth() {
        return (int) SIZE.getWidth();
    }

    public static int getHeight() {
        return (int) SIZE.getHeight();
    }

    public void setPosition(int x, int y) {
        this.position.setLocation(x, y);
    }

    public Ellipse2D getLEDLocation() {
        return new Ellipse2D.Double((position.getX() + RAW_LED_X * SCALE), (position.getY() + RAW_LED_Y * SCALE),
                (RAW_DIAMETER * SCALE), (RAW_DIAMETER * SCALE));
    }

    public boolean isHovered(int mouseX, int mouseY) {
        Point ledCenter = new Point((int) (position.getX() + RAW_LED_X * SCALE + (RAW_DIAMETER * SCALE) / 2),
                (int) (position.getY() + RAW_LED_Y * SCALE + (RAW_DIAMETER * SCALE) / 2));
        double distance = Point2D.distance(ledCenter.getX(), ledCenter.getY(), mouseX, mouseY);
        return distance <= (RAW_DIAMETER * SCALE) / 2;
    }

    int spacing = 1;

    public boolean isBigHovered(int mouseX, int mouseY) {
        return (mouseX >= getBigX() && mouseX + spacing <= getBigX() + BIG_LED_SIZE - spacing
                && mouseY >= getBigY() + spacing && mouseY <= getBigY() + BIG_LED_SIZE * 2 - spacing);
    }

    public boolean isBigPressed(int mouseX, int mouseY) {
        return isBigHovered(mouseX, mouseY);
    }

    public boolean isHoveredOverEnclosure(int mouseX, int mouseY) {
        return (mouseX >= position.getX() + SOLDER_SPACING && mouseX <= position.getX() + RAW_WIDTH * SCALE - SOLDER_SPACING
                && mouseY >= position.getY() && mouseY <= position.getY() + RAW_HEIGHT * SCALE);
    }

    public Color getRandomColor() {
        Random r = new Random();
        return new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255));
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void onPressed(MouseEvent e) {
        AnimationMakerForm.CAN_MOVE_WINDOW = false;
        if (e.getButton() == MouseEvent.BUTTON1) {
            setColor(/*getRandomColor()*/Color.RED);
        } else if (e.getButton() == MouseEvent.BUTTON2) {
            setColor(Color.BLACK);
        }
        getSmartHomeSystem().getAnimationMakerPanel().ledPressed(this);
    }

    @Override
    public void onSelected() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onDeselected() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onMouseEnteredLED() {
        AnimationMakerForm ref = (AnimationMakerForm) getSmartHomeSystem().getGUI(AnimationMakerForm.class);
        ref.animationMakerPanel1.setCursor(new Cursor(Cursor.HAND_CURSOR));

    }

    @Override
    public void onMouseExitedLED() {
        AnimationMakerForm ref = (AnimationMakerForm) getSmartHomeSystem().getGUI(AnimationMakerForm.class);
        isEnclosureHovered = false;
        ref.animationMakerPanel1.setCursor(null);
    }

    @Override
    public void onMouseEnteredEnclosure() {
        System.out.println("entered " + index);
        AnimationMakerForm ref = (AnimationMakerForm) getSmartHomeSystem().getGUI(AnimationMakerForm.class);
        ref.animationMakerPanel1.setCursor(new Cursor(Cursor.MOVE_CURSOR));
        ref.resizing = false;
        LED.CAN_MOVE = true;

    }

    @Override
    public void onMouseExitedEnclosure() {
        System.out.println("exited " + index);
        AnimationMakerForm ref = (AnimationMakerForm) getSmartHomeSystem().getGUI(AnimationMakerForm.class);
        ref.animationMakerPanel1.setCursor(null);
        LED.CAN_MOVE = false;
    }

    Point bigLocation = new Point();

    public int getBigX() {
        return (int) bigLocation.getX();
    }

    public int getBigY() {
        return (int) bigLocation.getY();
    }

    public void setBigLocation(Point location) {
        bigLocation = location;
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        if (DRAW_MODE == DrawMode.NORMAL) {
            g2.drawImage(ICON.getImage(), getX(), getY(), null);
            g2.setColor(color);
            g2.fillOval((int) (getX() + RAW_LED_X * SCALE), (int) (getY() + RAW_LED_Y * SCALE),
                    (int) (RAW_DIAMETER * SCALE), (int) (RAW_DIAMETER * SCALE));
        } else if (DRAW_MODE == DrawMode.BIG) {
            getSmartHomeSystem().getAnimationMakerPanel().repositionLEDsForDrawModeBIG();
            g2.setColor(color);
            g2.fillRect(getBigX(), getBigY(), BIG_LED_SIZE, BIG_LED_SIZE * 2);
            g2.setColor(Color.WHITE);
            g2.drawRect(getBigX(), getBigY(), BIG_LED_SIZE, BIG_LED_SIZE * 2);
        }
    }

}
