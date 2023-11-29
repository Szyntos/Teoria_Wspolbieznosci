package ex1;

public class Producer implements Runnable{

    private final Buffer buffer;
    int ID;

    Producer(int ID, Buffer buffer){
        this.ID = ID;
        this.buffer = buffer;
    }
    @Override
    public void run(){
        for (int i = 0; i < 100; i++) {
            buffer.put(new Stuff(ID, Integer.toString(i)));
        }
    }
}
