package edu.rit.swen253.test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import edu.rit.swen253.utils.BrowserWindow;

import java.util.Set;
import java.util.function.Supplier;

public abstract class AbstractWebTest {
    protected WebDriver driver;

    @BeforeEach
    public void setUp() {
        // Set system property if needed, e.g.:
        // System.setProperty("webdriver.chrome.driver", "/path/to/chromedriver");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        // Add implicit wait or other config here if needed
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    /**
     * Navigate to the given URL and return a new instance of the page object.
     */
    public <T> T navigateToPage(String url, Supplier<T> pageSupplier) {
        driver.get(url);
        return pageSupplier.get();
    }

    /**
     * Assert that the current page matches the expected page and return the page object.
     * You can customize this method with actual assertions.
     */
    public <T> T assertNewPage(Supplier<T> pageSupplier) {
        // Example: assert the page title or URL here
        // Assertions.assertEquals(expectedTitle, driver.getTitle());
        return pageSupplier.get();
    }

    /**
     * Assert a new window opened and switch to it, returning the new page object.
     */
    public <T> T assertNewWindowAndSwitch(Supplier<T> pageSupplier) {
        String currentWindow = driver.getWindowHandle();
        Set<String> allWindows = driver.getWindowHandles();

        for (String window : allWindows) {
            if (!window.equals(currentWindow)) {
                driver.switchTo().window(window);
                return pageSupplier.get();
            }
        }
        throw new RuntimeException("No new window found");
    }

    /**
     * Return the current window handle.
     */
    public String getCurrentWindow() {
        return driver.getWindowHandle();
    }

    /**
     * Switch to the specified window handle.
     */
    public void switchToWindow(String windowHandle) {
        driver.switchTo().window(windowHandle);
    }

    

    public <T> BrowserWindow<T> getWindow(String windowHandle, T page) {
    driver.switchTo().window(windowHandle);
    return new BrowserWindow<>(driver, windowHandle, page);
}

}
