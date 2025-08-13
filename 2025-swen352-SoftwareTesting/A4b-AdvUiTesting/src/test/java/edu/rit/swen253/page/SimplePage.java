package edu.rit.swen253.page;

import edu.rit.swen253.utils.SeleniumUtils;

/**
 * A concrete Page class that supplies a few basic pieces of information.
 * Use this when you really don't need a complex analysis or interaction with
 * the page that the test navigated to.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
public class SimplePage extends AbstractPage {
  /**
   * Get the web page title.
   */
  public String getTitle() {
    return SeleniumUtils.getInstance().getTitle();
  }

  /**
   * Get the web page URL.
   */
  public String getURL() {
    return SeleniumUtils.getInstance().getCurrentUrl();
  }
}
