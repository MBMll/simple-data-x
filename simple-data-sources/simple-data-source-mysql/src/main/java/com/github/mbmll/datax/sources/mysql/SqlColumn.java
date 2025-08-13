package com.github.mbmll.datax.sources.mysql;


public class SqlColumn {
    private String columnName;
    private String columnTypeName;
    private int columnType;
    public SqlColumn(String columnName, String columnTypeName, int columnType) {
        this.columnName = columnName;
        this.columnTypeName = columnTypeName;
        this.columnType = columnType;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnTypeName() {
        return columnTypeName;
    }

    public void setColumnTypeName(String columnTypeName) {
        this.columnTypeName = columnTypeName;
    }

    public int getColumnType() {
        return columnType;
    }

    public void setColumnType(int columnType) {
        this.columnType = columnType;
    }
}
