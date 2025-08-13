package edu.rit.swen253.page.tiger;

import edu.rit.swen253.page.AbstractAngularPage;
import edu.rit.swen253.utils.DomElement;
import edu.rit.swen253.utils.HtmlUtils;
import org.openqa.selenium.By;

import java.util.List;

/**
 * The Home page of the TigerCenter Angular web application.
 * <p>
 * We have provided for you a skeleton version of this Page Object class.
 * You may augment it as you see fit for this activity.
 * </p>
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
public class TigerCenterHomePage extends AbstractAngularPage {

  public TigerCenterHomePage() {
    super("landing-page");
  }

  /**
   * Select the <em>Maps at RIT</em> feature.
   * <p>
   * This action results in a new window/tab that display the map.rit.edu web
   * page.
   */
  public void selectMapsAtRIT() {
    findButtonLink(NavButton.MAPS_AT_RIT).click();
  }

  /**
   * Select the <em>Class Search</em> feature.
   * <p>
   * This action navigates to the Class Search page of the TigerCenter app.
   */
  public void selectClassSearch() {
    findButtonLink(NavButton.CLASS_SEARCH).click();
  }

  /**
   * Select the <em>Hours & Locations</em> feature.
   * <p>
   * This action navigates to the Hours & Locations page of the TigerCenter app.
   */
  public void selectHoursAndLocations() {
    findButtonLink(NavButton.HOURS_AND_LOCATIONS).click();
  }

  //
  // Private
  //

  private enum NavButton {
    CLASS_SEARCH("Class Search"),
    GPA_CALCULATOR("GPA Calculator"),
    HOURS_AND_LOCATIONS("Hours & Locations"),
    MAPS_AT_RIT("Maps at RIT");

    private final String buttonText;

    NavButton(final String buttonText) {
      this.buttonText = buttonText;
    }
  }

  private DomElement findButtonLink(final NavButton navButton) {
    final List<DomElement> allAnchors = angularView.findChildrenBy(HtmlUtils.ANCHOR_FINDER);
    final String buttonFinderXPath = String.format("./div[text() = '%s']", navButton.buttonText);
    final By buttonFinder = By.xpath(buttonFinderXPath);
    return allAnchors.stream()
        .filter(anchor -> anchor.hasChild(buttonFinder))
        .findFirst()
        .orElseThrow(() -> new RuntimeException("Could not find button link"));
  }
}
