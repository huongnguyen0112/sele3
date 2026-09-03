package com.internet.webdriver.selenium;

import com.internet.webdriver.DriverConfig;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.logging.Level;

public class FirefoxDriverProvider extends AbstractDriverProvider {
    @Override
    public WebDriver createWebDriver(DriverConfig config) {

        FirefoxOptions ffOptions = new FirefoxOptions();
        ffOptions.setLogLevel(FirefoxDriverLogLevel.fromLevel(Level.ALL));

        ffOptions.addArguments(config.getArguments());
        if (config.isHeadless()) ffOptions.addArguments("--headless");
        ffOptions.merge(config.getCapabilities());

        if (config.getRemoteUrl() != null) {
            return new RemoteWebDriver(config.getRemoteUrl(), ffOptions);
        }
        WebDriverManager.firefoxdriver().setup();
        return new FirefoxDriver(ffOptions);
    }
}
