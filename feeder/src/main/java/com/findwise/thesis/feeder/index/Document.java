package com.findwise.thesis.feeder.index;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Nawar Alkurdi
 */
public class Document {

    private List<Entry> list;

    public Document(List<Entry> list) {
        this.list = list;
    }

    public Document() {
        list = new LinkedList<Entry>();
    }

    public void addEntry(Entry entry) {
        list.add(entry);
    }

    public List<Entry> getList() {
        return list;
    }
}
