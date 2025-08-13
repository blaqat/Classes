package edu.rit.swen253.page.tiger.hours;

import edu.rit.swen253.utils.DomElement;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

/**
 * View object class for the Dining Services section of Hours & Locations.
 * Handles interactions with dining location elements.
 *
 * @author <a href='mailto:ajg7654@rit.edu'>Aiden Green</a>
 */
public class DiningServicesView {

	private final DomElement containerElement;
	private static final By DINING_CONTAINER = By.tagName("dining");
	private static final By LOCATIONS = By.cssSelector(
			"div.container-fluid.greyBorder > div > div > div.col-xs-12.col-sm-9.col-md-9.col-lg-9.diningTabVerticalLine.diningTabLeftMarginMobile > div.ng-star-inserted");
	private static final By BUTTON_FILTERS = By.cssSelector(
			"div.container-fluid.greyBorder > div > div > div.col-xs-12.col-sm-9.col-md-9.col-lg-9.diningTabVerticalLine.diningTabLeftMarginMobile > div");

	public DiningServicesView() {
		this.containerElement = findDiningContainer();
	}

	/**
	 * Attempts to locate the dining container element in the DOM.
	 * <p>
	 * If the element is found, it is returned. If a {@link TimeoutException}
	 * occurs,
	 * the test fails with an appropriate error message and {@code null} is
	 * returned.
	 *
	 * @return the {@link DomElement} representing the dining container, or
	 *         {@code null} if not found
	 */
	private DomElement findDiningContainer() {
		try {
			return DomElement.findBy(DINING_CONTAINER);
		} catch (TimeoutException e) {
			fail(String.format("Dining Services view not found: %s", e.getMessage()));
			return null;
		}
	}

	/**
	 * Get all dining location elements displayed for the current day.
	 * 
	 * @return List of DomElement objects representing dining locations
	 */
	public List<DomElement> getAllDiningLocationElements() {
		return containerElement.findChildrenBy(LOCATIONS);
	}

	/**
	 * Get the name of a dining location from its DOM element.
	 * 
	 * @param locationElement the dining location DOM element
	 * @return the name of the location
	 */
	public String getLocationName(DomElement locationElement) {
		return locationElement.getText();
	}

	/**
	 * Get the hours of a dining location from its DOM element.
	 * The hours are only visible when the location is expanded.
	 * 
	 * @param locationElement the dining location DOM element
	 * @return the hours string, or empty string if not expanded
	 */
	public String getLocationHours(DomElement locationElement) {
		try {
			DomElement locationHourDiv = locationElement.findChildBy(By.cssSelector(
					"div.mat-tab-body-wrapper > mat-tab-body > div > div > div.ng-star-inserted"));
			return locationHourDiv.getAttribute("innerText");
		} catch (Exception e) {
			return "Hours not available";
		}
	}

	/**
	 * Check if a dining location is open from its icon
	 * 
	 * @param locationElement the dining location DOM element
	 * @return true if open, false otherwise
	 */
	public boolean isLocationOpen(DomElement locationElement) {
		try {
			DomElement locationIcon = locationElement.findChildBy(By.className("diningTabOpenIcon"));
			String iconSrc = locationIcon.getAttribute("src");
			if (iconSrc == null) {
				return false;
			}
			return iconSrc.contains("open.svg");
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Sort locations to show open ones first.
	 */
	public void sortByOpenNow() {
		DomElement buttons = containerElement.findChildBy(BUTTON_FILTERS);
		DomElement sortButton = buttons.findChildBy(By.cssSelector("a:nth-child(3)"));
		sortButton.click();
	}

	/**
	 * Search for dining locations by name.
	 * 
	 * @param searchTerm the term to search for
	 */
	public void searchLocation(String searchTerm) {
		DomElement searchInput = containerElement.findChildBy(By.cssSelector("#mat-input-0"));
		searchInput.clear();
		searchInput.sendKeys(searchTerm);
	}
}
