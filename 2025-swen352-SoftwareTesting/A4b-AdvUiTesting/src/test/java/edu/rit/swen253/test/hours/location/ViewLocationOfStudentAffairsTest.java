package edu.rit.swen253.test.hours.location;

import edu.rit.swen253.page.SimplePage;
import edu.rit.swen253.page.tiger.TigerCenterHomePage;
import edu.rit.swen253.page.tiger.hours.HoursAndLocationsPage;
import edu.rit.swen253.page.tiger.hours.StudentAffairsView;
import edu.rit.swen253.test.AbstractWebTest;
import edu.rit.swen253.utils.DomElement;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.logging.Logger;
import static edu.rit.swen253.utils.BrowserType.FIREFOX;
import static edu.rit.swen253.utils.TimingUtils.sleep;
import static edu.rit.swen253.utils.BrowserType.onBrowser;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Scenario #3: View location detail for Student Affair offices.
 * 
 * User Story: As a public user I want to see location for any service so I can
 * find it on campus
 *
 * @author <a href='mailto:ajg7654@rit.edu'>Aiden Green</a>
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ViewLocationOfStudentAffairsTest extends AbstractWebTest {

	private TigerCenterHomePage homePage;
	private HoursAndLocationsPage hoursPage;
	private StudentAffairsView studentAffairsView;
	private static final String CASE_MANAGEMENT_OFFICE_NAME = "Case Management";
	private static final String CASE_MANAGEMENT_OFFICE_URL = "https://maps.rit.edu/index.html?mdo_id=1890";

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
	@DisplayName("Second, navigate to Student Affairs tab.")
	void navigateToStudentAffairs() {
		studentAffairsView = hoursPage.navigateToStudentAffairs();
		assertNotNull(studentAffairsView);
	}

	@Test
	@Order(3)
	@DisplayName("Third, verify page shows list of student affair office locations.")
	void verifyStudentAffairOfficesDisplayed() {
		List<DomElement> officeElements = studentAffairsView.getAllStudentAffairsOfficeElements();
		assertFalse(officeElements.isEmpty(), "Should display student affair offices");
		var logger = Logger.getLogger(ViewLocationOfStudentAffairsTest.class.getName());

		for (DomElement officeElement : officeElements) {
			String officeName = studentAffairsView.getOfficeName(officeElement);
			logger.info("Office Name: " + officeName);

			assertNotNull(officeName, "Office should have a name");
			assertFalse(officeName.trim().isEmpty(), "Office name should not be empty");
		}
	}

	@Test
	@Order(4)
	@DisplayName("Finally go to maps")
	void clickLocationIconAndVerifyMapsUrl() {
		List<DomElement> officeElements = studentAffairsView.getAllStudentAffairsOfficeElements();
		assertFalse(officeElements.isEmpty(), "Should have at least one office to test location");

		// get case manage office
		DomElement caseManagementOffice = officeElements.stream()
				.filter(office -> CASE_MANAGEMENT_OFFICE_NAME.equals(studentAffairsView.getOfficeName(office)))
				.findFirst()
				.orElseThrow(() -> new AssertionError("Case Management office not found"));

		// click the location icon to navigate to maps
		studentAffairsView.navigateToMaps(caseManagementOffice);

		final SimplePage mapsPage = assertNewWindowAndSwitch(SimplePage::new);

		// There's a timing issue with Firefox (give it a second to render)
		if (onBrowser(FIREFOX)) {
			sleep(1);
		}

		assertEquals(CASE_MANAGEMENT_OFFICE_URL, mapsPage.getURL());
	}

}
