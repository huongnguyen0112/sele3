package com.internet.webdriver.selenium;

import com.internet.webdriver.DriverConfig;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.RemoteWebDriver;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.util.logging.Level;

public class ChromeDriverProvider extends AbstractDriverProvider {
    @Override
    public WebDriver createWebDriver(DriverConfig config) {
        LoggingPreferences logPrefs = new LoggingPreferences();
        logPrefs.enable(LogType.PERFORMANCE, Level.ALL);

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setCapability("goog:loggingPrefs", logPrefs);

        chromeOptions.addArguments(config.getArguments());
        if (config.isHeadless()) chromeOptions.addArguments("--headless");
        chromeOptions.merge(config.getCapabilities());

        if (config.getRemoteUrl() != null) {
            return new RemoteWebDriver(config.getRemoteUrl(), chromeOptions);
        }
        WebDriverManager.chromedriver().setup();
        return new ChromeDriver(chromeOptions);
    }
}
