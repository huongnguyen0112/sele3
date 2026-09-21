package com.internet.waits;

import org.openqa.selenium.support.ui.FluentWait;

import com.internet.elements.BaseElement;
import com.internet.webdriver.DriverProvider;

import org.openqa.selenium.WebDriver;

/**
 * Fluent wait that evaluates conditions against a {@link BaseElement}.
 */
public class MyWait extends FluentWait<WebDriver> {

    private BaseElement element;

	/**
	 * Creates a wait using the WebDriver associated with the current thread.
	 *
	 * @param element element whose state is evaluated by this wait
	 */
	public MyWait(BaseElement element) {
		super(DriverProvider.getWebDriver());
		this.element = element;
	}

    /**
     * Waits until the supplied condition matches the configured element.
     *
     * @param condition condition to evaluate until it returns {@code true}
     */
    public void until(ElementCondition condition) {
        super.until(driver -> condition.matches(element));
    }
}
