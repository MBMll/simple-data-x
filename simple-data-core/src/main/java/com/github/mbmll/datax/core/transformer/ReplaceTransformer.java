package com.github.mbmll.datax.core.transformer;


import com.github.mbmll.datax.core.concepts.Transformer;
import com.github.mbmll.datax.core.constants.JavaType;
import com.github.mbmll.datax.core.entity.Column;
import com.github.mbmll.datax.core.entity.Record;
import com.github.mbmll.datax.core.exceptions.DataXException;

/**
 * no comments.
 * Created by liqiang on 16/3/4.
 */
public class ReplaceTransformer implements Transformer<Record> {
    private int columnIndex;
    private int startIndex;
    private int length;
    private String replaceHolder;

    @Override
    public Record transform(Record record) throws DataXException {

        Column column = record.getColumns().get(columnIndex);
        try {
            if (!JavaType.STRING.equals(column.getType())) {
                throw new DataXException("column type must be string");
            }
            String oriValue = (String) column.getRawData();
            //如果字段为空，跳过replace处理
            if (oriValue == null) {
                return record;
            }
            String newValue;
            if (startIndex > oriValue.length()) {
                throw new DataXException(String.format("dx_replace startIndex(%s) out of range(%s)", startIndex,
                        oriValue.length()));
            }
            if (startIndex + length >= oriValue.length()) {
                newValue = oriValue.substring(0, startIndex) + replaceHolder;
            } else {
                newValue = oriValue.substring(0, startIndex) + replaceHolder + oriValue.substring(startIndex + length);
            }
            column.setRawData(newValue);
        } catch (Exception e) {
            throw new DataXException("replace error", e);
        }
        return record;
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    public void setColumnIndex(int columnIndex) {
        this.columnIndex = columnIndex;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getReplaceHolder() {
        return replaceHolder;
    }

    public void setReplaceHolder(String replaceHolder) {
        this.replaceHolder = replaceHolder;
    }
}
