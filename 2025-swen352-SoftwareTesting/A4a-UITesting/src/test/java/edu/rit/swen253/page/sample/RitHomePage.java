package edu.rit.swen253.page.sample;

import edu.rit.swen253.page.AbstractPage;
import edu.rit.swen253.utils.BrowserType;
import edu.rit.swen253.utils.DomElement;
import edu.rit.swen253.utils.SeleniumUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;

import java.util.List;

import static edu.rit.swen253.utils.BrowserType.*;
import static org.junit.jupiter.api.Assertions.fail;


/**
 * A Page Object for the RIT Home page.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
public class RitHomePage extends AbstractPage {

  /**
   * A finder to extract the main body content.
   * WARN: this is fragile code
   */
  private static final By MAIN_CONTENT_FINDER =
    By.cssSelector("main > div.advanced-page-content > div.field--name-field-content");

  private DomElement mainContentPanel;

  public RitHomePage() {
    super();
    // validate basic page structure
    try {
      mainContentPanel = findOnPage(MAIN_CONTENT_FINDER);
    } catch (TimeoutException e) {
      fail("Main content panel not found");
    }
  }

  public List<RitAreaOfStudyLink> getStudyLinks() {
    // the Study Links panel is in the eight <div> of the main content
    // WARN: this is fragile code
    return mainContentPanel.findChildBy(By.xpath("div[8]"))
      // scroll the "Areas of Study" list into view
      .doWith(listElement -> {
        switch (BrowserType.discover()) {
          // Chromium-based Selenium drivers handle DOM element scrolling well-enough
          case CHROME, EDGE -> listElement.scrollIntoView();
          // bugs in these Selenium drivers require a more "manual" scrolling approach
          case FIREFOX, SAFARI -> SeleniumUtils.getInstance().scrollDown(2700);
        }
      })
      // Chromium browsers need an additional nudge to push the list into view
      .doWith(listElement -> {
        if (BrowserType.onOneOfTheseBrowsers(CHROME, EDGE)) {
          SeleniumUtils.getInstance().scrollDown(200);
        }
      })
      // the curriculum links are organized in an unordered list embedded in a layout div
      .findChildBy(By.cssSelector("div.row ul"))
      // extract each curriculum link
      .findChildrenBy(By.cssSelector("li > div.card"))
      // build an Area of Study link View Object for each of these divs
      .stream()
      .map(RitAreaOfStudyLink::new)
      .toList();
  }

  public List<RatingInfoView> getRatingViews() {
    // the Ratings panel is in the fifth <div> of the main content
    // WARN: this is fragile code
    return mainContentPanel.findChildBy(By.xpath("div[5]"))
      // the statistics columns are organized in a layout div
      .findChildBy(By.cssSelector("div.row"))
      // extract each statistics columns
      .findChildrenBy(By.cssSelector("div.card"))
      .stream()
      // build a Rating Info view object for each of these divs
      .map(RatingInfoView::new)
      .toList();
  }
}
