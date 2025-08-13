package edu.rit.swen253.test.navigation;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

public class NavigateBetweenFeaturesTest {

    private WebDriver driver;

    @BeforeEach
    public void setup() {
        driver = new ChromeDriver();
        driver.get("https://tigercenter.rit.edu/tigerCenterApp/login_shib/tc/homepage");
    }

    @Test
    public void testNavigateBetweenFeatures() {
        // Click "Class Search" link
        WebElement classSearchLink = driver.findElement(By.linkText("Class Search"));
        classSearchLink.click();
        Assertions.assertTrue(driver.getCurrentUrl().contains("classSearch"));

        // Click "GPA Calculator" link
        WebElement gpaCalcLink = driver.findElement(By.linkText("GPA Calculator"));
        gpaCalcLink.click();
        Assertions.assertTrue(driver.getCurrentUrl().contains("gpaCalculator"));
    }

    @AfterEach
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
