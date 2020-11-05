/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smarthomesystem;

import auxclasses.ComponentResizer;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import smarthomesystem.SmartHomeSystem.ARGB;
import static smarthomesystem.SmartHomeSystem.getSmartHomeSystem;

/**
 *
 * @author Manel
 */
public class ScreenMirrorSetup extends javax.swing.JFrame {

    //<editor-fold desc="Variabile si definitii de clase" defaultstate="collapsed">
    JFrame recordingAreaFrame;
    JButton exitButton;
    JLabel recordingAreaFrameLocationLabel, recordingAreaFrameDimensionLabel;
    private int offX = 0, offY = 0;
    private Dimension sizeOnPress = new Dimension(0, 0);
    JButton defaultButton;
    static Dimension NEW_RECORDING_AREA_DIMENSION = new Dimension(1366, 768);
    static Point NEW_RECORDING_AREA_LOCATION = new Point(0, 0);
    boolean saveRequired = true;
    static Thread arduinoScreenMirrorSaveSender;
    static final int STATE_SENDING=0;
    static final int STATE_STANDBY=1;
                                        //T+ 1 100 45
    Pattern ARDUINO_CONFIRMATION_PATTERN=Pattern.compile("T\\+ (\\d\\d?\\d?\\d?) (\\d\\d?\\d?) (\\d\\d?\\d?\\d?) (\\d\\d?\\d?) (\\d\\d?)");
    
    int getCorrectBufferSize(){
        return (int)jSpinner3.getValue()*3+3;
    }
    
    private class ArduinoScreenMirrorApplier implements Runnable {

        @Override
        public void run() {
            //<editor-fold desc="body" defaultstate="collapsed">
            getSmartHomeSystem().send(ARGB.COMMAND_SET_SCREEN_MIRROR_PARAMS);
            int loopCounter=0;
            Pattern arduinoBufferExtractor=Pattern.compile("T\\+(\\d\\d?\\d?\\d?)");
            while(/*!getSmartHomeSystem().getHC05().newData&&*/ loopCounter<1000){
                
                App.print("Asteptam confirmarea buffer-ului de la Arduino",ScreenMirrorSetup.class);
                String in="";
                if (getSmartHomeSystem().isHC_06Connected()){
                    in=getSmartHomeSystem().getHC05().readData;
                }else if (getSmartHomeSystem().isUSBConnected()){
                    in=getSmartHomeSystem().getArduino().readData;
                }else{
                    System.out.println("Nu am de la cine sa citesc");
                }
                Matcher m=arduinoBufferExtractor.matcher(in);
                if (m.find()){
                    App.print("Am primit confirmare cu buffer de "+m.group(1), ScreenMirrorSetup.class);
                    if (Integer.valueOf(m.group(1)) == getCorrectBufferSize()) {
                        App.print("Buffer setat corect ", ScreenMirrorSetup.class);
                        saveRequired = false;
                    } else {
                        App.print(String.format("Buffer-ul din arduino este setat incorect, "
                                + "din setarile animatiei rezulta ca ar trebui un buffer de %d iar arduino"
                                + "are un buffer de", getCorrectBufferSize(), Integer.valueOf(m.group(1))), ScreenMirrorSetup.class);
                        saveRequired=true;
                    }
                    break;
                }
                try {
                    Thread.sleep(1);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ScreenMirrorSetup.class.getName()).log(Level.SEVERE, null, ex);
                }
                loopCounter++;
            }
            if (loopCounter>=1000){
                App.print("Timeout asteptare confirmare de la Arduino",ScreenMirrorSetup.class);
            }
            //</editor-fold>
        }

    }

    //</editor-fold>
    
    public ScreenMirrorSetup() {
        //<editor-fold desc="Constructor body" defaultstate="collapsed">
        initComponents();
        setLocationRelativeTo(null);
        
        //<editor-fold desc="recording area stuff" defaultstate="collapsed">
        recordingAreaFrame = new JFrame();
        recordingAreaFrame.setUndecorated(true);
        recordingAreaFrame.setOpacity(0.7f);
        recordingAreaFrame.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                JFrame f = (JFrame) e.getSource();
                offX = e.getX();
                offY = e.getY();
                sizeOnPress = f.getSize();
            }
        });
        recordingAreaFrame.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                JFrame f = (JFrame) e.getSource();
                if (sizeOnPress.equals(f.getSize())) {
                    f.setLocation(f.getX() + e.getX() - offX, f.getY() + e.getY() - offY);
                }
            }
        });
  
        ComponentResizer cr = new ComponentResizer();
        cr.registerComponent(recordingAreaFrame);
        recordingAreaFrame.setLayout(new FlowLayout());
        recordingAreaFrameDimensionLabel = new JLabel("dim");
        recordingAreaFrameLocationLabel = new JLabel("loc");
        
        exitButton = new JButton("Iesire");
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                recordingAreaFrame.dispatchEvent(new WindowEvent(recordingAreaFrame, WindowEvent.WINDOW_CLOSING));
            }
        });
        exitButton.setFocusable(false);
        exitButton.setOpaque(true);
        defaultButton=new JButton("Setari default");
        defaultButton.setFocusable(false);
        defaultButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                recordingAreaFrame.setLocation(0,0);
                recordingAreaFrame.setSize(SmartHomeSystem.getSmartHomeSystem().getScreenMirror().screen_size);
            }
        });
        recordingAreaFrame.add(defaultButton);
        recordingAreaFrame.add(recordingAreaFrameDimensionLabel);
        recordingAreaFrame.add(recordingAreaFrameLocationLabel);
        recordingAreaFrame.add(exitButton);
        recordingAreaFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        recordingAreaFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (recordingAreaFrame.getX() < 0
                        || recordingAreaFrame.getX() + recordingAreaFrame.getWidth() > SmartHomeSystem.getSmartHomeSystem().getScreenMirror().screen_size.getWidth()
                        || recordingAreaFrame.getY() < 0
                        || recordingAreaFrame.getY() + recordingAreaFrame.getHeight() > SmartHomeSystem.getSmartHomeSystem().getScreenMirror().screen_size.getHeight()) {
                    JOptionPane.showMessageDialog(null, "Fereastra e inafara ecranului");
                } else {
                    if (!recordingAreaFrame.getSize().equals(NEW_RECORDING_AREA_DIMENSION)
                            || !recordingAreaFrame.getLocation().equals(NEW_RECORDING_AREA_LOCATION)) {
                        int x = JOptionPane.showOptionDialog(null,
                                String.format("Locatie (%d,%d)\nDimensiune (%d,%d)",
                                        recordingAreaFrame.getX(),
                                        recordingAreaFrame.getY(),
                                        recordingAreaFrame.getWidth(),
                                        recordingAreaFrame.getHeight()),
                                "Confirmi noua suprafata de inregistrare ?",
                                JOptionPane.OK_OPTION,
                                JOptionPane.INFORMATION_MESSAGE, null, new Object[]{"Da", "Nu"}, "da");
                        if (x == JOptionPane.OK_OPTION) {
                            saveRequired=true;
                            setLocationLabel(recordingAreaFrame.getLocation());
                            setDimensionLabel(recordingAreaFrame.getSize());
                        } else {

                        }
                    }
                    JFrame f = (JFrame) e.getSource();
                    f.dispose();
                }

            }
        });
        recordingAreaFrame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                JFrame sursa = (JFrame) e.getSource();
                recordingAreaFrameDimensionLabel.setText(String.format("Dimensiune (%d,%d)",
                        sursa.getWidth(), sursa.getHeight()));
            }
            @Override
            public void componentMoved(ComponentEvent e){
                JFrame sursa = (JFrame) e.getSource();
                recordingAreaFrameLocationLabel.setText(String.format("Locatie (%d,%d)",
                        sursa.getX(), sursa.getY()));
            }
        });
        //</editor-fold>
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (SmartHomeSystem.getSmartHomeSystem().getScreenMirror().isRunning()) {
                    if (JOptionPane.showOptionDialog(ScreenMirrorSetup.this,
                            "Animatia se va opri daca iesi din aceasta fereastra si Arduino va intra in modul normal", "Inchizi?",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Da", "Nu"}, "Da") == JOptionPane.YES_OPTION) {
                        SmartHomeSystem.getSmartHomeSystem().getScreenMirror().stop();
                        setUI(STATE_STANDBY);
                        setVisible(false);
                    }
                } else {
                    setVisible(false);
                }
            }
        });
        Settings.applyPreviousSettingsToScreenMirrorSetup(this);
        recordingAreaFrame.setSize(NEW_RECORDING_AREA_DIMENSION);
        recordingAreaFrame.setLocation(NEW_RECORDING_AREA_LOCATION);
        recordingAreaFrameDimensionLabel.setText(jLabel5.getText());
        recordingAreaFrameLocationLabel.setText(jLabel4.getText());
        //</editor-fold>
    }
    
    void setUI(int state) {
        //<editor-fold desc="body" defaultstate="collapsed">
        if (state == STATE_SENDING) {
            jButton3.setEnabled(false);
            jSpinner1.setEnabled(false);
            jSpinner2.setEnabled(false);
            jSpinner4.setEnabled(false);
            jSlider1.setEnabled(false);
            jSpinner3.setEnabled(false);
            jButton4.setEnabled(false);
            jButton1.setEnabled(false);
            jSpinner5.setEnabled(false);
            jCheckBox1.setEnabled(false);
            jButton2.setEnabled(true);
            
        } else if (state == STATE_STANDBY) {
            jButton3.setEnabled(true);
            jSpinner1.setEnabled(true);
            jSpinner2.setEnabled(true);
            jSpinner4.setEnabled(true);
            jSlider1.setEnabled(true);
            jSpinner3.setEnabled(true);
            jButton4.setEnabled(true);
            jButton1.setEnabled(true);
            jButton2.setEnabled(true);
            jSpinner5.setEnabled(true);
            jCheckBox1.setEnabled(true);
        }
        //</editor-fold>
    }
    
    @Override
    public void setVisible(boolean state) {
        //<editor-fold desc="body" defaultstate="collapsed">
        super.setVisible(state);
        if (state){
            toFront();
        }
        /*if (state) {
            SmartHomeSystem.getSmartHomeSystem().getHC05().setRaportorEnabled(false);
        } else {
            SmartHomeSystem.getSmartHomeSystem().getHC05().setRaportorEnabled(true);
        }*/
        //</editor-fold>
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jSpinner1 = new javax.swing.JSpinner();
        jLabel7 = new javax.swing.JLabel();
        jSpinner2 = new javax.swing.JSpinner();
        jLabel8 = new javax.swing.JLabel();
        jSlider1 = new javax.swing.JSlider();
        jLabel9 = new javax.swing.JLabel();
        jSpinner3 = new javax.swing.JSpinner();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jSpinner4 = new javax.swing.JSpinner();
        jCheckBox1 = new javax.swing.JCheckBox();
        jLabel11 = new javax.swing.JLabel();
        jSpinner5 = new javax.swing.JSpinner();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("ScreenMirror-Ecran de setup");
        setResizable(false);

        jLabel3.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel3.setText("Aria de înregistrare :");

        jLabel4.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel4.setText("Locație (0,0)");

        jLabel5.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel5.setText("Dimensiune (1366,768)");

        jButton3.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jButton3.setText("Modifică aria de înregistrare");
        jButton3.setFocusable(false);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel6.setText("Spațierea dintre pixeli :");

        jSpinner1.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jSpinner1.setModel(new javax.swing.SpinnerNumberModel(5, 1, 30, 1));
        jSpinner1.setFocusable(false);
        jSpinner1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner1StateChanged(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel7.setText("Iterații :");

        jSpinner2.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jSpinner2.setModel(new javax.swing.SpinnerNumberModel(28, 1, 90, 1));
        jSpinner2.setFocusable(false);
        jSpinner2.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner2StateChanged(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel8.setText("Intensitate :");

        jSlider1.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jSlider1.setValue(100);
        jSlider1.setFocusable(false);
        jSlider1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider1StateChanged(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel9.setText("COLOR_PACKETS_COUNT :");

        jSpinner3.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jSpinner3.setModel(new javax.swing.SpinnerNumberModel(14, 1, 100, 1));
        jSpinner3.setFocusable(false);
        jSpinner3.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner3StateChanged(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jButton1.setText("START");
        jButton1.setFocusable(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jButton2.setText("STOP");
        jButton2.setFocusable(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel10.setText("Scara :");

        jButton4.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jButton4.setText("SAVE");
        jButton4.setFocusable(false);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jSpinner4.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jSpinner4.setModel(new javax.swing.SpinnerNumberModel(1, 1, 50, 1));
        jSpinner4.setFocusable(false);
        jSpinner4.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner4StateChanged(evt);
            }
        });

        jCheckBox1.setText("Oglindit");
        jCheckBox1.setFocusable(false);
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel11.setText("Offset :");

        jSpinner5.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jSpinner5.setModel(new javax.swing.SpinnerNumberModel(0, 0, 100, 1));
        jSpinner5.setFocusable(false);
        jSpinner5.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner5StateChanged(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel3)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel5)
                                            .addComponent(jLabel4)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel6)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel8)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel9)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jSpinner3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(0, 1, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSpinner2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSpinner4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jCheckBox1)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSpinner5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jSpinner2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jSpinner4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel8)
                    .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jSpinner3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jSpinner5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    
    
    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        //<editor-fold desc="body" defaulstate="collapsed">
        try {
            Settings.savePreviousDataOfScreenMirrorSetup(this);
        } catch (IOException ex) {
            Logger.getLogger(ScreenMirrorSetup.class.getName()).log(Level.SEVERE, null, ex);
        }
        SmartHomeSystem.getSmartHomeSystem().getScreenMirror().setIterations((int) jSpinner2.getValue());
        SmartHomeSystem.getSmartHomeSystem().getScreenMirror().setRecordingArea(NEW_RECORDING_AREA_DIMENSION, NEW_RECORDING_AREA_LOCATION);
        SmartHomeSystem.getSmartHomeSystem().getScreenMirror().setSpacing((int) jSpinner1.getValue());
        SmartHomeSystem.getSmartHomeSystem().getScreenMirror().setColorPacketsCount((int) jSpinner3.getValue());
        try {
            arduinoScreenMirrorSaveSender.interrupt();
        }catch(Exception ex){
            
        }
        arduinoScreenMirrorSaveSender=new Thread(new ArduinoScreenMirrorApplier());
        arduinoScreenMirrorSaveSender.start();
        //</editor-fold>
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        //<editor-fold desc="body" defaultstate="collapsed">        
        recordingAreaFrame.setLocation(NEW_RECORDING_AREA_LOCATION);
        recordingAreaFrame.setSize(NEW_RECORDING_AREA_DIMENSION);
        recordingAreaFrame.setVisible(true);
        //</editor-fold>
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        //<editor-fold desc="body" defaultstate="collapsed">
        if (saveRequired){
            JOptionPane.showMessageDialog(null, "Salveaza setarile inainte de a incepe animatia,\n"
                    + " daca aceasta eroare persista inseamna ca a fost o eroare in comunicarea cu arduino, verifica "
                    + "consola pentru detalii");
        } else {
            //SmartHomeSystem.getSmartHomeSystem().getHC05().setRaportorEnabled(false);
            setUI(STATE_SENDING);
            SmartHomeSystem.getSmartHomeSystem().getScreenMirror().start();
        }
        //</editor-fold>
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        //<editor-fold desc="body" defaultstate="collapsed">
        SmartHomeSystem.getSmartHomeSystem().getScreenMirror().stop();
        setUI(STATE_STANDBY);
        //</editor-fold>
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jSpinner1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner1StateChanged
        saveRequired=true;
    }//GEN-LAST:event_jSpinner1StateChanged

    private void jSpinner2StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner2StateChanged
        saveRequired=true;
    }//GEN-LAST:event_jSpinner2StateChanged

    private void jSpinner4StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner4StateChanged
        saveRequired=true;
    }//GEN-LAST:event_jSpinner4StateChanged

    private void jSlider1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider1StateChanged
        saveRequired=true;
    }//GEN-LAST:event_jSlider1StateChanged

    private void jSpinner3StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner3StateChanged
        saveRequired=true;
    }//GEN-LAST:event_jSpinner3StateChanged

    private void jSpinner5StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner5StateChanged
        saveRequired=true;
    }//GEN-LAST:event_jSpinner5StateChanged

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        saveRequired=true;
    }//GEN-LAST:event_jCheckBox1ActionPerformed
    void setLocationLabel(Point location){
        //<editor-fold desc="body" defaultstate="collapsed">
        NEW_RECORDING_AREA_LOCATION=location;
        jLabel4.setText("Locație ("+(int)location.getX()+","+(int)location.getY()+")");
        //</editor-fold>
    }
    void setDimensionLabel(Dimension dimension){
        //<editor-fold desc="body" defaultstate="collapsed">
        NEW_RECORDING_AREA_DIMENSION=dimension;
        jLabel5.setText("Dimensiune ("+(int)dimension.getWidth()+","+(int)dimension.getHeight()+")");
        //</editor-fold>
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    public javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel3;
    public javax.swing.JLabel jLabel4;
    public javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    public javax.swing.JSlider jSlider1;
    public javax.swing.JSpinner jSpinner1;
    public javax.swing.JSpinner jSpinner2;
    public javax.swing.JSpinner jSpinner3;
    public javax.swing.JSpinner jSpinner4;
    public javax.swing.JSpinner jSpinner5;
    // End of variables declaration//GEN-END:variables
}
