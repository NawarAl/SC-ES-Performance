package com.findwise.thesis.feeder.query;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

/**
 *
 * @author Nawar Alkurdi
 */
public class SolrQueries extends Query {

    //SolrServer server;
    CloudSolrServer server;
    SolrQuery query;
    long startTimer, endTimer, total, totalSearchResults;
    String[] terms;
    String eachQueryResult = "Results for each query:\n";

    public SolrQueries(String[] terms) {
        this.terms = terms;
    }

    @Override
    public void search() throws Exception {
        if (server == null) {
            getServer();
        }
    }

    @Override
    public void queryOneHit() throws Exception {
        for (String term : terms) {
            try {
                query = new SolrQuery()
                        .setQuery(term)
                        .setStart(0)
                        .setRows(1)
                        .setFacetMinCount(1)
                        .setFacetLimit(1);
                queryResult();
            } catch (Exception e) {
                System.out.println("Response size is set to: "
                        + e.getMessage() + " hit");
            }
        }
    }

    @Override
    public void queryOneTerm() throws Exception {
        for (String term : terms) {
            try {
                query = new SolrQuery()
                        .setQuery(term)
                        .setStart(0);
                queryResult();
            } catch (Exception e) {
                byDefault(e);
            }
        }
    }

    @Override
    public void queryTwoTerms() throws Exception {
        for (String twoTerms : terms) {
            try {
                query = new SolrQuery()
                        .setQuery(twoTerms)
                        .setStart(0);
                queryResult();
            } catch (Exception e) {
                byDefault(e);
            }
        }
    }

    @Override
    public long totalQueryTime() {
        return total;
    }

    @Override
    public long getTotalResult() {
        return totalSearchResults;
    }

    @Override
    public String getSingleResults() {
        return eachQueryResult;
    }

    private void getServer() throws Exception {
        //server = new HttpSolrServer("http://localhost:8983/solr/collection1");
        server = new CloudSolrServer("localhost:9983");
        server.setDefaultCollection("collection1");
    }

    private void queryResult() {
        SolrDocumentList queryResults = null;
        QueryResponse response = null;

        startClock();
        try {
            response = server.query(query);
            queryResults = response.getResults();

        } catch (Exception e) {
            System.out.println("QueryResponse error: " + e.getMessage());
        }
        endClock();
        total += queryTime();

        if (queryResults != null) {
            setTotalResult(queryResults.getNumFound());
            System.out.println("\nQuery results: " + response.toString());

            for (int i = 0; i < queryResults.getNumFound(); i++) {
                SolrDocument document = queryResults.get(i);
            }
        }
    }

    private void setTotalResult(long totalSearchResults) {
        eachQueryResult += totalSearchResults + "\n";
        this.totalSearchResults += totalSearchResults;
    }

    private void startClock() {
        startTimer = System.nanoTime();
    }

    private void endClock() {
        endTimer = System.nanoTime();
    }

    private void byDefault(Exception e) {
        System.out.println("By default response size: "
                + e.getMessage() + " hits");
    }

    private long queryTime() {
        return (endTimer - startTimer);
    }
}