package com.internet.webdriver;

import java.util.ServiceLoader;

import com.internet.webdriver.selenium.AbstractDriverProvider;

public class DriverLoader {
    /**
     * Loads the registered driver providers and finds the provider matching the browser name.
     *
     * @param browserName the browser name used to match a driver provider class
     * @return the fully qualified class name of the matching driver provider, or {@code null}
     *         when no matching provider is found
     */
    public String loadDriverProviders(String browserName) {
        // Load all implementations of AbstractDriverProvider
        ServiceLoader<?> loader = ServiceLoader.load(AbstractDriverProvider.class);

        int count = 0;
        String driverClassName = null;
        for (Object loadedProvider : loader) {
            if (loadedProvider instanceof AbstractDriverProvider<?>) {
                AbstractDriverProvider<?> provider = (AbstractDriverProvider<?>) loadedProvider;
                if (provider.getClass().getName().toLowerCase().contains(browserName.toLowerCase())) {
                    driverClassName = provider.getClass().getName();
                }
            }
            count++;
        }

        if (count != 0 && driverClassName != null) {
            System.out.println("Loaded Driver Provider: " + driverClassName);
        } else {    
            System.out.println("No Driver Provider found.");
        }
        
        return driverClassName;
    }
    
}
