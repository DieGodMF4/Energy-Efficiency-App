package marrero_ferrera_gcid_ulpgc.control;

import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import marrero_ferrera_gcid_ulpgc.model.Weather;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class OpenWeatherMapSupplier implements WeatherSupplier {
    private final String apiKey;

    public OpenWeatherMapSupplier(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public ArrayList<Weather> getWeather(Weather.Location location, Instant predictionTime) {
        ArrayList<Weather> weathers = new ArrayList<>();
        try {
            JsonObject jsonObject = apiConnector(location);
            int listSize = jsonObject.getAsJsonArray("list").size();
            for (int i = 0; i < listSize; i++) {
                JsonObject currentListObject = jsonObject.getAsJsonArray("list").get(i).getAsJsonObject();
                    float temperature = currentListObject.getAsJsonObject("main")
                            .get("temp").getAsFloat();
                    String weatherType = currentListObject.getAsJsonArray("weather")
                            .get(0).getAsJsonObject().get("main").getAsString();
                    int cloud = currentListObject.getAsJsonObject("clouds")
                            .get("all").getAsInt();
                    float humidity = currentListObject.getAsJsonObject("main")
                            .get("humidity").getAsFloat();
                    float rain = currentListObject.get("pop").getAsFloat();
                    Instant ts = dateToInstant(currentListObject.get("dt_txt").getAsString());
                    float wind = currentListObject.getAsJsonObject("wind")
                            .get("speed").getAsFloat();
                    weathers.add(new Weather(ts, weatherType, cloud, temperature, humidity, location, rain, wind));
            }
            if (!(weathers.isEmpty())) return weathers;
            else throw new MySenderException("Location or time were incorrect!");
        } catch (Exception e) {
            throw new JsonParseException(e);
        }
    }

    private Instant dateToInstant(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse(date, formatter);
        return localDateTime.toInstant(ZoneOffset.UTC);
    }

    private JsonObject apiConnector(Weather.Location location) {
        HttpClient httpClient = HttpClients.createDefault();
        String httpUrl = "https://api.openweathermap.org/data/2.5/forecast?lat=" + location.latitude() +
                "&lon=" + location.longitude() + "&units=metric&appid=" + apiKey;
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
