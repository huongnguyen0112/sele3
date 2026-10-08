package com.internet.configuration;

import com.internet.utils.Utilities;

public class Constants {
    public static final String BASE_URL = System.getProperty("baseUrl", Utilities.getEnv("BASE_URL", "https://www.google.com/"));
}
