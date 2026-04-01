package com.demo.pom.runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(features = "src/test/resources/features", glue = { "com.demo.pom.steps",
		"com.demo.pom.hooks" }, plugin = { "pretty", "html:target/cucumber-reports/cucumber.html",
				"json:target/cucumber-reports/cucumber.json" }, monochrome = true)
public class RunCucumberTest extends AbstractTestNGCucumberTests {
}