package marrero_ferrera_gcid_ulpgc.control;

import marrero_ferrera_gcid_ulpgc.model.Weather;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

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
        for (int i = 0; i < 5; i++) {
            Weather weather = supplier.getWeather(location, calculateInstant(i));
            store.insertWeather(weather);
        }
    }

    private static Instant calculateInstant(int i) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime scheduledTime = now.withHour(12).withMinute(0).withSecond(0).withNano(0);
        if (now.isAfter(scheduledTime)) scheduledTime = scheduledTime.plusDays(1);
        LocalDateTime nextExecutionTime = scheduledTime.plusDays(i);
        return nextExecutionTime.atZone(ZoneId.systemDefault()).toInstant();
    }

}

