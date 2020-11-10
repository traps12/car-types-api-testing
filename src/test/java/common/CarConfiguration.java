package common;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Class to load configuration params and use at other places
 */
public class CarConfiguration {

    private String wa_key;
    private String baseURI;
    private String manufacturerURL;
    private String mainTypeURL;
    private String buildDateURL;

    private static CarConfiguration carConfigObj;

    private CarConfiguration() {
    }

    /**
     * @return CarConfiguration singleton object
     */
    public static CarConfiguration getInstance() {
        if(carConfigObj == null){
            synchronized (CarConfiguration.class){
                if(carConfigObj == null){
                    carConfigObj = new CarConfiguration();
                    //Load configuration values from config file
                    carConfigObj.loadConfiguration();
                }
            }
        }
        return carConfigObj;
    }


    /**
     * Method to load configuration values from config.properties file
     */
    private void loadConfiguration(){
        Properties configProperties = new Properties();
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream(Constants.TEST_PROPERTIES_FILENAME);
            configProperties.load(is);
            wa_key = configProperties.getProperty("WA_KEY");
            baseURI = configProperties.getProperty("BASE_URI");
            manufacturerURL = configProperties.getProperty("MANUFACTURER_API_URL");
            mainTypeURL = configProperties.getProperty("MAIN_TYPE_API_URL");
            buildDateURL = configProperties.getProperty("BUILD_DATE_API_URL");
        } catch (IOException e) {
            //Can create logger and log the error
            e.printStackTrace();
        }
    }

    public String getWa_key() {
        return wa_key;
    }

    public String getBaseURI() {
        return baseURI;
    }

    public String getManufacturerURL() {
        return manufacturerURL;
    }
    public String getMainTypeURL() {
        return mainTypeURL;
    }

    public String getBuildDateURL() {
        return buildDateURL;
    }

}
