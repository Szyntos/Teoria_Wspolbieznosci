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
        int prodCount = 1;
        int consCount = 7;
        int bufferCount = 3;
        int capacity = 4;
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
        int sum2 = 0;
        int sum = 0;
        for (int i = 0; i < 30;){
            System.out.println("\n\n=======================");
            sum = 0;
            for (int j = 0; j < producers.length; j++) {
                System.out.println("Producer - \t" + producers[j].ID + "   \t - Made  - " + producers[j].made);
                sum += producers[j].made;
            }
            System.out.println("Sum = " + sum);
            sum = 0;
            System.out.println();
            for (int j = 0; j < consumers.length; j++) {
                System.out.println("Consumer - \t" + consumers[j].ID + "\t - Taken - " + consumers[j].taken);
                sum += consumers[j].taken;
            }
            System.out.println("Sum = " + sum);
            sum = 0;
            sum2 = 0;
            System.out.println();
            for (int j = 0; j < buffers.length; j++) {
                System.out.println("Buffer - \t" + buffers[j].ID + "\t - sentToCons - " + buffers[j].sentToCons);
                System.out.println("Buffer - \t" + buffers[j].ID + "\t - takenFromProd - " + buffers[j].takenFromProd);
                sum += buffers[j].sentToCons;
                sum2 += buffers[j].takenFromProd;
            }
            System.out.println("Sum_taken = " + sum2 + ", Sum_sent = " + sum);
            Thread.sleep(1000);

        }
    }
}