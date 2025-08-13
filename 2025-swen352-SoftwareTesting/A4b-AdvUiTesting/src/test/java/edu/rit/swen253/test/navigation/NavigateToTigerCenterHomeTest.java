package edu.rit.swen253.test.navigation;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

public class NavigateToTigerCenterHomeTest {

    private WebDriver driver;

    @BeforeEach
    public void setup() {
        // Setup ChromeDriver (ensure chromedriver is in your PATH or specify location)
        driver = new ChromeDriver();
    }

    @Test
    public void testNavigateToTigerCenterHomepage() {
        // Open RIT SIS webpage with TigerCenter link
        driver.get("https://sis.rit.edu/");

        // Find and click the TigerCenter link
        WebElement tigerCenterLink = driver.findElement(By.linkText("TigerCenter"));
        tigerCenterLink.click();

        // Switch to new tab
        for (String handle : driver.getWindowHandles()) {
            driver.switchTo().window(handle);
        }

        // Assert URL is correct
        Assertions.assertEquals("https://tigercenter.rit.edu/tigerCenterApp/login_shib/tc/homepage", driver.getCurrentUrl());
    }

    @AfterEach
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
