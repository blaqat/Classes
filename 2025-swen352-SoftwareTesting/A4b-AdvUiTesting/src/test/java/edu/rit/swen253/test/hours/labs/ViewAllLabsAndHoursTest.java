package edu.rit.swen253.test.hours.labs;

import edu.rit.swen253.page.tiger.TigerCenterHomePage;
import edu.rit.swen253.page.tiger.hours.ComputerLabsView;
import edu.rit.swen253.page.tiger.hours.HoursAndLocationsPage;
import edu.rit.swen253.test.AbstractWebTest;
import edu.rit.swen253.utils.DomElement;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Scenario #1: View all labs and their hours.
 * 
 * User Story: As a public user I want to view lab hours and locations
 * so that I can find available computer labs.
 *
 * @author <a href='mailto:ajg7654@rit.edu'>Aiden Green</a>
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ViewAllLabsAndHoursTest extends AbstractWebTest {

	private TigerCenterHomePage homePage;
	private HoursAndLocationsPage hoursPage;
	private ComputerLabsView labsView;

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
	@DisplayName("Second, navigate to Computer Labs tab.")
	void navigateToComputerLabs() {
		labsView = hoursPage.navigateToComputerLabs();
		assertNotNull(labsView);
	}

	@Test
	@Order(3)
	@DisplayName("Third, verify page shows list of all labs and hours.")
	void verifyAllLabsDisplayed() {
		List<DomElement> allLabElements = labsView.getAllComputerLabElements();
		assertFalse(allLabElements.isEmpty(), "Should display computer labs");
		var logger = Logger.getLogger(ViewAllLabsAndHoursTest.class.getName());

		// Verify each lab has required information
		for (DomElement labElement : allLabElements) {
			String labName = labsView.getLabName(labElement);
			logger.info("Lab Name: " + labName);

			assertNotNull(labName, "Lab should have a name");
			assertFalse(labName.trim().isEmpty(), "Lab name should not be empty");
		}
	}
}
