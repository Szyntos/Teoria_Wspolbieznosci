package lock;

public interface LockBuffer {
    int inBuffer = 0;
    int capacity = 1;
    void put(Producer producer, int count) throws BufferOverflowException;
    void take(Consumer consumer, int count)throws BufferUnderflowException;
}
