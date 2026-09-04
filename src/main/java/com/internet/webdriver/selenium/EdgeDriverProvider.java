package com.internet.webdriver.selenium;

import com.internet.webdriver.DriverConfig;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;

import java.util.logging.Level;

public class EdgeDriverProvider extends AbstractDriverProvider<EdgeOptions> {
    @Override
    protected EdgeOptions createOptions(DriverConfig config) {
        LoggingPreferences logPrefs = new LoggingPreferences();
        logPrefs.enable(LogType.PERFORMANCE, Level.ALL);

        EdgeOptions edgeOptions = new EdgeOptions();
        edgeOptions.setCapability("goog:loggingPrefs", logPrefs);

        if (config.getArguments() != null) edgeOptions.addArguments(config.getArguments());

        if (config.isHeadless()) edgeOptions.addArguments("--headless=new");

        if (config.getCapabilities() != null) edgeOptions.merge(config.getCapabilities());

        return edgeOptions;
    }

    @Override
    protected WebDriver createLocalDriver(EdgeOptions options) {
        return new EdgeDriver(options);
    }
}
 