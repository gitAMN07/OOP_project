package com.university.exception;

public class FailLimitException extends Exception {
    public FailLimitException(int count) {
        super("Fail limit exceeded: student has failed " + count + " times (maximum is 3).");
    }
}