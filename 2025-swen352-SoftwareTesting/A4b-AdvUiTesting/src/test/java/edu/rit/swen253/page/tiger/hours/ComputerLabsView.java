package edu.rit.swen253.page.tiger.hours;

import edu.rit.swen253.utils.DomElement;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.HashMap;
import java.util.List;

/**
 * View object class for the Computer Labs section of Hours & Locations.
 * Handles interactions with computer lab elements.
 *
 * @author <a href='mailto:ajg7654@rit.edu'>Aiden Green</a>
 */
public class ComputerLabsView {
	public static enum OperatingSystem {
		MAC, WINDOWS, PRINTER
	}

	private static HashMap<OperatingSystem, String> OSToIDMap = new HashMap<>() {
		{
			put(OperatingSystem.MAC, "mat-checkbox-5");
			put(OperatingSystem.WINDOWS, "mat-checkbox-6");
			put(OperatingSystem.PRINTER, "mat-checkbox-7");
		}
	};

	private static HashMap<OperatingSystem, String> OSToImageMap = new HashMap<>() {
		{
			put(OperatingSystem.MAC, "/assets/svgLib/apple_orange.svg");
			put(OperatingSystem.WINDOWS, "/assets/svgLib/windows_orange.svg");
			put(OperatingSystem.PRINTER, "/assets/svgLib/printer_orange.svg");
		}
	};

	private final DomElement containerElement;
	private static final By LABS_CONTAINER = By.tagName("labs");
	private static final By LAB_ELEMENTS = By.cssSelector(
			"div > div > div.col-xs-12.col-sm-9.col-md-9.col-lg-9.labsTabVerticalLine > div.ng-star-inserted");
	private static final By LAB_SOFTWARE_TAB = By.cssSelector(
			"mat-tab-group > mat-tab-header > div.mat-tab-label-container > div > div >  div:nth-child(2)");

	public ComputerLabsView() {
		this.containerElement = findLabsContainer();
	}

	/**
	 * Attempts to locate the DOM element representing the labs container in the
	 * Computer Labs view.
	 * <p>
	 * If the element cannot be found within the expected time frame, this method
	 * fails the test
	 * with an appropriate error message and returns {@code null}.
	 * </p>
	 *
	 * @return the {@link DomElement} representing the labs container, or
	 *         {@code null} if not found
	 */
	private DomElement findLabsContainer() {
		try {
			return DomElement.findBy(LABS_CONTAINER);
		} catch (TimeoutException e) {
			fail(String.format("Computer Labs view not found: %s", e.getMessage()));
			return null;
		}
	}

	/**
	 * Get all computer lab elements displayed.
	 * 
	 * @return List of DomElement objects representing computer labs
	 */
	public List<DomElement> getAllComputerLabElements() {
		return containerElement.findChildrenBy(LAB_ELEMENTS);
	}

	/**
	 * Get the name of a computer lab from its DOM element.
	 * 
	 * @param labElement the computer lab DOM element
	 * @return the name of the lab
	 */
	public String getLabName(DomElement labElement) {
		DomElement nameElement = labElement.findChildBy(By.className("labsTabEateryName"));
		return nameElement.getAttribute("innerText");
	}

	/**
	 * Click on a specific computer lab to expand its details.
	 * 
	 * @param labName the name of the lab to click
	 */
	public void clickLab(DomElement labElement) {
		labElement.click();
	}

	/**
	 * Filter labs by operating system.
	 * 
	 * @param osType the operating system type ("Mac", "Windows", "Linux")
	 */
	public void filterByOperatingSystem(OperatingSystem osType) {
		String osId = OSToIDMap.get(osType);
		DomElement osFilterCheckbox = containerElement.findChildBy(By.id(osId));
		osFilterCheckbox.click();
	}

	/**
	 * Get filtered computer lab elements after applying filters.
	 * 
	 * @return List of filtered DomElement objects
	 */
	public List<DomElement> getFilteredComputerLabElements() {
		// Return currently visible lab elements
		return containerElement.findChildrenBy(LAB_ELEMENTS);
	}

	/**
	 * Search for software in the current lab's software list.
	 * 
	 * @param softwareName the name of the software to search for
	 */
	public List<String> searchSoftware(DomElement labElement, String softwareName) {
		// Click Software Tab
		DomElement softwareTab = labElement.findChildBy(LAB_SOFTWARE_TAB);
		softwareTab.click();

		// Get Search Input for SOftware Tab
		DomElement softwareSearchInput = labElement.findChildBy(By.cssSelector("input.mat-input-element"));
		softwareSearchInput.clear();
		softwareSearchInput.sendKeys(softwareName);

		// Type software name into the search input
		List<DomElement> softwareElements = labElement.findChildrenBy(By.cssSelector("p.labsTabSoftwareText"));

		// return list of software names displayed
		return softwareElements.stream()
				.map(softwareElement -> softwareElement.getAttribute("innerText"))
				.toList();
	}

	/**
	 * Check if a lab has a specific operating system by examining its DOM element.
	 * 
	 * @param labElement the lab DOM element
	 * @param osType     the operating system to check for
	 * @return true if the lab has that OS
	 */
	public boolean labHasOperatingSystem(DomElement labElement, OperatingSystem osType) {
		String osImageSrc = OSToImageMap.get(osType);

		List<DomElement> softwareIcons = labElement
				.findChildrenBy(By.cssSelector("img.labsTabSoftwareIcons"));

		if (softwareIcons.isEmpty()) {
			return false;
		}

		// check if any of the toggled software icons match the osType image
		return softwareIcons.stream()
				.anyMatch(icon -> {
					String src = icon.getAttribute("src");
					return src != null && src.contains(osImageSrc);
				});
	}
}
