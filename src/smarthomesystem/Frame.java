/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smarthomesystem;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static smarthomesystem.SmartHomeSystem.getByte;

/**
 *
 * @author Manel
 */
public class Frame {

    private List<Color> colors;
    int sleepTimeBefore = -1;
    int sleepTimeAfter = -1;
    int index;
    private List<LED> strip;

    public Frame() {
        colors = new ArrayList<>();
    }

    public Frame(List<LED> currentStrip, int index) {
        strip = new ArrayList<>();
        colors = new ArrayList<>();
        for (LED led : currentStrip) {
            strip.add(new LED(led));
            colors.add(led.color);
        }
        this.index = index;
    }

    public List<Color> getColors() {
        return colors;
    }

    public void set(List<LED> currentStrip) {
        strip.clear();
        colors.clear();
        for (LED led : currentStrip) {
            strip.add(new LED(led));
            colors.add(led.color);
        }
    }

    public void updateColors() {
        colors.clear();
        for (LED led : strip) {
            colors.add(led.color);
        }
    }

    /**
     *
     * @param c Orice culoarea
     * @return {@link Byte} culoarea in 8 biti
     */
    public static byte to8BitColor(Color c) {
        int total;
        int r = c.getRed() >> 5 << 5;
        int g = c.getGreen() >> 5 << 2;
        int b = c.getBlue() >> 6;
        total = r + g + b;
        return (byte) total;
    }

    public List<LED> getStrip() {
        return strip;
    }

    public void setColors(List<Color> colors) {
        this.colors = colors;
    }

    public void setColor(int index, Color c) {
        colors.set(index, c);
    }

    /**
     *
     * @return returneaza varianta in 8 biti a culorilor acestui frame
     */
    private List<Byte> to8BitColors() {
        List<Byte> to8Bit = new ArrayList<>();
        for (Color color : colors) {
            to8Bit.add(to8BitColor(color));
        }
        return to8Bit;
    }

    /**
     *
     * @return {@link byte][]} Secventa de cod care genereaza intregul frame cu
     * culori pe 8 biti (ineficient)
     */
    public byte[][] getRaw8BitColorCommands() {
        List<Byte> to8Bit = to8BitColors();
        byte[][] commands = new byte[to8Bit.size()][2];
        for (int i = 0; i < commands.length; i++) {
            commands[i] = new byte[]{getByte(i), to8Bit.get(i)};
        }
        return commands;
    }

    public boolean sameCommand(byte[] command1, byte[] command2) {
        if (command1.length != command2.length) {
            return false;
        }
        for (int i = 0; i < command1.length; i++) {
            if (command1[i] != command2[i]) {
                return false;
            }
        }
        return true;
    }

    public byte[][] getDifferenceBetweenPackedOptimizedCommands(Frame nextFrame) {
        List<List<Byte>> listaFinala = new ArrayList<>();
        List<Byte> to8Bit = to8BitColors();
        for (int i = 0; i < to8Bit.size(); i += 5) {
            List<Byte> toAdd = new ArrayList<>();
            toAdd.add(getByte(i));
            for (int j = i; j < i + 5; j++) {
                toAdd.add(to8Bit.get(j));
            }
            listaFinala.add(toAdd);
        }
        byte[][] lastFrameCommands = getArray(listaFinala);

        List<List<Byte>> listaNextFrame = new ArrayList<>();
        List<Byte> to8BitNextFrame = nextFrame.to8BitColors();
        for (int i = 0; i < to8Bit.size(); i += 5) {
            List<Byte> toAdd = new ArrayList<>();
            toAdd.add(getByte(i));
            for (int j = i; j < i + 5; j++) {
                toAdd.add(to8BitNextFrame.get(j));
            }
            listaNextFrame.add(toAdd);
        }
        byte[][] nextFrameCommands = getArray(listaNextFrame);

        List<List<Byte>> diffData = new ArrayList<>();

        for (int i = 0; i < nextFrameCommands.length; i++) {
            boolean same = true;
            for (int j = 0; j < nextFrameCommands[i].length; j++) {
                if (nextFrameCommands[i][j] != lastFrameCommands[i][j]) {
                    same = false;
                    break;
                }
            }
            if (!same) {
                List<Byte> newCommand = new ArrayList<>();
                for (int j = 0; j < nextFrameCommands[i].length; j++) {
                    newCommand.add(nextFrameCommands[i][j]);
                }
                diffData.add(newCommand);
            }
        }
        return getArray(diffData);
    }

    public byte[][] getColorPackedOptimizationCommands(Frame nextFrame) {
        List<List<Byte>> listaFinala = new ArrayList<>();
        List<Byte> to8Bit = nextFrame.to8BitColors();
        for (int i = 0; i < to8Bit.size(); i += 5) {
            List<Byte> toAdd = new ArrayList<>();
            toAdd.add(getByte(i));
            for (int j = i; j < i + 5; j++) {
                toAdd.add(to8Bit.get(j));
            }
            listaFinala.add(toAdd);
        }
        return getDifferenceBetweenPackedOptimizedCommands(nextFrame);
    }

    int sendCount = 0;

    public byte[][] getOptimizedCommands(Frame nextFrame) {

        byte[][] differenceOptimization = differenceBetweenCommands(nextFrame);
        //byte[][] sameColorOptimization = getColorOptimizedCommands(nextFrame);
        byte[][] colorPackedOptimization = getColorPackedOptimizationCommands(nextFrame);

        int difByteCount = 0;
        //int sameByteCount = 0;
        int bruteByteCount = 0;

        for (int i = 0; i < differenceOptimization.length; i++) {
            difByteCount += differenceOptimization[i].length;
        }

        /*for (int i = 0; i < sameColorOptimization.length; i++) {
            sameByteCount += sameColorOptimization[i].length;
        }*/
        for (int i = 0; i < colorPackedOptimization.length; i++) {
            bruteByteCount += colorPackedOptimization[i].length;
        }

        if (bruteByteCount < difByteCount) {
            System.out.println("Optimizare colorPacked, " + String.format("colorPacked : %d  ,  optimizareDiferenta : %d ", bruteByteCount, difByteCount));
            return colorPackedOptimization;
        }
        System.out.println("Optimizare diferenta, " + String.format("optimizareDiferenta : %d  ,  colorPacked : %d ", difByteCount, bruteByteCount));
        return differenceOptimization;
    }

    public static byte[][] getArray(List<List<Byte>> lista) {
        byte[][] toReturn = new byte[lista.size()][];
        for (int i = 0; i < toReturn.length; i++) {
            toReturn[i] = new byte[lista.get(i).size()];
            for (int j = 0; j < toReturn[i].length; j++) {
                toReturn[i][j] = lista.get(i).get(j);
            }
        }
        return toReturn;
    }

    @Deprecated
    public byte[][] getColorOptimizedCommands(Frame nextFrame) {
        List<List<Byte>> list = new ArrayList<>();
        byte[][] rawCommands = nextFrame.getRaw8BitColorCommands();
        for (int i = 0; i < rawCommands.length; i++) {
            int start = i;
            byte match = rawCommands[start][1];
            while (true) {
                if (start < rawCommands.length) {
                    if (rawCommands[start][1] != match) {
                        break;
                    }
                } else {
                    break;
                }
                start++;
            }
            List<Byte> toAdd = new ArrayList<>();
            if (start - i >= 2) {
                toAdd.add(getByte(i - 1));
                toAdd.add(rawCommands[i][1]);
                toAdd.add(getByte(start - 2));
                i = start - 1;
            } else {
                toAdd.add(getByte(i));
                toAdd.add(rawCommands[i][1]);
            }
            list.add(toAdd);
        }
        return getArray(list);
    }

    /**
     * Daca de exemplu primul frame e
     * [[0,0],[0,1],[0,2],[0,2],[0,3],[0,4],[0,5],[0,5]] si al doilea
     * [[255,0],[0,1],[255,2],[0,3],...] o sa returneaza doar [[255,0],[255,2]]
     * In exemplul asta am luat invers indexul, dar functia tot pe principiul
     * [[index,culoare]] functioneaza
     *
     * @param nextFrame
     * @return {@link byte[][]} diferenta dintre doua Frame-uri
     */
    public byte[][] differenceBetweenCommands(Frame nextFrame) {
        List<List<Byte>> list = new ArrayList<>();
        byte[][] nextFrameCommands = nextFrame.getRaw8BitColorCommands();
        byte[][] frameCommands = getRaw8BitColorCommands();
        if (nextFrameCommands.length != frameCommands.length) {
            throw new ArrayIndexOutOfBoundsException("Frame-urile nu au aceleasi dimensiuni");
        }
        for (int i = 0; i < nextFrameCommands.length; i++) {
            if (!sameCommand(nextFrameCommands[i], frameCommands[i])) {
                List<Byte> toAdd = new ArrayList<>();
                for (int j = 0; j < nextFrameCommands[i].length; j++) {
                    toAdd.add(nextFrameCommands[i][j]);
                }
                list.add(toAdd);
            }
        }
        byte[][] toReturn = new byte[list.size()][];
        for (int i = 0; i < toReturn.length; i++) {
            toReturn[i] = new byte[list.get(i).size()];
            for (int j = 0; j < toReturn[i].length; j++) {
                toReturn[i][j] = list.get(i).get(j);
            }
        }
        return toReturn;
    }

}
