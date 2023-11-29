public class Buffer {
    private final Stuff[] stuffs;;
    public int inBuffer = 0;
    private final int capacity;

    Buffer(int capacity){
        this.capacity = capacity;
        stuffs = new Stuff[capacity];
    }
    public synchronized void put(Stuff stuff){
        while (isFull()){
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("In Buffer: " + inBuffer + " Put   from: " + stuff.fromID + " Content: " + stuff.content);
        inBuffer += 1;

        stuffs[inBuffer-1] = stuff;
        notifyAll();
    }
    public synchronized void take(){
        while (isEmpty()){
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("In Buffer: " + inBuffer + " Taken from: " + stuffs[inBuffer-1].fromID +
                " Content: " + stuffs[inBuffer-1].content);
        inBuffer -= 1;
        notifyAll();

    }
    Boolean isEmpty(){
        return inBuffer == 0;
    }
    Boolean isFull(){
        return inBuffer == capacity;
    }
}
