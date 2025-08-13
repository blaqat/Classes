package edu.rit.swen253.test.wikipedia;

import edu.rit.swen253.page.SimplePage;
import edu.rit.swen253.page.wikipedia.WikiHomePage;
import edu.rit.swen253.page.wikipedia.WikiSearchResult;
import edu.rit.swen253.page.wikipedia.WikiSearchResultPage;
import edu.rit.swen253.test.AbstractWebTest;

import org.junit.jupiter.api.*;

import java.util.List;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * A simple test searches Wikipedia for "page object model" and goes to the
 * first result.
 * 
 * You need to create a single test that performs two actions:
 * 1. Navigate to the Home page and perform a search for “page object model”
 * 1a. Find the search field
 * 1b. Enter the search phrase and submit the search
 * 1c. Using a logger in the Test class display the title and URL for each
 * search result
 * 2. Click the first search result and validate that the browser navigates to
 * the expected page
 *
 * @author <a href='mailto:ajg7654@rit.edu'>Aiden Green</a>
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class WikiSearchTest extends AbstractWebTest {

  private WikiHomePage homePage;
  private WikiSearchResultPage searchResultsPage;
  private List<WikiSearchResult> searchResults;

  //
  // Test sequence
  //

  @Test
  @Order(1)
  @DisplayName("First, navigate to the Wikipedia English Home page.")
  void navigateToHomePage() {
    // NOTE: Using the english homepage as the starting point for this instead of
    // the root site
    homePage = navigateToPage("https://en.wikipedia.org/wiki/Main_Page", WikiHomePage::new);
  }

  @Test
  @Order(2)
  @DisplayName("Second, find out how many 'Area of Study' links are visible.")
  void searchForPageObjectModel() {
    // guard condition
    Assumptions.assumeTrue(homePage != null,
        "Failed to navigate to the Wikipedia Home page.");

    // perform search and go to search results page
    homePage.searchFor("page object model");
    homePage.waitUntilGone();
    searchResultsPage = assertNewPage(WikiSearchResultPage::new);
  }

  @Test
  @Order(3)
  @DisplayName("Third, ensure on search results page and log results.")
  void logResults() {
    // guard condition
    Assumptions.assumeTrue(searchResultsPage != null,
        "Failed to navigate to the search results page.");

    // get search results
    searchResults = searchResultsPage.getResults();
    assertNotNull(searchResults, "Search results should not be null");

    // Log the title and URL for each search result
    Logger logger = Logger.getLogger(WikiSearchTest.class.getName());
    searchResults.forEach(result -> logger.info("Title: " + result.getTitle() +
        ", URL: " + result.getURL()));
  }

  @Test
  @Order(4)
  @DisplayName("Third, navigate to the first search result.")
  void navigateToFirstResult() {
    // guard condition
    Assumptions.assumeTrue(searchResults != null && !searchResults.isEmpty(),
        "No search results found");

    // Get the first search result
    WikiSearchResult firstResult = searchResults.get(0);

    // Click the link to navigate to the page
    firstResult.navigateToArticle();
    searchResultsPage.waitUntilGone();
    SimplePage resultPage = assertNewPage(SimplePage::new);

    // validate page content
    assertAll("assert result is correct page",
        () -> assertEquals("https://en.wikipedia.org/wiki/Object_model", resultPage.getURL()),
        () -> assertEquals("Object model - Wikipedia", resultPage.getTitle()));
  }
}
