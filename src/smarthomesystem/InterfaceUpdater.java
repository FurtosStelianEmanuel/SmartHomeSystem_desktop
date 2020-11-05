/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smarthomesystem;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Folosita pentru functionarea interfetelor intre ele de exemplu 
 * legarea dintre {@link ColorWheelPanel} si {@link ColorSlider} este facuta prin aceasta clasa
 * @author Manel
 */
public class InterfaceUpdater {

    Class c;
    String metoda = "";
    Object instanta;
    Class args[];

    InterfaceUpdater(Class c, String metoda, Object instanta, Class... args) {
        this.c = c;
        this.metoda = metoda;
        this.instanta = instanta;
        this.args = args;
    }

    void notifyChanges(Object... objs) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Method method = c.getMethod(metoda, args);
        method.invoke(instanta, objs);
    }

}
