package com.github.mbmll.datax.sources.mysql;

import com.github.mbmll.datax.core.RowChannel;
import com.github.mbmll.datax.core.concepts.Reader;
import com.github.mbmll.datax.core.constants.Type;
import com.github.mbmll.datax.core.entity.Column;
import com.github.mbmll.datax.core.entity.Record;
import com.github.mbmll.datax.core.exceptions.UnsupportedTypeException;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.*;

/**
 * @Author xlc
 * @Description
 * @Date 2025/8/13 00:41:14
 */

public class MysqlReader implements Reader<Record> {
    private MysqlReaderConfig config;

    /**
     * @param metaData
     * @param i
     * @param rs
     *
     * @return
     *
     * @throws SQLException
     * @throws UnsupportedEncodingException
     * @throws UnsupportedTypeException
     */
    private Column getColumn(ResultSetMetaData metaData, int i, ResultSet rs)
            throws SQLException, UnsupportedEncodingException, UnsupportedTypeException {
        switch (metaData.getColumnType(i)) {
            case Types.CHAR:
            case Types.NCHAR:
            case Types.VARCHAR:
            case Types.LONGVARCHAR:
            case Types.NVARCHAR:
            case Types.LONGNVARCHAR:
                String rawData;
                if (StringUtils.isBlank(config.getMandatoryEncoding())) {
                    rawData = rs.getString(i);
                } else {
                    rawData = new String((rs.getBytes(i) == null ? ArrayUtils.EMPTY_BYTE_ARRAY :
                            rs.getBytes(i)), config.getMandatoryEncoding());
                }
                return new Column(Type.STRING, rawData, rawData.getBytes().length);
            case Types.CLOB:
            case Types.NCLOB:
                String s = rs.getString(i);
                return new Column(Type.STRING, s, s.getBytes().length);
            case Types.SMALLINT:
            case Types.TINYINT:
            case Types.INTEGER:
            case Types.BIGINT:
                return new Column(Type.LONG, new BigInteger(rs.getString(i)), 0);
            case Types.NUMERIC:
            case Types.DECIMAL:
                return new Column(Type.DECIMAL, new BigDecimal(rs.getString(i)), 0);
            case Types.FLOAT:
            case Types.REAL:
            case Types.DOUBLE:
                return new Column(Type.DOUBLE, rs.getString(i), 0);
            case Types.TIME:
                return new Column(Type.DATE, new Date(rs.getTime(i).getTime()), 0);
            // for mysql bug, see http://bugs.mysql.com/bug.php?id=35115
            case Types.DATE:
                return new Column(Type.DATE, rs.getDate(i), 0);
            case Types.TIMESTAMP:
                return new Column(Type.DATE, new Date(rs.getTimestamp(i).getTime()), 0);
            case Types.BINARY:
            case Types.VARBINARY:
            case Types.BLOB:
            case Types.LONGVARBINARY:
                byte[] bytes = rs.getBytes(i);
                return new Column(Type.BYTES, bytes, bytes.length);
            // warn: bit(1) -> Types.BIT 可使用BoolColumn
            // warn: bit(>1) -> Types.VARBINARY 可使用BytesColumn
            case Types.BOOLEAN:
            case Types.BIT:
                return new Column(Type.BOOL, rs.getBoolean(i), 0);
            case Types.NULL:
                String stringData = null;
                if (rs.getObject(i) != null) {
                    stringData = rs.getObject(i).toString();
                }
                return new Column(Type.NULL, stringData, 0);
            default:
                throw new UnsupportedTypeException(
                        String.format(
                                "您的配置文件中的列配置信息有误. 因为DataX 不支持数据库读取这种字段类型. 字段名:[%s], 字段名称:[%s], " +
                                        "字段Java类型:[%s]. 请尝试使用数据库函数将其转换datax支持的类型 或者不同步该字段 .",
                                metaData.getColumnName(i),
                                metaData.getColumnType(i),
                                metaData.getColumnClassName(i)));
        }
    }

    /**
     * @param queue
     *
     * @throws Exception
     */
    @Override
    public void read(RowChannel<Record> queue) throws Exception {
        try (Connection conn = DBUtils.getConnection(config)) {
            // make sure autocommit is off
            conn.setAutoCommit(false);
            Statement stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY,
                    ResultSet.CONCUR_READ_ONLY);
            stmt.setFetchSize(config.getFetchSize());
            stmt.setQueryTimeout(config.getQueryTimeout());
            ResultSet rs = stmt.executeQuery(config.getQuerySql());
            ResultSetMetaData metaData = rs.getMetaData();
            int columnNumber = metaData.getColumnCount();
            while (rs.next()) {
                Record record = new Record(columnNumber);
                for (int i = 0; i < columnNumber; i++) {
                    record.addColumn(getColumn(metaData, i, rs));
                }
                queue.offer(record);
            }
        }
    }
}
