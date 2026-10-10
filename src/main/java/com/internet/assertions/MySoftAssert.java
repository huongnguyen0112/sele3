package com.internet.assertions;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import com.internet.elements.MyElement;
import com.internet.waits.ElementCondition;

/**
 * Soft assertions for {@link MyElement}. Failed checks are collected until
 * {@link #assertAll()} is called.
 */
public final class MySoftAssert {
    private final List<AssertionError> failures = new ArrayList<>();

    /**
     * Starts soft assertions using the configured wait timeout.
     *
     * @param element the element to assert
     * @return an assertion object for the element
     */
    public ElementAssert assertThat(MyElement element) {
        return assertThat(element, null);
    }

    /**
     * Starts soft assertions using a custom wait timeout.
     *
     * @param element the element to assert
     * @param timeout maximum time to wait, or {@code null} to use the configured timeout
     * @return an assertion object for the element
     */
    public ElementAssert assertThat(MyElement element, Duration timeout) {
        Objects.requireNonNull(element, "element must not be null");
        return new ElementAssert(element, timeout);
    }

    /**
     * Throws one error containing all failures collected by this instance.
     * Call this after completing the soft assertions in a test.
     */
    public void assertAll() {
        if (failures.isEmpty()) {
            return;
        }

        AssertionError aggregate = new AssertionError(
                "The following " + failures.size() + " soft assertion(s) failed.");
        aggregate.initCause(failures.get(0));
        for (int i = 1; i < failures.size(); i++) {
            aggregate.addSuppressed(failures.get(i));
        }
        throw aggregate;
    }

    public final class ElementAssert {
        private final MyElement element;
        private final Duration timeout;

        private ElementAssert(MyElement element, Duration timeout) {
            this.element = element;
            this.timeout = timeout;
        }

        public ElementAssert isDisplayed() {
            return check(assertion -> assertion.isDisplayed());
        }

        public ElementAssert isEnabled() {
            return check(assertion -> assertion.isEnabled());
        }

        public ElementAssert isChecked() {
            return check(assertion -> assertion.isChecked());
        }

        public ElementAssert hasText(String expectedText) {
            Objects.requireNonNull(expectedText, "expectedText must not be null");
            return check(assertion -> assertion.hasText(expectedText));
        }

        public ElementAssert containsText(String expectedText) {
            Objects.requireNonNull(expectedText, "expectedText must not be null");
            return check(assertion -> assertion.containsText(expectedText));
        }

        public ElementAssert satisfies(String expectation, ElementCondition condition) {
            Objects.requireNonNull(expectation, "expectation must not be null");
            Objects.requireNonNull(condition, "condition must not be null");
            return check(assertion -> assertion.satisfies(expectation, condition));
        }

        private ElementAssert check(Consumer<MyHardAssert.ElementAssert> assertion) {
            try {
                assertion.accept(MyHardAssert.assertThat(element, timeout));
            } catch (AssertionError failure) {
                failures.add(failure);
            }
            return this;
        }
    }
}
