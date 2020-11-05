/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smarthomesystem;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;



/**
 *
 * @author Manel
 */
public class App{

    //<editor-fold desc="Varibile comune">
    static final int SENDBACK_DATA_REQUEST_CODE=1;
    static final char COMMAND_MOUSE_MOVE_CLASSIC = 'm';
    static final char COMMAND_MOUSE_MOVE_TOUCH_PAD = 'n';
    static final char COMMAND_MOUSE_PRESS = 'b';
    static final char COMMAND_MOUSE_RELEASE = 'v';
    static final char COMMAND_MOUSE_WHEEL_UP = 'c';
    static final char COMMAND_MOUSE_WHEEL_DOWN = 'x';
    static final char COMMAND_MOUSE_WHEEL_PRESS = 'z';
    static final char COMMAND_MOUSE_WHEEL_RELEASE = ';';
    static final char COMMAND_MOUSE_RIGHT_PRESS = 'l';
    static final char COMMAND_MOUSE_RIGHT_RELEASE = 'k';
    static final char COMMAND_YOUTUBE_PLAY = 'j';
    static final char COMMAND_KEY_PRESS = 'h';
    static final char COMMAND_KEY_RELEASE = 'g';
    static final char COMMAND_VOLUME='f';
    static final char COMMAND_SHUTDOWN_PC='d';
    static final char COMMAND_IP_ALIVE='s';
    static final char COMMAND_OPEN_LINK='a';
    static final char COMMAND_OPEN_LINK_NEW_TAB='p';
    static final char COMMAND_KILL_CHROME='o';
    static final char COMMAND_YOUTUBE_PLAY_NEW_TAB='i';
    static final char COMMAND_YOUTUBE_RESULTS='u';
    static final char COMMAND_YOUTUBE_RESULTS_NEW_TAB = 'y';
    static final char COMMAND_YOUTUBE_RANDOM = 't';
    static final char COMMAND_YOUTUBE_RANDOM_NEW_TAB = 'r';
    static final char COMMAND_ARDUINO_REDIRECT = '>';
    public static final char COMMAND_TURN_MID_LIGHT_ON = 'e';
    public static final char COMMAND_PC_APPLY = 'w';
    public static final char COMMAND_ANDROID_APPLY='M';
    public static final char COMMAND_PC_REQUEST_RESPONSE='N';
    public static final char DOOR_COMMAND='J';
    
    public static final char CHECK_CONNECTION_TYPE_TOKEN='B';
    public static final char CONFIRM_CONNECTION_TYPE_TOKEN='V';
    public static final char PC_MASTER='C';
    public static final char ANDROID_MASTER='X';
    public static final char SWITCH_CONNECTION_TYPE='Z';
    
    public static char CONNECTION_TYPE=App.PC_MASTER;
    
    
    /**
     * d01 daca setarea e activa, d00 daca setarea e inactiva
     */
    public static final String ARDUINO_AUTOMATIC_LIGHT_ON_DOOR="d0";
    /**
     * d11 daca setarea e activa, d10 daca setarea e inactiva
     */
    public static final String ARDUINO_AUTOMATIC_LIGHT_OFF_DOOR="d1";
    /**
     * Aceasta comanda nu este trimisa la arduino, ea tine doar de android si de interfata de la pc, dar ca sa nu mai 
     * caut dupa ea cand am implementat ca setarile de la android sa apara si la pc si sa poata fi schimbate de la pc 
     * i am dat prefix-ul ARDUINO
     */
    public static final String ARDUINO_AUTOMATIC_DOOR_APPLIER="d2";
    /**
     * dt 0 0 255 pentru a seta culoare la deschiderea usii sa fie albastra
     */
    public static final String ARDUINO_DOOR_ON_COLOR="dt";
    
    
    
    static final char KEY_LEFT_ARROW=(char)37;
    static final char KEY_RIGHT_ARROW=(char)39;
    static final char KEY_UP_ARROW=(char)38;
    static final char KEY_DOWN_ARROW=(char)40;
    static final char KEY_ESCAPE=(char)27;
    static final char KEY_BACKSPACE=(char)8;
    static final char KEY_TAB=(char)9;
    static final char KEY_SHIFT=(char)16;
    static final char KEY_SPACE=(char)32;
    static final char KEY_ENTER=(char)10;
    static final char KEY_WINDOWS=(char)524;
    static final char KEY_CTRL=(char)17;
    static final char KEY_DEL=(char)127;
    static final char KEY_CAPSLOCK=(char)20;
    static final char KEY_ALT=(char)18;
    static final char KEY_A=(char)65;
    static final char KEY_B=(char)66;
    static final char KEY_C=(char)67;
    static final char KEY_D=(char)68;
    static final char KEY_E=(char)69;
    static final char KEY_F=(char)70;
    static final char KEY_G=(char)71;
    static final char KEY_H=(char)72;
    static final char KEY_I=(char)73;
    static final char KEY_J=(char)74;
    static final char KEY_K=(char)75;
    static final char KEY_L=(char)76;
    static final char KEY_M=(char)77;
    static final char KEY_N=(char)78;
    static final char KEY_O=(char)79;
    static final char KEY_P=(char)80;
    static final char KEY_Q=(char)81;
    static final char KEY_R=(char)82;
    static final char KEY_S=(char)83;
    static final char KEY_T=(char)84;
    static final char KEY_U=(char)85;
    static final char KEY_V=(char)86;
    static final char KEY_W=(char)87;
    static final char KEY_X=(char)88;
    static final char KEY_Y=(char)89;
    static final char KEY_Z=(char)90;
    static final char KEY_F4=(char)115;
    
    static final String SHUTDOWN_CODE="shutdown";
    
    
    //</editor-fold>
    static boolean containsNewLineSeparator(String x) {
        String newline = System.getProperty("line.separator");
        boolean hasNewline = x.contains(newline);
        return hasNewline;
    }

    static int map(double x, double in_min, double in_max, double out_min, double out_max) {
        return (int) ((x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min);
    }

    static String getTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    static void print(String masaj, Class c) {
        System.out.println(getTime()+" : "+c.getName() + ">--/-->" + masaj);
    }

    static double mapd(double x, double in_min, double in_max, double out_min, double out_max) {
        return ((x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min);
    }
    
}
