import marrero_ferrera_gcid_ulpgc.control.MyManagerException;
import marrero_ferrera_gcid_ulpgc.control.fetchers.DataLakeFetcher;
import marrero_ferrera_gcid_ulpgc.control.fetchers.DataLakeWeatherFetcher;
import marrero_ferrera_gcid_ulpgc.control.handlers.WeatherHandler;
import marrero_ferrera_gcid_ulpgc.model.Model;

import java.time.Instant;

public class Tests {
    public static void main(String[] args) throws MyManagerException {
        String topicNameWeather = "prediction.Weather";
        Model model = new Model();
        WeatherHandler weatherHandler = new WeatherHandler(model);
        DataLakeFetcher weatherDataLakeFetcher = new DataLakeWeatherFetcher(topicNameWeather, Instant.now(), weatherHandler, "");
        if (weatherDataLakeFetcher.fileExists()) weatherDataLakeFetcher.fetchFiles();
    }
}
