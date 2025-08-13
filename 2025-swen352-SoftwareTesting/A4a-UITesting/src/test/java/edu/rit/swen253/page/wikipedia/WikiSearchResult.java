
package edu.rit.swen253.page.wikipedia;

import edu.rit.swen253.utils.DomElement;
import org.openqa.selenium.By;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * A wrapper for the DOM element representing a search result on Wikipedia.
 *
 * @author <a href='mailto:ajg765@rit.edu'>Aiden Green</a>
 */
public class WikiSearchResult {

	// The header text of a result containing its hyperlinked title
	private static final By TITLE_ELEMENT = By.cssSelector("div.mw-search-result-heading > a");

	private DomElement resultTitleElement;
	private String title;
	private String url;

	public WikiSearchResult(DomElement result) {
		super();
		// ensures the result has the title text/hyperlink
		try {
			resultTitleElement = result.findChildBy(TITLE_ELEMENT);
			this.title = resultTitleElement.getText();
			this.url = resultTitleElement.getAttribute("href");
		} catch (RuntimeException e) {
			fail("Search result title element not found");
		}
	}

	public String getTitle() {
		return title;
	}

	public String getURL() {
		return url;
	}

	/**
	 * Navigates to the Wikipedia article by clicking on the title hyperlink.
	 */
	public void navigateToArticle() {
		resultTitleElement.click();
	}
}
