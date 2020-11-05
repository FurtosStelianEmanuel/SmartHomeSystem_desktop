package smarthomesystem;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.bluetooth.BluetoothStateException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Clasa principala pentru aplicatia desktop SmartHomeSystem, de aici sunt
 * initializate si pornite toate sistemele de baza ale programului
 *
 * @author Manel
 */
public class SmartHomeSystem {

    //<editor-fold desc="Variabile" defaultstate="collapsed">
    private SPPServer sppAndroidConnection;
    private static DataHandler handler;
    private SocketConnection socketConnection;
    private static Tray tray;
    private static SplashScreen splashScreen;
    private static SmartHomeSystem selfReference;
    private HC05 hc05;
    private HC05Connector hC05Connector;
    private SettingsForm settingsForm;
    private ColorPickerForm colorPicker;
    public static String path;
    public static final String ANDROID_REROUTE_BLUETOOTH = "Conexiune prin Bluetooth";
    public static final String ANDROID_REROUTE_LAN = "Conexiune prin LAN";
    public static final String ARDUINO_DIRECT_CONNECTION = "Conexiune prin USB";
    public static final String RESEND_ERROR_TOKEN = "inv";
    private ScreenMirror screenMirror;
    private ScreenMirrorSetup screenMirrorSetup;
    private MainForm mainForm;
    private ConnectUSBForm usbConnectForm;
    private Arduino arduino;
    private AnimationMakerForm animationMakerForm;
    private LEDAnimationSender animationSender;

    public enum ARGB {
        COMMAND_TURN_OFF, COMMAND_TURN_ON, COMMAND_ROMANIAN_ANIMATION, COMMAND_BLENDING_ANIMATION, COMMAND_TURN_ON_WHITE,
        COMMAND_SET_SCREEN_MIRROR_PARAMS, COMMAND_SET_COLOR, COMMAND_FADER_ANIMATION
    }

    public enum RGB {
        COMMAND_TURN_ON, COMMAND_TURN_OFF, COMMAND_SET_COLOR
    }

//</editor-fold>
    public SmartHomeSystem() {
        //<editor-fold desc="Constructor body" defaultstate="collapsed">
        try {
            path = Settings.get_path("SmartHomeSystem.jar");
        } catch (URISyntaxException ex) {
            Logger.getLogger(SmartHomeSystem.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Nu am putut obtine path-ul jar-ului SmartHomeSystem.jar");
        }
        handler = new DataHandler();
        sppAndroidConnection = new SPPServer();
        try {
            socketConnection = new SocketConnection();
        } catch (IOException ex) {
            App.print("Nu am putut crea socketConnection poate serverul inca ruleaza "
                    + ex.toString(), SmartHomeSystem.class);
        }
        try {
            sppAndroidConnection.printLocalDevice();
        } catch (BluetoothStateException ex) {
            App.print("Nu am putut detecta device local, sigur e bluetooth pornit?", SmartHomeSystem.class);
        }
        hc05 = new HC05();
        selfReference = this;
        screenMirror = new ScreenMirror(30);
        animationSender = new LEDAnimationSender();
        //</editor-fold>
    }

    //<editor-fold desc="Functii utilitare si de status" defaultstate="collapsed">
    static ImageIcon resize(ImageIcon icon, Dimension dimension) {
        return new ImageIcon(icon.getImage().getScaledInstance((int) dimension.getWidth(), (int) dimension.getHeight(), Image.SCALE_SMOOTH));
    }

    static ImageIcon resize(String image, int width, int height) {
        //<editor-fold desc="body" defaultstate="collapsed">
        return new ImageIcon((new ImageIcon(SmartHomeSystem.class.getResource("/resurse/" + image)).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH)));
        //</editor-fold>
    }

    static ImageIcon resize(String image) {
        //<editor-fold desc="body" defaultstate="collapsed">
        Dimension dimensiune = SystemTray.getSystemTray().getTrayIconSize();
        return new ImageIcon(new ImageIcon(SmartHomeSystem.class.getResource("/resurse/" + image)).getImage()
                .getScaledInstance((int) dimensiune.getWidth(),
                        (int) dimensiune.getHeight(),
                        Image.SCALE_SMOOTH));
        //</editor-fold>
    }

    public boolean isHC_06Connected() {
        //<editor-fold desc="body" defaultstate="collapsed">
        return tray.trayIcon.getToolTip().equals("Conexiune prin " + hc05.name);
        //</editor-fold>
    }

    public boolean isUSBConnected() {
        return tray.trayIcon.getToolTip().equals(ARDUINO_DIRECT_CONNECTION);
    }

    public void connectionEstablished(Class c) {
        //<editor-fold desc="body" defaultstate="collapsed">
        tray.trayIcon.setImage(tray.iconConnected);
        if (c == SPPServer.class) {
            tray.trayIcon.setToolTip(ANDROID_REROUTE_BLUETOOTH);
        } else if (c == SocketConnection.class) {
            tray.trayIcon.setToolTip(ANDROID_REROUTE_LAN);
        } else if (c == HC05Connector.class) {
            tray.trayIcon.setToolTip(Tray.CONNECTION_ESTABLISHED_TOOL_TIP + hc05.name);
        } else if (c == Arduino.class) {
            tray.trayIcon.setToolTip(ARDUINO_DIRECT_CONNECTION);
        }
        //</editor-fold>
    }

    public void connectionLost(Class c) {
        //<editor-fold desc="body" defaultstate="collapsed">
        if (App.CONNECTION_TYPE == App.ANDROID_MASTER) {
            tray.trayIcon.getImage().flush();
            tray.trayIcon.setImage(tray.iconDisconnected);
            tray.trayIcon.setToolTip(Tray.CONNECTION_LOST_TOOL_TIP);
            tray.trayIcon.displayMessage("Smart Home System",
                    "Am pierdut conexiunea cu telefonul",
                    TrayIcon.MessageType.INFO);
        } else if (App.CONNECTION_TYPE == App.PC_MASTER) {
            if (c.equals(SPPServer.class) || c.equals(SocketConnection.class)) {
                tray.trayIcon.displayMessage("Smart Home System",
                        "Am pierdut conexiunea prin " + c.getSimpleName() + " dar " + getHC05().name + " e inca activ",
                        TrayIcon.MessageType.INFO);
            } else if (c.equals(HC05.class)) {
                tray.trayIcon.displayMessage("Smart Home System",
                        "Am pierdut conexiunea cu " + getHC05().name + ", nicio alta conexiune disponibila spre Arduino",
                        TrayIcon.MessageType.INFO);
                tray.trayIcon.getImage().flush();
                tray.trayIcon.setImage(tray.iconDisconnected);
                tray.trayIcon.setToolTip(Tray.CONNECTION_LOST_TOOL_TIP);
            }
        }
        //</editor-fold>
    }
    //</editor-fold>

    //<editor-fold desc="Getters" defaultstate="collapsed">
    public AnimationMakerPanel getAnimationMakerPanel() {
        return animationMakerForm.animationMakerPanel1;
    }

    public LEDAnimationSender getAnimationSender() {
        return animationSender;
    }

    public Arduino getArduino() {
        return arduino;
    }

    public void setArduino(Arduino arduino) {
        this.arduino = arduino;
    }

    public static byte getByte(int value) {
        value -= 128;
        if (value > 127) {
            value = 127;
        } else if (value < -128) {
            value = -128;
        }
        return (byte) (value);
    }

    public byte[] getCommand(ARGB identifier) {
        switch (identifier) {
            case COMMAND_TURN_OFF: {
                byte byteData[] = new byte[7];
                byteData[0] = 'j';
                byteData[1] = '-';
                byteData[2] = getByte((int) settingsForm.jSpinner6.getValue());
                byteData[3] = getByte((int) settingsForm.jSpinner5.getValue());
                byteData[4] = getByte(settingsForm.jComboBox3.getSelectedIndex() + 1);
                byteData[5] = getByte((int) settingsForm.jSpinner4.getValue());
                byte suma = byteData[0];
                for (int i = 1; i < 6; i++) {
                    suma += byteData[i];
                }
                byteData[6] = suma;
                return byteData;
            }
            case COMMAND_TURN_ON: {
                byte[] byteData = new byte[10];
                byteData[0] = 'j';
                byteData[1] = '+';
                byteData[2] = getByte(settingsForm.jButton4.getBackground().getRed());
                byteData[3] = getByte(settingsForm.jButton4.getBackground().getGreen());
                byteData[4] = getByte(settingsForm.jButton4.getBackground().getBlue());
                byteData[5] = getByte(settingsForm.jComboBox2.getSelectedIndex() + 1);
                byteData[6] = getByte((int) settingsForm.jSpinner1.getValue());
                byteData[7] = getByte((int) settingsForm.jSpinner2.getValue());
                byteData[8] = getByte((int) settingsForm.jSpinner3.getValue());
                byte suma = byteData[0];
                for (int i = 1; i < 9; i++) {
                    suma += byteData[i];
                }
                byteData[9] = suma;
                return byteData;
            }
            case COMMAND_ROMANIAN_ANIMATION: {
                byte byteData[] = new byte[9];
                byteData[0] = 'j';
                byteData[1] = '0';
                byteData[2] = getByte((int) settingsForm.jSpinner8.getValue());
                byteData[3] = getByte((int) settingsForm.jSpinner9.getValue());
                byteData[4] = getByte((int) settingsForm.jSpinner7.getValue());
                byteData[5] = getByte((int) settingsForm.jComboBox4.getSelectedIndex());
                byteData[6] = getByte((int) settingsForm.jComboBox5.getSelectedIndex());
                byteData[7] = getByte((int) settingsForm.jSpinner10.getValue());
                byte suma = byteData[0];
                for (int i = 1; i < 8; i++) {
                    suma += byteData[i];
                }
                byteData[8] = suma;
                return byteData;
            }
            case COMMAND_BLENDING_ANIMATION: {
                byte byteData[] = new byte[6];
                byteData[0] = 'j';
                byteData[1] = '1';
                byteData[2] = getByte((int) settingsForm.jSpinner11.getValue());
                byteData[3] = getByte(settingsForm.jComboBox6.getSelectedIndex() + 1);
                byteData[4] = getByte((int) settingsForm.jSpinner12.getValue());
                byte suma = byteData[0];
                for (int i = 1; i < 5; i++) {
                    suma += byteData[i];
                }
                byteData[5] = suma;
                return byteData;
            }
            case COMMAND_TURN_ON_WHITE: {
                byte[] byteData = new byte[10];
                byteData[0] = 'j';
                byteData[1] = '+';
                byteData[2] = getByte(255);
                byteData[3] = getByte(255);
                byteData[4] = getByte(255);
                byteData[5] = getByte(settingsForm.jComboBox2.getSelectedIndex() + 1);
                byteData[6] = getByte((int) settingsForm.jSpinner1.getValue());
                byteData[7] = getByte((int) settingsForm.jSpinner2.getValue());
                byteData[8] = getByte((int) settingsForm.jSpinner3.getValue());
                byte suma = byteData[0];
                for (int i = 1; i < 9; i++) {
                    suma += byteData[i];
                }
                byteData[9] = suma;
                return byteData;
            }
            case COMMAND_SET_SCREEN_MIRROR_PARAMS: {
                byte byteData[] = new byte[7];
                byteData[0] = 'T';
                byteData[1] = '-';
                byteData[2] = getByte((int) screenMirrorSetup.jSpinner4.getValue());
                byteData[3] = getByte(screenMirrorSetup.jSlider1.getValue());
                byteData[4] = getByte((int) screenMirrorSetup.jSpinner5.getValue());
                byteData[5] = getByte(screenMirrorSetup.jCheckBox1.isSelected() ? 1 : 0);
                byte suma = byteData[0];
                for (int i = 1; i < 6; i++) {
                    suma += byteData[i];
                }
                byteData[6] = suma;
                return byteData;
            }
            case COMMAND_FADER_ANIMATION: {
                if (settingsForm.jComboBox7.getSelectedIndex() == 0) {
                    byte[] byteData = new byte[11];
                    byteData[0] = 'j';
                    byteData[1] = '3';
                    byteData[2] = getByte(settingsForm.jComboBox7.getSelectedIndex());
                    byteData[3] = getByte((int) settingsForm.jSpinner13.getValue());
                    byteData[4] = getByte((int) settingsForm.jSpinner14.getValue());

                    byteData[5] = getByte(settingsForm.jButton12.getBackground().getRed());
                    byteData[6] = getByte(settingsForm.jButton12.getBackground().getGreen());
                    byteData[7] = getByte(settingsForm.jButton12.getBackground().getBlue());

                    byteData[8] = getByte(settingsForm.colorSliderPanel1.slider.getValue());
                    byteData[9] = getByte(settingsForm.colorSliderPanel2.slider.getValue());

                    byte suma = byteData[0];
                    for (int i = 1; i < byteData.length - 1; i++) {
                        suma += byteData[i];
                    }
                    byteData[byteData.length - 1] = suma;

                    return byteData;
                } else if (settingsForm.jComboBox7.getSelectedIndex() == 1) {
                    byte[] byteData = new byte[9];
                    byteData[0] = 'j';
                    byteData[1] = '3';
                    byteData[2] = getByte(1);

                    byteData[3] = getByte((int) settingsForm.jSpinner14.getValue());

                    byteData[4] = getByte(settingsForm.jButton12.getBackground().getRed());
                    byteData[5] = getByte(settingsForm.jButton12.getBackground().getGreen());
                    byteData[6] = getByte(settingsForm.jButton12.getBackground().getBlue());

                    int dimensiune = settingsForm.colorSliderPanel3.slider.getValue();
                    if (dimensiune % 2 == 0) {
                        dimensiune--;
                    }
                    byteData[7] = getByte(dimensiune);

                    byte suma = byteData[0];

                    for (int i = 1; i < byteData.length - 1; i++) {
                        suma += byteData[i];
                    }
                    byteData[byteData.length - 1] = suma;

                    return byteData;
                }
            }
            default:
                App.print("Comanda necunoascuta " + identifier, SmartHomeSystem.class);
                break;
        }
        return new byte[]{};
    }

    public byte[] getCommand(RGB identifier) {
        switch (identifier) {
            case COMMAND_TURN_ON: {
                byte byteData[] = new byte[2];
                byteData[0] = 'L';
                byteData[1] = '1';
                return byteData;
            }
            case COMMAND_TURN_OFF: {
                byte byteData[] = new byte[2];
                byteData[0] = 'L';
                byteData[1] = '0';
                return byteData;
            }
            default:
                break;
        }
        return new byte[]{};
    }

    static SmartHomeSystem getSmartHomeSystem() {
        //<editor-fold desc="body" defaultstate="collapsed">
        return selfReference;
        //</editor-fold>
    }

    public ScreenMirror getScreenMirror() {
        //<editor-fold desc="body" defaultstate="collapsed">
        return screenMirror;
        //</editor-fold>
    }

    public HC05 getHC05() {
        //<editor-fold desc="body" defaultstate="collapsed">
        return hc05;
        //</editor-fold>
    }

    static DataHandler getDefaultDataHandler() {
        //<editor-fold desc="body" defaultstate="collapsed">
        return handler;
        //</editor-fold>
    }

    static Tray getTray() {
        //<editor-fold desc="body" defaultstate="collapsed">
        return tray;
        //</editor-fold>
    }

    public JFrame getGUI(Class c) {
        //<editor-fold desc="body" defaultstate="collapsed">
        if (c == HC05Connector.class) {
            return hC05Connector;
        } else if (c == SettingsForm.class) {
            return settingsForm;
        } else if (c == ColorPickerForm.class) {
            return colorPicker;
        } else if (c == ScreenMirrorSetup.class) {
            return screenMirrorSetup;
        } else if (c == MainForm.class) {
            return mainForm;
        } else if (c == ConnectUSBForm.class) {
            return usbConnectForm;
        } else if (c == AnimationMakerForm.class) {
            return animationMakerForm;
        }
        return null;
        //</editor-fold>
    }

    public SPPServer getSppConnection() {
        return sppAndroidConnection;
    }
    //</editor-fold>

    //<editor-fold desc="Functii apelate la start-up de initializare exceptand constructorul" defaultstate="collapsed">
    public static void setLookAndFeel() {
        //<editor-fold desc="body" defaultstate="collapsed">
        try {
            /*LookAndFeelInfo[] plafs = UIManager.getInstalledLookAndFeels();
            for (LookAndFeelInfo info : plafs) {
                System.out.println(info.getClassName());
            }*/
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            //UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(SmartHomeSystem.class.getName()).log(Level.SEVERE, null, ex);
        }
        //</editor-fold>
    }

    void initGuis() {
        //<editor-fold desc="body" defaultstate="collapsed">
        hC05Connector = new HC05Connector();
        settingsForm = new SettingsForm();
        colorPicker = new ColorPickerForm();
        screenMirrorSetup = new ScreenMirrorSetup();
        mainForm = new MainForm();
        usbConnectForm = new ConnectUSBForm();
        animationMakerForm = new AnimationMakerForm();
        //</editor-fold>
    }

    public static void waitForSplashScreen() {
        //<editor-fold desc="body" defaultstate="collapsed">
        if (splashScreen == null) {
            splashScreen = new SplashScreen();
        }
        splashScreen.start();
        //</editor-fold>
    }

    public void startSPPServer() throws IOException {
        //<editor-fold desc="body" defaultstate="collapsed">
        sppAndroidConnection.start();
        //</editor-fold>
    }

    public void startSocketServer() throws IOException {
        //<editor-fold desc="body" defaultstate="collapsed">
        socketConnection.startServer();
        //</editor-fold>
    }

    void runInTray() {
        //<editor-fold desc="body" defaultstate="collapsed">
        try {
            tray = new Tray();
        } catch (AWTException ex) {
            Logger.getLogger(SmartHomeSystem.class.getName()).log(Level.SEVERE, null, ex);
        }
        //</editor-fold>
    }
    //</editor-fold>

    //<editor-fold desc="Functii IO" defaultstate="collapsed">
    public void send(String data) {
        //<editor-fold desc="body" defaultstate="collapsed">
        if (!App.containsNewLineSeparator(data)) {
            data += "\n";
        }
        if (tray.trayIcon.getToolTip().equals(ANDROID_REROUTE_BLUETOOTH)) {
            try {
                sppAndroidConnection.outputStream.write(data.getBytes());
            } catch (IOException ex) {
                Logger.getLogger(SmartHomeSystem.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (tray.trayIcon.getToolTip().equals(ANDROID_REROUTE_LAN)) {
            try {
                socketConnection.outputStream.write(data.getBytes());
            } catch (IOException ex) {
                Logger.getLogger(SmartHomeSystem.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (isHC_06Connected()) {
            try {
                App.print("Trimitem " + data.replace("\n", ""), SmartHomeSystem.class);

                hc05.write(data);

            } catch (IOException ex) {
                Logger.getLogger(SmartHomeSystem.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (isUSBConnected()) {
            arduino.write(data);
        }
        //</editor-fold>
    }

    int incerc = 0;

    /**
     *
     * @param data
     */
    public void send(byte data[]) {
        //<editor-fold desc="body" defaultstate="collapsed">
        if (tray.trayIcon.getToolTip().equals(ANDROID_REROUTE_BLUETOOTH)) {

            try {
                System.out.println("Trimit "+Arrays.toString(data));
                //sppAndroidConnection.outputStream.write(data);
                sppAndroidConnection.outputStream.write(data);
            } catch (IOException ex) {
                Logger.getLogger(SmartHomeSystem.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (tray.trayIcon.getToolTip().equals(ANDROID_REROUTE_LAN)) {
            try {
                socketConnection.outputStream.write(data);
            } catch (IOException ex) {
                Logger.getLogger(SmartHomeSystem.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (isHC_06Connected()) {
            try {
                if (getScreenMirror().isRunning()) {
                    int x = JOptionPane.showOptionDialog(null, "Comenzile conventionale nu functioneaza in acest mod, vrei sa iesi din acest mod si sa executi comanda " + Arrays.toString(data) + " ?",
                            "Arduino este in modul turbo",
                            JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new Object[]{"Da, executa comanda " + Arrays.toString(data), "Ramai in modul curent"},
                            "Da, executa comanda " + Arrays.toString(data));
                    if (x == JOptionPane.OK_OPTION) {
                        getScreenMirror().stop();
                        Thread.sleep(100);
                        screenMirrorSetup.setUI(ScreenMirrorSetup.STATE_STANDBY);
                        screenMirrorSetup.setVisible(false);
                        hc05.write(data);
                    } else {

                    }
                } else {
                    hc05.write(data);
                }
            } catch (IOException | InterruptedException ex) {
                Logger.getLogger(SmartHomeSystem.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (tray.trayIcon.getToolTip().equals(ARDUINO_DIRECT_CONNECTION)) {

            arduino.write(data);
        }
        //</editor-fold>
    }

    public void send(ARGB identifier) {
        send(getCommand(identifier));
    }

    public void send(RGB identifier) {
        send(getCommand(identifier));
    }

    public void send(ARGB identifier, Color color) {
        //<editor-fold desc="body" defaultstate="collapsed">
        if (identifier == ARGB.COMMAND_SET_COLOR) {
            byte dataBytes[] = new byte[5];
            dataBytes[0] = 'j';
            dataBytes[1] = getByte(color.getRed());
            dataBytes[2] = getByte(color.getGreen());
            dataBytes[3] = getByte(color.getBlue());
            byte suma = dataBytes[0];
            for (int i = 1; i < 4; i++) {
                suma += dataBytes[i];
            }
            dataBytes[4] = suma;
            send(dataBytes);
        }
        //</editor-fold>
    }

    public void send(RGB identifier, Color color) {
        if (identifier == RGB.COMMAND_SET_COLOR) {
            byte dataBytes[] = new byte[4];
            dataBytes[0] = 'G';
            dataBytes[1] = getByte(color.getRed());
            dataBytes[2] = getByte(color.getGreen());
            dataBytes[3] = getByte(color.getBlue());
            send(dataBytes);
        }
    }

    public String read() throws IOException {
        //<editor-fold desc="body" defaultstate="collapsed">
        String result = "nimic";
        if (tray.trayIcon.getToolTip().equals(ANDROID_REROUTE_BLUETOOTH)) {
            result = sppAndroidConnection.inputStream.readLine();
        } else if (tray.trayIcon.getToolTip().equals(ANDROID_REROUTE_LAN)) {
            result = socketConnection.reader.readLine();
        } else if (isHC_06Connected()) {
            result = hc05.inputStream.readLine();
        }
        return result;
        //</editor-fold>
    }
    //</editor-fold>

    public static void main(String[] args) throws IOException {

        //<editor-fold desc="Main body" defaultstate="collapsed">
        SmartHomeSystem.waitForSplashScreen();
        SmartHomeSystem.setLookAndFeel();
        SmartHomeSystem smartHomeSystem = new SmartHomeSystem();
        smartHomeSystem.initGuis();
        smartHomeSystem.runInTray();
        smartHomeSystem.startSPPServer();
        smartHomeSystem.startSocketServer();
        //</editor-fold>
    }
    /**
     * As of Java 1.7, you can add the following line to remove the application
     * bar from the taskbar:
     *
     * frame.setType(Type.UTILITY);
     */
}

//<editor-fold desc="dump" defaultstate="collapsed">
/*
        JFrame f=new JFrame();
        f.setSize(600, 400);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLayout(new FlowLayout());
        JButton b=new JButton();
        JButton c= new JButton();
        c.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                 try {
                    smartHomeSystem.sppAndroidConnection.outputStream.write(">f0\n".getBytes());
                } catch (IOException ex) {
                    Logger.getLogger(SmartHomeSystem.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    smartHomeSystem.sppAndroidConnection.outputStream.write(">f1\n".getBytes());
                } catch (IOException ex) {
                    Logger.getLogger(SmartHomeSystem.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });
        JSlider slider =new JSlider();
        slider.setMaximum(255);
        slider.addChangeListener(new ChangeListener() {
            Timer t = new Timer(50, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    try {
                        smartHomeSystem.sppAndroidConnection.outputStream.write((">y " + slider.getValue() + " 0 0\n").getBytes());
                    } catch (IOException ex) {
                        Logger.getLogger(SmartHomeSystem.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            @Override
            public void stateChanged(ChangeEvent ce) {
                if (!t.isRunning()){
                    t.start();
                }
            }
        });
        f.add(slider);
        f.add(b);
        f.add(c);
        f.setVisible(true);*/
//</editor-fold>
