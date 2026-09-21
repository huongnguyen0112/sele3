package com.internet.configurations;

import java.time.Duration;

import com.internet.utils.Utilities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Builder
@Slf4j
public class Configurations {
    private String env;
    private String test;
    private String suite;
    private String reports;
    private Integer retry;
    private Duration navigationTimeout;
    private Duration expectedTimeout;
    private Duration actionTimeout;
    private Duration pageloadTimeout;
    private Duration timeout;
    private Duration pollingInterval;

    public static Configurations init() {
        Configurations configurations = Configurations.builder()
                    .env(
                            System.getProperty(
                                    "env",
                                    Utilities.getEnv(
                                            "ENV",
                                            "qat")))
                    .test(
                            System.getProperty(
                                    "test",
                                    Utilities.getEnv(
                                            "TEST",
                                            "testNG.xml")))
                    .suite(
                            System.getProperty(
                                    "suite",
                                    Utilities.getEnv(
                                            "SUITE",
                                            "smoke")))
                    .reports(
                            System.getProperty(
                                    "reports",
                                    Utilities.getEnv(
                                            "REPORTS",
                                            "allure")))
                    .retry(
                            Integer.parseInt(
                                    System.getProperty(
                                            "retry",
                                            Utilities.getEnv(
                                                    "RETRY",
                                                    "2"))))
                    .navigationTimeout(
                            Duration.ofMillis(Long.parseLong(
                                    System.getProperty(
                                            "navigation.timeout",
                                            Utilities.getEnv(
                                                    "NAVIGATION_TIMEOUT", "45000")))))
                    .expectedTimeout(
                            Duration.ofMillis(Long.parseLong(
                                    System.getProperty(
                                            "expected.timeout",
                                            Utilities.getEnv(
                                                    "EXPECTED_TIMEOUT", "15000")))))
                    .actionTimeout(
                            Duration.ofMillis(Long.parseLong(
                                    System.getProperty(
                                            "action.timeout",
                                            Utilities.getEnv(
                                                    "ACTION_TIMEOUT", "25000")))))
                    .pageloadTimeout(
                            Duration.ofMillis(Long.parseLong(
                                    System.getProperty(
                                            "pageload.timeout",
                                            Utilities.getEnv(
                                                    "PAGE_LOAD_TIMEOUT", "30000")))))
                    .timeout(
                            Duration.ofMillis(Long.parseLong(
                                    System.getProperty(
                                            "timeout",
                                            Utilities.getEnv(
                                                    "TIMEOUT", "10000")))))
                    .pollingInterval(
                            Duration.ofMillis(Long.parseLong(
                                    System.getProperty(
                                            "polling.interval",
                                            Utilities.getEnv(
                                                    "POLLING_INTERVAL", "200")))))
                    .build();

        return configurations;
    }
}
