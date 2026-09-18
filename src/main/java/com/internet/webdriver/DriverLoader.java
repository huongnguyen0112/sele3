package com.internet.webdriver;

import java.util.ServiceLoader;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.internet.webdriver.selenium.AbstractDriverProvider;

public class DriverLoader {
    private static final ConcurrentMap<String, AbstractDriverProvider<?>> PROVIDERS = new ConcurrentHashMap<>();

    /**
     * Returns the registered driver provider for the requested browser.
     *
     * <p>The browser name is trimmed and compared case-insensitively with the provider class name
     * after removing the {@code DriverProvider} suffix. The first lookup scans the registered
     * providers and caches the matching instance; subsequent lookups for the same browser return
     * the cached instance without initializing it again.
     *
     * @param browserName the browser name used to locate a registered driver provider
     * @return the cached matching {@link AbstractDriverProvider} instance
     * @throws IllegalArgumentException if the browser name is null or blank, no provider matches,
     *         or more than one provider matches the requested browser
     */
    public AbstractDriverProvider<?> loadDriverProviders(String browserName) {
        if (browserName == null || browserName.isBlank()) {
            throw new IllegalArgumentException("Browser name must not be blank");
        }

        String requestedBrowser = browserName.trim();
        String browserKey = requestedBrowser.toLowerCase(java.util.Locale.ROOT);
        return PROVIDERS.computeIfAbsent(browserKey, key -> loadProvider(requestedBrowser, browserName));
    }

    /**
     * Returns the parameterized service type used to load driver providers.
     *
     * <p>The cast is required because Java class literals cannot retain the wildcard generic
     * parameter of {@link AbstractDriverProvider}. The runtime service type is still the raw class,
     * while the returned reference preserves generic type information for the rest of this class.
     *
     * @return the service type for {@link AbstractDriverProvider} implementations
     */
    @SuppressWarnings({"unchecked" })
    private Class<AbstractDriverProvider<?>> providerType() {
        return (Class<AbstractDriverProvider<?>>) (Class<?>) AbstractDriverProvider.class;
    }

    /**
     * Scans the registered services for a provider matching the requested browser.
     *
     * <p>A provider matches when its simple class name equals the requested browser name after the
     * {@code DriverProvider} suffix is removed. Exactly one matching provider must exist; its
     * instance is created by {@link ServiceLoader.Provider#get()} and returned to the caller.
     *
     * @param requestedBrowser the trimmed browser name to match
     * @param browserName the original browser name, used in error messages
     * @return the matching provider instance
     * @throws IllegalArgumentException if no provider or multiple providers match
     */
    private AbstractDriverProvider<?> loadProvider(String requestedBrowser, String browserName) {
        List<ServiceLoader.Provider<AbstractDriverProvider<?>>> matches = ServiceLoader
                .load(providerType())
                .stream()
                .filter(provider -> provider.type().getSimpleName()
                        .replaceFirst("DriverProvider$", "")
                        .equalsIgnoreCase(requestedBrowser))
                .toList();

        if (matches.size() > 1) {
            throw new IllegalArgumentException(
                    "Multiple driver providers found for browser: " + browserName);
        }
        if (matches.isEmpty()) {
            throw new IllegalArgumentException("Unsupported browser: " + browserName);
        }

        return matches.get(0).get();
    }
    
}
