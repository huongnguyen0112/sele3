# Selenium WebDriver Test Framework

A Java and Maven test automation project built around Selenium WebDriver and TestNG. It provides reusable browser setup, element interactions and waits, assertions, and pluggable test reporting.

## Features

- Browser providers discovered through Java's `ServiceLoader` for Chrome, Edge, and Firefox.
- Local or remote WebDriver creation, configurable browser arguments, capabilities, and headless mode.
- Thread-local WebDriver storage for tests running in parallel.
- `MyElement` helpers, configurable explicit waits, and hard and soft assertions.
- Pluggable Allure and Extent report providers.
- JSON configuration with JVM system-property and environment-variable overrides for report and wait settings.

## Requirements

- JDK 25 (the Maven compiler is configured with `release` 25).
- Maven.
- Chrome and Edge installed to run the default TestNG suite, which launches both browsers in parallel.

## Run the tests

From the project root:

```bash
mvn test
```

The default [TestNG.xml](TestNG.xml) suite runs `SearchTests` with separate Chrome and Edge configurations. `BaseTest` loads the selected browser configuration before each test and closes the driver afterward.

## Configuration

Browser settings are loaded from `src/test/resources/configs/` using the `config` parameter in `TestNG.xml`. The repository includes `chrome.json` and `edge.json`. A browser configuration can set:

| Setting | Description |
| --- | --- |
| `browser` | Browser provider name: `chrome`, `edge`, or `firefox`. |
| `capabilities` | Selenium capabilities to merge into the browser options. |
| `arguments` | Browser command-line arguments. |
| `remoteUrl` | Selenium remote endpoint; an empty string starts a local browser. |
| `headless` | Whether to start the browser in headless mode. |

Report and wait defaults are in `src/test/resources/configs/configurations.json`. Its current settings select Allure, a 4-second wait timeout, and a 200-millisecond polling interval. The following overrides take precedence over that file:

| Setting | JVM system property | Environment variable |
| --- | --- | --- |
| Report provider | `reports` | `REPORTS` |
| Wait timeout in milliseconds | `timeout` | `TIMEOUT` |
| Wait polling interval in milliseconds | `pollingInterval` | `POLLING_INTERVAL` |

For example:

```bash
mvn test -Dtimeout=10000 -DpollingInterval=250 -Dreports=allure
```

The test base URL defaults to `https://www.google.com/`. Override it with the `baseUrl` JVM property or the `BASE_URL` environment variable.

## Project structure

```text
src/main/java/com/internet/
  assertions/       Hard and soft assertions and TestNG assertion listener
  configurations/   Framework report and wait configuration
  elements/         Selenium element wrapper and interactions
  reports/          Report facade, provider discovery, and integrations
  utils/            JSON, duration, and general utilities
  waits/            Element conditions and explicit wait helpers
  webdriver/        Driver configuration, provider discovery, and lifecycle
src/main/resources/META-INF/services/
                    Registered browser and report providers
src/test/java/com/internet/
  pages/            Page objects
  tests/            TestNG test cases and shared test lifecycle
src/test/resources/configs/
                    Browser and framework JSON configuration
```

TestNG writes Allure results to `target/allure-results` as configured by Maven Surefire.
