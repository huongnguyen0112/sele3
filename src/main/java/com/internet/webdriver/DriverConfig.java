package com.internet.webdriver;

import com.internet.utils.JsonHelper;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

@Getter
@Setter
@Builder
@Slf4j
public class DriverConfig {
    private String browser;
    private String driverProviderLocation;
    private Map<String, Object> capabilities;
    private String[] arguments;
    private String remoteUrl;
    private boolean headless;

    /**
     * Converts the configured remote URL to a URL object.
     *
     * @return configured remote URL, or {@code null} when no remote URL is set
     * @throws RuntimeException when the configured URL is malformed
     */
    public URL getRemoteUrl() {
        try {
            if (this.remoteUrl != null && !this.remoteUrl.equals(""))
                return new URL(this.remoteUrl);
            return null;
        } catch (MalformedURLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Converts configured capability values to Selenium capabilities.
     *
     * @return Selenium capabilities created from the configuration map
     */
    public DesiredCapabilities getCapabilities() {
        return new DesiredCapabilities(capabilities);
    }

    /**
     * Loads a driver configuration from a test resource JSON file.
     *
     * @param fileName configuration file name
     * @return deserialized driver configuration
     */
    public static DriverConfig loadFromFile(String fileName) {
        String jsonConfigFile = "src/test/resources/configs/" + fileName;
        log.debug("Loading configuration from json file {}", jsonConfigFile);
        return JsonHelper.fromJsonFile(jsonConfigFile, DriverConfig.class);
    }
}
