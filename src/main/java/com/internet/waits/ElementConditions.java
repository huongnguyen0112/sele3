package com.internet.waits;

/**
 * Reusable conditions for waiting on element state.
 */
public class ElementConditions {

    /** Matches when the element is displayed. */
    public static final ElementCondition VISIBLE = element -> element.isDisplayed();

    /** Matches when the element keeps the same bounding box for consecutive animation frames. */
    public static final ElementCondition STABLE = element -> element.isStable();

    /** Matches when the element is enabled. */
    public static final ElementCondition ENABLED = element -> element.isEnabled();

    /** Matches when the element is enabled and not readonly. */
    public static final ElementCondition EDITABLE = element -> element.isEditable();

    /** Matches when the element is the pointer hit target at the action point. */
    public static final ElementCondition NOT_OVERLAID = element -> element.isNotOverlaid();

    /** Matches when the element is not displayed. */
    public static final ElementCondition NOT_VISIBLE = element -> !element.isDisplayed();

    /** Matches when the element is not enabled. */
    public static final ElementCondition NOT_ENABLED = element -> !element.isEnabled();
}
