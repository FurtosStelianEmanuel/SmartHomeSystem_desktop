/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smarthomesystem;

import java.awt.Color;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.bluetooth.RemoteDevice;

/**
 *
 * @author Manel
 */
public class Settings {

    //<editor-fold desc="Variabile" defaultstate="collapsed">
    static boolean AUTOMATIC_LIGHT_ON_DOOR = true;
    static boolean AUTOMATIC_LIGHT_OFF_DOOR = true;
    static boolean AUTOMATIC_DOOR_APPLIER = false;
    static Color AUTOMATIC_LIGHT_ON_COLOR_DOOR = new Color(255, 255, 255);
    private static List<Listener> ascultatori = new ArrayList<>();
    static final int STRIP_CONTROL_RGB = 123;
    static final int STRIP_CONTROL_ARGB = 124;
    static final int STRIP_CONTROL_BOTH = 125;
    int STRIP_CONTROL_TYPE = STRIP_CONTROL_BOTH;
    //</editor-fold>

    //<editor-fold desc="Functii ajutatoare" defaultstate="collapsed">
    public static String getPathToJar() throws URISyntaxException {
        String x = SmartHomeSystem.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
        StringBuilder b = new StringBuilder(x);
        b.delete(0, 1);
        return b.toString();
    }

    public static String get_path(String finalName) throws URISyntaxException {
        String full = getPathToJar();
        StringTokenizer b = new StringTokenizer(full, "/\\");
        String x = "";
        while (b.hasMoreElements()) {
            String y = b.nextToken();
            if (!(y.toLowerCase().equals(finalName.toLowerCase())) && !(y.toLowerCase().equals("store"))) {
                x += y + "\\";
            }
        }
        return x;
    }
    //</editor-fold>

    //<editor-fold desc="Functii pentru serializare si deserializare" defaultstate="collapsed">
    static void storeBluetoothConnections(List<RemoteDevice> devices) throws FileNotFoundException, IOException {
        //<editor-fold desc="body" defaultstate="collapsed">
        FileOutputStream file = new FileOutputStream(SmartHomeSystem.path + "bluetoothConnections.shs");
        ObjectOutputStream out = new ObjectOutputStream(file);
        List<ModulBluetooth> module = new ArrayList<>();
        for (RemoteDevice dev : devices) {
            module.add(new ModulBluetooth(dev.getFriendlyName(false), dev.getBluetoothAddress()));
        }
        out.writeObject(module);
        out.close();
        file.close();
        //</editor-fold>
    }

    static List<ModulBluetooth> getPreviousBluetoothConnections() throws FileNotFoundException, IOException, ClassNotFoundException {
        //<editor-fold desc="body" defaultstate="collapsed">
        FileInputStream file = new FileInputStream(SmartHomeSystem.path + "bluetoothConnections.shs");
        ObjectInputStream in = new ObjectInputStream(file);
        List<ModulBluetooth> object1 = (List<ModulBluetooth>) in.readObject();
        in.close();
        file.close();
        return object1;
        //</editor-fold>
    }

    /**
     * Serializeaza obiectele claselor animatiilor
     *
     * @param obj un obiect al unei clase serializabile
     * @param fileName numele fisierului FARA extensie !!!
     * @throws FileNotFoundException
     * @throws IOException
     */
    private static void storeAnimation(Object obj, Class clasa) throws FileNotFoundException, IOException {
        //<editor-fold desc="body" defaultstate="collapsed">
        String storePath = SmartHomeSystem.path + "animatii\\";
        String finalFileName = storePath + clasa.getSimpleName() + ".anm";
        File f = new File(storePath);
        if (!f.exists()) {
            App.print(f.mkdir() ? "A trebuit sa creez un nou folder 'animatii' " : "Nu am putut un nou folder 'animatii'",
                    Settings.class);
        }
        try (FileOutputStream file = new FileOutputStream(finalFileName); ObjectOutputStream out = new ObjectOutputStream(file)) {
            out.writeObject(obj);
        }
        //</editor-fold>
    }

    /**
     * Deserializeaza clasele animatiilor si le returneaza
     *
     * @param c de exemplu {@link LightOnAnimation}
     * @return daca primeste ca parametru exemplul de mai sus returneaza
     * obiectul de tip {@link LightOnAnimation}
     * @throws FileNotFoundException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Object getAnimation(Class c) throws FileNotFoundException, IOException, ClassNotFoundException {
        //<editor-fold desc="body" defaultstate="collapsed">
        String storePath = SmartHomeSystem.path + "animatii\\";
        String finalFileName = storePath + c.getSimpleName() + ".anm";
        FileInputStream file = new FileInputStream(finalFileName);
        ObjectInputStream in = new ObjectInputStream(file);
        Object object = in.readObject();
        in.close();
        file.close();
        return object;
        //</editor-fold>
    }

    //</editor-fold>
    //<editor-fold desc="Clase serializabile, prin intermediul lor se stocheaza setarile facute de utilizator" defaultstate="collapsed">
    static class ModulBluetooth implements Serializable {

        String nume, adresa;

        public ModulBluetooth(String nume, String adresa) {
            this.nume = nume;
            this.adresa = adresa;
        }
    }

    static class StripType implements Serializable {

        int type;

        public StripType(SettingsForm form) {
            type = form.jComboBox1.getSelectedIndex();
        }
    }

    static class LightOnAnimation implements Serializable {

        int type;
        Color color;
        int animationSpeed;
        int increment;
        int sections;

        public LightOnAnimation(SettingsForm form) {
            type = form.jComboBox2.getSelectedIndex();
            color = form.jButton4.getBackground();
            animationSpeed = (int) form.jSpinner1.getValue();
            increment = (int) form.jSpinner2.getValue();
            sections = (int) form.jSpinner3.getValue();
        }

    }

    static class LightOffAnimation implements Serializable {

        int type;
        int animationSpeed;
        int decrement;
        int sections;

        public LightOffAnimation(SettingsForm form) {
            type = form.jComboBox3.getSelectedIndex();
            animationSpeed = (int) form.jSpinner6.getValue();
            decrement = (int) form.jSpinner5.getValue();
            sections = (int) form.jSpinner4.getValue();
        }
    }

    static class RomanianFlagAnimation implements Serializable {

        int animationSpeed, nrFlags, flagWidth, direction, orientation, intensity;

        public RomanianFlagAnimation(SettingsForm form) {
            animationSpeed = (int) form.jSpinner7.getValue();
            nrFlags = (int) form.jSpinner8.getValue();
            flagWidth = (int) form.jSpinner9.getValue();
            direction = (int) form.jComboBox4.getSelectedIndex();
            orientation = (int) form.jComboBox5.getSelectedIndex();
            intensity = (int) form.jSpinner10.getValue();
        }
    }

    static class FastLEDAnimations implements Serializable {

        int animationSpeed, type, intensity;

        public FastLEDAnimations(SettingsForm form) {
            animationSpeed = (int) form.jSpinner11.getValue();
            type = (int) form.jComboBox6.getSelectedIndex();
            intensity = (int) form.jSpinner12.getValue();

        }
    }

    static class ScreenMirrorAnimation implements Serializable {

        Rectangle arieInregistrare;
        int spatierePixeli, iteratii, scara, intensitate, COLOR_PACKETS_COUNT, offset;
        boolean oglindit;

        public ScreenMirrorAnimation(ScreenMirrorSetup form) {
            arieInregistrare = new Rectangle(ScreenMirrorSetup.NEW_RECORDING_AREA_DIMENSION);
            arieInregistrare.setLocation(ScreenMirrorSetup.NEW_RECORDING_AREA_LOCATION);
            spatierePixeli = (int) form.jSpinner1.getValue();
            iteratii = (int) form.jSpinner2.getValue();
            scara = (int) form.jSpinner4.getValue();
            intensitate = (int) form.jSlider1.getValue();
            COLOR_PACKETS_COUNT = (int) form.jSpinner3.getValue();
            offset = (int) form.jSpinner5.getValue();
            oglindit = form.jCheckBox1.isSelected();
        }
    }

    static class FaderAnimation implements Serializable {

        int tip, increment, viteza;
        Color color;
        int intensitateMinima, intensitateMaxima;
        int dimensiune;

        public FaderAnimation(SettingsForm form) {
            this.tip = form.jComboBox7.getSelectedIndex();
            this.increment = (int) form.jSpinner13.getValue();
            this.viteza = (int) form.jSpinner14.getValue();
            this.color = form.jButton12.getBackground();
            this.intensitateMinima = form.colorSliderPanel1.slider.getValue();
            this.intensitateMaxima = form.colorSliderPanel2.slider.getValue();
            this.dimensiune = form.colorSliderPanel3.slider.getValue();
        }
    }

    //</editor-fold>
    //<editor-fold desc="Functii de salvare sau reaplicare a setarilor">
    public static void savePreviousDataOfScreenMirrorSetup(ScreenMirrorSetup form) throws IOException {
        ScreenMirrorAnimation mirrorAnimation = new ScreenMirrorAnimation(form);
        storeAnimation(mirrorAnimation, ScreenMirrorAnimation.class);
    }

    public static void applyPreviousSettingsToScreenMirrorSetup(ScreenMirrorSetup form) {
        try {
            ScreenMirrorAnimation screenMirrorAnimationStored = (ScreenMirrorAnimation) getAnimation(ScreenMirrorAnimation.class);
            form.setLocationLabel(screenMirrorAnimationStored.arieInregistrare.getLocation());
            form.setDimensionLabel(screenMirrorAnimationStored.arieInregistrare.getSize());
            form.jSpinner1.setValue(screenMirrorAnimationStored.spatierePixeli);
            form.jSpinner2.setValue(screenMirrorAnimationStored.iteratii);
            form.jSpinner4.setValue(screenMirrorAnimationStored.scara);
            form.jSlider1.setValue(screenMirrorAnimationStored.intensitate);
            form.jSpinner3.setValue(screenMirrorAnimationStored.COLOR_PACKETS_COUNT);
            form.jSpinner5.setValue(screenMirrorAnimationStored.offset);
            form.jCheckBox1.setSelected(screenMirrorAnimationStored.oglindit);
        } catch (ClassNotFoundException | IOException ex) {
            Logger.getLogger(Settings.class.getSimpleName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Salveaza toate setarile facute de utilizator in {@link SettingsForm}
     * {@link SettingsForm}
     *
     * @param form {@link SettingsForm}
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void savePreviousDataOfSettingsForm(SettingsForm form) throws FileNotFoundException, IOException {
        //<editor-fold desc="body" defaultstate="collapsed">
        App.print("animationsSaved", Settings.class);
        StripType stripTypeStore = new StripType(form);
        LightOnAnimation lightOnAnimationStore = new LightOnAnimation(form);
        LightOffAnimation lightOffAnimationStore = new LightOffAnimation(form);
        RomanianFlagAnimation romanianFlagAnimationStore = new RomanianFlagAnimation(form);
        FastLEDAnimations fastLEDAnimationsStore = new FastLEDAnimations(form);
        FaderAnimation faderAnimationStore = new FaderAnimation(form);

        storeAnimation(stripTypeStore, StripType.class);
        storeAnimation(lightOffAnimationStore, LightOffAnimation.class);
        storeAnimation(lightOnAnimationStore, LightOnAnimation.class);
        storeAnimation(romanianFlagAnimationStore, RomanianFlagAnimation.class);
        storeAnimation(fastLEDAnimationsStore, FastLEDAnimations.class);
        storeAnimation(faderAnimationStore, FaderAnimation.class);
        //</editor-fold>
    }
    public static boolean STARTUP_ERROR_StripType = false;

    /**
     * Reaplica setarile facute salvate de utilizator
     *
     * @param form {@link SettingsForm} Frame-ul unde vor aparea aceste setari
     */
    public static void applyPreviousDataToSettingsForm(SettingsForm form) {
        //<editor-fold desc="body" defaultstate="collapsed">
        try {
            StripType stripTypeStored = (StripType) getAnimation(StripType.class);
            form.jComboBox1.setSelectedIndex(stripTypeStored.type);
        } catch (ClassNotFoundException | IOException ex) {
            STARTUP_ERROR_StripType = true;
            Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            LightOnAnimation lightOnAnimationStored = (LightOnAnimation) getAnimation(LightOnAnimation.class);
            form.jComboBox2.setSelectedIndex(lightOnAnimationStored.type);
            form.jButton4.setBackground(lightOnAnimationStored.color);
            String hex = Integer.toHexString(lightOnAnimationStored.color.getRGB()).substring(2);
            form.jButton4.setText(hex);
            form.jSpinner1.setValue(lightOnAnimationStored.animationSpeed);
            form.jSpinner2.setValue(lightOnAnimationStored.increment);
            form.jSpinner3.setValue(lightOnAnimationStored.sections);
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            LightOffAnimation lightOffAnimationStored = (LightOffAnimation) getAnimation(LightOffAnimation.class);
            form.jComboBox3.setSelectedIndex(lightOffAnimationStored.type);
            form.jSpinner6.setValue(lightOffAnimationStored.animationSpeed);
            form.jSpinner5.setValue(lightOffAnimationStored.decrement);
            form.jSpinner4.setValue(lightOffAnimationStored.sections);
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            RomanianFlagAnimation romanianFlagAnimationStored = (RomanianFlagAnimation) getAnimation(RomanianFlagAnimation.class);
            form.jSpinner7.setValue(romanianFlagAnimationStored.animationSpeed);
            form.jSpinner8.setValue(romanianFlagAnimationStored.nrFlags);
            form.jSpinner9.setValue(romanianFlagAnimationStored.flagWidth);
            form.jComboBox4.setSelectedIndex(romanianFlagAnimationStored.direction);
            form.jComboBox5.setSelectedIndex(romanianFlagAnimationStored.orientation);
            form.jSpinner10.setValue(romanianFlagAnimationStored.intensity);
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            FastLEDAnimations fastLEDAnimationsStored = (FastLEDAnimations) getAnimation(FastLEDAnimations.class);
            form.jSpinner11.setValue(fastLEDAnimationsStored.animationSpeed);
            form.jComboBox6.setSelectedIndex(fastLEDAnimationsStored.type);
            form.jSpinner12.setValue(fastLEDAnimationsStored.intensity);
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            FaderAnimation faderAnimationStored = (FaderAnimation) getAnimation(FaderAnimation.class);
            String hex = Integer.toHexString(faderAnimationStored.color.getRGB()).substring(2);

            form.jComboBox7.setSelectedIndex(faderAnimationStored.tip);
            form.jSpinner13.setValue(faderAnimationStored.increment);
            form.jSpinner14.setValue(faderAnimationStored.viteza);
            form.jButton12.setBackground(faderAnimationStored.color);
            form.jButton12.setText(hex);
            form.colorSliderPanel1.slider.setGeneralColor(faderAnimationStored.color);
            form.colorSliderPanel2.slider.setGeneralColor(faderAnimationStored.color);
            form.colorSliderPanel1.slider.setValue(faderAnimationStored.intensitateMinima);
            form.colorSliderPanel2.slider.setValue(faderAnimationStored.intensitateMaxima);
            form.colorSliderPanel3.slider.setValue(faderAnimationStored.dimensiune);
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
            try {
                form.colorSliderPanel1.slider.setGeneralColor(
                        form.jButton12.getBackground()
                );
                form.colorSliderPanel2.slider.setGeneralColor(
                        form.jButton12.getBackground()
                );
            } catch (Exception except) {

            }
        }

        //</editor-fold>
    }
    //</editor-fold>

    //<editor-fold desc="Partea de listeneri" defaultstate="collapsed">
    static class Listener {

        Class c;
        String metoda = "";
        Object instanta;

        Listener(Class c, String metoda, Object instanta) {
            this.c = c;
            this.metoda = metoda;
            this.instanta = instanta;
        }
    }

    /**
     * La apelarea acestei functii toti listenerii din {@link#ascultatori} vor
     * fi notificati de schimbari momentan o folosesc doar pentru evenimentele
     * usii, nu stiu cat de folositoare e chestia asta
     *
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    static void notifyChanges() throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        for (Listener ascultator : ascultatori) {
            Method method = ascultator.c.getMethod(ascultator.metoda);
            method.invoke(ascultator.instanta);
        }
    }

    static void addListener(Class clasa, String numeFunctie, Object instanta) {
        ascultatori.add(new Listener(clasa, numeFunctie, instanta));
    }
    //</editor-fold>

}
