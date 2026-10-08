package com.internet.reports.providers;

public abstract class AbstractReportProvider {
    /**
     * Writes a message to the report.
     *
     * @param message the message to record
     */
    public abstract void log(String message);

    /**
     * Records a named test step in the report.
     *
     * @param step the step description to record
     */
    public abstract void step(String step);

    /**
     * Captures and adds a screenshot to the report.
     *
     * @param name the name assigned to the screenshot
     */
    public abstract void screenshot(String name);

    /**
     * Adds a text attachment to the report.
     *
     * @param name the name assigned to the attachment
     * @param content the text content of the attachment
     */
    public abstract void attachText(String name, String content);

    /**
     * Adds a binary attachment to the report.
     *
     * @param name the name assigned to the attachment
     * @param data the binary content of the attachment
     */
    public abstract void attachBinary(String name, byte[] data);

    /**
     * Notifies the provider that a test has started.
     *
     * @param testName the name of the test that started
     */
    public abstract void onTestStart(String testName);

    /**
     * Notifies the provider that a test has finished.
     *
     * @param testName the name of the test that finished
     */
    public abstract void onTestFinish(String testName);

    /**
     * Flushes any pending report data to its destination.
     */
    public abstract void flush();
}
