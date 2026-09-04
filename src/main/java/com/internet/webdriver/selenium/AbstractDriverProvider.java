package com.internet.webdriver.selenium;

import com.internet.webdriver.DriverConfig;

import lombok.extern.slf4j.Slf4j;

import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

@Slf4j
public abstract class AbstractDriverProvider<T extends MutableCapabilities> {
    public WebDriver create (DriverConfig config){
        T options = createOptions(config);
        if (config.getRemoteUrl() != null) {
            try {
                return new RemoteWebDriver(config.getRemoteUrl(), options);
            } catch (Exception e) {
                throw new RuntimeException("Failed to start remote driver", e);
            }
        }
        return createLocalDriver(options);
    }

    protected abstract T createOptions(DriverConfig config);
    protected abstract WebDriver createLocalDriver(T options);
}
