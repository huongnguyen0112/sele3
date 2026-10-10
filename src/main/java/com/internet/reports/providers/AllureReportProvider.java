package com.internet.reports.providers;

import java.io.ByteArrayInputStream;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import com.internet.webdriver.DriverProvider;

import io.qameta.allure.Allure;

public class AllureReportProvider implements ReportProviderInterface {
    /**
     * Adds a message as an Allure step.
     *
     * @param message the message to record
     */
    @Override
    public void log(String message) {
        Allure.step(message);
    }

    /**
     * Adds a test step to the Allure report.
     *
     * @param step the step description to record
     */
    @Override
    public void step(String step) {
        Allure.step(step);
    }

    /**
     * Captures the current browser page and adds it to the Allure report.
     *
     * @param name the name assigned to the screenshot attachment
     */
    @Override
    public void screenshot(String name) {
        byte[] full = ((TakesScreenshot)
                DriverProvider.getWebDriver())
                .getScreenshotAs(OutputType.BYTES);

        Allure.addAttachment(name + " (page)",
                new ByteArrayInputStream(full));
    }

    /**
     * Adds a text attachment to the Allure report.
     *
     * @param name the name assigned to the attachment
     * @param content the text content of the attachment
     */
    @Override
    public void attachText(String name, String content) {
        Allure.addAttachment(name, content);
    }

    /**
     * Adds a binary attachment to the Allure report.
     *
     * @param name the name assigned to the attachment
     * @param data the binary content of the attachment
     */
    @Override
    public void attachBinary(String name, byte[] data) {
        Allure.addAttachment(name, new ByteArrayInputStream(data));
    }

    /**
     * Handles the start of a test; Allure does not require additional setup here.
     *
     * @param testName the name of the test that started
     */
    @Override
    public void onTestStart(String testName) {
    }

    /**
     * Handles the completion of a test; Allure finalizes the test lifecycle separately.
     *
     * @param testName the name of the test that finished
     */
    @Override
    public void onTestFinish(String testName) {
    }

    /**
     * Flushes the report; Allure manages report output without an explicit flush here.
     */
    @Override
    public void flush() {
    }
    
}
