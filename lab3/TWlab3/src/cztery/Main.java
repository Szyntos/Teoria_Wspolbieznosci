package cztery;

public class Main {
    public static void main(String[] args) {
        fourCondBuffer buffer = new fourCondBuffer(1);
        Thread consumer = new Thread(new Consumer(0, buffer));
        Thread producer = new Thread(new Producer(0, buffer));
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