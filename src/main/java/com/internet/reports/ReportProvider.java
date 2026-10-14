package com.internet.reports;

import com.google.common.base.Throwables;
import com.internet.configurations.Configurations;
import com.internet.reports.providers.ReportProviderInterface;

import lombok.extern.slf4j.Slf4j;

@Slf4j 
public class ReportProvider {
    private static final ThreadLocal<ReportProviderInterface> REPORT_THREAD = new ThreadLocal<>();

    static ReportProviderInterface newInstance() {
        try {
            ReportProviderInterface abstractReportProvider = new ReportLoader().loadReportProviders(Configurations.init().getReports());
            REPORT_THREAD.set(abstractReportProvider);  
            return abstractReportProvider;
        } catch (Exception e) {
            throw new RuntimeException("Could not create new Report instance. " + Throwables.getStackTraceAsString(e));
        }
    }

    static ReportProviderInterface getInstance() {
        ReportProviderInterface instance = REPORT_THREAD.get();
        if (instance == null) {
            instance = newInstance();
            REPORT_THREAD.set(instance);
        }
        return instance;
    }

    /**
     * Logs an informational message to the active report and the logger.
     *
     * @param msg the message to record
     */
    public static void log(String msg) {
        log.info("[LOG] " + msg);
        getInstance().log(msg);
    }

    /**
     * Records a failed assertion in the report and application logs.
     *
     * @param msg the failure message to report
     */
    public static void fail(String msg) {
        log.error("[ASSERTION FAILED] " + msg);
        getInstance().fail(msg);
    }

    /**
     * Records a step entry in the current test report.
     *
     * @param msg the action or verification step to log
     */
    public static void step(String msg) {
        log.info("[STEP] " + msg);
        getInstance().step(msg);
    }

    /**
     * Captures a screenshot and attaches it to the active report.
     *
     * @param name the screenshot label
     */
    public static void screenshot(String name) {
        log.info("[SCREENSHOT] " + name);
        getInstance().screenshot(name);
    }

    /**
     * Adds text content to the current report output.
     *
     * @param name the attachment label
     * @param content the text to store
     */
    public static void attachText(String name, String content) {
        log.info("[ATTACH TEXT] " + name);
        getInstance().attachText(name, content);
    }

    /**
     * Adds binary content to the current report output.
     *
     * @param name the attachment label
     * @param data the binary bytes to attach
     */
    public static void attachBinary(String name, byte[] data) {
        log.info("[ATTACH BINARY] " + name);
        getInstance().attachBinary(name, data);
    }

    /**
     * Starts tracking a test in the active reporting backend.
     *
     * @param testName the test name to begin reporting under
     */
    public static void start(String testName) {
        log.info("TEST START: " + testName);
        getInstance().onTestStart(testName);
    }

    /**
     * Finishes the active test in the current reporting backend.
     *
     * @param testName the test name being completed
     */
    public static void finish(String testName) {
        log.info("TEST FINISH: " + testName);
        getInstance().onTestFinish(testName);
    }

    /**
     * Flushes all pending report data to the active report writer.
     */
    public static void flush() {
        log.info("FLUSHING REPORTS");
        getInstance().flush();
    }
    
}
