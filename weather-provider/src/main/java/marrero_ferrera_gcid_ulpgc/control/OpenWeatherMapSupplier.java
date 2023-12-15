package marrero_ferrera_gcid_ulpgc.control;

import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import marrero_ferrera_gcid_ulpgc.model.Location;
import marrero_ferrera_gcid_ulpgc.model.Weather;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.time.Instant;

public class OpenWeatherMapSupplier implements WeatherSupplier {
    private final String apiKey;

    public OpenWeatherMapSupplier(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public Weather getWeather(Location location, Instant ts) {
        long unixTimestamp = ts.getEpochSecond();
        try {
            JsonObject jsonObject = apiConnector(location);

            int listSize = jsonObject.getAsJsonArray("list").size();

            for (int i = 0; i < listSize; i++) {
                JsonObject currentListObject = jsonObject.getAsJsonArray("list").get(i).getAsJsonObject();
                if (currentListObject.get("dt").getAsLong() == unixTimestamp) {
                    float temperature = currentListObject.getAsJsonObject("main")
                            .get("temp").getAsFloat();
                    String weatherType = currentListObject.getAsJsonArray("weather")
                            .get(0).getAsJsonObject().get("main").getAsString();
                    int cloud = currentListObject.getAsJsonObject("clouds")
                            .get("all").getAsInt();
                    float humidity = currentListObject.getAsJsonObject("main")
                            .get("humidity").getAsFloat();
                    float rain = currentListObject.get("pop").getAsFloat();
                    float wind = currentListObject.getAsJsonObject("wind")
                            .get("speed").getAsFloat();
                    return new Weather(weatherType, cloud, temperature, humidity, location, ts, rain, wind);
                }
            }
            throw new MySenderException("Location or time were incorrect!");
        } catch (Exception e) {
            throw new JsonParseException(e);
        }
    }

    private JsonObject apiConnector(Location location) {
        HttpClient httpClient = HttpClients.createDefault();
        String httpUrl = "https://api.openweathermap.org/data/2.5/forecast?lat=" + location.getLatitude() +
                "&lon=" + location.getLongitude() + "&units=metric&appid=" + apiKey;
        HttpGet httpGet = new HttpGet(httpUrl);

        try {
            HttpResponse response = httpClient.execute(httpGet);

            String json = EntityUtils.toString(response.getEntity());
            return new com.google.gson.JsonParser().parse(json).getAsJsonObject();
        } catch (IOException e) {
            throw new JsonIOException(e);
        }
    }
}
