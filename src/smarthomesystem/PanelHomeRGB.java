/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smarthomesystem;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import static smarthomesystem.SmartHomeSystem.getSmartHomeSystem;
import smarthomesystem.SmartHomeSystem.RGB;
/**
 *
 * @author Manel
 */
public class PanelHomeRGB extends SHSPanel {

    /**
     * Creates new form PanelHomeRGB
     */
    public PanelHomeRGB() {
        //<editor-fold desc="body" defaultstate="collapsed">
        initComponents();
        wheelPanel.setBrightnessSlider(brightnessPanel);
        roundButton1.setIcon(SmartHomeSystem.resize("icon_aprindeLumina.png", 55, 55));
        roundButton2.setIcon(SmartHomeSystem.resize("icon_stingeLumina.png", 55, 55));
       
        wheelPanel.setMainFormListener(new MainFormActionListener() {
            @Override
            public void onColorMove(Color c) {
                getSmartHomeSystem().send(RGB.COMMAND_SET_COLOR, c);
            }
        });

        brightnessPanel.slider.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                getSmartHomeSystem().send(RGB.COMMAND_SET_COLOR, wheelPanel.getColor());
            }
        });

        wheelPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                getSmartHomeSystem().send(RGB.COMMAND_SET_COLOR, wheelPanel.getColor());
            }
        });
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

        wheelPanel = new smarthomesystem.ColorWheelPanel();
        jLabel1 = new javax.swing.JLabel();
        brightnessPanel = new smarthomesystem.ColorSliderPanel();
        roundButton1 = new smarthomesystem.RoundButton();
        roundButton2 = new smarthomesystem.RoundButton();

        javax.swing.GroupLayout wheelPanelLayout = new javax.swing.GroupLayout(wheelPanel);
        wheelPanel.setLayout(wheelPanelLayout);
        wheelPanelLayout.setHorizontalGroup(
            wheelPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 350, Short.MAX_VALUE)
        );
        wheelPanelLayout.setVerticalGroup(
            wheelPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 350, Short.MAX_VALUE)
        );

        jLabel1.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("RGB-Strip");

        roundButton1.setText("roundButton2");
        roundButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                roundButton1ActionPerformed(evt);
            }
        });

        roundButton2.setText("roundButton2");
        roundButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                roundButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addComponent(roundButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(brightnessPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(wheelPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(roundButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 6, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(wheelPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(roundButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(roundButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(brightnessPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void roundButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_roundButton2ActionPerformed
        getSmartHomeSystem().send("L0");
    }//GEN-LAST:event_roundButton2ActionPerformed

    private void roundButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_roundButton1ActionPerformed
        getSmartHomeSystem().send("L1");
    }//GEN-LAST:event_roundButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public smarthomesystem.ColorSliderPanel brightnessPanel;
    private javax.swing.JLabel jLabel1;
    private smarthomesystem.RoundButton roundButton1;
    private smarthomesystem.RoundButton roundButton2;
    public smarthomesystem.ColorWheelPanel wheelPanel;
    // End of variables declaration//GEN-END:variables

}
