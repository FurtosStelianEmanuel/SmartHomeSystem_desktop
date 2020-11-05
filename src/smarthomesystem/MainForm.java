/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smarthomesystem;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.plaf.basic.BasicArrowButton;

/**
 *
 * @author Manel
 */
public class MainForm extends javax.swing.JFrame{
    
    //<editor-fold desc="Variabile" defaultstate="collapsed">
    SHSPanel selectedPanel;
    static Color BACKGROUND_COLOR=Color.GRAY;
    List<SHSPanel>panels;    
    int selectedPanelIndex=0;
    //</editor-fold>
    
    
    public MainForm() {
        //<editor-fold desc="body" defaultstate="collapsed">
        initComponents();
        setLocationRelativeTo(null);
        jLabel1.setIcon(SmartHomeSystem.resize("icon_connected.png", jLabel1.getHeight(), jLabel1.getHeight()));
        BACKGROUND_COLOR = getContentPane().getBackground();
        panels = new ArrayList<>();
        initPanels();
        hideAllPanels();
        changePanel(panelHomeRGB1);
        //</editor-fold>
    }

    
    //<editor-fold desc="Functii utilitare" defaultstate="collapsed">
    public final void hideAllPanels() {
        for (SHSPanel panel : panels) {
            panel.setVisible(false);
        }
    }

    final void initPanels() {
        if (!panels.contains(panelHomeRGB1)) {
            panels.add(panelHomeRGB1);
        }
        if (!panels.contains(panelHomeARGB1)) {
            panels.add(panelHomeARGB1);
        }
        if (!panels.contains(panelSetariUsa)) {
            panels.add(panelSetariUsa);
        }
    }

    public final void changePanel(SHSPanel panel) {
        boolean error = false;
        try {
            if (selectedPanel != null) {
                selectedPanel.fadeOut();
            }
        } catch (IllegalStateException ex) {
            Logger.getLogger(MainForm.class.getSimpleName()).log(Level.SEVERE, null, ex);
            error = true;
        }
        try {
            panel.fadeIn();
        } catch (IllegalStateException ex) {
            Logger.getLogger(MainForm.class.getSimpleName()).log(Level.SEVERE, null, ex);
            error = true;
        }
        if (!error) {
            selectedPanel = panel;
            for (int i = 0; i < panels.size(); i++) {
                if (panels.get(i).equals(selectedPanel)) {
                    selectedPanelIndex = i;
                    break;
                }
            }
        } else {
            App.print("Eroare in changePanel", MainForm.class);
        }
    }
    //</editor-fold>
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelHomeRGB1 = new smarthomesystem.PanelHomeRGB();
        panelHomeARGB1 = new smarthomesystem.PanelHomeARGB();
        jLabel1 = new javax.swing.JLabel();
        nextMenuButton = new auxclasses.ArrowButton(BasicArrowButton.EAST);
        prevMenuButton = new auxclasses.ArrowButton(BasicArrowButton.WEST);
        panelSetariUsa = new smarthomesystem.SetariUsaPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        setSize(new java.awt.Dimension(560, 560));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        getContentPane().add(panelHomeRGB1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 90, 500, -1));
        getContentPane().add(panelHomeARGB1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 90, -1, -1));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 0, 410, 80));

        nextMenuButton.setText("arrowButton1");
        nextMenuButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextMenuButtonActionPerformed(evt);
            }
        });
        getContentPane().add(nextMenuButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 0, 40, 80));

        prevMenuButton.setText("arrowButton2");
        prevMenuButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prevMenuButtonActionPerformed(evt);
            }
        });
        getContentPane().add(prevMenuButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 40, 80));
        getContentPane().add(panelSetariUsa, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 90, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void nextMenuButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextMenuButtonActionPerformed
        //<editor-fold desc="body" defaultstate="collapsed">
        selectedPanelIndex++;
        selectedPanelIndex%=panels.size();
        changePanel(panels.get(selectedPanelIndex));
        //</editor-fold>
    }//GEN-LAST:event_nextMenuButtonActionPerformed

    private void prevMenuButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prevMenuButtonActionPerformed
        //<editor-fold desc="body" defaultstate="collapsed">
        selectedPanelIndex--;
        if (selectedPanelIndex<0){
            selectedPanelIndex=panels.size()-1;
        }
        changePanel(panels.get(selectedPanelIndex));
        //</editor-fold>
    }//GEN-LAST:event_prevMenuButtonActionPerformed
      
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private auxclasses.ArrowButton nextMenuButton;
    public smarthomesystem.PanelHomeARGB panelHomeARGB1;
    public smarthomesystem.PanelHomeRGB panelHomeRGB1;
    public smarthomesystem.SetariUsaPanel panelSetariUsa;
    private auxclasses.ArrowButton prevMenuButton;
    // End of variables declaration//GEN-END:variables


}
