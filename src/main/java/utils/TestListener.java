package utils;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.jacoco.core.analysis.Analyzer;
import org.jacoco.core.analysis.CoverageBuilder;
import org.jacoco.core.tools.ExecFileLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jacoco.core.analysis.ICounter;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import drivers.BrowserConfig;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

public class TestListener implements ITestListener {

    private static final Logger logger = LogManager.getLogger(TestListener.class);

    private int totalTests = 0;
    private int passedTests = 0;
    private int failedTests = 0;
    private int skippedTests = 0;

    @Override
    public void onStart(ITestContext context) {
        System.out.println("Starting Test Suite: " + context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println("Finished Test Suite: " + context.getName());
        System.out.println("--------------------------------------------------");
        System.out.println("Test Summary:");
        System.out.println("Total Tests Run: " + totalTests);
        System.out.println("Passed Tests: " + passedTests);
        System.out.println("Failed Tests: " + failedTests);
        System.out.println("Skipped Tests: " + skippedTests);
        System.out.println("--------------------------------------------------");

        // Generate coverage summary after tests complete (jacoco.exec is created by the agent)
        try {
            File execFile = new File("target/jacoco.exec");
            if (!execFile.exists()) {
                logger.warn("Jacoco exec file not found at target/jacoco.exec. Run 'mvn clean verify' to produce coverage.");
                return;
            }

            ExecFileLoader loader = new ExecFileLoader();
            loader.load(execFile);

            CoverageBuilder coverageBuilder = new CoverageBuilder();
            Analyzer analyzer = new Analyzer(loader.getExecutionDataStore(), coverageBuilder);
            analyzer.analyzeAll(new File("target/classes"));

            File out = new File("target/jacoco-summary.txt");
            try (PrintWriter pw = new PrintWriter(new FileWriter(out))) {
                pw.println("Coverage Summary:");

                // also prepare Extent test if available
                final com.aventstack.extentreports.ExtentTest coverageTest = (BrowserConfig.extent != null)
                        ? BrowserConfig.extent.createTest("Coverage Summary")
                        : null;

                coverageBuilder.getClasses().forEach(cls -> {
                    try {
                        ICounter instructionCounter = cls.getInstructionCounter();
                        long total = instructionCounter.getTotalCount();
                        long covered = instructionCounter.getCoveredCount();
                        double coverage = total == 0 ? 0.0 : (double) covered / total * 100.0;
                        String line = String.format("%s : %.2f%% (%d/%d)", cls.getName(), coverage, covered, total);
                        pw.println(line);
                        logger.info(line);
                        if (coverageTest != null) {
                            coverageTest.log(Status.INFO, line);
                        }
                    } catch (Exception e) {
                        logger.error("Error reading coverage for class: " + cls.getName(), e);
                    }
                });

                if (coverageTest != null) {
                    coverageTest.log(Status.PASS, "Coverage summary written to target/jacoco-summary.txt");
                }
            }

            logger.info("Jacoco coverage summary written to target/jacoco-summary.txt");
        } catch (Exception e) {
            logger.error("Error calculating coverage data", e);
            if (BrowserConfig.extent != null) {
                BrowserConfig.extent.createTest("Coverage Summary").log(Status.FAIL, "Error calculating coverage data: " + e.getMessage());
            }
        }
    }

    @Override
    public void onTestStart(ITestResult result) {
        totalTests++;
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        passedTests++;
        // coverage will be computed at the end of suite (onFinish)
    }

    @Override
    public void onTestFailure(ITestResult result) {
        failedTests++;
        // coverage will be computed at the end of suite (onFinish)
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        skippedTests++;
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        // Not implemented
    }
}
