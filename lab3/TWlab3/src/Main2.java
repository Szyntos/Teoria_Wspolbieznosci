public class Main2 {
    public static void main(String[] args) {
        Buffer buffer = new Buffer(200);
        int n = 4;
        Thread[] consumers = new Thread[n];
        Thread[] producers = new Thread[n];
        Consumer[] consumer = new Consumer[n];
        Producer[] producer = new Producer[n];
        for (int i = 1; i < n; i++) {
            consumer[i] = new Consumer(i, buffer);
            producer[i] = new Producer(i, buffer);
            consumers[i] = new Thread(consumer[i]);
            producers[i] = new Thread(producer[i]);
        }
        Producer differentProducer = new Producer(555, buffer);
        differentProducer.randInt = 100;
        consumers[0] = new Thread(new Consumer(0, buffer));
        producers[0] = new Thread(differentProducer);
        for (int i = 0; i < n; i++) {
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