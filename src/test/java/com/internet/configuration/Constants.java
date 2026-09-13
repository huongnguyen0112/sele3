package com.internet.configuration;

import com.internet.utils.Utilities;

public class Constants {
    public static final String BASE_URL = System.getProperty("baseUrl", Utilities.getEnv("BASE_URL", "https://www.google.com/"));
    public static String REPORT_IN_USE = System.getProperty("reportInUse", Utilities.getEnv("REPORT_IN_USE", "allure")); // accepted value: 'allure' and 'extend'
}
