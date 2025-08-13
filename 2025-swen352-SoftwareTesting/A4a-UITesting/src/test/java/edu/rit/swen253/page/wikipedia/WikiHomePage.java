package edu.rit.swen253.page.wikipedia;

import edu.rit.swen253.page.AbstractPage;
import edu.rit.swen253.utils.BrowserType;
import edu.rit.swen253.utils.DomElement;
import edu.rit.swen253.utils.ScreenMode;
import edu.rit.swen253.utils.SeleniumUtils;
import edu.rit.swen253.utils.TimingUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * A Page Object for the Wikipedia Main page.
 *
 * @author <a href='mailto:ajg7654@rit.edu'>Aiden Green</a>
 */
public class WikiHomePage extends AbstractPage {

  // The search bar in the header of all wikipedia pages
  private static final By SEARCH_BAR = By.cssSelector("#searchInput");
  private static final By SEARCH_BUTTON = By.cssSelector("#p-search > a");
  private DomElement searchBar;
  private DomElement searchButton;

  public WikiHomePage() {
    super();
    try {
      switch (ScreenMode.discover()) {
        case DESKTOP -> {
          // on desktop ensure search bar is present
          searchBar = findOnPage(SEARCH_BAR);
        }
        case PHONE -> {
          // on mobile, ensure the search button is present
          searchButton = findOnPage(SEARCH_BUTTON);
        }
      }
    } catch (TimeoutException e) {
      fail("Search bar not found");
    }
  }

  /**
   * Performs a search on the Wikipedia home page by entering the specified query
   * into the search bar,
   * then selects and clicks the item that sends to seperate results page
   * 
   * @param query the search term to enter into the search bar
   * @throws RuntimeException if can't go to the search results page
   */
  public void searchFor(String query) {
    switch (ScreenMode.discover()) {
      // on mobile, click search button and type query (the search bar is auto
      // selected when button is clicked)
      case PHONE -> {
        searchButton.click();
        SeleniumUtils.getInstance().makeAction().sendKeys(query).perform();
      }
      // on desktop, query is just typed into the search bar
      case DESKTOP -> {
        searchBar.sendKeys(query);
      }
    }

    // finds suggestions menu and get all suggestions
    DomElement suggestionsMenu = findOnPage(By.cssSelector("div.cdx-typeahead-search__menu > ul"));
    List<DomElement> suggestions = suggestionsMenu.findChildrenBy(By.tagName("li"));

    // finds the last li that doesn't have the `.cdx-menu-item-has-description`
    // class
    DomElement lastSuggestion = suggestions.stream()
        .filter(li -> !li.hasClass("cdx-menu-item-has-description"))
        .reduce((first, second) -> second)
        .orElseThrow(() -> new RuntimeException("No 'Search for pages' item found"));

    // Waits for suggestions to appear on safari since it clicks the suggestions
    // before finishing typing unlike other browsers for some reason
    if (BrowserType.discover() == BrowserType.SAFARI) {
      TimingUtils.sleep(1);
    }

    // click the search results page link
    DomElement searchLink = lastSuggestion.findChildBy(By.tagName("a"));
    searchLink.click();
  }
}
