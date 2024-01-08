package marrero_ferrera_gcid_ulpgc.control;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import marrero_ferrera_gcid_ulpgc.control.schemas.EnergyPrice;
import marrero_ferrera_gcid_ulpgc.model.Model.Item;

import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;

public class JsonOperator {
    // DEPRECIATED
    // DEPRECIATED
    // DEPRECIATED
    // DEPRECIATED
    // DEPRECIATED
    // DEPRECIATED
    private final float powerChargeSolar;
    private final float powerChargeWind;
    private final float batteryCapacity;
    private final boolean recommendedHalfBattery;
    private final ArrayList<Item> resultItems;


    public JsonOperator(float powerChargeSolar, float powerChargeWind,
                        float batteryCapacity, boolean recommendedHalfBattery) {
        this.powerChargeSolar = powerChargeSolar;
        this.powerChargeWind = powerChargeWind;
        this.batteryCapacity = batteryCapacity;
        this.recommendedHalfBattery = recommendedHalfBattery;
        this.resultItems = new ArrayList<>();
    }

    public void operateAsSubscriber(String jsonString) throws MyManagerException {
        BuildModelFinal buildModelFinal = new BuildModelFinal();
        JsonElement jsonElement = parseJsonString(jsonString);
        if (isWeatherProvider(jsonElement)) {
            Item item = processWeatherProvider(jsonElement);
            resultItems.add(item);
        } else {
            Item item = processEnergyProvider(jsonElement);
            resultItems.add(item);
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new MyManagerException("An error occurred while receiving Subscriber messages.", e);
        }
    }

    public void operateAsFetcher(ArrayList<String> jsonStrings) {
        ArrayList<JsonElement> jsonElements = parseJsonStrings(jsonStrings);
        if (isWeatherProvider(jsonElements.get(0))) {
            for (JsonElement currentJson : jsonElements) {
                Item item = processWeatherProvider(currentJson);
                resultItems.add(item);
            }
        } else {
            for (JsonElement currentJson : jsonElements) {
                Item item = processEnergyProvider(currentJson);
                resultItems.add(item);
            }
        }
        for (Item item : resultItems) {
            System.out.println(item.toString());
        }
        //buildModelFinal.buildFinalModels(resultModels);
    }

    private Item processEnergyProvider(JsonElement currentJsonElement) {
        Item item = new Item();
        item.setPredictionTime(getPredictionTime(currentJsonElement.getAsJsonObject()));
        if (currentJsonElement.isJsonObject()) {
            JsonObject energyObject = currentJsonElement.getAsJsonObject();
            item.setPrice(getPrice(energyObject));
            item.setSlot(getSlot(energyObject));
        }
        return item;
    }

    private Item processWeatherProvider(JsonElement currentJsonElement) {
        Item item = new Item();
        item.setPredictionTime(getPredictionTime(currentJsonElement.getAsJsonObject()));
        if (currentJsonElement.isJsonObject()) {
            JsonObject weatherObject = currentJsonElement.getAsJsonObject();
            item.setWeatherType(getWeatherType(weatherObject));
            item.setWindGained(getWindEfficiency(weatherObject));
            item.setSolarGained(getSolarEfficiency(weatherObject));
            item.setBatteryGained(getBatteryGained(weatherObject));
        }
        return item;
    }

    private float getBatteryGained(JsonObject weatherObject) {
        if (batteryCapacity == 0) return 0.0f;
        else {
            float windEfficiency = getWindEfficiency(weatherObject);
            float solarEfficiency = getSolarEfficiency(weatherObject);
            int multiplier = recommendedHalfBattery ? 2 : 1;
            return ((windEfficiency / batteryCapacity) + (solarEfficiency / batteryCapacity)) * multiplier;
        }
    }

    private Instant getPredictionTime(JsonObject jsonObject) {
        return Instant.parse(jsonObject.get("predictionTime").getAsString());
    }

    private String getWeatherType(JsonObject jsonObject) {
        return jsonObject.get("weatherType").getAsString();
    }

    private float getWindEfficiency(JsonObject jsonObject) {
        if (powerChargeWind == 0) return 0.0f;
        float windSpeed = jsonObject.get("wind").getAsFloat();
        float lowToModerateThreshold = 2.5f;
        float strongToStormyThreshold = 11.0f;
        float excessiveThreshold = 25.0f;
        if (windSpeed < lowToModerateThreshold) return powerChargeWind * 0.4f;
        else if (windSpeed >= lowToModerateThreshold && windSpeed < strongToStormyThreshold) {
            return powerChargeWind;
        } else if (windSpeed >= strongToStormyThreshold && windSpeed < excessiveThreshold) {
            return powerChargeWind * 0.7f;
        } else return 0.0f;
    }

    private float getSolarEfficiency(JsonObject jsonObject) {
        if (powerChargeSolar == 0) return 0.0f;
        if (isDay(jsonObject)) {
            int clouds = jsonObject.get("clouds").getAsInt();
            int maxSolarThreshold = 25;
            int mediumSolarThreshold = 50;
            int minimumSolarThreshold = 80;
            if (clouds <= maxSolarThreshold) return powerChargeSolar;
            else if (clouds <= mediumSolarThreshold) {
                return powerChargeSolar * 0.7f;
            } else if (clouds <= minimumSolarThreshold) {
                return powerChargeSolar * 0.5f;
            } else if (clouds <= 100) {
                return powerChargeSolar * 0.3f;
            } else return 0.0f;
        } else return 0.0f;
    }

    private boolean isDay(JsonObject jsonObject) {
        Instant predictionTime = getPredictionTime(jsonObject);
        LocalTime localTime = LocalTime.ofInstant(predictionTime, ZoneId.of("UTC"));
        LocalTime startOfDay = LocalTime.of(8, 0);
        LocalTime endOfDay = LocalTime.of(20, 0);
        return localTime.isAfter(startOfDay) && localTime.isBefore(endOfDay);
    }

    private float getPrice(JsonObject jsonObject) {
        return jsonObject.get("price").getAsFloat(); // Ejemplo, reemplazar con la implementación real
    }

    private EnergyPrice.Slot getSlot(JsonObject jsonObject) {
        String slotName = jsonObject.get("slot").getAsString();
        return switch (slotName) {
            case "Valley" -> EnergyPrice.Slot.Valley;
            case "Flat" -> EnergyPrice.Slot.Flat;
            case "Peak" -> EnergyPrice.Slot.Peak;
            default -> throw new IllegalArgumentException("Slot name not suitable: " + slotName);
        };
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
