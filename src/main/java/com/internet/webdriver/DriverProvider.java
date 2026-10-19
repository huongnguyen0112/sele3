package com.internet.webdriver;

import com.google.common.base.Throwables;
import com.internet.webdriver.selenium.AbstractDriverProvider;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.Set;

public class DriverProvider {
    private static final ThreadLocal<WebDriver> WEB_DRIVER = new ThreadLocal<>();

    /**
     * Discovers and creates the registered provider for the browser in the supplied configuration.
     *
     * @param config configuration whose browser name identifies the provider to load
     * @return the provider that creates the configured browser driver
     * @throws RuntimeException if provider discovery or instantiation fails
     */
    static AbstractDriverProvider<?> newInstance(DriverConfig config) {
        try {
            AbstractDriverProvider<?> abstractDriverProvider = new DriverLoader().loadDriverProviders(config.getBrowser());
            return abstractDriverProvider;
        } catch (Exception e) {
            throw new RuntimeException("Could not create new Driver instance. " + Throwables.getStackTraceAsString(e));
        }
    }

    /**
     * Retrieves the WebDriver previously associated with the calling thread.
     *
     * @return the calling thread's WebDriver, or {@code null} if no driver has been started there
     */
    public static WebDriver getWebDriver() {
        return WEB_DRIVER.get();
    }

    /**
     * Stores a WebDriver in the calling thread's driver context.
     *
     * @param driver WebDriver to associate with the calling thread
     */
    static void setWebDriver(WebDriver driver) {
        WEB_DRIVER.set(driver);
    }

    /**
     * Creates a browser driver through its registered provider and associates it with the calling thread.
     *
     * @param config browser and driver settings used to create the WebDriver
     */
    public static void startWebDriver(DriverConfig config) {
        WebDriver localWebDriver = newInstance(config).create(config);
        setWebDriver(localWebDriver);
    }

    /**
     * Navigates the current browser to the supplied URL.
     *
     * @param url destination URL
     */
    public static void navigateTo(String url) {
        getWebDriver().navigate().to(url);
    }

    /**
     * Retrieves the URL of the current browser page.
     *
     * @return current page URL
     */
    public static String getCurrentUrl() {
        return getWebDriver().getCurrentUrl();
    }

    /**
     * Navigates to the previous page in the current browser window's history.
     */
    public static void navigateBack() {
        getWebDriver().navigate().back();
    }

    /**
     * Navigates to the next page in the current browser window's history.
     */
    public static void navigateForward() {
        getWebDriver().navigate().forward();
    }

    /**
     * Reloads the current browser page.
     */
    public static void refreshPage() {
        getWebDriver().navigate().refresh();
    }

    /**
     * Closes the current browser window without ending the WebDriver session.
     */
    public static void closeWebDriver() {
        getWebDriver().close();
    }

    /**
     * Ends the WebDriver session and clears it from the calling thread.
     */
    public static void quitWebDriver() {
        try {
            getWebDriver().quit();
        } finally {
            WEB_DRIVER.remove();
        }
    }

    /**
     * Maximizes the current browser window.
     */
    public static void maximizeWindow() {
        getWebDriver().manage().window().maximize();
    }

    /**
     * Minimizes the current browser window.
     */
    public static void minimizeWindow() {
        getWebDriver().manage().window().minimize();
    }

    /**
     * Switches the current browser window to fullscreen mode.
     */
    public static void fullscreenWindow() {
        getWebDriver().manage().window().fullscreen();
    }

    /**
     * Sets the size of the current browser window.
     *
     * @param size requested window size
     */
    public static void setWindowSize(Dimension size) {
        getWebDriver().manage().window().setSize(size);
    }

    /**
     * Sets the position of the current browser window.
     *
     * @param position requested window position
     */
    public static void setWindowPosition(Point position) {
        getWebDriver().manage().window().setPosition(position);
    }

    /**
     * Switches to the browser window or tab with the supplied handle.
     *
     * @param windowHandle handle of the window or tab
     */
    public static void switchToWindow(String windowHandle) {
        getWebDriver().switchTo().window(windowHandle);
    }

    /**
     * Switches to a browser window or tab by its index in the current handle set.
     *
     * @param windowIndex zero-based index into the handles returned by {@link #getWindowHandles()}
     * @throws IndexOutOfBoundsException if the index does not identify an open window
     */
    public static void switchToWindow(int windowIndex) {
        ArrayList<String> windowHandles = new ArrayList<>(getWebDriver().getWindowHandles());
        if (windowIndex < 0 || windowIndex >= windowHandles.size()) {
            throw new IndexOutOfBoundsException("No browser window at index " + windowIndex);
        }
        switchToWindow(windowHandles.get(windowIndex));
    }

    /**
     * Retrieves the handles of all open browser windows and tabs.
     *
     * @return handles for all open windows and tabs
     */
    public static Set<String> getWindowHandles() {
        return getWebDriver().getWindowHandles();
    }
}
