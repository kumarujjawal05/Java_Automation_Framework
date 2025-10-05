package drivers;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;





public class BrowserConfig {

    public WebDriver driver;
    protected Properties prop;


    protected ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
    protected static ExtentReports extent;


    private final String browser = System.getProperty("browser", "chrome");
    private final String headlessProperty = System.getProperty("headless", "false");
    private final boolean isHeadless = headlessProperty.equalsIgnoreCase("true");


    public void browser_setup() {
        prop = new Properties();

        try {
            FileInputStream fis = new FileInputStream("src/main/resources/config.properties");
            prop.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }

        switch (browser.toLowerCase()) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions options = new ChromeOptions();
                if (isHeadless) {
                    options.addArguments("--headless");
                }
                driver = new ChromeDriver(options);
                break;


            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions options1 = new EdgeOptions();
                if (isHeadless) {
                    options1.addArguments("--headless");
                }
                driver = new EdgeDriver(options1);
                break;

            default:
                throw new RuntimeException("Browser Not Supported..." + browser);
        }

        driver.manage().window().maximize();
        driver.get(prop.getProperty("url"));


        //Initialize the Extent Reports

        if (extent == null) {
            ExtentSparkReporter spark = new ExtentSparkReporter("Reports/extent-report.html");
            spark.config().setDocumentTitle("Java Automation Testing");
            spark.config().setReportName("UI Automation Report");
            spark.config().setTheme(Theme.DARK);
            extent = new ExtentReports();
            extent.attachReporter(spark);
        }
    }

    @BeforeMethod
    public void startTest(Method method) {
        ExtentTest test = extent.createTest(method.getName());
        extentTest.set(test);
    }

    @AfterMethod
    public void tearDownMethod(ITestResult result) {
        ExtentTest test = extentTest.get();
        if (test == null) return;

        if (result.getStatus() == ITestResult.FAILURE) {
            try {
                String dirPath = System.getProperty("user.dir") + "/Screenshots";
                File dir = new File(dirPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                String timestamp = new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());
                String fileName = result.getName() + "_" + timestamp + ".png";
                String filePath = dirPath + File.separator + fileName;

                if (driver instanceof TakesScreenshot) {
                    File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                    File destFile = new File(filePath);
                    FileUtils.copyFile(srcFile, destFile);

                    test.fail("Test Failed: " + result.getThrowable().getMessage(),
                            MediaEntityBuilder.createScreenCaptureFromPath(destFile.getAbsolutePath()).build());
                }
            } catch (Exception e) {
                throw new RuntimeException(e);

            }
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            test.pass("Test Passed");

        } else if (result.getStatus() == ITestResult.SKIP) {
            test.skip("Test Skipped");

        }


    }

    @AfterClass
    public void teardown(){
        if (driver!=null){
            driver.quit();
        }
        if (extent!=null){
            extent.flush();
        }
    }
}
