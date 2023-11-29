import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Buffer {
    private final Stuff[] stuffs;;
    public int inBuffer = 0;
    private final int capacity;
    ReentrantLock lock = new ReentrantLock();
    Condition bufferEmptyCond = lock.newCondition();
    Condition bufferFullCond = lock.newCondition();

    Buffer(int capacity){
        this.capacity = capacity;
        stuffs = new Stuff[capacity];
    }
    public void put(int n, Stuff stuff, Producer producer){
        lock.lock();
        try {
            while (inBuffer + n > capacity) {
                try {
                    producer.inLoop++;
                    bufferEmptyCond.await();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            if (1==1){
                System.out.println("In Buffer:\t" + inBuffer + " Put\t" + n + " from:\t" + stuff.fromID +
                        " Content:\t" + stuff.content + " " + producer.inLoop + " " + producer.ID);
            }
            for (int i = 0; i < n; i++) {
                inBuffer += 1;
                stuffs[inBuffer - 1] = stuff;
            }
            bufferFullCond.signal();

        } finally {
            lock.unlock();
        }
    }
    public void take(int n, Consumer consumer){
        lock.lock();
        try {
            while (inBuffer - n <= 0){
                try {
                    consumer.inLoop++;
                    bufferFullCond.await();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
//            if (consumer.ID == 0 || consumer.ID == 3){
//                System.out.println("In Buffer:\t" + inBuffer + " Taken\t" + n + " from:\t" + stuffs[inBuffer-1].fromID +
//                        " Content:\t" + stuffs[inBuffer-1].content + " " + consumer.inLoop + " " + consumer.ID);
//            }

            inBuffer -= n;
            bufferEmptyCond.signal();
        } finally {
            lock.unlock();
        }


    }
    Boolean isEmpty(){
        return inBuffer == 0;
    }
    Boolean isFull(){
        return inBuffer == capacity;
    }
}
