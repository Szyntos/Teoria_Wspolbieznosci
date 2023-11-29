package sss;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Buffer {
    private final int bufferSize;
    private int messagesInBuffer;

    private final ReentrantLock lock;
    private final Condition consumers;
    private final Condition producers;

    private final Condition firstConsumer;

    private final Condition firstProducer;

    private boolean hasFirstProducer = false;
    private boolean hasFirstConsumer = false;

    private int numWaitingProd = 0;
    private int numWaitingProdFirst = 0;
    private int numWaitingCons = 0;
    private int numWaitingConsFirst = 0;


    public Buffer(int bufferSize){
        this.bufferSize = bufferSize;
        this.lock = new ReentrantLock();
        this.consumers = lock.newCondition();
        this.producers = lock.newCondition();
        this.firstConsumer = lock.newCondition();
        this.firstProducer = lock.newCondition();
        this.messagesInBuffer = 0;
    }

    public void put(int toPut, Producer pr) {
        lock.lock();
        System.out.println("inside produce");
        try {
            numWaitingProd++;
            while (hasFirstProducer){
                System.out.println("Producer " + pr.id + " is waiting on condition producers with " + numWaitingProd);
//                pr.waitLoops++;
                producers.await();
            }

            numWaitingProd--;

            numWaitingProdFirst++;
            while (messagesInBuffer + toPut > bufferSize){
//                pr.waitLoops++;
                System.out.println("Producer " + pr.id + " is waiting on condition First producer " + numWaitingProdFirst);
                hasFirstProducer = true;
                firstProducer.await();
//                hasFirstProducer = false;
            }
            if (numWaitingProdFirst > 0) numWaitingProdFirst--;

            System.out.println(">> Producer " + pr.id + " put " + toPut);
            this.messagesInBuffer += toPut;
            hasFirstProducer = false;
            producers.signal();
            firstConsumer.signal();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }

    }

    public synchronized int take(int toTake, Consumer co) {
        lock.lock();
        System.out.println("inside take");
        try{
            numWaitingCons++;
            while (hasFirstConsumer){
//                co.waitLoops++;
                System.out.println("Consumer " + co.id + " is waiting on condition consumers with " + numWaitingCons);
                consumers.await();
            }
            numWaitingCons--;

            numWaitingConsFirst++;

            while (messagesInBuffer - toTake < 0){
//                co.waitLoops++;
                System.out.println("Consumer " + co.id + " is waiting on condition consumer First with " + numWaitingConsFirst + "  " +  numWaitingCons);
                hasFirstConsumer = true;
                firstConsumer.await();
            }
            if (numWaitingConsFirst > 0) numWaitingConsFirst--;


            messagesInBuffer -= toTake;
            System.out.println("<< Consumer " + co.id + " taken " + toTake);
            hasFirstConsumer = false;
            consumers.signal();
            firstProducer.signal();

            return toTake;

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();

        }

    }
}
