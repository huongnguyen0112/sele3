package com.internet.waits;

import org.openqa.selenium.support.ui.FluentWait;

import com.internet.configurations.Configurations;
import com.internet.elements.MyElement;
import com.internet.webdriver.DriverProvider;

import java.time.Duration;

import org.openqa.selenium.WebDriver;

/**
 * Fluent wait that evaluates conditions against a {@link MyElement}.
 */
public class MyWait extends FluentWait<WebDriver> {

    private final MyElement element;
    private final Duration timeout;
    private final Duration pollingInterval;

    /**
     * Creates a wait using the WebDriver associated with the current thread.
     *
     * @param element element whose state is evaluated by this wait
     */
    public MyWait(MyElement element) {
        super(DriverProvider.getWebDriver());
        this.element = element;
        Configurations configurations = Configurations.init();
        this.timeout = configurations.getTimeout();
        this.pollingInterval = configurations.getPollingInterval();
        super.withTimeout(timeout).pollingEvery(pollingInterval);
    }

    /**
     * Waits until the supplied condition matches the configured element.
     *
     * @param condition condition to evaluate until it returns {@code true}
     */
    public void waitUntil(ElementCondition condition) {
        super.until(driver -> condition.matches(element));
    }

    /**
     * Waits until all supplied conditions match the configured element.
     *
     * @param conditions conditions to evaluate until they all return {@code true}
     */
    public void waitUntil(ElementCondition... conditions) {
        super.until(driver -> {
            for (ElementCondition condition : conditions) {
                if (!condition.matches(element)) {
                    return false;
                }
            }
            return true;
        });
    }
    
    
    public MyWait configuredWait(String message) {
        return configuredWait(message, null);
    }

    public MyWait configuredWait(String message, Duration duration) {
        super.withTimeout(duration == null ? timeout : duration)
                .pollingEvery(pollingInterval)
                .withMessage(message);
        return this;
    }
}
