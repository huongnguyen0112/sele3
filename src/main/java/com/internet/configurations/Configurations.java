package com.internet.configurations;

import java.time.Duration;

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
        private String reports;
        private Duration timeout;
        private Duration pollingInterval;

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

        /**
         * Resolves a string configuration value from a system property, environment
         * variable,
         * or fallback value, in that order.
         *
         * @param systemPropertyKey system property name
         * @param environmentKey    environment variable name
         * @param defaultValue      fallback value
         * @return resolved configuration value
         */
        private static String setValue(String systemPropertyKey, String environmentKey, String defaultValue) {
                return System.getProperty(systemPropertyKey, Utilities.getEnv(environmentKey, defaultValue));
        }

        /**
         * Resolves a duration from a system property, environment variable, or fallback
         * duration.
         * The configured override value is interpreted as milliseconds.
         *
         * @param systemPropertyKey system property name
         * @param environmentKey    environment variable name
         * @param defaultValue      fallback duration
         * @return resolved duration
         */
        private static Duration setValue(String systemPropertyKey, String environmentKey, Duration defaultValue) {
                String value = System.getProperty(systemPropertyKey,
                                Utilities.getEnv(environmentKey, String.valueOf(defaultValue.toMillis())));
                return Duration.ofMillis(Long.parseLong(value));
        }

        /**
         * Loads the base configuration and applies system property or environment
         * variable
         * overrides, falling back to the default report and timing values when
         * necessary.
         *
         * @return initialized configuration
         */
        public static Configurations init() {
            Configurations configurations = loadFromFile("configurations.json");

            if (configurations == null) {
                configurations = Configurations.builder()
                    .reports(
                        setValue(
                            SystemPropertyKey.REPORTS,
                            EnvironmentKey.REPORTS,
                            "allure"))
                    .timeout(
                        Duration.ofMillis(
                             Long.parseLong(
                                setValue(
                                    SystemPropertyKey.TIMEOUT,
                                    EnvironmentKey.TIMEOUT,
                                    "10000"))))
                    .pollingInterval(
                        Duration.ofMillis(
                            Long.parseLong(
                                setValue(
                                    SystemPropertyKey.POLLING_INTERVAL,
                                    EnvironmentKey.POLLING_INTERVAL,
                                    "200"))))
                    .build();
                } else {
                    configurations.setReports(
                        setValue(
                            SystemPropertyKey.REPORTS,
                            EnvironmentKey.REPORTS,
                            configurations.getReports()));
                    configurations.setTimeout(
                        setValue(
                            SystemPropertyKey.TIMEOUT,
                            EnvironmentKey.TIMEOUT,
                            configurations.getTimeout()));
                    configurations.setPollingInterval(
                        setValue(
                            SystemPropertyKey.POLLING_INTERVAL,
                            EnvironmentKey.POLLING_INTERVAL,
                            configurations.getPollingInterval()));
                }

                return configurations;
        }
}
