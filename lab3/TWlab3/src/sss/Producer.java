package sss;

import java.util.Random;

public class Producer implements Runnable {
    private final Buffer buffer;

    public final int id;
    public int waitLoops = 0;
    public Producer(int id, Buffer buffer) {
        this.buffer = buffer;
        this.id = id;
    }



    public void run() {

        for(int i = 0; i < Main.messagesNumber; i++) {
            Random random = new Random();

            int toPut;
            if (id == -1){
                toPut = random.nextInt(2) + Main.bufferSize-2;
            }
            else {
                toPut = random.nextInt(Main.bufferSize - 1) + 1;
            }
            buffer.put(toPut, this);
//            System.out.println("[Producer id " + this.id + "] produced " + toPut);
        }
    }

    public int getWaitLoops(){
        return waitLoops;
    }
}
