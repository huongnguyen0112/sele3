package com.internet.webdriver.selenium;

import com.internet.webdriver.DriverConfig;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;

import java.util.logging.Level;

public class FirefoxDriverProvider extends AbstractDriverProvider<FirefoxOptions> {
    @Override
    protected FirefoxOptions createOptions(DriverConfig config) {
        LoggingPreferences logPrefs = new LoggingPreferences();
        logPrefs.enable(LogType.PERFORMANCE, Level.ALL);

        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.setCapability("goog:loggingPrefs", logPrefs);

        if (config.getArguments() != null) firefoxOptions.addArguments(config.getArguments());

        if (config.isHeadless()) firefoxOptions.addArguments("--headless");

        if (config.getCapabilities() != null) firefoxOptions.merge(config.getCapabilities());

        return firefoxOptions;
    }

    @Override
    protected WebDriver createLocalDriver(FirefoxOptions options) {
        return new FirefoxDriver(options);
    }
}
