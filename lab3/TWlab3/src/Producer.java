import java.util.Random;

public class Producer implements Runnable{

    private final Buffer buffer;
    int ID;
    Random rand = new Random();
    int inLoop = 0;
    int randInt;

    Producer(int ID, Buffer buffer){
        this.ID = ID;
        this.buffer = buffer;
        this.randInt = 1;
    }
    @Override
    public void run(){
        for (int i = 0; i < 100; ) {
            buffer.put(this.randInt, new Stuff(ID, Integer.toString(i)), this);

        }
    }
}
