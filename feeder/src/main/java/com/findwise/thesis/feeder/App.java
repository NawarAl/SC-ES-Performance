package com.findwise.thesis.feeder;

import com.findwise.thesis.feeder.index.Algorithm;
import com.findwise.thesis.feeder.index.ESAlgorithm;
import com.findwise.thesis.feeder.index.Feeder;
import com.findwise.thesis.feeder.index.SolrAlgorithm;
import com.findwise.thesis.feeder.index.SolrCloudAlgorithm;
import com.findwise.thesis.feeder.monitor.Monitor;
import com.findwise.thesis.feeder.print.FilePrint;
import com.findwise.thesis.feeder.query.*;
import com.findwise.thesis.feeder.time.ConvertTime;
import java.util.Scanner;

/**
 *
 * @author Nawar Alkurdi
 */
public class App {

    public static void main(String[] args) throws Exception {
        Algorithm alg;
        Query query;
        String resultFile;
        String time = null;
        FilePrint toFile;
        Terms readTerms;
        ConvertTime convertTime;

        System.out.print("Do you want to test solr, solrcloud or elasticsearch: ");
        String searchEngine;
        Scanner scanIn = new Scanner(System.in);
        searchEngine = scanIn.nextLine();

        for (int samples = 1; samples <= 1; samples++) {
            if (searchEngine.equals("solr")) {
                alg = new SolrAlgorithm();
                resultFile = "c:/tmp/indexresult/collection4/SolrRAM4.txt";
            } else if (searchEngine.equals("solrcloud")) {
                alg = new SolrCloudAlgorithm();
                resultFile = "c:/tmp/indexresult/collection4/SolrCloud4.txt";
            } else {
                alg = new ESAlgorithm();
                resultFile = "c:/tmp/indexresult/collection4/ESRAM3.txt";
            }
            Monitor m = new Monitor();
            Feeder f = new Feeder(alg);

            double cpu = m.getCPUInfo();
            f.run();
            double cpuAfterRun = m.getCPUInfo();
            long usedMemory = m.getInfo().getUsedMemory();
            int threads = m.getInfo().getTotalThread();

            convertTime = new ConvertTime("\n" + searchEngine + " index", alg.totalClock());
            time = time + convertTime.print()
                    + "\t\tUsed meory: " + usedMemory + " byte"
                    + "\t\tCPU ratio before run: " + cpu
                    + "\t\tCPU ration after run: " + cpuAfterRun
                    + "\t\tTotal threads: " + threads;

            toFile = new FilePrint(resultFile, time);
            toFile.printToFile();
            System.out.println("Sample nr: " + samples);
        }

        System.out.print("\nCollecting terms....");
        String mftFile = "c:/tmp/terms/collection4/MostFrequent.txt";
        String lftFile = "c:/tmp/terms/collection4/LeastFrequent.txt";

        /* Query most frequent one hit */
        readTerms = new Terms(mftFile, true);
        String[] mostFrequentOneTermArray = readTerms.read();
        System.out.println("\nQuerying the most frequent terms.");
        if (!searchEngine.equals("elasticsearch")) {
            query = new SolrQueries(mostFrequentOneTermArray);
            if (searchEngine.equals("solr")) {
                resultFile = "c:/tmp/queryresult/collection4/2nodes/SolrFile_OneHit.txt";
            } else {
                resultFile = "c:/tmp/queryresult/collection4/2nodes/SolrCloud4_OneHit.txt";
            }
        } else {
            query = new ESQueries(mostFrequentOneTermArray);
            resultFile = "c:/tmp/queryresult/collection4/2nodes/ESFile_OneHit.txt";
        }
        query.search();
        query.queryOneHit();
        convertTime = new ConvertTime("Query most frequent readTerms - one hit", query.totalQueryTime());
        toFile = new FilePrint(resultFile, convertTime.print() + "\tTotal response results: "
                + query.getTotalResult() + "\n" + query.getSingleResults());
        toFile.printToFile();

        /* Query most frequent one term */
        if (!searchEngine.equals("elasticsearch")) {
            query = new SolrQueries(mostFrequentOneTermArray);
            if (searchEngine.equals("solr")) {
                resultFile = "c:/tmp/queryresult/collection4/2nodes/SolrFile_OneTerm.txt";
            } else {
                resultFile = "c:/tmp/queryresult/collection4/2nodes/SolrCloud4_OneTerm.txt";
            }
        } else {
            query = new ESQueries(mostFrequentOneTermArray);
            resultFile = "c:/tmp/queryresult/collection4/2nodes/ESFile_OneTerm.txt";
        }
        query.search();
        query.queryOneTerm();
        convertTime = new ConvertTime("Query most frequent readTerms - one term", query.totalQueryTime());
        toFile = new FilePrint(resultFile, convertTime.print() + "\tTotal response results: "
                + query.getTotalResult() + "\n" + query.getSingleResults());
        toFile.printToFile();

        /* Query most frequent two terms */
        readTerms = new Terms(mftFile, false);
        String[] mostFrequentTwoTermsArray = readTerms.read();
        if (!searchEngine.equals("elasticsearch")) {
            query = new SolrQueries(mostFrequentTwoTermsArray);
            if (searchEngine.equals("solr")) {
                resultFile = "c:/tmp/queryresult/collection4/2nodes/SolrFile_TwoTerms.txt";
            } else {
                resultFile = "c:/tmp/queryresult/collection4/2nodes/SolrCloud4_TwoTerms.txt";
            }
        } else {
            query = new ESQueries(mostFrequentOneTermArray);
            resultFile = "c:/tmp/queryresult/collection4/2nodes/ESFile_TwoTerms.txt";
        }
        query.search();
        query.queryTwoTerms();
        convertTime = new ConvertTime("Query most frequent readTerms - two terms", query.totalQueryTime());
        toFile = new FilePrint(resultFile, convertTime.print() + "\tTotal response results: "
                + query.getTotalResult() + "\n" + query.getSingleResults());
        toFile.printToFile();

        /* Query least frequent one hit */
        readTerms = new Terms(lftFile, true);
        String[] leastFrequentOneTermArray = readTerms.read();
        System.out.println("\nQuerying the least frequent terms.");
        if (!searchEngine.equals("elasticsearch")) {
            query = new SolrQueries(leastFrequentOneTermArray);
            if (searchEngine.equals("solr")) {
                resultFile = "c:/tmp/queryresult/collection4/2nodes/SolrFile_OneHit_Least.txt";
            } else {
                resultFile = "c:/tmp/queryresult/collection4/2nodes/SolrCloud4_OneHit_Least.txt";
            }
        } else {
            query = new ESQueries(leastFrequentOneTermArray);
            resultFile = "c:/tmp/queryresult/collection4/2nodes/ESFile_OneHit_Least.txt";
        }
        query.search();
        query.queryOneHit();
        convertTime = new ConvertTime("Query least frequent readTerms - one hit", query.totalQueryTime());
        toFile = new FilePrint(resultFile, convertTime.print() + "\tTotal response results: "
                + query.getTotalResult() + "\n" + query.getSingleResults());
        toFile.printToFile();

        /* Query least frequent one term */
        if (!searchEngine.equals("elasticsearch")) {
            query = new SolrQueries(leastFrequentOneTermArray);
            if (searchEngine.equals("solr")) {
                resultFile = "c:/tmp/queryresult/collection4/2nodes/SolrFile_OneTerm_Least.txt";
            } else {
                resultFile = "c:/tmp/queryresult/collection4/2nodes/SolrCloud4_OneTerm_Least.txt";
            }
        } else {
            query = new ESQueries(leastFrequentOneTermArray);
            resultFile = "c:/tmp/queryresult/collection4/2nodes/ESFile_OneTerm_Least.txt";
        }
        query.search();
        query.queryOneTerm();
        convertTime = new ConvertTime("Query least frequent readTerms - one term", query.totalQueryTime());
        toFile = new FilePrint(resultFile, convertTime.print() + "\tTotal response results: "
                + query.getTotalResult() + "\n" + query.getSingleResults());
        toFile.printToFile();

        /* Query least frequent two terms */
        readTerms = new Terms(lftFile, false);
        String[] leastFrequentTwoTermsArray = readTerms.read();
        if (!searchEngine.equals("elasticsearch")) {
            query = new SolrQueries(leastFrequentTwoTermsArray);
            if (searchEngine.equals("solr")) {
                resultFile = "c:/tmp/queryresult/collection4/2nodes/SolrFile_TwoTerms_Least.txt";
            } else {
                resultFile = "c:/tmp/queryresult/collection4/2nodes/SolrCloud4_TwoTerms_Least.txt";
            }
        } else {
            query = new ESQueries(leastFrequentOneTermArray);
            resultFile = "c:/tmp/queryresult/collection4/2nodes/ESFile_TwoTerms_Least.txt";
        }
        query.search();
        query.queryTwoTerms();
        convertTime = new ConvertTime("Query least frequent readTerms - two terms", query.totalQueryTime());
        toFile = new FilePrint(resultFile, convertTime.print() + "\tTotal response results: "
                + query.getTotalResult() + "\n" + query.getSingleResults());
        toFile.printToFile();
    }
}
