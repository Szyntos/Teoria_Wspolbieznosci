package lock;

import java.util.Random;

public class RNG {
    int type = 0;
    long seed = 0;
    Random random = new Random();
    public RNG(int type, long seed){
        this.type = type;
        this.seed = seed;
        random.setSeed(seed);
    }
    public int randomInt(int a, int b){
        if (type == 0){
            return random.nextInt(b - a + 1) + a;
        }else{
            return -1;
        }
    }
    public void setSeed(long seed){
        random.setSeed(seed);

    }
}
