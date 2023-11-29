package cztery;

public class Main2 {
    public static void main(String[] args) {
        fourCondBuffer buffer = new fourCondBuffer(200);
        Thread[] consumers = new Thread[4];
        Thread[] producers = new Thread[4];
        Consumer[] consumer = new Consumer[4];
        Producer[] producer = new Producer[4];
        for (int i = 1; i < 4; i++) {
            consumer[i] = new Consumer(i, buffer);
            producer[i] = new Producer(i, buffer);
            consumers[i] = new Thread(consumer[i]);
            producers[i] = new Thread(producer[i]);
        }
        Producer differentProducer = new Producer(555, buffer);
        differentProducer.randInt = 100;
        consumers[0] = new Thread(new Consumer(0, buffer));
        producers[0] = new Thread(differentProducer);
        for (int i = 0; i < 4; i++) {
            consumers[i].start();
            producers[i].start();
        }


        try {
            for (int i = 0; i < 4; i++) {
                consumers[i].join();
                producers[i].join();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Left In Buffer: " + buffer.inBuffer);
    }
}