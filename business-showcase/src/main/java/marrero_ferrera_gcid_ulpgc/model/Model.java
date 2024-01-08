package marrero_ferrera_gcid_ulpgc.model;

import marrero_ferrera_gcid_ulpgc.control.schemas.EnergyPrice;

import java.time.Instant;

public class Model {
    private Instant predictionTime;
    private String weatherType;
    private float windGained;
    private float solarGained;
    private float batteryGained;
    private float price;
    private EnergyPrice.Slot slot;

    public Model(Instant predictionTime, String weatherType, float windGained, float solarGained, float batteryGained, float price, EnergyPrice.Slot slot) {
        this.predictionTime = predictionTime;
        this.weatherType = weatherType;
        this.windGained = windGained;
        this.solarGained = solarGained;
        this.batteryGained = batteryGained;
        this.price = price;
        this.slot = slot;
    }

    public Model(){}

    public void setPredictionTime(Instant predictionTime) {
        this.predictionTime = predictionTime;
    }

    public void setWeatherType(String weatherType) {
        this.weatherType = weatherType;
    }

    public void setWindGained(float windGained) {
        this.windGained = windGained;
    }

    public void setSolarGained(float solarGained) {
        this.solarGained = solarGained;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setSlot(EnergyPrice.Slot slot) {
        this.slot = slot;
    }

    public Instant getPredictionTime() {
        return predictionTime;
    }

    public String getWeatherType() {
        return weatherType;
    }

    public float getWindGained() {
        return windGained;
    }

    public float getPrice() {
        return price;
    }

    public EnergyPrice.Slot getSlot() {
        return slot;
    }

    public float getSolarGained() {
        return solarGained;
    }

    public float getBatteryGained() {
        return batteryGained;
    }

    public void setBatteryGained(float batteryGained) {
        this.batteryGained = batteryGained;
    }

    @Override
    public String toString(){
        return "Model: "+ getPredictionTime()+", "+getPrice()+", "+getWeatherType()+", "+", "+getSolarGained()+", "+getWindGained()+", "+getBatteryGained();
    }
}
