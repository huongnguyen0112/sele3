package com.internet.webdriver;

import com.google.common.base.Throwables;
import com.internet.webdriver.selenium.AbstractDriverProvider;
import org.openqa.selenium.WebDriver;

public class DriverProvider {
    private static final ThreadLocal<WebDriver> WEB_DRIVER = new ThreadLocal<>();

    /**
     * Creates the driver provider registered for the configured browser.
     *
     * @param config driver configuration containing the browser name
     * @return browser-specific driver provider
     * @throws RuntimeException when the provider cannot be loaded or instantiated
     */
    static AbstractDriverProvider<?> newInstance(DriverConfig config) {
        try {
            AbstractDriverProvider<?> abstractDriverProvider = new DriverLoader().loadDriverProviders(config.getBrowser());
            return abstractDriverProvider;
        } catch (Exception e) {
            throw new RuntimeException("Could not create new Driver instance. " + Throwables.getStackTraceAsString(e));
        }
    }

    /**
     * Returns the WebDriver associated with the current thread.
     *
     * @return current thread's WebDriver, or {@code null} when none has been started
     */
    public static WebDriver getWebDriver() {
        return WEB_DRIVER.get();
    }

    /**
     * Associates a WebDriver with the current thread.
     *
     * @param driver WebDriver to store for the current thread
     */
    static void setWebDriver(WebDriver driver) {
        WEB_DRIVER.set(driver);
    }

    /**
     * Creates and stores a WebDriver using the configured browser provider.
     *
     * @param config driver configuration used to create the browser
     */
    public static void startWebDriver(DriverConfig config) {
        WebDriver localWebDriver = newInstance(config).create(config);
        setWebDriver(localWebDriver);
    }
}
