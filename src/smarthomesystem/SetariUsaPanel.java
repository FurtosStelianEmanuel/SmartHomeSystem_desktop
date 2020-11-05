/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smarthomesystem;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JRadioButton;

/**
 *
 * @author Manel
 */
public class SetariUsaPanel extends SHSPanel {

    /**
     * Creates new form SetariUsaPanel
     */
    public SetariUsaPanel() {
        //<editor-fold desc="body" defaultstate="collapsed">
        initComponents();
        jLabel1.requestFocus();
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((int) d.getWidth() / 2 - getWidth() / 2,
                (int) d.getHeight() / 2 - getHeight() / 2);
        culoare.setContentAreaFilled(false);
        culoare.setOpaque(true);

        aprindereAutomata.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                saveChanges();
            }
        });
        oprireAutomata.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                saveChanges();
            }
        });
        aplicaSetari.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                saveChanges();
            }
        });
        Settings.addListener(SetariUsaPanel.class, "onUpdate", this);
        //</editor-fold>
    }

    void saveChanges() {
        //<editor-fold desc="body" defaultstate="collapsed">
        byte[] changes = new byte[]{'>',App.COMMAND_ANDROID_APPLY,
            App.DOOR_COMMAND,
            aprindereAutomata.isSelected() ? (byte) '1' : (byte) '0',
            oprireAutomata.isSelected() ? (byte) '1' : (byte) '0',
            aplicaSetari.isSelected() ? (byte) '1' : (byte) '0',
            (byte) culoare.getBackground().getRed(),
            (byte) culoare.getBackground().getGreen(),
            (byte) culoare.getBackground().getBlue()};

        SmartHomeSystem.getSmartHomeSystem().send(changes);

        new Thread() {
            @Override
            public void run() {

                if (aprindereAutomata.isSelected() != Settings.AUTOMATIC_LIGHT_ON_DOOR) {
                    aprindereAutomata.setBackground(java.awt.Color.RED);
//flash(aprindereAutomata);
                } else if (oprireAutomata.isSelected() != Settings.AUTOMATIC_LIGHT_OFF_DOOR) {
                    oprireAutomata.setBackground(java.awt.Color.RED);
//flash(oprireAutomata);
                } else if (aplicaSetari.isSelected() != Settings.AUTOMATIC_DOOR_APPLIER) {
                    aplicaSetari.setBackground(java.awt.Color.RED);
//flash(aplicaSetari);
                }

                Settings.AUTOMATIC_DOOR_APPLIER = aplicaSetari.isSelected();
                Settings.AUTOMATIC_LIGHT_OFF_DOOR = oprireAutomata.isSelected();
                Settings.AUTOMATIC_LIGHT_ON_COLOR_DOOR = culoare.getBackground();
                Settings.AUTOMATIC_LIGHT_ON_DOOR = aprindereAutomata.isSelected();
            }
        }.start();

        App.print("Am trimis comenzi " + changes, SetariUsaPanel.class);
        //</editor-fold>
    }

    public void receivedResponse() {
        //<editor-fold desc="body" defaultstate="collapsed">
        aprindereAutomata.setBackground(SetariUsaPanel.this.getBackground());
        oprireAutomata.setBackground(SetariUsaPanel.this.getBackground());
        aplicaSetari.setBackground(SetariUsaPanel.this.getBackground());
        //</editor-fold>
    }

    @Deprecated
    void flash(JRadioButton button) {
        new Thread() {
            @Override
            public void run() {
                button.setBackground(java.awt.Color.RED);
                try {
                    Thread.sleep(300);
                } catch (InterruptedException ex) {
                    Logger.getLogger(SetariUsaGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                button.setBackground(java.awt.Color.GREEN);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ex) {
                    Logger.getLogger(SetariUsaGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                button.setBackground(SetariUsaPanel.this.getBackground());
            }
        }.start();
    }

    public void onUpdate() {
        //<editor-fold desc="body" defaultstate="collapsed">
        aprindereAutomata.setSelected(Settings.AUTOMATIC_LIGHT_ON_DOOR);
        oprireAutomata.setSelected(Settings.AUTOMATIC_LIGHT_OFF_DOOR);
        aplicaSetari.setSelected(Settings.AUTOMATIC_DOOR_APPLIER);
        culoare.setBackground(Settings.AUTOMATIC_LIGHT_ON_COLOR_DOOR);
        App.print("onUpdate", SetariUsaPanel.class);
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

        aprindereAutomata = new javax.swing.JRadioButton();
        oprireAutomata = new javax.swing.JRadioButton();
        aplicaSetari = new javax.swing.JRadioButton();
        culoare = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        aprindereAutomata.setFont(new java.awt.Font("Dialog", 3, 14)); // NOI18N
        aprindereAutomata.setText("Aprindere automată la deschiderea ușii");
        aprindereAutomata.setAlignmentX(0.5F);
        aprindereAutomata.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        oprireAutomata.setFont(new java.awt.Font("Dialog", 3, 14)); // NOI18N
        oprireAutomata.setText("Oprire automată la închiderea ușii");
        oprireAutomata.setAlignmentX(0.5F);
        oprireAutomata.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        aplicaSetari.setFont(new java.awt.Font("Dialog", 3, 14)); // NOI18N
        aplicaSetari.setText("Aplică setările la conectarea cu telefonul");
        aplicaSetari.setAlignmentX(0.5F);
        aplicaSetari.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        culoare.setBackground(new java.awt.Color(255, 255, 255));
        culoare.setFont(new java.awt.Font("Dialog", 3, 14)); // NOI18N
        culoare.setText("Culoarea la deschiderea ușii");

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resurse/icon_usa.png"))); // NOI18N

        jLabel2.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Automatizare lumini-ușă");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(culoare, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(aplicaSetari, javax.swing.GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE)
                            .addComponent(oprireAutomata, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(aprindereAutomata, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(145, 145, 145)
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(7, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(aprindereAutomata)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(oprireAutomata)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(aplicaSetari)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(culoare)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JRadioButton aplicaSetari;
    public javax.swing.JRadioButton aprindereAutomata;
    public javax.swing.JButton culoare;
    public javax.swing.JLabel jLabel1;
    public javax.swing.JLabel jLabel2;
    public javax.swing.JRadioButton oprireAutomata;
    // End of variables declaration//GEN-END:variables
}
