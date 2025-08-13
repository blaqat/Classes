package edu.rit.swen253.test.navigation;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

public class AccessHelpSupportTest {

    private WebDriver driver;

    @BeforeEach
    public void setup() {
        driver = new ChromeDriver();
        driver.get("https://tigercenter.rit.edu/tigerCenterApp/login_shib/tc/homepage");
    }

    @Test
    public void testAccessHelpSupport() {
        // Click Help or Support link
        WebElement helpLink = driver.findElement(By.linkText("Help")); // or "Support"
        helpLink.click();

        // Verify URL or page title for Help/Support page
        Assertions.assertTrue(driver.getCurrentUrl().contains("help") || driver.getTitle().contains("Help"));
    }

    @AfterEach
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
