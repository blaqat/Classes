package edu.rit.swen253.test.sample;

import edu.rit.swen253.page.sample.RatingInfoView;
import edu.rit.swen253.page.sample.RitHomePage;
import edu.rit.swen253.test.AbstractWebTest;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * A simple test that explores RIT's ratings on their home page.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RitRatingsTest extends AbstractWebTest {

  private RitHomePage homePage;
  private RatingInfoView coopRatingInfo;

  //
  // Test sequence
  //

  @Test
  @Order(1)
  @DisplayName("First, navigate to the RIT Home page.")
  void navigateToHomePage() {
    homePage = navigateToPage("https://rit.edu", RitHomePage::new);
  }

  @Test
  @Order(2)
  @DisplayName("Second, find out how many rating info views are visible.")
  void exploreRatings() {
    final List<RatingInfoView> ratingInfoViews = homePage.getRatingViews();
    assertEquals(3, ratingInfoViews.size(), "Number of views should be 3");
    // prepare for the next test
    coopRatingInfo = ratingInfoViews.get(2);  // co-op rank is the third item
  }

  @Test
  @Order(3)
  @DisplayName("Third, inspect the content of the first rating info panel.")
  void inspectCoopRatingInfo() {
    assertAll("group assertions"
      , () -> assertEquals("#6", coopRatingInfo.getRating())
      , () -> assertEquals("Top School for Co-op or Internship Programs", coopRatingInfo.getTitle())
    );
  }
}
