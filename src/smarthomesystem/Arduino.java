/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smarthomesystem;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortList;
import static smarthomesystem.SmartHomeSystem.getSmartHomeSystem;

/**
 *
 * @author Manel
 */
public class Arduino {

    private String comPortName;
    private SerialPort port;
    private StringBuilder message;
    private String lastStringCommand = "";
    private byte[] lastByteCommand = new byte[]{};
    boolean lastCommandWasString = false;
    public boolean readFirstLine = false;
    String readData = "";
    ConnectionEventCallback callback;

    public interface ConnectionEventCallback {

        public void connectionLost();

        public void connectionEstablished();

        public void connectionFailed();
    }

    public Arduino(String comPortName) {
        this.comPortName = comPortName;
        message = new StringBuilder();
    }

    public void close() throws SerialPortException {
        port.closePort();
    }

    public void connect(ConnectionEventCallback callback) throws NullPointerException {
        this.callback = callback;
        if (comPortName == null) {
            throw new NullPointerException("comPortName null");
        }
        port = new SerialPort(comPortName);
        try {
            port.openPort();
            port.setParams(SerialPort.BAUDRATE_115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            port.addEventListener(new SerialPortEventListener() {
                @Override
                public void serialEvent(SerialPortEvent serialPortEvent) {
                    if (serialPortEvent.isRXCHAR() && serialPortEvent.getEventValue() > 0) {
                        try {
                            byte buffer[] = port.readBytes();
                            for (byte b : buffer) {
                                if ((b == '\r' || b == '\n') && message.length() > 0) {
                                    receivedData(message.toString());
                                    message.setLength(0);
                                } else {
                                    message.append((char) b);
                                }
                            }
                        } catch (SerialPortException ex) {
                            Logger.getLogger(Arduino.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            });
            callback.connectionEstablished();
        } catch (SerialPortException ex) {
            callback.connectionFailed();
            Logger.getLogger(Arduino.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void write(String data) {
        try {
            if (!port.writeBytes(data.getBytes())) {
                App.print("Nu am putut trimite comanda " + data, Arduino.class);
            } else {
                lastCommandWasString = true;
                lastStringCommand = data;
            }
        } catch (SerialPortException ex) {
            callback.connectionLost();
            Logger.getLogger(Arduino.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void write(byte[] data) {
        try {
            if (!port.writeBytes(data)) {
                App.print("Nu am putut trimite comanda " + Arrays.toString(data), Arduino.class);
            } else {
                lastCommandWasString = false;
                lastByteCommand = data;
            }
        } catch (SerialPortException ex) {
            Logger.getLogger(Arduino.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void receivedData(String data) {
        readFirstLine = true;
        readData = data;
        if (data.contains("6")) {
            LEDAnimationSender animationSender=getSmartHomeSystem().getAnimationSender();
            synchronized (animationSender){
                //System.out.println("am notificat");
                animationSender.notify();
            }
        }
        if (data.contains(SmartHomeSystem.RESEND_ERROR_TOKEN)) {
            if (lastCommandWasString) {
                App.print("A fost detectata o eroare de comunicare, se retrimite "
                        + lastStringCommand, Arduino.class);
                write(lastStringCommand);
            } else {
                App.print("A fost detectata o eroare de comunicare, se retrimite "
                        + Arrays.toString(lastByteCommand), Arduino.class);
                write(lastByteCommand);
            }
        }
    }

    public static String[] getAllCOMPorts() {
        return SerialPortList.getPortNames();
    }
}
