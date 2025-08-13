package edu.rit.swen253.test.hours.dining;

import edu.rit.swen253.page.tiger.TigerCenterHomePage;
import edu.rit.swen253.page.tiger.hours.DiningServicesView;
import edu.rit.swen253.page.tiger.hours.HoursAndLocationsPage;
import edu.rit.swen253.test.AbstractWebTest;
import edu.rit.swen253.utils.DomElement;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Scenario #2: Filter dining locations to see what's open
 * currently.
 * 
 * User Story: As a public user I want to view today's hours for all dining
 * locations so that I can decide where to eat.
 *
 * @author <a href='mailto:ajg7654@rit.edu'>Aiden Green</a>
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FilterOpenDiningLocationsTest extends AbstractWebTest {

	private TigerCenterHomePage homePage;
	private HoursAndLocationsPage hoursPage;
	private DiningServicesView diningView;

	@Test
	@Order(1)
	@DisplayName("First, navigate to Tiger Center and Hours & Locations page.")
	void navigateToHoursAndLocations() {
		homePage = navigateToPage("https://tigercenter.rit.edu", TigerCenterHomePage::new);
		assertNotNull(homePage);

		homePage.selectHoursAndLocations();
		hoursPage = assertNewPage(HoursAndLocationsPage::new);
		assertNotNull(hoursPage);
	}

	@Test
	@Order(2)
	@DisplayName("Second, navigate to Dining Services section.")
	void navigateToDiningServices() {
		diningView = hoursPage.navigateToDiningServices();
		assertNotNull(diningView);
	}

	@Test
	@Order(3)
	@DisplayName("Third, select 'Sort Open Now' filter.")
	void selectSortOpenNow() {
		diningView.sortByOpenNow();
	}

	@Test
	@Order(4)
	@DisplayName("Finally, verify page shows locations with open ones on top.")
	void verifyDiningLocationsDisplayed() {
		List<DomElement> diningLocationElements = diningView.getAllDiningLocationElements();

		// Verify that dining locations are displayed
		assertFalse(diningLocationElements.isEmpty(), "Should display at least one dining location");
		boolean foundClosed = false;

		// Verify that all open locations appear before any closed locations
		for (DomElement locationElement : diningLocationElements) {
			boolean locationOpen = diningView.isLocationOpen(locationElement);
			var logger = Logger.getLogger(FilterOpenDiningLocationsTest.class.getName());
			if (locationOpen) {
				logger.info("Open: " + diningView.getLocationName(locationElement));
			} else {
				logger.info("Closed: " + diningView.getLocationName(locationElement));
			}

			if (foundClosed && locationOpen) {
				fail("All open locations should appear before any closed locations");
			} else if (!locationOpen) {
				foundClosed = true;
			}
		}
	}
}
