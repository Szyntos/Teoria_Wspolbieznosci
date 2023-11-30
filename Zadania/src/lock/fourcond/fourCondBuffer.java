package lock.fourcond;

import lock.*;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class FourCondBuffer implements LockBuffer {
    public int inBuffer = 0;
    public final int capacity;
    ReentrantLock lock = new ReentrantLock();
    Condition restConsumers = lock.newCondition();
    Condition firstConsumer = lock.newCondition();
    Condition restProducers = lock.newCondition();
    Condition firstProducer = lock.newCondition();


    boolean hasFirstProducer = false;
    boolean hasFirstConsumer = false;

    public void put(Producer producer, int count) throws BufferOverflowException {
        lock.lock();
        try{
            while (hasFirstProducer){
                restProducers.await();
            }
            hasFirstProducer = true;
            while (inBuffer + count > capacity){
                firstProducer.await();
            }
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
            Thread.currentThread().interrupt();
        }
        finally {
            lock.unlock();
        }
    }

    public void take(Consumer consumer, int count) throws BufferUnderflowException {

        lock.lock();

        try{

            while (hasFirstConsumer){
                restConsumers.await();
            }
            hasFirstConsumer = true;
            while (inBuffer - count < 0){
                firstConsumer.await();
            }
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

        } catch(InterruptedException e){
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }

    public FourCondBuffer(int capacity){
        this.capacity = capacity;

    }
    public int getInBuffer(){
        return inBuffer;
    }
    public int getCapacity(){
        return capacity;
    }
}
