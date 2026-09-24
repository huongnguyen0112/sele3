package com.internet.elements;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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
public class BaseElement {
    protected By by;
    private Class<?> byClass;
    protected String locator;

    /**
     * Creates a lazily resolved element using the provided Selenium locator.
     *
     * @param by the locator used to find this element
     */
    public BaseElement(By by) {
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
    private void actionWithRetry(Consumer<WebElement> action) {
        withRetry(() -> {
            action.accept(element());
            return null;
        },
                NoSuchElementException.class,
                StaleElementReferenceException.class,
                ElementNotInteractableException.class,
                InvalidElementStateException.class);
    }

    /**
     * Gets a String from an element and retries when a retryable exception is
     * thrown.
     *
     * @param getter the operation used to get the String
     * @return the String returned by the getter
     */
    private String getWithRetry(Function<WebElement, String> getter) {
        return withRetry(() -> getter.apply(element()),
                NoSuchElementException.class,
                StaleElementReferenceException.class,
                InvalidElementStateException.class);
    }

    /**
     * Checks an element and retries when a retryable exception is thrown.
     *
     * @param checker the operation used to check the element
     * @return the Boolean result of the check
     */
    private Boolean checkWithRetry(Function<WebElement, Boolean> checker) {
        return withRetry(() -> checker.apply(element()),
                NoSuchElementException.class,
                StaleElementReferenceException.class,
                ElementNotInteractableException.class);
    }

    /**
     * Executes an element operation and retries until it succeeds or times out.
     *
     * @param operation the operation to perform
     * @param <T>       the operation result type
     * @return the operation result
     */
    @SafeVarargs
    private final <T> T withRetry(Supplier<T> operation, Class<? extends RuntimeException>... retryableExceptions) {
        class Result {
            private T value;
        }

        Result result = new Result();
        MyWait wait = myWait().configuredWait("Retrying operation: " + this.locator);
        wait.ignoreAll(List.of(retryableExceptions));
        wait.waitUntil(ignored -> {
            result.value = operation.get();
            return true;
        });
        return result.value;
    }

    /**
     * Formats the stored locator with the provided arguments and rebuilds the
     * underlying By instance.
     *
     * @param args values used to format the locator string
     * @return this element instance for chaining
     */
    public BaseElement set(Object... args) {
        this.locator = String.format(this.locator, args);
        try {
            Constructor<?> constructor = this.byClass.getConstructor(String.class);
            this.by = (By) constructor.newInstance(this.locator);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException
                | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return this;
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
        return checkWithRetry(webElement -> Boolean.TRUE.equals(Utilities.executeJavaScript(script, webElement)));
    }

    /**
     * Checks whether the element is not covered by another element at its action
     * point.
     *
     * @return true when the element is the hit target at its center, otherwise
     *         false
     */
    public boolean isNotOverlaid() {
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
        return checkWithRetry(webElement -> Boolean.TRUE.equals(Utilities.executeJavaScript(script, webElement)));
    }

    /**
     * Checks whether the element is displayed in the current page.
     *
     * @return true if the element is visible, otherwise false
     */
    public boolean isDisplayed() {
        return checkWithRetry(webElement -> Boolean.TRUE.equals(webElement.isDisplayed()));
    }

    /**
     * Checks whether the element is enabled for user interaction.
     *
     * @return true if the element is enabled, otherwise false
     */
    public boolean isEnabled() {
        return checkWithRetry(webElement -> Boolean.TRUE.equals(webElement.isEnabled()));
    }

    /**
     * Checks whether the element is selected or checked.
     *
     * @return true if the element is selected, otherwise false
     */
    public boolean isChecked() {
        return checkWithRetry(webElement -> Boolean.TRUE.equals(webElement.isSelected()));
    }

    public void waitFor(ElementCondition... conditions) {
        MyWait wait = myWait().configuredWait("Waiting for element to be: " + this.locator);
        wait.waitUntil(conditions);
    }

    /**
     * Waits until the element becomes visible using the configured timeout and
     * polling interval.
     */
    public void waitForVisible() {
        MyWait wait = myWait().configuredWait("Waiting for element to be visible: " + this.locator);
        wait.waitUntil(ElementConditions.VISIBLE);
    }

    /**
     * Waits until the element becomes enabled using the configured timeout and
     * polling interval.
     */
    public void waitForEnabled() {
        MyWait wait = myWait().configuredWait("Waiting for element to be enabled: " + this.locator);
        wait.waitUntil(ElementConditions.ENABLED);
    }

    /**
     * Waits until the element is stable across animation frames using the
     * configured wait settings.
     */
    public void waitForStable() {
        MyWait wait = myWait().configuredWait("Waiting for element to be stable: " + this.locator);
        wait.waitUntil(ElementConditions.STABLE);
    }

    /**
     * Waits until the element is not covered by another element at its action
     * point.
     */
    public void waitForNotOverlaid() {
        MyWait wait = myWait().configuredWait("Waiting for element to be not overlaid: " + this.locator);
        wait.waitUntil(ElementConditions.NOT_OVERLAID);
    }

    /**
     * Clicks the element after waiting for it to be actionable.
     */
    public void click() {
        waitFor(
                ElementConditions.VISIBLE,
                ElementConditions.ENABLED,
                ElementConditions.STABLE,
                ElementConditions.NOT_OVERLAID);

        actionWithRetry(WebElement::click);
    }

    /**
     * Hovers over the center of the element and dispatches a mouseover event.
     */
    public void hover() {
        waitFor(
                ElementConditions.VISIBLE,
                ElementConditions.STABLE,
                ElementConditions.NOT_OVERLAID);
        actionWithRetry(element -> new Actions(webDriver()).moveToElement(element).perform());
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
        this.clear();
        this.sendKeys(text);
    }

    /**
     * Clears the element after waiting for it to be editable.
     */
    public void clear() {
        waitFor(
                ElementConditions.VISIBLE,
                ElementConditions.STABLE,
                ElementConditions.NOT_OVERLAID);
        actionWithRetry(element -> element.clear());
    }

    /**
     * Sends keystrokes to the element after waiting for it to be actionable.
     *
     * @param keysToSend the keystrokes to send
     */
    public void sendKeys(CharSequence... keysToSend) {
        waitFor(
                ElementConditions.VISIBLE,
                ElementConditions.STABLE,
                ElementConditions.NOT_OVERLAID);
        actionWithRetry(element -> {
            try {
                element.sendKeys(keysToSend);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Failed to send keys to element: " + this.locator, e);
            }
        });

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
        waitFor(
                ElementConditions.VISIBLE);
        return getWithRetry(webElement -> webElement.getText());
    }

    /**
     * Returns the value of the named attribute for this element.
     *
     * @param name the attribute name to read
     * @return the attribute value, or null if the attribute is not present
     */
    public String getAttribute(@NonNull String name) {
        waitFor(
                ElementConditions.VISIBLE);
        return getWithRetry(webElement -> webElement.getAttribute(name));
    }
}
