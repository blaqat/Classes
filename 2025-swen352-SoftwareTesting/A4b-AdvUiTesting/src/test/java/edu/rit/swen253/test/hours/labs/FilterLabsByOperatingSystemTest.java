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
 * Test class for Scenario #2: Find labs that have Windows computers.
 * 
 * User Story: As a public user I want to view lab hours and locations
 * so that I can find available computer labs.
 *
 * @author <a href='mailto:ajg7654@rit.edu'>Aiden Green</a>
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FilterLabsByOperatingSystemTest extends AbstractWebTest {

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
	@DisplayName("Third, verify page shows list of all labs.")
	void verifyAllLabsDisplayed() {
		List<DomElement> allLabElements = labsView.getAllComputerLabElements();
		assertFalse(allLabElements.isEmpty(), "Should display computer labs");
	}

	@Test
	@Order(4)
	@DisplayName("Third, Select Windows Filter")
	void selectWindowsFilter() {
		labsView.filterByOperatingSystem(ComputerLabsView.OperatingSystem.WINDOWS);
	}

	@Test
	@Order(5)
	@DisplayName("Finally, Select windows filter and verified labs displayed have Windows computers.")
	void verifyWindowsLabsDisplayed() {
		List<DomElement> windowsLabElements = labsView.getFilteredComputerLabElements();
		assertFalse(windowsLabElements.isEmpty(), "Should show labs with Windows computers");
		var logger = Logger.getLogger(FilterLabsByOperatingSystemTest.class.getName());

		// Check No lab is missing Windows computers
		for (DomElement labElement : windowsLabElements) {
			String labName = labsView.getLabName(labElement);

			boolean hasWindows = labsView.labHasOperatingSystem(labElement, ComputerLabsView.OperatingSystem.WINDOWS);
			assertTrue(hasWindows, "All listed labs should have Windows computers, but " + labName + " does not.");

			logger.info(labName + " has Windows computers.");
		}
	}
}
