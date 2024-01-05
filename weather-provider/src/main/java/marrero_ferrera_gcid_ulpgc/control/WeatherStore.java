package marrero_ferrera_gcid_ulpgc.control;

import marrero_ferrera_gcid_ulpgc.model.Weather;

import java.util.ArrayList;

public interface WeatherStore {
    void insertWeather(ArrayList<Weather> weathers);
}
