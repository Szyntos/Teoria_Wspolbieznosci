package sss;

import java.util.Random;

public class Consumer implements Runnable {
    private final Buffer buffer;
    public final int id;

    public Consumer(int id, Buffer buffer) {
        this.buffer = buffer;
        this.id = id;
    }



    public int waitLoops=0;

    public void run() {

        for(int i = 0; i < Main.messagesNumber; i++) {
            System.out.println("consumer loop" + i );
            Random random = new Random();
            int message;
            if (id == -1){
                message = buffer.take(random.nextInt(2) + Main.bufferSize-2, this);
            }
            else {
                message = buffer.take(random.nextInt(Main.bufferSize - 1) + 1, this);
            }
            System.out.println("[Consumer id " + id + "] consumed " + message);

            if (i == Main.messagesNumber-4){
                Main.printWaitingLoops();
            }
        }
    }

    public int getWaitLoops(){
        return waitLoops;
    }
}
