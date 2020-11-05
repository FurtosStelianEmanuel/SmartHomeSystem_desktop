/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smarthomesystem;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import smarthomesystem.SmartHomeSystem.ARGB;
import static smarthomesystem.SmartHomeSystem.getSmartHomeSystem;
import static smarthomesystem.SmartHomeSystem.getByte;

/**
 *
 * @author Manel
 */
public final class SettingsForm extends javax.swing.JFrame {

    //<editor-fold desc="Variabile" defaultstate="collapsed">
    static final String LED_STRIP_ARGB = "Adresabil";
    static final String LED_SIMPLU = "Simplu";
    static final String ANIMATION_FADE_ON = "Fade in";
    static final String ANIMATION_SECTIUNI1 = "Extindere secțiuni stanga-dreapta";
    static final String ANIMATION_SECTIUNI2 = "Extindere secțiuni dreapta";
    static final String ANIMATION_SECTIUNI3 = "Extindere secțiuni stânga";
    static final String ANIMATION_FADE_OUT = "Fade out";
    static final String ANIMATION_RETRACTIE_STANGA_DREAPTA = "Retracție secțiuni stânga-dreapta";
    static final String ANIMATION_RETRACTIE_STANGA = "Retracție secțiuni stânga";
    static final String ANIMATION_RETRACTIE_DREAPTA = "Retracție secțiuni dreapta";
    static final String ANIMATION_FADER_EXTENSIE_1 = "Extindere centrală 1";
    static final Color GRAY = new Color(0, 0, 0);

    //</editor-fold>
    /**
     * Se uita la selectiile facute de user in unele meniuri si decide ce sa
     * apara pe frame si ce sa nu apara
     */
    void updateUI() {
        //<editor-fold desc="body" defaultstate="collapsed">
        if (jComboBox1.getSelectedItem().equals(LED_STRIP_ARGB)) {
            setARGBOptionsVisible(true);

            if (jComboBox2.getSelectedItem().equals(ANIMATION_FADE_ON)) {
                jLabel6.setVisible(true);
                jSpinner2.setVisible(true);
            } else {
                jLabel6.setVisible(false);
                jSpinner2.setVisible(false);
            }
            if (jComboBox2.getSelectedItem().equals(ANIMATION_SECTIUNI1)
                    || jComboBox2.getSelectedItem().equals(ANIMATION_SECTIUNI2)
                    || jComboBox2.getSelectedItem().equals(ANIMATION_SECTIUNI3)) {
                jLabel7.setVisible(true);
                jSpinner3.setVisible(true);
            } else {
                jLabel7.setVisible(false);
                jSpinner3.setVisible(false);
            }
            if (jComboBox3.getSelectedItem().equals(ANIMATION_FADE_OUT)) {
                jLabel9.setVisible(true);
                jSpinner5.setVisible(true);
            } else {
                jLabel9.setVisible(false);
                jSpinner5.setVisible(false);
            }
            if (jComboBox3.getSelectedItem().equals(ANIMATION_RETRACTIE_STANGA_DREAPTA)
                    || jComboBox3.getSelectedItem().equals(ANIMATION_RETRACTIE_STANGA)
                    || jComboBox3.getSelectedItem().equals(ANIMATION_RETRACTIE_DREAPTA)) {
                jLabel10.setVisible(true);
                jSpinner4.setVisible(true);
            } else {
                jLabel10.setVisible(false);
                jSpinner4.setVisible(false);
            }
            if (jComboBox7.getSelectedItem().equals(ANIMATION_FADER_EXTENSIE_1)) {
                jLabel26.setVisible(false);
                jSpinner13.setVisible(false);
                jLabel29.setVisible(false);
                colorSliderPanel1.setVisible(false);
                jLabel31.setVisible(false);
                colorSliderPanel2.setVisible(false);

                jButton12.setVisible(true);
                jSpinner14.setVisible(true);
                jLabel27.setVisible(true);
                jLabel28.setVisible(true);
                jLabel32.setVisible(true);
                colorSliderPanel3.setVisible(true);

            } else {
                jLabel26.setVisible(true);
                jSpinner13.setVisible(true);
                jLabel29.setVisible(true);
                colorSliderPanel1.setVisible(true);
                jLabel31.setVisible(true);
                colorSliderPanel2.setVisible(true);
                jSpinner14.setVisible(true);
                jLabel27.setVisible(true);
                jButton12.setVisible(true);
                jLabel28.setVisible(true);

                jLabel32.setVisible(false);
                colorSliderPanel3.setVisible(false);
            }
        } else if (jComboBox1.getSelectedItem().equals(LED_SIMPLU)) {
            setARGBOptionsVisible(false);
        }
        //</editor-fold>
    }

    /**
     * Seteaza vizibilitatea componentelor care tin de animatiile strip-ului
     * adresabil
     *
     * @param state
     */
    void setARGBOptionsVisible(boolean state) {
        //<editor-fold desc="body" defaultstate="collapsed">
        jLabel7.setVisible(state);
        jSpinner3.setVisible(state);
        jSpinner2.setVisible(state);
        jLabel6.setVisible(state);
        jLabel5.setVisible(state);
        jSpinner1.setVisible(state);
        jComboBox2.setVisible(state);
        jLabel2.setVisible(state);
        jLabel3.setVisible(state);
        jComboBox3.setVisible(state);
        jLabel8.setVisible(state);
        jSpinner6.setVisible(state);
        jSpinner5.setVisible(state);
        jLabel9.setVisible(state);
        jLabel10.setVisible(state);
        jSpinner4.setVisible(state);
        jButton1.setVisible(state);
        jButton2.setVisible(state);

        jLabel4.setVisible(state);
        jLabel14.setVisible(state);
        jSpinner7.setVisible(state);
        jSpinner8.setVisible(state);
        jLabel15.setVisible(state);
        jLabel16.setVisible(state);
        jSpinner9.setVisible(state);
        jComboBox4.setVisible(state);
        jLabel17.setVisible(state);
        jLabel18.setVisible(state);
        jComboBox5.setVisible(state);
        jSpinner10.setVisible(state);
        jLabel19.setVisible(state);
        jButton3.setVisible(state);

        jLabel12.setVisible(state);
        jLabel13.setVisible(state);
        jButton4.setVisible(state);

        jLabel11.setVisible(state);

        jLabel20.setVisible(state);
        jSpinner11.setVisible(state);
        jLabel21.setVisible(state);
        jComboBox6.setVisible(state);
        jLabel23.setVisible(state);
        jLabel22.setVisible(state);
        jSpinner12.setVisible(state);
        jButton5.setVisible(state);

        jSeparator1.setVisible(state);
        jSeparator2.setVisible(state);
        jSeparator3.setVisible(state);
        jSeparator4.setVisible(state);
        jSeparator5.setVisible(state);

        jButton6.setVisible(state);
        jButton7.setVisible(state);
        jButton8.setVisible(state);
        jButton9.setVisible(state);

        jLabel24.setVisible(state);
        jComboBox7.setVisible(state);
        jLabel25.setVisible(state);
        jLabel26.setVisible(state);
        jSpinner13.setVisible(state);
        jLabel27.setVisible(state);
        jSpinner14.setVisible(state);
        jButton12.setVisible(state);
        jLabel28.setVisible(state);
        jLabel29.setVisible(state);
        jLabel31.setVisible(state);
        colorSliderPanel2.setVisible(state);
        colorSliderPanel1.setVisible(state);
        jButton11.setVisible(state);
        jButton10.setVisible(state);

        jLabel26.setVisible(state);
        jSpinner13.setVisible(state);
        jLabel29.setVisible(state);
        colorSliderPanel1.setVisible(state);
        jLabel31.setVisible(state);
        colorSliderPanel2.setVisible(state);

        jButton12.setVisible(state);
        jSpinner14.setVisible(state);
        jLabel27.setVisible(state);
        jLabel28.setVisible(state);
        jLabel32.setVisible(state);
        colorSliderPanel3.setVisible(state);

//</editor-fold>
    }

    public SettingsForm() {
        //<editor-fold desc="Constructor body" defaultstate="collapsed">

        initComponents();

        jComboBox1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                updateUI();
            }
        });

        setLocationRelativeTo(null);
        setARGBOptionsVisible(false);
        Settings.applyPreviousDataToSettingsForm(this);
        notifySaved();

        colorSliderPanel2.slider.setSoftMinimum(colorSliderPanel1.slider.getValue());
        colorSliderPanel1.slider.setSoftMaximum(colorSliderPanel2.slider.getValue());

        colorSliderPanel1.slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                colorSliderPanel2.slider.setSoftMinimum(colorSliderPanel1.slider.getValue());
            }
        });

        colorSliderPanel2.slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent ce) {
                colorSliderPanel1.slider.setSoftMaximum(colorSliderPanel2.slider.getValue());
            }
        });

        //</editor-fold> 
    }

    void notifySaveRequired(JButton b) {
        //<editor-fold desc="body" defaultstate="collapsed">
        b.setForeground(Color.RED);
        //</editor-fold>
    }

    void notifySaved() {
        //<editor-fold desc="body" defaultstate="collapsed">
        jButton6.setForeground(GRAY);
        jButton7.setForeground(GRAY);
        jButton9.setForeground(GRAY);
        jButton8.setForeground(GRAY);
        jButton11.setForeground(GRAY);
        try {
            for (Component ch : SmartHomeSystem.getTray().animatiiPornire.getMenuComponents()) {
                JCheckBoxMenuItem boxMenuItem = (JCheckBoxMenuItem) ch;
                if (jComboBox2.getSelectedItem().equals(boxMenuItem.getText())) {
                    boxMenuItem.setSelected(true);
                } else {
                    boxMenuItem.setSelected(false);
                }
            }
        } catch (NullPointerException ex) {
            //Logger.getLogger(SettingsForm.class.getName()).log(Level.SEVERE,ex.toString());
        }
        try {
            for (Component ch : SmartHomeSystem.getTray().animatiiOprire.getMenuComponents()) {
                JCheckBoxMenuItem boxMenuItem = (JCheckBoxMenuItem) ch;
                if (jComboBox3.getSelectedItem().equals(boxMenuItem.getText())) {
                    boxMenuItem.setSelected(true);
                } else {
                    boxMenuItem.setSelected(false);
                }
            }
        } catch (NullPointerException ex) {
            //Logger.getLogger(SettingsForm.class.getName()).log(Level.SEVERE,ex.toString());
        }
        try {
            for (Component ch : SmartHomeSystem.getTray().animatiiFastLED.getMenuComponents()) {
                JCheckBoxMenuItem boxMenuItem = (JCheckBoxMenuItem) ch;
                if (jComboBox6.getSelectedItem().equals(boxMenuItem.getText())) {
                    boxMenuItem.setSelected(true);
                } else {
                    boxMenuItem.setSelected(false);
                }
            }
        } catch (NullPointerException ex) {
            //Logger.getLogger(SettingsForm.class.getName()).log(Level.SEVERE,ex.toString());
        }
        //</editor-fold>
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jInternalFrame1 = new javax.swing.JInternalFrame();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox<>();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jSpinner4 = new javax.swing.JSpinner();
        jSpinner5 = new javax.swing.JSpinner();
        jSpinner6 = new javax.swing.JSpinner();
        jSeparator2 = new javax.swing.JSeparator();
        jComboBox2 = new javax.swing.JComboBox<>();
        jSpinner1 = new javax.swing.JSpinner();
        jSpinner2 = new javax.swing.JSpinner();
        jSpinner3 = new javax.swing.JSpinner();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        jSpinner7 = new javax.swing.JSpinner();
        jLabel15 = new javax.swing.JLabel();
        jSpinner8 = new javax.swing.JSpinner();
        jLabel16 = new javax.swing.JLabel();
        jSpinner9 = new javax.swing.JSpinner();
        jLabel17 = new javax.swing.JLabel();
        jComboBox4 = new javax.swing.JComboBox<>();
        jLabel18 = new javax.swing.JLabel();
        jComboBox5 = new javax.swing.JComboBox<>();
        jLabel19 = new javax.swing.JLabel();
        jSpinner10 = new javax.swing.JSpinner();
        jButton3 = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JSeparator();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jSpinner11 = new javax.swing.JSpinner();
        jLabel22 = new javax.swing.JLabel();
        jComboBox6 = new javax.swing.JComboBox<>();
        jSpinner12 = new javax.swing.JSpinner();
        jLabel23 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JSeparator();
        jLabel24 = new javax.swing.JLabel();
        jButton10 = new javax.swing.JButton();
        jComboBox7 = new javax.swing.JComboBox<>();
        jLabel25 = new javax.swing.JLabel();
        jButton11 = new javax.swing.JButton();
        jSpinner13 = new javax.swing.JSpinner();
        jLabel26 = new javax.swing.JLabel();
        jSpinner14 = new javax.swing.JSpinner();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jButton12 = new javax.swing.JButton();
        jLabel29 = new javax.swing.JLabel();
        colorSliderPanel1 = new smarthomesystem.ColorSliderPanel();
        colorSliderPanel1.slider.setType(ColorSlider.TYPE_GENERAL_SLIDER);
        colorSliderPanel1.slider.setValue(5);
        colorSliderPanel2 = new smarthomesystem.ColorSliderPanel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        colorSliderPanel3 = new smarthomesystem.ColorSliderPanel();
        jButton13 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(600, 600));

        jInternalFrame1.setIconifiable(true);
        jInternalFrame1.setTitle("Animații");
        jInternalFrame1.setMinimumSize(new java.awt.Dimension(200, 30));
        jInternalFrame1.setVisible(true);
        jInternalFrame1.addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
                jInternalFrame1InternalFrameDeiconified(evt);
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
                jInternalFrame1InternalFrameIconified(evt);
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
        });

        jScrollPane1.getVerticalScrollBar().setUnitIncrement(16);
        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel3.setText("Animația la oprire");

        jComboBox3.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Fade out", "Retracție centrală", "Retracție secțiuni stânga-dreapta", "Retracție secțiuni dreapta", "Retracție secțiuni stânga" }));
        jComboBox3.setFocusable(false);
        jComboBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox3ActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel8.setText("Viteză animație");

        jLabel9.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel9.setText("Decrement");

        jLabel10.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel10.setText("Secțiuni");

        jSpinner4.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jSpinner4.setModel(new javax.swing.SpinnerNumberModel(1, 1, 50, 1));
        jSpinner4.setFocusable(false);
        jSpinner4.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner4StateChanged(evt);
            }
        });

        jSpinner5.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jSpinner5.setModel(new javax.swing.SpinnerNumberModel(1, 1, 50, 1));
        jSpinner5.setFocusable(false);
        jSpinner5.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner5StateChanged(evt);
            }
        });

        jSpinner6.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jSpinner6.setModel(new javax.swing.SpinnerNumberModel(255, 1, 255, 1));
        jSpinner6.setFocusable(false);
        jSpinner6.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner6StateChanged(evt);
            }
        });

        jComboBox2.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Fade in", "Extindere centrală 1", "Extindere centrală 2", "Extindere secțiuni stanga-dreapta", "Extindere secțiuni dreapta", "Extindere secțiuni stânga" }));
        jComboBox2.setFocusable(false);
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });

        jSpinner1.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jSpinner1.setModel(new javax.swing.SpinnerNumberModel(255, 1, 255, 1));
        jSpinner1.setFocusable(false);
        jSpinner1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner1StateChanged(evt);
            }
        });

        jSpinner2.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jSpinner2.setModel(new javax.swing.SpinnerNumberModel(1, 1, 50, 1));
        jSpinner2.setFocusable(false);
        jSpinner2.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner2StateChanged(evt);
            }
        });

        jSpinner3.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jSpinner3.setModel(new javax.swing.SpinnerNumberModel(1, 1, 50, 1));
        jSpinner3.setFocusable(false);
        jSpinner3.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner3StateChanged(evt);
            }
        });

        jComboBox1.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Adresabil", "Simplu" }));
        jComboBox1.setSelectedItem(null);
        jComboBox1.setFocusable(false);
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel2.setText("Animația la setarea culorii");

        jLabel7.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel7.setText("Secțiuni");

        jLabel5.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel5.setText("Viteză animație");

        jLabel6.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel6.setText("Increment");

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel1.setText("Tip de strip RGB");

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel4.setText("Animația cu steagul României");

        jLabel12.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel12.setText("Tip");

        jLabel13.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel13.setText("Tip");

        jButton1.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jButton1.setText("Testează");
        jButton1.setFocusable(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jButton2.setText("Testează");
        jButton2.setFocusable(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel14.setText("Viteză animație");

        jSpinner7.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jSpinner7.setModel(new javax.swing.SpinnerNumberModel(250, 1, 255, 1));
        jSpinner7.setFocusable(false);
        jSpinner7.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner7StateChanged(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel15.setText("Nr. steaguri");

        jSpinner8.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jSpinner8.setModel(new javax.swing.SpinnerNumberModel(1, 1, 50, 1));
        jSpinner8.setFocusable(false);
        jSpinner8.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner8StateChanged(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel16.setText("Lățime steaguri");

        jSpinner9.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jSpinner9.setModel(new javax.swing.SpinnerNumberModel(6, 3, 500, 1));
        jSpinner9.setFocusable(false);
        jSpinner9.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner9StateChanged(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel17.setText("Direcție");

        jComboBox4.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Dreapta", "Stânga" }));
        jComboBox4.setFocusable(false);
        jComboBox4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox4ActionPerformed(evt);
            }
        });

        jLabel18.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel18.setText("Orientare");

        jComboBox5.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Dreapta", "Stânga" }));
        jComboBox5.setFocusable(false);
        jComboBox5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox5ActionPerformed(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel19.setText("Intensitate");

        jSpinner10.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jSpinner10.setModel(new javax.swing.SpinnerNumberModel(255, 1, 255, 1));
        jSpinner10.setFocusable(false);
        jSpinner10.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner10StateChanged(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jButton3.setText("Testează");
        jButton3.setFocusable(false);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel11.setText("Culoare");

        jButton4.setBackground(new java.awt.Color(255, 0, 255));
        jButton4.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        jButton4.setForeground(new java.awt.Color(0, 0, 0));
        jButton4.setText("#ff00ff");
        jButton4.setContentAreaFilled(false);
        jButton4.setFocusable(false);
        jButton4.setOpaque(true);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel20.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel20.setText("Animații din librăria FastLED");

        jLabel21.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel21.setText("Viteză animație");

        jSpinner11.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jSpinner11.setModel(new javax.swing.SpinnerNumberModel(255, 1, 255, 1));
        jSpinner11.setFocusable(false);
        jSpinner11.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner11StateChanged(evt);
            }
        });

        jLabel22.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel22.setText("Tip");

        jComboBox6.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jComboBox6.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Rainbow-LinearBlend", "RainbowStripeColors-NoBlend", "RainbowStripeColors-LinearBlend", "Green and purple-LinearBlend", "Random-LinearBlend", "Black&White-NoBlend", "Black&White-LinearBlend", "CloudColors-LinearBlend", "PartyColors-LinearBlend" }));
        jComboBox6.setFocusable(false);
        jComboBox6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox6ActionPerformed(evt);
            }
        });

        jSpinner12.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jSpinner12.setModel(new javax.swing.SpinnerNumberModel(255, 1, 255, 1));
        jSpinner12.setFocusable(false);
        jSpinner12.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner12StateChanged(evt);
            }
        });

        jLabel23.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel23.setText("Intensitate");

        jButton5.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jButton5.setText("Testează");
        jButton5.setFocusable(false);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jButton6.setText("Save");
        jButton6.setFocusable(false);
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jButton7.setText("Save");
        jButton7.setFocusable(false);
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jButton8.setText("Save");
        jButton8.setFocusable(false);
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton9.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jButton9.setText("Save");
        jButton9.setFocusable(false);
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jLabel24.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel24.setText("Animații fader");

        jButton10.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jButton10.setText("Testează");
        jButton10.setFocusable(false);
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jComboBox7.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jComboBox7.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Basic", "Extindere centrală 1" }));
        jComboBox7.setFocusable(false);
        jComboBox7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox7ActionPerformed(evt);
            }
        });

        jLabel25.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel25.setText("Tip");

        jButton11.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jButton11.setText("Save");
        jButton11.setFocusable(false);
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jSpinner13.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jSpinner13.setModel(new javax.swing.SpinnerNumberModel(12, 1, 255, 1));
        jSpinner13.setFocusable(false);
        jSpinner13.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner13StateChanged(evt);
            }
        });

        jLabel26.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel26.setText("Increment");

        jSpinner14.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jSpinner14.setModel(new javax.swing.SpinnerNumberModel(255, 1, 255, 1));
        jSpinner14.setFocusable(false);
        jSpinner14.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner14StateChanged(evt);
            }
        });

        jLabel27.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel27.setText("Viteză animație");

        jLabel28.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel28.setText("Culoare");

        jButton12.setBackground(new java.awt.Color(255, 0, 255));
        jButton12.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        jButton12.setForeground(new java.awt.Color(0, 0, 0));
        jButton12.setText("#ff00ff");
        jButton12.setContentAreaFilled(false);
        jButton12.setFocusable(false);
        jButton12.setOpaque(true);
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jLabel29.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel29.setText("Intensitate minimă ");

        colorSliderPanel2.slider.setType(ColorSlider.TYPE_GENERAL_SLIDER);

        jLabel31.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel31.setText("Intensitate maximă");

        jLabel32.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel32.setText("Dimensiune");

        colorSliderPanel3.slider.setType(ColorSlider.TYPE_GENERAL_SLIDER);
        colorSliderPanel3.slider.setMaximum(255);
        colorSliderPanel3.slider.setMinimum(3);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(jSeparator3)
            .addComponent(jSeparator4, javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(jSeparator5, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel24)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel25)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jComboBox7, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel26)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jSpinner13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel27)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jSpinner14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jButton11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton10, javax.swing.GroupLayout.Alignment.TRAILING)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel20)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel21)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jSpinner11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel22)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel4)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel14)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jSpinner7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel15)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jSpinner8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel3)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel8)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jSpinner6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel12)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel10)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jSpinner4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel9)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jSpinner5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel1)
                                        .addGap(11, 11, 11)
                                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel6)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jSpinner2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel7)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jSpinner3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel16)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jSpinner9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel17)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel18)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel19)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jSpinner10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel23)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jSpinner12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 107, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jButton5))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jButton3, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jButton2, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel5)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel2)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel13)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel11)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jButton4)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButton1)
                                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(20, 20, 20))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel29)
                                    .addGap(15, 15, 15)
                                    .addComponent(colorSliderPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel31)
                                    .addGap(15, 15, 15)
                                    .addComponent(colorSliderPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel28)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton12))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel32)
                                .addGap(15, 15, 15)
                                .addComponent(colorSliderPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(jButton4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton6)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jSpinner2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jSpinner3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12)
                            .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(jSpinner6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(jSpinner5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(jSpinner4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton7)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14)
                            .addComponent(jSpinner7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel15)
                            .addComponent(jSpinner8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel16)
                            .addComponent(jSpinner9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel17)
                            .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel18)
                            .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel19)
                            .addComponent(jSpinner10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton8)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel20)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel21)
                            .addComponent(jSpinner11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel22)
                            .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel23)
                            .addComponent(jSpinner12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton9)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton11))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel24)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel25)
                            .addComponent(jComboBox7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel26)
                            .addComponent(jSpinner13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel27)
                            .addComponent(jSpinner14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28)
                    .addComponent(jButton12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel29)
                        .addGap(7, 7, 7))
                    .addComponent(colorSliderPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel31)
                        .addGap(7, 7, 7))
                    .addComponent(colorSliderPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel32)
                        .addGap(7, 7, 7))
                    .addComponent(colorSliderPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22))
        );

        jScrollPane1.setViewportView(jPanel1);

        javax.swing.GroupLayout jInternalFrame1Layout = new javax.swing.GroupLayout(jInternalFrame1.getContentPane());
        jInternalFrame1.getContentPane().setLayout(jInternalFrame1Layout);
        jInternalFrame1Layout.setHorizontalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
        jInternalFrame1Layout.setVerticalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jInternalFrame1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 433, Short.MAX_VALUE))
        );

        jButton13.setText("jButton13");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jButton14.setText("jButton13");
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        jButton15.setText("jButton15");
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jInternalFrame1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton13)
                    .addComponent(jButton14)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jButton15)))
                .addContainerGap(51, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jInternalFrame1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(83, 83, 83)
                        .addComponent(jButton13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton15)))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        try {
            jInternalFrame1.setIcon(true);
        } catch (java.beans.PropertyVetoException e1) {
            e1.printStackTrace();
        }

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void jInternalFrame1InternalFrameIconified(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_jInternalFrame1InternalFrameIconified
        jScrollPane1.setVisible(false);
    }//GEN-LAST:event_jInternalFrame1InternalFrameIconified

    private void jInternalFrame1InternalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_jInternalFrame1InternalFrameDeiconified
        jScrollPane1.setVisible(true);

    }//GEN-LAST:event_jInternalFrame1InternalFrameDeiconified

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        ColorPickerForm pickerForm = (ColorPickerForm) getSmartHomeSystem().getGUI(ColorPickerForm.class);
        pickerForm.setListener(new ColorPickerActionListener() {
            @Override
            public void onApplyColor(Color c) {
                jButton12.setBackground(c);
                jButton12.setText(Integer.toHexString(c.getRGB()).substring(2));
                colorSliderPanel1.slider.setGeneralColor(c);
                colorSliderPanel2.slider.setGeneralColor(c);
                notifySaveRequired(jButton11);
                getSmartHomeSystem().send(ARGB.COMMAND_TURN_OFF);
                pickerForm.dispose();
            }

            @Override
            public void onCancel() {
                pickerForm.dispose();
                getSmartHomeSystem().send(ARGB.COMMAND_TURN_OFF);
            }

            @Override
            public void onColorMove(Color c) {
                getSmartHomeSystem().send(ARGB.COMMAND_SET_COLOR, c);
            }
        });
        pickerForm.fadeIn();
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jSpinner14StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner14StateChanged
        notifySaveRequired(jButton11);
    }//GEN-LAST:event_jSpinner14StateChanged

    private void jSpinner13StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner13StateChanged
        notifySaveRequired(jButton11);
    }//GEN-LAST:event_jSpinner13StateChanged

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        //<editor-fold desc="body" defaultstate="collapsed">
        try {
            Settings.savePreviousDataOfSettingsForm(this);
            notifySaved();
        } catch (IOException ex) {
            Logger.getLogger(SettingsForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        //</editor-fold>
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jComboBox7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox7ActionPerformed
        updateUI();
        notifySaveRequired(jButton11);
    }//GEN-LAST:event_jComboBox7ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        getSmartHomeSystem().send(ARGB.COMMAND_FADER_ANIMATION);
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        //<editor-fold desc="body" defaultstate="collapsed">
        try {
            Settings.savePreviousDataOfSettingsForm(this);
            notifySaved();
        } catch (IOException ex) {
            Logger.getLogger(SettingsForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        //</editor-fold>
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        //<editor-fold desc="body" defaultstate="collapsed">
        try {
            Settings.savePreviousDataOfSettingsForm(this);
            notifySaved();
        } catch (IOException ex) {
            Logger.getLogger(SettingsForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        //</editor-fold>
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        //<editor-fold desc="body" defaultstate="collapsed">
        try {
            Settings.savePreviousDataOfSettingsForm(this);
            notifySaved();
        } catch (IOException ex) {
            Logger.getLogger(SettingsForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        //</editor-fold>
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        //<editor-fold desc="body" defaultstate="collapsed">
        try {
            Settings.savePreviousDataOfSettingsForm(this);
            notifySaved();
        } catch (IOException ex) {
            Logger.getLogger(SettingsForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        //</editor-fold>
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        //<editor-fold desc="body" defaultstate="collapsed">
        getSmartHomeSystem().send(ARGB.COMMAND_BLENDING_ANIMATION);
        //</editor-fold>
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jSpinner12StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner12StateChanged
        //<editor-fold desc="body" defaultstate="collapsed">
        notifySaveRequired(jButton9);
        //</editor-fold>
    }//GEN-LAST:event_jSpinner12StateChanged

    private void jComboBox6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox6ActionPerformed
        //<editor-fold desc="body" defaultstate="collapsed">
        notifySaveRequired(jButton9);
        //</editor-fold>
    }//GEN-LAST:event_jComboBox6ActionPerformed

    private void jSpinner11StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner11StateChanged
        //<editor-fold desc="body" defaultstate="collapsed">
        notifySaveRequired(jButton9);
        //</editor-fold>
    }//GEN-LAST:event_jSpinner11StateChanged

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        //<editor-fold desc="body" defaultstate="collapsed">
        //setState(JFrame.ICONIFIED);
        ColorPickerForm pickerForm = (ColorPickerForm) getSmartHomeSystem().getGUI(ColorPickerForm.class);
        pickerForm.setListener(new ColorPickerActionListener() {
            @Override
            public void onApplyColor(Color c) {
                jButton4.setBackground(c);
                jButton4.setText(Integer.toHexString(c.getRGB()).substring(2));
                notifySaveRequired(jButton6);
                getSmartHomeSystem().send(ARGB.COMMAND_TURN_OFF);
                pickerForm.dispose();
            }

            @Override
            public void onCancel() {
                pickerForm.dispose();
                getSmartHomeSystem().send(ARGB.COMMAND_TURN_OFF);
            }

            @Override
            public void onColorMove(Color c) {
                getSmartHomeSystem().send(ARGB.COMMAND_SET_COLOR, c);
            }
        });
        pickerForm.fadeIn();
        //</editor-fold>
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        //<editor-fold desc="body" defaultstate="collapsed">
        getSmartHomeSystem().send(ARGB.COMMAND_ROMANIAN_ANIMATION);

        //</editor-fold>
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jSpinner10StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner10StateChanged
        //<editor-fold desc="body" defaultstate="collapsed">
        notifySaveRequired(jButton8);
        //</editor-fold>
    }//GEN-LAST:event_jSpinner10StateChanged

    private void jComboBox5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox5ActionPerformed
        //<editor-fold desc="body" defaultstate="collapsed">
        notifySaveRequired(jButton8);
        //</editor-fold>
    }//GEN-LAST:event_jComboBox5ActionPerformed

    private void jComboBox4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox4ActionPerformed
        //<editor-fold desc="body" defaultstate="collapsed">
        notifySaveRequired(jButton8);
        //</editor-fold>
    }//GEN-LAST:event_jComboBox4ActionPerformed

    private void jSpinner9StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner9StateChanged
        //<editor-fold desc="body" defaultstate="collapsed">
        notifySaveRequired(jButton8);
        //</editor-fold>
    }//GEN-LAST:event_jSpinner9StateChanged

    private void jSpinner8StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner8StateChanged
        //<editor-fold desc="body" defaultstate="collapsed">
        notifySaveRequired(jButton8);
        //</editor-fold>
    }//GEN-LAST:event_jSpinner8StateChanged

    private void jSpinner7StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner7StateChanged
        //<editor-fold desc="body" defaultstate="collapsed">
        notifySaveRequired(jButton8);
        //</editor-fold>
    }//GEN-LAST:event_jSpinner7StateChanged

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        //<editor-fold desc="body" defaultstate="collapsed">
        getSmartHomeSystem().send(ARGB.COMMAND_TURN_OFF);

        //</editor-fold>
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        //<editor-fold desc="body" defaultstate="collapsed">
        getSmartHomeSystem().send(ARGB.COMMAND_TURN_ON);
        //</editor-fold>
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        //<editor-fold desc="body" defaultstate="collapsed">
        if (Settings.STARTUP_ERROR_StripType) {
            JOptionPane.showMessageDialog(null, "Reporneste aplicatia");
            try {
                Settings.savePreviousDataOfSettingsForm(this);
                System.exit(0);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Eroare cand am incercat sa salvez starea SettingsForm");
                Logger.getLogger(SettingsForm.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //</editor-fold>
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jSpinner3StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner3StateChanged
        //<editor-fold desc="body" defaultstate="collapsed">
        notifySaveRequired(jButton6);
        //</editor-fold>
    }//GEN-LAST:event_jSpinner3StateChanged

    private void jSpinner2StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner2StateChanged
        //<editor-fold desc="body" defaultstate="collapsed">
        notifySaveRequired(jButton6);
        //</editor-fold>
    }//GEN-LAST:event_jSpinner2StateChanged

    private void jSpinner1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner1StateChanged
        //<editor-fold desc="body" defaultstate="collapsed">
        notifySaveRequired(jButton6);
        //</editor-fold>
    }//GEN-LAST:event_jSpinner1StateChanged

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        //<editor-fold desc="body" defaultstate="collapsed">
        updateUI();
        notifySaveRequired(jButton6);
        //</editor-fold>
    }//GEN-LAST:event_jComboBox2ActionPerformed

    private void jSpinner6StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner6StateChanged
        //<editor-fold desc="body" defaultstate="collapsed">
        notifySaveRequired(jButton7);
        //</editor-fold>
    }//GEN-LAST:event_jSpinner6StateChanged

    private void jSpinner5StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner5StateChanged
        //<editor-fold desc="body" defaultstate="collapsed">
        notifySaveRequired(jButton7);
        //</editor-fold>
    }//GEN-LAST:event_jSpinner5StateChanged

    private void jSpinner4StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner4StateChanged
        //<editor-fold desc="body" defaultstate="collapsed">
        notifySaveRequired(jButton7);
        //</editor-fold>
    }//GEN-LAST:event_jSpinner4StateChanged

    private void jComboBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox3ActionPerformed
        //<editor-fold desc="body" defaultstate="collapsed">
        updateUI();
        notifySaveRequired(jButton7);
        //</editor-fold>
    }//GEN-LAST:event_jComboBox3ActionPerformed
    boolean sent = false;

    int bytesSent = 0;

    byte[] lastCommand = new byte[]{getByte(255)};

    public synchronized void sendAnimationCommand(byte[] command) {
        System.out.println("trimit " + Arrays.toString(command));
        getSmartHomeSystem().send(command);
        lastCommand = command;
        bytesSent += command.length;
        try {
            //System.out.println("Asteptam");
            wait();
            //System.out.println("Gata asteptarea");
        } catch (InterruptedException ex) {
            Logger.getLogger(SettingsForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendUpdateCommand() {
        sendAnimationCommand(new byte[]{getByte(255)});
    }
    Thread t = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true) {
                System.out.println(String.format("%s/s", bytesSent));
                bytesSent = 0;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(SettingsForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    });

    byte[][] comenziAnimatie = new byte[][]{
        new byte[]{getByte(0), getByte(0), getByte(75)},
        new byte[]{getByte(16), getByte(255), getByte(255), getByte(0), getByte(17)},
        new byte[]{getByte(50), getByte(255), getByte(255), getByte(0), getByte(51)},
        new byte[]{getByte(15), getByte(255), getByte(255), getByte(0), getByte(18)},
        new byte[]{getByte(49), getByte(255), getByte(255), getByte(0), getByte(52)},
        new byte[]{getByte(14), getByte(255), getByte(255), getByte(0), getByte(19)},
        new byte[]{getByte(48), getByte(255), getByte(255), getByte(0), getByte(53)},
        new byte[]{getByte(13), getByte(255), getByte(255), getByte(0), getByte(20)},
        new byte[]{getByte(47), getByte(255), getByte(255), getByte(0), getByte(54)},
        new byte[]{getByte(12), getByte(255), getByte(255), getByte(0), getByte(21)},
        new byte[]{getByte(46), getByte(255), getByte(255), getByte(0), getByte(55)},
        new byte[]{getByte(11), getByte(255), getByte(255), getByte(0), getByte(22)},
        new byte[]{getByte(45), getByte(255), getByte(255), getByte(0), getByte(56)},
        new byte[]{getByte(10), getByte(255), getByte(255), getByte(0), getByte(23)},
        new byte[]{getByte(44), getByte(255), getByte(255), getByte(0), getByte(57)},
        new byte[]{getByte(9), getByte(255), getByte(255), getByte(0), getByte(24)},
        new byte[]{getByte(43), getByte(255), getByte(255), getByte(0), getByte(58)},
        new byte[]{getByte(8), getByte(255), getByte(255), getByte(0), getByte(25)},
        new byte[]{getByte(42), getByte(255), getByte(255), getByte(0), getByte(59)},
        new byte[]{getByte(7), getByte(255), getByte(255), getByte(0), getByte(26)},
        new byte[]{getByte(41), getByte(255), getByte(255), getByte(0), getByte(59)},
        new byte[]{getByte(6), getByte(255), getByte(255), getByte(0), getByte(27)},
        new byte[]{getByte(40), getByte(255), getByte(255), getByte(0), getByte(60)},
        new byte[]{getByte(5), getByte(255), getByte(255), getByte(0), getByte(28)},
        new byte[]{getByte(39), getByte(255), getByte(255), getByte(0), getByte(61)},
        new byte[]{getByte(4), getByte(255), getByte(255), getByte(0), getByte(29)},
        new byte[]{getByte(38), getByte(255), getByte(255), getByte(0), getByte(62)},
        new byte[]{getByte(3), getByte(255), getByte(255), getByte(0), getByte(30)},
        new byte[]{getByte(37), getByte(255), getByte(255), getByte(0), getByte(63)},
        new byte[]{getByte(2), getByte(255), getByte(255), getByte(0), getByte(31)},
        new byte[]{getByte(36), getByte(255), getByte(255), getByte(0), getByte(64)},
        new byte[]{getByte(1), getByte(255), getByte(255), getByte(0), getByte(32)},
        new byte[]{getByte(35), getByte(255), getByte(255), getByte(0), getByte(65)},
        new byte[]{getByte(1), getByte(32)},
        new byte[]{getByte(35), getByte(65)},
        new byte[]{getByte(2), getByte(31)},
        new byte[]{getByte(36), getByte(64)},
        new byte[]{getByte(3), getByte(30)},
        new byte[]{getByte(37), getByte(63)},
        new byte[]{getByte(4), getByte(29)},
        new byte[]{getByte(38), getByte(62)},
        new byte[]{getByte(5), getByte(28)},
        new byte[]{getByte(39), getByte(61)},
        new byte[]{getByte(6), getByte(27)},
        new byte[]{getByte(40), getByte(60)},
        new byte[]{getByte(7), getByte(26)},
        new byte[]{getByte(41), getByte(59)},
        new byte[]{getByte(8), getByte(25)},
        new byte[]{getByte(42), getByte(58)},
        new byte[]{getByte(9), getByte(24)},
        new byte[]{getByte(43), getByte(57)},
        new byte[]{getByte(10), getByte(23)},
        new byte[]{getByte(44), getByte(56)},
        new byte[]{getByte(11), getByte(22)},
        new byte[]{getByte(45), getByte(55)},
        new byte[]{getByte(12), getByte(21)},
        new byte[]{getByte(46), getByte(54)},
        new byte[]{getByte(13), getByte(20)},
        new byte[]{getByte(47), getByte(53)},
        new byte[]{getByte(14), getByte(19)},
        new byte[]{getByte(48), getByte(52)},
        new byte[]{getByte(15), getByte(18)},
        new byte[]{getByte(49), getByte(51)},
        new byte[]{getByte(16), getByte(17)},
        new byte[]{getByte(50), getByte(50)},};

    
    StringBuilder message = new StringBuilder();



    boolean done = false;
    boolean on = true;
    int counter = 0;

    static byte to8BitColor(Color c) {
        int total = 0;
        int r = c.getRed() >> 5 << 5;
        int g = c.getGreen() >> 5 << 2;
        int b = c.getBlue() >> 6;

        total = r + g + b;

        return (byte) total;
    }

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed

        if (!done) {
            getSmartHomeSystem().send(new byte[]{'A', '1', '\r'});
            done = true;
        } else {
                        
            for (int led = 0; led < 75; led++) {
                sendAnimationCommand(new byte[]{getByte(led)});
                sendUpdateCommand();
            }
            
            int startPoint = 36;
            int length = 30;
            int c = 1;
            while (c <= length / 2) {
                sendAnimationCommand(new byte[]{getByte(startPoint - c),  to8BitColor(Color.RED)});
                sendAnimationCommand(new byte[]{getByte(startPoint + c),  to8BitColor(Color.RED)});
                
                sendAnimationCommand(new byte[]{getByte(0),  to8BitColor(Color.BLUE)});
                sendAnimationCommand(new byte[]{getByte(74),  to8BitColor(Color.BLUE)});
                sendAnimationCommand(new byte[]{getByte(1),  to8BitColor(Color.BLUE)});
                sendAnimationCommand(new byte[]{getByte(73),  to8BitColor(Color.BLUE)});
                sendAnimationCommand(new byte[]{getByte(2),  to8BitColor(Color.BLUE)});
                sendAnimationCommand(new byte[]{getByte(72),  to8BitColor(Color.BLUE)});
                sendAnimationCommand(new byte[]{getByte(3),  to8BitColor(Color.BLUE)});
                sendAnimationCommand(new byte[]{getByte(71),  to8BitColor(Color.BLUE)});
                
                sendUpdateCommand();
                c++;
            }
            while (c >= 0) {
                sendAnimationCommand(new byte[]{getByte(startPoint - c + length / 2)});
                sendAnimationCommand(new byte[]{getByte(startPoint + c - length / 2)});
                try {
                    Thread.sleep(5);
                } catch (InterruptedException ex) {
                    Logger.getLogger(SettingsForm.class.getName()).log(Level.SEVERE, null, ex);
                }
                sendUpdateCommand();
                c--;
            }
            int countLeds = 75;
            for (int repeat = 0; repeat < 10; repeat++) {
                for (int i = 0; i < 255; i += 31) {
                    for (int led = 0; led < countLeds; led += 5) {
                        Color color = new Color(i, i, 0);
                        sendAnimationCommand(new byte[]{getByte(led),  to8BitColor(color),  to8BitColor(color),  to8BitColor(color),  to8BitColor(color), to8BitColor(color)});
                    }
                    sendUpdateCommand();
                }
                for (int i = 255; i >= 0; i -= 31) {
                    for (int led = 0; led < countLeds; led += 5) {
                        Color color = new Color(i, i, 0);
                        sendAnimationCommand(new byte[]{getByte(led),  to8BitColor(color),  to8BitColor(color), to8BitColor(color),  to8BitColor(color), to8BitColor(color)});
                    }
                    sendUpdateCommand();
                }
            }
            for (int i = 0; i < 255; i += 31) {
                byte[] command = new byte[]{getByte(0), to8BitColor(Color.ORANGE), getByte(74)};
                sendAnimationCommand(command);
                sendUpdateCommand();
            }
        }

    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed


    }//GEN-LAST:event_jButton14ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        sendAnimationCommand(new byte[]{getByte(255),getByte(255),getByte(255),getByte(255),getByte(255),getByte(255)});
    }//GEN-LAST:event_jButton15ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public smarthomesystem.ColorSliderPanel colorSliderPanel1;
    public smarthomesystem.ColorSliderPanel colorSliderPanel2;
    public smarthomesystem.ColorSliderPanel colorSliderPanel3;
    public javax.swing.JButton jButton1;
    public javax.swing.JButton jButton10;
    public javax.swing.JButton jButton11;
    public javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    public javax.swing.JButton jButton2;
    public javax.swing.JButton jButton3;
    public javax.swing.JButton jButton4;
    public javax.swing.JButton jButton5;
    public javax.swing.JButton jButton6;
    public javax.swing.JButton jButton7;
    public javax.swing.JButton jButton8;
    public javax.swing.JButton jButton9;
    public javax.swing.JComboBox<String> jComboBox1;
    public javax.swing.JComboBox<String> jComboBox2;
    public javax.swing.JComboBox<String> jComboBox3;
    public javax.swing.JComboBox<String> jComboBox4;
    public javax.swing.JComboBox<String> jComboBox5;
    public javax.swing.JComboBox<String> jComboBox6;
    public javax.swing.JComboBox<String> jComboBox7;
    private javax.swing.JInternalFrame jInternalFrame1;
    public javax.swing.JLabel jLabel1;
    public javax.swing.JLabel jLabel10;
    public javax.swing.JLabel jLabel11;
    public javax.swing.JLabel jLabel12;
    public javax.swing.JLabel jLabel13;
    public javax.swing.JLabel jLabel14;
    public javax.swing.JLabel jLabel15;
    public javax.swing.JLabel jLabel16;
    public javax.swing.JLabel jLabel17;
    public javax.swing.JLabel jLabel18;
    public javax.swing.JLabel jLabel19;
    public javax.swing.JLabel jLabel2;
    public javax.swing.JLabel jLabel20;
    public javax.swing.JLabel jLabel21;
    public javax.swing.JLabel jLabel22;
    public javax.swing.JLabel jLabel23;
    public javax.swing.JLabel jLabel24;
    public javax.swing.JLabel jLabel25;
    public javax.swing.JLabel jLabel26;
    public javax.swing.JLabel jLabel27;
    public javax.swing.JLabel jLabel28;
    public javax.swing.JLabel jLabel29;
    public javax.swing.JLabel jLabel3;
    public javax.swing.JLabel jLabel31;
    public javax.swing.JLabel jLabel32;
    public javax.swing.JLabel jLabel4;
    public javax.swing.JLabel jLabel5;
    public javax.swing.JLabel jLabel6;
    public javax.swing.JLabel jLabel7;
    public javax.swing.JLabel jLabel8;
    public javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JSeparator jSeparator1;
    public javax.swing.JSeparator jSeparator2;
    public javax.swing.JSeparator jSeparator3;
    public javax.swing.JSeparator jSeparator4;
    public javax.swing.JSeparator jSeparator5;
    public javax.swing.JSpinner jSpinner1;
    public javax.swing.JSpinner jSpinner10;
    public javax.swing.JSpinner jSpinner11;
    public javax.swing.JSpinner jSpinner12;
    public javax.swing.JSpinner jSpinner13;
    public javax.swing.JSpinner jSpinner14;
    public javax.swing.JSpinner jSpinner2;
    public javax.swing.JSpinner jSpinner3;
    public javax.swing.JSpinner jSpinner4;
    public javax.swing.JSpinner jSpinner5;
    public javax.swing.JSpinner jSpinner6;
    public javax.swing.JSpinner jSpinner7;
    public javax.swing.JSpinner jSpinner8;
    public javax.swing.JSpinner jSpinner9;
    // End of variables declaration//GEN-END:variables
}
