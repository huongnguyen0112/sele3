package com.internet.webdriver;

import java.util.ServiceLoader;

import com.internet.webdriver.selenium.AbstractDriverProvider;

public class DriverLoader {
    public String loadDriverProviders(String browserName) {
        // Load all implementations of AbstractDriverProvider
        ServiceLoader<?> loader = ServiceLoader.load(AbstractDriverProvider.class);

        int count = 0;
        String driverClassName = null;
        for (Object loadedProvider : loader) {
            if (loadedProvider instanceof AbstractDriverProvider<?>) {
                AbstractDriverProvider<?> provider = (AbstractDriverProvider<?>) loadedProvider;
                if (provider.getClass().getName().contains(browserName)) {
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
