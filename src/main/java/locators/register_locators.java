package locators;

import org.openqa.selenium.By;

public class register_locators {
    public static final By click_register_link = By.xpath("//a[normalize-space()='Register']");
    public static final By enter_firstName = By.id("customer.firstName");
    public static final By enter_lastName = By.id("customer.lastName");
    public static final By enter_address = By.id("customer.address.street");
    public static final By enter_city = By.id("customer.address.city");
    public static final By enter_state = By.id("customer.address.state");
    public static final By enter_zipCode = By.id("customer.address.zipCode");
    public static final By enter_phoneNumber = By.id("customer.phoneNumber");
    public static final By enter_ssn = By.id("customer.ssn");
    public static final By enter_username = By.id("customer.username");
    public static final By enter_password = By.id("customer.password");
    public static final By enter_confirmPassword = By.id("repeatedPassword");
    public static final By click_register_button = By.xpath("//input[@value='Register']");

    // --- Locators for Validation ---
    public static final By registration_success_message = By.xpath("//p[contains(text(),'Your account was created successfully.')]");
    
    // A generic locator for any validation error message on the page.
    // This is more robust for checking different failure scenarios (e.g., username exists, password mismatch, etc.)
    public static final By any_registration_error_message = By.className("error");
}
