package monitors.many;

public class Main {
    public static void main(String[] args) {
//        int n = 2;
        int noConsumers = 1;
        int noPrdo = 4;
        Buffer buffer = new Buffer(2);
        Thread[] consumers = new Thread[noConsumers];
        Thread[] producers = new Thread[noPrdo];
        for (int i = 0; i < noConsumers; i++) {
            consumers[i] = new Thread(new Consumer(i, 1, buffer));

        }
        for (int i = 0; i < noPrdo; i++) {
            producers[i] = new Thread(new Producer(i, 1, buffer));

        }
        for (int i = 0; i < noPrdo; i++) {
            producers[i].start();
        }
        for (int i = 0; i < noConsumers; i++) {
            consumers[i].start();
        }


//        try {
//            for (int i = 0; i < n; i++) {
//                consumers[i].join();
//                producers[i].join();
//            }
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }
    }
}