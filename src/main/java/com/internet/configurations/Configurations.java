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
    private String env;
    private String test;
    private String suite;
    private String reports;
    private Integer retry;
    private Duration navigationTimeout;
    private Duration expectedTimeout;
    private Duration actionTimeout;
    private Duration payloadTimeout;
    private Duration timeout;
    private Duration pollingInterval;


    /**
     * Loads a configuration from a test resource JSON file.
     *
     * @param fileName configuration file name
    * @return deserialized test configuration
     */
    public static Configurations loadFromFile(String fileName) {
        String jsonConfigFile = "src/test/resources/configs/" + fileName;
        log.debug("Loading configuration from json file {}", jsonConfigFile);
        return JsonHelper.fromJsonFile(jsonConfigFile, Configurations.class);
    }

    /**
     * Initializes configuration values from the default JSON file and runtime overrides.
     *
     * <p>System properties take precedence over environment variables, which take precedence
     * over values loaded from the configuration file. Defaults are used when no value exists.</p>
     *
     * @return initialized configuration
     */
    public static Configurations init() {
        // Load configurations from a JSON file or set default values
        Configurations configurations = Configurations.loadFromFile("configurations.json");
        if (configurations == null) {
            configurations = Configurations.builder()
                .env            (System.getProperty("env",          Utilities.getEnv("ENV", "qat")))
                .test           (System.getProperty("test",         Utilities.getEnv("TEST", "testNG.xml")))
                .suite          (System.getProperty("suite",        Utilities.getEnv("SUITE", "smoke")))
                .reports        (System.getProperty("reports",      Utilities.getEnv("REPORTS", "allure")))
                .retry          (Integer.parseInt(System.getProperty("retry",         Utilities.getEnv("RETRY", "2"))))
                .navigationTimeout  (Duration.ofMillis(Long.parseLong(System.getProperty("navigation.timeout",  Utilities.getEnv("NAVIGATION_TIMEOUT", "45000")))))
                .expectedTimeout    (Duration.ofMillis(Long.parseLong(System.getProperty("expected.timeout",    Utilities.getEnv("EXPECTED_TIMEOUT", "15000")))))
                .actionTimeout      (Duration.ofMillis(Long.parseLong(System.getProperty("action.timeout",      Utilities.getEnv("ACTION_TIMEOUT", "25000")))))
                .payloadTimeout     (Duration.ofMillis(Long.parseLong(System.getProperty("page.load.timeout",   Utilities.getEnv("PAGE_LOAD_TIMEOUT", "30000")))))
                .timeout            (Duration.ofMillis(Long.parseLong(System.getProperty("timeout",             Utilities.getEnv("TIMEOUT", "4000")))))
                .pollingInterval    (Duration.ofMillis(Long.parseLong(System.getProperty("polling.interval",    Utilities.getEnv("POLLING_INTERVAL", "200")))))
                .build();
        } else {
            configurations.setEnv(      System.getProperty("env",       Utilities.getEnv("ENV", configurations.getEnv())));
            configurations.setTest(     System.getProperty("test",      Utilities.getEnv("TEST", configurations.getTest())));
            configurations.setSuite(    System.getProperty("suite",     Utilities.getEnv("SUITE", configurations.getSuite())));
            configurations.setReports(  System.getProperty("reports",   Utilities.getEnv("REPORTS", configurations.getReports())));
            configurations.setRetry(Integer.parseInt(System.getProperty("retry", Utilities.getEnv("RETRY", String.valueOf(configurations.getRetry())))));
            configurations.setNavigationTimeout(    Duration.ofMillis(Long.parseLong(System.getProperty("navigation.timeout",   Utilities.getEnv("NAVIGATION_TIMEOUT", String.valueOf(configurations.getNavigationTimeout().toMillis()))))));
            configurations.setExpectedTimeout(      Duration.ofMillis(Long.parseLong(System.getProperty("expected.timeout",     Utilities.getEnv("EXPECTED_TIMEOUT", String.valueOf(configurations.getExpectedTimeout().toMillis()))))));
            configurations.setActionTimeout(        Duration.ofMillis(Long.parseLong(System.getProperty("action.timeout",       Utilities.getEnv("ACTION_TIMEOUT", String.valueOf(configurations.getActionTimeout().toMillis()))))));
            configurations.setPayloadTimeout(       Duration.ofMillis(Long.parseLong(System.getProperty("page.load.timeout",    Utilities.getEnv("PAGE_LOAD_TIMEOUT", String.valueOf(configurations.getPayloadTimeout().toMillis()))))));
            configurations.setTimeout(              Duration.ofMillis(Long.parseLong(System.getProperty("timeout",              Utilities.getEnv("TIMEOUT", String.valueOf(configurations.getTimeout().toMillis()))))));
            configurations.setPollingInterval(      Duration.ofMillis(Long.parseLong(System.getProperty("polling.interval",     Utilities.getEnv("POLLING_INTERVAL", String.valueOf(configurations.getPollingInterval().toMillis()))))));
        }
        return configurations;
    }
}
