package com.demo.pom.hooks;
import java.util.Base64;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.demo.pom.factory.DriverFactory;
import com.demo.pom.utils.ScreenshotUtil;
import com.demo.pom.reports.ExtentManager;
import com.demo.pom.reports.ExtentTestHolder;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
public class Hooks {
    private static final Logger logger = LoggerFactory.getLogger(Hooks.class);
    private static final ExtentReports extent = ExtentManager.getExtent();
    @Before
    public void setUp(Scenario scenario) {
        logger.info("Starting browser");
        ExtentTest test = extent.createTest(scenario.getName());
        scenario.getSourceTagNames().forEach(test::assignCategory);
 
        ExtentTestHolder.set(test);
        test.info("Browser launched");
        DriverFactory.initDriver();
    }
    @After
    public void tearDown(Scenario scenario) {
    	ExtentTest test = ExtentTestHolder.get();
        WebDriver driver = DriverFactory.getDriver();
        try {
            if (driver != null && scenario.isFailed()) {
                logger.info("Scenario failed. Capturing screenshot for: {}", scenario.getName());
                byte[] screenshotBytes = ScreenshotUtil.captureScreenshotAsBytes(driver);
                scenario.attach(screenshotBytes, "image/png", scenario.getName());
                String screenshotPath = ScreenshotUtil.captureScreenshotToFile(driver, scenario.getName());
                logger.info("Screenshot saved at: {}", screenshotPath);
            
	            String base64 = Base64.getEncoder().encodeToString(screenshotBytes);
	            if (test != null) {
	                test.fail("❌ FAILED");
	                test.addScreenCaptureFromBase64String(base64, "Failure Screenshot");
	            	}
            }
        } catch (Exception e) {
            logger.error("Failed to capture screenshot: {}", e.getMessage());
        } finally {
        	extent.flush();
            ExtentTestHolder.remove();
            logger.info("Closing browser");
            DriverFactory.quitDriver();
        }
    }
}