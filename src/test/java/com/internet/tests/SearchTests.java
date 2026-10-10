package com.internet.tests;

import org.testng.annotations.Test;
import org.openqa.selenium.By;

import com.internet.assertions.MyHardAssert;
import com.internet.assertions.MySoftAssert;
import com.internet.elements.MyElement;
import com.internet.pages.Homepage;
import com.internet.reports.ReportProvider;

public class SearchTests extends BaseTest {

    @Test 
    public void testSearch() {
        Homepage homepage = new Homepage();
        MyElement searchBox = new MyElement(By.name("q"));
        MyElement searchBoxFake = new MyElement(By.name("zxczxczxc"));
        MySoftAssert softAssert = new MySoftAssert();

        ReportProvider.step("1. Step 1: Open search page");
        homepage.gogo();
        MyHardAssert.assertThat(searchBox).isDisplayed();
        ReportProvider.screenshot("Search page opened");


        softAssert.assertThat(5).isEqualTo(4);
        softAssert.assertThat("Hello").isEqualTo("World");
        softAssert.assertThat(true).isFalse();

        softAssert.assertThat(searchBoxFake).isDisplayed();
        softAssert.assertThat(searchBoxFake).isEnabled();
        softAssert.assertThat(searchBoxFake).isChecked();


        ReportProvider.step("2. Step 1: Enter search query");
        homepage.inputSearchKey("Selenium");
        ReportProvider.log("Search query entered");
        softAssert.assertThat(searchBox)
                .isEnabled()
                .satisfies("to contain the search query",
                        element -> "Selenium".equals(element.getAttribute("value")));


        softAssert.assertThat(searchBox).hasText("zzzzzz");

        ReportProvider.step("3. Step 3: Submit search");    
        homepage.submitSearch();
        ReportProvider.screenshot("Search submitted");
    }
}
