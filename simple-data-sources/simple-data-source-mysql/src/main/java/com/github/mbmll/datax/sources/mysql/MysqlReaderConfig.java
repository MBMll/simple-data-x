package com.github.mbmll.datax.sources.mysql;

/**
 *
 */
public class MysqlReaderConfig extends ConnectionProperties{
    private String database;
    private String table;
    private String whereCondition;
    private Integer fetchSize = 1000;
    private String querySql;
    private Integer queryTimeout;
    private String mandatoryEncoding;

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public Integer getFetchSize() {
        return fetchSize;
    }

    public void setFetchSize(Integer fetchSize) {
        this.fetchSize = fetchSize;
    }



    public String getQuerySql() {
        return querySql;
    }

    public void setQuerySql(String querySql) {
        this.querySql = querySql;
    }

    public Integer getQueryTimeout() {
        return queryTimeout;
    }

    public void setQueryTimeout(Integer queryTimeout) {
        this.queryTimeout = queryTimeout;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }


    public String getWhereCondition() {
        return whereCondition;
    }

    public void setWhereCondition(String whereCondition) {
        this.whereCondition = whereCondition;
    }

    public String getMandatoryEncoding() {
        return mandatoryEncoding;
    }

    public void setMandatoryEncoding(String mandatoryEncoding) {
        this.mandatoryEncoding = mandatoryEncoding;
    }
}
