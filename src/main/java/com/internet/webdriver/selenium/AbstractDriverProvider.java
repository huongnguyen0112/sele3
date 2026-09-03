package com.internet.webdriver.selenium;

import com.internet.webdriver.DriverConfig;
import org.openqa.selenium.WebDriver;

public abstract class AbstractDriverProvider {
    public abstract WebDriver createWebDriver(DriverConfig config);
}
