/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smarthomesystem;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import javax.swing.ImageIcon;

/**
 *
 * @author Manel
 */
public class LEDAnimationButton {

    private Dimension size = new Dimension(150, 150);
    private ImageIcon icon;
    private ImageIcon pressedIcon;
    private ImageIcon currentIcon;
    private Point location;
    private ImageIcon initialIcon;
    public LEDAnimationButtonEventListener listener;
    public static int BOUNDARY_SPACING = 3;
    public boolean isHovered = false;
    public boolean isPressed = false;


    public LEDAnimationButton(ImageIcon icon) {
        this.initialIcon = icon;
        setIcon(icon);
    }

    public LEDAnimationButton(String name) {
        this.initialIcon = new ImageIcon(getClass().getResource("/resurse/" + name + ".png"));
        setIcon(initialIcon);
    }

    public void setListener(LEDAnimationButtonEventListener listener) {
        this.listener = listener;
    }

    public final void setLocation(Point location) {
        this.location = location;
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return mouseX >= getX() + BOUNDARY_SPACING && mouseX <= getX() + getWidth() - BOUNDARY_SPACING
                && mouseY >= getY() + BOUNDARY_SPACING && mouseY <= getY() + getHeight() - BOUNDARY_SPACING;
    }

    public final void setIcon(ImageIcon icon) {
        this.icon = icon;
        pressedIcon = new ImageIcon(
                icon.getImage().getScaledInstance((int) (getWidth() * 0.8),
                        (int) (getHeight() * 0.8),
                        Image.SCALE_SMOOTH));
        currentIcon = icon;
    }

    public final void setIcon() {
        setIcon(new ImageIcon(initialIcon.getImage().getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH)));
    }

    public int getWidth() {
        return (int) size.getWidth();
    }

    public int getHeight() {
        return (int) size.getHeight();
    }

    public int getX() {
        return (int) location.getX();
    }

    public int getY() {
        return (int) location.getY();
    }

    public void setSize(int size) {
        this.size = new Dimension(size, size);
        setIcon();
    }

    public void setCurrentIcon(ImageIcon icon) {
        currentIcon = icon;
    }

    public ImageIcon getPressedIcon() {
        return pressedIcon;
    }

    public ImageIcon getInitialIcon() {
        return initialIcon;
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        if (currentIcon != null) {
            if (currentIcon == pressedIcon) {
                g2.drawImage(currentIcon.getImage(), getX() + (int) (getWidth() - getWidth() * 0.8) / 2, getY() + (int) (getHeight() - getHeight() * 0.8) / 2, null);
            } else {
                g2.drawImage(currentIcon.getImage(), getX(), getY(), null);
            }
        } else {
            g2.setColor(Color.RED);
            g2.fillRect(0, 0, getWidth(), getHeight());
        }

    }
}
