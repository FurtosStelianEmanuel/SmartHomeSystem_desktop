/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smarthomesystem;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.bluetooth.BluetoothStateException;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Manel
 */
public class HC05Connector extends javax.swing.JFrame {
    
    //<editor-fold desc="Variabile" defaultstate="collapsed">
    JPopupMenu pop;
    JMenuItem connectItem;
    //</editor-fold>
    
    public HC05Connector() {
        //<editor-fold desc="Constructor body" defaultstate="collapsed">
        initComponents();
        //setAlwaysOnTop(true);//scoate asta, e bugguit windows.... cacat
        setLocationRelativeTo(null);
        pop = new JPopupMenu();
        pop.setInvoker(pop);
        connectItem = new JMenuItem();
        connectItem.setIcon(SmartHomeSystem.resize("btConnectIcon.png"));
        connectItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                pop.setVisible(false);
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            userSelectedADevice((String) jTable1.getValueAt(jTable1.getSelectedRow(), 0),
                                    (String) jTable1.getValueAt(jTable1.getSelectedRow(), 1));
                            SmartHomeSystem.getSmartHomeSystem().connectionEstablished(HC05Connector.class);
                            dispose();
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(null, "Conexiunea cu "
                                    +(String) jTable1.getValueAt(jTable1.getSelectedRow(), 0)+" a esuat");
                            App.print("Eroare " + ex.toString(),HC05Connector.class);
                        }
                    }
                }.start();
            }
        });
        pop.add(connectItem);
        new Thread(){
            @Override
            public void run(){
                try {
                    List<Settings.ModulBluetooth>moduleVechi=Settings.getPreviousBluetoothConnections();
                    DefaultTableModel model=(DefaultTableModel)jTable1.getModel();
                    for (Settings.ModulBluetooth modul:moduleVechi){
                        model.addRow(new Object[]{modul.nume,modul.adresa});
                    }
                } catch (IOException | ClassNotFoundException ex) {
                    Logger.getLogger(HC05Connector.class.getName()).log(Level.SEVERE, null, ex);
                    App.print("Nu am putut incarca datele pentru modulele vechi",HC05Connector.class);
                }
            }
        }.start();
        //</editor-fold>
    }
    private String getConnectToken(String nume,String adresa){
        return "Conectează-te la "+nume;
    }
    @Override
    public void dispose(){
        //<editor-fold desc="body" defaultstate="collapsed">
        super.dispose();
        pop.setVisible(false);
        //</editor-fold>
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nume", "Adresa"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jButton1.setText("Caută conexiuni");
        jButton1.setFocusable(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        //<editor-fold desc="body" defaultstate="collapsed">
        jButton1.setEnabled(false);
        jButton1.setText("Se caută conexiuni, te rog asteaptă ");
        Timer t = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (jButton1.getText().length() < "Se caută conexiuni, te rog asteaptă . . . . . . . . . . .".length()) {
                    jButton1.setText(jButton1.getText() + ". ");
                } else {
                    jButton1.setText("Se caută conexiuni, te rog asteaptă ");
                }
            }
        });
        t.start();
        new Thread() {
            @Override
            public void run() {
                try {              
                    SmartHomeSystem.getSmartHomeSystem().getHC05().setTableContent((HC05Connector) SmartHomeSystem.getSmartHomeSystem().getGUI(HC05Connector.class));
                    jButton1.setText("Refresh");
                    jButton1.setEnabled(true);
                    t.stop();
                } catch (BluetoothStateException | InterruptedException ex) {
                    Logger.getLogger(HC05Connector.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }.start();
        //</editor-fold>
    }//GEN-LAST:event_jButton1ActionPerformed
    @Override
    public void setVisible(boolean state) {
        //<editor-fold desc="body" defaultstate="collapsed">
        super.setVisible(state);
        if (state == true) {
            toFront();
        }
        //</editor-fold>
    }

    public void userSelectedADevice(String nume, String adresa) throws IOException {
        SmartHomeSystem.getSmartHomeSystem().getHC05().connect(nume, adresa);
    }
    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        //<editor-fold desc="body" defaultstate="collapsed">
        pop.setVisible(false);
        if (evt.getButton() == MouseEvent.BUTTON3) {
            try {
                connectItem.setText(getConnectToken((String) jTable1.getValueAt(jTable1.getSelectedRow(), 0),
                        (String) jTable1.getValueAt(jTable1.getSelectedRow(), 1)));
            } catch (java.lang.ArrayIndexOutOfBoundsException ex) {
                App.print("Niciun element selectat, da prima data click stanga pe un element", HC05Connector.class);
            }
            pop.setLocation(evt.getXOnScreen(), evt.getYOnScreen());
            pop.setVisible(true);
        }
        //</editor-fold>
    }//GEN-LAST:event_jTable1MouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
