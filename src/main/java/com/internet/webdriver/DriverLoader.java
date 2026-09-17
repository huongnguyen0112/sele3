package com.internet.webdriver;

import java.util.ServiceLoader;

import com.internet.webdriver.selenium.AbstractDriverProvider;

public class DriverLoader {
    /**
     * Loads all registered {@link AbstractDriverProvider} implementations and returns the provider
     * whose class name matches the requested browser name, ignoring case.
     *
     * <p>The browser name is normalized by trimming whitespace. A blank or null value is rejected,
     * an unsupported browser throws an exception, and multiple matching providers trigger an error.
     *
     * @param browserName the browser name used to match a registered driver provider
     * @return the matching {@link AbstractDriverProvider} instance
     * @throws IllegalArgumentException if the browser name is blank, no provider matches, or more than
     *         one provider matches the requested browser
     */
    public AbstractDriverProvider<?> loadDriverProviders(String browserName) {
        if (browserName == null || browserName.isBlank()) {
            throw new IllegalArgumentException("Browser name must not be blank");
        }

        // Load all implementations of AbstractDriverProvider
        ServiceLoader<?> loader = ServiceLoader.load(AbstractDriverProvider.class);

        AbstractDriverProvider<?> provider = null;
        String requestedBrowser = browserName.trim();
        for (Object loadedProvider : loader) {
            if (loadedProvider instanceof AbstractDriverProvider<?>) {
                AbstractDriverProvider<?> candidate = (AbstractDriverProvider<?>) loadedProvider;
                String providerIdentifier = candidate.getClass().getSimpleName();
                if (providerIdentifier.endsWith("DriverProvider")) {
                    providerIdentifier = providerIdentifier.substring(0,
                            providerIdentifier.length() - "DriverProvider".length());
                } else if (providerIdentifier.endsWith("Provider")) {
                    providerIdentifier = providerIdentifier.substring(0,
                            providerIdentifier.length() - "Provider".length());
                }

                if (providerIdentifier.equalsIgnoreCase(requestedBrowser)) {
                    if (provider != null) {
                        throw new IllegalArgumentException(
                                "Multiple driver providers found for browser: " + browserName);
                    }
                    provider = candidate;
                }
            }
        }

        if (provider == null) {
            throw new IllegalArgumentException("Unsupported browser: " + browserName);
        }

        return provider;
    }
    
}
