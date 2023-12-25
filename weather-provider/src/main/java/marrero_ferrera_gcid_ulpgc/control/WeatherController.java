package marrero_ferrera_gcid_ulpgc.control;

import com.google.gson.*;
import marrero_ferrera_gcid_ulpgc.model.Location;
import marrero_ferrera_gcid_ulpgc.model.Weather;

import java.lang.reflect.Type;
import java.time.Clock;
import java.time.Instant;
import java.util.ArrayList;

public class WeatherController {
    private final ArrayList<Weather> weathers;

    public WeatherController() {
        weathers = new ArrayList<>();
    }

    public void getAndPublishWeatherData(Location location, String apiKey, String topicName, Instant instant) {
        OpenWeatherMapSupplier supplier = new OpenWeatherMapSupplier(apiKey);
        Weather weather = supplier.getWeather(location, instant);

        addWeather(weather);
        long nowUtc = Instant.now(Clock.systemUTC()).getEpochSecond();
        DataContainer dataContainer = new DataContainer(weather.getSs(), nowUtc, weather.getTs(), weather, location);

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Instant.class, new InstantSerializer())
                .create();
        String dataContainerToJson = gson.toJson(dataContainer);
        JMSWeatherStore store = new JMSWeatherStore(topicName);
        store.insertWeather(dataContainerToJson);
    }

    public void addWeather(Weather weather) {
        if (!weathers.contains(weather)) {
            weathers.add(weather);
        } else {
            System.out.println("That weather is already in the list!");
        }
    }

    public record DataContainer(String ss, long ts, long predTime, Weather weather, Location location) {
    }

    static class InstantSerializer implements JsonSerializer<Instant> {
        @Override
        public JsonElement serialize(Instant instant, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(instant.toString());
        }
    }
}

