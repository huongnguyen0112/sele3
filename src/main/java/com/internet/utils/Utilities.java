package com.internet.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;

import io.opentelemetry.api.internal.StringUtils;

@Slf4j
public class Utilities {
    public static String getProjectPath() {
        return System.getProperty("user.dir");
    }

    public static int getRandomNumberInRange(int min, int max) {
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    public static String getEnv(String key, String defaultValue) {
        String value = System.getenv(key);
        return StringUtils.isNullOrEmpty(value) ? defaultValue : value;
    }
}
