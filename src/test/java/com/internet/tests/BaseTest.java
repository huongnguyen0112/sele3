package com.internet.tests;

import com.internet.configuration.Constants;
import com.internet.webdriver.DriverConfig;
import com.internet.webdriver.DriverProvider;
import org.testng.annotations.*;

public class BaseTest {
    String configFile;
    /**
     * Stores the TestNG configuration file name for the test class.
     *
     * @param config configuration file name supplied by TestNG
     */
    @BeforeClass
    @Parameters({"config"})
    public void loadConfig(@Optional("chrome.config.json") String config) {
        configFile = config;
    }
    /**
     * Starts a configured browser, maximizes it, and navigates to the base URL.
     */
    @BeforeMethod
    public void startTest() {
        System.out.println("Pre-condition");
        DriverConfig driverConfig = DriverConfig.loadFromFile(configFile);
        DriverProvider.startWebDriver(driverConfig);
        DriverProvider.getWebDriver().manage().window().maximize();
        DriverProvider.getWebDriver().navigate().to(Constants.BASE_URL);
    }

    /**
     * Closes the WebDriver after each test method.
     */
    @AfterMethod
    public void afterMethod() {
        System.out.println("Post-condition");

        DriverProvider.getWebDriver().quit();
    }
}
