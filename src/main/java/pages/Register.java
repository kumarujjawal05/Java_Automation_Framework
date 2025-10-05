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

    public void completeRegistrationProcess(String firstName, String lastName){
        WebElement click_register_button = helper.waitForVisibilityofElement(register_locators.click_register_button);
        click_register_button.click();
    }
}
