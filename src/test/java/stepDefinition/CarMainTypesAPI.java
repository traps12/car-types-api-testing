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

public class CarMainTypesAPI {

    public Response response;
    protected final String invalidKey = "dummy_api_key";
    protected final String invalidLocale = "dummy_locale";
    private String defaultManufaturerCode = "020";

    String manufacturer_code;
    CarUtility carUtility = new CarUtility();;
    CarConfiguration config = CarConfiguration.getInstance();

    @Given("main types API endpoint")
    public void main_types_api_endpoint() {
        carUtility.createNewRequestSpec(config.getBaseURI());
        carUtility.setQueryParams(config.getWa_key(), Constants.DEFAULT_LOCALE);
        response = carUtility.makeCallToAPI(config.getManufacturerURL());
        manufacturer_code = carUtility.getManufacturerCode(response);
        if(manufacturer_code == null){
            manufacturer_code = defaultManufaturerCode;
        }
        // Creating new request for main type API
        carUtility.createNewRequestSpec(config.getBaseURI());
    }

    @When("I perform GET operation with valid key and {string} and manufacturer code")
    public void i_perform_get_operation_with_valid_key_and_and_manufacturer_code(String locale) {
        carUtility.setQueryParams(config.getWa_key(), manufacturer_code, locale);
        response = carUtility.makeCallToAPI(config.getMainTypeURL());
    }

    @Then("status code is {int} for main types api")
    public void status_code_is_for_main_types_api(int statusCode) {
        Assert.assertEquals(statusCode, response.getStatusCode());
    }

    @Then("I get list of main types of car in response body")
    public void i_get_list_of_main_types_of_car_in_response_body() {
        Map<String,String> maiTypesData = response.getBody().jsonPath().getMap("wkda");
        Assert.assertFalse(maiTypesData.isEmpty());
    }

    @When("user access main type api with valid token manufacturer code and without locale")
    public void user_access_main_type_api_with_valid_token_manufacturer_code_and_without_locale() {
        carUtility.setQueryParams(config.getWa_key(), manufacturer_code, Constants.DEFAULT_LOCALE);
        response = carUtility.makeCallToAPI(config.getMainTypeURL());
    }

    @Then("main type api should work with default locale")
    public void main_type_api_should_work_with_default_locale() {
        Assert.assertTrue( response.getBody().asString().contains("Grande Punto"));
    }

    @Then("response structure should match with json schema of main type")
    public void response_structure_should_match_with_json_schema_of_main_type() {
        response.then().assertThat().body(matchesJsonSchemaInClasspath("response_json_schema.json"));

    }

    @When("access main types api without token")
    public void access_main_types_api_without_token() {
        carUtility.setQueryParams(null, manufacturer_code, Constants.DEFAULT_LOCALE);
        response = carUtility.makeCallToAPI(config.getMainTypeURL());
    }

    @When("access main types api with invalid token")
    public void access_main_types_api_with_invalid_token() {
        carUtility.setQueryParams(invalidKey, manufacturer_code, Constants.DEFAULT_LOCALE);
        response = carUtility.makeCallToAPI(config.getMainTypeURL());
    }

    @Then("main types api response body contains error message {string}")
    public void main_types_api_response_body_contains_error_message(String errorMessage) {
        Assert.assertNotNull("error message is expected but not found ",response.header("Content-Type"));
        Assert.assertTrue(response.header("Content-Type").contains("application/json"));
        Assert.assertEquals(errorMessage, response.jsonPath().getString("error"));
    }

    @When("access main types api without manufacturer code")
    public void access_main_types_api_without_manufacturer_code() {
        carUtility.setQueryParams(config.getWa_key(), null,Constants.DEFAULT_LOCALE);
        response = carUtility.makeCallToAPI(config.getBuildDateURL());
    }
}
