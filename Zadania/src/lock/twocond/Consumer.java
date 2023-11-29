package lock.twocond;

import lock.RNG;

import java.util.ArrayList;
import java.util.List;

public class Consumer implements Runnable{
    public final Buffer buffer;
    int ID;
    int inLoop = 0;
    int toTake;
    int taken = 0;
    public List<List<Long>> timesList;
    RNG rng = new RNG(0, 0);
    Consumer(int ID, int toTake, Buffer buffer){
        this.ID = ID;
        this.buffer = buffer;
        this.toTake = toTake;
        rng.setSeed(this.ID);
        timesList = new ArrayList<>();
        for (int i = 0; i <= buffer.capacity/2; i++){
            timesList.add(new ArrayList<>());
        }
    }
    @Override
    public void run(){
        int val;
        for (int i = 0; i < 100; ) {
            try{
                if (this.toTake == 0){
                    val = rng.randomInt(1, buffer.capacity/2);
                    long start = System.nanoTime();
                    buffer.take(this, val);
                    long elapsed = System.nanoTime() - start;
                    timesList.get(val).add(elapsed);
                }else{
                    buffer.take(this, this.toTake);
                }
            }catch (Exception e){
                throw new RuntimeException(e);
            }


        }
    }
}
