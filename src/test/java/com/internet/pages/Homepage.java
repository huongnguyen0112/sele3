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
    /**
     * Opens the home page for the example application.
     *
     * @return a new Homepage instance after navigation
     */
    public Homepage gogo() {
        DriverProvider.navigateTo(Constants.BASE_URL);
        return new Homepage();
    }

    /**
     * Clicks the Drag and Drop link from the home page.
     *
     * @return a new Homepage instance after the click
     */
    public Homepage clickDragAndDropLink() {
        dragAndDropLink.click();
        return new Homepage();
    }

    /**
     * Clicks the Dropdown link from the home page.
     *
     * @return a new Homepage instance after the click
     */
    public Homepage clickDropdownLink() {
        dropdownLink.click();
        return new Homepage();
    }

    /**
     * Selects a dropdown option using its visible text.
     *
     * @param option the option label to select
     * @return a new Homepage instance after selection
     */
    public Homepage selectOption(String option) {
        dropdownBox.selectByVisibleText(option);
        return new Homepage();
    }


    // Verification methods
    /**
     * Verifies that the drag-and-drop source element is displayed.
     *
     * @return a new Homepage instance after validation
     */
    public Homepage verifyDragLocationElementIsDisplayed() {
        MySoftAssert.assertThat(dragLocationElement).isDisplayed();
        return new Homepage();
    }

    /**
     * Verifies that the drag-and-drop target element is displayed.
     *
     * @return a new Homepage instance after validation
     */
    public Homepage verifyDropLocationElementIsDisplayed() {
        MySoftAssert.assertThat(dropLocationElement).isDisplayed();
        return new Homepage();
    }

    /**
     * Verifies that the dropdown field is visible on the page.
     *
     * @return a new Homepage instance after validation
     */
    public Homepage verifyDropdownBoxIsDisplayed() {
        MySoftAssert.assertThat(dropdownBox).isDisplayed();
        return new Homepage();
    }

    /**
     * Verifies that the specified option is currently selected in the dropdown.
     *
     * @param option the expected option text
     * @return a new Homepage instance after validation
     */
    public Homepage verifyOptionIsSelected(String option) {
        MySoftAssert.assertThat(dropdownBox).containsText(option);
        return new Homepage();
    }

}
