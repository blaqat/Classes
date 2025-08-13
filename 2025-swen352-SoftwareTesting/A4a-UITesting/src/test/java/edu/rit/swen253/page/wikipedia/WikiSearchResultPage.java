package edu.rit.swen253.page.wikipedia;

import edu.rit.swen253.page.AbstractPage;
import edu.rit.swen253.utils.DomElement;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * A Page Object for the search results page on Wikipedia.
 *
 * @author <a href='mailto:ajg7654@rit.edu'>Aiden Green</a>
 */
public class WikiSearchResultPage extends AbstractPage {

	private static final By SEARCH_RESULTS = By.cssSelector("div.mw-search-results-container > ul");
	private DomElement searchResultsContainer;

	public WikiSearchResultPage() {
		super();
		// ensure search results container is on the page
		try {
			searchResultsContainer = findOnPage(SEARCH_RESULTS);
		} catch (TimeoutException e) {
			fail("Search results container not found");
		}
	}

	/**
	 * Retrieves a list of search results from the search results container.
	 * 
	 * This method finds all list item elements within the search results container
	 * and converts them into WikiSearchResult objects.
	 * 
	 * @return a List of WikiSearchResult objects representing the search results
	 *         found on the page. Returns an empty list if no results are found.
	 */
	public List<WikiSearchResult> getResults() {
		// gets all search results on the first page
		List<DomElement> resultItems = searchResultsContainer.findChildrenBy(By.tagName("li"));

		// converts the search result dom elements into WikiSearchResult
		return resultItems.stream()
				.map(item -> new WikiSearchResult(item))
				.toList();
	}

}
