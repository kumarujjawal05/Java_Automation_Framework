package pages;

import locators.register_locators;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.ElementHelper;

public class Register {

    private final ElementHelper helper;

    public Register(WebDriver driver){
        this.helper = new ElementHelper(driver, 10);
    }

    public void completeRegistrationProcess(String firstName, String lastName, String street, String city,
                                            String state, Integer zipcode, Integer phone, Integer ssn, String password){
        WebElement click_register_button = helper.waitForVisibilityofElement(register_locators.click_register_button);
        click_register_button.click();
        WebElement enter_firstName = helper.waitForElementClickable(register_locators.enter_firstName);
        enter_firstName.sendKeys(firstName);
        WebElement enter_lastName = helper.waitForElementClickable(register_locators.enter_lastName);
        enter_lastName.sendKeys(lastName);
        WebElement enter_address = helper.waitForElementClickable(register_locators.enter_address);
        enter_address.sendKeys(street);
        WebElement enter_city = helper.waitForElementClickable(register_locators.enter_city);
        enter_city.sendKeys(city);
        WebElement enter_state = helper.waitForElementClickable(register_locators.enter_state);
        enter_state.sendKeys(state);
        WebElement enter_zipCode = helper.waitForElementClickable(register_locators.enter_zipCode);
        enter_zipCode.sendKeys(String.valueOf(zipcode));
        WebElement enter_phone = helper.waitForElementClickable(register_locators.enter_phoneNumber);
        enter_phone.sendKeys(String.valueOf(phone));
        WebElement enter_ssn = helper.waitForElementClickable(register_locators.enter_ssn);
        enter_ssn.sendKeys(String.valueOf(ssn));
        WebElement enter_username = helper.waitForElementClickable(register_locators.enter_username);
        enter_username.sendKeys(firstName);
        WebElement enter_password = helper.waitForElementClickable(register_locators.enter_password);
        enter_password.sendKeys(password);
        WebElement enter_confirmPassword = helper.waitForElementClickable(register_locators.enter_confirmPassword);
        enter_confirmPassword.sendKeys(password);
        WebElement click_register = helper.waitForElementClickable(register_locators.click_register);
        click_register.click();




    }
}
