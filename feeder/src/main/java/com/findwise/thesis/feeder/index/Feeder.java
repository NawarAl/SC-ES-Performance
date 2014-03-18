package com.findwise.thesis.feeder.index;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;

/**
 *
 * @author Nawar Alkurdi
 */
public class Feeder {

    Algorithm alg;

    public Feeder(Algorithm alg) {
        this.alg = alg;
    }

    public void run() throws Exception {
        File startfolder = new File("c:/tmp/files4");
        if (startfolder.isDirectory()) {
            handleDir(startfolder);
        }
    }

    private void handleDir(File d) throws Exception {
        for (File f : d.listFiles()) {
            if (f.isDirectory()) {
                handleDir(f);
            } else {
                handleFile(f);
            }
        }
    }

    private void handleFile(File f) throws Exception {
        InputStream stream = new FileInputStream(f); // Open the stream. 
        Metadata metadata = new Metadata();
        Parser parser = new AutoDetectParser();
        ParseContext parseContext = new ParseContext();
        parseContext.set(Parser.class, parser);
        StringWriter textData = new StringWriter();

        /*
         * Parse the stream and finally close it. 
         * Parsing allows documents to be parsed without excessive resource 
         * requirements. A TikaException is thrown in if a document is corrupted.
         */
        try {
            parser.parse(stream, new BodyContentHandler(textData), metadata, parseContext);
        } catch (TikaException e) {
            //System.out.println("Error: " + e.getMessage());
        } catch (NullPointerException e) {
            //System.out.println("Error: " + e.getMessage());
        } catch (IOException e) {
            //System.out.println("Error: " + e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            //System.out.println("Error: " + e.getMessage());
        }

        Document d = new Document();
        d.addEntry(new Entry("text", textData.toString()));
        d.addEntry(new Entry("id", f.getAbsolutePath()));
        alg.send(d);
    }
}
