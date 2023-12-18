package jcsp2.bufor_rozproszony;

import org.jcsp.lang.CSProcess;
import org.jcsp.lang.Channel;
import org.jcsp.lang.One2OneChannelInt;
import org.jcsp.lang.Parallel;


public final class Main {
    public static void main(String[] args) throws InterruptedException {
        PCMain2();
    } // main

    public static void PCMain2() throws InterruptedException{
        int prodCount = 5;
        int bufferCount = 15;
        int consCount = 5;
        int capacity = 1;
        final One2OneChannelInt[][] bufferChannelsProd = new One2OneChannelInt[bufferCount][prodCount];
        final One2OneChannelInt[][] bufferChannelsProdReq = new One2OneChannelInt[bufferCount][prodCount];
        final One2OneChannelInt[][] bufferChannelsCons = new One2OneChannelInt[bufferCount][consCount];
        final One2OneChannelInt[][] bufferChannelsConsReq = new One2OneChannelInt[bufferCount][consCount];


        for (int i = 0; i < bufferCount; i++) {
            for (int j = 0; j < prodCount; j++) {
                bufferChannelsProd[i][j] = Channel.one2oneInt();
                bufferChannelsProdReq[i][j] = Channel.one2oneInt();
            }
            for (int j = 0; j < consCount; j++) {
                bufferChannelsCons[i][j] = Channel.one2oneInt();
                bufferChannelsConsReq[i][j] = Channel.one2oneInt();
            }
        }

        Buffer[] buffers = new Buffer[bufferCount];
        Producer[] producers = new Producer[prodCount];
        Consumer[] consumers = new Consumer[consCount];

        for (int i = 0; i < bufferCount; i++) {
            buffers[i] = new Buffer(i, capacity, bufferChannelsProdReq[i],
                    bufferChannelsProd[i], bufferChannelsConsReq[i], bufferChannelsCons[i]);

        }
        for (int i = 0; i < prodCount; i++) {
            One2OneChannelInt[] bufferChannels = new One2OneChannelInt[bufferCount];
            One2OneChannelInt[] reqFromBuffers = new One2OneChannelInt[bufferCount];
            for (int j = 0; j < bufferCount; j++) {
                bufferChannels[j] = bufferChannelsProd[j][i];
                reqFromBuffers[j] = bufferChannelsProdReq[j][i];
            }
            producers[i] = new Producer(i, bufferChannels, reqFromBuffers);

        }
        for (int i = 0; i < consCount; i++) {
            One2OneChannelInt[] bufferChannels = new One2OneChannelInt[bufferCount];
            One2OneChannelInt[] reqToBuffers = new One2OneChannelInt[bufferCount];
            for (int j = 0; j < bufferCount; j++) {
                bufferChannels[j] = bufferChannelsCons[j][i];
                reqToBuffers[j] = bufferChannelsConsReq[j][i];
            }
            consumers[i] = new Consumer(i, bufferChannels, reqToBuffers);

        }

        CSProcess[] procList = new CSProcess[prodCount + consCount + bufferCount];
        System.arraycopy(producers, 0, procList, 0, prodCount);
        System.arraycopy(consumers, 0, procList, prodCount, consCount);
        System.arraycopy(buffers, 0, procList, prodCount + consCount, bufferCount);

        Runnable myThread = () -> {
            try {
                printStats(producers, consumers, buffers);
            } catch (InterruptedException e){
               throw new RuntimeException(e);
            }
        };

        Thread run = new Thread(myThread);


        run.start();
        Parallel par = new Parallel(procList);
        par.run();
    }
    static void printStats(Producer[] producers, Consumer[] consumers, Buffer[] buffers) throws InterruptedException {
        int sumMade = 0;
        int sumTaken = 0;
        int sumSentToCons = 0;
        int sumTakenFromProd = 0;
        for (int i = 0; i < 30;){
            System.out.println("\n\n=======================");
            for (Producer producer : producers) {
                System.out.println("Producer - \t" + producer.ID + "   \t - Made  - " + producer.made);
                sumMade += producer.made;
            }
            System.out.println();
            for (Buffer buffer : buffers) {
                System.out.println("Buffer - \t" + buffer.ID + "\t - sentToCons - " + buffer.sentToCons);
                sumSentToCons += buffer.sentToCons;
            }
            System.out.println();
            for (Buffer buffer : buffers) {
                System.out.println("Buffer - \t" + buffer.ID + "\t - takenFromProd - " + buffer.takenFromProd);
                sumTakenFromProd += buffer.takenFromProd;
            }
            System.out.println();
            for (Consumer consumer : consumers) {
                System.out.println("Consumer - \t" + consumer.ID + "\t - Taken - " + consumer.taken);
                sumTaken += consumer.taken;
            }
            System.out.println();

            System.out.println("Sum_made = " + sumMade);
            System.out.println("Sum_taken = " + sumTaken);
            System.out.println("Sum_takenFromProd = " + sumTakenFromProd + ", Sum_sentToCons = " + sumSentToCons);
            Thread.sleep(1000);

        }
    }
}