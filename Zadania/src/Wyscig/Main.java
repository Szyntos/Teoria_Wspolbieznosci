package Wyscig;

class AddThread extends Thread {
    int n;
    public AddThread(int n){
        this.n = n;
    }
    public void run(){
        for (int i = 0; i < 100; i++) {
            Main.i++;
        }
    }
}
class SubThread extends Thread {
    int n;
    public SubThread(int n){
        this.n = n;
    }
    public void run(){
        for (int i = 0; i < n; i++) {
            Main.i--;
        }
    }
}


public class Main {
    public static int i = 0;
    public static void main(String[] args) throws InterruptedException {
        int threadCount = 100;
        int loopCount = 100;

        AddThread[] arrAdd = new AddThread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            arrAdd[i] = new AddThread(loopCount);
            arrAdd[i].start();
        }

        SubThread[] arrSub = new SubThread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            arrSub[i] = new SubThread(loopCount);
            arrSub[i].start();
        }

        for (int k = 0; k < threadCount; k++) {
            arrAdd[k].join();
            arrSub[k].join();
        }

        System.out.println(i);
    }
}