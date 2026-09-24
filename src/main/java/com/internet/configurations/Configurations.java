package com.internet.configurations;

import java.time.Duration;

import com.internet.utils.JsonHelper;
import com.internet.utils.Utilities;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
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
         * Loads a configuration object from the JSON file in the test resources
         * configuration directory.
         *
         * @param fileName the JSON configuration file name to read
         * @return the deserialized configuration, or {@code null} if the file is not
         *         found or cannot be parsed
         */
        public static Configurations loadFromFile(String fileName) {
                String jsonConfigFile = "src/test/resources/configs/" + fileName;
                log.debug("Loading configuration from json file {}", jsonConfigFile);
                return JsonHelper.fromJsonFile(jsonConfigFile, Configurations.class);
        }

        /**
         * Resolves a string setting by checking the JVM system property first,
         * then the matching environment variable, and finally the fallback value.
         *
         * @param systemPropertyKey the name of the Java system property
         * @param environmentKey the name of the environment variable
         * @param defaultValue the fallback value to use when no override is present
         * @return the highest-priority configured value
         */
        private static String resolveValueWithPriority(String systemPropertyKey, String environmentKey, String defaultValue) {
                String systemPropertyValue = System.getProperty(systemPropertyKey);
                return systemPropertyValue != null
                                ? systemPropertyValue
                                : Utilities.getEnv(environmentKey, defaultValue);
        }

        /**
         * Resolves a duration setting using the JVM system property first, then the
         * environment variable, and finally the supplied default value. The override
         * value is interpreted as milliseconds.
         *
         * @param systemPropertyKey the name of the Java system property
         * @param environmentKey the name of the environment variable
         * @param defaultValue the fallback duration used when no override is present
         * @return the resolved duration in milliseconds
         */
        private static Duration resolveValueWithPriority(String systemPropertyKey, String environmentKey,@NonNull Duration defaultValue) {
                String systemPropertyValue = System.getProperty(systemPropertyKey);
                String value = systemPropertyValue != null
                                ? systemPropertyValue
                                : Utilities.getEnv(environmentKey, String.valueOf(defaultValue.toMillis()));
                return Duration.ofMillis(Long.parseLong(value));
        }

        /**
         * Loads the base test configuration, applies any system-property or
         * environment-variable overrides, and fills in default values for the report
         * and timing settings when they are missing.
         *
         * @return the initialized configuration with resolved report, timeout, and
         *         polling interval values
         */
        public static Configurations init() {
            return init(10000, 200);
        }

        /**
         * Loads the base configuration while using custom default values for the
         * timeout and polling interval. Any matching JVM system property or
         * environment variable override still takes precedence over those defaults.
         *
         * @param defaultTimeout the fallback timeout in milliseconds when no
         *        configuration value is provided
         * @param defaultPollingInterval the fallback polling interval in milliseconds
         *        when no configuration value is provided
         * @return the initialized configuration with custom default timing values
         */
        public static Configurations init(int defaultTimeout, int defaultPollingInterval) {
            Configurations configurations = loadFromFile("configurations.json");
            if (configurations == null) {
                configurations = Configurations.builder()
                    .reports("allure")
                    .timeout(Duration.ofMillis(defaultTimeout))
                    .pollingInterval(Duration.ofMillis(defaultPollingInterval))
                    .build();
            } else {
                configurations.setReports(configurations.getReports() == null ? "allure" : configurations.getReports());
                configurations.setTimeout(configurations.getTimeout() == null
                    ? Duration.ofMillis(defaultTimeout) : configurations.getTimeout());
                configurations.setPollingInterval(configurations.getPollingInterval() == null
                    ? Duration.ofMillis(defaultPollingInterval) : configurations.getPollingInterval());
            }

            configurations.setReports(
                resolveValueWithPriority(
                    SystemPropertyKey.REPORTS,
                    EnvironmentKey.REPORTS,
                    configurations.getReports()));
            configurations.setTimeout(
                resolveValueWithPriority(
                    SystemPropertyKey.TIMEOUT,
                    EnvironmentKey.TIMEOUT,
                    configurations.getTimeout()));
            configurations.setPollingInterval(
                resolveValueWithPriority(
                    SystemPropertyKey.POLLING_INTERVAL,
                    EnvironmentKey.POLLING_INTERVAL,
                    configurations.getPollingInterval()));

            return configurations;
        }
}
