package com.internet.assertions;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import com.internet.elements.MyElement;
import com.internet.waits.ElementCondition;

/**
 * Soft assertions for primitive values and {@link MyElement}. Failed checks are
 * collected until {@link #assertAll()} is called or the test method finishes.
 */
public final class MySoftAssert {
    private static final ThreadLocal<List<AssertionError>> failures = ThreadLocal.withInitial(ArrayList::new);

    /**
     * Starts collecting integer assertions.
     *
     * @param actual value being checked
     * @return an integer assertion object
     * <p>Example: {@code MySoftAssert.assertThat(resultCount).isGreaterThan(0);}</p>
     */
    public static IntAssert assertThat(int actual) {
        return new IntAssert(actual);
    }

    /**
     * Starts collecting string assertions.
     *
     * @param actual value being checked
     * @return a string assertion object
     * <p>Example: {@code MySoftAssert.assertThat(pageTitle).contains("Results");}</p>
     */
    public static StringAssert assertThat(String actual) {
        return new StringAssert(actual);
    }

    /**
     * Starts collecting boolean assertions.
     *
     * @param actual value being checked
     * @return a boolean assertion object
     * <p>Example: {@code MySoftAssert.assertThat(isLoggedIn).isTrue();}</p>
     */
    public static BooleanAssert assertThat(boolean actual) {
        return new BooleanAssert(actual);
    }

    /**
     * Starts soft assertions using the configured wait timeout.
     *
     * @param element the element to assert
     * @return an assertion object for the element
     * @throws NullPointerException if {@code element} is {@code null}
     * <p>Example: {@code MySoftAssert.assertThat(searchBox).isDisplayed();}</p>
     */
    public static ElementAssert assertThat(MyElement element) {
        return assertThat(element, null);
    }

    /**
     * Starts soft assertions using a custom wait timeout.
     *
     * @param element the element to assert
     * @param timeout maximum time to wait, or {@code null} to use the configured timeout
     * @return an assertion object for the element
     * @throws NullPointerException if {@code element} is {@code null}
     * <p>Example: {@code MySoftAssert.assertThat(searchBox, Duration.ofSeconds(5)).isEnabled();}</p>
     */
    public static ElementAssert assertThat(MyElement element, Duration timeout) {
        Objects.requireNonNull(element, "element must not be null");
        return new ElementAssert(element, timeout);
    }

    /**
     * Throws one error containing all failures collected on the current thread.
     * Call this after completing the soft assertions in a test; failures
     * reported here are cleared and will not be reported again automatically.
     *
     * @throws AssertionError if one or more assertions failed
     * <p>Example: {@code MySoftAssert.assertAll();}</p>
     */
    public static void assertAll() {
        List<AssertionError> threadFailures = failures.get();
        if (threadFailures.isEmpty()) {
            return;
        }
        List<AssertionError> collectedFailures = new ArrayList<>(threadFailures);
        threadFailures.clear();
        AssertionError aggregate = new AssertionError(
                "The following " + collectedFailures.size() + " soft assertion(s) failed.");
        aggregate.initCause(collectedFailures.get(0));
        for (int i = 1; i < collectedFailures.size(); i++) {
            aggregate.addSuppressed(collectedFailures.get(i));
        }
        throw aggregate;
    }

    /**
     * Fluent soft assertions for integer values.
     *
     * <p>Example: {@code MySoftAssert.assertThat(count).isGreaterThan(0);}</p>
     */
    public static final class IntAssert {
        private final int actual;

        /**
         * Creates an integer assertion for the supplied value.
         *
         * @param actual value being checked
         */
        private IntAssert(int actual) {
            this.actual = actual;
        }

        /**
         * Collects a check that the actual integer equals the expected value.
         *
         * @param expected value expected to equal the actual value
         * @return this assertion object for chaining
         * <p>Example: {@code MySoftAssert.assertThat(3).isEqualTo(3);}</p>
         */
        public IntAssert isEqualTo(int expected) {
            record(() -> MyHardAssert.assertThat(actual).isEqualTo(expected));
            return this;
        }

        /**
         * Collects a check that the actual integer differs from the expected value.
         *
         * @param expected value that must not equal the actual value
         * @return this assertion object for chaining
         * <p>Example: {@code MySoftAssert.assertThat(3).isNotEqualTo(0);}</p>
         */
        public IntAssert isNotEqualTo(int expected) {
            record(() -> MyHardAssert.assertThat(actual).isNotEqualTo(expected));
            return this;
        }

        /**
         * Collects a check that the actual integer is greater than the expected value.
         *
         * @param expected exclusive lower bound
         * @return this assertion object for chaining
         * <p>Example: {@code MySoftAssert.assertThat(3).isGreaterThan(0);}</p>
         */
        public IntAssert isGreaterThan(int expected) {
            record(() -> MyHardAssert.assertThat(actual).isGreaterThan(expected));
            return this;
        }

        /**
         * Collects a check that the actual integer is less than the expected value.
         *
         * @param expected exclusive upper bound
         * @return this assertion object for chaining
         * <p>Example: {@code MySoftAssert.assertThat(3).isLessThan(10);}</p>
         */
        public IntAssert isLessThan(int expected) {
            record(() -> MyHardAssert.assertThat(actual).isLessThan(expected));
            return this;
        }
    }

    /**
     * Fluent soft assertions for string values.
     *
     * <p>Example: {@code MySoftAssert.assertThat(title).contains("Search");}</p>
     */
    public static final class StringAssert {
        private final String actual;

        /**
         * Creates a string assertion for the supplied value.
         *
         * @param actual value being checked; may be {@code null}
         */
        private StringAssert(String actual) {
            this.actual = actual;
        }

        /**
         * Collects a check that the actual string equals the expected value.
         *
         * @param expected value expected to equal the actual value; may be {@code null}
         * @return this assertion object for chaining
         * <p>Example: {@code MySoftAssert.assertThat("Home").isEqualTo("Home");}</p>
         */
        public StringAssert isEqualTo(String expected) {
            record(() -> MyHardAssert.assertThat(actual).isEqualTo(expected));
            return this;
        }

        /**
         * Collects a check that the actual string differs from the expected value.
         *
         * @param expected value that must not equal the actual value; may be {@code null}
         * @return this assertion object for chaining
         * <p>Example: {@code MySoftAssert.assertThat("Home").isNotEqualTo("Login");}</p>
         */
        public StringAssert isNotEqualTo(String expected) {
            record(() -> MyHardAssert.assertThat(actual).isNotEqualTo(expected));
            return this;
        }

        /**
         * Collects a check that the actual string contains the expected substring.
         *
         * @param expected substring that must be present; must not be {@code null}
         * @return this assertion object for chaining
         * <p>Example: {@code MySoftAssert.assertThat("Search results").contains("results");}</p>
         */
        public StringAssert contains(String expected) {
            record(() -> MyHardAssert.assertThat(actual).contains(expected));
            return this;
        }

        /**
         * Collects a check that the actual string is non-null and empty.
         *
         * @return this assertion object for chaining
         * <p>Example: {@code MySoftAssert.assertThat("").isEmpty();}</p>
         */
        public StringAssert isEmpty() {
            record(() -> MyHardAssert.assertThat(actual).isEmpty());
            return this;
        }
    }

    /**
     * Fluent soft assertions for boolean values.
     *
     * <p>Example: {@code MySoftAssert.assertThat(isReady).isTrue();}</p>
     */
    public static final class BooleanAssert {
        private final boolean actual;

        /**
         * Creates a boolean assertion for the supplied value.
         *
         * @param actual value being checked
         */
        private BooleanAssert(boolean actual) {
            this.actual = actual;
        }

        /**
         * Collects a check that the actual value is {@code true}.
         *
         * @return this assertion object for chaining
         * <p>Example: {@code MySoftAssert.assertThat(isLoggedIn).isTrue();}</p>
         */
        public BooleanAssert isTrue() {
            record(() -> MyHardAssert.assertThat(actual).isTrue());
            return this;
        }

        /**
         * Collects a check that the actual value is {@code false}.
         *
         * @return this assertion object for chaining
         * <p>Example: {@code MySoftAssert.assertThat(isLoggedOut).isFalse();}</p>
         */
        public BooleanAssert isFalse() {
            record(() -> MyHardAssert.assertThat(actual).isFalse());
            return this;
        }

        /**
         * Collects a check that the actual boolean equals the expected value.
         *
         * @param expected value expected to equal the actual value
         * @return this assertion object for chaining
         * <p>Example: {@code MySoftAssert.assertThat(isLoggedIn).isEqualTo(true);}</p>
         */
        public BooleanAssert isEqualTo(boolean expected) {
            record(() -> MyHardAssert.assertThat(actual).isEqualTo(expected));
            return this;
        }
    }

    /**
     * Fluent soft assertions for a web element's state and text.
     *
     * <p>Example: {@code MySoftAssert.assertThat(button).isDisplayed().isEnabled();}</p>
     */
    public static final class ElementAssert {
        private final MyElement element;
        private final Duration timeout;

        /**
         * Creates an element assertion for the supplied element.
         *
         * @param element element being checked
         * @param timeout maximum wait time, or {@code null} to use the configured timeout
         */
        private ElementAssert(MyElement element, Duration timeout) {
            this.element = element;
            this.timeout = timeout;
        }

        /**
         * Collects a check that the element becomes displayed.
         *
         * @return this assertion object for chaining
         * <p>Example: {@code MySoftAssert.assertThat(searchBox).isDisplayed();}</p>
         */
        public ElementAssert isDisplayed() {
            return check(assertion -> assertion.isDisplayed());
        }

        /**
         * Collects a check that the element becomes enabled.
         *
         * @return this assertion object for chaining
         * <p>Example: {@code MySoftAssert.assertThat(submitButton).isEnabled();}</p>
         */
        public ElementAssert isEnabled() {
            return check(assertion -> assertion.isEnabled());
        }

        /**
         * Collects a check that the element becomes checked.
         *
         * @return this assertion object for chaining
         * <p>Example: {@code MySoftAssert.assertThat(termsCheckbox).isChecked();}</p>
         */
        public ElementAssert isChecked() {
            return check(assertion -> assertion.isChecked());
        }

        /**
         * Collects a check that the element's text exactly matches the expected text.
         *
         * @param expectedText expected text; must not be {@code null}
         * @return this assertion object for chaining
         * @throws NullPointerException if {@code expectedText} is {@code null}
         * <p>Example: {@code MySoftAssert.assertThat(heading).hasText("Search");}</p>
         */
        public ElementAssert hasText(String expectedText) {
            Objects.requireNonNull(expectedText, "expectedText must not be null");
            return check(assertion -> assertion.hasText(expectedText));
        }

        /**
         * Collects a check that the element's text contains the expected text.
         *
         * @param expectedText expected substring; must not be {@code null}
         * @return this assertion object for chaining
         * @throws NullPointerException if {@code expectedText} is {@code null}
         * <p>Example: {@code MySoftAssert.assertThat(heading).containsText("Search");}</p>
         */
        public ElementAssert containsText(String expectedText) {
            Objects.requireNonNull(expectedText, "expectedText must not be null");
            return check(assertion -> assertion.containsText(expectedText));
        }

        /**
         * Collects a check that a custom element condition eventually becomes true.
         *
         * @param expectation description included in the reported failure
         * @param condition condition to evaluate while waiting
         * @return this assertion object for chaining
         * @throws NullPointerException if either argument is {@code null}
         * <p>Example: {@code MySoftAssert.assertThat(searchBox).satisfies("to contain query", element -> "Selenium".equals(element.element().getAttribute("value")));}</p>
         */
        public ElementAssert satisfies(String expectation, ElementCondition condition) {
            Objects.requireNonNull(expectation, "expectation must not be null");
            Objects.requireNonNull(condition, "condition must not be null");
            return check(assertion -> assertion.satisfies(expectation, condition));
        }

        /**
         * Runs a hard assertion and stores any resulting assertion failure for
         * later reporting by {@link #assertAll()} or when the test method finishes.
         *
         * @param assertion assertion operation to run
         * @return this element assertion for chaining
         */
        private ElementAssert check(Consumer<MyHardAssert.ElementAssert> assertion) {
            record(() -> assertion.accept(MyHardAssert.assertThat(element, timeout)));
            return this;
        }
    }

    /**
     * Runs an assertion and collects an {@link AssertionError} instead of
     * propagating it immediately.
     *
     * @param assertion assertion operation to run
     */
    private static void record(Runnable assertion) {
        try {
            assertion.run();
        } catch (AssertionError failure) {
            failures.get().add(failure);
        }
    }
}
