package com.internet.configuration;

import com.internet.utils.Utilities;

public class Constants {
    public static final String BASE_URL = "https://www.google.com.vn/";
    public static String REPORT_IN_USE = Utilities.getEnv("REPORT_IN_USE", "allure"); // accepted value: 'allure' and 'extend'
}
