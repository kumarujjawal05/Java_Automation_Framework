package locators;

import org.openqa.selenium.By;

public class login_locators {

    public static final By enter_username = By.name("username");
    public static final By enter_password = By.name("password");
    public static final By click_login_button = By.xpath("//input[@value='Log In']");
    public static final By click_forgot_link = By.linkText("Forgot login info?");

    // Locators for validation
    public static final By login_error_message = By.cssSelector("p.error");
    public static final By account_overview_title = By.xpath("//h1[text()='Accounts Overview']");

}
