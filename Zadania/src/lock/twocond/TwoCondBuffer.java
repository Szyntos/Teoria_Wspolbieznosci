package lock.twocond;

import lock.*;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class TwoCondBuffer implements LockBuffer {
    public int inBuffer = 0;
    public int capacity;
    ReentrantLock lock = new ReentrantLock();
    Condition bufferEmptyCond = lock.newCondition();
    Condition bufferFullCond = lock.newCondition();

    public TwoCondBuffer(int capacity){
        this.capacity = capacity;
    }
    public int getInBuffer(){
        return inBuffer;
    }
    public int getCapacity(){
        return capacity;
    }
    public void put(Producer producer, int count) throws BufferOverflowException {
        lock.lock();
        try {
            while (inBuffer + count > capacity) {
                producer.inLoop++;
                bufferEmptyCond.await();

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
        }catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }
    public void take(Consumer consumer, int count) throws BufferUnderflowException {
        lock.lock();
        try {
            while (inBuffer - count < 0){
                consumer.inLoop++;
                bufferFullCond.await();
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
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }
}
