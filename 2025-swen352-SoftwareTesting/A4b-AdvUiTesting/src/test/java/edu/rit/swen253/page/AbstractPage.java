package edu.rit.swen253.page;

import edu.rit.swen253.utils.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.FluentWait;

import java.util.List;


/**
 * The base class of a web page.
 * <p>
 *   This class supports the Page Object testing pattern.
 *   @see <a href="https://martinfowler.com/bliki/PageObject.html">Page Object (Martin Fowler)</a>
 *   @see <a href="https://www.selenium.dev/documentation/test_practices/encouraged/page_object_models/">Page object models (Selenium)</a>
 * </p>
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
public abstract class AbstractPage {

  //
  // Attributes
  //

  private final DomElement pageBody;

  //
  // Constructor
  //

  protected AbstractPage() {
    this.pageBody = DomElement.findBy(HtmlUtils.BODY_FINDER);
  }

  //
  // Page content methods
  //

  /**
   * Query the page to see if the supplied {@linkplain By finder} finds the element.
   *
   * @param finder the {@link By} finder object
   * @return true if a DOM element for this finder was found; otherwise, false
   */
  public boolean hasDomElement(final By finder) {
    return pageBody.hasChild(finder);
  }

  /**
   * Find a specific, single {@linkplain DomElement DOM element} with the supplied {@linkplain By finder}.
   *
   * @param finder the {@link By} finder object
   * @return a single {@link DomElement} that matches this finder
   * @throws org.openqa.selenium.TimeoutException  if a match is not found
   */
  public DomElement findOnPage(By finder) {
    return pageBody.findChildBy(finder);
  }

  /**
   * Find all {@linkplain DomElement DOM elements} that match the supplied {@linkplain By finder}.
   *
   * @param finder the {@link By} finder object
   * @return a list of {@link DomElement}s that match this finder
   */
  public List<DomElement> findAllOnPage(By finder) {
    return pageBody.findChildrenBy(finder);
  }

  /**
   * Scroll the browser window down by a fixed amount.
   *
   * @param deltaY  how far to scroll down (Y-axis)
   */
  public void scrollDown(final int deltaY) {
    SeleniumUtils.getInstance().scrollDown(deltaY);
  }

  /**
   * Scroll down to the top of the given DOM element.
   *
   * @param element  the DOM element to expose
   */
  public void scrollToTopOfElement(final DomElement element) {
    scrollDown(element.getLocation().y);
  }

  /**
   * Scroll up or down to put the DOM element into view.
   *
   * @param element  the DOM element to expose
   */
  public void scrollIntoView(final DomElement element) {
    switch (BrowserType.discover()) {
      // Chromium-based Selenium drivers handle DOM element scrolling well-enough
      case CHROME, EDGE -> element.scrollIntoView();
      // bugs in these Selenium drivers require a more "manual" scrolling approach
      case FIREFOX, SAFARI -> scrollToTopOfElement(element);
    }
  }

  /**
   * Wait until the page has gone stale; meaning, the browser has navigated to a brand-new page.
   * <p>
   * If this succeeds, then this Page Object should be discarded and a new one created.  Any actions
   * on this page will result in Staleness exceptions.
   * </p>
   */
  public void waitUntilGone() {
    waitUntilGone(SeleniumUtils.getInstance().getLongWait());
  }

  /**
   * Wait until the page has gone stale; meaning, the browser has navigated to a brand-new page.
   * <p>
   * If this succeeds, then this Page Object should be discarded and a new one created.  Any actions
   * on this page will result in Staleness exceptions.
   * </p>
   * @param waitTime  a custom wait condition
   */
  public void waitUntilGone(FluentWait<WebDriver> waitTime) {
    pageBody.waitUntilGone(waitTime);
  }
}
