/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smarthomesystem;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

/**
 *
 * @author Manel
 */
public class ColorPickerForm extends javax.swing.JFrame implements ColorPickerActionListener {

    //<editor-fold desc="Variabile" defaultstate="collapsed">
    Timer t;
    ColorPickerActionListener listener;
    //</editor-fold>

    public ColorPickerForm() {
        //<editor-fold desc="Constructor body" defaultstate="collapsed">
        setUndecorated(true);
        initComponents();
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(1f, 1f, 1f, 0f));
        setBackground(new Color(1f, 1f, 1f, 1f));
        colorPickerPanel1.setBackground(Color.WHITE);
        colorPickerPanel1.colorWheel.setBackground(colorPickerPanel1.getBackground());
        //</editor-fold>
    }

    public void setListener(ColorPickerActionListener listener) {
        this.listener = listener;
        colorPickerPanel1.colorWheel.setColorPickerListener(listener);
    }

    public void fadeIn() {
        //<editor-fold desc="body" defaultstate="collapsed">
        setOpacity(0f);
        setVisible(true);
        t = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                float opac = getOpacity();
                opac += 0.05f;
                if (opac > 1f) {
                    opac = 1;
                }
                setOpacity(opac);
                if (opac == 1) {
                    t.stop();
                }
            }
        });
        t.start();
        //</editor-fold>
    }

    @Override
    public void onApplyColor(Color color) {
        //<editor-fold desc="body" defaultstate="collapsed">
        listener.onApplyColor(color);
        //</editor-fold>
    }

    @Override
    public void onCancel() {
        listener.onCancel();
    }

    @Override
    public void onColorMove(Color c) {
        listener.onColorMove(c);
    }

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        colorPickerPanel1 = new smarthomesystem.ColorPickerPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(colorPickerPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(colorPickerPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public smarthomesystem.ColorPickerPanel colorPickerPanel1;
    // End of variables declaration//GEN-END:variables

}
