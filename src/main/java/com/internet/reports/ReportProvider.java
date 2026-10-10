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

    public static void log(String msg) {
        log.info("[LOG] " + msg);
        getInstance().log(msg);
    }

    public static void step(String msg) {
        log.info("[STEP] " + msg);
        getInstance().step(msg);
    }

    public static void screenshot(String name) {
        log.info("[SCREENSHOT] " + name);
        getInstance().screenshot(name);
    }

    public static void attachText(String name, String content) {
        log.info("[ATTACH TEXT] " + name);
        getInstance().attachText(name, content);
    }

    public static void attachBinary(String name, byte[] data) {
        log.info("[ATTACH BINARY] " + name);
        getInstance().attachBinary(name, data);
    }

    public static void start(String testName) {
        log.info("TEST START: " + testName);
        getInstance().onTestStart(testName);
    }

    public static void finish(String testName) {
        log.info("TEST FINISH: " + testName);
        getInstance().onTestFinish(testName);
    }

    public static void flush() {
        log.info("FLUSHING REPORTS");
        getInstance().flush();
    }
    
}
