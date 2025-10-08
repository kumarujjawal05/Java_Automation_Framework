package pages;

import locators.register_locators;
import org.openqa.selenium.WebDriver;
import utils.ElementHelper;

public class RegisterPage {

    private final ElementHelper helper;

    public RegisterPage(WebDriver driver) {
        this.helper = new ElementHelper(driver, 10);
    }

    public void clickRegisterLink() {
        helper.waitForElementClickable(register_locators.click_register_link).click();
    }

    public void fillRegistrationForm(String firstName, String lastName, String street, String city, String state, String zipcode, String phone, String ssn, String username, String password, String confirmPassword) {
        if (firstName == null || firstName.isEmpty()) throw new IllegalArgumentException("First name cannot be null or empty");
        if (lastName == null || lastName.isEmpty()) throw new IllegalArgumentException("Last name cannot be null or empty");
        if (street == null || street.isEmpty()) throw new IllegalArgumentException("Street cannot be null or empty");
        if (city == null || city.isEmpty()) throw new IllegalArgumentException("City cannot be null or empty");
        if (state == null || state.isEmpty()) throw new IllegalArgumentException("State cannot be null or empty");
        if (zipcode == null || zipcode.isEmpty()) throw new IllegalArgumentException("Zipcode cannot be null or empty");
        if (phone == null || phone.isEmpty()) throw new IllegalArgumentException("Phone number cannot be null or empty");
        if (ssn == null || ssn.isEmpty()) throw new IllegalArgumentException("SSN cannot be null or empty");
        if (username == null || username.isEmpty()) throw new IllegalArgumentException("Username cannot be null or empty");
        if (password == null || password.isEmpty()) throw new IllegalArgumentException("Password cannot be null or empty");
        if (confirmPassword == null || confirmPassword.isEmpty()) throw new IllegalArgumentException("Confirm password cannot be null or empty");

        helper.waitForElementClickable(register_locators.enter_firstName).sendKeys(firstName);
        helper.waitForElementClickable(register_locators.enter_lastName).sendKeys(lastName);
        helper.waitForElementClickable(register_locators.enter_address).sendKeys(street);
        helper.waitForElementClickable(register_locators.enter_city).sendKeys(city);
        helper.waitForElementClickable(register_locators.enter_state).sendKeys(state);
        helper.waitForElementClickable(register_locators.enter_zipCode).sendKeys(zipcode);
        helper.waitForElementClickable(register_locators.enter_phoneNumber).sendKeys(phone);
        helper.waitForElementClickable(register_locators.enter_ssn).sendKeys(ssn);
        helper.waitForElementClickable(register_locators.enter_username).sendKeys(username);
        helper.waitForElementClickable(register_locators.enter_password).sendKeys(password);
        helper.waitForElementClickable(register_locators.enter_confirmPassword).sendKeys(confirmPassword);
    }

    public void clickRegisterButton() {
        helper.waitForElementClickable(register_locators.click_register_button).click();
    }

    public boolean isRegistrationSuccessful() {
        return helper.isElementVisible(register_locators.registration_success_message);
    }

    // Updated to use the more generic error locator for robust checking
    public boolean isRegistrationFailed() {
        return helper.isElementVisible(register_locators.any_registration_error_message);
    }

    public String getErrorMessage() {
        return helper.waitForVisibilityofElement(register_locators.any_registration_error_message).getText();
    }
}
