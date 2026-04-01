package com.demo.pom.steps;

import com.demo.pom.factory.DriverFactory;
import com.demo.pom.pages.LoginPage;
import com.demo.pom.utils.ExcelReader;
import com.demo.pom.reports.ExtentLogger;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.openqa.selenium.WebDriver;
 
import java.util.Map;
 
public class LoginSteps {
 
    private WebDriver driver = DriverFactory.getDriver();
    private LoginPage loginPage = new LoginPage(driver);
    private Map<String, String> currentRow;
 
    @Given("user launches the application")
    public void user_launches_the_application() {
    	 ExtentLogger.step("Open URL: https://the-internet.herokuapp.com/login");
        loginPage.open("https://the-internet.herokuapp.com/login");
    }
 
    @When("user logs in using excel row {int}")
    public void user_logs_in_using_excel_row(Integer rowNumber) {
        // Load row data from Excel (rowNumber matches your Examples table)
        currentRow = ExcelReader.getRowData("testdata/login-data.xlsx", "LoginData", rowNumber);
        
        String username = currentRow.get("username");
        ExtentLogger.step("Enter username: " + username);
        String password = currentRow.get("password");
        ExtentLogger.step("Enter password: " + password);
        loginPage.enterUsername(username);
        loginPage.enterPassword(password);
        ExtentLogger.step("Click Login button");
        loginPage.clickLogin();
    }
 
    @Then("user should see expected result from excel")
    public void user_should_see_expected_result_from_excel() {
        String expectedResult = currentRow.get("expectedResult");
        String expectedMessage = currentRow.get("expectedMessage");
        String actualMessage = loginPage.getFlashMessage();
        ExtentLogger.step("Verify flash message contains: " + expectedMessage);
        if ("success".equalsIgnoreCase(expectedResult)) {
            assert actualMessage.contains(expectedMessage) :
                    "Expected success message not found! Actual: " + actualMessage;
        } else {
            assert actualMessage.contains(expectedMessage) :
                    "Expected failure message not found! Actual: " + actualMessage;
        }
    }
}
 