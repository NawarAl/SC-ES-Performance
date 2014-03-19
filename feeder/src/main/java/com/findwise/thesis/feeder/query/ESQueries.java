package com.findwise.thesis.feeder.query;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

/**
 *
 * @author Nawar Alkurdi
 */
public class ESQueries extends Query {

    TransportClient client;
    SearchRequestBuilder query;
    long startTimer, endTimer, total, totalSearchResults;
    String[] terms;
    String eachQueryResult = "Results for each query:\n";

    public ESQueries(String[] terms) {
        this.terms = terms;
    }

    @Override
    public void search() throws Exception {
        if (client == null) {
            getClient();
        }
    }

    @Override
    public void queryOneHit() throws Exception {
        for (String term : terms) {
            try {
                query = client.prepareSearch()
                        .setQuery(QueryBuilders
                        .queryString(term))
                        .setSize(1);
                queryResponse(query);
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
                query = client.prepareSearch()
                        .setQuery(QueryBuilders
                        .queryString(term));
                queryResponse(query);
            } catch (Exception e) {
                byDefault(e);
            }
        }
    }

    @Override
    public void queryTwoTerms() throws Exception {
        for (String twoTerms : terms) {
            try {
                query = client.prepareSearch()
                        .setQuery(QueryBuilders
                        .queryString(twoTerms));
                queryResponse(query);
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

    private void getClient() {
        client = new TransportClient(ImmutableSettings
                .settingsBuilder()
                .build());
        client.addTransportAddress(new InetSocketTransportAddress("localhost", 9300));
    }

    private void queryResponse(SearchRequestBuilder termQuery) throws Exception {
        SearchHits queryResults = null;
        SearchResponse response = null;

        startClock();
        try {
            response = termQuery.execute().actionGet();
            queryResults = response.getHits();
        } catch (Exception e) {
            System.out.println("QueryResponse error: " + e.getMessage());
        }
        endClock();
        total += queryTime();

        if (queryResults != null) {
            setTotalResult(queryResults.getTotalHits());
            System.out.print("\nQuery results: " + getTotalResult()
                    + " documents found, returned: ");
            System.out.println("\n" + response.toString());

            for (int i = 0; i < queryResults.getTotalHits(); i++) {
                SearchHit document = queryResults.getAt(i);
            }
        }
    }

    private void setTotalResult(long totalSearchResults) {
        eachQueryResult += totalSearchResults + "\n";
        this.totalSearchResults = totalSearchResults;
    }

    private void startClock() {
        startTimer = System.nanoTime();
    }

    private void endClock() {
        endTimer = System.nanoTime();
    }

    private long queryTime() {
        return (endTimer - startTimer);
    }

    private void byDefault(Exception e) {
        System.out.println("By default response size: "
                + e.getMessage() + " hits");
    }
}
