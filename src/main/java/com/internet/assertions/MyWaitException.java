package com.internet.assertions;

/**
 * Thrown when a wait condition fails during an interaction or assertion.
 * This runtime exception signals that the expected state was not reached
 * within the configured waiting period while interacting with the page.
 */
public class MyWaitException extends RuntimeException {
    public MyWaitException(String message) {
        super(message);
    }

    public MyWaitException(String message, Throwable cause) {
        super(message, cause);
    }
}
