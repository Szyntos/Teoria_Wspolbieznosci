package monitors.many;

import monitors.one.BufferOverflowException;
import monitors.one.BufferUnderflowException;

public class Buffer {
    public int inBuffer = 0;
    public final int capacity;

    public Buffer(int capacity){
        this.capacity = capacity;
    }
    public synchronized void put(Producer producer, int count) throws BufferOverflowException {
        while (inBuffer + count > capacity){
            try {
                wait();
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
        System.out.println("Prod:\t" + producer.ID +" Put: \t" +count + " In Buffer:\t" + inBuffer);
        notify();
    }
    public synchronized void take(Consumer consumer, int count) throws BufferUnderflowException {
        while (inBuffer - count < 0){
            try {
                wait();
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
        System.out.println("Cons:\t" + consumer.ID +" Took:\t" +count + " In Buffer:\t" + inBuffer);
        notify();

    }
    Boolean isEmpty(){
        return inBuffer == 0;
    }
    Boolean isFull(){
        return inBuffer == capacity;
    }
}
