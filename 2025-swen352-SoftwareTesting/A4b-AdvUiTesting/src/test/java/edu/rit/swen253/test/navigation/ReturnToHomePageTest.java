package edu.rit.swen253.test.navigation;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

public class ReturnToHomePageTest {

    private WebDriver driver;

    @BeforeEach
    public void setup() {
        driver = new ChromeDriver();
        driver.get("https://tigercenter.rit.edu/tigerCenterApp/login_shib/tc/gpaCalculator");
    }

    @Test
    public void testReturnToHomePage() {
        // Click Home button or logo
        WebElement homeButton = driver.findElement(By.id("homeButton")); // or By.cssSelector or By.linkText depending on UI
        homeButton.click();

        // Verify redirected to homepage URL
        Assertions.assertEquals("https://tigercenter.rit.edu/tigerCenterApp/login_shib/tc/homepage", driver.getCurrentUrl());
    }

    @AfterEach
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
