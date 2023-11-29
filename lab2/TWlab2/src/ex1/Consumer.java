package ex1;

public class Consumer implements Runnable{
    private final Buffer buffer;
    int ID;

    Consumer(int ID, Buffer buffer){
        this.ID = ID;
        this.buffer = buffer;
    }
    @Override
    public void run(){
        for (int i = 0; i < 100; i++) {
            buffer.take();
        }
    }
}
