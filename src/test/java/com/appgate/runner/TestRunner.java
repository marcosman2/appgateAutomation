package com.appgate.runner;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"com.appgate.stepDefinitions"},
        plugin = {"pretty", "html:target/Customer-Availability.html"},
        monochrome = true
)

public class TestRunner extends AbstractTestNGCucumberTests {}
