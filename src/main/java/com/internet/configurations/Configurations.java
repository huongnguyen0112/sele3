package com.internet.configurations;

import com.internet.utils.JsonHelper;
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
        private Integer navigationTimeout;
        private Integer expectedTimeout;
        private Integer actionTimeout;
        private Integer pageloadTimeout;
        private Integer timeout;
        private Integer pollingInterval;

        /**
         * Loads a configuration from a test resource JSON file.
         *
         * @param fileName configuration file name
         * @return deserialized configuration
         */
        public static Configurations loadFromFile(String fileName) {
                String jsonConfigFile = "src/test/resources/configs/" + fileName;
                log.debug("Loading configuration from json file {}", jsonConfigFile);
                return JsonHelper.fromJsonFile(jsonConfigFile, Configurations.class);
        }

        public static Configurations init() {
                Configurations configurations = loadFromFile("configurations.json");
                if (configurations == null) {
                        configurations = Configurations.builder()
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
                                                        Integer.parseInt(
                                                                        System.getProperty(
                                                                                        "navigation.timeout",
                                                                                        Utilities.getEnv(
                                                                                                        "NAVIGATION_TIMEOUT",
                                                                                                        "45000"))))
                                        .expectedTimeout(
                                                        Integer.parseInt(
                                                                        System.getProperty(
                                                                                        "expected.timeout",
                                                                                        Utilities.getEnv(
                                                                                                        "EXPECTED_TIMEOUT",
                                                                                                        "15000"))))
                                        .actionTimeout(
                                                        Integer.parseInt(
                                                                        System.getProperty(
                                                                                        "action.timeout",
                                                                                        Utilities.getEnv(
                                                                                                        "ACTION_TIMEOUT",
                                                                                                        "25000"))))
                                        .pageloadTimeout(
                                                        Integer.parseInt(
                                                                        System.getProperty(
                                                                                        "pageload.timeout",
                                                                                        Utilities.getEnv(
                                                                                                        "PAGE_LOAD_TIMEOUT",
                                                                                                        "30000"))))
                                        .timeout(
                                                        Integer.parseInt(
                                                                        System.getProperty(
                                                                                        "timeout",
                                                                                        Utilities.getEnv(
                                                                                                        "TIMEOUT",
                                                                                                        "10000"))))
                                        .pollingInterval(
                                                        Integer.parseInt(
                                                                        System.getProperty(
                                                                                        "polling.interval",
                                                                                        Utilities.getEnv(
                                                                                                        "POLLING_INTERVAL",
                                                                                                        "200"))))
                                        .build();
                } else {
                        configurations.setEnv(
                                        System.getProperty(
                                                        "env",
                                                        Utilities.getEnv(
                                                                        "ENV",
                                                                        configurations.getEnv())));
                        configurations.setTest(
                                        System.getProperty(
                                                        "test",
                                                        Utilities.getEnv(
                                                                        "TEST",
                                                                        configurations.getTest())));
                        configurations.setSuite(
                                        System.getProperty(
                                                        "suite",
                                                        Utilities.getEnv(
                                                                        "SUITE",
                                                                        configurations.getSuite())));
                        configurations.setReports(
                                        System.getProperty(
                                                        "reports",
                                                        Utilities.getEnv(
                                                                        "REPORTS",
                                                                        configurations.getReports())));
                        configurations.setRetry(
                                        Integer.parseInt(
                                                        System.getProperty(
                                                                        "retry",
                                                                        Utilities.getEnv(
                                                                                        "RETRY",
                                                                                        configurations.getRetry()
                                                                                                        .toString()))));
                        configurations.setNavigationTimeout(
                                        Integer.parseInt(
                                                        System.getProperty(
                                                                        "navigation.timeout",
                                                                        Utilities.getEnv(
                                                                                        "NAVIGATION_TIMEOUT",
                                                                                        configurations.getNavigationTimeout()
                                                                                                        .toString()))));
                        configurations.setExpectedTimeout(
                                        Integer.parseInt(
                                                        System.getProperty(
                                                                        "expected.timeout",
                                                                        Utilities.getEnv(
                                                                                        "EXPECTED_TIMEOUT",
                                                                                        configurations.getExpectedTimeout()
                                                                                                        .toString()))));
                        configurations.setActionTimeout(
                                        Integer.parseInt(
                                                        System.getProperty(
                                                                        "action.timeout",
                                                                        Utilities.getEnv(
                                                                                        "ACTION_TIMEOUT",
                                                                                        configurations.getActionTimeout()
                                                                                                        .toString()))));
                        configurations.setPageloadTimeout(
                                        Integer.parseInt(
                                                        System.getProperty(
                                                                        "pageload.timeout",
                                                                        Utilities.getEnv(
                                                                                        "PAGE_LOAD_TIMEOUT",
                                                                                        configurations.getPageloadTimeout()
                                                                                                        .toString()))));
                        configurations.setTimeout(
                                        Integer.parseInt(
                                                        System.getProperty(
                                                                        "timeout",
                                                                        Utilities.getEnv(
                                                                                        "TIMEOUT",
                                                                                        configurations.getTimeout()
                                                                                                        .toString()))));
                        configurations.setPollingInterval(
                                        Integer.parseInt(
                                                        System.getProperty(
                                                                        "polling.interval",
                                                                        Utilities.getEnv(
                                                                                        "POLLING_INTERVAL",
                                                                                        configurations.getPollingInterval()
                                                                                                        .toString()))));

                }

                return configurations;
        }
}
