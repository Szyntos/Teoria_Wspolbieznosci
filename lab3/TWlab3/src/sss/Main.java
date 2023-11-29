package sss;


public class Main {
    public static int messagesNumber = 2000;
    public static int threadsNumber = 5;
    public static int bufferSize = 10;

    public static Consumer[] consumers = new Consumer[threadsNumber];
    public static Producer[] producers = new Producer[threadsNumber];

    public static Thread[] consumersTh = new Thread[threadsNumber];
    public static Thread[] producersTh = new Thread[threadsNumber];

    public static void main(String[] args) {
        Buffer buffer = new Buffer(bufferSize*2);

        int j = 0;
        while (j < threadsNumber){
//            j = 0;
            consumers[j] = new Consumer(j, buffer);
            producers[j] = new Producer(j, buffer);
            consumersTh[j] = new Thread(consumers[j]);
            producersTh[j] = new Thread(producers[j]);

            producersTh[j].start();
            consumersTh[j].start();
            j++;
        }

        for (int i = 0; i < threadsNumber; i++){
            try {
                producersTh[i].join();
                consumersTh[i].join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

    }

    public static void printWaitingLoops(){
        System.out.println("**** Statistics of waiting loops ****");
        for (int i = 0; i < threadsNumber; i++){
            if (producers[i] != null){
                System.out.println("Producer id: " + i + " loops in wait: " + producers[i].getWaitLoops());
            }
        }
        System.out.println("==================================");
        for (int i = 0; i < threadsNumber; i++){
            if (consumers[i] != null) {
                System.out.println("Consumer id: " + i + " loops in wait: " + consumers[i].getWaitLoops());
            }
        }
        System.out.println("**** **** **** **** **** **** ****");
    }
}
