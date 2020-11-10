package common;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;
import java.util.Set;

/**
 * Class for utility purpose,
 */
public class CarUtility {

    RequestSpecification requestSpec;

    public void createNewRequestSpec(String baseUri){
        requestSpec = null;
        RestAssured.baseURI = baseUri;
        requestSpec = RestAssured.given();
    }

    public void setQueryParams(String wa_key, String language){
        if(wa_key != null)
             requestSpec.queryParam(Constants.WA_KEY, wa_key);
        if(language != null)
             requestSpec.queryParam(Constants.LOCALE, language);
    }

    public void setQueryParams(String wa_key,String manufacturerCode, String language){
        setQueryParams(wa_key,language);
        requestSpec.queryParam(Constants.MANUFACTURER_CODE, manufacturerCode);
    }

    public void setQueryParams(String wa_key,String manufacturerCode,String mainType, String language){
        setQueryParams(wa_key,manufacturerCode,language);
        requestSpec.queryParam(Constants.MAIN_TYPE, mainType);
    }

    /**
     * @param apiURL to be called
     * @return response body
     */
    public Response makeCallToAPI(String apiURL){
        return requestSpec.get(apiURL);
    }

    /**
     * Get Manufacturer code from manufacturer API response
     * @param manufacturerAPIResponse
     * @return Manufacturer code
     */
    public String getManufacturerCode(Response manufacturerAPIResponse){
        String manufacturerCode = getKeyCodeFromResponseBody(manufacturerAPIResponse);
        return manufacturerCode;
    }


    /**
     * Get Main Type value from main types API response
     * @return Main Type Code from response body
     */
    public String getMainType(Response mainTypeAPIResponse){
        String mainType = getKeyCodeFromResponseBody(mainTypeAPIResponse);
        return mainType;
    }

    public String getKeyCodeFromResponseBody(Response response){
        String keyData = null;
        if(response != null && response.getStatusCode() == 200){
            Map<String, String> wakdadata = response.getBody().jsonPath().getMap("wkda");
            Set<String> manufacturer_codes = wakdadata.keySet();
            if(manufacturer_codes.size() > 0){
                keyData = manufacturer_codes.iterator().next();
            }
        }
        return keyData;
    }
}
