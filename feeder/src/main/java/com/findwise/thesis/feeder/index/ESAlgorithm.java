package com.findwise.thesis.feeder.index;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

/**
 *
 * @author Nawar Alkurdi
 */
public class ESAlgorithm extends Algorithm {

    private final static String INDEX_NAME = "index_name";
    private final static String TYPE_NAME = "type_name";
    long total = 0;
    long startTimer, endTimer;
    TransportClient client;

    @Override
    public void send(Document d) throws Exception {
        if (client == null) {
            connect();
            clearPreviousDocument();
        }

        /*
         * A built-in helper to generate JSON content. 
         */
        XContentBuilder builder = XContentFactory.jsonBuilder().startObject();

        startClock();

        for (Entry e : d.getList()) {
            builder.field(e.getKey(), e.getValue());
        }
        builder.endObject();

        /*
         * Indexing JSON documents. The IndexResponse can give a report.
         */
        IndexResponse response = client.prepareIndex(INDEX_NAME, TYPE_NAME)
                .setSource(builder)
                .execute()
                .actionGet();

        endClock();
        total += totalFileIndexTime();
    }

    @Override
    public long totalClock() {
        return total;
    }

    /*
     * Connects to a cluster remotely, without joining the cluster. 
     * The communication is done in a round robin fashion on each action, 
     * which will be a two hop operation. 
     * 
     */
    private void connect() {
        client = new TransportClient(ImmutableSettings
                .settingsBuilder()
                .build());
        client.addTransportAddress(new InetSocketTransportAddress("localhost", 9300));
    }

    public void clearPreviousDocument() {
        try {
            client.admin().indices().prepareDelete(INDEX_NAME).execute().actionGet();
        } catch (Exception e) {
            System.out.println("Failed to delete data in ElasticSearch." + e.getMessage());
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
