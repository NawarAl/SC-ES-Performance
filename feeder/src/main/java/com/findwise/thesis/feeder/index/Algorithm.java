package com.findwise.thesis.feeder.index;

/**
 *
 * @author Nawar Alkurdi
 */
public abstract class Algorithm {

    public abstract void send(Document d) throws Exception;

    public abstract long totalClock();
}
