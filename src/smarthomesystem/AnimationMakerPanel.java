/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smarthomesystem;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author Manel
 */
public class AnimationMakerPanel extends JPanel {

    List<LED> ledStrip;
    static double UPSCALE_INCREMENT = 0.01;
    static double DOWNSCALE_DECREMENT = 0.01;
    static Color BACKGROUND_COLOR = new Color(51, 51, 51);
    Point lastMouseCoordinates;
    static final int BUTTON_SIZE = 150;
    Frames frames;
    List<FrameHolder> frameHolders;

    List<LEDAnimationButton> butoaneInterfata;

    LEDAnimationButton leftButton;
    LEDAnimationButton playButton;
    LEDAnimationButton rightButton;

    public final Point getLeftButtonLocation() {
        return new Point(
                getWidth() / 2 - leftButton.getWidth() - leftButton.getWidth() / 2,
                getHeight() - leftButton.getHeight()
        );
    }

    public final Point getPlayButtonLocation() {
        return new Point(
                getWidth() / 2 - playButton.getWidth() / 2,
                getHeight() - playButton.getHeight()
        );
    }

    public final Point getRightButtonLocation() {
        return new Point(
                getWidth() / 2 + playButton.getWidth() / 2,
                getHeight() - rightButton.getHeight()
        );
    }

    public AnimationMakerPanel() {
        super();
        ledStrip = new ArrayList<>();
        frames = new Frames();
        frameHolders = new ArrayList<>();
    }

    public boolean isValidDoubleClickToResize() {
        return !leftButton.isHovered && !playButton.isHovered && !rightButton.isHovered;
    }

    public int getInterfaceButtonSize() {
        return (int) (((getWidth() + getHeight()) / 2) * 0.1);
    }

    public synchronized void addFrame(Frame f) {
        frames.add(f);
        FrameHolder frameHolder = new FrameHolder(f);

        if (frameHolders.size() > 0) {
            if (frameHolders.get(frameHolders.size() - 1).isSelected()) {
                setSelectedHolder(frameHolder);
            }
        } else {
            frameHolder.setBackgroundColorToSelectedBackgroundColor();
        }
        frameHolders.add(frameHolder);
        setFrameHoldersLocation();

    }

    public void ledPressed(LED led) {
        int index = getSelectedFrameHolderIndex();
        if (index != -1) {
            frames.get(index).updateColors();
        }
    }

    public synchronized void setSelectedHolder(FrameHolder frameHolder) {

        for (FrameHolder holder : frameHolders) {
            holder.setBackgroundColorToNormalBackgroundColor();
        }
        frameHolder.setBackgroundColorToSelectedBackgroundColor();

        int index = getSelectedFrameHolderIndex();
        if (index != -1) {
            ledStrip = frames.get(index).getStrip();
        }
    }

    void setFrameHoldersLocation() {
        for (int i = 0; i < frameHolders.size(); i++) {
            frameHolders.get(i).setLocation(i * frameHolders.get(i).getWidth() + frameHolders.get(0).getX(), getHeight() - playButton.getHeight() - 80);
        }
    }

    void initInterface() {
        butoaneInterfata = new ArrayList<>();
        leftButton = new LEDAnimationButton("leftArrow");
        leftButton.setListener(new LEDAnimationButtonEventListener() {
            @Override
            public void mouseMoving(MouseEvent evt) {
                //System.out.println(".mouseMoving()");
            }

            @Override
            public void mouseEntered(MouseEvent evt) {
                //System.out.println(".mouseEntered() leftButton");
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                // System.out.println(".mouseExited() leftButton");
            }

            @Override
            public void mousePressed(MouseEvent evt) {
                if (isLeftClick(evt)) {
                    leftButton.setCurrentIcon(leftButton.getPressedIcon());
                    repaint();
                }

                // System.out.println(".mousePressed() leftButton");
            }

            @Override
            public void mouseReleased(MouseEvent evt) {
                if (isLeftClick(evt)) {
                    leftButton.setIcon();
                    repaint();
                }
                // System.out.println(".mouseReleased() leftButton");
            }

            @Override
            public void mouseClicked(MouseEvent evt) {
                if (isLeftClick(evt)) {
                    int index = getSelectedFrameHolderIndex();
                    if (index - 1 >= 0) {
                        setSelectedHolder(frameHolders.get(index - 1));
                    }
                }
// System.out.println(".mouseClicked() leftButton");
            }
        });

        playButton = new LEDAnimationButton("playButton");
        playButton.setListener(new LEDAnimationButtonEventListener() {
            @Override
            public void mouseMoving(MouseEvent evt) {
                //System.out.println(".mouseMoving() playButton");
            }

            @Override
            public void mouseEntered(MouseEvent evt) {
                // System.out.println(".mouseEntered() playButton");
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                //System.out.println(".mouseExited() playButton");
            }

            @Override
            public void mousePressed(MouseEvent evt) {
                if (isLeftClick(evt)) {
                    playButton.setCurrentIcon(playButton.getPressedIcon());
                    repaint();
                }
// System.out.println(".mousePressed() playButton");
            }

            @Override
            public void mouseReleased(MouseEvent evt) {
                if (isLeftClick(evt)) {
                    playButton.setIcon();
                    repaint();
                }
                //  System.out.println(".mouseReleased() playButton");
            }

            @Override
            public void mouseClicked(MouseEvent evt) {
                // System.out.println(".mouseClicked() playButton");
                if (isLeftClick(evt)) {
                    try {
                        frames.play();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(AnimationMakerPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });

        rightButton = new LEDAnimationButton("rightArrow");
        rightButton.setListener(new LEDAnimationButtonEventListener() {
            @Override
            public void mouseMoving(MouseEvent evt) {
                //System.out.println(".mouseMoving() rightButton");
            }

            @Override
            public void mouseEntered(MouseEvent evt) {

//System.out.println(".mouseEntered() rightButton");
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                // System.out.println(".mouseExited() rightButton");
            }

            @Override
            public void mousePressed(MouseEvent evt) {
                if (isLeftClick(evt)) {
                    rightButton.setCurrentIcon(rightButton.getPressedIcon());
                    repaint();
                }

                //System.out.println(".mousePressed() rightButton");
            }

            @Override
            public void mouseReleased(MouseEvent evt) {
                if (isLeftClick(evt)) {
                    rightButton.setIcon();
                    repaint();
                }
// System.out.println(".mouseReleased() rightButton");
            }

            @Override
            public void mouseClicked(MouseEvent evt) {
                if (isLeftClick(evt)) {
                    new Thread() {
                        @Override
                        public void run() {
                            if (frameHolders.size() > 0) {
                                if (frameHolders.get(frameHolders.size() - 1).isSelected()) {
                                    synchronized (AnimationMakerPanel.this) {
                                        addFrame(new Frame(ledStrip, frames.size()));
                                    }
                                } else {
                                    int index = getSelectedFrameHolderIndex();
                                    if (index != -1) {
                                        if (index + 1 < frameHolders.size()) {
                                            setSelectedHolder(frameHolders.get(index + 1));
                                        }
                                    } else {
                                        App.print("Eroare la obtinerea indexului pentru FrameHolder selectat", AnimationMakerPanel.class);
                                    }
                                }
                            } else {
                                synchronized (AnimationMakerPanel.this) {
                                    addFrame(new Frame(ledStrip, frames.size()));
                                }
                            }
                            repaint();
                        }
                    }.start();
                }
            }
        });

        butoaneInterfata.add(leftButton);
        butoaneInterfata.add(playButton);
        butoaneInterfata.add(rightButton);
        windowResized();

    }
    int NUM_LEDS = 0;

    public boolean isLeftClick(MouseEvent e) {
        if (e == null) {
            return true;
        }
        return SwingUtilities.isLeftMouseButton(e);
    }

    public int getSelectedFrameHolderIndex() {
        for (int i = 0; i < frameHolders.size(); i++) {
            if (frameHolders.get(i).isSelected()) {
                return i;
            }
        }
        return -1;
    }

    public void initialiseLEDStrip(int numLeds) {
        NUM_LEDS = numLeds;
        ledStrip.clear();
        for (int i = 0; i < numLeds; i++) {
            ledStrip.add(new LED(i, new java.awt.Point(i * LED.INITIAL_WIDTH, 50)));
        }
    }

    public void repositionLEDsForDrawModeBIG() {
        for (int i = 0; i < ledStrip.size(); i++) {
            ledStrip.get(i).setBigLocation(new Point(i * LED.BIG_LED_SIZE, ledStrip.get(i).getY()));
        }
    }

    public void setLedDrawMode(LED.DrawMode mode) {
        if (mode == LED.DrawMode.BIG) {
            LED.BIG_LED_SIZE = getWidth() / NUM_LEDS;
            repositionLEDsForDrawModeBIG();
        }
        LED.DRAW_MODE = mode;
    }

    boolean isVisible(LED led) {
        return led.getX() >= 0 && led.getX() <= getWidth();
    }

    private void resizeLEDStrip(double scale) {
        LED.setSize(scale);
        for (int i = 0; i < ledStrip.size(); i++) {
            ledStrip.get(i).setPosition((int) (i * LED.getWidth() + ledStrip.get(0).getInitialX()),
                    ledStrip.get(i).getInitialY());
        }
    }

    public void windowResized() {
        leftButton.setSize(getInterfaceButtonSize());
        leftButton.setLocation(getLeftButtonLocation());

        playButton.setSize(getInterfaceButtonSize());
        playButton.setLocation(getPlayButtonLocation());

        rightButton.setSize(getInterfaceButtonSize());
        rightButton.setLocation(getRightButtonLocation());
    }

    public void upscale() {
        double scale = LED.SCALE;
        scale += UPSCALE_INCREMENT;
        if (scale > 1) {
            scale = 1d;
        }
        if (LED.DRAW_MODE == LED.DrawMode.BIG) {
            setLedDrawMode(LED.DrawMode.NORMAL);
        }
        resizeLEDStrip(scale);
        repaint();
    }

    public void downscale() {
        double scale = LED.SCALE;
        scale -= UPSCALE_INCREMENT;
        if (scale <= 0) {
            scale = 0d;
            if (LED.DRAW_MODE == LED.DrawMode.NORMAL) {
                setLedDrawMode(LED.DrawMode.BIG);
            }
        }
        resizeLEDStrip(scale);
        repaint();
    }

    public void moveStrip(int moveX, int moveY) {
        ledStrip.forEach((led) -> {
            led.setPosition(led.getX() + moveX, led.getY() + moveY);
            led.setInitialPosition(led.getX(), led.getY());
        });
        repaint();
    }

    public void frameHolderPressed(FrameHolder frameHolder) {
        for (FrameHolder holder : frameHolders) {
            holder.setBackgroundColorToNormalBackgroundColor();
        }
        frameHolders.get(frameHolder.index).setBackgroundColorToSelectedBackgroundColor();
        setSelectedHolder(frameHolder);
        repaint();
    }

    public void mouseMoved(MouseEvent e) {

        //<editor-fold desc="mouse pe leduri" defaultstate="collapsed">
        //<editor-fold desc="leduri normale" defaultstate="collapsed">
        if (LED.DRAW_MODE == LED.DrawMode.NORMAL) {
            for (int i = 0; i < ledStrip.size(); i++) {
                if (!ledStrip.get(i).isHoveredOverEnclosure(e.getX(), e.getY())) {
                    if (ledStrip.get(i).isEnclosureHovered) {
                        ledStrip.get(i).isEnclosureHovered = false;
                        ledStrip.get(i).onMouseExitedEnclosure();
                    }
                } else {

                    if (!ledStrip.get(i).isEnclosureHovered) {
                        ledStrip.get(i).isEnclosureHovered = true;
                        ledStrip.get(i).onMouseEnteredEnclosure();
                    }
                }
                if (!ledStrip.get(i).isHovered(e.getX(), e.getY())) {
                    if (ledStrip.get(i).isHovered) {
                        ledStrip.get(i).isHovered = false;
                        ledStrip.get(i).onMouseExitedLED();
                    }
                } else {
                    if (!ledStrip.get(i).isHovered) {
                        ledStrip.get(i).isHovered = true;
                        ledStrip.get(i).onMouseEnteredLED();
                    }
                }
            }
        } //</editor-fold>
        //<editor-fold desc="leduri big" defaultstate="collapsed">
        else if (LED.DRAW_MODE == LED.DrawMode.BIG) {
            for (int i = 0; i < ledStrip.size(); i++) {
                if (ledStrip.get(i).isBigHovered(e.getX(), e.getY())) {
                    if (!ledStrip.get(i).isBigHovered) {
                        ledStrip.get(i).isBigHovered = true;
                        ledStrip.get(i).onMouseEnteredLED();
                    }
                } else {
                    if (ledStrip.get(i).isBigHovered) {
                        ledStrip.get(i).isBigHovered = false;
                        ledStrip.get(i).onMouseExitedLED();
                    }
                }
            }
        }
        //</editor-fold>
        //</editor-fold>

        //<editor-fold desc="interfata" defaultstate="collapsed">
        for (LEDAnimationButton button : butoaneInterfata) {
            if (button.isHovered(e.getX(), e.getY())) {
                if (!button.isHovered) {
                    if (button.listener != null) {
                        button.isHovered = true;
                        button.listener.mouseEntered(e);
                    }

                } else {
                    if (button.listener != null) {
                        button.listener.mouseMoving(e);
                    }
                }
            } else {
                if (button.isHovered) {
                    if (button.listener != null) {
                        button.listener.mouseExited(e);
                    }
                    button.isHovered = false;
                }
            }
        }
        //</editor-fold>

        //<editor-fold desc="pentru frameholdere" defaultstate="collapsed">
        for (FrameHolder holder : frameHolders) {
            if (holder.isHovered(e.getX(), e.getY())) {
                if (!holder.isHovered) {
                    holder.isHovered = true;
                    holder.onMouseEnter(e);
                } else {
                    holder.onMouseHover(e);
                }
            } else {
                if (holder.isHovered) {
                    holder.isHovered = false;
                    holder.onMouseExit(e);
                }
            }
        }
        //</editor-fold>
    }

    public void mouseDragged(MouseEvent e) {

    }

    public void moveFrameHolders(int moveX, int moveY) {
        frameHolders.forEach((frameHolder) -> {
            frameHolder.setLocation(frameHolder.getX() + moveX, FrameHolder.Y);
        });
        repaint();
    }

    public void moveFrameHolders(MouseEvent e) {
        int dirX;
        dirX = (int) (e.getX() - lastMouseCoordinates.getX());
        int dirY = (int) (e.getY() - lastMouseCoordinates.getY());
        lastMouseCoordinates = e.getPoint();
        moveFrameHolders(dirX, dirY);
    }

    public void moveStrip(MouseEvent e) {
        int dirX;
        dirX = (int) (e.getX() - lastMouseCoordinates.getX());
        int dirY = (int) (e.getY() - lastMouseCoordinates.getY());
        lastMouseCoordinates = e.getPoint();
        moveStrip(dirX, dirY);
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (rightButton.listener != null) {
                rightButton.listener.mousePressed(null);
            } else {
                App.print("Degeaba ai apasat pt ca listeneru rightButton e null", AnimationMakerPanel.class);
            }
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (leftButton.listener != null) {
                leftButton.listener.mousePressed(null);
            } else {
                App.print("Degeaba ai apasat pt ca listeneru leftButton e null", AnimationMakerPanel.class);
            }
        }
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (rightButton.listener != null) {
                rightButton.listener.mouseReleased(null);
                rightButton.listener.mouseClicked(null);
            } else {
                App.print("Degeaba ai apasat pt ca listeneru rightButton e null", AnimationMakerPanel.class);
            }
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (leftButton.listener != null) {
                leftButton.listener.mouseReleased(null);
                leftButton.listener.mouseClicked(null);
            } else {
                App.print("Degeaba ai apasat pt ca listeneru leftButton e null", AnimationMakerPanel.class);
            }
        }
    }

    public void mousePressed(MouseEvent e) {

        for (LED led : ledStrip) {
            if (led.isHovered) {
                led.onPressed(e);
                repaint();
            }
            if (led.isBigHovered && LED.DRAW_MODE == LED.DrawMode.BIG) {
                led.onPressed(e);
                repaint();
            }
        }

        lastMouseCoordinates = e.getPoint();
        for (LEDAnimationButton button : butoaneInterfata) {
            if (button.isHovered(e.getX(), e.getY())) {
                if (button.listener != null) {
                    AnimationMakerForm.CAN_MOVE_WINDOW = false;
                    button.listener.mousePressed(e);
                }
                button.isPressed = true;
            }
        }

        for (FrameHolder holder : frameHolders) {
            if (holder.isHovered(e.getX(), e.getY())) {
                holder.onPress(e);
            }
        }

    }

    public void mouseReleased(MouseEvent e) {
        for (LEDAnimationButton button : butoaneInterfata) {
            if (button.isPressed) {
                if (button.listener != null) {
                    AnimationMakerForm.CAN_MOVE_WINDOW = true;
                    button.listener.mouseReleased(e);
                }
                if (button.isHovered(e.getX(), e.getY())) {
                    if (button.listener != null) {
                        button.listener.mouseClicked(e);
                    }
                }
                button.isPressed = false;
            }
        }
    }

    public boolean noLedIsHovered() {
        for (LED led : ledStrip) {
            if (led.isBigHovered || led.isHovered) {
                return false;
            }
        }
        return true;
    }

    public void drawInterface(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        leftButton.paintComponent(g);
        playButton.paintComponent(g);
        rightButton.paintComponent(g);

        String frameCounterDisplay = Integer.toString(frames.size());
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Times New Roman", Font.BOLD, 16));
        int stringWidth = g2.getFontMetrics().stringWidth(frameCounterDisplay);

        g2.drawString(frameCounterDisplay, getWidth() / 2 - stringWidth / 2, getHeight() - playButton.getHeight() - 20);

    }

    public void drawFrameHolders(Graphics g) {
        FrameHolder.Y = playButton.getY() - 80;
        for (FrameHolder frameHolder : frameHolders) {
            frameHolder.paintComponent(g);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(BACKGROUND_COLOR);
        g.fillRect(0, 0, getWidth(), getHeight());

        if (ledStrip != null) { //pot sa scot asta, e pt ca netbeans da eroare la niste chestii
            for (LED led : ledStrip) {
                led.paintComponent(g);
            }
            drawInterface(g);
            drawFrameHolders(g);
        }

    }
}
