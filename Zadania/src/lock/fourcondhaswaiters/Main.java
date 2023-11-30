package lock.fourcondhaswaiters;

import lock.Consumer;
import lock.Producer;
import lock.RNG;
import lock.RNGType;

public class Main {
    public static RNG seedRNG = new RNG(RNGType.RANDOMRANDOM, 0);
    public static void main(String[] args) {
        FourCondBufferHasWaiters buffer = new FourCondBufferHasWaiters(1);
        Thread consumer = new Thread(new Consumer(0, 1, buffer, seedRNG.randomInt(0, 10000)));
        Thread producer = new Thread(new Producer(0, 1, buffer, seedRNG.randomInt(0, 10000)));
        consumer.start();
        producer.start();

        try {
            producer.join();
            consumer.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}