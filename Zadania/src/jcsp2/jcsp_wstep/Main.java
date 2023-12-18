package jcsp2.jcsp_wstep;
import org.jcsp.lang.CSProcess;
import org.jcsp.lang.One2OneChannelInt;
import org.jcsp.lang.Parallel;
import org.jcsp.lang.Channel;

public class Main {
    public static void main(String[] args){ // Create channel object
        final One2OneChannelInt channel = Channel.one2oneInt();
        // Create and run parallel construct with a list of processes
        CSProcess[] procList = { new Producer(channel), new Consumer(channel) };
        Parallel par = new Parallel(procList); // PAR construct
        par.run(); // Execute processes in parallel
    }
}
