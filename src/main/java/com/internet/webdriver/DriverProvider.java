package com.internet.webdriver;

import com.google.common.base.Throwables;
import com.internet.webdriver.selenium.AbstractDriverProvider;
import org.openqa.selenium.WebDriver;

import java.lang.reflect.Constructor;

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
            String fullClassName = new DriverLoader().loadDriverProviders(config.getBrowser());

            if (fullClassName == null || fullClassName.isBlank()) {
                throw new IllegalArgumentException("Driver class missing in META-INF/services/com.internet.webdriver.selenium.AbstractDriverProvider for browser: " + config.getBrowser());
            }

            Class<?> clazz = Class.forName(fullClassName);

            Constructor<?> cons = clazz.getDeclaredConstructor();
            Object obj = cons.newInstance();
            return (AbstractDriverProvider<?>) obj;
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
