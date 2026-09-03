package com.internet.webdriver.selenium;

import com.internet.webdriver.DriverConfig;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.logging.Level;

public class EdgeDriverProvider extends AbstractDriverProvider {
    @Override
    public WebDriver createWebDriver(DriverConfig config) {
        LoggingPreferences logPrefs = new LoggingPreferences();
        logPrefs.enable(LogType.PERFORMANCE, Level.ALL);

        EdgeOptions edgeOptions = new EdgeOptions();
        edgeOptions.setCapability("goog:loggingPrefs", logPrefs);

        edgeOptions.addArguments(config.getArguments());
        if (config.isHeadless()) edgeOptions.addArguments("--headless");
        edgeOptions.merge(config.getCapabilities());

        if (config.getRemoteUrl() != null) {
            return new RemoteWebDriver(config.getRemoteUrl(), edgeOptions);
        }
        WebDriverManager.edgedriver().setup();
        return new EdgeDriver(edgeOptions);
    }
}
