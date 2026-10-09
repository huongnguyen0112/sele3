package com.internet.reports.providers;

import java.io.IOException;
import java.io.File;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.UUID;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.internet.webdriver.DriverProvider;

public class ExtendReportProvider implements AbstractReportProvider {

    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    private static final String REPORT_FOLDER_PATH = "ExtentReports";
    private static final String SCREENSHOT_SUB_DIR = "Images";

    /**
     * Initializes the shared ExtentReports instance and configures its output file.
     */
    public ExtendReportProvider() {
        if (extent == null) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = "Report_" + timestamp + ".html";
            String fullPath = REPORT_FOLDER_PATH + File.separator + fileName;

            File reportDir = new File(REPORT_FOLDER_PATH);
            if (!reportDir.exists()) {
                reportDir.mkdirs();
            }

            ExtentSparkReporter spark = new ExtentSparkReporter(fullPath);
            spark.config().setReportName("Automation Test Execution Report");
            spark.config().setDocumentTitle("Test Execution - " + timestamp);

            extent = new ExtentReports();
            extent.attachReporter(spark);
        }
    }

    /**
     * Returns the Extent test associated with the current thread.
     *
     * @return the current thread's test, or {@code null} when no test is active
     */
    private ExtentTest getTest() {
        return test.get();
    }

    /**
     * Creates and registers an Extent test for the current thread.
     *
     * @param testName the name of the test to create
     */
    @Override
    public void onTestStart(String testName) {
        ExtentTest extentTest = extent.createTest(testName);
        test.set(extentTest);
        extentTest.log(Status.INFO, "Starting test: " + testName);
    }

    /**
     * Logs test completion and clears the current thread's test association.
     *
     * @param testName the name of the test that finished
     */
    @Override
    public void onTestFinish(String testName) {
        ExtentTest currentTest = getTest();
        if (currentTest != null) {
            currentTest.log(Status.INFO, "Finishing test: " + testName);
            test.remove();
        }
    }

    /**
     * Adds an informational message to the current Extent test.
     *
     * @param message the message to record
     */
    @Override
    public void log(String message) {
        if (getTest() != null) {
            getTest().info(message);
        }
    }

    /**
     * Adds a test step to the current Extent test.
     *
     * @param step the step description to record
     */
    @Override
    public void step(String step) {
        if (getTest() != null) {
            getTest().info("STEP: " + step);
        }
    }

    /**
     * Captures the current browser page and attaches it to the Extent report.
     * The image is saved under the report's image directory.
     *
     * @param name the name of the image, without the {@code .png} extension
     */
    @Override
    public void screenshot(String name) {
        if (getTest() != null) {

            // Example: ExtentReports/Images/My_Test_Step_123456.png
            String uniqueId = UUID.randomUUID().toString();
            String fileName = name.replaceAll("[^a-zA-Z0-9_\\-]", "_") + "_" + uniqueId + ".png";
            String fullImageDir = REPORT_FOLDER_PATH + File.separator + SCREENSHOT_SUB_DIR;
            String fullImagePath = fullImageDir + File.separator + fileName;

            try {
                Files.createDirectories(Paths.get(fullImageDir));
                File srcFile = ((TakesScreenshot) DriverProvider.getWebDriver()).getScreenshotAs(OutputType.FILE);
                Files.copy(srcFile.toPath(), Paths.get(fullImagePath));

                // Images/My_Test_Step_123456.png
                String relativePath = SCREENSHOT_SUB_DIR + "/" + fileName;
                getTest().addScreenCaptureFromPath(relativePath, name);
            } catch (IOException | NullPointerException e) {
                getTest().warning("Failed to capture screenshot: " + e.getMessage());
            }
        }
    }

    /**
     * Adds a text message to the current Extent test.
     *
     * @param name the label assigned to the text content
     * @param content the text content to record
     */
    @Override
    public void attachText(String name, String content) {
        if (getTest() != null) {
            getTest().info(name + ": " + content);
        }
    }

    /**
     * Adds binary data as a base64 image attachment to the current Extent test.
     *
     * @param name the name assigned to the attachment
     * @param data the binary image data to attach
     */
    @Override
    public void attachBinary(String name, byte[] data) {
        if (getTest() != null) {
            String base64 = Base64.getEncoder().encodeToString(data);
            getTest().addScreenCaptureFromBase64String(base64, name);
        }
    }

    /**
     * Flushes pending Extent report data to the configured report file.
     */
    @Override
    public void flush() {
        extent.flush();
    }
}
