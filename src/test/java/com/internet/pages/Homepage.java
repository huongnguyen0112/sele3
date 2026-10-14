package com.internet.pages;

import org.openqa.selenium.By;

import com.internet.assertions.MySoftAssert;
import com.internet.configuration.Constants;
import com.internet.elements.MyElement;
import com.internet.webdriver.DriverProvider;


public class Homepage {
    MyElement dragAndDropLink = new MyElement(By.xpath("//a[@href='/drag_and_drop']"));
    MyElement dropdownLink = new MyElement(By.xpath("//a[@href='/dropdown']"));

    MyElement dragLocationElement = new MyElement(By.id("column-a"));
    MyElement dropLocationElement = new MyElement(By.id("column-b"));
    MyElement dropdownBox = new MyElement(By.id("dropdown"));

    // Methods
    public Homepage gogo() {
        DriverProvider.navigateTo(Constants.BASE_URL);
        return new Homepage();
    }

    public Homepage clickDragAndDropLink() {
        dragAndDropLink.click();
        return new Homepage();
    }

    public Homepage clickDropdownLink() {
        dropdownLink.click();
        return new Homepage();
    }

    public Homepage selectOption(String option) {
        dropdownBox.selectByVisibleText(option);
        return new Homepage();
    }


    // Verification methods
    public Homepage verifyDragLocationElementIsDisplayed() {
        MySoftAssert.assertThat(dragLocationElement).isDisplayed();
        return new Homepage();
    }

    public Homepage verifyDropLocationElementIsDisplayed() {
        MySoftAssert.assertThat(dropLocationElement).isDisplayed();
        return new Homepage();
    }

    public Homepage verifyDropdownBoxIsDisplayed() {
        MySoftAssert.assertThat(dropdownBox).isDisplayed();
        return new Homepage();
    }

    public Homepage verifyOptionIsSelected(String option) {
        MySoftAssert.assertThat(dropdownBox).containsText(option);
        return new Homepage();
    }

}
