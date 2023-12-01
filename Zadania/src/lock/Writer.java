package lock;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Writer {
    public static void clearFiles() throws IOException{
        BufferedWriter writer = new BufferedWriter(new FileWriter("times.csv", false));
        writer.write("bufferType,rngType,function,bufferSize,prodConsCount,putTakeChunk,meanTime\n");
        writer.close();
        BufferedWriter writer2 = new BufferedWriter(new FileWriter("executions.csv", false));
        writer2.write("bufferType,rngType,function,bufferSize,prodConsCount,threadID,executions\n");
        writer2.close();
    }
    public static void printAverageTimes(Producer[] producers, Consumer[] consumers, BufferType bufferType,
                                         RNGType rngType,
                                         int bufferSize, int threadsNumber) throws IOException {
        System.out.println("******");
        System.out.println("Printing Times For: " + bufferType + "," + rngType + "," + "take/put" + "," + bufferSize +
                ","  + threadsNumber);
        List<List<Long>> takeTimes = new ArrayList<>();
        List<List<Long>> putTimes = new ArrayList<>();
        for (int i = 0; i <= bufferSize; i++){
            takeTimes.add(new ArrayList<>());
            putTimes.add(new ArrayList<>());
        }

        for (int i = 0; i < threadsNumber; i++){
            for (int j = 0; j <= bufferSize; j++){
                takeTimes.get(j).addAll(consumers[i].timesList.get(j));
                putTimes.get(j).addAll(producers[i].timesList.get(j));
            }
        }

        List<Double> takeMeans = new ArrayList<>();
        List<Double> putMeans = new ArrayList<>();
        for (int i = 0; i <= bufferSize; i++){
            long getSum = 0;
            long putSum = 0;
            for (Long val : takeTimes.get(i)){
                getSum += val;
            }
            for (Long val : putTimes.get(i)){
                putSum += val;
            }
            double getMean = (double) (!takeTimes.get(i).isEmpty() ? getSum / takeTimes.get(i).size() : 0);

            double putMean = (double) (!putTimes.get(i).isEmpty() ? putSum / putTimes.get(i).size() : 0);

            takeMeans.add(getMean);
            putMeans.add(putMean);
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter("times.csv", true));
        for (int i = 1; i < bufferSize; i++){
            writer.write(bufferType + "," + rngType + "," + "take" + "," + bufferSize +
                            ","  + threadsNumber + ","  + (i+1) + ","  + takeMeans.get(i)+"\n");

//            System.out.println("taketwocond,"+bufferSize+","+threadsNumber+","+i+","+takeMeans.get(i));
        }

        for (int i = 1; i < bufferSize; i++){
            writer.write(bufferType + "," + rngType + "," + "put" + "," + bufferSize +
                    ","  + threadsNumber + ","  + (i+1) + ","  + putMeans.get(i)+"\n");
//            writer.write("puttwocond,"+bufferSize+","+threadsNumber+","+i+","+putMeans.get(i)+"\n");
//            System.out.println("puttwocond,"+bufferSize+","+threadsNumber+","+i+","+putMeans.get(i));
        }

        writer.close();

    }
    public static void printWaitingLoops(Producer[] producers, Consumer[] consumers, BufferType bufferType,
                                         RNGType rngType,
                                         int bufferSize, int threadsNumber) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("executions.csv", true));

        System.out.println("**** Number of productions / consumptions ****");
        for (int i = 0; i < threadsNumber; i++){
            if (producers[i] != null){
                writer.write(bufferType + "," + rngType + "," + "put" + "," + bufferSize +
                        ","  + threadsNumber + ","  + (i) + ","  + producers[i].made+"\n");
//                System.out.println("Producer id: " + i + " number of executions: " + producers[i].made);
//                writer.write("producertwocond,"+bufferSize+","+threadsNumber+","+i+","+producers[i].made+"\n");
            }
        }
        System.out.println("==========================================");
        for (int i = 0; i < threadsNumber; i++){
            if (consumers[i] != null) {
                writer.write(bufferType + "," + rngType + "," + "take" + "," + bufferSize +
                        ","  + threadsNumber + ","  + (i) + ","  + consumers[i].taken+"\n");
//                System.out.println("Consumer id: " + i + " number of executions: " + consumers[i].taken);
//                writer.write("consumertwocond,"+bufferSize+","+threadsNumber+","+i+","+consumers[i].taken+"\n");
            }
        }
        System.out.println("**** **** **** ****  ****  **** **** **** ****");

        writer.close();
    }
}
