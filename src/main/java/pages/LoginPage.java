package pages;

import locators.login_locators;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.ElementHelper;

public class LoginPage {

    private final ElementHelper helper;
    private final WebDriver driver;

    public LoginPage(WebDriver driver){
        this.helper = new ElementHelper(driver, 10);
        this.driver = driver;
    }

    public void enterUsername(String username) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        helper.waitForElementClickable(login_locators.enter_username).sendKeys(username);
    }

    public void enterPassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        helper.waitForElementClickable(login_locators.enter_password).sendKeys(password);
    }

    public void clickLoginButton() {
        helper.waitForElementClickable(login_locators.click_login_button).click();
    }

    public void clickForgotPasswordLink() {
        helper.waitForElementClickable(login_locators.click_forgot_link).click();
    }

    public boolean isLoginSuccessful() {
        return helper.isElementVisible(login_locators.account_overview_title);
    }

    public boolean isLoginFailed() {
        return helper.isElementVisible(login_locators.login_error_message);
    }

    public String getErrorMessage() {
        return helper.waitForVisibilityofElement(login_locators.login_error_message).getText();
    }
}
