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

public class CarBuildDatesAPI {
    public Response response;
    protected final String invalidKey = "dummy_api_key";
    protected final String invalidLocale = "dummy_locale";
    private String defaultManufaturerCode = "020";
    private String defaultMainType = "Punto";
    private String defaultBuildDate = "2016";


    String manufacturer_code;
    String mainTypes;
    CarUtility carUtility = new CarUtility();;
    CarConfiguration config = CarConfiguration.getInstance();

    @Given("build dates API endpoint and main type and manufacturer code")
    public void build_dates_api_endpoint_and_main_type_and_manufacturer_code() {
        carUtility.createNewRequestSpec(config.getBaseURI());
        carUtility.setQueryParams(config.getWa_key(), "en");
        response = carUtility.makeCallToAPI(config.getManufacturerURL());
        manufacturer_code = carUtility.getManufacturerCode(response);
        if(manufacturer_code == null){
            manufacturer_code = defaultManufaturerCode;
        }

        carUtility.createNewRequestSpec(config.getBaseURI());
        carUtility.setQueryParams(config.getWa_key(), manufacturer_code, "en");
        response = carUtility.makeCallToAPI(config.getMainTypeURL());
        mainTypes = carUtility.getMainType(response);
        if(mainTypes == null){
            mainTypes = defaultMainType;
        }

    }

    @When("I perform GET operation with valid key {string} manufacturer code and main types")
    public void i_perform_get_operation_with_valid_key_manufacturer_code_and_main_types(String locale) {
        carUtility.createNewRequestSpec(config.getBaseURI());
        carUtility.setQueryParams(config.getWa_key(), manufacturer_code, mainTypes,locale);
        response = carUtility.makeCallToAPI(config.getBuildDateURL());
    }

    @Then("status code is {int} for build dates api")
    public void status_code_is_for_build_dates_api(int statusCode) {
        Assert.assertEquals(statusCode, response.getStatusCode());
    }

    @Then("build dates api response body contains message and error details")
    public void build_dates_api_response_body_contains_message_and_error_details() {
        Assert.assertNotNull("error message is expected but not found ",response.header("Content-Type"));
        Assert.assertTrue(response.header("Content-Type").contains("application/json"));
        Assert.assertEquals(Constants.ERROR_MESSAGE, response.jsonPath().getString("error"));
    }

    @Then("I get list of build dates of car in response body")
    public void i_get_list_of_build_dates_of_car_in_response_body() {
        Map<String,String> buildDatesData = response.getBody().jsonPath().getMap("wkda");
        Assert.assertFalse(buildDatesData.isEmpty());
    }

    @Then("build date api should work with default locale")
    public void build_date_api_should_work_with_default_locale() {
        Assert.assertTrue( response.getBody().asString().contains(defaultBuildDate));
    }

    @Then("response structure should match with json schema of build dates")
    public void response_structure_should_match_with_json_schema_of_build_dates() {
        response.then().assertThat().body(matchesJsonSchemaInClasspath("response_json_schema.json"));

    }

    @When("access build dates api without token")
    public void access_build_dates_api_without_token() {
        carUtility.createNewRequestSpec(config.getBaseURI());
        carUtility.setQueryParams(null, manufacturer_code, mainTypes,"en");
        response = carUtility.makeCallToAPI(config.getBuildDateURL());
    }

    @When("access build dates api with invalid token")
    public void access_build_dates_api_with_invalid_token() {
        carUtility.createNewRequestSpec(config.getBaseURI());
        carUtility.setQueryParams(invalidKey, manufacturer_code, mainTypes,"en");
        response = carUtility.makeCallToAPI(config.getBuildDateURL());
    }

    @When("access build dates api with invalid locale")
    public void access_build_dates_api_with_invalid_locale() {
        carUtility.createNewRequestSpec(config.getBaseURI());
        carUtility.setQueryParams(config.getWa_key(), manufacturer_code, mainTypes,invalidLocale);
        response = carUtility.makeCallToAPI(config.getBuildDateURL());
    }


}
