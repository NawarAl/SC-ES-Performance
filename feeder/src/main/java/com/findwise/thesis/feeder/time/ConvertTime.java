package com.findwise.thesis.feeder.time;

/**
 *
 * @author Nawar Alkurdi
 */
public class ConvertTime {

    long nanoSeconds;
    String action = null; //Index time or query time.

    public ConvertTime(String action, long nanoSeconds) {
        this.nanoSeconds = nanoSeconds;
        this.action = action;
    }

    public String print() {
        double totalSec = (double) nanoSeconds / 1000000000.0;
        String s = Double.toString(totalSec);
        String[] splitSec = s.split("\\.");
        int totalSecNoFraction = Integer.parseInt(splitSec[0]);
        /*
        int hours = totalSecNoFraction / 3600;
        int remainder = totalSecNoFraction % 3600;
        int min = remainder / 60; 
        */
        double sec = /*remainder*/ totalSecNoFraction /*% 60 */;

        if (splitSec[1].contains("E")) {
            sec = Double.parseDouble("." + splitSec[1]);
        } else {
            sec += Double.parseDouble("." + splitSec[1]);
        }

        /*
         * Format the string into hours, minutes and seconds.
         */
        StringBuilder time = new StringBuilder(".");
        String seperate = "", nextSeperate = " and "; //seperate hours, min and sec

        if (sec > 0) {
            time.insert(0, " sec").insert(0, sec);
            seperate = nextSeperate;
            nextSeperate = ", ";
        }

       /* 
        if (min > 0) {
            time.insert(0, seperate).insert(0, " min").insert(0, min);
            seperate = nextSeperate;
            nextSeperate = ", ";
        }
        if (hours > 0) {
            time.insert(0, seperate).insert(0, " hours").insert(0, hours);
        }
        */

        System.out.println(action + " time: " + time.toString());
        return action + " time: " + time.toString();
    }
}
