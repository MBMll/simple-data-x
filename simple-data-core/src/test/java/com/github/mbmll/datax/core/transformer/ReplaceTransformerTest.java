package com.github.mbmll.datax.core.transformer;

import com.github.mbmll.datax.core.constants.JavaType;
import com.github.mbmll.datax.core.entity.Column;
import com.github.mbmll.datax.core.entity.Record;
import com.github.mbmll.datax.core.exceptions.DataXException;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @Author xlc
 * @Description
 * @Date 2025/8/15 00:03:01
 */

public class ReplaceTransformerTest {
    /**
     *
     */
    @Test
    public void testEquals() {
        ConcurrentMap<List<Object>, Map<String, Object>> map = new ConcurrentHashMap<>();
        List<Object> list1 = new ArrayList<>(Arrays.asList(1, "2", 3));
        Map<String, Object> m1 = map.putIfAbsent(list1, new HashMap<>());
        System.out.println(m1);
        //
        List<Object> list2 = new ArrayList<>(Arrays.asList(1, "2", 3));
        Map<String, Object> m2 = map.putIfAbsent(list2, new HashMap<>());
        System.out.println(m2);
        System.out.println(m1 == m2);
        Map<String, Object> m3 = map.computeIfPresent(list2, (objects, stringObjectMap) -> stringObjectMap);
    }

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

    @Test
    public void transform2() throws DataXException {
        String s = "sdaonod\nsodneaida\n\nsssn\n\n";
        System.out.println(s.split("\\n").length);
        System.out.println(s.trim());
        System.out.println(s.split("\\n").length);
        Base64.Encoder encoder = Base64.getEncoder();

        ByteBuffer buffer = ByteBuffer.allocate(8)
                .order(ByteOrder.LITTLE_ENDIAN)
                .putLong(165632544L);
        String encode =
                encoder.withoutPadding().encodeToString(buffer.array());
        //        byte[] encode = encoder.encode(ByteBuffer.allocate(Long.BYTES).putLong(165632544).array());
        System.out.println(encode);
        Base64.Decoder decoder = Base64.getDecoder();
        System.out.println(new String(decoder.decode(encode)));
    }
}