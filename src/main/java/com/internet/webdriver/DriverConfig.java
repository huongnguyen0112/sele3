package com.internet.webdriver;

import com.internet.utils.JsonHelper;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

@Getter
@Builder
@Slf4j
public class DriverConfig {
    private String browser;
    private String driverProviderLocation;
    private Map<String, Object> capabilities;
    private String[] arguments;
    private String remoteUrl;
    private boolean headless;

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

    public void setRemoteUrl(String remoteUrl) {
        this.remoteUrl = remoteUrl;
    }

    public void setHeadless(boolean headless) {
        this.headless = headless;
    }

    public DesiredCapabilities getCapabilities() {
        return new DesiredCapabilities(capabilities);
    }

    public static DriverConfig loadFromFile(String fileName) {
        String jsonConfigFile = "src/test/resources/configs/" + fileName;
        log.debug("Loading configuration from json file {}", jsonConfigFile);
        return JsonHelper.fromJsonFile(jsonConfigFile, DriverConfig.class);
    }
}
