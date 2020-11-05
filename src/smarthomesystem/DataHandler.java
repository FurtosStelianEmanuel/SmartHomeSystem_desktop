package smarthomesystem;

import com.sun.glass.events.KeyEvent;
import java.awt.AWTException;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.InputEvent;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static smarthomesystem.SmartHomeSystem.getSmartHomeSystem;

/**
 * Aceasta clasa este folosita pentru a executa toate comenzile pe care serverul
 * le primeste toate clasele ce se folosesc de {@link DataHandler} trebuie sa
 * isi ia referinta clasei din {@link SmartHomeSystem#getDefaultDataHandler() }
 * In aceasta clasa se initializeaza si clientul UDP pentru comunicarea cu
 * python (setare volum,youtube query,etc)
 *
 * @author Manel
 */
public class DataHandler {

    //<editor-fold desc="Variabile" defaultstate="collapsed">
    Robot mouseRobot;
    UDPClient python;
    Dimension screenDimension;
    int MAX_WIDTH = 824;
    int MAX_HEIGHT = 465;
    Pattern mouseCoordinatesPattern = Pattern.compile("[a-zA-Z] (\\-?\\d\\d?\\d?\\d?) (\\-?\\d\\d?\\d?\\d?)");
    Pattern mouseWheelUpPattern=Pattern.compile("c (-1)");
    Pattern mouseWheelDownPattern=Pattern.compile("x (1)");
    //</editor-fold>

    public DataHandler() {
        //<editor-fold desc="body" defaultstate="collapsed">
        try {
            mouseRobot = new Robot();
        } catch (AWTException ex) {
            App.print("Nu am putut initializa robotul\n" + ex.toString(), DataHandler.class);
        }
        screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
        try {
            python = new UDPClient();
        } catch (SocketException | UnknownHostException ex) {
            App.print("Nu am putut crea clientul UDP " + ex.toString(), DataHandler.class);
            App.print("Comunicarea cu python nu poate fi realizata", DataHandler.class);
        }
        //</editor-fold>
    }

    /**
     *
     * @param x coordonata x a event ului de drag de pe telefon
     * @param y coordonata y a event ului de drag de pe telefon mai sunt nevoie
     * de mappuri pentru a acoperi intreaga rezolutie a ecranului ok cu un delay
     * de 5 ms intre strigari, dar se poate umbla la valoarea asta
     */
    private void mouseMoveClassic(int x, int y) {
        //<editor-fold desc="body" defaultstate="collapsed">
        x = App.map(x, 0, MAX_WIDTH, 0, screenDimension.getWidth());
        y = App.map(y, 0, MAX_HEIGHT, 0, screenDimension.getHeight());
        mouseRobot.mouseMove(x, y);
        //</editor-fold>
    }

    /**
     * Functionalitatea de touchpad
     *
     * @param ax
     * @param ay
     */
    private void mouseMoveTouchpad(int ax, int ay) {
        //<editor-fold desc="body" defaultstate="collapsed">
        Point mouse = MouseInfo.getPointerInfo().getLocation();
        mouseRobot.mouseMove(mouse.x + ax, mouse.y + ay);
        //</editor-fold>
    }

    /**
     * Mouse stanga apasat, actiune indeplinita de {@link #mouseRobot}
     */
    private void mousePress() {
        //<editor-fold desc="body" defaultstate="collapsed">
        mouseRobot.mousePress(InputEvent.BUTTON1_MASK);
        //</editor-fold>
    }

    /**
     * Mouse stanga eliberat, actiune indeplinita de {@link #mouseRobot}
     */
    private void mouseRelease() {
        //<editor-fold desc="body" defaultstate="collapsed">
        mouseRobot.mouseRelease(InputEvent.BUTTON1_MASK);
        //</editor-fold>
    }

    /**
     * Mouse wheel in sus, actiune indeplinita de {@link #mouseRobot}
     *
     * @param increment cate unitati sa dea scroll, practic {@link #mouseWheelDown(int)
     * } e acc functie doar ca incrementul e negativ intr-o directie
     */
    private void mouseWheelUp(int increment) {
        //<editor-fold desc="body" defaultstate="collapsed">
        mouseRobot.mouseWheel(increment);
        //</editor-fold>
    }

    /**
     * Mouse wheel in jos, actiune indeplinita de {@link #mouseRobot}
     *
     * @param increment cate unitati sa dea scroll, practic {@link #mouseWheelUp(int)
     * } e acc functie doar ca incrementul e negativ intr-o directie
     */
    private void mouseWheelDown(int increment) {
        //<editor-fold desc="body" defaultstate="collapsed">
        mouseRobot.mouseWheel(increment);
        //</editor-fold>
    }

    /**
     * Mouse wheel buton din mijloc apasat, actiune indeplinita de
     * {@link #mouseRobot}
     */
    private void mouseWheelPress() {
        //<editor-fold desc="body" defaultstate="collapsed">
        mouseRobot.mousePress(InputEvent.BUTTON2_MASK);
        //</editor-fold>
    }

    /**
     * Mouse wheel buton din mijloc eliberat, actiune indeplinita de
     * {@link #mouseRobot}
     */
    private void mouseWheelRelease() {
        //<editor-fold desc="body" defaultstate="collapsed">
        mouseRobot.mouseRelease(InputEvent.BUTTON2_MASK);
        //</editor-fold>
    }

    /**
     * Mouse dreapta apasat,actiune indeplinita de {@link #mouseRobot}
     */
    private void mouseRightPress() {
        //<editor-fold desc="body" defaultstate="collapsed">
        mouseRobot.mousePress(InputEvent.BUTTON3_MASK);
        //</editor-fold>
    }

    /**
     * Mouse dreapta eliberat,actiune indeplinita de {@link #mouseRobot}
     */
    private void mouseRightRelease() {
        //<editor-fold desc="body" defaultstate="collapsed">
        mouseRobot.mouseRelease(InputEvent.BUTTON3_MASK);
        //</editor-fold>
    }

    /**
     * Foloseste {@link ProcessBuilder } si {@link Process} pentru a executa
     * comenzi in command prompt Inchide una dintre ferestrele chrome deschise
     *
     * @throws IOException
     */
    void killChrome() throws IOException {
        //<editor-fold desc="body" defaultstate="collapsed">
        ProcessBuilder builder = new ProcessBuilder(
                "cmd.exe", "/c", "taskkill /im chrome.exe");
        builder.redirectErrorStream(true);
        Process p = builder.start();
        //</editor-fold>
    }

    /**
     * Primeste un tablou cu string-uri de format
     * [{@link App#COMMAND_YOUTUBE_PLAY} nume_piesa] si trimite comanda prin
     * {@link UDPClient} care spune lui python sa caute pe youtube in functie de
     * nume_piesa
     *
     * @param ar exemplu : String.split({@link App#COMMAND_YOUTUBE_PLAY } slayer
     * angel of death)
     * @throws IOException Daca conexiunea cu serverul UDP nu mai e valabila
     */
    private void youtubePlay(String ar[]) throws IOException {
        //<editor-fold desc="body" defaultstate="collapsed">
        String x = "search ";
        for (int i = 1; i < ar.length; i++) {
            x += ar[i] + (i < ar.length - 1 ? " " : "");
        }
        python.send(x);
        //</editor-fold>
    }

    /**
     * Primeste un tablou cu string-uri de format
     * [{@link App#COMMAND_YOUTUBE_RESULTS} nume_piesa] si trimite comanda prin
     * {@link UDPClient} care spune lui python sa afiseze rezultatele cautarii
     * pe youtube a piesei
     *
     * @param ar exemplu : String.split({@link App#COMMAND_YOUTUBE_RESULTS }
     * slayer angel of death)
     * @throws IOException Daca conexiunea cu serverul UDP nu mai e valabila
     */
    private void youtubeResults(String ar[]) throws IOException {
        //<editor-fold desc="body" defaultstate="collapsed">
        String x = "youtube_results ";
        for (int i = 1; i < ar.length; i++) {
            x += ar[i] + (i < ar.length - 1 ? " " : "");
        }
        python.send(x);
        //</editor-fold>

    }

    /**
     * Foloseste {@link #mouseRobot} pentru a realiza key press-ul unui key
     *
     * @param key caracterul asociat cu fiecare key de exemplu pentru
     * {@link KeyEvent#VK_ENTER} avem caracterul (char)10
     */
    private void keyPress(char key) {
        //<editor-fold desc="body" defaultstate="collapsed">
        try {
            mouseRobot.keyPress(key);
        } catch (IllegalArgumentException ex) {
            mouseRobot.keyPress(KeyEvent.VK_WINDOWS);
        }
        //</editor-fold>
    }

    /**
     * Foloseste {@link #mouseRobot} pentru a reliza key release-ul unui key
     *
     * @param key
     */
    private void keyRelease(char key) {
        //<editor-fold desc="body" defaultstate="collapsed">
        try {
            mouseRobot.keyRelease(key);
        } catch (IllegalArgumentException ex) {
            mouseRobot.keyRelease(KeyEvent.VK_WINDOWS);
        }
        //</editor-fold>
    }

    /**
     * Seteaza volumul tuturor aplicatiilor deschise in momentul apelarii cu
     * ajutorul lui python
     *
     * @param val in intervalul [0,100]
     * @throws IOException Daca {@link #python} nu mai e valabil
     */
    void setVolume(String val) throws IOException {
        //<editor-fold desc="body" defaultstate="collapsed">
        try {
            double dabla = Double.valueOf(val);
            dabla /= 100d;
            String x = "volume " + Double.toString(dabla);
            python.send(x);
        } catch (NumberFormatException ex) {
            Logger.getLogger(DataHandler.class.getSimpleName()).log(Level.SEVERE, null, ex);
        }
        //</editor-fold>
    }

    /**
     * Foloseste {@link ProcessBuilder } si {@link Process} pentru a executa
     * comenzi in command prompt Trimite comanda de oprire "shutdown /s /t 0" si
     * calculatorul ar trebui sa se stinga imediat dupa executarea comenzii din
     * cauza lui /t 0
     *
     * @throws IOException
     */
    void shutdownComputer() throws IOException {
        //<editor-fold desc="body" defaultstate="collapsed">
        ProcessBuilder builder = new ProcessBuilder(
                "cmd.exe", "/c", "shutdown /s /t 0");
        builder.redirectErrorStream(true);
        Process p = builder.start();
        //</editor-fold>
    }

    /**
     * Apelat de fiecare data cand este primit {@link App#COMMAND_IP_ALIVE}
     *
     * @param source
     */
    void received_IP_Ping(Class source) {
        //<editor-fold desc="body" defaultstate="collapsed">
        App.print("Am primit ping", source);
        //</editor-fold>
    }

    /**
     * Deschide un link
     *
     * @param uri trebuie sa contina la inceput http:
     * @return {@link Desktop#browse(java.net.URI) }
     */
    public static boolean openWebpage(URI uri) {
        //<editor-fold desc="body" defaultstate="collapsed">
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(uri);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
        //</editor-fold>
    }

    /**
     * Deschide un link
     *
     * @param url trebuie sa contina la inceput http:
     * @return {@link #openWebpage(java.net.URI) }
     */
    public static boolean openWebpage(URL url) {
        //<editor-fold desc="body" defaultstate="collapsed">
        try {
            boolean x = openWebpage(url.toURI());
            App.print(url.toString(), DataHandler.class);
            return x;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return false;
        //</editor-fold>
    }

    /**
     * Ii spune lui python sa puna o piesa random de pe youtube din my mix
     *
     * @throws java.io.IOException Daca {@link #python} nu mai e valabil
     */
    public void youtubeRandom() throws IOException {
        //<editor-fold desc="body" defaultstate="collapsed">
        python.send("youtube_random");
        //</editor-fold>
    }

    /**
     * Updateaza variabilele {@link Settings#AUTOMATIC_DOOR_APPLIER}, {@link Settings#AUTOMATIC_LIGHT_ON_DOOR},
     * {@link Settings#AUTOMATIC_LIGHT_OFF_DOOR} si
     * {@link Settings#AUTOMATIC_LIGHT_ON_COLOR_DOOR} Apeleaza {@link Settings#notifyChanges()
     * } care notifica toti listenerii setarilor
     *
     * @param command {@link String} de forma wd111 -352 unde caracterele de la
     * index-urile {2,3,4} sunt starile curente ale aprinderii automate, opririi
     * automate si respectiv aplicarii automate (1 pt adev 0 pt fals) si ultima
     * secventa de cifre este culoarea la deschiderea usii
     */
    public void saveDoorChanges(String command) {
        //<editor-fold desc="body" defaultstate="collapsed">
        App.print("Procesam comanda pt setari usa " + command, DataHandler.class);
        boolean commandActivated = false;
        boolean notifiy = true;
        //wd111 -352

        boolean aprindereAutomata = command.charAt(2) == '1';
        boolean oprireAutomata = command.charAt(3) == '1';
        boolean aplicareAutomata = command.charAt(4) == '1';
        int culoare = -1;
        Pattern colorExtractor = Pattern.compile("wJ\\d\\d\\d (\\-?\\d\\d?\\d?\\d?\\d?\\d?\\d?\\d?)");
        Matcher m=colorExtractor.matcher(command);
        if (m.find()){
            culoare = Integer.valueOf(m.group(1));
        }else{
            App.print("Nu am gasit match in input "+command,DataHandler.class);
            Logger.getLogger(DataHandler.class.getSimpleName()).log(Level.SEVERE, null, m);
        }

        Settings.AUTOMATIC_DOOR_APPLIER = aplicareAutomata;
        Settings.AUTOMATIC_LIGHT_ON_DOOR = aprindereAutomata;
        Settings.AUTOMATIC_LIGHT_OFF_DOOR = oprireAutomata;
        Settings.AUTOMATIC_LIGHT_ON_COLOR_DOOR = new java.awt.Color(culoare);

        if (notifiy) {
            try {
                Settings.notifyChanges();
            } catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                Logger.getLogger(DataHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //</editor-fold>
    }

    public void switchConnectionTypeEvent(char CONNECTION_TYPE) {
        if (CONNECTION_TYPE == App.PC_MASTER) {
            SmartHomeSystem.getTray().trayIcon.setToolTip("Conexiune prin " + SmartHomeSystem.getSmartHomeSystem().getHC05().name);
            SmartHomeSystem.getTray().trayIcon.displayMessage("Smart Home System",
                     "Conexiune cu Android , programul ruleaza "
                    + "in modul PC_MASTER",
                     TrayIcon.MessageType.INFO);
        }

        App.CONNECTION_TYPE = CONNECTION_TYPE;
        App.print("Am schimbat CONNECTION_TYPE la " + CONNECTION_TYPE, DataHandler.class);
    }

    /**
     * Functie apelata fie de {@link SPPServer} fie de {@link SocketConnection}
     * ea proceseaza si executa toate comenzile primite la InputStream-uri
     *
     * @param line
     * @param byteData
     */
    public int dataReceived(String line, byte... byteData) {
        //<editor-fold desc="body" defaultstate="collapsed">
        char c;
        int cod = 0;
        String ar[];
        Matcher mousePositionMatcher = mouseCoordinatesPattern.matcher(line);
        Matcher mouseWheelUpMatcher = mouseWheelUpPattern.matcher(line);
        Matcher mouseWheelDownMatcher=mouseWheelDownPattern.matcher(line);
        //<editor-fold desc="ANDROID_MASTER" defaulstate="collapsed">
        if (App.CONNECTION_TYPE == App.ANDROID_MASTER) {
            try {
                c = line.charAt(0);
                switch (c) {

                    case App.COMMAND_MOUSE_MOVE_CLASSIC:
                        //se asteapta input de genul m 122 234
                        //122 si 234 sunt coordonatele event ului de drag in componenta de pe telefon, mai este 
                        //nevoie transformari pentru a putea acoperi toata rezolutia ecranului 
                        //Toolkit.getDefaultToolkit().getScreenSize() ?
                        if (mousePositionMatcher.find()) {
                            mouseMoveClassic(Integer.valueOf(mousePositionMatcher.group(1)),
                                    Integer.valueOf(mousePositionMatcher.group(2)));
                        }
                        break;
                    case App.COMMAND_MOUSE_MOVE_TOUCH_PAD:
                        //se asteapta input de genul n 3.4 4.5
                        //unde 3 si patru sunt variabilele care dicteaza cat cursorul trebuie sa mearga pe orizontala
                        //si respectiv pe verticala
                        if (mousePositionMatcher.find()) {
                            mouseMoveTouchpad(Integer.valueOf(mousePositionMatcher.group(1)),
                                     Integer.valueOf(mousePositionMatcher.group(2)));
                        }
                        break;
                    case App.COMMAND_MOUSE_PRESS:
                        mousePress();
                        break;
                    case App.COMMAND_MOUSE_RELEASE:
                        mouseRelease();
                        break;
                    case App.COMMAND_MOUSE_WHEEL_UP:
                        if (mouseWheelUpMatcher.find()) {
                            mouseWheelUp(Integer.valueOf(mouseWheelUpMatcher.group(1)));
                        }
                        break;
                    case App.COMMAND_MOUSE_WHEEL_DOWN:
                        if (mouseWheelDownMatcher.find()) {
                            mouseWheelDown(Integer.valueOf(mouseWheelDownMatcher.group(1)));
                        }
                        break;
                    case App.COMMAND_MOUSE_WHEEL_PRESS:
                        mouseWheelPress();
                        break;
                    case App.COMMAND_MOUSE_WHEEL_RELEASE:
                        mouseWheelRelease();
                        break;
                    case App.COMMAND_MOUSE_RIGHT_PRESS:
                        mouseRightPress();
                        break;
                    case App.COMMAND_MOUSE_RIGHT_RELEASE:
                        mouseRightRelease();
                        break;
                    case App.COMMAND_YOUTUBE_PLAY:
                        App.print("Piesa " + line.replace(App.COMMAND_YOUTUBE_PLAY + " ", ""), DataHandler.class);
                        ar = line.split(" ");
                        killChrome();
                         {
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(DataHandler.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        youtubePlay(ar);
                        break;
                    case App.COMMAND_KEY_PRESS:
                        try {
                            keyPress(line.charAt(1));
                        } catch (StringIndexOutOfBoundsException ex) {
                            mouseRobot.keyPress(KeyEvent.VK_ENTER);
                        }
                        break;

                    case App.COMMAND_KEY_RELEASE:
                        try {
                            keyRelease(line.charAt(1));
                        } catch (StringIndexOutOfBoundsException ex) {
                            mouseRobot.keyRelease(KeyEvent.VK_ENTER);
                        }
                        break;
                    case App.COMMAND_VOLUME:
                        ar = line.split(" ");
                        setVolume(ar[1]);
                        break;
                    case App.COMMAND_SHUTDOWN_PC:
                        try {
                            if (line.contains(App.SHUTDOWN_CODE)) {
                                shutdownComputer();
                            }
                        } catch (ArrayIndexOutOfBoundsException ex) {

                        }
                        break;
                    case App.COMMAND_IP_ALIVE:
                        received_IP_Ping(DataHandler.class);
                        break;
                    case App.COMMAND_OPEN_LINK:
                        ar = line.split(" ");
                        killChrome();
                         {
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(DataHandler.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        openWebpage(new URL(ar[1]));
                        App.print("Deschidem link " + ar[1], DataHandler.class);
                        break;
                    case App.COMMAND_OPEN_LINK_NEW_TAB:
                        ar = line.split(" ");
                        openWebpage(new URL(ar[1]));
                        App.print("Deschidem link " + ar[1], DataHandler.class);
                        break;
                    case App.COMMAND_KILL_CHROME:
                        killChrome();
                        break;
                    case App.COMMAND_YOUTUBE_PLAY_NEW_TAB:
                        ar = line.split(" ");
                        youtubePlay(ar);
                        break;
                    case App.COMMAND_YOUTUBE_RESULTS:
                        ar = line.split(" ");
                        killChrome();
                         {
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(DataHandler.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        youtubeResults(ar);
                        break;
                    case App.COMMAND_YOUTUBE_RESULTS_NEW_TAB:
                        ar = line.split(" ");
                        youtubeResults(ar);
                        break;
                    case App.COMMAND_YOUTUBE_RANDOM:
                        killChrome();
                         {
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(DataHandler.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        youtubeRandom();
                        break;
                    case App.COMMAND_YOUTUBE_RANDOM_NEW_TAB:
                        youtubeRandom();
                        break;
                    case App.COMMAND_PC_APPLY:
                        /**
                         * ca sa intre aici trebe sa primeasca ceva de genu
                         * "w"+"J"sau ce mai adaug eu pe parcurs
                         */
                        switch (line.charAt(1)) {
                            case 'J':
                                saveDoorChanges(line);
                                break;
                        }
                        break;
                    case App.COMMAND_PC_REQUEST_RESPONSE:
                        MainForm panelSetariUsa=(MainForm) getSmartHomeSystem().getGUI(MainForm.class);
                        panelSetariUsa.panelSetariUsa.receivedResponse();
                        break;
                    case App.CHECK_CONNECTION_TYPE_TOKEN:
                        SmartHomeSystem.getSmartHomeSystem().send(App.CONFIRM_CONNECTION_TYPE_TOKEN + "PC");
                        break;
                    case App.SWITCH_CONNECTION_TYPE:
                        try {
                            switchConnectionTypeEvent(line.charAt(1));
                        } catch (Exception ex) {
                            App.print("Nu am putut schimba CONNECTION_TYPE", DataHandler.class);
                        }
                        break;
                    default:
                        App.print("Comanda necunoscuta pentru DataHandler in mod ANDROID_MASTER" + line, DataHandler.class);
                        cod = 1;
                        break;
                }
            } catch (StringIndexOutOfBoundsException | NumberFormatException | IOException ex) {

            }
        } //</editor-fold>
        //<editor-fold desc="PC_MASTER" defaultstate="collapsed">
        else if (App.CONNECTION_TYPE == App.PC_MASTER) {
            line = line.replace("\n", "");
            try {
                switch (line.charAt(0)) {

                    case App.COMMAND_MOUSE_MOVE_CLASSIC:
                        //se asteapta input de genul m 122 234
                        //122 si 234 sunt coordonatele event ului de drag in componenta de pe telefon, mai este 
                        //nevoie transformari pentru a putea acoperi toata rezolutia ecranului 
                        //Toolkit.getDefaultToolkit().getScreenSize() ?
                        try {
                            if (mousePositionMatcher.find()) {
                                mouseMoveClassic(Integer.valueOf(mousePositionMatcher.group(1)),
                                        Integer.valueOf(mousePositionMatcher.group(2)));
                            }

                        } catch (NumberFormatException ex) {
                            Logger.getLogger(DataHandler.class.getSimpleName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                    case App.COMMAND_MOUSE_MOVE_TOUCH_PAD:
                        //se asteapta input de genul n 3.4 4.5
                        //unde 3 si patru sunt variabilele care dicteaza cat cursorul trebuie sa mearga pe orizontala
                        //si respectiv pe verticala
                        if (mousePositionMatcher.find()) {
                            mouseMoveTouchpad(Integer.valueOf(mousePositionMatcher.group(1)),
                                    Integer.valueOf(mousePositionMatcher.group(2)));
                        }
                        break;
                    case App.COMMAND_MOUSE_PRESS:
                        mousePress();
                        break;
                    case App.COMMAND_MOUSE_RELEASE:
                        mouseRelease();
                        break;
                    case App.COMMAND_MOUSE_WHEEL_UP:
                        ar = line.split(" ");
                        mouseWheelUp(Integer.valueOf(ar[1]));
                        break;
                    case App.COMMAND_MOUSE_WHEEL_DOWN:
                        ar = line.split(" ");
                        mouseWheelDown(Integer.valueOf(ar[1]));
                        break;
                    case App.COMMAND_MOUSE_WHEEL_PRESS:
                        mouseWheelPress();
                        break;
                    case App.COMMAND_MOUSE_WHEEL_RELEASE:
                        mouseWheelRelease();
                        break;
                    case App.COMMAND_MOUSE_RIGHT_PRESS:
                        mouseRightPress();
                        break;
                    case App.COMMAND_MOUSE_RIGHT_RELEASE:
                        mouseRightRelease();
                        break;
                    case App.COMMAND_YOUTUBE_PLAY:
                        App.print("Piesa " + line.replace(App.COMMAND_YOUTUBE_PLAY + " ", ""), DataHandler.class);
                        ar = line.split(" ");
                        killChrome();
                         {
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(DataHandler.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        youtubePlay(ar);
                        break;
                    case App.COMMAND_KEY_PRESS:
                        try {
                            keyPress(line.charAt(1));
                        } catch (StringIndexOutOfBoundsException ex) {
                            mouseRobot.keyPress(KeyEvent.VK_ENTER);
                        }
                        break;

                    case App.COMMAND_KEY_RELEASE:
                        try {
                            keyRelease(line.charAt(1));
                        } catch (StringIndexOutOfBoundsException ex) {
                            mouseRobot.keyRelease(KeyEvent.VK_ENTER);
                        }
                        break;
                    case App.COMMAND_VOLUME:
                        ar = line.split(" ");
                        setVolume(ar[1]);
                        break;
                    case App.COMMAND_SHUTDOWN_PC:
                        ar = line.split(" ");
                        try {
                            if (ar[1].equals(App.SHUTDOWN_CODE)) {
                                shutdownComputer();
                            }
                        } catch (ArrayIndexOutOfBoundsException ex) {

                        }
                        break;
                    case App.COMMAND_IP_ALIVE:
                        received_IP_Ping(DataHandler.class);
                        break;
                    case App.COMMAND_OPEN_LINK:
                        ar = line.split(" ");
                        killChrome();
                         {
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(DataHandler.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        openWebpage(new URL(ar[1]));
                        App.print("Deschidem link " + ar[1], DataHandler.class);
                        break;
                    case App.COMMAND_OPEN_LINK_NEW_TAB:
                        ar = line.split(" ");
                        openWebpage(new URL(ar[1]));
                        App.print("Deschidem link " + ar[1], DataHandler.class);
                        break;
                    case App.COMMAND_KILL_CHROME:
                        killChrome();
                        break;
                    case App.COMMAND_YOUTUBE_PLAY_NEW_TAB:
                        ar = line.split(" ");
                        youtubePlay(ar);
                        break;
                    case App.COMMAND_YOUTUBE_RESULTS:
                        ar = line.split(" ");
                        killChrome();
                         {
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(DataHandler.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        youtubeResults(ar);
                        break;
                    case App.COMMAND_YOUTUBE_RESULTS_NEW_TAB:
                        ar = line.split(" ");
                        youtubeResults(ar);
                        break;
                    case App.COMMAND_YOUTUBE_RANDOM:
                        killChrome();
                         {
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(DataHandler.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        youtubeRandom();
                        break;
                    case App.COMMAND_YOUTUBE_RANDOM_NEW_TAB:
                        youtubeRandom();
                        break;
                    case App.CHECK_CONNECTION_TYPE_TOKEN:
                        SmartHomeSystem.getSmartHomeSystem().send(App.CONFIRM_CONNECTION_TYPE_TOKEN + "PC");
                        break;
                    case App.SWITCH_CONNECTION_TYPE:
                        try {
                            switchConnectionTypeEvent(line.charAt(1));
                        } catch (Exception ex) {
                            App.print("Nu am putut schimba CONNECTION_TYPE", DataHandler.class);
                        }
                        break;
                    case 'J':
                        break;
                    case '>':
                        if (line.length() > 1) {
                            System.out.println("Cu ping a mai venit cv " + line);
                        }
                        break;
                    default:
                        App.print("Aceasta comanda e redirectionata automat spre arduino, deoarece nu a "
                                + "fost introdusa in alta categorie"
                                + line, DataHandler.class);

                        SmartHomeSystem.getSmartHomeSystem().send(byteData);
                        cod = 0;
                        break;
                }

            } catch (StringIndexOutOfBoundsException | IOException | NumberFormatException ex) {

            }
        }
        //</editor-fold>
        return cod;
        //</editor-fold>
    }
}
