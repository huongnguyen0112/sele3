package com.internet.webdriver.selenium;

import com.internet.webdriver.DriverConfig;

import lombok.extern.slf4j.Slf4j;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;

import java.util.logging.Level;

@Slf4j
public class ChromeDriverProvider extends AbstractDriverProvider<ChromeOptions> {
    @Override
    protected ChromeOptions createOptions(DriverConfig config) {
        LoggingPreferences logPrefs = new LoggingPreferences();
        logPrefs.enable(LogType.PERFORMANCE, Level.ALL);

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setCapability("goog:loggingPrefs", logPrefs);

        if (config.getArguments() != null) chromeOptions.addArguments(config.getArguments());

        if (config.isHeadless()) chromeOptions.addArguments("--headless=new");

        if (config.getCapabilities() != null) chromeOptions.merge(config.getCapabilities());

        return chromeOptions;
    }

    @Override
    protected WebDriver createLocalDriver(ChromeOptions options) {
        return new ChromeDriver(options);
    }
}
