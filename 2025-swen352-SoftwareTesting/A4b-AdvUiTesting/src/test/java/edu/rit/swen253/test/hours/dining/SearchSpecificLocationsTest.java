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
 * Test class for Scenario #3: Search for specific dining location.
 * 
 * User Story: As a public user I want to see location for any service
 * so I can find it on campus.
 *
 * @author <a href='mailto:ajg7654@rit.edu'>Aiden Green</a>
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SearchSpecificLocationsTest extends AbstractWebTest {

    private TigerCenterHomePage homePage;
    private HoursAndLocationsPage hoursPage;
    private DiningServicesView diningView;
    private String searchTerm = "cafe";

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
    @DisplayName("Second, navigate to Dining Services tab.")
    void navigateToDiningServicesTab() {
        diningView = hoursPage.navigateToDiningServices();
        assertNotNull(diningView);
    }

    @Test
    @Order(3)
    @DisplayName("Third, type dining location into search bar.")
    void typeInSearchBar() {
        // Use a partial search term to test filtering functionality
        diningView.searchLocation(searchTerm);
    }

    @Test
    @Order(4)
    @DisplayName("Fourth, verify page shows filtered list matching search.")
    void verifyFilteredResults() {
        List<DomElement> filteredLocationElements = diningView.getAllDiningLocationElements();
        assertFalse(filteredLocationElements.isEmpty(), "Should display filtered dining locations");
        var logger = Logger.getLogger(SearchSpecificLocationsTest.class.getName());

        // Verify that all displayed locations match the search term
        for (DomElement locationElement : filteredLocationElements) {
            String locationName = diningView.getLocationName(locationElement);
            if (!locationName.toLowerCase().contains(searchTerm.toLowerCase())) {
                fail("Filtered results should only contain locations matching search term");
            } else {
                logger.info("Filtered Location: " + locationName);
            }
        }
    }

    @Test
    @Order(5)
    @DisplayName("Finally, check that filtered locations have hours information.")
    void checkFilteredLocationsHours() {
        List<DomElement> filteredLocationElements = diningView.getAllDiningLocationElements();

        // Verify that dining locations are displayed
        assertFalse(filteredLocationElements.isEmpty(), "Should display at least one dining location");
        var logger = Logger.getLogger(SearchSpecificLocationsTest.class.getName());

        for (DomElement locationElement : filteredLocationElements) {
            String locationName = diningView.getLocationName(locationElement);
            String locationHours = diningView.getLocationHours(locationElement);
            assertNotNull(locationHours, "Location should have hours information");
            logger.info("Location: " + locationName + ", Hours: " + locationHours);
        }
    }
}