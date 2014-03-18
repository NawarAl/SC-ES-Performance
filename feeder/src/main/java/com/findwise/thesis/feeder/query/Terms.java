package com.findwise.thesis.feeder.query;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.String;

/**
 *
 * @author Nawar Alkurdi
 */
public class Terms {

    String fileName;
    Boolean oneTerm;

    public Terms(String fileName, Boolean oneTerm) {
        this.fileName = fileName;
        this.oneTerm = oneTerm;
    }

    public String[] read() {
        String[] terms = new String[100];
        File file = new File(fileName);
        String line1, line2;
        try {
            BufferedReader in1 = new BufferedReader(new FileReader(file));
            if (oneTerm) {
                for (int i = 0; i < terms.length; i++) {
                    line1 = in1.readLine();
                    if (line1 == null) {
                        break;
                    }
                    terms[i] = line1;
                }

            } else {
                BufferedReader in2 = new BufferedReader(new FileReader(file));
                String firstLine = in2.readLine();
                for (int i = 0; i < terms.length; i++) {
                    line1 = in1.readLine();
                    line2 = in2.readLine();

                    if (line1 == null) {
                        break;
                    }
                    if (line2 == null) {
                        line2 = firstLine;
                    }
                    terms[i] = line1 + " OR " + line2;
                }
            }
        } catch (IOException e) {
        }
        return terms;
    }
}
