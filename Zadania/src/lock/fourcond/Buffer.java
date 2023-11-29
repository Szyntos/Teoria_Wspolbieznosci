package lock.fourcond;

import lock.*;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Buffer implements LockBuffer{
    public int inBuffer = 0;
    private final int capacity;
    ReentrantLock lock = new ReentrantLock();
    Condition bufferEmptyCond = lock.newCondition();
    Condition bufferFullCond = lock.newCondition();

    Buffer(int capacity){
        this.capacity = capacity;
    }
    public void put(Producer producer, int count) throws BufferOverflowException {
        lock.lock();
        try {
            while (inBuffer + count > capacity) {
                try {
                    producer.inLoop++;
                    bufferEmptyCond.await();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            if (inBuffer + count <= capacity){
                inBuffer += count;
            } else {
                System.out.println("ERROR");
                throw new BufferOverflowException("Buffer Capacity Exceded");
            }
            producer.made += 1;
//            System.out.println("Prod:\t" + producer.ID + " In Loop:\t" +producer.inLoop +" Put: \t" +count + " In Buffer:\t" + inBuffer);
            bufferFullCond.signal();
        } finally {
            lock.unlock();
        }
    }
    public void take(Consumer consumer, int count) throws BufferUnderflowException {
        lock.lock();
        try {
            while (inBuffer - count < 0){
                try {
                    consumer.inLoop++;
                    bufferFullCond.await();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            if (inBuffer - count >= 0){
                inBuffer -= count;
            } else {
                System.out.println("ERROR");
                throw new BufferUnderflowException("Buffer Capacity Exceded");

            }
            consumer.taken += 1;
//            System.out.println("Con: \t" + consumer.ID + " In Loop:\t" +consumer.inLoop +" Put: \t" + count + " In Buffer:\t" + inBuffer);
            bufferEmptyCond.signal();
        } finally {
            lock.unlock();
        }


    }
}
