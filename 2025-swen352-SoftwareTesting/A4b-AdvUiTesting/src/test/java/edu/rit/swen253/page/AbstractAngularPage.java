package edu.rit.swen253.page;

import edu.rit.swen253.utils.DomElement;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;

import static org.junit.jupiter.api.Assertions.fail;


/**
 * The base class of an Angular webapp page.
 * <p>
 *   An Angular webapp is a type of Single-Page Application (SPA).  This means that once the
 *   initial page loads the {@code <body>} element is never replaced.  Instead, the Angular
 *   Router is used to replace some of the DOM structure as the application navigates from
 *   one page to another.  This is what is called the {@code angularView} in this class.
 *   The constructor takes a string argument that is the tag name of the Angular view for
 *   the page being represented by the subclass of this class.
 * </p>
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
public class AbstractAngularPage extends AbstractPage {

    protected DomElement angularView;

    protected AbstractAngularPage(final String angularViewName) {
        super();
        // validate basic page structure
        try {
            angularView = findOnPage(By.tagName(angularViewName));
        } catch (TimeoutException e) {
            fail(String.format("Angular view '%s' not found", angularViewName));
        }
    }

    @Override
    public void waitUntilGone() {
        angularView.waitUntilGone();
    }

    public void waitUntilAppIsGone() {
      super.waitUntilGone();
    }
}
