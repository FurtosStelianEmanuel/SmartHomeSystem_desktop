package smarthomesystem;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class HC05 {

    //<editor-fold desc="Variabile" defaultstate="collapsed">
    private static boolean scanFinished = false;
    RemoteDevice hc05device;
    static StreamConnection streamConnection;
    OutputStream os;
    InputStream in;
    public BufferedReader inputStream;
    String hc05Url;
    String name, address;
    String lastCommand = "";
    byte lastByteCommand[];
    boolean enabledRaportor = true;
    boolean runRaportor = false;
    static Thread raportorThread;
    public String readData = "";
    boolean newData = false;
    private static final int STRING_COMMAND = 1;
    private static final int BYTE_COMMAND = 2;
    private static int LAST_SENT_COMMAND_TYPE = STRING_COMMAND;
    //</editor-fold>

    /**
     * Executia functiei dureaza cateva secunde, pana cautarea este finalizata
     *
     * @return {@link RemoteDevice[]}
     * @throws javax.bluetooth.BluetoothStateException
     * @throws java.lang.InterruptedException
     */
    public static List<RemoteDevice> getAvailableDevices() throws BluetoothStateException, InterruptedException {
        //<editor-fold desc="body" defaultstate="collapsed">
        List<RemoteDevice> list = new ArrayList<>();
        LocalDevice.getLocalDevice().getDiscoveryAgent().startInquiry(DiscoveryAgent.GIAC, new DiscoveryListener() {
            @Override
            public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
                //String name = btDevice.getFriendlyName(false);
                //System.out.format("%s (%s)\n", name, btDevice.getBluetoothAddress());
                list.add(btDevice);
            }

            @Override
            public void inquiryCompleted(int discType) {
                scanFinished = true;
            }

            @Override
            public void serviceSearchCompleted(int transID, int respCode) {
            }

            @Override
            public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
            }
        });
        while (!scanFinished) {
            Thread.sleep(500);
        }
        return list;
        //</editor-fold>
    }

    public void setTableContent(HC05Connector connectorFrame) throws BluetoothStateException, InterruptedException {
        //<editor-fold desc="body" defaultstate="collapsed">
        DefaultTableModel model = (DefaultTableModel) connectorFrame.jTable1.getModel();
        for (int i = model.getRowCount() - 1; i >= 0; i--) {
            model.removeRow(i);
        }
        List<RemoteDevice> listaDevices = new ArrayList<>();
        LocalDevice.getLocalDevice().getDiscoveryAgent().startInquiry(DiscoveryAgent.GIAC, new DiscoveryListener() {
            @Override
            public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
                try {
                    //String name = btDevice.getFriendlyName(false);
                    //System.out.format("%s (%s)\n", name, btDevice.getBluetoothAddress());
                    listaDevices.add(btDevice);
                    model.addRow(new Object[]{btDevice.getFriendlyName(false), btDevice.getBluetoothAddress()});
                } catch (IOException ex) {
                    Logger.getLogger(HC05.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            @Override
            public void inquiryCompleted(int discType) {
                scanFinished = true;
            }

            @Override
            public void serviceSearchCompleted(int transID, int respCode) {
            }

            @Override
            public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
            }
        });
        while (!scanFinished) {
            Thread.sleep(500);
        }
        try {
            Settings.storeBluetoothConnections(listaDevices);
        } catch (IOException ex) {
            App.print("Nu am putut salva fisierul cu conexiuni " + ex.toString(), HC05.class);
        }
        //</editor-fold>
    }

    public void showAvailableDevices() throws BluetoothStateException, InterruptedException {
        //<editor-fold desc="body" defaultstate="collapsed">
        scanFinished = false;

        LocalDevice.getLocalDevice().getDiscoveryAgent().startInquiry(DiscoveryAgent.GIAC, new DiscoveryListener() {
            @Override
            public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
                try {
                    String name = btDevice.getFriendlyName(false);
                    System.out.format("%s (%s)\n", name, btDevice.getBluetoothAddress());
                    if (name.matches("HC.*")) {
                        hc05device = btDevice;
                        System.out.println("got it!");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void inquiryCompleted(int discType) {
                scanFinished = true;
            }

            @Override
            public void serviceSearchCompleted(int transID, int respCode) {
            }

            @Override
            public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
            }
        });
        while (!scanFinished) {
            //this is easier to understand (for me) as the thread stuff examples from bluecove
            Thread.sleep(500);
        }

        //search for services:
        UUID uuid = new UUID(0x1101); //scan for btspp://... services (as HC-05 offers it)
        UUID[] searchUuidSet = new UUID[]{uuid};
        int[] attrIDs = new int[]{
            0x0100 // service name
        };
        scanFinished = false;
        LocalDevice.getLocalDevice().getDiscoveryAgent().searchServices(attrIDs, searchUuidSet,
                hc05device, new DiscoveryListener() {
            @Override
            public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
            }

            @Override
            public void inquiryCompleted(int discType) {
            }

            @Override
            public void serviceSearchCompleted(int transID, int respCode) {
                scanFinished = true;
            }

            @Override
            public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
                for (int i = 0; i < servRecord.length; i++) {
                    hc05Url = servRecord[i].getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
                    if (hc05Url != null) {
                        break; //take the first one
                    }
                }
            }
        });

        while (!scanFinished) {
            Thread.sleep(500);
        }

        System.out.println(hc05device.getBluetoothAddress());
        System.out.println(hc05Url);
        //</editor-fold>
    }

    public void write(String data) throws IOException {
        //<editor-fold desc="body" defaultstate="collapsed">
        LAST_SENT_COMMAND_TYPE = STRING_COMMAND;
        write(data.getBytes());
        lastCommand = data;
        //</editor-fold>
    }

    public void write(byte data[]) throws IOException {
        //<editor-fold desc="body" defaultstate="collapsed">
        LAST_SENT_COMMAND_TYPE = BYTE_COMMAND;
        lastByteCommand = data;
        os.write(data);
        //</editor-fold>
    }

    public void setRaportorEnabled(boolean state) {
        //<editor-fold desc="body" defaultstate="collapsed">
        runRaportor = state;
        if (!state) {
            raportorThread.interrupt();
            App.print("Raportor oprit", HC05.class);
            try {
                Thread.sleep(150);
            } catch (InterruptedException ex) {
                Logger.getLogger(HC05.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            if (!raportorThread.isAlive()) {
                raportorThread = new Thread(new RaportorFormat());
                raportorThread.start();
                App.print("Raportor repornit", HC05.class);
            } else {
                App.print("Nu am pornit o alta instanta de raportorThread pentru ca avem deja una activa", HC05.class);
            }
        }
        //</editor-fold>
    }

    public class RaportorFormat implements Runnable {

        @Override
        public void run() {
            //<editor-fold desc="body" defaultstate="collapsed">
            App.print("S-a pornit raportorul de erori primite de la " + name, HC05.class);
            runRaportor = true;
            while (runRaportor) {
                try {
                    newData = false;
                    String dataIn = inputStream.readLine();
                    if (dataIn == null) {
                        runRaportor = false;
                        SmartHomeSystem.getSmartHomeSystem().connectionLost(HC05.class);
                        break;
                    }
                    /*
                    SettingsForm stfrm = (SettingsForm) SmartHomeSystem.getSmartHomeSystem().getGUI(SettingsForm.class);

                    synchronized (stfrm) {
                        stfrm.notify();
                    }
                    */
                    newData = true;
                    readData = dataIn;
                    if (!dataIn.equals("6")) {
                        App.print("Am primit de la Arduino : " + dataIn, HC05.class);
                        App.print("In bytes : " + Arrays.toString(readData.getBytes()), HC05.class);
                    }
                    if (dataIn.contains(SmartHomeSystem.RESEND_ERROR_TOKEN)) {
                        if (LAST_SENT_COMMAND_TYPE == STRING_COMMAND) {
                            App.print("A fost detectata o eroare de trimitere, se retrimite comanda " + lastCommand,
                                    HC05.class);
                            write(lastCommand);
                        } else if (LAST_SENT_COMMAND_TYPE == BYTE_COMMAND) {
                            App.print("A fost detectata o eroare de trimitere, se retrimite comanda " + Arrays.toString(lastByteCommand),
                                    HC05.class);
                            write(lastByteCommand);
                        }

                        inputStream.reset();
                    }
                } catch (IOException ex) {
                    //Logger.getLogger(HC05.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            App.print("S-a oprit raportorul de erori", HC05.class);
            //</editor-fold>
        }
    }

    /**
     * Incearca sa se conecteza la adresa specificata
     *
     * @param address fara spatii de ex 98D321F48A41
     * @param name Foloseste doar pentru {@link #name}, nu afecteaza conexiunea
     * @throws IOException
     */
    public void connect(String name, String address) throws IOException {
        //<editor-fold desc="body" defaultstate="collapsed">
        try {
            streamConnection = (StreamConnection) Connector.open("btspp://" + address + ":1;authenticate=false;encrypt=false;master=false");
            os = streamConnection.openOutputStream();
            in = streamConnection.openInputStream();
            inputStream = new BufferedReader(new InputStreamReader(in));
            this.name = name;
            this.address = address;
            write("T0\n");
            raportorThread = new Thread(new RaportorFormat());
            raportorThread.start();
        } catch (IOException ex) {
            throw ex;
        }
        //</editor-fold>
    }

    public static String getConnectionURL(String address) {
        return "btspp://" + address + ":1;authenticate=false;encrypt=false;master=false";
    }

    /**
     * Porneste o fereastra cu o interfata de a trimite date spre modulul
     * bluetooth, adresa la care se conecteaza este 98D321F48A41 , adica modulul
     * ce e in sistemul lui Teo adresa de la modulul meu este 98D311F81C78
     *
     * @throws Exception
     */
    public void go() throws Exception {
        //<editor-fold desc="body" defaultstate="collapsed">
        //98D311F81C78
        //
        streamConnection = (StreamConnection) Connector.open("btspp://98D321F48A41:1;authenticate=false;encrypt=false;master=false");
        os = streamConnection.openOutputStream();

        JFrame f = new JFrame();
        f.setSize(600, 400);
        f.setLayout(new FlowLayout());
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(300, 50));
        field.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    os.write((field.getText() + "\n").getBytes());
                } catch (IOException ex) {
                    Logger.getLogger(HC05.class.getName()).log(Level.SEVERE, null, ex);
                }
                field.setText("");
            }
        });
        f.add(field);

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
        //os.write("f1".getBytes()); //just send '1' to the device

        //os.close();
        // streamConnection.close();
        //</editor-fold>
    }

}
