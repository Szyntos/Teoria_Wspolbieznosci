package lock;

import java.util.Random;
import java.security.SecureRandom;
public class RNG {
    RNGType type = RNGType.RANDOMRANDOM;
    long seed = 0;
    Random randomRandom = new Random();
    SecureRandom secureRandom = new SecureRandom();
    public RNG(RNGType type, long seed){
        this.type = type;
        this.seed = seed;
        randomRandom.setSeed(seed);
        secureRandom.setSeed(seed);
    }
    public int randomInt(int a, int b){
        switch (type){
            case RANDOMRANDOM -> {
                return randomRandom.nextInt(b - a + 1) + a;
            }
            case SECURERANDOM -> {
                return secureRandom.nextInt(b - a + 1) + a;
            }
            default -> {return a;}
        }
    }
    public void setSeed(long seed){
        randomRandom.setSeed(seed);

    }
}
