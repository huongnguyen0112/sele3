package com.internet.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;

import io.opentelemetry.api.internal.StringUtils;
import org.openqa.selenium.JavascriptExecutor;

import com.internet.webdriver.DriverProvider;

@Slf4j
public class Utilities {
    /**
     * Returns the current working directory of the project.
     *
     * @return project working directory
     */
    public static String getProjectPath() {
        return System.getProperty("user.dir");
    }

    /**
     * Generates a random integer within an inclusive range.
     *
     * @param min minimum allowed value
     * @param max maximum allowed value
     * @return random integer between {@code min} and {@code max}
     */
    public static int getRandomNumberInRange(int min, int max) {
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    /**
     * Reads an environment variable and falls back when it is absent or empty.
     *
     * @param key environment variable name
     * @param defaultValue value to return when the variable is unavailable
     * @return environment variable value or the default value
     */
    public static String getEnv(String key, String defaultValue) {
        String value = System.getenv(key);
        return StringUtils.isNullOrEmpty(value) ? defaultValue : value;
    }

    /**
     * Executes JavaScript in the current browser window.
     *
     * @param script JavaScript source to execute
     * @param args arguments available to the script as {@code arguments}
     * @return the script result, or {@code null} when the script has no result
     */
    public static Object executeJavaScript(String script, Object... args) {
        return ((JavascriptExecutor) DriverProvider.getWebDriver()).executeScript(script, args);
    }
}
