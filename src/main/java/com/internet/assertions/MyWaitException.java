package com.internet.assertions;

/**
 * Thrown when a wait condition fails during an interaction or assertion.
 * This runtime exception signals that the expected state was not reached
 * within the configured waiting period while interacting with the page.
 */
public class MyWaitException extends RuntimeException {
    /**
     * Creates a wait failure without an underlying cause.
     *
     * @param message details of the wait condition that failed
     */
    public MyWaitException(String message) {
        super(message);
    }

    /**
     * Creates a wait failure with an underlying error that caused the timeout.
     *
     * @param message details of the wait condition that failed
     * @param cause the exception that triggered the failure, if any
     */
    public MyWaitException(String message, Throwable cause) {
        super(message, cause);
    }
}
