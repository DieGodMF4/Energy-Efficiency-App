package marrero_ferrera_gcid_ulpgc.control;

import marrero_ferrera_gcid_ulpgc.model.Weather;

import java.time.Instant;
import java.util.ArrayList;

public class WeatherController {

    private final WeatherSupplier supplier;
    private final WeatherStore store;
    private final Weather.Location location;

    public WeatherController(WeatherSupplier supplier, WeatherStore store, Weather.Location location) {
        this.supplier = supplier;
        this.store = store;
        this.location = location;
    }

    public void execute() {
        ArrayList<Weather> weathers = supplier.getWeather(location, Instant.now());
        store.insertWeather(weathers);
    }
}

