package tests;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import drivers.BrowserConfig;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.RegisterPage;
import utils.ConfigReader;
import utils.ExcelUtils;

public class RegisterANDLoginTest extends BrowserConfig {

    @DataProvider(name = "testCases")
    public Object[][] testData() {
        String excelPath = "src/main/resources/TestCases_Login_Register.xlsx";
        ExcelUtils excelUtils = new ExcelUtils(excelPath);
        return excelUtils.getSheetData("Sheet1");
    }


    @BeforeClass
    public void browser_setup(){
        super.browser_setup();
    }

    @Test(dataProvider = "testCases")
    public void registerTest(String id, String page, String testCase, String steps, String expectedResult){

        ExtentTest test = extentTest.get();
        test.log(Status.PASS,"pass");
        String actualResult = "";

        // Shared config data
        String firstName = ConfigReader.get("firstName");
        String lastName = ConfigReader.get("lastName");
        String street = ConfigReader.get("street");
        String city = ConfigReader.get("city");
        String state = ConfigReader.get("state");
        int zipcode = Integer.parseInt(ConfigReader.get("zipcode"));
        int phone = Integer.parseInt(ConfigReader.get("phone"));
        int ssn = Integer.parseInt(ConfigReader.get("ssn"));
        String password = ConfigReader.get("password");

        try {
            if (page.equalsIgnoreCase("Login")) {
                LoginPage login = new LoginPage(driver);
                switch (id) {
                    case "L01":
                        actualResult = login.loginWithValidCredentials();
                        break;
                    case "L02":
                        actualResult = login.loginWithInvalidCredentials();
                        break;
                    case "L03":
                        actualResult = login.loginWithEmptyFields();
                        break;
                    case "L04":
                        actualResult = login.verifyForgotPasswordLink();
                        break;
                    case "L05":
                        actualResult = login.verifyRememberMeFunctionality();
                        break;
                }
            } else if (page.equalsIgnoreCase("Register")) {
                RegisterPage register = new RegisterPage(driver);
                switch (id) {
                    case "R01":
                        actualResult = register.registerWithValidData(firstName, lastName, street, city, state, zipcode, phone, ssn, password);
                        break;
                    case "R02":
                        actualResult = register.registerWithExistingEmail(firstName, lastName, street, city, state, zipcode, phone, ssn, password);
                        break;
                    case "R03":
                        actualResult = register.verifyMandatoryFieldValidation();
                        break;
                    case "R04":
                        actualResult = register.verifyWeakPassword(firstName, lastName, street, city, state, zipcode, phone, ssn);
                        break;
                    case "R05":
                        actualResult = register.verifyPasswordMismatch(firstName, lastName, street, city, state, zipcode, phone, ssn);
                        break;
                }
            }

            // Assertion and reporting
            Assert.assertEquals(actualResult.trim(), expectedResult.trim());
            test.log(Status.PASS, "Passed | Expected: " + expectedResult + " | Actual: " + actualResult);

        } catch (AssertionError e) {
            test.log(Status.FAIL, "Failed | Expected: " + expectedResult + " | Actual: " + actualResult);
            throw e;
        } catch (Exception e) {
            test.log(Status.FAIL, "Exception while executing test " + id + ": " + e.getMessage());
            throw e;
        }
    }
}
