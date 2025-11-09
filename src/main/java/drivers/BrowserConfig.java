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
import org.openqa.selenium.safari.SafariDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BrowserConfig {

    protected WebDriver driver;
    // make extent public so listeners can write to the same report
    public static ExtentReports extent;
    protected ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    @BeforeSuite(alwaysRun = true)
    public void initializeReport() {
        synchronized (BrowserConfig.class) {
            if (extent == null) {
                ExtentSparkReporter spark = new ExtentSparkReporter("Reports/extent-report.html");
                spark.config().setDocumentTitle("Java Automation Testing");
                spark.config().setReportName("UI Automation Report");
                spark.config().setTheme(Theme.DARK);
                extent = new ExtentReports();
                extent.attachReporter(spark);
            }
        }
    }

    @AfterSuite(alwaysRun = true)
    public void flushReport() {
        if (extent != null) {
            extent.flush();
        }
    }

    @Parameters("browser")
    @BeforeMethod(alwaysRun = true)
    public void setupTest(Method method, @Optional("chrome") String browser) {
        // Ensure extent is initialized even if @BeforeSuite didn't run in this fork/classloader
        if (extent == null) {
            initializeReport();
        }

        switch (browser.toLowerCase()) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                if (Boolean.parseBoolean(System.getProperty("headless", "false"))) {
                    chromeOptions.addArguments("--headless");
                }
                driver = new ChromeDriver(chromeOptions);
                break;
            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                if (Boolean.parseBoolean(System.getProperty("headless", "false"))) {
                    edgeOptions.addArguments("--headless");
                }
                driver = new EdgeDriver(edgeOptions);
                break;
            case "safari":
                driver = new SafariDriver();
                break;
            default:
                throw new RuntimeException("Browser Not Supported: " + browser);
        }

        try {
            driver.manage().window().maximize();
        } catch (Exception ignored) {
            // Some drivers (headless or remote) may not support maximize; ignore gracefully
        }

        ExtentTest test = extent.createTest(method.getName() + " - " + browser);
        extentTest.set(test);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDownTest(ITestResult result) {
        ExtentTest test = extentTest.get();
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

                File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                FileUtils.copyFile(srcFile, new File(filePath));

                test.fail("Test Failed: " + result.getThrowable().getMessage(),
                        MediaEntityBuilder.createScreenCaptureFromPath(new File(filePath).getAbsolutePath()).build());
            } catch (IOException e) {
                test.fail("Failed to capture screenshot: " + e.getMessage());
            } catch (Exception e) {
                test.fail("Failed to capture screenshot: " + e.getMessage());
            }
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            if (test != null) test.pass("Test Passed");
        } else if (result.getStatus() == ITestResult.SKIP) {
            if (test != null) test.skip("Test Skipped");
        }

        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception ignored) {
            }
        }
    }
}
