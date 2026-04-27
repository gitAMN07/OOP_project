package com.university.exception;

public class LowHIndexException extends Exception {
    public LowHIndexException(int hIndex) {
        super("Cannot assign supervisor: h-index is " + hIndex + ", minimum required is 3.");
    }
}