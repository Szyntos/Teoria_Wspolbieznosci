package lock.threelocks;

import monitors.one.BufferOverflowException;
import monitors.one.BufferUnderflowException;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ThreeLockBuffer {
    public int inBuffer = 0;
    public final int capacity;
    ReentrantLock commonLock = new ReentrantLock();
    ReentrantLock producerLock = new ReentrantLock();
    ReentrantLock consumerLock = new ReentrantLock();
    Condition waiting = commonLock.newCondition();

    ThreeLockBuffer(int capacity){
        this.capacity = capacity;
    }
    public void put(Producer producer, int count) throws BufferOverflowException{
        producerLock.lock();
        commonLock.lock();
        try {
            while (inBuffer + count > capacity) {
                try {
                    producer.inLoop++;
                    waiting.await();
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
//            Thread.sleep(1);
//            System.out.println("Prod:\t" + producer.ID + " In Loop:\t" +producer.inLoop +" Put: \t" +count + " In Buffer:\t" + inBuffer);
            waiting.signal();
        } finally {
            commonLock.unlock();
            producerLock.unlock();
        }
    }
    public void take(Consumer consumer, int count) throws BufferUnderflowException{
        consumerLock.lock();
        commonLock.lock();
        try {

            while (inBuffer - count < 0){
                try {
                    consumer.inLoop++;
                    waiting.await();
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
//            Thread.sleep(1);
            consumer.taken += 1;
//            System.out.println("Con: \t" + consumer.ID + " In Loop:\t" +consumer.inLoop +" Put: \t" + count + " In Buffer:\t" + inBuffer);
            waiting.signal();
        } finally {
            commonLock.unlock();
            consumerLock.unlock();
        }


    }
}
