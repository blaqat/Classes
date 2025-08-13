package edu.rit.swen253.page.tiger.hours;

import edu.rit.swen253.page.AbstractAngularPage;
import edu.rit.swen253.utils.DomElement;
import org.openqa.selenium.By;

/**
 * The Hours & Locations page of the TigerCenter Angular web application.
 * This is the main page for accessing dining, computer labs, and student
 * affairs.
 *
 * @author <a href='mailto:ajg7654@rit.edu'>Aiden Green</a>
 */
public class HoursAndLocationsPage extends AbstractAngularPage {

	private static final By DINING_TAB = By.cssSelector("#mat-tab-label-0-0");
	private static final By LABS_TAB = By.cssSelector("#mat-tab-label-0-1");
	private static final By AFFAIRS_TAB = By.cssSelector("#mat-tab-label-0-2");

	public HoursAndLocationsPage() {
		super("hours-and-locations");
	}

	/**
	 * Navigate to the Dining Services tab.
	 * 
	 * @return DiningServicesView for the dining services section
	 */
	public DiningServicesView navigateToDiningServices() {
		DomElement diningTab = angularView.findChildBy(DINING_TAB);
		diningTab.click();
		return new DiningServicesView();
	}

	/**
	 * Navigate to the Computer Labs tab.
	 * 
	 * @return ComputerLabsView for the computer labs section
	 */
	public ComputerLabsView navigateToComputerLabs() {
		DomElement labsTab = angularView.findChildBy(LABS_TAB);
		labsTab.click();
		return new ComputerLabsView();
	}

	/**
	 * Navigate to the Student Affairs tab.
	 * 
	 * @return StudentAffairsView for the student affairs section
	 */
	public StudentAffairsView navigateToStudentAffairs() {
		DomElement affairsTab = angularView.findChildBy(AFFAIRS_TAB);
		affairsTab.click();
		return new StudentAffairsView();
	}
}
