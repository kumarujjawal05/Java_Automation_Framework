package locators;

import org.openqa.selenium.By;

public class register_locators {
    public static By click_register_button = By.xpath("//a[normalize-space()='Register']");
    public static By enter_firstName = By.id("customer.firstName");
    public static By enter_lastName = By.id("customer.lastName");
    public static By enter_address = By.id("customer.address.street");
    public static By enter_city = By.id("customer.address.city");
    public static By enter_state = By.id("customer.address.state");
    public static By enter_zipCode = By.id("customer.address.zipCode");
    public static By enter_phoneNumber = By.id("customer.phoneNumber");
    public static By enter_ssn = By.id("customer.ssn");
    public static By enter_username = By.id("customer.username");
    public static By enter_password = By.id("customer.password");
    public static By enter_confirmPassword = By.id("repeatedPassword");
    public static By click_register = By.xpath("//input[@value='Register']");
}
