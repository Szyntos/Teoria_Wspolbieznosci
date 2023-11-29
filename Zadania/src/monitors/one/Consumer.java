package monitors.one;
public class Consumer implements Runnable{
    private final Buffer buffer;
    int ID;
    int toTake;

    public Consumer(int ID, int toTake, Buffer buffer){
        this.ID = ID;
        this.buffer = buffer;
        this.toTake = toTake;
    }
    @Override
    public void run(){
        for (int i = 0; i < 100;) {
            try{
                buffer.take(this, toTake);
            }catch (Exception e){
                throw new RuntimeException(e);
            }

        }
    }
}
