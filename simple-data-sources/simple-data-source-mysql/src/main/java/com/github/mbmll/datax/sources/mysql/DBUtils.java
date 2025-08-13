package com.github.mbmll.datax.sources.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtils {
    private final MysqlReader mysqlReader;

    public DBUtils(MysqlReader mysqlReader) {
        this.mysqlReader = mysqlReader;
    }

    public static Connection getConnection(ConnectionProperties config) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        DriverManager.setLoginTimeout(15);
        return DriverManager.getConnection(config.getUrl(), config.getUsername(), config.getPassword());
    }
}
