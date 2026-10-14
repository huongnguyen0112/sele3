package com.internet.tests;

import com.internet.reports.ReportProvider;
import com.internet.webdriver.DriverConfig;
import com.internet.webdriver.DriverProvider;
import org.testng.annotations.*;

@Listeners(com.internet.assertions.AssertListener.class)
public class BaseTest {

    /**
     * Starts a configured browser, maximizes it, and navigates to the base URL.
     */
    @BeforeMethod
    public void startTest() {
        ReportProvider.start("zzz");
        DriverConfig driverConfig = DriverConfig.init();
        DriverProvider.startWebDriver(driverConfig);
        DriverProvider.maximizeWindow();
    }

    /**
     * Closes the WebDriver after each test method.
     */
    @AfterMethod
    public void afterMethod() {
        DriverProvider.quitWebDriver();
        ReportProvider.finish("zzz");
    }
}
