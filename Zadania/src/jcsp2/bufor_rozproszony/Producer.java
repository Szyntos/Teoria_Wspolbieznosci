package jcsp2.bufor_rozproszony;

import org.jcsp.lang.Alternative;
import org.jcsp.lang.CSProcess;
import org.jcsp.lang.Guard;
import org.jcsp.lang.One2OneChannelInt;


public class Producer implements CSProcess {
    private final One2OneChannelInt[] bufferChannels;
    private final One2OneChannelInt[] reqFromBuffers;
    int ID;
    long made = 0;

    public Producer(int ID, final One2OneChannelInt[] bufferChannels, final One2OneChannelInt[] reqFromBuffers) {
        this.ID = ID;
        this.bufferChannels = bufferChannels;
        this.reqFromBuffers = reqFromBuffers;
    } // constructor

    public void run() {
        int item;
        int index;
        Guard[] guards = new Guard[reqFromBuffers.length];
        for (int i = 0; i < reqFromBuffers.length; i++) {
            guards[i] = reqFromBuffers[i].in();
        }
        Alternative alt = new Alternative(guards);
        while (true){
            index = alt.select();
            reqFromBuffers[index].in().read();
//            item = (int) (Math.random() * 100) + 1;
            item = ID;
            bufferChannels[index].out().write(item);
            this.made += 1;
//            System.out.println("Producer " + ID + " sent item: " + item + " to Buffer " + index);

        }
    } // run
} // class Producer2