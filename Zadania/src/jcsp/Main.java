package jcsp;

import org.jcsp.lang.CSProcess;
import org.jcsp.lang.Channel;
import org.jcsp.lang.One2OneChannelInt;
import org.jcsp.lang.Parallel;

/**
 * Main program class for Producer/Consumer example.
 * Sets up channels, creates processes then
 * executes them in parallel, using JCSP.
 */
public final class Main {
    public static void main(String[] args) {
        PCMain2();
    } // main

    public static void PCMain2() { // Create channel objects
        final One2OneChannelInt[] prodChan = {Channel.one2oneInt(), Channel.one2oneInt()}; // Producers
        final One2OneChannelInt[] consReq = {Channel.one2oneInt(), Channel.one2oneInt()}; // Consumer requests
        final One2OneChannelInt[] consChan = {Channel.one2oneInt(), Channel.one2oneInt()}; // Consumer data
// Create parallel construct
        CSProcess[] procList = {new Producer2(prodChan[0], 0), new Producer2(prodChan[1], 100),
                new Buffer(prodChan, consReq, consChan), new Consumer2(consReq[0], consChan[0]),
                new Consumer2(consReq[1], consChan[1])}; // Processes
        Parallel par = new Parallel(procList); // PAR construct
        par.run(); // Execute processes in parallel
    } // PCMain constructor
} // class PCMain2