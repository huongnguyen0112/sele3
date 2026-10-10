package com.internet.assertions;

import java.time.Duration;
import java.util.Objects;

import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;

import com.internet.elements.MyElement;
import com.internet.waits.ElementCondition;
import com.internet.waits.ElementConditions;
import com.internet.waits.MyWait;

/**
 * Assertions for {@link MyElement} that wait for the expected state.
 */
public final class MyHardAssert {
    private MyHardAssert() {
    }

    /**
     * Starts an assertion using the timeout configured for {@link MyWait}.
     *
     * @param element the element to assert
     * @return an assertion object for the element
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
     */
    public static ElementAssert assertThat(MyElement element, Duration timeout) {
        return new ElementAssert(Objects.requireNonNull(element, "element must not be null"), timeout);
    }

    public static final class ElementAssert {
        private final MyElement element;
        private final Duration timeout;

        private ElementAssert(MyElement element, Duration timeout) {
            this.element = element;
            this.timeout = timeout;
        }

        public ElementAssert isDisplayed() {
            return satisfies("to be displayed", ElementConditions.VISIBLE);
        }

        public ElementAssert isEnabled() {
            return satisfies("to be enabled", ElementConditions.ENABLED);
        }

        public ElementAssert isChecked() {
            return satisfies("to be checked", MyElement::isChecked);
        }

        public ElementAssert hasText(String expectedText) {
            Objects.requireNonNull(expectedText, "expectedText must not be null");
            return satisfies("to have text \"" + expectedText + "\"",
                    candidate -> expectedText.equals(candidate.element().getText()));
        }

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
         */
        public ElementAssert satisfies(String expectation, ElementCondition condition) {
            Objects.requireNonNull(expectation, "expectation must not be null");
            Objects.requireNonNull(condition, "condition must not be null");

            try {
                MyWait wait = new MyWait(element).configuredWait("Waiting for assertion: " + expectation, timeout);
                wait.ignoring(StaleElementReferenceException.class);
                wait.waitUntil(condition);
            } catch (TimeoutException e) {
                throw new AssertionError("Expected element " + expectation + " before the wait timed out.", e);
            }
            return this;
        }
    }
}
