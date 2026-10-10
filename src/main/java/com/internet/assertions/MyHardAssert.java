package com.internet.assertions;

import java.time.Duration;
import java.util.Objects;

import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;

import com.internet.elements.MyElement;
import com.internet.reports.ReportProvider;
import com.internet.waits.ElementCondition;
import com.internet.waits.ElementConditions;
import com.internet.waits.MyWait;

/**
 * Hard assertions for primitive values and {@link MyElement} states.
 */
public final class MyHardAssert {
    /**
     * Prevents instances of this utility class.
     */
    private MyHardAssert() {
    }

    /**
     * Starts an element assertion using the timeout configured for {@link MyWait}.
     *
     * @param element the element to assert
     * @return an assertion object for the element
     * @throws NullPointerException if {@code element} is {@code null}
     * <p>Example: {@code MyHardAssert.assertThat(searchBox).isDisplayed();}</p>
     */
    public static ElementAssert assertThat(MyElement element) {
        return assertThat(element, null);
    }

    /**
     * Starts an assertion using a custom timeout.
     *
     * @param element the element to assert
     * @param timeout maximum time to wait, or {@code null} to use the configured timeout
     * @return an assertion object for the element
     * @throws NullPointerException if {@code element} is {@code null}
     * <p>Example: {@code MyHardAssert.assertThat(searchBox, Duration.ofSeconds(5)).isEnabled();}</p>
     */
    public static ElementAssert assertThat(MyElement element, Duration timeout) {
        return new ElementAssert(Objects.requireNonNull(element, "element must not be null"), timeout);
    }

    /**
     * Starts a hard assertion for an integer value.
     *
     * @param actual value being checked
     * @return an integer assertion object
     * <p>Example: {@code MyHardAssert.assertThat(resultCount).isGreaterThan(0);}</p>
     */
    public static IntAssert assertThat(int actual) {
        return new IntAssert(actual);
    }

    /**
     * Starts a hard assertion for a string value. A {@code null} actual value is
     * permitted and can be compared with {@link StringAssert#isEqualTo(String)}.
     *
     * @param actual value being checked
     * @return a string assertion object
     * <p>Example: {@code MyHardAssert.assertThat(pageTitle).contains("Results");}</p>
     */
    public static StringAssert assertThat(String actual) {
        return new StringAssert(actual);
    }

    /**
     * Starts a hard assertion for a boolean value.
     *
     * @param actual value being checked
     * @return a boolean assertion object
     * <p>Example: {@code MyHardAssert.assertThat(isLoggedIn).isTrue();}</p>
     */
    public static BooleanAssert assertThat(boolean actual) {
        return new BooleanAssert(actual);
    }

    /**
     * Creates an assertion failure and records its message in the active report.
     *
     * @param message failure message
     * @param cause underlying cause, or {@code null} if there is none
     * @return the assertion error to throw
     */
    private static AssertionError failure(String message, Throwable cause) {
        AssertionError failure = cause == null
                ? new AssertionError(message)
                : new AssertionError(message, cause);
        try {
            ReportProvider.fail(message);
        } catch (RuntimeException reportingFailure) {
            failure.addSuppressed(reportingFailure);
        }
        return failure;
    }

    private static String formatDuration(Duration duration) {
        long seconds = duration.getSeconds();
        int nanos = duration.getNano();
        if (nanos == 0) {
            return seconds + (seconds == 1 ? " second" : " seconds");
        }
        if (seconds == 0 && nanos % 1_000_000 == 0) {
            return (nanos / 1_000_000) + " ms";
        }
        if (seconds == 0) {
            return nanos + " ns";
        }
        return duration.toString();
    }

    /**
     * Fluent hard assertions for integer values.
     *
     * <p>Example: {@code MyHardAssert.assertThat(count).isGreaterThan(0);}</p>
     */
    public static final class IntAssert {
        private final int actual;

        /**
         * Creates an integer assertion for the supplied actual value.
         *
         * @param actual value being checked
         */
        private IntAssert(int actual) {
            this.actual = actual;
        }

        /**
         * Checks that the actual integer equals the expected integer.
         *
         * @param expected value expected to equal the actual value
         * @return this assertion object for chaining
         * @throws AssertionError if the values differ
         * <p>Example: {@code MyHardAssert.assertThat(3).isEqualTo(3);}</p>
         */
        public IntAssert isEqualTo(int expected) {
            if (actual != expected) {
                throw failure("Expected " + actual + " to be equal to " + expected + ".", null);
            }
            return this;
        }

        /**
         * Checks that the actual integer differs from the expected integer.
         *
         * @param expected value that must not equal the actual value
         * @return this assertion object for chaining
         * @throws AssertionError if the values are equal
         * <p>Example: {@code MyHardAssert.assertThat(3).isNotEqualTo(0);}</p>
         */
        public IntAssert isNotEqualTo(int expected) {
            if (actual == expected) {
                throw failure("Expected " + actual + " not to be equal to " + expected + ".", null);
            }
            return this;
        }

        /**
         * Checks that the actual integer is greater than the expected value.
         *
         * @param expected exclusive lower bound
         * @return this assertion object for chaining
         * @throws AssertionError if the actual value is less than or equal to the expected value
         * <p>Example: {@code MyHardAssert.assertThat(3).isGreaterThan(0);}</p>
         */
        public IntAssert isGreaterThan(int expected) {
            if (actual <= expected) {
                throw failure("Expected " + actual + " to be greater than " + expected + ".", null);
            }
            return this;
        }

        /**
         * Checks that the actual integer is less than the expected value.
         *
         * @param expected exclusive upper bound
         * @return this assertion object for chaining
         * @throws AssertionError if the actual value is greater than or equal to the expected value
         * <p>Example: {@code MyHardAssert.assertThat(3).isLessThan(10);}</p>
         */
        public IntAssert isLessThan(int expected) {
            if (actual >= expected) {
                throw failure("Expected " + actual + " to be less than " + expected + ".", null);
            }
            return this;
        }
    }

    /**
     * Fluent hard assertions for string values.
     *
     * <p>Example: {@code MyHardAssert.assertThat(title).contains("Search");}</p>
     */
    public static final class StringAssert {
        private final String actual;

        /**
         * Creates a string assertion for the supplied actual value.
         *
         * @param actual value being checked; may be {@code null}
         */
        private StringAssert(String actual) {
            this.actual = actual;
        }

        /**
         * Checks that the actual string equals the expected string.
         *
         * @param expected value expected to equal the actual value; may be {@code null}
         * @return this assertion object for chaining
         * @throws AssertionError if the values differ
         * <p>Example: {@code MyHardAssert.assertThat("Home").isEqualTo("Home");}</p>
         */
        public StringAssert isEqualTo(String expected) {
            if (!Objects.equals(actual, expected)) {
                throw failure("Expected \"" + actual + "\" to be equal to \"" + expected + "\".", null);
            }
            return this;
        }

        /**
         * Checks that the actual string differs from the expected string.
         *
         * @param expected value that must not equal the actual value; may be {@code null}
         * @return this assertion object for chaining
         * @throws AssertionError if the values are equal
         * <p>Example: {@code MyHardAssert.assertThat("Home").isNotEqualTo("Login");}</p>
         */
        public StringAssert isNotEqualTo(String expected) {
            if (Objects.equals(actual, expected)) {
                throw failure("Expected \"" + actual + "\" not to be equal to \"" + expected + "\".", null);
            }
            return this;
        }

        /**
         * Checks that the actual string contains the expected substring.
         *
         * @param expected substring that must be present; must not be {@code null}
         * @return this assertion object for chaining
         * @throws NullPointerException if {@code expected} is {@code null}
         * @throws AssertionError if the actual value is {@code null} or does not contain the substring
         * <p>Example: {@code MyHardAssert.assertThat("Search results").contains("results");}</p>
         */
        public StringAssert contains(String expected) {
            Objects.requireNonNull(expected, "expected must not be null");
            if (actual == null || !actual.contains(expected)) {
                throw failure("Expected \"" + actual + "\" to contain \"" + expected + "\".", null);
            }
            return this;
        }

        /**
         * Checks that the actual string is non-null and empty.
         *
         * @return this assertion object for chaining
         * @throws AssertionError if the actual value is {@code null} or not empty
         * <p>Example: {@code MyHardAssert.assertThat("").isEmpty();}</p>
         */
        public StringAssert isEmpty() {
            if (actual == null || !actual.isEmpty()) {
                throw failure("Expected \"" + actual + "\" to be empty.", null);
            }
            return this;
        }
    }

    /**
     * Fluent hard assertions for boolean values.
     *
     * <p>Example: {@code MyHardAssert.assertThat(isReady).isTrue();}</p>
     */
    public static final class BooleanAssert {
        private final boolean actual;

        /**
         * Creates a boolean assertion for the supplied actual value.
         *
         * @param actual value being checked
         */
        private BooleanAssert(boolean actual) {
            this.actual = actual;
        }

        /**
         * Checks that the actual value is {@code true}.
         *
         * @return this assertion object for chaining
         * @throws AssertionError if the actual value is {@code false}
         * <p>Example: {@code MyHardAssert.assertThat(isLoggedIn).isTrue();}</p>
         */
        public BooleanAssert isTrue() {
            if (!actual) {
                throw failure("Expected value to be true, but it was false.", null);
            }
            return this;
        }

        /**
         * Checks that the actual value is {@code false}.
         *
         * @return this assertion object for chaining
         * @throws AssertionError if the actual value is {@code true}
         * <p>Example: {@code MyHardAssert.assertThat(isLoggedOut).isFalse();}</p>
         */
        public BooleanAssert isFalse() {
            if (actual) {
                throw failure("Expected value to be false, but it was true.", null);
            }
            return this;
        }

        /**
         * Checks that the actual boolean equals the expected boolean.
         *
         * @param expected value expected to equal the actual value
         * @return this assertion object for chaining
         * @throws AssertionError if the values differ
         * <p>Example: {@code MyHardAssert.assertThat(isLoggedIn).isEqualTo(true);}</p>
         */
        public BooleanAssert isEqualTo(boolean expected) {
            if (actual != expected) {
                throw failure("Expected " + actual + " to be equal to " + expected + ".", null);
            }
            return this;
        }
    }

    /**
     * Fluent hard assertions for a web element's state and text.
     *
     * <p>Example: {@code MyHardAssert.assertThat(button).isDisplayed().isEnabled();}</p>
     */
    public static final class ElementAssert {
        private final MyElement element;
        private final Duration timeout;

        /**
         * Creates an element assertion with its wait timeout.
         *
         * @param element element being checked
         * @param timeout maximum wait time, or {@code null} to use the configured timeout
         */
        private ElementAssert(MyElement element, Duration timeout) {
            this.element = element;
            this.timeout = timeout;
        }

        /**
         * Waits until the element is displayed.
         *
         * @return this assertion object for chaining
         * @throws AssertionError if the element is not displayed before the wait times out
         * <p>Example: {@code MyHardAssert.assertThat(searchBox).isDisplayed();}</p>
         */
        public ElementAssert isDisplayed() {
            return satisfies("to become visible", ElementConditions.VISIBLE);
        }

        /**
         * Waits until the element is enabled.
         *
         * @return this assertion object for chaining
         * @throws AssertionError if the element is not enabled before the wait times out
         * <p>Example: {@code MyHardAssert.assertThat(submitButton).isEnabled();}</p>
         */
        public ElementAssert isEnabled() {
            return satisfies("to become enabled", ElementConditions.ENABLED);
        }

        /**
         * Waits until the element is checked.
         *
         * @return this assertion object for chaining
         * @throws AssertionError if the element is not checked before the wait times out
         * <p>Example: {@code MyHardAssert.assertThat(termsCheckbox).isChecked();}</p>
         */
        public ElementAssert isChecked() {
            return satisfies("to become checked", MyElement::isChecked);
        }

        /**
         * Waits until the element's text exactly matches the expected text.
         *
         * @param expectedText text expected from the element; must not be {@code null}
         * @return this assertion object for chaining
         * @throws NullPointerException if {@code expectedText} is {@code null}
         * @throws AssertionError if the text does not match before the wait times out
         * <p>Example: {@code MyHardAssert.assertThat(heading).hasText("Search");}</p>
         */
        public ElementAssert hasText(String expectedText) {
            Objects.requireNonNull(expectedText, "expectedText must not be null");
            return satisfies("to have text exactly \"" + expectedText + "\"",
                    candidate -> expectedText.equals(candidate.element().getText()));
        }

        /**
         * Waits until the element's text contains the expected text.
         *
         * @param expectedText substring expected in the element's text; must not be {@code null}
         * @return this assertion object for chaining
         * @throws NullPointerException if {@code expectedText} is {@code null}
         * @throws AssertionError if the text does not contain the substring before the wait times out
         * <p>Example: {@code MyHardAssert.assertThat(heading).containsText("Search");}</p>
         */
        public ElementAssert containsText(String expectedText) {
            Objects.requireNonNull(expectedText, "expectedText must not be null");
            return satisfies("to contain text \"" + expectedText + "\"",
                    candidate -> candidate.element().getText().contains(expectedText));
        }

        /**
         * Waits until a custom element condition is met.
         *
         * @param expectation description used in the assertion failure message
         * @param condition condition to evaluate
         * @return this assertion object
         * @throws NullPointerException if either argument is {@code null}
         * @throws AssertionError if the condition is not met before the wait times out; the message includes the
         *         unmet expectation and effective timeout duration
         * <p>Example: {@code MyHardAssert.assertThat(searchBox).satisfies("to contain query", element -> "Selenium".equals(element.element().getAttribute("value")));}</p>
         */
        public ElementAssert satisfies(String expectation, ElementCondition condition) {
            Objects.requireNonNull(expectation, "expectation must not be null");
            Objects.requireNonNull(condition, "condition must not be null");

            MyWait wait = new MyWait(element).configuredWait("Waiting for assertion: " + expectation, timeout);
            try {
                wait.ignoring(StaleElementReferenceException.class);
                wait.waitUntil(condition);
            } catch (TimeoutException e) {
                throw failure("Timed out waiting for the element " + expectation + " within "
                        + formatDuration(wait.getConfiguredTimeout()) + ".", e);
            }
            return this;
        }
    }
}
