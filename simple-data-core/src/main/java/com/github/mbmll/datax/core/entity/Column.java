package com.github.mbmll.datax.core.entity;


import com.github.mbmll.datax.core.constants.Type;

public class Column {

    private Type type;

    private Object rawData;

    private int byteSize;

    public Column(Type type, Object rawData, int byteSize) {
        this.type = type;
        this.rawData = rawData;
        this.byteSize = byteSize;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Object getRawData() {
        return rawData;
    }

    public void setRawData(Object rawData) {
        this.rawData = rawData;
    }

    public int getByteSize() {
        return byteSize;
    }

    public void setByteSize(int byteSize) {
        this.byteSize = byteSize;
    }
}
