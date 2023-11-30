package lock.threelocks;

import lock.*;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ThreeLockBuffer implements LockBuffer {
    public int inBuffer = 0;
    public final int capacity;
    ReentrantLock commonLock = new ReentrantLock();
    ReentrantLock producerLock = new ReentrantLock();
    ReentrantLock consumerLock = new ReentrantLock();
    Condition waiting = commonLock.newCondition();

    public ThreeLockBuffer(int capacity){
        this.capacity = capacity;
    }
    public int getInBuffer(){
        return inBuffer;
    }
    public int getCapacity(){
        return capacity;
    }
    public void put(Producer producer, int count) throws BufferOverflowException {
        producerLock.lock();
        commonLock.lock();
        try {
            while (inBuffer + count > capacity) {
                producer.inLoop++;
                waiting.await();
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
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            commonLock.unlock();
            producerLock.unlock();
        }
    }
    public void take(Consumer consumer, int count) throws BufferUnderflowException {
        consumerLock.lock();
        commonLock.lock();
        try {

            while (inBuffer - count < 0){
                consumer.inLoop++;
                waiting.await();
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
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            commonLock.unlock();
            consumerLock.unlock();
        }


    }
}
