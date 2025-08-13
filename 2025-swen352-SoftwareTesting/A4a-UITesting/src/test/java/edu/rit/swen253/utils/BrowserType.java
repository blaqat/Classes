package edu.rit.swen253.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;

/**
 * An enumeration of browser types that are supported for Web UI testing.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
public enum BrowserType {
  /**
   * A representation of the Google Chrome browser.
   */
  CHROME() {
    @Override
    void configureDriverExecutable(OperatingSystem operatingSystem) {
      switch (operatingSystem) {
        case WINDOWS ->
          System.setProperty("webdriver.chrome.driver", driverHome + "/windows/chromedriver.exe");
        case MAC_OSX ->
          System.setProperty("webdriver.chrome.driver", driverHome + "/mac/chromedriver");
        case LINUX ->
          System.setProperty("webdriver.chrome.driver", driverHome + "/linux/chromedriver");
      }
    }

    @Override
    WebDriver buildDriver(DesiredCapabilities capabilities) {
      ChromeOptions options = new ChromeOptions();
      options.setAcceptInsecureCerts(true);
      options.merge(capabilities);
      return new ChromeDriver(options);
    }
  },

  /**
   * A representation of the Mozilla Firefox browser.
   */
  FIREFOX() {
    @Override
    void configureDriverExecutable(OperatingSystem operatingSystem) {
      switch (operatingSystem) {
        case WINDOWS ->
          System.setProperty("webdriver.gecko.driver", driverHome + "/windows/geckodriver.exe");
        case MAC_OSX ->
          System.setProperty("webdriver.gecko.driver", driverHome + "/mac/geckodriver");
        case LINUX ->
          System.setProperty("webdriver.gecko.driver", driverHome + "/linux/geckodriver");
      }
    }

    @Override
    WebDriver buildDriver(DesiredCapabilities capabilities) {
      FirefoxProfile profile = new FirefoxProfile();
      profile.setAcceptUntrustedCertificates(true);
      profile.setAssumeUntrustedCertificateIssuer(false);

      //Use No Proxy Settings
      profile.setPreference("network.proxy.type", 0);
      FirefoxOptions options = new FirefoxOptions(capabilities);
      options.setProfile(profile);
      options.setLogLevel(FirefoxDriverLogLevel.WARN);

      return new FirefoxDriver(options);
    }
  },

  /**
   * A representation of the Apple Safari browser.
   */
  SAFARI() {
    @Override
    void configureDriverExecutable(OperatingSystem operatingSystem) {
      switch (operatingSystem) {
        case MAC_OSX ->
          System.setProperty("webdriver.safari.driver", "/usr/bin/safaridriver");
        case WINDOWS, LINUX ->
          throw new IllegalArgumentException("Safari not supported on this OS: " + operatingSystem);
      }
    }

    @Override
    WebDriver buildDriver(DesiredCapabilities capabilities) {
      final SafariOptions options = new SafariOptions();
      capabilities.setAcceptInsecureCerts(false);
      options.merge(capabilities);
      return new SafariDriver(options);
    }
  },

  /**
   * A representation of the Microsoft Edge browser.
   */
  EDGE() {
    @Override
    void configureDriverExecutable(OperatingSystem operatingSystem) {
      switch (operatingSystem) {
        case WINDOWS ->
          System.setProperty("webdriver.edge.driver", driverHome + "/windows/msedgedriver.exe");
        case MAC_OSX ->
          System.setProperty("webdriver.edge.driver", driverHome + "/mac/msedgedriver");
        case LINUX ->
          System.setProperty("webdriver.edge.driver", driverHome + "/linux/msedgedriver");
      }
    }

    @Override
    WebDriver buildDriver(DesiredCapabilities capabilities) {
      final EdgeOptions options = new EdgeOptions();
      options.setAcceptInsecureCerts(true);
      options.merge(capabilities);
      return new EdgeDriver(options);
    }
  }
  ;

  /**
   * Discover the {@linkplain BrowserType browser type} from the
   * custom Java property, {@code browserType}.
   *
   * @return  the {@link BrowserType} to run the UI tests against
   *
   * @throws IllegalArgumentException  if the BrowserType is not recognized
   */
  public static synchronized BrowserType discover() {
    if (cachedBrowser == null) {
      cachedBrowser = Optional.ofNullable(System.getProperty("browserType"))
        .map(BrowserType::valueOf)
        .orElse(BrowserType.CHROME);
    }
    return cachedBrowser;
  }
  private static volatile BrowserType cachedBrowser;

  /**
   * Query whether the current browser matches the specified browser.
   *
   * @see BrowserType#discover()
   * @param browser  the specific browser to test
   * @return {@code true} if the current browser matches the specified browser
   */
  public static boolean onBrowser(final BrowserType browser) {
    return BrowserType.discover().equals(browser);
  }

  /**
   * Query whether the current browser matches one of the specified types.
   *
   * @see BrowserType#discover()
   * @param types  the collector of browsers to test
   * @return {@code true} if the current browser matches on of these {@code types}
   */
  public static boolean onOneOfTheseBrowsers(BrowserType... types) {
    return Arrays.stream(types).anyMatch(it -> BrowserType.discover().equals(it));
  }

  /**
   * Drivers must be stored under the {@code drivers/} directory in the root of
   * the Maven project.
   */
  private static final Path driverHome =
    Path.of(System.getProperty("user.dir")).resolve("drivers");

  /**
   * Configure the Selenium driver based upon the host operating system.
   */
  abstract void configureDriverExecutable(OperatingSystem operatingSystem);

  /**
   * Build a Selenium {@link WebDriver} with a basic set of capabilities.
   */
  abstract WebDriver buildDriver(DesiredCapabilities capabilities);
}

