package com.github.mbmll.datax.core.entity;


import com.github.mbmll.datax.core.constants.JavaType;

public class Column {

    private JavaType javaType;

    private Object rawData;

    private int byteSize;

    public Column(JavaType javaType, Object rawData, int byteSize) {
        this.javaType = javaType;
        this.rawData = rawData;
        this.byteSize = byteSize;
    }

    public JavaType getType() {
        return javaType;
    }

    public void setType(JavaType javaType) {
        this.javaType = javaType;
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
