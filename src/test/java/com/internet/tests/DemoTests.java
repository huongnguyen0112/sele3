package com.internet.tests;

import org.testng.annotations.Test;

import com.internet.pages.Homepage;
import com.internet.reports.ReportProvider;
import com.internet.webdriver.DriverProvider;

public class DemoTests extends BaseTest {

    /**
     * Executes the example demo workflow for the home page links and dropdown interactions.
     */
    @Test 
    public void testDemo() {
        Homepage homepage = new Homepage();

        ReportProvider.step("1. Step 1: Open home page");
        homepage.gogo();

        ReportProvider.step("2. Step 2: Click on drag and drop link");
        homepage.clickDragAndDropLink();

        ReportProvider.step("3. Step 3: Verify drag location element is displayed");
        homepage.verifyDragLocationElementIsDisplayed();    

        ReportProvider.step("4. Step 4: Verify drop location element is displayed");
        homepage.verifyDropLocationElementIsDisplayed();

        ReportProvider.step("5. Step 5: Go back to home page");
        DriverProvider.navigateBack();

        ReportProvider.step("6. Step 6: Click on dropdown link");
        homepage.clickDropdownLink();

        ReportProvider.step("7. Step 7: Verify dropdown box is displayed");
        homepage.verifyDropdownBoxIsDisplayed();

        ReportProvider.step("8. Step 8: Select option 1 from dropdown");
        homepage.selectOption("Option 1");

        ReportProvider.step("9. Step 9: Verify option 1 is selected");
        homepage.verifyOptionIsSelected("Option 1");
    }
}
