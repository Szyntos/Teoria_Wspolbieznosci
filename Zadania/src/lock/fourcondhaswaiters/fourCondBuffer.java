package lock.fourcondhaswaiters;

import monitors.one.BufferOverflowException;
import monitors.one.BufferUnderflowException;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class fourCondBuffer {
    public int inBuffer = 0;
    public final int capacity;
    ReentrantLock lock = new ReentrantLock();
    Condition restConsumers = lock.newCondition();
    Condition firstConsumer = lock.newCondition();
    Condition restProducers = lock.newCondition();
    Condition firstProducer = lock.newCondition();

    private int countRestProducers = 0;
    private int countFirstProducer = 0;
    private int countRestConsumers = 0;
    private int countFirstConsumer = 0;

    boolean hasFirstProducer = false;
    boolean hasFirstConsumer = false;

    public void put(Producer producer, int count) throws BufferOverflowException{
        lock.lock();
        try{
            countRestProducers++;
            while (lock.hasWaiters(firstProducer)){
                restProducers.await();
            }
            countRestProducers--;
            countFirstProducer++;
            hasFirstProducer = true;
            while (inBuffer + count > capacity){
                firstProducer.await();
            }
            countFirstProducer--;
            hasFirstProducer = false;

            if (inBuffer + count <= capacity){
                inBuffer += count;
            } else {
                System.out.println("ERROR");
                throw new BufferOverflowException("Buffer Capacity Exceded");
            }
            producer.made++;
            restProducers.signal();
            firstConsumer.signal();

        }catch(InterruptedException e){
            throw new RuntimeException(e);
        }
        finally {
            lock.unlock();
        }
    }

    public void take(Consumer consumer, int count) throws BufferUnderflowException {
        lock.lock();
        try{
            countRestConsumers++;
            while (lock.hasWaiters(firstConsumer)){
                restConsumers.await();
            }
            countRestConsumers--;
            countFirstConsumer++;
            hasFirstConsumer = true;
            while (inBuffer - count < 0){
                firstConsumer.await();
            }
            countFirstConsumer--;
            hasFirstConsumer = false;

            if (inBuffer - count >= 0){
                inBuffer -= count;
            } else {
                System.out.println("ERROR");
                throw new BufferUnderflowException("Buffer Capacity Exceded");

            }
            consumer.taken++;
            restConsumers.signal();
            firstProducer.signal();

        }catch(InterruptedException e){
            throw new RuntimeException(e);
        }finally {
            lock.unlock();
        }
    }

    fourCondBuffer(int capacity){
        this.capacity = capacity;

    }
    Boolean isEmpty(){
        return inBuffer == 0;
    }
    Boolean isFull(){
        return inBuffer == capacity;
    }
}
