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
import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;

/**
 *
 * @author Manel
 */
public class FrameHolder implements FrameHolderListener {

    Dimension size = new Dimension(30, 30);
    int index;

    public static boolean CAN_MOVE = false;

    Color normalBackgroundColor = Color.BLACK;
    Color selectedBackgroundColor = Color.GREEN;
    Color backgroundColor = normalBackgroundColor;

    static AnimationMakerPanel animationMakerPanelReff;

    public boolean isHovered = false;

    public static int Y = 50;
    int initialX = 0;
    int initialY = Y;

    private int x;

    public FrameHolder(Frame f) {
        this.index = f.index;
        animationMakerPanelReff = SmartHomeSystem.getSmartHomeSystem().getAnimationMakerPanel();
    }

    int spacing = 1;

    public boolean isSelected() {
        return backgroundColor == selectedBackgroundColor;
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return (mouseX >= getX() + spacing && mouseX <= getX() + getWidth() - spacing
                && mouseY >= getY() && mouseY <= getY() + getHeight());
    }

    public void setBackgroundColorToSelectedBackgroundColor() {
        this.backgroundColor = selectedBackgroundColor;
    }

    public void setBackgroundColorToNormalBackgroundColor() {
        this.backgroundColor = normalBackgroundColor;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return FrameHolder.Y;
    }

    public int getWidth() {
        return (int) size.getWidth();
    }

    public int getHeight() {
        return (int) size.getHeight();
    }

    public void setLocation(int x, int y) {
        this.x = x;
        FrameHolder.Y = y;
    }

    public void setInitialLocation(int x, int y) {
        this.initialX = x;
        this.initialY = y;
    }

    Font indexFont = new Font("Times New Roman", Font.PLAIN, 13);
    BasicStroke borderStroke = new BasicStroke(0.5f);

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        g2.setColor(backgroundColor);
        g2.fillRect(getX(), getY(), (int) size.getWidth(), (int) size.getHeight());

        g2.setColor(Color.WHITE);
        g2.setFont(indexFont);
        g2.drawString(index + "", getX() + 1, Y + getHeight() / 2 + 3);

        g2.setStroke(borderStroke);
        g2.drawRect(getX(), getY(), (int) size.getWidth(), (int) size.getHeight());
    }

    @Override
    public void onPress(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            animationMakerPanelReff.frameHolderPressed(this);
        }
        //System.out.println("smarthomesystem.FrameHolder.onPress() " + index);
//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onMouseEnter(MouseEvent e) {
        FrameHolder.CAN_MOVE = true;
//System.out.println("smarthomesystem.FrameHolder.onMouseEnter() " + index);
//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onMouseHover(MouseEvent e) {
        //System.out.println("smarthomesystem.FrameHolder.onMouseHover() " + index);
// throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onMouseExit(MouseEvent e) {
        FrameHolder.CAN_MOVE = false;
//System.out.println("smarthomesystem.FrameHolder.onMouseExit() " + index);
//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
