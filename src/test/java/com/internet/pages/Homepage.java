package com.internet.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import com.internet.configuration.Constants;
import com.internet.elements.MyElement;
import com.internet.webdriver.DriverProvider;


public class Homepage {
    private MyElement searchTextBox = new MyElement(By.name("q"));

    // Methods
    public Homepage gogo() {
        DriverProvider.getWebDriver().navigate().to(Constants.BASE_URL);
        return new Homepage();
    }

    public Homepage inputSearchKey(String searchKey) {
        searchTextBox.fill(searchKey);
        return new Homepage();
    }

    public Homepage submitSearch() {
        searchTextBox.sendKeys(Keys.ENTER);
        return new Homepage();
    }

}
