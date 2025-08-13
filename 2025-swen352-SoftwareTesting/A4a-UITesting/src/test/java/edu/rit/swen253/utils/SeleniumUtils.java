package edu.rit.swen253.utils;

import edu.rit.swen253.page.AbstractPage;
import edu.rit.swen253.page.PageFactory;
import edu.rit.swen253.test.AbstractWebTest;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.FluentWait;

import java.time.Duration;
import java.util.logging.Logger;

/**
 * A Utility class for Selenium.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
public final class SeleniumUtils {
  private static final Logger logger = Logger.getLogger(SeleniumUtils.class.getName());

  public static synchronized SeleniumUtils getInstance() {
    if (instance == null) {
      instance = new SeleniumUtils();
    }
    return instance;
  }
  private static volatile SeleniumUtils instance;

  /**
   * Set up the {@link WebDriver} into the Utilities class.  This <strong>must</strong> be called
   * by the {@link AbstractWebTest#setUpSeleniumDriver()} method to set up the standard
   * set of {@link FluentWait} objects.
   */
  public void setupDriver() {
    // discover the three primary test properties
    final OperatingSystem operatingSystem = OperatingSystem.discover();
    final BrowserType browserType = BrowserType.discover();
    logger.info("Operating system: " + operatingSystem);
    logger.info("Browser: " + browserType);
    logger.info("Screen Mode: " + ScreenMode.discover());
    // configure Selenium and build the Driver
    browserType.configureDriverExecutable(operatingSystem);
    theDriver = buildDriver();
    // build common wait conditions
    shortWait = makeWait(ONE_SECOND);
    mediumWait = makeWait(FIVE_SECONDS);
    longWait = makeWait(TEN_SECONDS);
  }

  /**
   * Shutdown the Selenium driver. This is usually done in the
   * {@code AfterAll} JUnit life cycle method.
   * <p>
   *   See: {@link AbstractWebTest#tearDown()}.
   * </p>
   */
  public void shutdownDriver() {
    if (theDriver != null) {
      logger.info("Quiting the driver.");
      theDriver.quit();
    } else {
      logger.warning("No driver to quit.");
    }
  }

  public <P extends AbstractPage> P navigateToPage(String url, PageFactory<P> pageFactory) {
    theDriver.navigate().to(url);
    return pageFactory.createPage();
  }

  public String getCurrentUrl() {
    return theDriver.getCurrentUrl();
  }

  public void refresh() {
    theDriver.navigate().refresh();
  }

  public String getTitle() {
    return theDriver.getTitle();
  }

  public FluentWait<WebDriver> getShortWait() {
    return shortWait;
  }

  public FluentWait<WebDriver> getMediumWait() {
    return mediumWait;
  }

  public FluentWait<WebDriver> getLongWait() {
    return longWait;
  }

  /**
   * Make a {@linkplain FluentWait Selenium wait operator} with a specific timeout.
   *
   * @param timeout the length of time to wait for the condition
   * @return a {@link FluentWait} operator that can be used for any condition
   */
  public FluentWait<WebDriver> makeWait(final Duration timeout) {
    return new FluentWait<>(theDriver)
      .withTimeout(timeout)
      .pollingEvery(Duration.ofMillis(SHORT_INTERVAL))
      .ignoring(NoSuchElementException.class);
  }

  /**
   * Make a new {@link Actions} object from the test's Selenium driver.
   */
  public Actions makeAction() {
    return new Actions(theDriver);
  }

  /**
   * Perform a scroll down (Y-axis) action.
   * @param deltaY  how many pixels down
   */
  public void scrollDown(final int deltaY) {
    makeAction().scrollByAmount(0, deltaY).perform();
  }

  /**
   * Execute some JavaScript code in the browser against a specific DOM element.
   *
   * @param script  the JavaScript code, use "arguments[0]" to reference the element
   * @param element  the DOM element being referenced in the code
   */
  public void executeScript(final String script, final WebElement element) {
    ((JavascriptExecutor)theDriver).executeScript(script, element);
  }

  //
  // Private
  //

  /**
   * A short interval is half a second long (500ms).
   */
  private final int SHORT_INTERVAL = 500;

  private final Duration ONE_SECOND = Duration.ofSeconds(1);
  private final Duration FIVE_SECONDS = Duration.ofSeconds(5);
  private final Duration TEN_SECONDS = Duration.ofSeconds(10);

  private FluentWait<WebDriver> shortWait;
  private FluentWait<WebDriver> mediumWait;
  private FluentWait<WebDriver> longWait;

  /**
   * The {@link WebDriver} created for a single test run.
   */
  private WebDriver theDriver;

  /**
   * Initialize the driver depending upon the type of browser and system properties.
   *
   * @return WebDriver (ChromeDriver or FirefoxDriver)
   */
  private WebDriver buildDriver() {
    // create essential browser capabilities
    DesiredCapabilities capabilities = new DesiredCapabilities();
    capabilities.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
    // build the driver for a specific Browser
    final WebDriver driver = BrowserType.discover().buildDriver(capabilities);
    // more driver configuration
    driver.manage().window().setSize(ScreenMode.discover().getScreenSize());
    driver.manage().timeouts().implicitlyWait(ONE_SECOND);
    driver.manage().timeouts().pageLoadTimeout(TEN_SECONDS);
    //
    return driver;
  }


  /* hide ctor to complete the Utility idiom */
  private SeleniumUtils() {
  }
}
