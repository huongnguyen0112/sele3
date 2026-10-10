package com.internet.elements;

import java.time.Duration;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.jspecify.annotations.NonNull;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.internet.webdriver.DriverProvider;
import com.internet.utils.Utilities;
import com.internet.waits.ElementCondition;
import com.internet.waits.ElementConditions;
import com.internet.waits.MyWait;

/**
 * Represents a lazily located element and provides state checks, waits, and
 * user interactions for a specific locator.
 */
public class MyElement {
    protected By by;
    private Class<?> byClass;
    protected String locator;

    /**
     * Creates a lazily resolved element using the provided Selenium locator.
     *
     * @param by the locator used to find this element
     */
    public MyElement(By by) {
        this.by = by;
        this.byClass = by.getClass();
        String byString = by.toString();
        int separator = byString.indexOf(": ");
        this.locator = separator >= 0 ? byString.substring(separator + 2) : byString;
    }

    /**
     * Returns the WebDriver associated with the current thread.
     *
     * @return the current driver instance
     */
    protected WebDriver webDriver() {
        return DriverProvider.getWebDriver();
    }

    /**
     * Creates a wait helper configured for this element.
     *
     * @return a wait object bound to this element
     */
    protected MyWait myWait() {
        return new MyWait(this);
    }

    /**
     * Executes an element action and retries when the element becomes stale.
     *
     * @param action the action to perform on the element
     */
    @SuppressWarnings("unchecked")
    private void actionWithRetry(Consumer<WebElement> action, ElementCondition... conditions) {
        withRetry(
                () -> {
                    action.accept(element());
                    return null;
                },
                new Class[] {
                        NoSuchElementException.class,
                        StaleElementReferenceException.class,
                        ElementNotInteractableException.class,
                        InvalidElementStateException.class
                },
                null,
                conditions);
    }

    /**
     * Gets a String from an element and retries when a retryable exception is
     * thrown.
     *
     * @param getter the operation used to get the String
     * @return the String returned by the getter
     */
    @SuppressWarnings("unchecked")
    private String getWithRetry(Function<WebElement, String> getter, ElementCondition... conditions) {
        return withRetry(
                () -> {
                    return getter.apply(element());
                },
                new Class[] {
                        NoSuchElementException.class,
                        StaleElementReferenceException.class,
                        InvalidElementStateException.class
                },
                null,
                conditions);
    }

    /**
     * Checks an element and retries when a retryable exception is thrown.
     *
     * @param checker the operation used to check the element
     * @return the Boolean result of the check
     */
    @SuppressWarnings("unchecked")
        private Boolean checkWithRetry(Function<WebElement, Boolean> checker, Duration duration,
            ElementCondition... conditions) {
        return withRetry(
                () -> checker.apply(element()),
                new Class[] {
                        NoSuchElementException.class,
                        StaleElementReferenceException.class,
                        ElementNotInteractableException.class
                },
                duration,
                conditions);
    }

    /**
     * Executes an element operation and retries until it succeeds or times out.
     *
     * @param operation the operation to perform
     * @param <T>       the operation result type
     * @return the operation result
     */
    @SafeVarargs
    private final <T> T withRetry(Supplier<T> operation,
            Class<? extends RuntimeException>[] retryableExceptions,
            Duration duration,
            ElementCondition... conditions) {
        class Result {
            private T value;
        }

        Result result = new Result();
        MyWait wait = myWait().configuredWait("Retrying operation: " + this.locator, duration);
        wait.ignoreAll(List.of(retryableExceptions));
        ElementCondition[] retryConditions = new ElementCondition[conditions.length + 1];
        System.arraycopy(conditions, 0, retryConditions, 0, conditions.length);
        retryConditions[conditions.length] = ignored -> {
            result.value = operation.get();
            return true;
        };
        wait.waitUntil(retryConditions);
        return result.value;
    }

    /**
     * Finds and returns the first matching element on the page.
     *
     * @return the first WebElement matching the locator
     */
    public WebElement element() {
        return this.webDriver().findElement(this.by);
    }

    /**
     * Finds and returns all matching elements on the page.
     *
     * @return all WebElements matching the locator
     */
    public List<WebElement> elements() {
        return this.webDriver().findElements(this.by);
    }

    /**
     * Determines whether the element remains visually unchanged across animation
     * frames.
     *
     * @return true if the element is stable, otherwise false
     */
    public boolean isStable() {
        return isStable(Duration.ZERO);
    }

    public boolean isStable(Duration duration) {
        String script = """
                const element = arguments[0];
                const interval = 16;
                const duration = 100;

                if (!element || !element.isConnected) {
                    return false;
                }

                const snapshot = () => {
                    const rect = element.getBoundingClientRect();
                    return [rect.top, rect.left, rect.width, rect.height, element.innerHTML];
                };

                return new Promise(resolve => {
                    let previous = snapshot();
                    let stableTime = 0;
                    const check = setInterval(() => {
                        if (!element.isConnected) {
                            clearInterval(check);
                            resolve(false);
                            return;
                        }

                        const current = snapshot();
                        const unchanged = current.every((value, index) => value === previous[index]);
                        previous = current;
                        stableTime = unchanged ? stableTime + interval : 0;

                        if (stableTime >= duration) {
                            clearInterval(check);
                            resolve(true);
                        }
                    }, interval);
                });
                """;
        return checkWithRetry(webElement -> Boolean.TRUE.equals(Utilities.executeJavaScript(script, webElement)),
            duration);
    }

    /**
     * Checks whether the element is not covered by another element at its action
     * point.
     *
     * @return true when the element is the hit target at its center, otherwise
     *         false
     */
    public boolean isNotOverlaid() {
        return isNotOverlaid(Duration.ZERO);
    }

    public boolean isNotOverlaid(Duration duration) {
        String script = """
                const element = arguments[0];
                const rect = element.getBoundingClientRect();
                if (!element.isConnected || rect.width <= 0 || rect.height <= 0) {
                    return false;
                }

                const x = rect.left + rect.width / 2;
                const y = rect.top + rect.height / 2;
                const hitTarget = document.elementFromPoint(x, y);
                return hitTarget === element || element.contains(hitTarget);
                """;
        return checkWithRetry(webElement -> Boolean.TRUE.equals(Utilities.executeJavaScript(script, webElement)),
            duration);
    }

    /**
     * Checks whether the element is displayed in the current page.
     *
     * @return true if the element is visible, otherwise false
     */
    public boolean isDisplayed() {
        return isDisplayed(Duration.ZERO);
    }

    public boolean isDisplayed(Duration duration) {
        return checkWithRetry(webElement -> Boolean.TRUE.equals(webElement.isDisplayed()), duration);
    }

    /**
     * Checks whether the element is enabled for user interaction.
     *
     * @return true if the element is enabled, otherwise false
     */
    public boolean isEnabled() {
        return isEnabled(Duration.ZERO);
    }

    public boolean isEnabled(Duration duration) {
        return checkWithRetry(webElement -> Boolean.TRUE.equals(webElement.isEnabled()), duration);
    }

    /**
     * Checks whether the element is selected or checked.
     *
     * @return true if the element is selected, otherwise false
     */
    public boolean isChecked() {
        return isChecked(Duration.ZERO);
    }

    public boolean isChecked(Duration duration) {
        return checkWithRetry(
            webElement -> Boolean.TRUE.equals(webElement.isSelected()),
            duration,
            ElementConditions.VISIBLE,
            ElementConditions.ENABLED
        );
    }

    public void waitFor(ElementCondition... conditions) {
        waitFor(null, conditions);
    }

    public void waitFor(Duration duration, ElementCondition... conditions) {
        MyWait wait = myWait().configuredWait("Waiting for element to be: " + this.locator, duration);
        wait.waitUntil(conditions);
    }

    /**
     * Clicks the element after waiting for it to be actionable.
     */
    public void click() {
        actionWithRetry(
                element -> element.click(),
                    ElementConditions.VISIBLE,
                    ElementConditions.ENABLED,
                    ElementConditions.STABLE);
    }

    /**
     * Hovers over the center of the element and dispatches a mouseover event.
     */
    public void hover() {
        actionWithRetry(
                element -> new Actions(webDriver()).moveToElement(element).perform(),
                ElementConditions.VISIBLE,
                ElementConditions.STABLE);
    }

    /**
     * Scrolls the element into the center of the viewport.
     */
   public void scrollIntoView() {
        String script = "arguments[0].scrollIntoView({ block: 'center', inline: 'nearest' });";
        actionWithRetry(element -> Utilities.executeJavaScript(script, element));
    }

    /**
     * Clears the element and enters the supplied text.
     *
     * @param text the text to type into the field
     */

    public void fill(String text) {
        actionWithRetry(
                element -> {
                    element.clear();
                    element.sendKeys(text);
                },
                ElementConditions.VISIBLE,
                ElementConditions.STABLE);
    }

    /**
     * Clears the element after waiting for it to be editable.
     */
    public void clear() {
        actionWithRetry(
                element -> element.clear(),
                ElementConditions.VISIBLE,
                ElementConditions.STABLE);
    }

    /**
     * Sends keystrokes to the element after waiting for it to be actionable.
     *
     * @param keysToSend the keystrokes to send
     */
    public void sendKeys(CharSequence... keysToSend) {
        actionWithRetry(
                element -> element.sendKeys(keysToSend),
                ElementConditions.VISIBLE,
                ElementConditions.STABLE);

    }

    /**
     * Checks the element when it is not selected.
     */
    public void check() {
        if (!isChecked()) {
            this.click();
        }
    }

    /**
     * Unchecks the element when it is currently selected.
     */
    public void uncheck() {
        if (isChecked()) {
            this.click();
        }
    }

    /**
     * Returns the visible text content of the element.
     *
     * @return the element text
     */
    public String getText() {
        return getWithRetry(
                webElement -> webElement.getText(),
                ElementConditions.VISIBLE);
    }

    /**
     * Returns the value of the named attribute for this element.
     *
     * @param name the attribute name to read
     * @return the attribute value, or null if the attribute is not present
     */
    public String getAttribute(@NonNull String name) {
        return getWithRetry(
                webElement -> webElement.getAttribute(name),
                ElementConditions.VISIBLE);
    }
}
