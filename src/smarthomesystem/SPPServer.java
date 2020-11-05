package smarthomesystem;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.*;

public class SPPServer {

    //<editor-fold desc="Variabile">
    private int serverRestartCount = 0;
    private boolean AUTO_RESTART = true;
    public BufferedReader inputStream;
    public OutputStream outputStream;
    private StreamConnectionNotifier streamConnNotifier;
    private Thread inputThread;
    Thread serverThread;
    DataHandler handler;
    static StreamConnection connection;
    private RemoteDevice remoteDevice;
    InputStream is = null;
    DataInputStream dis = null;
    byte[] buf = new byte[256];
    //</editor-fold>

    public SPPServer() {
        //<editor-fold desc="body" defaultstate="collapsed">
        handler = SmartHomeSystem.getDefaultDataHandler();
        if (handler == null) {
            throw new NullPointerException("DataHander null, verifica initializarea inainte sa creezi"
                    + " un nou SPPServer");
        }
        //</editor-fold>
    }

    @Deprecated
    /**
     * Dureaza prea mult sa returneze valoare..
     */
    public String getName() {
        try {
            return remoteDevice.getFriendlyName(true);
        } catch (IOException ex) {
            Logger.getLogger(SPPServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Nume indisponibil";
    }

    void connectionLost() {
        //<editor-fold desc="body" defaultstate="collapsed">
        App.print("Conexiune pierduta", SPPServer.class);
        SmartHomeSystem.getSmartHomeSystem().connectionLost(SPPServer.class);
        //</editor-fold>
    }

    void connectionEstablished() {
        //<editor-fold desc="body" defaultstate="collapsed">
        App.print("InputStream deschis si activ", SPPServer.class);
        App.print("OutputStream " + outputStream.toString(), SPPServer.class);
        App.print("Se trimite ping-ul de confirmare a conexiunii ", SPPServer.class);
        SmartHomeSystem.getSmartHomeSystem().connectionEstablished(SPPServer.class);
        //</editor-fold>
    }

    private void startCommunication() throws IOException {
        //<editor-fold desc="body" defaultstate="collapsed">
        inputThread = new Thread() {
            @Override
            public void run() {
                try {
                    int n;
                    while ((n = dis.read(buf)) != -1) {
                        StringBuilder b = new StringBuilder();
                        for (int i = 0; i < n; i++) {
                            b.append((char) buf[i]);
                        }
                        int cod = handler.dataReceived(b.toString(), buf);

                        /*
                        if (cod==1){
                            App.print("Trimitem", SPPServer.class);
                            outputStream.write("dap, esti gay\n".getBytes());
                        }
                         */
                        //outputStream.write("gay\n".getBytes());
                    }
                    connectionLost();
                    if (AUTO_RESTART) {
                        streamConnNotifier.close();
                        startServer();
                    } else {
                        App.print("AUTORESTART=false", SPPServer.class);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(SPPServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        inputThread.start();
        connectionEstablished();
        try {
            outputStream.write("spp-1\n".getBytes());

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        //</editor-fold>
    }

    private void startServer() throws IOException {
        //<editor-fold desc="body" defaultstate="collapsed">
        if (serverThread != null) {
            serverThread.interrupt();
        }
        serverThread = new Thread() {
            @Override
            public void run() {
                try {
                    UUID uuid = new UUID("1101", true);
                    String connectionString = "btspp://localhost:" + uuid + ";name=BluetoothServer" + serverRestartCount;
                    try {
                        streamConnNotifier = (StreamConnectionNotifier) Connector.open(connectionString);
                    } catch (javax.bluetooth.BluetoothStateException ex) {
                        App.print("Nu avem bluetooth", SPPServer.class);
                        return;
                    }
                    App.print("Serverul SPP a " + (serverRestartCount == 0 ? "" : "re") + "pornit, se asteapta conexiuni..."
                            + (serverRestartCount == 0 ? "" : ""/*"\npentru a inceta repornirea server-ului trimite string-ul '0451' "*/),
                            SPPServer.class);
                    connection = streamConnNotifier.acceptAndOpen();
                    remoteDevice = RemoteDevice.getRemoteDevice(connection);
                    InputStream inStream = connection.openInputStream();
                    dis = new DataInputStream(inStream);
                    inputStream = new BufferedReader(new InputStreamReader(inStream));

                    App.print("InputStream initializat", SPPServer.class);
                    outputStream = connection.openOutputStream();
                    App.print("OutputStream initializat", SPPServer.class);

                    startCommunication();
                    serverRestartCount++;
                } catch (IOException ex) {
                    Logger.getLogger(SPPServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        serverThread.start();
        //</editor-fold>
    }

    public void start() throws IOException {
        //<editor-fold desc="body" defaultstate="collapsed">
        startServer();
        //</editor-fold>
    }

    void printLocalDevice() throws BluetoothStateException {
        //<editor-fold desc="body" defaultstate="collapsed">
        LocalDevice localDevice = LocalDevice.getLocalDevice();
        App.print("Adresa server: " + localDevice.getBluetoothAddress(), SPPServer.class);
        App.print("Nume server: " + localDevice.getFriendlyName(), SPPServer.class);
        //</editor-fold>
    }

}

//<editor-fold desc="dump" defaultstate="collapsed">
/*    
public static void main(String[] args) throws IOException {
        
        BluetoothServer sampleSPPServer = new BluetoothServer();
        sampleSPPServer.printLocalDevice();
        sampleSPPServer.startServer();
        
         
        //String loca=Character.toString(x)+(char)App.KEY_ESCAPE;
        
        //System.out.println((int)x);
        //sampleSPPServer.mouseRobot.keyPress(KeyEvent.VK_BACKSPACE);
        
    }*/
//</editor-fold>
