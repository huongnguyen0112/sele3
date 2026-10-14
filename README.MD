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
- The browser selected for the TestNG run installed; the GitHub Actions default is Chrome.

## Run the tests

From the project root:

```bash
mvn test -Dsurefire.suiteXmlFiles=TestNG.xml
```

The default [TestNG.xml](TestNG.xml) suite is also run by the GitHub Actions workflow. The workflow runs tests for pull requests targeting `main`, and tests and publishes a Maven package to GitHub Packages on pushes to `main`, version tags (`v*`), and manual runs.

To run the Maven deployment locally, configure a Maven server with id `github` and credentials for GitHub Packages, then run `mvn deploy`.

### GitHub Actions inputs

When starting the workflow manually, these inputs configure the TestNG run:

| Input | Default | Description |
| --- | --- | --- |
| `suiteXmlFile` | `TestNG.xml` | TestNG suite XML file passed to Maven Surefire. |
| `browser` | `chrome` | Browser provider (`chrome`, `edge`, or `firefox`). |
| `reports` | `allure` | Report provider (`allure` or `extent`). |
| `timeout` | `5000` | Explicit wait timeout in milliseconds. |
| `pollingInterval` | `200` | Wait polling interval in milliseconds. |

The workflow uploads generated Allure results or Extent reports as a `test-reports` artifact.

## Configuration

Browser settings are loaded from `src/test/resources/configs/` by browser name. The repository includes `chrome.json`, `firefox.json`, and `edge.json`; choose the browser with the `browser` JVM property or `BROWSER` environment variable. A browser configuration can set:

| Setting | Description |
| --- | --- |
| `browser` | Browser provider name: `chrome`, `edge`, or `firefox`. |
| `capabilities` | Selenium capabilities to merge into the browser options. |
| `arguments` | Browser command-line arguments. |
| `remoteUrl` | Selenium remote endpoint; an empty string starts a local browser. |
| `headless` | Whether to start the browser in headless mode. |

Report and wait defaults are in `src/test/resources/configs/configurations.json`. The following overrides take precedence over that file:

| Setting | JVM system property | Environment variable |
| --- | --- | --- |
| Browser provider | `browser` | `BROWSER` |
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
