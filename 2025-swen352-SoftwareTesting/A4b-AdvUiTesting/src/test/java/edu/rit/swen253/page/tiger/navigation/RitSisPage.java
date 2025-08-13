package edu.rit.swen253.page.tiger.navigation;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class RitSisPage {
    private WebDriver driver;

    private By tigerCenterLink = By.linkText("Tiger Center"); // adjust locator as needed

    public RitSisPage(WebDriver driver) {
        this.driver = driver;
    }

    /** 
     * Clicks the Tiger Center link, which opens a new tab.
     */
    public void clickTigerCenterLink() {
        WebElement link = driver.findElement(tigerCenterLink);
        link.click();
    }
}
