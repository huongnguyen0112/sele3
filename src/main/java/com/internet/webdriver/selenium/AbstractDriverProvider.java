package com.internet.webdriver.selenium;

import com.internet.webdriver.DriverConfig;

import lombok.extern.slf4j.Slf4j;

import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

@Slf4j
public abstract class AbstractDriverProvider<T extends MutableCapabilities> {
    /**
     * Creates a local or remote WebDriver from the supplied configuration.
     *
     * @param config driver configuration
     * @return created WebDriver
     * @throws RuntimeException when a remote driver cannot be started
     */
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

    /**
     * Builds browser-specific options from the driver configuration.
     *
     * @param config driver configuration
     * @return browser-specific mutable capabilities
     */
    protected abstract T createOptions(DriverConfig config);

    /**
     * Starts a local browser using the supplied options.
     *
     * @param options browser-specific options
     * @return started local WebDriver
     */
    protected abstract WebDriver createLocalDriver(T options);
}
