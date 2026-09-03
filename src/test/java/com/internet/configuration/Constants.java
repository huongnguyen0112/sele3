package com.internet.configuration;

import com.internet.utils.Utils;

public class Constants {
    public static final String BASE_URL = "https://www.google.com.vn/";
    public static String REMOTE_URL = Utils.getEnv("REMOTE_URL", "");
    public static String REPORT_IN_USE = Utils.getEnv("REPORT_IN_USE", "allure"); // accepted value: 'allure' and 'extend'
    public static boolean HEADLESS = Boolean.parseBoolean(Utils.getEnv("HEADLESS", "false"));
}
