package locators;

import org.openqa.selenium.By;

public class login_locators {

    public static final By enter_username = By.name("username");
    public static final By enter_password = By.name("password");
    public static final By click_forgot_link = By.xpath("//a[normalize-space()='Forgot login info?']");
    public static final By click_login_button = By.xpath("//input[@value='Log In']");

}
