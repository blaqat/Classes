package edu.rit.swen253.page;

/**
 * Functional interface to create a Page Object.
 *
 * @param <P> the type of Page Object
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
@FunctionalInterface
public interface PageFactory<P extends AbstractPage> {
  /**
   * Create a Page of a specific type.
   */
    P createPage();
}
