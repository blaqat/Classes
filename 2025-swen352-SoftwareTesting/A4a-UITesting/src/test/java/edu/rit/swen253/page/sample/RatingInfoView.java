package edu.rit.swen253.page.sample;

import edu.rit.swen253.utils.BrowserType;
import edu.rit.swen253.utils.DomElement;
import edu.rit.swen253.utils.SeleniumUtils;
import org.openqa.selenium.By;


/**
 * A View Object that contains rating information.
 *
 * <p>
 * Example:
 * <pre>
 *   <div class="card-text ">
 * 			<div class="rankings-badge-text">
 *        <p class="rankings-badge-text--number">
 *          #6
 *        </p>
 *        <p class="rankings-badge-text--title">
 *          <strong>Top School for Co-op or Internship Programs</strong>
 *        </p>
 *        <p class="rankings-badge-text--description">...</p>
 *     </div>
 *   </div>
 * </pre>
 */
public class RatingInfoView {

  private final DomElement viewContainer;

  public RatingInfoView(final DomElement viewContainer) {
    this.viewContainer = viewContainer;
  }

  public String getRating() {
    scrollIntoView();
    return viewContainer.findChildBy(By.cssSelector("p.rankings-badge-text--number")).getText();
  }

  public String getTitle() {
    scrollIntoView();
    return viewContainer.findChildBy(By.cssSelector("p.rankings-badge-text--title")).getText();
  }

  //
  // Private
  //

  private void scrollIntoView() {
    if (!scrolledAlready) {
      switch (BrowserType.discover()) {
        case CHROME, EDGE -> viewContainer.scrollIntoView();
        case FIREFOX, SAFARI -> SeleniumUtils.getInstance().makeAction().scrollByAmount(0, 2000).perform();
      }
      scrolledAlready = true;
    }
  }
  private static boolean scrolledAlready = false;
}
