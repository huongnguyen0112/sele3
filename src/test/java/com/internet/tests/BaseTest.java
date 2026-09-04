package com.internet.tests;

import com.internet.configuration.Constants;
import com.internet.webdriver.DriverConfig;
import com.internet.webdriver.DriverProvider;
import org.testng.annotations.*;

public class BaseTest {
    String configFile;
    @BeforeClass
    @Parameters({"config"})
    public void loadConfig(@Optional("chrome.config.json") String config) {
        configFile = config;
    }
    @BeforeMethod
    public void startTest() {
        System.out.println("Pre-condition");
        DriverConfig driverConfig = DriverConfig.loadFromFile(configFile);
        DriverProvider.startWebDriver(driverConfig);
        DriverProvider.getWebDriver().manage().window().maximize();
        DriverProvider.getWebDriver().navigate().to(Constants.BASE_URL);
    }

    @AfterMethod
    public void afterMethod() {
        System.out.println("Post-condition");

        DriverProvider.getWebDriver().quit();
    }
}
