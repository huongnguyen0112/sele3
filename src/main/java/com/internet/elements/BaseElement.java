package com.internet.elements;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.internet.webdriver.DriverProvider;
import com.internet.configurations.Configurations;
import com.internet.utils.Utilities;
import com.internet.waits.ElementConditions;
import com.internet.waits.MyWait;

/**
 * Represents a lazily located element and provides state checks, waits, and
 * interactions.
 */
public class BaseElement {
    protected By by;
    private Class<?> byClass;
    protected String locator;
    protected BaseElement parent;

    private static final Configurations CONFIGURATIONS = Configurations.init();

    /**
     * Creates an element scoped to a parent element.
     *
     * @param parent parent element used to resolve this element
     */
    public BaseElement(BaseElement parent) {
        this.parent = parent;
    }

    /** Creates an element resolved directly from the WebDriver. */
    public BaseElement() {
    }

    /**
     * Sets an ID locator.
     *
     * @param id element ID
     * @return this element
     */
    public BaseElement byId(String id) {
        this.locator = id;
        this.byClass = By.ById.class;
        this.by = By.id(id);
        return this;
    }

    /**
     * Sets a link-text locator.
     *
     * @param linkText exact link text
     * @return this element
     */
    public BaseElement byLinkText(String linkText) {
        this.locator = linkText;
        this.byClass = By.ByLinkText.class;
        this.by = By.linkText(linkText);
        return this;
    }

    /**
     * Sets a partial-link-text locator.
     *
     * @param partialLinkText link text fragment
     * @return this element
     */
    public BaseElement byPartialLinkText(String partialLinkText) {
        this.locator = partialLinkText;
        this.byClass = By.ByPartialLinkText.class;
        this.by = By.partialLinkText(partialLinkText);
        return this;
    }

    /**
     * Sets a name locator.
     *
     * @param name element name
     * @return this element
     */
    public BaseElement byName(String name) {
        this.locator = name;
        this.byClass = By.ByName.class;
        this.by = By.name(name);
        return this;
    }

    /**
     * Sets a tag-name locator.
     *
     * @param tagName element tag name
     * @return this element
     */
    public BaseElement byTagName(String tagName) {
        this.locator = tagName;
        this.byClass = By.ByTagName.class;
        this.by = By.tagName(tagName);
        return this;
    }

    /**
     * Sets an XPath locator.
     *
     * @param xpath XPath expression
     * @return this element
     */
    public BaseElement byXpath(String xpath) {
        this.locator = xpath;
        this.byClass = By.ByXPath.class;
        this.by = By.xpath(xpath);
        return this;
    }

    /**
     * Sets a class-name locator.
     *
     * @param className CSS class name
     * @return this element
     */
    public BaseElement byClassName(String className) {
        this.locator = className;
        this.byClass = By.ByClassName.class;
        this.by = By.className(className);
        return this;
    }

    /**
     * Sets a CSS selector locator.
     *
     * @param cssSelector CSS selector
     * @return this element
     */
    public BaseElement byCssSelector(String cssSelector) {
        this.locator = cssSelector;
        this.byClass = By.ByCssSelector.class;
        this.by = By.cssSelector(this.locator);
        return this;
    }

    /**
     * Sets a Selenium locator.
     *
     * @param by Selenium locator
     * @return this element
     */
    public BaseElement by(By by) {
        this.locator = by.toString().replaceAll("By.\\w*: (.*)", "$1");
        this.byClass = by.getClass();
        this.by = by;
        return this;
    }

    /**
     * Returns the WebDriver associated with the current thread.
     *
     * @return current WebDriver
     */
    protected WebDriver webDriver() {
        return DriverProvider.getWebDriver();
    }

    /**
     * Formats the current locator with the supplied arguments.
     *
     * @param args values used to format the locator
     * @return this element
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
     * Finds the first matching element
     *
     * @return first matching WebElement
     */
    public WebElement element() {
        return this.parent != null
                ? this.parent.element().findElement(this.by)
                : this.webDriver().findElement(this.by);

    }

    /**
     * Finds all matching elements
     *
     * @return matching WebElements
     */
    public List<WebElement> elements() {
        return this.parent != null
                ? this.parent.element().findElements(this.by)
                : this.webDriver().findElements(this.by);
    }

    /**
     * Checks whether the element keeps the same bounding box across animation
     * frames.
     *
     * @return true when the element is stable
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
        return Boolean.TRUE.equals(Utilities.executeJavaScript(script, element()));
    }

    /**
     * Checks whether the element is enabled and not readonly.
     *
     * @return true when the element is editable
     */
    public boolean isEditable() {
        String script = """
                const element = arguments[0];
                const nativeReadonly = element.matches('select[readonly], input[readonly], textarea[readonly]');
                const supportedRoles = new Set([
                    'checkbox', 'combobox', 'grid', 'gridcell', 'listbox',
                    'radiogroup', 'slider', 'spinbutton', 'textbox',
                    'columnheader', 'rowheader', 'searchbox', 'switch', 'treegrid'
                ]);
                const role = (element.getAttribute('role') || '').trim().toLowerCase().split(/\\s+/)[0];
                const ariaReadonly = element.getAttribute('aria-readonly')?.trim().toLowerCase() === 'true';

                return element.matches(':enabled') && !nativeReadonly
                        && !(ariaReadonly && supportedRoles.has(role));
                """;
        return Boolean.TRUE.equals(Utilities.executeJavaScript(script, element()));
    }

    /**
     * Checks whether the element receives pointer events at its action point.
     *
     * @return true when the element or one of its descendants is the hit target
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
        return Boolean.TRUE.equals(Utilities.executeJavaScript(script, element()));
    }

    /**
     * Checks whether the element is displayed.
     *
     * @return true when the element is displayed
     */
    public boolean isDisplayed() {
        return this.element().isDisplayed();
    }

    /**
     * Checks whether the element is enabled.
     *
     * @return true when the element is enabled
     */
    public boolean isEnabled() {
        return this.element().isEnabled();
    }

    /**
     * Waits until the element is displayed using the configured timeout and polling
     * interval.
     */
    public void waitForVisible() {
        MyWait wait = new MyWait(this);
        wait.withTimeout(Duration.ofMillis(CONFIGURATIONS.getTimeout())).pollingEvery(Duration.ofMillis(CONFIGURATIONS.getPollingInterval()))
                .withMessage("Waiting for element to be visible: " + this.locator);
        wait.until(ElementConditions.VISIBLE);
    }

    /**
     * Waits until the element is enabled using the configured timeout and polling
     * interval.
     */
    public void waitForEnabled() {
        MyWait wait = new MyWait(this);
        wait.withTimeout(Duration.ofMillis(CONFIGURATIONS.getTimeout())).pollingEvery(Duration.ofMillis(CONFIGURATIONS.getPollingInterval()))
                .withMessage("Waiting for element to be enabled: " + this.locator);
        wait.until(ElementConditions.ENABLED);
    }

    /**
     * Waits until the element is stable across animation frames using the
     * configured wait settings.
     */
    public void waitForStable() {
        MyWait wait = new MyWait(this);
        wait.withTimeout(Duration.ofMillis(CONFIGURATIONS.getTimeout())).pollingEvery(Duration.ofMillis(CONFIGURATIONS.getPollingInterval()))
                .withMessage("Waiting for element to be stable: " + this.locator);
        wait.until(ElementConditions.STABLE);
    }

    /**
     * Waits until the element is enabled and not readonly using the configured wait
     * settings.
     */
    public void waitForEditable() {
        MyWait wait = new MyWait(this);
        wait.withTimeout(Duration.ofMillis(CONFIGURATIONS.getTimeout())).pollingEvery(Duration.ofMillis(CONFIGURATIONS.getPollingInterval()))
                .withMessage("Waiting for element to be editable: " + this.locator);
        wait.until(ElementConditions.EDITABLE);
    }

    /** Waits until the element is the pointer hit target at its action point. */
    public void waitForNotOverlaid() {
        MyWait wait = new MyWait(this);
        wait.withTimeout(Duration.ofMillis(CONFIGURATIONS.getTimeout())).pollingEvery(Duration.ofMillis(CONFIGURATIONS.getPollingInterval()))
                .withMessage("Waiting for element to be not overlaid: " + this.locator);
        wait.until(ElementConditions.NOT_OVERLAID);
    }

    /** Clicks the element after waiting for it to be actionable. */
    public void click() {
        waitForVisible();
        waitForStable();
        waitForEnabled();
        waitForNotOverlaid();
        this.element().click();
    }

    /**
     * Double-clicks the center of the element after waiting for it to be
     * actionable.
     */
    public void doubleClick() {
        waitForVisible();
        waitForStable();
        waitForEnabled();
        waitForNotOverlaid();
        String script = "const element = arguments[0];" +
                "const rect = element.getBoundingClientRect();" +
                "const x = rect.left + rect.width / 2;" +
                "const y = rect.top + rect.height / 2;" +
                "element.dispatchEvent(new MouseEvent('dblclick', { bubbles: true, cancelable: true, clientX: x, clientY: y }));";
        Utilities.executeJavaScript(script, element());
    }

    /**
     * Dispatches a mouse-over event at the center of the element after waiting for
     * it to be actionable.
     */
    public void hover() {
        waitForVisible();
        waitForStable();
        waitForNotOverlaid();
        String script = "const element = arguments[0];" +
                "const rect = element.getBoundingClientRect();" +
                "const x = rect.left + rect.width / 2;" +
                "const y = rect.top + rect.height / 2;" +
                "element.dispatchEvent(new MouseEvent('mouseover', { bubbles: true, cancelable: true, clientX: x, clientY: y }));";
        Utilities.executeJavaScript(script, element());
    }

    /**
     * Clears the element and enters the supplied text.
     *
     * @param text text to enter
     */
    public void fill(String text) {
        this.clear();
        this.sendKeys(text);
    }

    /** Clears the element after waiting for it to be editable. */
    public void clear() {
        waitForVisible();
        waitForEnabled();
        waitForNotOverlaid();
        waitForEditable();
        this.element().clear();
    }

    /**
     * Sends keystrokes after waiting for the element to be editable.
     *
     * @param keysToSend keystrokes to send
     */
    public void sendKeys(CharSequence... keysToSend) {
        waitForVisible();
        waitForEnabled();
        waitForNotOverlaid();
        waitForEditable();
        try {
            this.element().sendKeys(keysToSend);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to send keys to element: " + this.locator, e);
        }

    }

    /**
     * Checks whether the element is selected.
     *
     * @return true when the element is selected
     */
    public boolean isChecked() {
        return this.element().isSelected();
    }

    /** Checks the element when it is not already selected. */
    public void check() {
        waitForVisible();
        waitForStable();
        waitForEnabled();
        waitForNotOverlaid();
        if (!isChecked()) {
            this.element().click();
        }
    }

    /** Unchecks the element when it is currently selected. */
    public void uncheck() {
        waitForVisible();
        waitForStable();
        waitForEnabled();
        waitForNotOverlaid();
        if (isChecked()) {
            this.element().click();
        }
    }

    /**
     * Returns the visible text of the element.
     *
     * @return element text
     */
    public String getText() {
        waitForVisible();
        return this.element().getText();
    }

    /**
     * Returns an attribute value from the element.
     *
     * @param name attribute name
     * @return attribute value, or {@code null} when absent
     */
    public String getAttribute(String name) {
        waitForVisible();
        return this.element().getAttribute(name);
    }
}
