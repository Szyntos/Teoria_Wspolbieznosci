package lock;

public interface LockBuffer {
    int getCapacity();
    int getInBuffer();
    void put(Producer producer, int count) throws BufferOverflowException;
    void take(Consumer consumer, int count)throws BufferUnderflowException;
}
