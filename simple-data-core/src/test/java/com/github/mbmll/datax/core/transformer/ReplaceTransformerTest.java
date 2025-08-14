package com.github.mbmll.datax.core.transformer;

import com.github.mbmll.datax.core.constants.JavaType;
import com.github.mbmll.datax.core.entity.Column;
import com.github.mbmll.datax.core.entity.Record;
import com.github.mbmll.datax.core.exceptions.DataXException;
import org.junit.Test;

/**
 * @Author xlc
 * @Description
 * @Date 2025/8/15 00:03:01
 */

public class ReplaceTransformerTest {
    @Test
    public void transform() throws DataXException {
        ReplaceTransformer replaceTransformer = new ReplaceTransformer();
        replaceTransformer.setColumnIndex(0);
        replaceTransformer.setReplaceHolder("22");
        replaceTransformer.setStartIndex(2);
        replaceTransformer.setLength(5);
        Record record = new Record(1);
        record.getColumns().add(new Column(JavaType.STRING, "123456789", 10));
        replaceTransformer.transform(record);
        System.out.println(record.getColumns().get(0).getRawData());
    }
}