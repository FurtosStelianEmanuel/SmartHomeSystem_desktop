/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smarthomesystem;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Manel
 */
public class Frames {

    private List<Frame> rawFrames;
    private LEDAnimationSender sender;

    public Frames() {
        try {
            rawFrames = new ArrayList<>();
            sender = SmartHomeSystem.getSmartHomeSystem().getAnimationSender();
        } catch (Exception ex) {
            //asta e din cauza ca netbeans da eroare, poate ar trebui sa schimb constructoru ce apeleaza Frames()
        }
    }
    
    public void add(Frame frame) {
        rawFrames.add(frame);
    }

    public Frame get(int index) {
        return rawFrames.get(index);
    }

    public void set(int index,Frame f){
        rawFrames.set(index, f);
    }
    
    public void clear() {
        rawFrames.clear();
    }

    public int size() {
        return rawFrames.size();
    }

    private byte[][] getOptimizedCommands(int index) {
        if (index == 0) {
            //daca e la primul frame, consideram ca mereu inainte sa inceapa primul frame ledurile sunt stinse
            //si optimizam trimitand doar led-urile care nu sunt stinse din prima secventa
            //in viitor ar trebui adaugata si optimizarea prin siruri de culori
            int diffByteCount = 0;
            int sameColorByteCount = 0;

            byte[][] rawCommands = rawFrames.get(0).getRaw8BitColorCommands();
            List<List<Byte>> listaFinala = new ArrayList<>();
            for (int i = 0; i < rawCommands.length; i++) {
                List<Byte> comanda = new ArrayList<>();
                if (rawCommands[i][1] != 0) {
                    for (int j = 0; j < rawCommands[i].length; j++) {
                        comanda.add(rawCommands[i][j]);
                    }
                    listaFinala.add(comanda);
                }
            }
            byte[][] diffByteCommands = Frame.getArray(listaFinala);
            for (int i = 0; i < diffByteCommands.length; i++) {
                diffByteCount += diffByteCommands[i].length;
            }
            byte[][] sameColorCommands = rawFrames.get(0).getColorOptimizedCommands(rawFrames.get(0));
            for (int i = 0; i < sameColorCommands.length; i++) {
                sameColorByteCount += sameColorCommands[i].length;
            }
            if (sameColorByteCount < diffByteCount) {
                System.out.println("La primul frame am ales same color");
                return sameColorCommands;
            }
            System.out.println("La primul frame am ales dif Color ");
            return diffByteCommands;
        } else {
            return rawFrames.get(index - 1).getOptimizedCommands(rawFrames.get(index));
        }
    }

    public void play() throws InterruptedException {
        sender.sendTurnOffAndUpdateCommand();
        for (int i = 0; i < rawFrames.size(); i++) {
            if (rawFrames.get(i).sleepTimeBefore > -1) {
                Thread.sleep(rawFrames.get(i).sleepTimeBefore);
            }
            byte[][] optimizedCommand = getOptimizedCommands(i);
            for (int j = 0; j < optimizedCommand.length; j++) {
                System.out.println("Trimit comanda optima cu i=" + i + " ->" + optimizedCommand[j].length);
                sender.sendAnimationCommand(optimizedCommand[j]);
            }
            sender.sendUpdateCommand();
            Thread.sleep(25);
            if (rawFrames.get(i).sleepTimeAfter > -1) {
                Thread.sleep(rawFrames.get(i).sleepTimeAfter);
            }
        }
    }

}
