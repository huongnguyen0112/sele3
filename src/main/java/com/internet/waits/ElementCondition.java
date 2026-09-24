package com.internet.waits;

import com.internet.elements.BaseElement;

public interface ElementCondition {
    /**
     * Checks if the condition is matched for the given element.
     *
     * @param element the element to check the condition against
     * @return {@code true} if the condition is satisfied, {@code false} otherwise
     */
    boolean matches(BaseElement element);
}
