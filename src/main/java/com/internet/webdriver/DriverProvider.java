package com.internet.webdriver;

import com.google.common.base.Throwables;
import com.internet.webdriver.selenium.AbstractDriverProvider;
import org.openqa.selenium.WebDriver;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

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
            Properties props = new Properties();
            try (InputStream in = DriverProvider.class.getClassLoader().getResourceAsStream("META-INF/driver.provider.classname.properties")) {
                if (in == null) {
                    throw new IOException("META-INF/driver.provider.classname.properties not found");
                }
                props.load(in);
            }

            String fullClassName = props.getProperty(config.getBrowser().toLowerCase());
            if (fullClassName == null || fullClassName.isBlank()) {
                throw new IllegalArgumentException("pluginClass property missing in META-INF/driver.provider.classname.properties");
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
