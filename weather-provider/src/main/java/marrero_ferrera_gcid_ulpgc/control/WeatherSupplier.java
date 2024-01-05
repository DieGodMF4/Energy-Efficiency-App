package marrero_ferrera_gcid_ulpgc.control;

import marrero_ferrera_gcid_ulpgc.model.Weather;

import java.time.Instant;
import java.util.ArrayList;

public interface WeatherSupplier {
    ArrayList<Weather> getWeather(Weather.Location location, Instant ts);
}
