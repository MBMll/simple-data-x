package com.github.mbmll.datax.core.entity;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class Record {
    /**
     *
     */
    private List<Column> columns;
    /**
     *
     */
    private int byteSize;
    /**
     *
     */
    private Map<String, String> meta;

    public Record(int columnNumber) {
        columns = new ArrayList<>(columnNumber);
    }

    public void addColumn(Column column) {
        columns.add(column);
    }

    public int getByteSize() {
        return byteSize;
    }

    public void setByteSize(int byteSize) {
        this.byteSize = byteSize;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public Map<String, String> getMeta() {
        return meta;
    }

    public void setMeta(Map<String, String> meta) {
        this.meta = meta;
    }
}

