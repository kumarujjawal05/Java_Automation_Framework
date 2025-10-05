package pages;

import locators.register_locators;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.ElementHelper;

public class RegisterPage {

    private final ElementHelper helper;
    private final WebDriver driver;

    public RegisterPage(WebDriver driver){
        this.helper = new ElementHelper(driver, 10);
        this.driver = driver;
    }

    // Core form-filling function
    public void completeRegistrationProcess(String firstName, String lastName, String street, String city,
                                            String state, Integer zipcode, Integer phone, Integer ssn, String password){
        WebElement click_register_button = helper.waitForVisibilityofElement(register_locators.click_register_button);
        click_register_button.click();

        helper.waitForElementClickable(register_locators.enter_firstName).sendKeys(firstName);
        helper.waitForElementClickable(register_locators.enter_lastName).sendKeys(lastName);
        helper.waitForElementClickable(register_locators.enter_address).sendKeys(street);
        helper.waitForElementClickable(register_locators.enter_city).sendKeys(city);
        helper.waitForElementClickable(register_locators.enter_state).sendKeys(state);
        helper.waitForElementClickable(register_locators.enter_zipCode).sendKeys(String.valueOf(zipcode));
        helper.waitForElementClickable(register_locators.enter_phoneNumber).sendKeys(String.valueOf(phone));
        helper.waitForElementClickable(register_locators.enter_ssn).sendKeys(String.valueOf(ssn));
        helper.waitForElementClickable(register_locators.enter_username).sendKeys(firstName);
        helper.waitForElementClickable(register_locators.enter_password).sendKeys(password);
        helper.waitForElementClickable(register_locators.enter_confirmPassword).sendKeys(password);
        helper.waitForElementClickable(register_locators.click_register).click();
    }

    // ✅ Test-case specific wrappers (called from Excel-driven test)
    public String registerWithValidData(String firstName, String lastName, String street, String city,
                                        String state, Integer zipcode, Integer phone, Integer ssn, String password) {
        completeRegistrationProcess(firstName, lastName, street, city, state, zipcode, phone, ssn, password);
        return driver.getCurrentUrl().contains("dashboard")
                ? "User account is created and redirected to dashboard"
                : "Registration failed";
    }

    public String registerWithExistingEmail(String firstName, String lastName, String street, String city,
                                            String state, Integer zipcode, Integer phone, Integer ssn, String password) {
        completeRegistrationProcess(firstName, lastName, street, city, state, zipcode, phone, ssn, password);
        boolean errorVisible = driver.getPageSource().contains("Email already exists");
        return errorVisible ? "Error message: Email already exists" : "No error message shown";
    }

    public String verifyMandatoryFieldValidation() {
        helper.waitForVisibilityofElement(register_locators.click_register_button).click();
        helper.waitForElementClickable(register_locators.click_register).click();

        boolean validationVisible = driver.getPageSource().toLowerCase().contains("required")
                || driver.findElements(By.className("error")).size() > 0;

        return validationVisible
                ? "Validation messages displayed for mandatory fields"
                : "No validation messages shown";
    }

    public String verifyWeakPassword(String firstName, String lastName, String street, String city,
                                     String state, Integer zipcode, Integer phone, Integer ssn) {
        completeRegistrationProcess(firstName, lastName, street, city, state, zipcode, phone, ssn, "12345");
        boolean weakPwdError = driver.getPageSource().contains("weak password");
        return weakPwdError ? "Error message displayed for weak password" : "No weak password warning";
    }

    public String verifyPasswordMismatch(String firstName, String lastName, String street, String city,
                                         String state, Integer zipcode, Integer phone, Integer ssn) {
        helper.waitForVisibilityofElement(register_locators.click_register_button).click();
        helper.waitForElementClickable(register_locators.enter_firstName).sendKeys(firstName);
        helper.waitForElementClickable(register_locators.enter_lastName).sendKeys(lastName);
        helper.waitForElementClickable(register_locators.enter_password).sendKeys("Password123");
        helper.waitForElementClickable(register_locators.enter_confirmPassword).sendKeys("Password321");
        helper.waitForElementClickable(register_locators.click_register).click();

        boolean mismatchError = driver.getPageSource().toLowerCase().contains("passwords do not match");
        return mismatchError ? "Error message: Passwords do not match" : "No mismatch message shown";
    }
}