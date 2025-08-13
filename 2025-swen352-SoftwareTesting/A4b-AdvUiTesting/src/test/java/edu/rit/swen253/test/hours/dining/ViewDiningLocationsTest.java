package edu.rit.swen253.test.hours.dining;

import edu.rit.swen253.page.tiger.TigerCenterHomePage;
import edu.rit.swen253.page.tiger.hours.DiningServicesView;
import edu.rit.swen253.page.tiger.hours.HoursAndLocationsPage;
import edu.rit.swen253.test.AbstractWebTest;
import edu.rit.swen253.utils.DomElement;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Scenario #1: View all dining locations and their hours for the
 * day.
 * 
 * User Story: As a public user I want to view today's hours for all dining
 * locations so that I can decide where to eat.
 *
 * @author <a href='mailto:ajg7654@rit.edu'>Aiden Green</a>
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ViewDiningLocationsTest extends AbstractWebTest {

	private TigerCenterHomePage homePage;
	private HoursAndLocationsPage hoursPage;
	private DiningServicesView diningView;

	@Test
	@Order(1)
	@DisplayName("First, navigate to the Tiger Center Home page.")
	void navigateToHomePage() {
		homePage = navigateToPage("https://tigercenter.rit.edu", TigerCenterHomePage::new);
		assertNotNull(homePage);
	}

	@Test
	@Order(2)
	@DisplayName("Second, navigate to Hours & Locations page.")
	void navigateToHoursAndLocations() {
		homePage.selectHoursAndLocations();
		hoursPage = assertNewPage(HoursAndLocationsPage::new);
		assertNotNull(hoursPage);
	}

	@Test
	@Order(3)
	@DisplayName("Third, navigate to Dining Services section.")
	void navigateToDiningServices() {
		diningView = hoursPage.navigateToDiningServices();
		assertNotNull(diningView);
	}

	@Test
	@Order(4)
	@DisplayName("Finally, verify page displays list of all dining locations for today.")
	void verifyDiningLocationsDisplayed() {
		List<DomElement> diningLocationElements = diningView.getAllDiningLocationElements();

		// Verify that dining locations are displayed
		assertFalse(diningLocationElements.isEmpty(), "Should display at least one dining location");

		for (DomElement locationElement : diningLocationElements) {
			String locationName = diningView.getLocationName(locationElement);
			String locationHours = diningView.getLocationHours(locationElement);

			// Verify each location has required name and hours information
			assertNotNull(locationName, "Location should have a name");
			assertFalse(locationName.trim().isEmpty(), "Location name should not be empty");
			assertNotNull(locationHours, "Location should have hours information");
		}
	}
}
