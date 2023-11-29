package monitors.many;

public class Main {
    public static void main(String[] args) {
        int n = 2;
        Buffer buffer = new Buffer(1);
        Thread[] consumers = new Thread[n];
        Thread[] producers = new Thread[n];
        for (int i = 0; i < n; i++) {
            consumers[i] = new Thread(new Consumer(i, 1, buffer));
            producers[i] = new Thread(new Producer(i, 1, buffer));
        }
        for (int i = 0; i < n; i++) {
            consumers[i].start();
            producers[i].start();
        }


        try {
            for (int i = 0; i < n; i++) {
                consumers[i].join();
                producers[i].join();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}