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

    @Given("build dates API endpoint")
    public void build_dates_api_endpoint() {
        carUtility.createNewRequestSpec(config.getBaseURI());
        carUtility.setQueryParams(config.getWa_key(), Constants.DEFAULT_LOCALE);
        response = carUtility.makeCallToAPI(config.getManufacturerURL());
        manufacturer_code = carUtility.getManufacturerCode(response);
        if(manufacturer_code == null){
            manufacturer_code = defaultManufaturerCode;
        }

        carUtility.createNewRequestSpec(config.getBaseURI());
        carUtility.setQueryParams(config.getWa_key(), manufacturer_code, Constants.DEFAULT_LOCALE);
        response = carUtility.makeCallToAPI(config.getMainTypeURL());
        mainTypes = carUtility.getMainType(response);
        if(mainTypes == null){
            mainTypes = defaultMainType;
        }
        // Creating new request for build date API
        carUtility.createNewRequestSpec(config.getBaseURI());
    }

    @When("I perform GET operation with valid key {string} manufacturer code and main types")
    public void i_perform_get_operation_with_valid_key_manufacturer_code_and_main_types(String locale) {
        carUtility.setQueryParams(config.getWa_key(), manufacturer_code, mainTypes,locale);
        response = carUtility.makeCallToAPI(config.getBuildDateURL());
    }

    @Then("status code is {int} for build dates api")
    public void status_code_is_for_build_dates_api(int statusCode) {
        Assert.assertEquals(statusCode, response.getStatusCode());
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
        carUtility.setQueryParams(null, manufacturer_code, mainTypes,Constants.DEFAULT_LOCALE);
        response = carUtility.makeCallToAPI(config.getBuildDateURL());
    }

    @When("access build dates api with invalid token")
    public void access_build_dates_api_with_invalid_token() {
        carUtility.setQueryParams(invalidKey, manufacturer_code, mainTypes,Constants.DEFAULT_LOCALE);
        response = carUtility.makeCallToAPI(config.getBuildDateURL());
    }

    @When("access build dates api with invalid locale")
    public void access_build_dates_api_with_invalid_locale() {
        carUtility.setQueryParams(config.getWa_key(), manufacturer_code, mainTypes,invalidLocale);
        response = carUtility.makeCallToAPI(config.getBuildDateURL());
    }

    @When("access build dates api without manufacturer code")
    public void access_build_dates_api_without_manufacturer_code() {
        carUtility.setQueryParams(config.getWa_key(), null, mainTypes,Constants.DEFAULT_LOCALE);
        response = carUtility.makeCallToAPI(config.getBuildDateURL());
    }

    @When("access build dates api without main type")
    public void access_build_dates_api_without_main_type() {
        carUtility.setQueryParams(config.getWa_key(), manufacturer_code, null,Constants.DEFAULT_LOCALE);
        response = carUtility.makeCallToAPI(config.getBuildDateURL());
    }

    @Then("build dates api response body contains error message {string}")
    public void build_dates_api_response_body_contains_error_message(String errorMessage) {
        Assert.assertNotNull("error message is expected but not found ",response.header("Content-Type"));
        Assert.assertTrue(response.header("Content-Type").contains("application/json"));
        Assert.assertEquals(errorMessage, response.jsonPath().getString("error"));
    }


}
