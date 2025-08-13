package edu.rit.swen253.page.tiger.hours;

import edu.rit.swen253.utils.DomElement;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

/**
 * View object class for the Student Affairs section of Hours & Locations.
 * Handles interactions with student affairs office elements.
 *
 * @author <a href='mailto:ajg7654@rit.edu'>Aiden Green</a>
 */
public class StudentAffairsView {

	private final DomElement containerElement;
	private static final By STUDENT_AFFAIRS_CONTAINER = By.tagName("student-affairs");
	private static final By OFFICE_ELEMENTS = By.cssSelector(
			"div > div > div.col-xs-12.col-sm-8.col-md-9.col-lg-9.student-affairsTabVerticalLine");

	public StudentAffairsView() {
		this.containerElement = findStudentAffairsContainer();
	}

	private DomElement findStudentAffairsContainer() {
		try {
			return DomElement.findBy(STUDENT_AFFAIRS_CONTAINER);
		} catch (TimeoutException e) {
			fail(String.format("Student Affairs view not found: %s", e.getMessage()));
			return null;
		}
	}

	/**
	 * Get all student affairs office elements displayed.
	 * 
	 * @return List of DomElement objects representing student affairs offices
	 */
	public List<DomElement> getAllStudentAffairsOfficeElements() {
		return containerElement.findChildrenBy(OFFICE_ELEMENTS);
	}

	/**
	 * Get the name of a student affairs office from its DOM element.
	 * 
	 * @param officeElement the student affairs office DOM element
	 * @return the name of the office
	 */
	public String getOfficeName(DomElement officeElement) {
		DomElement nameElement = officeElement.findChildBy(By.className("student-affairsTabEateryName"));
		return nameElement.getAttribute("innerText");
	}

	/**
	 * Navigate to the RIT Maps page for a specific office.
	 * 
	 * @param officeElement the office element containing the location icon
	 */
	public void navigateToMaps(DomElement officeElement) {
		officeElement.findChildBy(By.cssSelector("img.student-affairsTabLocationOrangeIcon")).click();
	}
}
