package jcsp2.bufor_rozproszony;

import org.jcsp.lang.CSProcess;
import org.jcsp.lang.One2OneChannelInt;

import java.util.Random;


public class Consumer implements CSProcess {
//    private final One2OneChannelInt[] bufferChannels;
    private final One2OneChannelInt[] reqToBuffers;
    Random rng = new Random(0);
    int ID;
    long taken = 0;

    public Consumer(int ID, final One2OneChannelInt[] bufferChannels, final One2OneChannelInt[] reqToBuffers) {
        this.ID = ID;
        rng.setSeed(this.ID);
        this.reqToBuffers = reqToBuffers;
//        this.bufferChannels = bufferChannels;
    } // constructor

    public void run() {
        int item;
        int index;
        while (true) {
            index = rng.nextInt(reqToBuffers.length);
//            System.out.println("Cons index: "+index);
            reqToBuffers[index].out().write(1);
            item = reqToBuffers[index].in().read();
//            System.out.println("Consumer " + ID + " taken item: " + item + " from Buffer " + index);
            taken += 1;
        } // for
    } // run
} // class Consumer2