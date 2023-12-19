package jcsp2.bufor_rozproszony;

import org.jcsp.lang.Alternative;
import org.jcsp.lang.CSProcess;
import org.jcsp.lang.Guard;
import org.jcsp.lang.One2OneChannelInt;

import java.util.Random;

public class Buffer implements CSProcess {
    private final One2OneChannelInt[] reqToProd;
    private final One2OneChannelInt[] channelFromProd;
    private final One2OneChannelInt[] reqFromCons;
    private final One2OneChannelInt[] channelToCons;
    int ID;
    int capacity;
    int inBuffer = 0;
    long takenFromProd = 0;
    long sentToCons = 0;
    Random rng = new Random(0);


    public Buffer(int ID, int capacity, final One2OneChannelInt[] reqToProd, final One2OneChannelInt[] channelFromProd,
                  final One2OneChannelInt[] reqFromCons, final One2OneChannelInt[] channelToCons) {
        this.ID = ID;
        rng.setSeed(this.ID);
        this.capacity = capacity;
        this.reqToProd = reqToProd;
        this.channelFromProd = channelFromProd;
        this.reqFromCons = reqFromCons;
        this.channelToCons = channelToCons;
    }

    public void run() {
        final Guard[] guardCons = new Guard[reqFromCons.length];
        for (int i = 0; i < reqFromCons.length; i++) {
            guardCons[i] = reqFromCons[i].in();
        }
        final Alternative altCons = new Alternative(guardCons);
        int index = 0;
        int item = 0;
        boolean prodSelected = false;
        int toPut = 0;
        while (true){
            if (inBuffer == 0){
                index = rng.nextInt(reqToProd.length);
                prodSelected = true;
            }else if (inBuffer == capacity){
                index = altCons.select();
                prodSelected = false;
            }else if (rng.nextInt() % 2 == 0){
                index = altCons.select();
                prodSelected = false;
            }else{
                index = rng.nextInt(reqToProd.length);
                prodSelected = true;
            }
            if (prodSelected){
                reqToProd[index].out().write(1);
                item = channelFromProd[index].in().read();
                inBuffer += 1;
                this.takenFromProd += 1;
//                System.out.println("Buffer " + ID + " taken item: " + item + " from Producer " +
//                        index + " inBuffer " + inBuffer);

            }else{
                item = reqFromCons[index].in().read();
                channelToCons[index].out().write(1);
                inBuffer -= 1;
                this.sentToCons += 1;
//                System.out.println("Buffer " + ID + " sent item: " + item + " to Consumer " +
//                        index + " inBuffer " + inBuffer);
            }

        }
    } // run
} // class Buffer