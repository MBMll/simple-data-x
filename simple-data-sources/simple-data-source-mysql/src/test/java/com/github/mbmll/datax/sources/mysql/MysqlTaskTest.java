package com.github.mbmll.datax.sources.mysql;

import com.github.mbmll.datax.core.Engine;
import com.github.mbmll.datax.core.concepts.Reader;
import com.github.mbmll.datax.core.concepts.Transformer;
import com.github.mbmll.datax.core.concepts.Writer;
import com.github.mbmll.datax.core.entity.Record;
import com.github.mbmll.datax.core.transformer.ReplaceTransformer;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @Author xlc
 * @Description
 * @Date 2025/8/15 00:09:35
 */

public class MysqlTaskTest {
    @Test
    public void test() {
        Engine<Record> engine = new Engine<>();
        MysqlReader mysqlReader = new MysqlReader();
        MysqlReaderConfig mysqlReaderConfig = new MysqlReaderConfig();
        mysqlReaderConfig.setUrl("jdbc:mysql://localhost:3306/test");
        mysqlReaderConfig.setUsername("root");
        mysqlReaderConfig.setPassword("123456");
        mysqlReaderConfig.setTable("job_log");
        mysqlReaderConfig.setQuerySql("select * from job_log");
        mysqlReader.setConfig(mysqlReaderConfig);
        List<Reader<Record>> readers = Collections.singletonList(mysqlReader);
        ReplaceTransformer replaceTransformer = new ReplaceTransformer();
        replaceTransformer.setColumnIndex(3);
        replaceTransformer.setStartIndex(2);
        replaceTransformer.setLength(3);
        replaceTransformer.setReplaceHolder("***");
        List<Transformer<Record>> transformers = Collections.singletonList(replaceTransformer);
        MysqlWriter writer = new MysqlWriter();
        MysqlWriterConfig mysqlWriterConfig = new MysqlWriterConfig();
        mysqlReaderConfig.setUrl("jdbc:mysql://localhost:3306/test");
        mysqlReaderConfig.setUsername("root");
        mysqlReaderConfig.setPassword("123456");
        mysqlWriterConfig.setTable("job_log");
        mysqlWriterConfig.setColumns(new ArrayList<>(Arrays.asList("id", "job_name", "job_content")));
        writer.setConfig(mysqlWriterConfig);
        List<Writer<Record>> writers = Collections.singletonList(writer);
        engine.run(readers, transformers, writers);
    }
}