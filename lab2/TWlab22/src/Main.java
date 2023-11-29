public class Main {
    public static void main(String[] args) {
        Buffer buffer = new Buffer(1);
        Thread[] consumers = new Thread[10];
        Thread[] producers = new Thread[10];
        for (int i = 0; i < 10; i++) {
            consumers[i] = new Thread(new Consumer(i, buffer));
            producers[i] = new Thread(new Producer(i, buffer));
        }
        for (int i = 0; i < 10; i++) {
            consumers[i].start();
            producers[i].start();
        }


        try {
            for (int i = 0; i < 10; i++) {
                consumers[i].join();
                producers[i].join();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Left In Buffer: " + buffer.inBuffer);
    }
}