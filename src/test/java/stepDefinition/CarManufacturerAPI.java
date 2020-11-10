package stepDefinition;

import common.CarConfiguration;
import common.CarUtility;
import common.Constants;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.junit.Assert;

import java.util.Map;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;


public class CarManufacturerAPI {
    CarUtility carUtility;
    public Response response;
    protected final String invalidKey = "dummy_api_key";
    protected final String invalidLocale = "dummy_locale";
    CarConfiguration config;

    @Given("manufacturer API endpoint")
    public void  manufacturer_api_endpoint() {
        config = CarConfiguration.getInstance();
        carUtility = new CarUtility();
        carUtility.createNewRequestSpec(config.getBaseURI());
    }

    @When("I perform GET operation with valid key and {string}")
    public void i_perform_get_operation_with_valid_key_and(String language) {
        carUtility.setQueryParams(config.getWa_key(), language);
        response = carUtility.makeCallToAPI(config.getManufacturerURL());
    }

    @Then("status code is {int}")
    public void status_code_is(int statusCode) {
        Assert.assertEquals(statusCode, response.getStatusCode());
    }

    @When("user access resource with valid token and without locale")
    public void user_access_resource_with_valid_token_and_without_locale() {
        carUtility.setQueryParams(config.getWa_key(), null);
        response = carUtility.makeCallToAPI(config.getManufacturerURL());
    }

    @Then("api should work with default locale")
    public void api_should_work_with_default_locale() {
        Assert.assertTrue( response.getBody().asString().contains("Abarth"));
    }

    @Then("I get list of manufacturer car types in response body")
    public void i_get_list_of_manufacturer_car_types_in_response_body() {
        Map<String,String> manufacturerData = response.getBody().jsonPath().getMap("wkda");
        Assert.assertFalse(manufacturerData.isEmpty());
    }

    @When("access manufacturer api without token")
    public void access_manufacturer_api_without_token() {
        carUtility.setQueryParams(null, "DE");
        response = carUtility.makeCallToAPI(config.getManufacturerURL());
    }

    @When("access manufacturer api with invalid token")
    public void access_manufacturer_api_with_invalid_token() {
        carUtility.createNewRequestSpec(config.getBaseURI());
        carUtility.setQueryParams(invalidKey, "DE");
        response = carUtility.makeCallToAPI(config.getManufacturerURL());
    }

    @When("access manufacturer api with invalid locale")
    public void access_manufacturer_api_with_invalid_locale() {
        carUtility.createNewRequestSpec(config.getBaseURI());
        carUtility.setQueryParams(config.getWa_key(), invalidKey);
        response = carUtility.makeCallToAPI(config.getManufacturerURL());
    }

    @Then("response structure should match with json schema")
    public void response_structure_should_match_with_json_schema() {
        response.then().assertThat().body(matchesJsonSchemaInClasspath("response_json_schema.json"));
    }

    @Then("manufacturer api response body contains message and error details")
    public void manufacturer_api_response_body_contains_message_and_error_details() {
        Assert.assertNotNull("Error message is expected but not found",response.header("Content-Type"));
        Assert.assertTrue(response.header("Content-Type").contains("application/json"));
        Assert.assertEquals(Constants.ERROR_MESSAGE, response.jsonPath().getString("error"));
    }
}
