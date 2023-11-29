package cztery;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class fourCondBuffer {
    private final Stuff[] stuffs;;
    public int inBuffer = 0;
    private final int capacity;
    ReentrantLock lock = new ReentrantLock();
    Condition consumerCond = lock.newCondition();
    Condition bufferFullCond = lock.newCondition();
    Condition bufferEmptyCond = lock.newCondition();
    Condition firstConsumerCond = lock.newCondition();
    Condition producerCond = lock.newCondition();
    Condition firstProducerCond = lock.newCondition();

    private int numWaitingProd = 0;
    private int numWaitingProdFirst = 0;
    private int numWaitingCons = 0;
    private int numWaitingConsFirst = 0;

    boolean waitingProducer = false;
    boolean waitingConsumer = false;

    public void put(int req, Stuff stuff, Producer person) {
        try{
            this.lock.lock();
            numWaitingProd++;
            while (this.waitingProducer){
                this.producerCond.await();
            }
            numWaitingProd--;
            numWaitingProdFirst++;
            while (inBuffer + req > capacity){
                this.waitingProducer = true;
                this.firstProducerCond.await();
            }
            if (numWaitingProdFirst > 0) numWaitingProdFirst--;
            person.inLoop++;
            for (int i = 0; i < req; i++) {
                inBuffer += 1;
                stuffs[inBuffer - 1] = stuff;
            }
            this.waitingProducer = false;
            this.producerCond.signal();
            this.firstConsumerCond.signal();
            if (1==1){
                System.out.println(person.ID + "\t inLoop: " + person.inLoop + " " + "In Buffer:\t" + inBuffer + " Put\t" + req + " from:\t" + stuff.fromID +
                        " Content:\t" + stuff.content);
            }
        }catch(InterruptedException ignored){

        }
        finally {
            this.lock.unlock();
        }
    }

    public void take(int req, Consumer person) {
        try{
            this.lock.lock();
            numWaitingCons++;
            while (this.waitingConsumer){
                this.consumerCond.await();
            }
            numWaitingCons--;

            while (inBuffer - req <= 0){
                this.waitingConsumer = true;
                this.firstConsumerCond.await();
            }
            if (numWaitingConsFirst > 0) numWaitingConsFirst--;
            this.waitingConsumer = false;


            person.inLoop++;
            inBuffer -= req;
            this.consumerCond.signal();
            this.firstProducerCond.signal();
            System.out.println(person.ID + "\t inLoop: " + person.inLoop + " " + "In Buffer:\t" + inBuffer + " Taken\t" + req + " from:\t" + stuffs[inBuffer-1].fromID +
                    " Content:\t" + stuffs[inBuffer-1].content);


        }catch(InterruptedException ignored){

        }finally {
            this.lock.unlock();
        }
    }

    fourCondBuffer(int capacity){
        this.capacity = capacity;
        stuffs = new Stuff[capacity];
    }
//    public void put(int n, Stuff stuff, Producer producer){
//        lock.lock();
//        try {
//            while (inBuffer + n > capacity) {
//                try {
//                    producer.inLoop++;
//                    bufferEmptyCond.await();
//                } catch (InterruptedException e) {
//                    Thread.currentThread().interrupt();
//                }
//            }
//            if (1==1){
//                System.out.println("In Buffer:\t" + inBuffer + " Put\t" + n + " from:\t" + stuff.fromID +
//                        " Content:\t" + stuff.content + " " + producer.inLoop + " " + producer.ID);
//            }
//            for (int i = 0; i < n; i++) {
//                inBuffer += 1;
//                stuffs[inBuffer - 1] = stuff;
//            }
//            bufferFullCond.signal();
//
//        } finally {
//            lock.unlock();
//        }
//    }
//    public void take(int n, Consumer consumer){
//        lock.lock();
//        try {
//            while (inBuffer - n <= 0){
//                try {
//                    consumer.inLoop++;
//                    bufferFullCond.await();
//                } catch (InterruptedException e) {
//                    Thread.currentThread().interrupt();
//                }
//            }
////            if (consumer.ID == 0 || consumer.ID == 3){
////                System.out.println("In Buffer:\t" + inBuffer + " Taken\t" + n + " from:\t" + stuffs[inBuffer-1].fromID +
////                        " Content:\t" + stuffs[inBuffer-1].content + " " + consumer.inLoop + " " + consumer.ID);
////            }
//
//            inBuffer -= n;
//            bufferEmptyCond.signal();
//        } finally {
//            lock.unlock();
//        }
//
//
//    }
    Boolean isEmpty(){
        return inBuffer == 0;
    }
    Boolean isFull(){
        return inBuffer == capacity;
    }
}
