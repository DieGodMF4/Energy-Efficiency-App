package marrero_ferrera_gcid_ulpgc.control.handlers;

import marrero_ferrera_gcid_ulpgc.control.schemas.Weather;
import marrero_ferrera_gcid_ulpgc.model.Model;

import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;

public class WeatherHandler {
    private final Model model;

    public WeatherHandler(Model model) {
        this.model = model;
    }

    public void handle(Weather weatherEvent) {
        Model.Item item = processWeatherToItem(weatherEvent);

        if (!isItemDuplicate(item.getPredictionTime(), item.getWeatherType())) {
            model.addItem(item);
        }
    }

    private boolean isItemDuplicate(Instant predictionTime, String weatherType) {
        ArrayList<Model.Item> items = model.getItems();
        return items.stream()
                .filter(item -> item.getWeatherType() != null)
                .anyMatch(item -> item.getPredictionTime().equals(predictionTime) &&
                        item.getWeatherType().equals(weatherType));
    }

    private Model.Item processWeatherToItem(Weather weather) {
        Model.Item item = new Model.Item();
        item.setPredictionTime(weather.getPredictionTime());
        item.setWeatherType(weather.getWeatherType());
        item.setWindGained(getWindEfficiency(weather));
        item.setSolarGained(getSolarEfficiency(weather));
        item.setBatteryGained(getBatteryGained(weather));
        return item;
    }

    private float getBatteryGained(Weather weather) {
        float batteryCapacity = model.getBatteryCapacity();
        if (batteryCapacity == 0) return 0.0f;
        else {
            float windEfficiency = getWindEfficiency(weather);
            float solarEfficiency = getSolarEfficiency(weather);
            int multiplier = model.isRecommendedHalfBattery() ? 2 : 1;
            return ((windEfficiency / batteryCapacity) + (solarEfficiency / batteryCapacity)) * multiplier;
        }
    }

    private float getWindEfficiency(Weather weather) {
        float powerChargeWind = model.getPowerChargeWind();
        if (powerChargeWind == 0) return 0.0f;
        float windSpeed = weather.getWind();
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

    private float getSolarEfficiency(Weather weather) {
        float powerChargeSolar = model.getPowerChargeSolar();
        if (powerChargeSolar == 0) return 0.0f;
        if (isDay(weather)) {
            int clouds = weather.getClouds();
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

    private boolean isDay(Weather weather) {
        Instant predictionTime = weather.getPredictionTime();
        LocalTime localTime = LocalTime.ofInstant(predictionTime, ZoneId.of("UTC"));
        LocalTime startOfDay = LocalTime.of(8, 0);
        LocalTime endOfDay = LocalTime.of(20, 0);
        return localTime.isAfter(startOfDay) && localTime.isBefore(endOfDay);
    }
}
