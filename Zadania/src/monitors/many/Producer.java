package monitors.many;

public class Producer implements Runnable{

    public final Buffer buffer;
    int ID;
    int toMake;

    public Producer(int ID, int toMake, Buffer buffer){
        this.ID = ID;
        this.buffer = buffer;
        this.toMake = toMake;
    }
    @Override
    public void run(){
        for (int i = 0; i < 100;) {
            try{
                buffer.put(this, 1);
            } catch (Exception e){
                throw new RuntimeException(e);
            }

        }
    }
}
