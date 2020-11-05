/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smarthomesystem;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import smarthomesystem.SmartHomeSystem.ARGB;
import smarthomesystem.SmartHomeSystem.RGB;
import static smarthomesystem.SmartHomeSystem.getSmartHomeSystem;

/**
 *
 * @author Manel
 */
public final class Tray {

    //<editor-fold desc="Variabile" defaultstate="collapsed">
    SystemTray systemTray;
    TrayIcon trayIcon;
    JPopupMenu meniuPrincipal;
    Dimension dimensiuneTray;
    Image iconConnected, iconDisconnected;
    JCheckBoxMenuItem previousFastLEDCheckBox, previousTurnOnAnimationCheckBox, previousTurnOffAnimationCheckBox, previousFaderAnimationCheckBox;
    public JMenu animatiiPornire;
    public JMenu animatiiOprire;
    public JMenu alteAnimatii;
    JMenuItem animatieSteag, createAnimation;
    public JMenu animatiiFastLED;
    public JMenu animatiiFader;
    public JMenu conectivitate;
    JMenu animatii;
    JMenuItem animatieScreenMirror;
    public static final String CONNECTION_LOST_TOOL_TIP = "Conexiune pierduta";
    public static final String CONNECTION_ESTABLISHED_TOOL_TIP = "Conexiune prin ";

    //</editor-fold>
    @Deprecated
    void connectionLost() {
        trayIcon.getImage().flush();
        trayIcon.setImage(iconDisconnected);
        trayIcon.setToolTip(CONNECTION_LOST_TOOL_TIP);
    }

    @Deprecated
    void connectionEstablished(Class c) {
        trayIcon.getImage().flush();
        trayIcon.setImage(iconConnected);
        trayIcon.setToolTip(CONNECTION_ESTABLISHED_TOOL_TIP
                + (c == SocketConnection.class ? "LAN" : "Bluetooth"));
    }

    public Tray() throws AWTException {
        //<editor-fold desc="constructor body" defaultstate="collapsed">
        SettingsForm settingsFormRef = (SettingsForm) getSmartHomeSystem().getGUI(SettingsForm.class);
        meniuPrincipal = new JPopupMenu();
        meniuPrincipal.setInvoker(meniuPrincipal); //linia asta face sa mearga nenorocitu de {@link JPopupMenu}
        systemTray = SystemTray.getSystemTray();
        dimensiuneTray = systemTray.getTrayIconSize();
        iconConnected = new ImageIcon(getClass().getResource("/resurse/icon_connected.png")).getImage()
                .getScaledInstance((int) dimensiuneTray.getWidth(),
                        (int) dimensiuneTray.getHeight(),
                        Image.SCALE_SMOOTH);
        iconDisconnected = new ImageIcon(getClass().getResource("/resurse/icon_disconnected.png")).getImage()
                .getScaledInstance((int) dimensiuneTray.getWidth(),
                        (int) dimensiuneTray.getHeight(),
                        Image.SCALE_SMOOTH);
        trayIcon = new TrayIcon(iconDisconnected);

        trayIcon.setToolTip("Fara conexiune");

        trayIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                switch (e.getButton()) {
                    case MouseEvent.BUTTON1:
                        if (trayIcon.getToolTip().equals(SmartHomeSystem.ANDROID_REROUTE_BLUETOOTH)
                                || trayIcon.getToolTip().equals(SmartHomeSystem.ANDROID_REROUTE_LAN)) {

                            SmartHomeSystem.getSmartHomeSystem().send(">" + App.COMMAND_TURN_MID_LIGHT_ON + "");
                        } else /*if (SmartHomeSystem.getSmartHomeSystem().isHC_06Connected()) */ {
                            SettingsForm form = (SettingsForm) getSmartHomeSystem().getGUI(SettingsForm.class);
                            if (form.jComboBox1.getSelectedIndex() != -1) {
                                switch (form.jComboBox1.getSelectedIndex()) {
                                    case 0:
                                        getSmartHomeSystem().send(ARGB.COMMAND_TURN_ON);
                                        break;
                                    case 1:
                                        getSmartHomeSystem().send(RGB.COMMAND_TURN_ON);
                                        break;
                                }
                            }
                        }
                        break;
                    case MouseEvent.BUTTON2:
                        if (trayIcon.getToolTip().equals(SmartHomeSystem.ANDROID_REROUTE_BLUETOOTH)
                                || trayIcon.getToolTip().equals(SmartHomeSystem.ANDROID_REROUTE_LAN)) {
                            SmartHomeSystem.getSmartHomeSystem().send("L0");
                        } else /* if (SmartHomeSystem.getSmartHomeSystem().isHC_06Connected())*/ {
                            SettingsForm form = (SettingsForm) getSmartHomeSystem().getGUI(SettingsForm.class);
                            if (form.jComboBox1.getSelectedIndex() != -1) {
                                switch (form.jComboBox1.getSelectedIndex()) {
                                    case 0:
                                        getSmartHomeSystem().send(ARGB.COMMAND_TURN_OFF);
                                        break;
                                    case 1:
                                        getSmartHomeSystem().send(RGB.COMMAND_TURN_OFF);
                                        break;
                                }
                            }

                        }
                        break;
                    case MouseEvent.BUTTON3:
                        meniuPrincipal.setLocation((int) e.getLocationOnScreen().getX(),
                                (int) e.getLocationOnScreen().getY());
                        meniuPrincipal.setVisible(true);
                        break;
                    default:
                        break;
                }
            }

        });
        //<editor-fold desc="Aprinde lumina" defaultstate="collapsed">
        JMenuItem aprindeLumina = new JMenuItem("Aprinde lumina");

        aprindeLumina.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (meniuPrincipal.isVisible()) {
                    meniuPrincipal.setVisible(false);
                }
                if (!getSmartHomeSystem().isHC_06Connected() && !getSmartHomeSystem().isUSBConnected()) {
                    getSmartHomeSystem().send("L1");
                } else {
                    SettingsForm form = (SettingsForm) getSmartHomeSystem().getGUI(SettingsForm.class);
                    if (form.jComboBox1.getSelectedIndex() != -1) {
                        switch (form.jComboBox1.getSelectedIndex()) {
                            case 0:
                                getSmartHomeSystem().send(SmartHomeSystem.ARGB.COMMAND_TURN_ON_WHITE);
                                break;
                            case 1:
                                getSmartHomeSystem().send(RGB.COMMAND_TURN_ON);
                                break;
                        }
                    }

                }
            }
        });

        aprindeLumina.setIcon(SmartHomeSystem.resize("icon_aprindeLumina.png"));
        meniuPrincipal.add(aprindeLumina);
        //</editor-fold>

        //<editor-fold desc="Stinge lumina" defaultstate="collapsed">
        JMenuItem stingeLumina = new JMenuItem("Stinge lumina");

        stingeLumina.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (meniuPrincipal.isVisible()) {
                    meniuPrincipal.setVisible(false);
                }
                if (!getSmartHomeSystem().isHC_06Connected() && !getSmartHomeSystem().isUSBConnected()) {
                    getSmartHomeSystem().send("L0");
                } else {

                    SettingsForm form = (SettingsForm) getSmartHomeSystem().getGUI(SettingsForm.class);
                    if (form.jComboBox1.getSelectedIndex() != -1) {
                        switch (form.jComboBox1.getSelectedIndex()) {
                            case 0:
                                getSmartHomeSystem().send(SmartHomeSystem.ARGB.COMMAND_TURN_OFF);
                                break;
                            case 1:
                                getSmartHomeSystem().send(RGB.COMMAND_TURN_OFF);
                                break;
                        }
                    }
                }
            }
        });

        stingeLumina.setIcon(SmartHomeSystem.resize("icon_stingeLumina.png"));

        meniuPrincipal.add(stingeLumina);
        //</editor-fold>

        //<editor-fold desc="Conectivitate" defaultstate="collapsed">
        conectivitate = new JMenu("Conectivitate");
        conectivitate.setIcon(SmartHomeSystem.resize("conectivity.png"));
        //<editor-fold desc="Connect hc05" defaultstate="collapsed">
        JMenuItem conectareDirectaHC05 = new JMenuItem("Bluetooth");
        conectareDirectaHC05.setIcon(SmartHomeSystem.resize("btIcon.png"));
        conectareDirectaHC05.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                HC05Connector connector = (HC05Connector) (SmartHomeSystem.getSmartHomeSystem().getGUI(HC05Connector.class));
                connector.setVisible(true);
                meniuPrincipal.setVisible(false);
            }
        });
        conectivitate.add(conectareDirectaHC05);
        //</editor-fold>

        //<editor-fold desc="USB" defaultstate="collapsed">
        JMenuItem conectareUSB = new JMenuItem("USB");
        conectareUSB.setIcon(SmartHomeSystem.resize("usb.png"));
        conectareUSB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((ConnectUSBForm) getSmartHomeSystem().getGUI(ConnectUSBForm.class)).setVisible(true);
                meniuPrincipal.setVisible(false);
            }
        });
        conectivitate.add(conectareUSB);
        //</editor-fold>

        meniuPrincipal.add(conectivitate);
        //</editor-fold>

        //<editor-fold desc="Animatii" defaultstate="collapsed">
        animatii = new JMenu("Animații");
        animatiiPornire = new JMenu("Animații de pornire");
        animatiiOprire = new JMenu("Animații de stingere");
        alteAnimatii = new JMenu("Alte animații");
        animatieSteag = new JMenuItem("Animație steag");
        animatiiFastLED = new JMenu("Animații FastLED");
        animatiiFader = new JMenu("Animații fader");
        animatieScreenMirror = new JMenuItem("Screen mirror");
        createAnimation = new JMenuItem("Animație custom");

        animatieScreenMirror.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                SmartHomeSystem.getSmartHomeSystem().getGUI(ScreenMirrorSetup.class).setVisible(true);
            }
        });
        animatieScreenMirror.setIcon(SmartHomeSystem.resize("screenMirrorIcon.png"));
        animatieSteag.setIcon(SmartHomeSystem.resize("romanianFlagAnimationIcon.png"));
        animatieSteag.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                getSmartHomeSystem().send(ARGB.COMMAND_ROMANIAN_ANIMATION);
            }
        });
        alteAnimatii.setIcon(SmartHomeSystem.resize("otherAnimationIcon.png"));
        animatii.setIcon(SmartHomeSystem.resize("animationIcon.png",
                (int) animatii.getPreferredSize().getHeight() - 5,
                (int) animatii.getPreferredSize().getHeight() - 5));

        createAnimation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                getSmartHomeSystem().getGUI(AnimationMakerForm.class).setVisible(true);
            }
        });

        try {
            if (settingsFormRef.jComboBox1.getSelectedItem().equals(SettingsForm.LED_STRIP_ARGB)) {
                DefaultComboBoxModel modelAnimatiiPornire = (DefaultComboBoxModel) settingsFormRef.jComboBox2.getModel();
                for (int i = 0; i < modelAnimatiiPornire.getSize(); i++) {
                    JCheckBoxMenuItem item = new JCheckBoxMenuItem(modelAnimatiiPornire.getElementAt(i).toString());
                    item.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent ae) {
                            JCheckBoxMenuItem sursa = (JCheckBoxMenuItem) ae.getSource();
                            settingsFormRef.jComboBox2.setSelectedItem(sursa.getText());
                            getSmartHomeSystem().send(ARGB.COMMAND_TURN_ON);
                            settingsFormRef.jButton7.doClick();
                            try {
                                if (previousTurnOnAnimationCheckBox != sursa) {
                                    previousTurnOnAnimationCheckBox.setSelected(false);
                                }
                            } catch (NullPointerException ex) {
                                Logger.getLogger(Tray.class.getSimpleName()).log(Level.SEVERE, null, ex);
                            }
                            previousTurnOnAnimationCheckBox = sursa;
                        }
                    });
                    if (modelAnimatiiPornire.getSelectedItem().equals(modelAnimatiiPornire.getElementAt(i))) {
                        item.setSelected(true);
                        previousTurnOnAnimationCheckBox = item;
                    }
                    animatiiPornire.add(item);
                }
                DefaultComboBoxModel modelAnimatiiOprire = (DefaultComboBoxModel) settingsFormRef.jComboBox3.getModel();
                for (int i = 0; i < modelAnimatiiOprire.getSize(); i++) {
                    JCheckBoxMenuItem item = new JCheckBoxMenuItem(modelAnimatiiOprire.getElementAt(i).toString());
                    item.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent ae) {
                            JCheckBoxMenuItem sursa = (JCheckBoxMenuItem) ae.getSource();
                            settingsFormRef.jComboBox3.setSelectedItem(sursa.getText());
                            getSmartHomeSystem().send(ARGB.COMMAND_TURN_OFF);
                            settingsFormRef.jButton7.doClick();
                            try {
                                if (previousTurnOffAnimationCheckBox != sursa) {
                                    previousTurnOffAnimationCheckBox.setSelected(false);
                                }
                            } catch (NullPointerException ex) {
                                Logger.getLogger(Tray.class.getSimpleName()).log(Level.SEVERE, null, ex);
                            }
                            previousTurnOffAnimationCheckBox = sursa;
                        }
                    });
                    if (modelAnimatiiOprire.getSelectedItem().equals(modelAnimatiiOprire.getElementAt(i))) {
                        item.setSelected(true);
                        previousTurnOffAnimationCheckBox = item;
                    }
                    animatiiOprire.add(item);
                }
                DefaultComboBoxModel modelAnimatiiFastLED = (DefaultComboBoxModel) settingsFormRef.jComboBox6.getModel();
                for (int i = 0; i < modelAnimatiiFastLED.getSize(); i++) {
                    JCheckBoxMenuItem item = new JCheckBoxMenuItem(modelAnimatiiFastLED.getElementAt(i).toString());
                    item.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent ae) {
                            JCheckBoxMenuItem sursa = (JCheckBoxMenuItem) ae.getSource();
                            settingsFormRef.jComboBox6.setSelectedItem(sursa.getText());
                            getSmartHomeSystem().send(ARGB.COMMAND_BLENDING_ANIMATION);
                            settingsFormRef.jButton7.doClick();
                            try {
                                if (previousFastLEDCheckBox != sursa) {
                                    previousFastLEDCheckBox.setSelected(false);
                                }
                            } catch (NullPointerException ex) {
                                Logger.getLogger(Tray.class.getSimpleName()).log(Level.SEVERE, null, ex);
                            }
                            previousFastLEDCheckBox = sursa;
                        }
                    });
                    if (modelAnimatiiFastLED.getElementAt(i).equals(modelAnimatiiFastLED.getSelectedItem())) {
                        item.setSelected(true);
                        previousFastLEDCheckBox = item;
                    }
                    animatiiFastLED.add(item);
                }

                DefaultComboBoxModel modelAnimatiiFader = (DefaultComboBoxModel) settingsFormRef.jComboBox7.getModel();
                for (int i = 0; i < modelAnimatiiFader.getSize(); i++) {
                    JCheckBoxMenuItem item = new JCheckBoxMenuItem(modelAnimatiiFader.getElementAt(i).toString());
                    item.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent ae) {
                            JCheckBoxMenuItem sursa = (JCheckBoxMenuItem) ae.getSource();
                            settingsFormRef.jComboBox7.setSelectedItem(sursa.getText());
                            settingsFormRef.jButton11.doClick();
                            getSmartHomeSystem().send(ARGB.COMMAND_FADER_ANIMATION);
                            try {
                                if (previousFaderAnimationCheckBox != sursa) {
                                    previousFaderAnimationCheckBox.setSelected(false);
                                }
                            } catch (NullPointerException ex) {
                                Logger.getLogger(Tray.class.getSimpleName()).log(Level.SEVERE, null, ex);
                            }
                            previousFaderAnimationCheckBox = sursa;
                        }
                    });
                    if (modelAnimatiiFader.getElementAt(i).equals(modelAnimatiiFader.getSelectedItem())) {
                        item.setSelected(true);
                        previousFaderAnimationCheckBox = item;
                    }
                    animatiiFader.add(item);
                }

                animatiiPornire.setIcon(SmartHomeSystem.resize("lightOnAnimationsIcon.png"));
                animatiiOprire.setIcon(SmartHomeSystem.resize("lightOffAnimationsIcon.png"));
                animatiiFastLED.setIcon(SmartHomeSystem.resize("animatiiFastLEDIcon.png"));
                animatiiFader.setIcon(SmartHomeSystem.resize("fadeicon.png"));

                alteAnimatii.add(animatieSteag);
                alteAnimatii.add(animatiiFastLED);
                alteAnimatii.add(animatieScreenMirror);
                alteAnimatii.add(animatiiFader);

                animatii.add(animatiiPornire);
                animatii.add(animatiiOprire);
                animatii.add(alteAnimatii);
                animatii.add(createAnimation);
            } else if (settingsFormRef.jComboBox1.getSelectedItem().equals(SettingsForm.LED_SIMPLU)) {

            }
        } catch (NullPointerException ex) {
            Logger.getLogger(Tray.class.getSimpleName()).log(Level.SEVERE, null, ex);
        }

        meniuPrincipal.add(animatii);
        //</editor-fold>

        //<editor-fold desc="Setari" defaultstate="collapsed">
        JMenuItem setari = new JMenuItem("Setări");
        setari.setIcon(SmartHomeSystem.resize("settingsIcon.png",
                (int) setari.getPreferredSize().getHeight() - 5,
                (int) setari.getPreferredSize().getHeight() - 5));
        setari.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                SettingsForm set = (SettingsForm) SmartHomeSystem.getSmartHomeSystem().getGUI(SettingsForm.class);
                set.setVisible(true);
                meniuPrincipal.setVisible(false);
            }
        });
        meniuPrincipal.add(setari);
        //</editor-fold>

        //<editor-fold desc="Meniu principal" defaultstate="collapsed">
        JMenuItem homeMenuItem = new JMenuItem("Meniu principal");
        homeMenuItem.setIcon(SmartHomeSystem.resize("home.png"));
        homeMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                getSmartHomeSystem().getGUI(MainForm.class).setVisible(true);
                meniuPrincipal.setVisible(false);
            }
        });
        meniuPrincipal.add(homeMenuItem);
        //</editor-fold>

        //trayIcon.setPopupMenu(meniuPrincipal);
        systemTray.add(trayIcon);
        //</editor-fold>
    }

}
