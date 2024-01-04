package marrero_ferrera_gcid_ulpgc.control;

import marrero_ferrera_gcid_ulpgc.model.Weather;

import java.time.Instant;

public interface WeatherSupplier {
    Weather getWeather(Weather.Location location, Instant ts);
}
