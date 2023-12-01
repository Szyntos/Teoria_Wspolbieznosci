package lock;

import java.util.ArrayList;
import java.util.List;

public class Producer implements Runnable{
    public LockBuffer buffer;
    public int ID;
    public int inLoop = 0;
    public int toMake;
    public int made = 0;
    public List<List<Long>> timesList;
    RNG rng = new RNG(RNGType.RANDOMRANDOM, 1);

    public Producer(int ID, int toMake, LockBuffer buffer, long seed){
        this.ID = ID;
        this.buffer = buffer;
        this.toMake = toMake;
        rng.setSeed(seed);
        timesList = new ArrayList<>();
        for (int i = 0; i <= buffer.getCapacity()/2; i++){
            timesList.add(new ArrayList<>());
        }
    }
    public void setRngType(RNGType type){
        rng.type = type;
    }
    @Override
    public void run(){
        int val;
        while (!Thread.currentThread().isInterrupted()) {
            try{
                if (this.toMake == 0){
                    val = rng.randomInt(1, buffer.getCapacity()/2);
                    long start = System.nanoTime();
                    buffer.put(this, val);
                    long elapsed = System.nanoTime() - start;
                    timesList.get(val).add(elapsed);
                }else{
                    val = rng.randomInt(1, buffer.getCapacity()/2);
                    long start = System.nanoTime();
                    buffer.put(this, this.toMake);
                    long elapsed = System.nanoTime() - start;
                    timesList.get(val).add(elapsed);
                }
//                Thread.sleep(1);
            }catch (Exception e){
                throw new RuntimeException(e);
            }
        }
    }
}
