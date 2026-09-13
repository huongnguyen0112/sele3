package com.internet.webdriver;

import com.google.common.base.Throwables;
import com.internet.webdriver.selenium.AbstractDriverProvider;
import org.openqa.selenium.WebDriver;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;

import java.lang.reflect.Constructor;

public class DriverProvider {
    private static final ThreadLocal<WebDriver> WEB_DRIVER = new ThreadLocal<>();

    static AbstractDriverProvider newInstance(DriverConfig config) {
        try {
            String fullClassName = null;
            switch (config.getBrowser().toLowerCase()) {
                case "chrome":
                    fullClassName = "com.internet.webdriver.selenium.ChromeDriverProvider";
                    break;
                case "firefox":
                    fullClassName = "com.internet.webdriver.selenium.FirefoxDriverProvider";
                    break; 
                case "edge":
                    fullClassName = "com.internet.webdriver.selenium.EdgeDriverProvider";
                    break;
                default:
                    try {
                        fullClassName = Files.readString(Path.of("src/test/resources/drivers/driver_class.txt"));
                    } catch (IOException e) {
                        throw new RuntimeException("Could not read custom driver class name from file.", e);
                    }
            }
            Class<?> clazz = Class.forName(fullClassName);

            Constructor<?> cons = clazz.getDeclaredConstructor();
            Object obj = cons.newInstance();
            return (AbstractDriverProvider) obj;
        } catch (Exception e) {
            throw new RuntimeException("Could not create new Driver instance. " + Throwables.getStackTraceAsString(e));
        }
    }

    public static WebDriver getWebDriver() {
        return WEB_DRIVER.get();
    }

    static void setWebDriver(WebDriver driver) {
        WEB_DRIVER.set(driver);
    }

    public static void startWebDriver(DriverConfig config) {
        WebDriver localWebDriver = newInstance(config).create(config);
        setWebDriver(localWebDriver);
    }
}
