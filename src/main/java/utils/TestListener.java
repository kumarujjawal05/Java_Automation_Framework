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

import java.io.File;

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
    }

    @Override
    public void onTestStart(ITestResult result) {
        totalTests++;
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        passedTests++;
        addCoverageData(result);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        failedTests++;
        addCoverageData(result);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        skippedTests++;
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        // Not implemented
    }

    private void addCoverageData(ITestResult result) {
        try {
            File execFile = new File("target/jacoco.exec");
            ExecFileLoader loader = new ExecFileLoader();
            loader.load(execFile);

            CoverageBuilder coverageBuilder = new CoverageBuilder();
            Analyzer analyzer = new Analyzer(loader.getExecutionDataStore(), coverageBuilder);
            analyzer.analyzeAll(new File("target/classes"));

            coverageBuilder.getClasses().forEach(cls -> {
                if (cls.getName().contains(result.getTestClass().getName())) {
                    ICounter instructionCounter = cls.getInstructionCounter();
                    double coverage = (double) instructionCounter.getCoveredCount() / instructionCounter.getTotalCount() * 100;
                    logger.info("Coverage for {}: {}%", result.getName(), coverage);
                    // Add to Extent Report
                    // extentTest.get().log(Status.INFO, "Coverage: " + coverage + "%");
                }
            });
        } catch (Exception e) {
            logger.error("Error calculating coverage data", e);
        }
    }
}
