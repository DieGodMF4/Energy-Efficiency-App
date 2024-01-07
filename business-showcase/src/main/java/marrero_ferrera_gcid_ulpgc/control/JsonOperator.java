package marrero_ferrera_gcid_ulpgc.control;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;

public class JsonOperator {
    private final float powerChargeSolar;
    private final float powerChargeWind;
    private final float batteryCapacity;
    private final boolean recommendedHalfBattery;


    public JsonOperator(float powerChargeSolar, float powerChargeWind,
                        float batteryCapacity, boolean recommendedHalfBattery) {
        this.powerChargeSolar = powerChargeSolar;
        this.powerChargeWind = powerChargeWind;
        this.batteryCapacity = batteryCapacity;
        this.recommendedHalfBattery = recommendedHalfBattery;
    }

    public void operateAsSubscriber(String jsonString) {
        // Si la string tiene como ss weather: hacer método para tratamiento weather
    }

    public void operateAsFetcher(ArrayList<String> jsonStrings) {
        ArrayList<JsonElement> jsonElements = parseJsonStrings(jsonStrings);

        if (isWeatherProvider(jsonElements.get(0))) {
            for (JsonElement currentJson : jsonElements) {
                // Realizar el método específico para WeatherProvider
                processWeatherProvider(currentJson);
            }
        }

        System.out.println("El operador tiene que hacer la tarea para:" + jsonStrings.size() +
                "con" + powerChargeSolar + powerChargeWind + batteryCapacity + recommendedHalfBattery);
    }

    private void processWeatherProvider(JsonElement firstJsonElement) {
    }

    private ArrayList<JsonElement> parseJsonStrings(ArrayList<String> jsonStrings) {
        ArrayList<JsonElement> jsonElements = new ArrayList<>();
        for (String jsonString : jsonStrings) {
            jsonElements.add(parseJsonString(jsonString));
        }
        return jsonElements;
    }

    private JsonElement parseJsonString(String jsonString) {
        return JsonParser.parseString(jsonString);
    }

    private boolean isWeatherProvider(JsonElement jsonElement) {
        if (jsonElement != null && jsonElement.isJsonObject()) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            if (jsonObject.has("ss") && jsonObject.get("ss").isJsonPrimitive()) {
                String ssValue = jsonObject.get("ss").getAsString();
                return "WeatherProvider".equals(ssValue);
            }
        }
        return false;
    }
}
