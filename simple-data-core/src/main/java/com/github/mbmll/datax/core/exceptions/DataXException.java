package com.github.mbmll.datax.core.exceptions;


public class DataXException extends Exception {
    public DataXException() {
    }

    public DataXException(String message) {
        super(message);
    }

    public DataXException(String message, Exception e) {
        super(message, e);
    }
}
