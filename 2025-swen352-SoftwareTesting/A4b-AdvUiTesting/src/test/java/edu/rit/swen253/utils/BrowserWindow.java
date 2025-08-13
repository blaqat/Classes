package edu.rit.swen253.utils;

import org.openqa.selenium.WebDriver;

public class BrowserWindow<T> {
    private final WebDriver driver;
    private final String windowHandle;
    private final T page;

    public BrowserWindow(WebDriver driver, String windowHandle, T page) {
        this.driver = driver;
        this.windowHandle = windowHandle;
        this.page = page;
    }

    public String getWindowHandle() {
        return windowHandle;
    }

    public T page() {
        return page;
    }

    public WebDriver getDriver() {
        return driver;
    }
}
