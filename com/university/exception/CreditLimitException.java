package com.university.exception;

public class CreditLimitException extends Exception {
    public CreditLimitException(int current, int attempted, int max) {
        super("Credit limit exceeded: current=" + current + ", attempted to add=" + attempted + ", max=" + max + ".");
    }
}