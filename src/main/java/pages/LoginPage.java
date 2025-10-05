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

    public void performLogin(String username, String password){
        WebElement usernameField = helper.waitForElementClickable(login_locators.enter_username);
        usernameField.sendKeys(username);
        WebElement passwordField = helper.waitForElementClickable(login_locators.enter_password);
        passwordField.sendKeys(password);
        WebElement loginButton = helper.waitForElementClickable(login_locators.click_login_button);
        loginButton.click();
    }

    // ✅ Test-case wrappers
    public String loginWithValidCredentials(){
        performLogin("validUser", "validPass");
        boolean dashboardVisible = driver.getCurrentUrl().contains("dashboard")
                || driver.getPageSource().contains("Welcome");
        return dashboardVisible ? "User is redirected to the dashboard" : "Login failed";
    }

    public String loginWithInvalidCredentials(){
        performLogin("wrongUser", "wrongPass");
        boolean errorVisible = driver.getPageSource().contains("Invalid username or password");
        return errorVisible ? "Error message is displayed" : "No error message displayed";
    }

    public String loginWithEmptyFields(){
        helper.waitForElementClickable(login_locators.click_login_button).click();
        boolean validationVisible = driver.getPageSource().toLowerCase().contains("required");
        return validationVisible
                ? "Validation messages displayed for mandatory fields"
                : "No validation shown";
    }

    public String verifyForgotPasswordLink(){
        helper.waitForElementClickable(login_locators.click_forgot_link).click();
        boolean redirected = driver.getCurrentUrl().contains("forgot-password");
        return redirected
                ? "User is redirected to Forgot Password page"
                : "Forgot Password navigation failed";
    }

    public String verifyRememberMeFunctionality(){
        WebElement usernameField = helper.waitForElementClickable(login_locators.enter_username);
        usernameField.sendKeys("validUser");
        WebElement passwordField = helper.waitForElementClickable(login_locators.enter_password);
        passwordField.sendKeys("validPass");
//        helper.waitForElementClickable(login_locators.remember_me_checkbox).click();
        helper.waitForElementClickable(login_locators.click_login_button).click();
        // simulate logout and revisit
        driver.navigate().back();

        boolean remembered = usernameField.getAttribute("value").equals("validUser");
        return remembered
                ? "User credentials remain saved on next visit"
                : "Remember Me not working";
    }
}
