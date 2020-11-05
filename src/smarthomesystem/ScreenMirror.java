/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smarthomesystem;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Timer;
import smarthomesystem.SmartHomeSystem.ARGB;
import static smarthomesystem.SmartHomeSystem.getSmartHomeSystem;
/**
 *
 * @author manel
 */
public class ScreenMirror {

    //<editor-fold desc="Variabile" defaultstate="collapsed">
    public Dimension screen_size;
    int iterations = -1;
    private Robot screen_recorder;
    private int spacing = 5;
    int COLOR_PACKETS_COUNT = 14;
    static Timer animationTimer;
    Rectangle recordingArea;
    //</editor-fold>

    public ScreenMirror(int iterations) {
        //<editor-fold desc="constructor body" defaultstate="collapsed">
        screen_size = Toolkit.getDefaultToolkit().getScreenSize();
        this.iterations = iterations;
        try {
            screen_recorder = new Robot();
        } catch (AWTException ex) {
            System.err.println("Can't create Robot instance");
            System.exit(0);
        }
        recordingArea=new Rectangle(screen_size);
        recordingArea.setLocation(0,0);
        //</editor-fold>
    }

    private BufferedImage recordScreen() {
        //<editor-fold desc="body" defaultstate="collapsed">
        Rectangle screenRect = recordingArea;
        BufferedImage capture = screen_recorder.createScreenCapture(screenRect);
        return capture;
        //</editor-fold>
    }

    public void setRecordingArea(Dimension dimension,Point location){
        //<editor-fold desc="body" defaultstate="collapsed">
        recordingArea.setSize(dimension);
        recordingArea.setLocation(location);
        //</editor-fold>
    }
    public void setColorPacketsCount(int COLOR_PACKETS_COUNT){
        this.COLOR_PACKETS_COUNT=COLOR_PACKETS_COUNT;
    }
    public void setSpacing(int spacing) {
        //<editor-fold desc="body" defaultstate="collapsed">
        this.spacing = spacing;
        //</editor-fold>
    }

    public void setIterations(int iteratii) {
        //<editor-fold desc="body" defaultstate="collapsed">
        this.iterations = iteratii;
        //</editor-fold>
    }

    public boolean isRunning() {
        //<editor-fold desc="body" defaultstate="collapsed">
        if (animationTimer != null) {
            return animationTimer.isRunning();
        }
        return false;
        //</editor-fold>
    }

    /**
     *
     * @param x1 {@link Integer} start.x
     * @param y1 {@link Integer} start.y
     * @param x2  {@link Integer} end.x
     * @param y2    {@link Integer} end.y
     * @param image {@link BufferedImage}
     * @return Media de culori din portiunea specificata
     */
    private Color getAverage(int x1, int y1, int x2, int y2, BufferedImage image) {
        //<editor-fold desc="body" defaultstate="collapsed">
        int ram = 0;
        int gam = 0;
        int bam = 0;
        int counter = 0;
        for (int i = x1; i < x2; i += spacing) {
            for (int j = y1; j < y2; j += spacing) {
                int clr = image.getRGB(i, j);
                int red = (clr & 0x00ff0000) >> 16;
                int green = (clr & 0x0000ff00) >> 8;
                int blue = clr & 0x000000ff;
                ram += red;
                gam += green;
                bam += blue;
                counter++;
            }
        }
        return new Color(ram / counter, gam / counter, bam / counter);
        //</editor-fold>
    }
    //<editor-fold desc="cod vechi pt functia splitImage">
    /*Color ar[] = new Color[iterations];
        for (int i = 0; i < iterations; i++) {
            ar[i] = getAverage(i * ((int) screen_size.getWidth() / iterations), 0, (i + 1) * ((int) screen_size.getWidth() / iterations), (int) screen_size.getHeight(), capturedImage);
        }
        return ar;*/
    //</editor-fold>
    private Color[] splitImage(BufferedImage capturedImage) {
        //<editor-fold desc="body" defaultstate="collapsed">
        Color ar[] = new Color[iterations];
        for (int i = 0, x1 = 0, y1 = 0, y2 = (int) (recordingArea.getHeight());
                i < ar.length; i++, x1 += recordingArea.getWidth() / iterations) {
            ar[i] = getAverage(x1, y1, (int) (x1 + recordingArea.getWidth() / iterations), y2, capturedImage);
        }
        return ar;
        //</editor-fold>
    }

    private Color[] splitImage() {
        //<editor-fold desc="body" defaultstate="collapsed">
        return splitImage(recordScreen());
        //</editor-fold>
    }

    public void start() {
        //<editor-fold desc="body" defaultstate="collapsed">
        SmartHomeSystem.getSmartHomeSystem().send("T1");
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            Logger.getLogger(ScreenMirror.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        animationTimer = new Timer(0, new ActionListener() {
            int i = 1;

            byte getRed(Color c) {
                int red = c.getRed();
                red -= 128;
                return (byte) red;
            }

            byte getGreen(Color c) {
                int green = c.getGreen();
                green -= 128;
                return (byte) green;
            }

            byte getBlue(Color c) {
                int blue = c.getBlue();
                blue -= 128;
                return (byte) blue;
            }
            byte b = -128;

            @Override
            public void actionPerformed(ActionEvent ae) {

                Color c[] = splitImage();
                Color culoriEcran[] = new Color[c.length + 1];
                for (int j = 1; j < culoriEcran.length; j++) {
                    culoriEcran[j] = c[j - 1];
                }
                boolean gol = true;
                int bufferSize = COLOR_PACKETS_COUNT * 3 + 3;
                byte send[] = new byte[bufferSize];
                send[0] = '<';

                send[1] = (byte) (i);
                int sendCounter = 2;
                int s = 0;
                for (int j = sendCounter; j < bufferSize - 1; j++) {
                    send[j] = -128;
                }
                for (int j = i; j < i + COLOR_PACKETS_COUNT && i < culoriEcran.length - COLOR_PACKETS_COUNT; j++) {
                    byte red = getRed(culoriEcran[j]);
                    byte green = getGreen(culoriEcran[j]);
                    byte blue = getBlue(culoriEcran[j]);
                    send[sendCounter++] = red;
                    send[sendCounter++] = green;
                    send[sendCounter++] = blue;
                    gol = false;
                    s += red + green + blue;
                }
                if (gol) {
                    for (int j = i; j < culoriEcran.length; j++) {
                        byte red = getRed(culoriEcran[j]);
                        byte green = getGreen(culoriEcran[j]);
                        byte blue = getBlue(culoriEcran[j]);
                        send[sendCounter++] = red;
                        send[sendCounter++] = green;
                        send[sendCounter++] = blue;
                        gol = false;
                        s += red + green + blue;
                    }
                }
                gol = false;
                for (int j = 1; j < bufferSize - 2; j++) {
                    if (send[j] == '<' || send[j] == '>') {
                        send[j] = '=';
                    }
                }
                send[bufferSize - 1] = '>';
                if (!gol) {
                    try {
                        //SmartHomeSystem.getSmartHomeSystem().send(send);
                        if (getSmartHomeSystem().isHC_06Connected()){
                            getSmartHomeSystem().getHC05().os.write(send);
                        }else if (getSmartHomeSystem().isUSBConnected()){
                            getSmartHomeSystem().getArduino().write(send);
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(ScreenMirror.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    i += COLOR_PACKETS_COUNT;
                    if (i >= iterations) {
                        i = 1;
                    }
                }
            }
        });
        animationTimer.start();
        //</editor-fold>
    }

    public void stop() {
        //<editor-fold desc="body" defaultstate="collapsed">
        try {
            animationTimer.stop();
            try {
                Thread.sleep(250);
            } catch (InterruptedException ex) {
                Logger.getLogger(ScreenMirror.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (NullPointerException ex) {

        }

        for (int i = 0; i < 5; i++) {
            SmartHomeSystem.getSmartHomeSystem().send("T0");
            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {
                Logger.getLogger(ScreenMirror.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        for (int i=0;i<2;i++){
            SmartHomeSystem.getSmartHomeSystem().send(ARGB.COMMAND_TURN_OFF);
        }
        //</editor-fold>
    }
}

//<editor-fold desc="dump" defaultstate="collapsed">
//    @Deprecated
//    public static void main(String[] args) throws SerialPortException, Exception {
//        //<editor-fold desc="main body" defaultstate="collapsed">
////        ScreenMirror mir=new ScreenMirror(30);
////        modul=new HC05();
////        modul.go();
////        JFrame f = new JFrame();
////        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
////        f.addWindowListener(new java.awt.event.WindowAdapter() {
////            @Override
////            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
////                if (JOptionPane.showConfirmDialog(f,
////                        "Are you sure you want to close this window?", "Close Window?",
////                        JOptionPane.YES_NO_OPTION,
////                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
////                    try {
////                        modul.in.close();
////                        modul.inputStream.close();
////                        modul.os.close();
////                        modul.streamConnection.close();
////                        
////                        System.exit(0);
////                    } catch (IOException | NullPointerException ex) {
////                        Logger.getLogger(ScreenMirror.class.getName()).log(Level.SEVERE, null, ex);
////                    }
////                }
////            }
////        });
////        
////        
////        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
////        f.setLayout(new FlowLayout());
////        f.setUndecorated(false);
////        f.setSize((int)mir.screen_size.getWidth(),35);
////        f.setLocation((int)mir.screen_size.getWidth()/2-f.getWidth()/2,(int) mir.screen_size.getHeight()/2-f.getHeight()/2);
////        f.setAlwaysOnTop(true);
////        
////        JButton butoane[]=new JButton[mir.iterations];
////        for (int i=0;i<mir.iterations;i++)
////        {
////            JButton j=new JButton();  
////            j.setBorder(null);
////            j.setBorderPainted(false);
////            j.setBackground(Color.yellow);
////            //j.setPreferredSize(new Dimension((int)f.getWidth()/mir.iterations-5,20));
////            j.setName(i+"");
////            j.addMouseListener(new MouseAdapter() {
////                @Override
////                public void mousePressed(MouseEvent e) {
////                    System.out.println(((JButton) e.getSource()).getName());
////                }
////            });
////            j.setPreferredSize(new Dimension(20, 900));
////            butoane[i] = j;
////            f.add(j);
////        }
////        //f.setVisible(true);
////        SerialPort port = new SerialPort("COM4");
////        /*port.openPort();
////        port.setParams(SerialPort.BAUDRATE_115200,
////                SerialPort.DATABITS_8,
////                
////                SerialPort.STOPBITS_1,
////                SerialPort.PARITY_NONE);*/
////        try {
////            Thread.sleep(3000);
////        } catch (InterruptedException ex) {
////            Logger.getLogger(ScreenMirror.class.getName()).log(Level.SEVERE, null, ex);
////        }
////        
////     
////        /*new Thread(){
////            @Override
////            public void run(){
////                try {
////                    String t=port.readString();
////                    
////                    System.out.println(t);
////                } catch (SerialPortException ex) {
////                    Logger.getLogger(ScreenMirror.class.getName()).log(Level.SEVERE, null, ex);
////                }
////            }
////        }.start();*/
////
////        Timer timer = new Timer(0, new ActionListener() {
////            int i = 1;
////            int pas = 15;
////            byte getRed(Color c){
////                int red=c.getRed();
////                red-=128;
////                return (byte)red;
////            }
////            byte getGreen(Color c){
////                int green=c.getGreen();
////                green-=128;
////                return (byte)green;
////            }
////            byte getBlue(Color c){
////                int blue = c.getBlue();
////                blue-=128;
////                return (byte)blue;
////            }
////            @Override
////            public void actionPerformed(ActionEvent ae) {
////                
////                Color c[] = mir.splitImage(mir.recordScreen());
////                Color culoriEcran[]=new Color[c.length+1];
////                for (int i=1;i<culoriEcran.length;i++){
////                    culoriEcran[i]=c[i-1];
////                }
////                /*String deTrimis = "jg " + i + " ";
////                boolean gol = true;
////                for (int j = i; j < i + pas && i < mir.iterations - pas; j++) {
////                    int culoare = c[j].getRGB();
////                    StringBuilder builder = new StringBuilder((Integer.toHexString(culoare)));
////                    builder.delete(0, 2);
////                    deTrimis += builder.toString() + " ";
////                    gol = false;
////                }
////                if (gol) {
////                    for (int j = i; j < mir.iterations; j++) {
////                        gol = false;
////                        int culoare = c[j].getRGB();
////                        StringBuilder builder = new StringBuilder((Integer.toHexString(culoare)));
////                        builder.delete(0, 2);
////                        deTrimis += builder.toString() + " ";
////                    }
////                }
////                */
////                boolean gol = true; 
////                byte send[]=new byte[pas*3+4];
////                
////                send[0]='j';
////                send[1]='b';
////                send[2]=(byte)(i);
////                int sendCounter=3;
////                int s=0;
////                for (int j = i; j < i + pas && i < culoriEcran.length - pas; j++) {
////                    byte red=getRed(culoriEcran[j]);
////                    byte green=getGreen(culoriEcran[j]);
////                    byte blue=getBlue(culoriEcran[j]);
////                    send[sendCounter++]=red;
////                    send[sendCounter++]=green;
////                    send[sendCounter++]=blue;
////                    gol = false;
////                    s+=red+green+blue;
////                }
////                if (gol) {
////                    for (int j = i; j < culoriEcran.length; j++) {
////                        byte red=getRed(culoriEcran[j]);
////                        byte green=getGreen(culoriEcran[j]);
////                        byte blue=getBlue(culoriEcran[j]);
////                        send[sendCounter++]=red;
////                        send[sendCounter++]=green;
////                        send[sendCounter++]=blue;
////                        gol = false;
////                        s+=red+green+blue;
////                    }
////                }
////                
////                send[sendCounter] = ' ';
////               
////                if (!gol) {
////                    try {
////                       // if (gata) {
////                            //System.out.println(Arrays.toString(send));
////                            modul.os.write(send);
////                            //port.writeBytes(send);
////                            
////    //  gata = false;
////                        //}
//////System.out.println("cu i "+i+" "+Arrays.toString(send));
////                    } catch (IOException ex) {
////                        Logger.getLogger(ScreenMirror.class.getName()).log(Level.SEVERE, null, ex);
////                    }
////                }
////                /*try {
////                    if (!gol) {
////                        //System.out.println(deTrimis);
////                        //port.writeBytes((deTrimis + "\n").getBytes());
////                        modul.os.write((byte)2);
////                    }
////
////                } catch (IOEx0eption ex) {
////                    Logger.getLogger(ScreenMirror.class.getName()).log(Level.SEVERE, null, ex);
////                }*/
////
////                i += pas;
////
////                if (i > mir.iterations) {
////                    i = 1;
////                }
////                for (int j = 0; j < mir.iterations; j++) {
////                    butoane[j].setBackground(c[j]);
////                }
////            }
////        });
////        timer.start();
////        
////        
////        /*byte[] bar = new byte[3];
////        for (byte car = -128; car < 126; car++) {
////            bar[0] = car;
////            bar[1]=64;
////            bar[2]=127;
////            System.out.println(Arrays.toString(bar));
////            modul.os.write(bar);
////            Thread.sleep(50);
////        }*/
////        f.requestFocus();
////        f.setVisible(true);
//        //</editor-fold>
//
//    }
//</editor-fold>