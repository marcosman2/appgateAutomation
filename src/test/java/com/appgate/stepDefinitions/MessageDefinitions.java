package com.appgate.stepDefinitions;
import com.appgate.util.Config;
import com.appgate.util.FetchResponse;
import com.appgate.util.SendMessage;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;;

public class MessageDefinitions {

    private String response;
    private String jsonResponse;
    private boolean isAvailable;
    private String responseCustomer;
    private String customerValue;
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageDefinitions.class);

    private String getConvertedResponse() throws Exception {
        response = FetchResponse.fetchResponseMessage();
        return FetchResponse.convertResponseToJson(response);
    }

    @When("User sends the request, using Customer {string}")
    public void userSendsMessage(String customerKey){
        try{
            customerValue = Config.get(customerKey);

            if (customerValue == null) {
                throw new RuntimeException("No customer data found");
            }

            SendMessage.sendMessageToQueue(customerValue);
            LOGGER.info("Request successfully sent.");
        }
        catch(Exception e){
            LOGGER.error("Error sending the request.", e);
            throw new RuntimeException("Failed to send the request.", e);
        }
    }

    @Then("User validates that the response schema is the expected one")
    public void userValidatesResponseSchema(){
        try{
            jsonResponse = getConvertedResponse();
            assertThat(jsonResponse, matchesJsonSchemaInClasspath(Config.get("responseSchemaPath")));
            LOGGER.info("Response schema is the expected one.");
        }
        catch(Exception e){
            LOGGER.error("Error validating the response schema.", e);
            throw new RuntimeException("Failed to validate the response schema.", e);
        }
    }

    @Then("User validates that Customer is available")
    public void userValidatesCustomerIsAvailable(){
        try{
            jsonResponse = getConvertedResponse();
            isAvailable = JsonPath.from(jsonResponse).getBoolean("isAvailable");
            assertTrue(isAvailable, "Customer was expected to be available, but he/she is not");
            LOGGER.info("Customer successfully available.");
        }
        catch(Exception e){
            LOGGER.error("Error validating if Customer is available.", e);
            throw new RuntimeException("Failed to validate if Customer is available.", e);
        }
    }

    @Then("User validates that Customer is not available")
    public void userValidatesCustomerIsNotAvailable(){
        try{
            jsonResponse = getConvertedResponse();
            isAvailable = JsonPath.from(jsonResponse).getBoolean("isAvailable");
            assertFalse(isAvailable, "Customer was expected to not be available, but he/she is");
            LOGGER.info("Customer successfully not available.");
        }
        catch(Exception e){
            LOGGER.error("Error validating if Customer is not available.", e);
            throw new RuntimeException("Failed to validate if Customer is not available.", e);
        }
    }

    @Then("User validates that Customer on the response is the expected one {string}")
    public void userValidatesCustomerOnResponse(String customerKey){
        try{
            customerValue = Config.get(customerKey);

            if (customerValue == null) {
                throw new RuntimeException("No customer data found");
            }
            jsonResponse = getConvertedResponse();
            responseCustomer = JsonPath.from(jsonResponse).getString("customer");
            assertTrue(responseCustomer.equalsIgnoreCase(customerValue), "Customer on the response is not the expected one");
            LOGGER.info("Customer fetched on the response is the expected one.");
        }
        catch(Exception e){
            LOGGER.error("Error validating if Customer fetched is the expected one.", e);
            throw new RuntimeException("Failed to validate if Customer fetched is the expected one.", e);
        }
    }
}
