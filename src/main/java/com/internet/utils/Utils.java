package com.internet.utils;

import org.apache.commons.lang3.StringUtils;

public class Utils {
    public static String getEnv(String key, String defaultValue) {
        String value = System.getenv(key);
        return StringUtils.isEmpty(value) ? defaultValue : value;
    }
}
