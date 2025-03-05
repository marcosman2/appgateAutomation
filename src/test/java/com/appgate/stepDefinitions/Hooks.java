package com.appgate.stepDefinitions;
import com.appgate.util.DockerContainers;
import com.appgate.util.StartStopApplication;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Hooks {

    private static final Logger LOGGER = LoggerFactory.getLogger(Hooks.class);

    @BeforeSuite
    public void startUp() {
        try {
            DockerContainers.startContainersUp();
            LOGGER.info("Containers successfully started.");
        }
        catch(Exception e){
            LOGGER.error("Error starting containers.", e);
            throw new RuntimeException("Failed to start containers.", e);
        }

        try {
            StartStopApplication.startApplicationAsync();
            LOGGER.info("Application successfully started.");
        }
        catch(Exception e){
            LOGGER.error("Error starting the application.", e);
            throw new RuntimeException("Failed to start the application.", e);
        }
    }

    @AfterSuite
    public void teardown() {
        try {
            StartStopApplication.stopApplication();
            LOGGER.info("Application successfully stopped.");
        }
        catch (Exception e){
            LOGGER.error("Error stopping the application.", e);
        }

        try {
            DockerContainers.stopContainers();
            LOGGER.info("Containers successfully stopped.");
        }
        catch (Exception e){
            LOGGER.error("Error stopping the containers.", e);
        }
    }
}
