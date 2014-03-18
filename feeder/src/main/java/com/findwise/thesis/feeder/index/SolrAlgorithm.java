package com.findwise.thesis.feeder.index;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.handler.ReplicationHandler.*;
import org.apache.solr.handler.RequestHandlerBase.*;

/**
 *
 * @author Nawar Alkurdi
 */
public class SolrAlgorithm extends Algorithm {

    SolrServer server;
    long startTimer, endTimer, total;

    @Override
    public void send(Document d) throws Exception {
        if (server == null) {
            connect();
            clearPreviousDocument();
        }

        /*
         * Construct and index documents. 
         */
        SolrInputDocument doc = new SolrInputDocument();
        startClock();

        for (Entry e : d.getList()) {
            doc.addField(e.getKey(), e.getValue());
        }

        server.add(doc); // Update the response. 
        server.commit();
        endClock();
        total += totalFileIndexTime();
    }

    @Override
    public long totalClock() {
        return total;
    }

    private void connect() throws Exception{
        server = new HttpSolrServer("http://localhost:8983/solr/collection1");
    }

    private void clearPreviousDocument() throws Exception {
        try {
            server.deleteByQuery("*:*");
        } catch (SolrServerException e) {
            System.out.println("Failed to delete data in Solr." + e.getMessage());
        }
    }

    private void startClock() {
        startTimer = System.nanoTime();
    }

    private void endClock() {
        endTimer = System.nanoTime();
    }

    private long totalFileIndexTime() {
        return (endTimer - startTimer);
    }
}
