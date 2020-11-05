package smarthomesystem;

//UDPClient.java
import java.net.*;
import java.io.*;
/**
 * Clasa folosita de DataHandler ca sa trimita comenzi la python
 * @author Manel
 */
class UDPClient {
    
    //<editor-fold desc="Variabile" defaultstate="collapsed">
    private byte[] send_data;
    private DatagramSocket client_socket;
    private InetAddress IPAddress;
    //</editor-fold>
    
    public void send(String message) throws IOException {
        //<editor-fold desc="body" defaultstate="collapsed">
        if (message.equals("q") || message.equals("Q")) {
            client_socket.close();
            return;
        }
        send_data = message.getBytes();

        DatagramPacket send_packet = new DatagramPacket(send_data,
                send_data.length,
                IPAddress, 5000);

        client_socket.send(send_packet);
        //</editor-fold>
    }

    public UDPClient() throws SocketException, UnknownHostException {
        //<editor-fold desc="body" defaultstate="collapsed">
        send_data = new byte[128];
        client_socket = new DatagramSocket();
        IPAddress = InetAddress.getByName("127.0.0.1");
        //</editor-fold>
    }

}



//<editor-fold desc="dump" defaultstate="collapsed">
/*
    public static void main(String args[]) throws Exception {
        
        
        byte[] send_data = new byte[1024];
        BufferedReader infromuser =
        new BufferedReader(new InputStreamReader(System.in));
        DatagramSocket client_socket = new DatagramSocket();
        InetAddress IPAddress =  InetAddress.getByName("127.0.0.1");
        while (true)
        {
        System.out.println("baga q sau Q sa iesi): ");
        String data = infromuser.readLine();
        if (data.equals("q") || data.equals("Q"))
        break;
        else
        {
        send_data = data.getBytes();
        DatagramPacket send_packet = new DatagramPacket(send_data,
        send_data.length,
        IPAddress, 5000);
        client_socket.send(send_packet);
        }
        }
        client_socket.close();
        
        UDPClient udpClient = new UDPClient();
        udpClient.send("ce faci baa");
        JFrame f=new JFrame();
        f.setSize(600, 400);
        f.setLayout(new FlowLayout());
        
        JButton b=new JButton("caca");
        JTextField ff=new JTextField();
        ff.setPreferredSize(new Dimension(150,50));
        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    udpClient.send(ff.getText());
                } catch (IOException ex) {
                    Logger.getLogger(UDPClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        f.add(b);
        f.add(ff);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }
*/
//</editor-fold>