package com.internet.assertions;

/**
 * MyWaitException
 */
public class MyWaitException extends RuntimeException {
    public MyWaitException(String message) {
        super(message);
    }

    public MyWaitException(String message, Throwable cause) {
        super(message, cause);
    }
}
