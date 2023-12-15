package marrero_ferrera_gcid_ulpgc.control;

import marrero_ferrera_gcid_ulpgc.model.Location;
import marrero_ferrera_gcid_ulpgc.model.Weather;

import java.time.Instant;

public interface WeatherSupplier {
    Weather getWeather(Location location, Instant ts);
}
