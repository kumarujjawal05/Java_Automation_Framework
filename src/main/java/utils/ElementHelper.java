package utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class ElementHelper {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public ElementHelper(WebDriver driver, long timeoutInSeconds){
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
    }

    public WebElement waitForVisibilityofElement(By locator){
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement waitForElementClickable(By locator){
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Checks if an element is visible on the page.
     * Uses a very short wait to quickly check for presence, making it efficient for assertions.
     * @param locator The element locator.
     * @return true if the element is visible, false otherwise.
     */
    public boolean isElementVisible(By locator) {
        try {
            // Use a short, 1-second wait to quickly check for visibility
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(1));
            shortWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
