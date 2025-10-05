package tests;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import drivers.BrowserConfig;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.Register;
import utils.ExcelUtils;

public class LoginTest extends BrowserConfig {

    @BeforeClass
    public void browser_setup(){
        super.browser_setup();
    }

//    @DataProvider(name = "loginData")
//    public Object[][] getLoginData() {
//        ExcelUtils excelUtils = new ExcelUtils("src/main/resources/TestCases_Login_Register.xlsx");
//        return excelUtils.getSheetData("Sheet1");
//    }

//    @Test(dataProvider = "loginData")
//    public void loginTest(String firstName, String lastName, String street, String city,
//                          String state, Integer zipcode, Integer phone, Integer ssn, String password) {
//        Register loginPage = new Register(driver);
//        loginPage.completeRegistrationProcess(firstName, lastName, street, city, state, zipcode, phone, ssn, password);
//        // Add assertions here to verify successful login
//    }

    @Test
    public void registerTest(){

        ExtentTest test = extentTest.get();
        test.log(Status.PASS,"pass");
        Register register = new Register(driver);
        register.completeRegistrationProcess(
                "John",        // firstName
                "Doe",         // lastName
                "123 Main St", // street
                "New York",    // city
                "NY",          // state
                10001,         // zipcode
                987654320,    // phone
                123456,        // ssn
                "P@ssw0rd"     // password
        );
    }
}
