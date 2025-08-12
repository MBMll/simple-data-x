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
}
