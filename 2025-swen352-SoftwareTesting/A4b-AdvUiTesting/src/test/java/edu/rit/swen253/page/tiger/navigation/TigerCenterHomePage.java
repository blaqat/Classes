package edu.rit.swen253.page.tiger.navigation;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class TigerCenterHomePage {
    private WebDriver driver;

    public TigerCenterHomePage(WebDriver driver) {
        this.driver = driver;
    }

    // Add this method since tests call it
    public HoursAndLocationsPage selectHoursAndLocations() {
        // Click on the "Hours and Locations" link/button on the page
        driver.findElement(By.linkText("Hours and Locations")).click();
        // Return the page object for the next page
        return new HoursAndLocationsPage(driver);
    }
}
