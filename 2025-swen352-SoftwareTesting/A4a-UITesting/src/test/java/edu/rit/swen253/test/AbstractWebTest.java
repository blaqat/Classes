package edu.rit.swen253.test;

import edu.rit.swen253.page.AbstractPage;
import edu.rit.swen253.page.PageFactory;
import edu.rit.swen253.utils.SeleniumUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import static org.junit.jupiter.api.Assertions.fail;


/**
 * The base class for all Web UI test classes.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
@TestInstance(Lifecycle.PER_CLASS)
public abstract class AbstractWebTest {

  private final SeleniumUtils seleniumUtils;

  protected AbstractWebTest() {
    seleniumUtils = SeleniumUtils.getInstance();
  }

  //
  // Test life cycle methods
  //

  /**
   * The Set-up method for every Web UI test class.
   */
  @BeforeAll
  public void setUpSeleniumDriver() {
    // configure logger
    System.setProperty(
      "java.util.logging.SimpleFormatter.format",
      // <DATE> <SOURCE> <LEVEL> <MESSAGE>
      "%1$tb %1$td, %1$tY %1$tl:%1$tM:%1$tS %1$Tp %2$s %4$s: %5$s%n"
    );
    // setup and build Selenium driver
    seleniumUtils.setupDriver();
  }

  /**
   * TestNG clean up method.
   */
  @AfterAll
  public final void tearDown() {
    seleniumUtils.shutdownDriver();
  }

  //
  // Protected methods
  //

  /**
   * Navigate to any URL and create Page object when it is rendered.
   *
   * @param pageURL  the URL string to navigate to
   * @param pageFactory  the Factory function to generate the Page Object
   * @return the Page Object
   */
  protected <P extends AbstractPage> P navigateToPage(final String pageURL, final PageFactory<P> pageFactory) {
    return seleniumUtils.navigateToPage(pageURL, pageFactory);
  }

  /**
   * Assert that the driver is on a new page and return the Page Object for that page.
   * @param pageFactory  the Factory function to generate the Page Object
   * @return the Page Object
   */
  protected <P extends AbstractPage> P assertNewPage(final PageFactory<P> pageFactory) {
    try {
      return pageFactory.createPage();
    } catch (Exception e) {
      fail("Page creation failed: " + e.getMessage());
      return null;
    }
  }

}
