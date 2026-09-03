package com.internet.util;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;

@Slf4j
public class Utilities {
    public static String getProjectPath() {
        return System.getProperty("user.dir");
    }

    public static int getRandomNumberInRange(int min, int max) {
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }
}
