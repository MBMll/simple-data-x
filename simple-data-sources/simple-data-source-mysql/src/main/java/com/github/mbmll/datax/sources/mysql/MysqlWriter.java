package com.github.mbmll.datax.sources.mysql;


import com.github.mbmll.datax.core.RowChannel;
import com.github.mbmll.datax.core.concepts.Writer;
import com.github.mbmll.datax.core.entity.Column;
import com.github.mbmll.datax.core.entity.Record;
import com.github.mbmll.datax.core.exceptions.DataXException;
import com.github.mbmll.datax.core.exceptions.UnsupportedTypeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MysqlWriter implements Writer<Record> {
    private final Logger log = LoggerFactory.getLogger(MysqlWriter.class);
    private MysqlWriterConfig config;

    /**
     * 获取写入模板
     *
     * @param columns columns
     *
     * @return String
     */
    public String getWriteSql(List<String> columns) {
        String columnsStr = columns.stream().map(column -> "`" + column + "`").collect(Collectors.joining(","));
        String holders = columns.stream().map(column -> "?").collect(Collectors.joining(","));
        if (config.getWriteMode().equals("insert")) {
            return String.format("INSERT INTO `%s` (%s) VALUES (%s) ON DUPLICATE KEY UPDATE %s",
                    config.getTable(), columnsStr, holders,
                    columns.stream().map(column -> "`" + column + "`=VALUES(`" + column + "`)")
                            .collect(Collectors.joining(",")));
        } else {
            return String.format("REPLACE INTO `%s` (%s) values (%s);", config.getTable(), columnsStr, holders);
        }
    }

    private void setValue(PreparedStatement preparedStatement, Record record, List<SqlColumn> sqlColumns,
                          int columnIndex) throws SQLException, UnsupportedTypeException {
        java.util.Date utilDate;
        int columnSqltype = sqlColumns.get(columnIndex).getColumnType();
        Column column = record.getColumns().get(columnIndex);
        String typeName;
        switch (columnSqltype) {
            case Types.CHAR:
            case Types.NCHAR:
            case Types.CLOB:
            case Types.NCLOB:
            case Types.VARCHAR:
            case Types.LONGVARCHAR:
            case Types.NVARCHAR:
            case Types.LONGNVARCHAR:
                preparedStatement.setString(columnIndex + 1, (String) column
                        .getRawData());
                break;

            case Types.SMALLINT:
            case Types.INTEGER:
            case Types.BIGINT:
            case Types.NUMERIC:
            case Types.DECIMAL:
            case Types.FLOAT:
            case Types.REAL:
            case Types.DOUBLE:
                String strValue = (String) column.getRawData();
                if (config.isEmptyAsNull() && "".equals(strValue)) {
                    preparedStatement.setString(columnIndex + 1, null);
                } else {
                    preparedStatement.setString(columnIndex + 1, strValue);
                }
                break;

            //tinyint is a little special in some database like mysql {boolean->tinyint(1)}
            case Types.TINYINT:
                Long longValue = (Long) column.getRawData();
                if (null == longValue) {
                    preparedStatement.setString(columnIndex + 1, null);
                } else {
                    preparedStatement.setString(columnIndex + 1, longValue.toString());
                }
                break;

            // for mysql bug, see http://bugs.mysql.com/bug.php?id=35115
            case Types.DATE:
                typeName = sqlColumns.get(columnIndex).getColumnTypeName();

                if (typeName.equalsIgnoreCase("year")) {
                    if (column.getRawData() == null) {
                        preparedStatement.setString(columnIndex + 1, null);
                    } else {
                        preparedStatement.setInt(columnIndex + 1, ((BigInteger) column.getRawData()).intValue());
                    }
                } else {
                    java.sql.Date sqlDate = null;
                    utilDate = (java.util.Date) column.getRawData();
                    if (null != utilDate) {
                        sqlDate = new java.sql.Date(utilDate.getTime());
                    }
                    preparedStatement.setDate(columnIndex + 1, sqlDate);
                }
                break;

            case Types.TIME:
                java.sql.Time sqlTime = null;
                utilDate = (java.util.Date) column.getRawData();
                if (null != utilDate) {
                    sqlTime = new java.sql.Time(utilDate.getTime());
                }
                preparedStatement.setTime(columnIndex + 1, sqlTime);
                break;

            case Types.TIMESTAMP:
                java.sql.Timestamp sqlTimestamp = null;
                utilDate = (java.util.Date) column.getRawData();

                if (null != utilDate) {
                    sqlTimestamp = new java.sql.Timestamp(
                            utilDate.getTime());
                }
                preparedStatement.setTimestamp(columnIndex + 1, sqlTimestamp);
                break;

            case Types.BINARY:
            case Types.VARBINARY:
            case Types.BLOB:
            case Types.LONGVARBINARY:
                preparedStatement.setBytes(columnIndex + 1, (byte[]) column
                        .getRawData());
                break;

            case Types.BOOLEAN:
                preparedStatement.setBoolean(columnIndex + 1, (Boolean) column.getRawData());
                break;

            // warn: bit(1) -> Types.BIT 可使用setBoolean
            // warn: bit(>1) -> Types.VARBINARY 可使用setBytes
            case Types.BIT:
                preparedStatement.setBoolean(columnIndex + 1, (Boolean) column.getRawData());
            default:
                throw new UnsupportedTypeException(
                        String.format(
                                "您的配置文件中的列配置信息有误. 因为DataX 不支持数据库写入这种字段类型. 字段名:[%s], 字段类型:[%d], " +
                                        "字段Java类型:[%s]. 请修改表中该字段的类型或者不同步该字段.",
                                sqlColumns.get(columnIndex).getColumnName(),
                                sqlColumns.get(columnIndex).getColumnType(),
                                sqlColumns.get(columnIndex).getColumnTypeName()));
        }
    }

    /**
     * 写入数据
     *
     * @param queue 队列，用于读取数据
     *
     * @throws DataXException         数据异常
     * @throws ClassNotFoundException 数据库驱动类找不到异常
     */
    @Override
    public void write(RowChannel<Record> queue) throws DataXException, ClassNotFoundException {
        try (Connection connection = DBUtils.getConnection(config)) {
            connection.setAutoCommit(false);
            // 获取表结构
            List<String> columnNames = new ArrayList<>();
            List<SqlColumn> columns = new ArrayList<>();
            try (Statement statement = connection.createStatement(); ResultSet resultSet =
                    statement.executeQuery("select " + String.join(",", config.getColumns()) + " from " + config.getTable() + ";")) {
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();
                for (int i = 0; i < columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    columnNames.add(columnName);
                    columns.add(new SqlColumn(columnName, metaData.getColumnTypeName(i), metaData.getColumnType(i)));
                }
            }
            // 构建写入SQL
            String writeSql = getWriteSql(columnNames);
            List<Record> records = new ArrayList<>(config.getBatchRowSize());
            long bytesRead = 0;
            Record record;
            do {
                record = queue.poll();
                if (record != null) {
                    records.add(record);
                    bytesRead += records.stream().mapToInt(Record::getByteSize).sum();
                }
                if (
                    // 如果队列为空，并且已经读取了数据，或者达到了批次大小或字节大小限制，则执行写入操作
                        (record == null && !records.isEmpty()) ||
                                records.size() >= config.getBatchRowSize() || bytesRead >= config.getBatchByteSize()) {
                    try (PreparedStatement preparedStatement = connection.prepareStatement(writeSql)) {
                        for (Record e : records) {
                            for (int i = 0; i < columns.size(); i++) {
                                // 设置值
                                setValue(preparedStatement, e, columns, i);
                            }
                            // 添加到批处理
                            preparedStatement.addBatch();
                        }
                        // 执行批处理
                        preparedStatement.executeBatch();
                        connection.commit();
                        preparedStatement.clearBatch();
                    } catch (SQLException e) {
                        connection.rollback();
                        throw e;
                    }
                    records.clear();
                    bytesRead = 0;
                }
            } while (record != null);
            try (PreparedStatement preparedStatement = connection.prepareStatement(writeSql)) {

            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            throw new DataXException("保存失败", e);
        }
    }
}
