package lock.threelocks;

import lock.RNG;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Producer implements Runnable{

    private final ThreeLockBuffer buffer;
    int ID;
    int inLoop = 0;
    int toMake;
    int made = 0;
    public List<List<Long>> timesList;
    RNG rng = new RNG(0, 1);

    Producer(int ID, int toMake, ThreeLockBuffer buffer){
        this.ID = ID;
        this.buffer = buffer;
        this.toMake = toMake;
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
                if (this.toMake == 0){
                    val = rng.randomInt(1, buffer.capacity/2);
                    long start = System.nanoTime();
                    buffer.put(this, val);
                    long elapsed = System.nanoTime() - start;
                    timesList.get(val).add(elapsed);
                }else{
                    buffer.put(this, this.toMake);
                }
                Thread.sleep(1);
            }catch (Exception e){
                throw new RuntimeException(e);
            }
        }
    }
}
