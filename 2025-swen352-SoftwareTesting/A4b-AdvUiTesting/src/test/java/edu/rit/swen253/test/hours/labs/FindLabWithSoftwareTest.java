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
 * Test class for Scenario #3: Check which labs have a specific software.
 * 
 * User Story: As a public user I want to view lab hours and locations
 * so that I can find available computer labs.
 *
 * @author <a href='mailto:ajg7654@rit.edu'>Aiden Green</a>
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FindLabWithSoftwareTest extends AbstractWebTest {

	private TigerCenterHomePage homePage;
	private HoursAndLocationsPage hoursPage;
	private ComputerLabsView labsView;
	private final String targetSoftware = "Blender";

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
	@DisplayName("Third, verify page shows list of all labs.")
	void verifyLabsDisplayed() {
		List<DomElement> labElements = labsView.getAllComputerLabElements();
		assertFalse(labElements.isEmpty(), "Should display computer labs");

		// Verify labs are displayed
		for (DomElement labElement : labElements) {
			String labName = labsView.getLabName(labElement);

			assertNotNull(labName, "Lab should have a name");
			assertFalse(labName.trim().isEmpty(), "Lab name should not be empty");
		}
	}

	@Test
	@Order(4)
	@DisplayName("Finally, search for specific software in search bar.")
	void findLabsWithBlender() {
		List<DomElement> labElements = labsView.getAllComputerLabElements();
		assertFalse(labElements.isEmpty(), "Should display computer labs");

		boolean foundSoftware = false;

		for (DomElement labElement : labElements) {
			// Expand the lab
			labsView.clickLab(labElement);

			// Search for the target software
			List<String> results = labsView.searchSoftware(labElement,
					targetSoftware.toLowerCase().substring(0, Math.min(targetSoftware.length(), 4)));

			// Check if the software is found in the results
			if (results.contains(targetSoftware)) {
				var logger = Logger.getLogger(FindLabWithSoftwareTest.class.getName());
				logger.info("Found " + targetSoftware + " in lab: " + labsView.getLabName(labElement));
				foundSoftware = true;
			}
		}

		assertTrue(foundSoftware, "At least one lab should have the target software: " + targetSoftware);
	}

}
