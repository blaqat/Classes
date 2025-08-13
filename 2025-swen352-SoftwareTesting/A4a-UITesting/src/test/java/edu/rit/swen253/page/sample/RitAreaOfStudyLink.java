package edu.rit.swen253.page.sample;

import edu.rit.swen253.utils.DomElement;
import org.openqa.selenium.By;

import java.util.List;

import static edu.rit.swen253.utils.HtmlUtils.ANCHOR_FINDER;


/**
 * A View Object that contains a link in the 'Area of Study' section of the Home page.
 *
 * <p>
 * Each such link contains two icons (represented by a FontAwesome span element) and a
 * div element with the name of the area of study.
 *
 * <p>
 * Example:
 * <pre>
 *   <span class="position-relative align-items-baseline d-flex icon-link-wrapper">
 *     <span class="fal fa-phone-laptop text-center" aria-hidden="true"></span>
 *     <span class="fal fa-code text-center mr-2 mr-md-1" aria-hidden="true"></span>
 *     <a href="https://www.rit.edu/study/computing-and-information-sciences" class="link-with-icon see-more">
 *       <div class="field field--name-name field--type-string field--label-hidden field__item">
 *         Computing and Information Sciences
 *       </div>
 *     </a>
 *   </span>
 * </pre>
 */
public class RitAreaOfStudyLink {

  private final DomElement viewContainer;
  private final DomElement link;
  private String firstIconName;
  private String secondIconName;

  /**
   * The View container is the DIV that holds the link: {@code <li><div> ...LINK... </div></li>}.
   */
  public RitAreaOfStudyLink(final DomElement viewContainer) {
    this.viewContainer = viewContainer;
    this.link = viewContainer.findChildBy(ANCHOR_FINDER);
  }

  /**
   * Navigate to this 'Area of Study' by clicking the link.
   */
  public void clickLink() {
    link.click();
  }

  /**
   * Extract the FontAwesome icon name from the first span.
   * <p>This method removes the 'fa-' prefix.</p>
   */
  public String getFirstIconName() {
    if (firstIconName == null) {
      firstIconName = extractFontName(By.xpath("span[1]"));
    }
    return firstIconName;
  }

  /**
   * Extract the FontAwesome icon name from the second span.
   * <p>This method removes the 'fa-' prefix.</p>
   */
  public String getSecondIconName() {
    if (secondIconName == null) {
      secondIconName = extractFontName(By.xpath("span[2]"));
    }
    return secondIconName;
  }

  /**
   * Extract the title of the 'Area of Study' link.
   */
  public String getTitle() {
    return link.findChildBy(By.cssSelector("div.field--name-name")).getText();
  }

  //
  // Private
  //

  private String extractFontName(final By fontSpanFinder) {
    final DomElement firstFontAwesomeSpan = viewContainer
      .findChildBy(By.cssSelector("span.icon-link-wrapper"))
      .findChildBy(fontSpanFinder);
    final List<String> spanClasses = firstFontAwesomeSpan.getClasses();
    return spanClasses.stream()
      // find the first class that matches fa-ICONNAME
      .filter(className -> className.startsWith("fa-"))
      .findFirst().get()
      // remove the fa- prefix
      .substring(3);
  }
}
