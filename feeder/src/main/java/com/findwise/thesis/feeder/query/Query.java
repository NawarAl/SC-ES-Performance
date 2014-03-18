package com.findwise.thesis.feeder.query;

/**
 *
 * @author Nawar Alkurdi
 */
public abstract class Query {

    public abstract void search() throws Exception;

    public abstract void queryOneHit() throws Exception;
    
    public abstract void queryOneTerm() throws Exception;

    public abstract void queryTwoTerms() throws Exception;

    public abstract long totalQueryTime();
    
    public abstract long getTotalResult();
    
    public abstract String getSingleResults();
}
