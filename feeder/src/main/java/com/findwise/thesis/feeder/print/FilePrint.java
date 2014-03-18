package com.findwise.thesis.feeder.print;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author Nawar Alkurdi
 */
public class FilePrint {

    String fileName;
    String indexTime;

    public FilePrint(String fileName, String indexTime) {
        this.fileName = fileName;
        this.indexTime = indexTime;
    }

    public void printToFile() {
        try {
            File file = new File(fileName);
            BufferedWriter out = new BufferedWriter(new FileWriter(file));
            out.newLine();
            out.write("\n" + indexTime + "\n");
            out.newLine();
            out.close();
        } catch (IOException e) {
        }
    }
}
