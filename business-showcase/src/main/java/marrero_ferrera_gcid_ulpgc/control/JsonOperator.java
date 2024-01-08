package marrero_ferrera_gcid_ulpgc.control;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import marrero_ferrera_gcid_ulpgc.control.schemas.EnergyPrice;
import marrero_ferrera_gcid_ulpgc.model.Model;

import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;

public class JsonOperator {
    private final float powerChargeSolar;
    private final float powerChargeWind;
    private final float batteryCapacity;
    private final boolean recommendedHalfBattery;
    private final ArrayList<Model> resultModels;


    public JsonOperator(float powerChargeSolar, float powerChargeWind,
                        float batteryCapacity, boolean recommendedHalfBattery) {
        this.powerChargeSolar = powerChargeSolar;
        this.powerChargeWind = powerChargeWind;
        this.batteryCapacity = batteryCapacity;
        this.recommendedHalfBattery = recommendedHalfBattery;
        this.resultModels = new ArrayList<>();
    }

    public void operateAsSubscriber(String jsonString) throws MyManagerException {
        BuildModelFinal buildModelFinal = new BuildModelFinal();
        JsonElement jsonElement = parseJsonString(jsonString);
        if (isWeatherProvider(jsonElement)) {
            Model model = processWeatherProvider(jsonElement);
            resultModels.add(model);
        } else {
            Model model = processEnergyProvider(jsonElement);
            resultModels.add(model);
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new MyManagerException("An error occurred while receiving Subscriber messages.", e);
        }
        buildModelFinal.buildFinalModels(resultModels);
    }

    public void operateAsFetcher(ArrayList<String> jsonStrings) {
        BuildModelFinal buildModelFinal = new BuildModelFinal();
        ArrayList<JsonElement> jsonElements = parseJsonStrings(jsonStrings);
        if (isWeatherProvider(jsonElements.get(0))) {
            for (JsonElement currentJson : jsonElements) {
                // Method for processing weather strings
                Model model = processWeatherProvider(currentJson);
                resultModels.add(model);
            }
        } else {
            for (JsonElement currentJson : jsonElements) {
                // Method for processing energy strings
                Model model = processEnergyProvider(currentJson);
                resultModels.add(model);
            }
        }
        for (Model model : resultModels) {
            System.out.println(model.toString());
        }
        //buildModelFinal.buildFinalModels(resultModels);
    }

    private Model processEnergyProvider(JsonElement currentJsonElement) {
        Model model = new Model();
        model.setPredictionTime(getPredictionTime(currentJsonElement.getAsJsonObject()));
        if (currentJsonElement.isJsonObject()) {
            JsonObject energyObject = currentJsonElement.getAsJsonObject();
            model.setPrice(getPrice(energyObject));
            model.setSlot(getSlot(energyObject));
        }
        return model;
    }

    private Model processWeatherProvider(JsonElement currentJsonElement) {
        Model model = new Model();

        // Extract common fields
        model.setPredictionTime(getPredictionTime(currentJsonElement.getAsJsonObject()));

        // Extract weather-specific fields
        if (currentJsonElement.isJsonObject()) {
            JsonObject weatherObject = currentJsonElement.getAsJsonObject();
            model.setWeatherType(getWeatherType(weatherObject));
            model.setWindGained(getWindEfficiency(weatherObject));
            model.setSolarGained(getSolarEfficiency(weatherObject));
            model.setBatteryGained(getBatteryGained(weatherObject));
        }
        return model;
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
