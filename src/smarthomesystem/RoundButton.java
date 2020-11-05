package smarthomesystem;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import static smarthomesystem.MainForm.BACKGROUND_COLOR;


public class RoundButton extends JButton
{
    private ImageIcon selected_icon;
    private ImageIcon small_icon;
    private ImageIcon normal_icon;
    private final int small_iconSize=5;

    public RoundButton() {
        super();
        addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent e) {
                if (small_icon != null) {
                    selected_icon=small_icon;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                selected_icon=normal_icon;
            }
        });

    }

    /**
     * 
     *
     * @param icon
     */
    public void setIcon(ImageIcon icon) {
        this.selected_icon = icon;
        normal_icon = icon;
        small_icon = new ImageIcon(
                icon.getImage().getScaledInstance(
                        icon.getIconWidth() - small_iconSize,
                        icon.getIconHeight() - small_iconSize,
                        Image.SCALE_SMOOTH));

    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        g2.setColor(BACKGROUND_COLOR);
        g2.fillRect(0, 0, getWidth(), getHeight());

        /*
        if (getModel().isArmed()) {
            g2.setColor(Color.lightGray);
        } else {
            g2.setColor(getBackground());
        }
        g2.fillOval(0, 0, getSize().width - 1, getSize().height - 1);
        */
        if (selected_icon != null) {
            g2.drawImage(selected_icon.getImage(), getWidth() / 2 - selected_icon.getIconWidth() / 2, getHeight() / 2 - selected_icon.getIconHeight() / 2, this);
        }
    }

    //Paint the border of the button using a simple stroke.
    @Override
    protected void paintBorder(Graphics g) {
        /*g.setColor(getForeground());
        g.drawOval(0, 0, getSize().width-1, getSize().height-1);*/
    }
}