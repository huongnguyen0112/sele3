package com.internet.tests;

import org.testng.annotations.Test;
import org.openqa.selenium.By;

import com.internet.assertions.MySoftAssert;
import com.internet.elements.MyElement;
import com.internet.pages.Homepage;
import com.internet.reports.ReportProvider;

public class SearchTests extends BaseTest {

    @Test 
    public void testSearch() {
        Homepage homepage = new Homepage();
        MyElement searchBoxFake = new MyElement(By.name("zzz"));
        MySoftAssert softAssert = new MySoftAssert();

        ReportProvider.step("1. Step 1: Open search page");
        homepage.gogo();

        // softAssert.assertThat(searchBoxFake).isDisplayed();
        // softAssert.assertThat(searchBoxFake).isEnabled();
        softAssert.assertThat(searchBoxFake).isChecked();
        softAssert.assertThat(searchBoxFake).hasText("zzz");

    }
}
