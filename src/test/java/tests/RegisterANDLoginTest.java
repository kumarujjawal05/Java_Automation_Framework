package tests;

import com.aventstack.extentreports.Status;
import drivers.BrowserConfig;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.RegisterPage;
import utils.ConfigReader;
import utils.ExcelUtils;

// The test class now inherits all setup and teardown from BrowserConfig
public class RegisterANDLoginTest extends BrowserConfig {

    // Page objects will be initialized in each test to ensure they use the correct driver instance
    private LoginPage loginPage;
    private RegisterPage registerPage;

    @DataProvider(name = "testCases")
    public Object[][] testData() {
        String excelPath = "src/main/resources/TestCases_Login_Register.xlsx";
        ExcelUtils excelUtils = new ExcelUtils(excelPath);
        return excelUtils.getSheetData("Sheet1");
    }

    @Test(dataProvider = "testCases")
    public void registerAndLoginTest(String id, String page, String testCase, String steps, String expectedResult) {
        // Initialize page objects here, using the 'driver' from the base class
        loginPage = new LoginPage(driver);
        registerPage = new RegisterPage(driver);

        // Navigate to the base URL before each test execution
        driver.get(ConfigReader.get("url"));

        extentTest.get().log(Status.INFO, "Executing Test Case: " + id + " - " + testCase);

        try {
            // --- Shared Test Data from Config ---
            String username = ConfigReader.get("username");
            String password = ConfigReader.get("password");
            String firstName = ConfigReader.get("firstName");
            String lastName = ConfigReader.get("lastName");
            String street = ConfigReader.get("street");
            String city = ConfigReader.get("city");
            String state = ConfigReader.get("state");
            String zipcode = ConfigReader.get("zipcode");
            String phone = ConfigReader.get("phone");
            String ssn = ConfigReader.get("ssn");

            // Validate test data
            if (username == null || username.isEmpty()) throw new IllegalArgumentException("Username cannot be null or empty");
            if (password == null || password.isEmpty()) throw new IllegalArgumentException("Password cannot be null or empty");
            if (firstName == null || firstName.isEmpty()) throw new IllegalArgumentException("First name cannot be null or empty");
            if (lastName == null || lastName.isEmpty()) throw new IllegalArgumentException("Last name cannot be null or empty");
            if (street == null || street.isEmpty()) throw new IllegalArgumentException("Street cannot be null or empty");
            if (city == null || city.isEmpty()) throw new IllegalArgumentException("City cannot be null or empty");
            if (state == null || state.isEmpty()) throw new IllegalArgumentException("State cannot be null or empty");
            if (zipcode == null || zipcode.isEmpty()) throw new IllegalArgumentException("Zipcode cannot be null or empty");
            if (phone == null || phone.isEmpty()) throw new IllegalArgumentException("Phone number cannot be null or empty");
            if (ssn == null || ssn.isEmpty()) throw new IllegalArgumentException("SSN cannot be null or empty");

            if ("Login".equalsIgnoreCase(page)) {
                switch (id) {
                    case "L01": // Valid Login
                        loginPage.enterUsername(username);
                        loginPage.enterPassword(password);
                        loginPage.clickLoginButton();
                        Assert.assertTrue(loginPage.isLoginSuccessful(), "Expected successful login.");
                        break;
                    case "L02": // Invalid Login
                        loginPage.enterUsername("invalidUser");
                        loginPage.enterPassword("invalidPass");
                        loginPage.clickLoginButton();
                        Assert.assertTrue(loginPage.isLoginFailed(), "Expected failed login with an error message.");
                        break;
                    case "L03": // Empty Fields Login
                        loginPage.clickLoginButton();
                        Assert.assertTrue(loginPage.isLoginFailed(), "Expected error message for empty fields.");
                        break;
                    case "L04": // Forgot Password Link
                        loginPage.clickForgotPasswordLink();
                        Assert.assertTrue(driver.getCurrentUrl().contains("lookup.htm"), "Expected to be on the 'Forgot Login Info' page.");
                        break;
                    default:
                        extentTest.get().skip("Login test case with ID '" + id + "' not implemented.");
                        break;
                }
            } else if ("Register".equalsIgnoreCase(page)) {
                registerPage.clickRegisterLink();
                switch (id) {
                    case "R01": // Valid Registration
                        String newUsername = "newuser" + System.currentTimeMillis();
                        registerPage.fillRegistrationForm(firstName, lastName, street, city, state, zipcode, phone, ssn, newUsername, password, password);
                        registerPage.clickRegisterButton();
                        Assert.assertTrue(registerPage.isRegistrationSuccessful(), "Expected successful registration.");
                        break;
                    case "R02": // Existing Username
                        registerPage.fillRegistrationForm(firstName, lastName, street, city, state, zipcode, phone, ssn, username, password, password);
                        registerPage.clickRegisterButton();
                        Assert.assertTrue(registerPage.isRegistrationFailed(), "Expected registration to fail for an existing username.");
                        break;
                    case "R03": // Mandatory Fields Validation
                        registerPage.clickRegisterButton(); // Submit empty form
                        Assert.assertTrue(registerPage.isRegistrationFailed(), "Expected validation errors for mandatory fields.");
                        break;
                    case "R05": // Password Mismatch
                        String mismatchUser = "mismatch" + System.currentTimeMillis();
                        registerPage.fillRegistrationForm(firstName, lastName, street, city, state, zipcode, phone, ssn, mismatchUser, password, "mismatchedPassword");
                        registerPage.clickRegisterButton();
                        Assert.assertTrue(registerPage.isRegistrationFailed(), "Expected registration to fail due to password mismatch.");
                        break;
                    default:
                        extentTest.get().skip("Register test case with ID '" + id + "' not implemented.");
                        break;
                }
            }

            extentTest.get().pass("✅ Passed | " + testCase);

        } catch (AssertionError e) {
            extentTest.get().fail("❌ Failed | " + testCase + " | Assertion failed: " + e.getMessage());
            throw e; // Re-throw to mark test as failed in TestNG
        } catch (Exception e) {
            extentTest.get().fail("⚠️ Exception during test " + id + ": " + e.getClass().getSimpleName() + " - " + e.getMessage());
            throw e; // Re-throw to mark test as failed in TestNG
        }
    }
}
