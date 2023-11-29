package cztery;

import java.util.Random;

public class Consumer implements Runnable{
    private final fourCondBuffer buffer;
    int ID;
    int inLoop = 0;
    Random rand = new Random();
    int randInt;
    Consumer(int ID, fourCondBuffer buffer){
        this.ID = ID;
        this.buffer = buffer;
        this.randInt = rand.nextInt(99) + 1;
    }
    @Override
    public void run(){
        for (int i = 0; i < 100; ) {
            buffer.take(this.randInt, this);

        }
    }
}
