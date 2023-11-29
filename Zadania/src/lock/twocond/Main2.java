package lock.twocond;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class Main2 {
    public static int n = 10;
    public static int m = 0;
    public static Buffer buffer = new Buffer(200);
    public static Thread[] consumerThreads = new Thread[n];
    public static Thread[] producerThreads = new Thread[n];
    public static Consumer[] consumers = new Consumer[n];
    public static Producer[] producers = new Producer[n];
    public static void main(String[] args) throws InterruptedException {

        for (int i = 1; i < n; i++) {
            consumers[i] = new Consumer(i, m, buffer);
            producers[i] = new Producer(i, m, buffer);
            consumerThreads[i] = new Thread(consumers[i]);
            producerThreads[i] = new Thread(producers[i]);
        }
        consumers[0] = new Consumer(0, m, buffer);
        producers[0] = new Producer(0, m, buffer);
        consumerThreads[0] = new Thread(consumers[0]);
        producerThreads[0] = new Thread(producers[0]);
        for (int i = 0; i < n; i++) {
            consumerThreads[i].start();
            producerThreads[i].start();
        }
//        for (int i = 0; i < 30; i ++){
//
//            System.out.println("\n\n=======================");
//            for (int j = 0; j < n; j++) {
//                System.out.println("Producer - \t" + producers[j].ID +
//                        "\t Makes - " + producers[j].toMake + "   \t - Made  - " + producers[j].made);
//            }
//            System.out.println();
//            for (int j = 0; j < n; j++) {
//                System.out.println("Consumer - \t" + consumers[j].ID +
//                        "\t Takes - " + consumers[j].toTake + "   \t - Taken - " + consumers[j].taken);
//            }
//            Thread.sleep(1000);
//
//        }
        sleep(30000);
//            sleep(30000);
        try{
            printAverageTimes(buffer.capacity/2, n);
            printWaitingLoops(buffer.capacity/2, n);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        try {
            for (int i = 0; i < n; i++) {
                consumerThreads[i].join();
                producerThreads[i].join();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Left In Buffer: " + buffer.inBuffer);
    }
    public static void printAverageTimes(int bufferSize, int threadsNumber) throws IOException {
        System.out.println("******");
        List<List<Long>> getTimes = new ArrayList<>();
        List<List<Long>> putTimes = new ArrayList<>();
        for (int i = 0; i <= bufferSize; i++){
            getTimes.add(new ArrayList<>());
            putTimes.add(new ArrayList<>());
        }
        for (int i = 0; i < threadsNumber; i++){
            for (int j = 0; j <= bufferSize; j++){
                getTimes.get(j).addAll(consumers[i].timesList.get(j));
                putTimes.get(j).addAll(producers[i].timesList.get(j));
            }
        }

        List<Double> getMeans = new ArrayList<>();
        List<Double> putMeans = new ArrayList<>();
        for (int i = 0; i <= bufferSize; i++){
            long getSum = 0;
            long putSum = 0;
            for (Long val : getTimes.get(i)){
                getSum += val;
            }
            for (Long val : putTimes.get(i)){
                putSum += val;
            }
            double getMean = (double) (!getTimes.get(i).isEmpty() ? getSum / getTimes.get(i).size() : 0);

            double putMean = (double) (!putTimes.get(i).isEmpty() ? putSum / putTimes.get(i).size() : 0);

            getMeans.add(getMean);
            putMeans.add(putMean);
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter("times2.csv", true));
        for (int i = 1; i < bufferSize; i++){
            writer.write("taketwocond,"+bufferSize+","+threadsNumber+","+i+","+getMeans.get(i)+"\n");
            System.out.println("taketwocond,"+bufferSize+","+threadsNumber+","+i+","+getMeans.get(i));
        }

        for (int i = 1; i < bufferSize; i++){
            writer.write("puttwocond,"+bufferSize+","+threadsNumber+","+i+","+putMeans.get(i)+"\n");
            System.out.println("puttwocond,"+bufferSize+","+threadsNumber+","+i+","+putMeans.get(i));
        }

        writer.close();

    }
    public static void printWaitingLoops(int bufferSize, int threadsNumber) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("executions.csv", true));

        System.out.println("**** Number of productions / consumptions ****");
        for (int i = 0; i < threadsNumber; i++){
            if (producers[i] != null){
                System.out.println("Producer id: " + i + " number of executions: " + producers[i].made);
                writer.write("producertwocond,"+bufferSize+","+threadsNumber+","+i+","+producers[i].made+"\n");
            }
        }
        System.out.println("==========================================");
        for (int i = 0; i < threadsNumber; i++){
            if (consumers[i] != null) {
                System.out.println("Consumer id: " + i + " number of executions: " + consumers[i].taken);
                writer.write("consumertwocond,"+bufferSize+","+threadsNumber+","+i+","+consumers[i].taken+"\n");
            }
        }
        System.out.println("**** **** **** ****  ****  **** **** **** ****");

        writer.close();
    }
}