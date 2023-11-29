package lock.threelocks;

public class Main {
    public static void main(String[] args) {
        ThreeLockBuffer buffer = new ThreeLockBuffer(1);
        Thread consumer = new Thread(new Consumer(0, 1, buffer));
        Thread producer = new Thread(new Producer(0, 1, buffer));
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