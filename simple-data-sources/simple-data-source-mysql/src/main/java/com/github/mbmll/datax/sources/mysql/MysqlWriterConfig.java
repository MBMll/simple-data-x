package com.github.mbmll.datax.sources.mysql;


import java.util.List;

/**
 * @Author xlc
 * @Description
 * @Date 2025/8/13 22:10:16
 */

public class MysqlWriterConfig extends ConnectionProperties {
    private String table;
    private String writeMode;
    private List<String> columns;
    /**
     * 默认1024行
     */
    private int batchRowSize = 1024;
    /**
     * 32MB
     */
    private long batchByteSize = 32 * 1024 * 1024;
    /**
     *
     */
    private boolean emptyAsNull;

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getWriteMode() {

        return writeMode;
    }

    public void setWriteMode(String writeMode) {
        this.writeMode = writeMode;
    }

    public int getBatchRowSize() {
        return batchRowSize;
    }

    public void setBatchRowSize(int batchRowSize) {
        this.batchRowSize = batchRowSize;
    }

    public long getBatchByteSize() {
        return batchByteSize;
    }

    public void setBatchByteSize(long batchByteSize) {
        this.batchByteSize = batchByteSize;
    }

    public boolean isEmptyAsNull() {
        return emptyAsNull;
    }

    public void setEmptyAsNull(boolean emptyAsNull) {
        this.emptyAsNull = emptyAsNull;
    }
}
