package marrero_ferrera_gcid_ulpgc.control.fetchers;

import com.google.gson.*;
import marrero_ferrera_gcid_ulpgc.control.schemas.Weather;

import java.time.Instant;

public class WeatherSerializer {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Instant.class, new InstantSerializer())
            .create();
    public static void main(String[] args) {
        String json = "{\"ts\":1704497585000,\"ss\":\"WeatherProvider\",\"predictionTime\":1704758400000,\"weatherType\":\"Clouds\",\"clouds\":100,\"temperature\":12.21,\"rain\":0.0,\"humidity\":74.0,\"wind\":6.73,\"location\":{\"latitude\":28.01,\"longitude\":-15.58,\"name\":\"My Home\"}}";
        Weather weather = gson.fromJson(json, Weather.class);
        System.out.println();
        System.out.println(weather);
    }
    static class InstantSerializer implements JsonSerializer<Instant>, JsonDeserializer<Instant> {
        @Override
        public JsonElement serialize(Instant instant, java.lang.reflect.Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(instant.toEpochMilli());
        }

        @Override
        public Instant deserialize(JsonElement jsonElement, java.lang.reflect.Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            return Instant.ofEpochMilli(jsonElement.getAsLong());
        }
    }
}
