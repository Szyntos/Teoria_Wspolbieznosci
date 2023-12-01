package lock;

import lock.fourcond.FourCondBuffer;
import lock.fourcondhaswaiters.FourCondBufferHasWaiters;
import lock.threelocks.ThreeLockBuffer;
import lock.twocond.TwoCondBuffer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class Main2 {
    public static int threadCount = 10;
    public static int bufferCapacity = 200;
    public static int m = 0;
    public static int testingTime = 1000 * 60 * 3;
    public static BufferType bufferType;
    public static RNG seedRNG;
    public static LockBuffer buffer;
    public static Thread[] consumerThreads = new Thread[threadCount];
    public static Thread[] producerThreads = new Thread[threadCount];
    public static Consumer[] consumers = new Consumer[threadCount];
    public static Producer[] producers = new Producer[threadCount];
    public static void main(String[] args) throws InterruptedException {
        createFile();
        for (BufferType bufferType : BufferType.values()) {
            for (RNGType rngType : RNGType.values()) {
                if (bufferType == BufferType.FOURCONDHASWAITERS){
                    continue;
                }
                System.out.println("Testing " + bufferType + " - " + rngType + " for " + testingTime/1000 + " [s]");
                testBuffer(bufferType, rngType, testingTime);
                writeResultsToFile(bufferType, rngType);
            }

        }
        System.out.println("Program Has Ended, Thank You");
    }

    public static void testBuffer(BufferType bufferType, RNGType rngType, int time) throws InterruptedException {
        seedRNG = new RNG(RNGType.RANDOMRANDOM, 23);
        switch (bufferType){
            case THREELOCKS -> buffer = new ThreeLockBuffer(bufferCapacity);
            case TWOCOND -> buffer = new TwoCondBuffer(bufferCapacity);
            case FOURCOND -> buffer = new FourCondBuffer(bufferCapacity);
            case FOURCONDHASWAITERS -> new FourCondBufferHasWaiters(bufferCapacity);
        }
        for (int i = 0; i < threadCount; i++) {
            consumers[i] = new Consumer(i, m, buffer, seedRNG.randomInt(0, 10000));
            producers[i] = new Producer(i, m, buffer, seedRNG.randomInt(0, 10000));
            consumers[i].setRngType(rngType);
            producers[i].setRngType(rngType);
            consumerThreads[i] = new Thread(consumers[i]);
            producerThreads[i] = new Thread(producers[i]);
        }
        for (int i = 0; i < threadCount; i++) {
            consumerThreads[i].start();
            producerThreads[i].start();
        }
        sleep(time);
        for (int i = 0; i < threadCount; i++) {
            consumerThreads[i].interrupt();
            producerThreads[i].interrupt();
        }
    }
    public static void writeResultsToFile(BufferType bufferType, RNGType rngType){
        try{
            Writer.printAverageTimes(producers, consumers, bufferType, rngType,
                    buffer.getCapacity()/2, threadCount);
            Writer.printWaitingLoops(producers, consumers, bufferType, rngType,
                    buffer.getCapacity()/2, threadCount);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    public static void createFile() {
        try{
            Writer.clearFiles();
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}