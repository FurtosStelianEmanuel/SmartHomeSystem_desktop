package smarthomesystem;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Manel
 */
public class SocketConnection {
   
    //<editor-fold desc="Variabile" defaultstate="collapsed">
    private ServerSocket socket;
    private Socket clientSocket;
    BufferedInputStream in;
    OutputStream outputStream;
    DataHandler handler;
    BufferedReader reader;
    
    boolean run = true;
    private boolean connected=false;
    //</editor-fold>
    
    void connectionEstablished(){
        connected=true;
        SmartHomeSystem.getSmartHomeSystem().connectionEstablished(SocketConnection.class);
    }
    
    void connectionLost(){
        connected=false;
        SmartHomeSystem.getSmartHomeSystem().connectionLost(SocketConnection.class);
    }
    public SocketConnection() throws IOException {
        //<editor-fold desc="body" defaultstate="collapsed">
        socket = new ServerSocket(153);
        handler=SmartHomeSystem.getDefaultDataHandler();
        if (handler == null) {
            throw new NullPointerException("DefaultDataHandler null, verifica"
                    + " initializarea inainte sa creezi un nou SocketConnection");
        }
        //</editor-fold>
    }

    void restartServer() throws IOException {
        //<editor-fold desc="body" defaultstate="collapsed">
        try {
            socket.close();
        } catch (Exception ex) {
            App.print("Nu am putut inchide socket-ul "+ex.toString(), SocketConnection.class);
        }
        try{
            clientSocket.close();
        }catch(Exception ex){
            App.print("Nu am putut inchide clientSocket "+ex.toString(),SocketConnection.class);
        }
        socket = new ServerSocket(153);
        handler = SmartHomeSystem.getDefaultDataHandler();
        if (handler == null) {
            throw new NullPointerException("DefaultDataHandler null, verifica"
                    + " initializarea inainte sa creezi un nou SocketConnection");
        }
        startServer();
        //</editor-fold>
    }
    public void startServer() throws IOException {
        //<editor-fold desc="body" defaultstate="collapsed">
        App.print("S-a pornit serverul auxiliar si se asteapta conexiuni"
                ,SocketConnection.class);
        App.print("Detalii conexiune :",SocketConnection.class);
        InetAddress inetAddress = InetAddress.getLocalHost();
        String ip = inetAddress.getHostAddress();
        App.print("Adresa IP: " + ip, SocketConnection.class);
        if (ip.equals("127.0.0.1") || ip.equals("localhost")) {
            App.print("Adresa IP indica faptul ca nu exista conexiune la internet",SocketConnection.class);
        }
        new Thread(){
            @Override
            public void run() {
                try {
                    clientSocket = socket.accept();
                    in = new BufferedInputStream(clientSocket.getInputStream());
                    reader = new BufferedReader(
                            new InputStreamReader(in, StandardCharsets.UTF_8));
                    outputStream = clientSocket.getOutputStream();
                    read();
                } catch (IOException ex) {
                    Logger.getLogger(SocketConnection.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }.start();
        
        
        
        //</editor-fold>
    }
    
    private void read() {
        //<editor-fold desc="body" defaultstate="collapsed">
        run=true;
        new Thread() {
            @Override
            public void run() {
                App.print("Socket conectat, se asteapta date",SocketConnection.class);
                while (run) {
                    try {
                   
                        String greeting = reader.readLine();

                        if (greeting != null) {
                            if (greeting.equals(App.COMMAND_IP_ALIVE)) {
                                //System.out.println("Am primit ping");
                                
                            } else {
                                if (greeting.charAt(0) == 'q') {
                                    App.print("AM PRIMTI Q",SocketConnection.class);

                                    App.print("Trimitem",SocketConnection.class);
                                    outputStream.write(("Na, raspuns ba\n").getBytes());

                                } else {
                                    int cod=handler.dataReceived(greeting);
                                    if (cod==1){
                                        outputStream.write("na, raspuns de la socket\n".getBytes());
                                    }
                                    if (!connected)
                                        connectionEstablished();
                                    //outputStream.write("gay\n".getBytes());
                                }
                            }
                        } else {
                            App.print("Am citit null -> Repornim server", SocketConnection.class);
                            run = false;
                            if (connected) {
                                connectionLost();
                            }
                            restartServer();
                        }
                    } catch (IOException | NullPointerException ex) {
                        App.print("Eroare: " + ex.toString(), SocketConnection.class);
                        if (connected) {
                            connectionLost();
                        }
                        if (run) {
                            run = false;
                            App.print("Am dat de o eroare -> Repornim server", SocketConnection.class);
                            try {
                                restartServer();
                            } catch (IOException ex1) {
                                App.print("Eroare la repornirea serverului", SocketConnection.class);
                            }
                        }
                        Logger.getLogger(SocketConnection.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }
        }.start();
        //</editor-fold>
    }
}

//<editor-fold desc="dump" defaultstate="collapsed">
/*
    public static void main(String[] args) throws IOException {

        SocketConnection socketConnection = new SocketConnection(null);
        socketConnection.startServer();
        socketConnection.read();
  String greeting = in.readLine();
        if ("hello server".equals(greeting)) {
        out.println("hello client");
        }
        else {
        out.println("unrecognised greeting");
        }
         
    }
 */
//</editor-fold>

